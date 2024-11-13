package com.example.cine_connect.ui.screens.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.cine_connect.R
import com.example.cine_connect.auth.AuthHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast

class LoginFragment : Fragment() {

    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_login, container, false)

        emailEditText = view.findViewById(R.id.emailEditText)
        passwordEditText = view.findViewById(R.id.passwordEditText)

        val cadastroTextfield: TextView = view.findViewById(R.id.cadastrolink)
        cadastroTextfield.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_cadastroFragment4)
        }

        val button: Button = view.findViewById(R.id.loginbutton)
        button.setOnClickListener {
            login()
        }

        return view
    }

    private fun login() {
        val email = emailEditText.text.toString()
        val password = passwordEditText.text.toString()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(context, "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show()
            return
        }

        CoroutineScope(Dispatchers.Main).launch {
            val uid = withContext(Dispatchers.IO) {
                AuthHelper.loginUser(email, password)
            }

            if (uid != null) {

                findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
            } else {
                Toast.makeText(context, "Credenciais inválidas. Tente novamente.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        hideBottomNavigation()
        hideToolbar()
    }

    override fun onPause() {
        super.onPause()
        showBottomNavigation()
        showToolbar()
    }

    private fun hideBottomNavigation() {
        activity?.findViewById<BottomNavigationView>(R.id.nav_view)?.visibility = View.GONE
    }

    private fun showBottomNavigation() {
        activity?.findViewById<BottomNavigationView>(R.id.nav_view)?.visibility = View.VISIBLE
    }

    private fun hideToolbar() {
        (activity as? AppCompatActivity)?.supportActionBar?.hide()
    }

    private fun showToolbar() {
        (activity as? AppCompatActivity)?.supportActionBar?.show()
    }
}
