package com.example.cine_connect.ui.screens.ia

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.cine_connect.databinding.FragmentIaBinding
import com.google.ai.client.generativeai.GenerativeModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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

        binding.buttonGenerate.setOnClickListener {
            lifecycleScope.launch {
                val movieTitle = binding.editTextMovieName.text.toString()
                if (movieTitle.isNotBlank()) {
                    generateDescription(movieTitle)
                } else {
                    binding.textViewRecommendations.text = "Por favor, insira o nome de um filme."
                }
            }
        }
    }

    private suspend fun generateDescription(movieTitle: String) {
        try {
            val generativeModel = GenerativeModel(
                modelName = "gemini-1.5-flash",
                apiKey = "AIzaSyBiQ-4TwXJZ2WfGzCxQPK2tsNOGAB_T9qo"
            )

            val prompt = "Fale uma curiosidade sobre o filme $movieTitle."
            val response = generativeModel.generateContent(prompt)
            val description = response?.text ?: "Curiosidade não encontrada."


            displayTextWithTypingEffect(description, binding.textViewRecommendations)

        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(requireContext(), "Erro ao gerar descrição.", Toast.LENGTH_SHORT).show()
        }
    }

    private suspend fun displayTextWithTypingEffect(text: String, textView: TextView) {
        textView.text = ""
        for (char in text) {
            textView.append(char.toString())
            delay(25)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
