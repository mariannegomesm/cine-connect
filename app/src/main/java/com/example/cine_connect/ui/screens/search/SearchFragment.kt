package com.example.cine_connect.ui.screens.search

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cine_connect.R
import com.example.cine_connect.data.repository.MovieAdapterSearch
import com.example.cine_connect.repository.TmdbRepository
import kotlinx.coroutines.launch
import android.view.animation.AlphaAnimation
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.example.cine_connect.models.Movie

class SearchFragment : Fragment() {

    private lateinit var searchButton: ImageView
    private lateinit var searchEditText: EditText
    private lateinit var recyclerView: RecyclerView
    private lateinit var alertImage: ImageView
    private lateinit var alertText: TextView

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
        alertImage = binding.findViewById(R.id.Imagealert)
        alertText = binding.findViewById(R.id.textalert)

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
                    recyclerView.visibility = View.GONE
                    alertImage.visibility = View.VISIBLE
                    alertText.visibility = View.VISIBLE
                } else {
                    recyclerView.visibility = View.VISIBLE
                    alertImage.visibility = View.GONE
                    alertText.visibility = View.GONE

                    val adapter = MovieAdapterSearch(searchResponse.results) { movie ->
                        navigateToDetails(movie)
                    }
                    recyclerView.adapter = adapter

                    val animation = AlphaAnimation(0.0f, 1.0f)
                    animation.duration = 500
                    recyclerView.startAnimation(animation)
                }
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Erro ao buscar dados da API: ${e.message}", Toast.LENGTH_LONG).show()
                recyclerView.visibility = View.GONE
                alertImage.visibility = View.VISIBLE
                alertText.visibility = View.VISIBLE
            }
        }
    }

    private fun navigateToDetails(movie: Movie) {
        Log.d("SearchFragment", "Navigating to MovieDetailsFragment with movie: ${movie.movieTitle}")
        val bundle = Bundle().apply {
            putString("movieTitle", movie.movieTitle)
            putInt("movieId", movie.id)
            putString("imageUrl", movie.posterPath)
        }
        findNavController().navigate(R.id.action_searchFragment_to_moviedetailsFragment, bundle)
    }
}
