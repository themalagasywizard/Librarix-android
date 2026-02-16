package com.librarix.data.remote

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Stub implementation - replace with real Supabase integration
 * when credentials are configured.
 */
@Singleton
class SupabaseAuthManager @Inject constructor() {

    suspend fun signUp(email: String, password: String): Result<User> {
        return Result.failure(NotImplementedError("Supabase not configured"))
    }

    suspend fun signIn(email: String, password: String): Result<User> {
        return Result.failure(NotImplementedError("Supabase not configured"))
    }

    suspend fun signInWithGoogle(idToken: String): Result<User> {
        return Result.failure(NotImplementedError("Supabase not configured"))
    }

    suspend fun signOut() {}

    fun getCurrentUser(): User? = null

    fun observeAuthState(): Flow<AuthState> = flowOf(AuthState.SIGNED_OUT)
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
