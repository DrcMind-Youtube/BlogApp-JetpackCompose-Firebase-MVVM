package com.drcmind.blogapp.di

import android.content.Context
import com.drcmind.blogapp.util.GoogleAuthUiHelper
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun providesSignedClient(@ApplicationContext context: Context): SignInClient{
        return Identity.getSignInClient(context)
    }

    @Provides
    fun providesGoogleAuthSignInClient(
        signInClient: SignInClient,
        @ApplicationContext context: Context
    ) : GoogleAuthUiHelper{
        return GoogleAuthUiHelper(context, signInClient)
    }

    @Provides
    fun providesBlogsRef() = Firebase.firestore.collection("Blogs")

    @Provides
    fun providesStorageRef() = Firebase.storage.reference

}