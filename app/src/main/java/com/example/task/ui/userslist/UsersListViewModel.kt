package com.example.task.ui.userslist

import android.text.TextUtils
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.task.data.models.NetworkResult
import com.example.task.data.models.User
import com.example.task.data.repo.UsersRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UsersListViewModel @Inject constructor(private val usersListRepository: UsersRepository) : ViewModel() {

    companion object {
        private const val CREATED_RESPONSE_CODE = 201
        private const val NO_CONTENT_RESPONSE_CODE = 204

        private const val MINIMUM_NAME_LENGTH = 3
    }

    private val _usersList: MutableLiveData<List<User>> = MutableLiveData()
    val usersList: LiveData<List<User>>
        get() = _usersList

    private val _message: MutableLiveData<String> = MutableLiveData()
    val message: LiveData<String>
        get() = _message

    private val _loading: MutableLiveData<Boolean> = MutableLiveData(false)
    val loading: LiveData<Boolean>
        get() = _loading


    fun fetchUsers() {
        _loading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            val response = usersListRepository.getUsers()
            _loading.postValue(false)
            when (response) {
                is NetworkResult.ApiSuccess<List<User>> -> {
                    response.data?.let {
                        _usersList.postValue(it)
                    }
                }
                is NetworkResult.ApiError<List<User>> -> {
                    _message.postValue("error loading users: " + response.message)
                }
                is NetworkResult.ApiException<List<User>> -> {
                    _message.postValue("exception loading users: " + response.e.message)
                }
            }
        }
    }

    fun deleteUser(user: User) {
        _loading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            user.id?.let {
                val response = usersListRepository.deleteUser(it)
                _loading.postValue(false)

                when (response) {
                    is NetworkResult.ApiSuccess<List<User>> -> {
                        if (response.code == NO_CONTENT_RESPONSE_CODE) {
                            (_usersList.value as ArrayList).remove(user)
                            _usersList.postValue(_usersList.value)
                            showStatusMessage("User deleted successfully")
                        } else {
                            showStatusMessage("error deleting user")
                        }
                    }
                    is NetworkResult.ApiException<List<User>> -> {
                        showStatusMessage("exception deleting user: " + response.e.message)
                    }
                    else -> {
                        showStatusMessage("error deleting user")
                    }
                }
            }

        }
    }

    fun addUser(name: String?, email: String?) {
        if (name == null || email == null) {
            return
        }
        val user = User(name = name, email = email)
        if (validateUser(user)) {
            _loading.value = true
            viewModelScope.launch(Dispatchers.IO) {
                val response = usersListRepository.addUser(user)
                _loading.postValue(false)

                when (response) {
                    is NetworkResult.ApiSuccess<User>
                    -> {
                        if (response.code == CREATED_RESPONSE_CODE) {
                            (_usersList.value as ArrayList).add(0, user)
                            _usersList.postValue(_usersList.value)
                            showStatusMessage("User added successfully")
                        } else {
                            showStatusMessage("error adding user")
                        }
                    }
                    is NetworkResult.ApiException<User> -> {
                        showStatusMessage("exception adding user: " + response.e.message)
                    }
                    else -> {
                        showStatusMessage("error adding user")
                    }
                }
            }
        } else {
            showStatusMessage("name or email invalid")
        }
    }

    private fun validateUser(user: User): Boolean {
        if (user.name.length < MINIMUM_NAME_LENGTH) return false
        if (TextUtils.isEmpty(user.email) || !Patterns.EMAIL_ADDRESS.matcher(user.email).matches()) return false
        return true
    }

    private fun showStatusMessage(message: String) {
        _message.postValue(message)
    }

}