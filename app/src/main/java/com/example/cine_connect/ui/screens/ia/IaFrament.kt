package com.example.cine_connect.ui.screens.ia

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.cine_connect.R
import com.example.cine_connect.databinding.FragmentIaBinding

class IAFragment : Fragment() {

    private var _binding: FragmentIaBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentIaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Configurando o botão de ação
        binding.buttonAction.setOnClickListener {
            // Ação ao clicar no botão, por exemplo, navegar para outro fragmento
            findNavController().navigate(R.id.action_iaFragment_to_homeFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Libera o binding
    }
}
