package com.example.cine_connect.ui.screens.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.cine_connect.R
import com.example.cine_connect.repository.TmdbRepository
import kotlinx.coroutines.launch

class SearchFragment : Fragment() {

    private lateinit var searchButton: Button
    private lateinit var searchEditText: EditText
    private lateinit var searchResultsLayout: View
    private lateinit var movieBanner: ImageView
    private lateinit var movieTitle: TextView
    private lateinit var movieDescription: TextView

    private val tmdbRepository = TmdbRepository()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val binding = inflater.inflate(R.layout.fragment_search, container, false)

        searchButton = binding.findViewById(R.id.btnSearch)
        searchEditText = binding.findViewById(R.id.editTextSearch)
        searchResultsLayout = binding.findViewById(R.id.searchResultsLayout)
        movieBanner = binding.findViewById(R.id.movieBanner)
        movieTitle = binding.findViewById(R.id.movieTitle)

        searchButton.setOnClickListener {
            val query = searchEditText.text.toString()
            if (query.isNotBlank()) {
                searchMovie(query)
            } else {
                Toast.makeText(
                                requireContext(),
                                "Por favor, insira um nome de filme",
                                Toast.LENGTH_SHORT
                        )
                        .show()
            }
        }

        return binding
    }

    private fun searchMovie(query: String) {
        val apiKey = "06ebff010c5d4faf628283ca3f1ac421"
        val language = "pt-BR"

        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val movieResponse = tmdbRepository.searchMovies(apiKey, query, language)

                if (movieResponse.results.isEmpty()) {
                    Toast.makeText(requireContext(), "Nenhum filme encontrado", Toast.LENGTH_SHORT)
                            .show()
                    searchResultsLayout.visibility = View.GONE
                } else {
                    val movie = movieResponse.results.first()
                    searchResultsLayout.visibility = View.VISIBLE
                    movieTitle.text = movie.title
                    val posterUrl = "https://image.tmdb.org/t/p/w500${movie.poster_path}"
                    Glide.with(requireContext()).load(posterUrl).into(movieBanner)

                    Toast.makeText(
                                    requireContext(),
                                    "Filme encontrado: ${movie.title}",
                                    Toast.LENGTH_SHORT
                            )
                            .show()
                }
            } catch (e: Exception) {
                Toast.makeText(
                                requireContext(),
                                "Erro ao buscar dados da API: ${e.message}",
                                Toast.LENGTH_LONG
                        )
                        .show()
                searchResultsLayout.visibility = View.GONE
            }
        }
    }
}
