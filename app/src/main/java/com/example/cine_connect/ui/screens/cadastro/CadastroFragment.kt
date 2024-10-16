package com.example.cine_connect.ui.cadastro

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
import androidx.appcompat.app.AppCompatActivity

class CadastroFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_cadastro, container, false)

        val cadastrarButton: Button = view.findViewById(R.id.cadastrarButton)
        cadastrarButton.setOnClickListener {
            findNavController().navigate(R.id.action_cadastroFragment4_to_loginFragment)
        }

        val cadastrotologintext: TextView = view.findViewById(R.id.Cadastrotologintext)
        cadastrotologintext.setOnClickListener {
            findNavController().navigate(R.id.action_cadastroFragment4_to_loginFragment)
        }

        return view
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