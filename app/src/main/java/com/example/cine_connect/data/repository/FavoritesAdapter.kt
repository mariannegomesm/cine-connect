package com.example.cine_connect.ui.screens.profile

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cine_connect.R
import com.squareup.picasso.Picasso

class FavoritesAdapter(private val favoriteMovies: List<String>) : RecyclerView.Adapter<FavoritesAdapter.FavoriteViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_movie, parent, false)
        return FavoriteViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        val movie = favoriteMovies[position]
        if (movie.isEmpty()) {
        } else {
            Picasso.get().load(movie).into(holder.imageView)
        }
    }

    override fun getItemCount(): Int {
        return favoriteMovies.size
    }

    class FavoriteViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.movieImage)
    }
}
