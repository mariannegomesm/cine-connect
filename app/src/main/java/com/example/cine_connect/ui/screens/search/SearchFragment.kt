package com.example.cine_connect.ui.screens.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cine_connect.R
import com.example.cine_connect.data.repository.MovieAdapterSearch
import com.example.cine_connect.repository.TmdbRepository
import kotlinx.coroutines.launch

class SearchFragment : Fragment() {

    private lateinit var searchButton: ImageView
    private lateinit var searchEditText: EditText
    private lateinit var recyclerView: RecyclerView

    private val tmdbRepository = TmdbRepository()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = inflater.inflate(R.layout.fragment_search, container, false)

        searchButton = binding.findViewById(R.id.iconSearch)
        searchEditText = binding.findViewById(R.id.editTextSearch)
        recyclerView = binding.findViewById(R.id.recyclerViewSearch)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)

        searchButton.setOnClickListener {
            val query = searchEditText.text.toString()
            if (query.isNotBlank()) {
                searchMovie(query)
            } else {
                Toast.makeText(
                    requireContext(),
                    "Por favor, insira um nome de filme",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        return binding
    }

    private fun searchMovie(query: String) {
        val apiKey = "06ebff010c5d4faf628283ca3f1ac421"
        val language = "pt-BR"

        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val searchResponse = tmdbRepository.searchMovies(apiKey, query, language)

                if (searchResponse.results.isEmpty()) {
                    Toast.makeText(requireContext(), "Nenhum filme encontrado", Toast.LENGTH_SHORT).show()
                    recyclerView.visibility = View.GONE
                } else {
                    recyclerView.visibility = View.VISIBLE

                    // Cria o adaptador com a lista de filmes
                    val adapter = MovieAdapterSearch(searchResponse.results) { movie ->
                        Toast.makeText(requireContext(), "Clicou em: ${movie.movieTitle}", Toast.LENGTH_SHORT).show()
                    }
                    recyclerView.adapter = adapter
                }
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Erro ao buscar dados da API: ${e.message}", Toast.LENGTH_LONG).show()
                recyclerView.visibility = View.GONE
            }
        }
    }
}
