package com.example.task.data.repo

import com.example.task.data.api.UsersService
import com.example.task.data.models.NetworkResult
import com.example.task.data.models.User
import javax.inject.Inject

class UsersRepositoryImpl @Inject constructor(private val usersService: UsersService): UsersRepository {

    override suspend fun getUsers(page: Int): NetworkResult<List<User>> {
        return UsersService.handleApi { usersService.getUsers(page) }
    }

    override suspend fun addUser(user: User): NetworkResult<User> {
        return UsersService.handleApi { usersService.addUser(user) }
    }

    override suspend fun deleteUser(id: Int): NetworkResult<List<User>> {
        return UsersService.handleApi { usersService.deleteUser(id) }
    }
}