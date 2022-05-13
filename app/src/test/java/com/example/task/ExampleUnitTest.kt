package com.example.task

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.task.data.models.NetworkResult
import com.example.task.data.models.User
import com.example.task.data.repo.UsersRepository
import com.example.task.ui.userslist.UsersListViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class ExampleUnitTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    private val testDispatcher = TestCoroutineDispatcher()

    @Before
    fun setup() {
        // 1
        Dispatchers.setMain(testDispatcher)
    }

    @Test
    fun testGetUsers() {
        val mockedRepo = Mockito.mock(UsersRepository::class.java)
        val viewModel = UsersListViewModel(mockedRepo)
        Mockito.`when`(runBlocking {mockedRepo.getUsers()}).thenReturn(getMockedUsersListResponse())
        viewModel.fetchUsers()
        assertEquals(getMockedUsersListResponse().data, LiveDataTestHelper.getValue(viewModel.usersList))
    }

    private fun getMockedUsersListResponse(): NetworkResult.ApiSuccess<List<User>> {
        val user1 = User(id = 1, name = "John", email = "john@doe.com")
        val user2 = User(id = 1, name = "Johanna", email = "johanna@doe.com")
        val usersList = listOf(user1, user2)
        return NetworkResult.ApiSuccess(usersList, 400)
    }
}