package com.drcmind.blogapp.data.repository

import android.net.Uri
import com.drcmind.blogapp.domain.model.Blog
import com.drcmind.blogapp.domain.model.User
import com.drcmind.blogapp.util.Result
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class BlogRepository @Inject constructor(
    private val blogsRef : CollectionReference,
    private val storageRef : StorageReference
) {
    suspend fun signOut(oneTapClient : SignInClient) = flow {
        emit(Result.Loading)
        oneTapClient.signOut().await()
        Firebase.auth.signOut()
        emit(Result.Success(true))
    }.catch { error->
        emit(Result.Error(error))
    }

    fun getSignedInUser()= flow {
        emit(Result.Loading)
        val fcu = Firebase.auth.currentUser
        val user = if(fcu!=null){
            User(fcu.uid,fcu.displayName,fcu.photoUrl.toString())
        }else null

        emit(Result.Success(user))
    }.catch { error->
        emit(Result.Error(error))
    }

    fun getBlogs()= callbackFlow {
        val snapshotListener = blogsRef.orderBy("createdDate")
            .addSnapshotListener { snapshot, error ->
                val result = if(snapshot != null){
                    Result.Success(snapshot.toObjects(Blog::class.java))
                }else{
                    Result.Error(error!!)
                }
                trySend(result)
            }
        awaitClose {
            snapshotListener.remove()
        }
    }

    fun addBlog(
        title : String,
        content : String,
        thumbnail : Uri,
        user: User
    ) = flow {
        emit(Result.Loading)
        val id = blogsRef.document().id

        val imageStorageRef = storageRef.child("images/$id.jpg")
        val downloadUrl = imageStorageRef.putFile(thumbnail).await().storage.downloadUrl.await()

        val blog = Blog(
            id = id, title = title, content = content, thumbnail = downloadUrl.toString(),
            isFavorite = false, user = user, createdDate = null
        )

        blogsRef.document(id).set(blog).await()

        emit(Result.Success(true))
    }.catch { error->
        emit(Result.Error(error))
    }

    fun updateBlog(id : String, title: String, content: String, thumbnail: Uri)= flow {
        emit(Result.Loading)

        val imageStorageRef = storageRef.child("images/$id.jpg")
        val downloadUrl = imageStorageRef.putFile(thumbnail).await().storage.downloadUrl.await()

        blogsRef.document(id).update(
            "title", title,"content", content, "thumbnail", downloadUrl.toString()
        ).await()
        emit(Result.Success(true))
    }.catch { error->
        emit(Result.Error(error))
    }

    fun deleteBlog(id: String) = flow {
        emit(Result.Loading)
        storageRef.child("images/$id.jpg").delete().await()
        blogsRef.document(id).delete().await()
        emit(Result.Success(true))
    }.catch { error->
        emit(Result.Error(error))
    }

}