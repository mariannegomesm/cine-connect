package com.example.cineconnection.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.cineconnection.R
import com.example.cineconnection.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.divVerde.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_movieDetailsFragment)
        }

        binding.divAmarela.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_movieDetailsFragment)
        }

        binding.divVermelha.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_movieDetailsFragment)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
