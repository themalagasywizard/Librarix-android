package com.librarix.data.remote

import com.librarix.domain.model.UserProfile
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SupabaseAuthManager @Inject constructor() {

    companion object {
        private const val SUPABASE_URL = "https://your-project.supabase.co"
        private const val SUPABASE_KEY = "your-anon-key"
    }

    private val client = createSupabaseClient(
        supabaseUrl = SUPABASE_URL,
        supabaseKey = SUPABASE_KEY
    ) {
        auth {
            autoRefresh = true
        }
        postgrest {
            // Ignore unknown columns
        }
    }

    val auth: Auth get() = client.auth

    suspend fun signUp(email: String, password: String): Result<User> {
        return try {
            val result = client.auth.signUpWith(email, password)
            Result.success(result.user!!)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun signIn(email: String, password: String): Result<User> {
        return try {
            val result = client.auth.signInWith(email, password)
            Result.success(result.user!!)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun signInWithGoogle(idToken: String): Result<User> {
        return try {
            val result = client.auth.signInWith(OAuthProvider.GOOGLE, idToken = idToken)
            Result.success(result.user!!)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun signOut() {
        client.auth.signOut()
    }

    fun getCurrentUser(): User? {
        return client.auth.currentUserOrNull()
    }

    fun observeAuthState(): Flow<AuthState> = callbackFlow {
        val authStateListener: (AuthStateChangeEvent) -> Unit = { event ->
            trySend(event.type)
        }
        client.auth.onAuthStateChange(authStateListener)
        awaitClose {
            client.auth.removeAuthStateListener(authStateListener)
        }
    }
}

enum class AuthState {
    SIGNED_IN,
    SIGNED_OUT,
    USER_UPDATED
}

data class User(
    val id: String,
    val email: String?,
    val isAnonymous: Boolean = false
)
