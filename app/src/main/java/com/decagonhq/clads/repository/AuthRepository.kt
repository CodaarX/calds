package com.decagonhq.clads.repository

import com.decagonhq.clads.data.domain.login.EmailLoginSuccessResponse
import com.decagonhq.clads.data.domain.login.LoginCredentials
import com.decagonhq.clads.data.domain.registration.UserRegSuccessResponse
import com.decagonhq.clads.data.domain.registration.UserRegistration
import com.decagonhq.clads.util.Resource
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun registerUser(user: UserRegistration): Flow<Resource<UserRegSuccessResponse>>
    suspend fun loginUser(loginCredentials: LoginCredentials): Flow<Resource<EmailLoginSuccessResponse>>

}
