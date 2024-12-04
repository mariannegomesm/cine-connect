package com.example.cine_connect.ui.screens.home

import androidx.recyclerview.widget.LinearLayoutManager
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
import com.example.cine_connect.models.Movie

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val homeViewModel: HomeViewModel by viewModels()

    private val movieNowPlaying = mutableListOf<Movie>()
    private val moviePopular = mutableListOf<Movie>()
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


        movieAdapterNowPlaying = MovieAdapter(movieNowPlaying) { movie -> navigateToDetails(movie) }
        binding.filmesRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.filmesRecyclerView.adapter = movieAdapterNowPlaying


        movieAdapterPopular = MovieAdapter(moviePopular) { movie -> navigateToDetails(movie) }
        binding.filmesPopularesRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.filmesPopularesRecyclerView.adapter = movieAdapterPopular


        homeViewModel.nowPlayingMovies.observe(viewLifecycleOwner, Observer { movies ->
            if (movies.isNotEmpty()) {
                movieNowPlaying.clear()
                movieNowPlaying.addAll(
                    movies.map { Movie(it.id, "https://image.tmdb.org/t/p/w500${it.poster_path}", it.title,it.overview) }
                )
                movieAdapterNowPlaying.notifyDataSetChanged()
            }
        })


        homeViewModel.popularMovies.observe(viewLifecycleOwner, Observer { movies ->
            if (movies.isNotEmpty()) {
                moviePopular.clear()
                moviePopular.addAll(
                    movies.map { Movie(it.id, "https://image.tmdb.org/t/p/w500${it.poster_path}",it.title,it.overview) }
                )
                movieAdapterPopular.notifyDataSetChanged()
            }
        })

        return binding.root
    }

    private fun navigateToDetails(movie: Movie) {
        val bundle = Bundle().apply {
            putString("movieTitle",movie.movieTitle)
            putInt("movieId", movie.id)
            putString("imageUrl", movie.posterPath)
        }
        findNavController().navigate(R.id.action_homeFragment_to_movieDetailsFragment, bundle)
    }
}