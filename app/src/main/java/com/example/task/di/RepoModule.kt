package com.example.task.di

import com.example.task.data.repo.UsersRepository
import com.example.task.data.repo.UsersRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
abstract class RepoModule {

    @Binds
    abstract fun provideUsersRepository(repoImpl: UsersRepositoryImpl): UsersRepository
}