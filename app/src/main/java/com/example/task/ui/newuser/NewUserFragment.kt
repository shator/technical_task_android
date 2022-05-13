package com.example.task.ui.newuser

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.DialogFragment
import com.example.task.R
import com.example.task.databinding.DialogNewUserBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewUserFragment(private val addUserCallback: (String, String) -> Unit) : DialogFragment(R.layout.dialog_new_user) {

    private var _binding: DialogNewUserBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogNewUserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonConfirmAddUser.setOnClickListener { addUser() }
    }

    override fun onResume() {
        super.onResume()
        val params: ViewGroup.LayoutParams = dialog!!.window!!.attributes
        params.width = ConstraintLayout.LayoutParams.MATCH_PARENT
        params.height = ConstraintLayout.LayoutParams.WRAP_CONTENT
        dialog!!.window!!.attributes = params as WindowManager.LayoutParams
    }

    private fun addUser() {
        val name = binding.textInputLayoutName.editText?.text.toString()
        val email = binding.textInputLayoutEmail.editText?.text.toString()
        addUserCallback(name, email)
        dismiss()
    }
}