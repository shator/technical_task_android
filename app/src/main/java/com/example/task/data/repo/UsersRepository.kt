package com.example.task.data.repo

import com.example.task.data.models.NetworkResult
import com.example.task.data.models.User

interface UsersRepository {

    suspend fun getUsers(page: Int = 1): NetworkResult<List<User>>

    suspend fun addUser(user: User): NetworkResult<User>

    suspend fun deleteUser(id: Int): NetworkResult<List<User>>

}