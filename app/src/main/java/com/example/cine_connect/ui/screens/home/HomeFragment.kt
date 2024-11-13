package com.example.cine_connect.ui.screens.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.cine_connect.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeFragment : Fragment() {

    private val movieImages = listOf(
        "https://img.elo7.com.br/product/main/2A1A4B7/big-poster-filme-joker-coringa-joaquin-phoenix-tam-90x60-cm-nerd.jpg",
        "https://acdn.mitiendanube.com/stores/004/687/740/products/pos-03566-553a8f3c507fb7865617181190103917-480-0.jpg",
        "https://ingresso-a.akamaihd.net/prd/img/movie/divertida-mente-2/61ac248d-e3e6-4e33-9515-8ce0621a32fa.webp"
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        loadImages(view)

        view.findViewById<ImageView>(R.id.div1).setOnClickListener {
            navigateToDetails(movieImages[0])
        }

        view.findViewById<ImageView>(R.id.div2).setOnClickListener {
            navigateToDetails(movieImages[1])
        }

        view.findViewById<ImageView>(R.id.div3).setOnClickListener {
            navigateToDetails(movieImages[2])
        }

        return view
    }

    private fun loadImages(view: View) {
        Glide.with(this).load(movieImages[0]).into(view.findViewById(R.id.div1))
        Glide.with(this).load(movieImages[1]).into(view.findViewById(R.id.div2))
        Glide.with(this).load(movieImages[2]).into(view.findViewById(R.id.div3))
    }

    private fun navigateToDetails(imageUrl: String) {
        if (imageUrl.isNotEmpty()) {
            val bundle = Bundle().apply {
                putString("imageUrl", imageUrl)
            }
            findNavController().navigate(R.id.action_homeFragment_to_movieDetailsFragment, bundle)
        } else {
            Log.e("HomeFragment", "imageUrl is empty")
        }
    }

    override fun onResume() {
        super.onResume()
        activity?.findViewById<BottomNavigationView>(R.id.nav_view)?.visibility = View.VISIBLE
    }

    override fun onPause() {
        super.onPause()
        activity?.findViewById<BottomNavigationView>(R.id.nav_view)?.visibility = View.GONE
    }
}
