package com.example.cine_connect.ui.screens.home

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.cine_connect.R
import com.example.cine_connect.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val homeViewModel: HomeViewModel by viewModels()

    private val movieImagesNowPlaying = mutableListOf<String>()
    private val movieImagesPopular = mutableListOf<String>()
    private lateinit var movieAdapterNowPlaying: MovieAdapter
    private lateinit var movieAdapterPopular: MovieAdapter

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        val apiKey = "06ebff010c5d4faf628283ca3f1ac421" 
        homeViewModel.fetchNowPlayingMovies(apiKey)
        homeViewModel.fetchPopularMovies(apiKey)

        //  RecyclerView para filmes em cartaz
        movieAdapterNowPlaying = MovieAdapter(movieImagesNowPlaying) { imageUrl -> navigateToDetails(imageUrl) }
        binding.filmesRecyclerView.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.filmesRecyclerView.adapter = movieAdapterNowPlaying

        // RecyclerView para filmes populares
        movieAdapterPopular = MovieAdapter(movieImagesPopular) { imageUrl -> navigateToDetails(imageUrl) }
        binding.filmesPopularesRecyclerView.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.filmesPopularesRecyclerView.adapter = movieAdapterPopular

        // Observar os filmes em cartaz
        homeViewModel.nowPlayingMovies.observe(viewLifecycleOwner, Observer { movies ->
            if (movies.isNotEmpty()) {
                movieImagesNowPlaying.clear()
                movieImagesNowPlaying.addAll(
                        movies.map { "https://image.tmdb.org/t/p/w500${it.poster_path}" }
                )
                movieAdapterNowPlaying.notifyDataSetChanged()
            }
        })

        // Observar os filmes populares
        homeViewModel.popularMovies.observe(viewLifecycleOwner, Observer { movies ->
            if (movies.isNotEmpty()) {
                movieImagesPopular.clear()
                movieImagesPopular.addAll(
                        movies.map { "https://image.tmdb.org/t/p/w500${it.poster_path}" }
                )
                movieAdapterPopular.notifyDataSetChanged()
            }
        })

        return binding.root
    }

    private fun navigateToDetails(imageUrl: String) {
        val bundle = Bundle().apply { putString("imageUrl", imageUrl) }
        findNavController().navigate(R.id.action_homeFragment_to_movieDetailsFragment, bundle)
    }
}
