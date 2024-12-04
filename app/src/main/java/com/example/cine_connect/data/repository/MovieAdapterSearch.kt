package com.example.cine_connect.data.repository
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

import com.example.cine_connect.databinding.SearchMovieBinding
import com.example.cine_connect.models.Movie

class MovieAdapterSearch(
    private val movies: List<com.example.cine_connect.network.model.Movie>,
    private val onItemClick: (Movie) -> Unit
) : RecyclerView.Adapter<MovieAdapterSearch.MovieViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = SearchMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = movies[position]

        Glide.with(holder.binding.root.context)
            .load("https://image.tmdb.org/t/p/w500${movie.poster_path}")
            .into(holder.binding.movieImage)

        holder.binding.movieTitle.text = movie.title
        holder.binding.movieDescription.text = movie.overview

        holder.binding.root.setOnClickListener {  }
    }

    override fun getItemCount(): Int = movies.size

    class MovieViewHolder(val binding: SearchMovieBinding) : RecyclerView.ViewHolder(binding.root)
}
