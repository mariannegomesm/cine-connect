package com.example.cineconnection.ui.cadastro

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.cine_connect.R
import com.google.android.material.bottomnavigation.BottomNavigationView

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
