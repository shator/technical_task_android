package com.example.task.ui.userslist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.task.R
import com.example.task.data.models.User
import com.example.task.databinding.FragmentUsersListBinding
import com.example.task.ui.newuser.NewUserFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UsersListFragment : Fragment(R.layout.fragment_users_list) {

    companion object {
        fun newInstance(): UsersListFragment {
            return UsersListFragment()
        }
    }

    private lateinit var adapter: UsersListAdapter
    private var _binding: FragmentUsersListBinding? = null

    private val viewModel: UsersListViewModel by viewModels()
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUsersListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonAddUser.setOnClickListener {
            NewUserFragment(::addUserCallback).show(childFragmentManager, "TAG")
        }
        setupRecyclerView()
        observeViewModel()
    }

    override fun onResume() {
        super.onResume()
        if (viewModel.usersList.value?.isEmpty() != false) {
            viewModel.fetchUsers()
        }
    }

    private fun setupRecyclerView() {
        adapter = UsersListAdapter(::onUserLongClicked)
        binding.recyclerViewUsers.adapter = adapter
        binding.recyclerViewUsers.layoutManager = LinearLayoutManager(context)
    }

    private fun observeViewModel() {
        with(viewModel) {
            usersList.observe(viewLifecycleOwner, {
                adapter.setUsers(it)
            })
            message.observe(viewLifecycleOwner, {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            })
            loading.observe(viewLifecycleOwner, { loading ->
                binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
            })
        }
    }

    private fun addUserCallback(name: String, email: String) {
        viewModel.addUser(name, email)
    }

    private fun onUserLongClicked(user: User) {
        viewModel.deleteUser(user)
    }
}