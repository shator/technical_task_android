package com.example.task.di

import com.example.task.data.api.UsersService
import com.example.task.data.repo.UsersRepositoryImpl
import com.example.task.data.repo.UsersRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {

    @Singleton
    @Provides
    fun provideUsersService(): UsersService {
        return UsersService.create()
    }
}