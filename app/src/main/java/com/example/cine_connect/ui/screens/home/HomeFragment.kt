package com.example.cine_connect.ui.screens.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.cine_connect.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        view.findViewById<View>(R.id.div1).setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_movieDetailsFragment)
        }

        view.findViewById<View>(R.id.div2).setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_movieDetailsFragment)
        }

        view.findViewById<View>(R.id.div3).setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_movieDetailsFragment)
        }

        view.findViewById<View>(R.id.div4).setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_movieDetailsFragment)
        }

        view.findViewById<View>(R.id.div5).setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_movieDetailsFragment)
        }

        view.findViewById<View>(R.id.div6).setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_movieDetailsFragment)
        }

        return view
    }

    override fun onResume() {
        super.onResume()
        activity?.findViewById<BottomNavigationView>(R.id.nav_view)?.visibility = View.VISIBLE
    }

    override fun onPause() {
        super.onPause()
        activity?.findViewById<BottomNavigationView>(R.id.nav_view)?.visibility = View.GONE
    }
}
