package com.example.cine_connect.ui.screens.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.cine_connect.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class LoginFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_login, container, false)

        val cadastroTextfield: TextView = view.findViewById(R.id.cadastrolink)
        cadastroTextfield.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_cadastroFragment4)
        }

        val button: Button = view.findViewById(R.id.loginbutton)
        button.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
        }

        return view
    }

    override fun onResume() {
        super.onResume()
        activity?.findViewById<BottomNavigationView>(R.id.nav_view)?.visibility = View.GONE
    }

    override fun onPause() {
        super.onPause()
        activity?.findViewById<BottomNavigationView>(R.id.nav_view)?.visibility = View.VISIBLE
    }
}
