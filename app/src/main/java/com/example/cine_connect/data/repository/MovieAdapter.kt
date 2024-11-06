package com.example.cine_connect.ui.screens.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cine_connect.databinding.ItemMovieBinding

class MovieAdapter(private val movies: List<String>, private val onItemClick: (String) -> Unit) :
        RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movieImage = movies[position]
        Glide.with(holder.itemView).load(movieImage).into(holder.binding.movieImage)
        holder.itemView.setOnClickListener { onItemClick(movieImage) }
    }

    override fun getItemCount(): Int = movies.size

    class MovieViewHolder(val binding: ItemMovieBinding) : RecyclerView.ViewHolder(binding.root)
}
