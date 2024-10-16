package com.example.cine_connect.ui.screens.moviedetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.cine_connect.R
import com.google.android.material.button.MaterialButton

class MovieDetailsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_moviedetails, container, false)

        val buttonRating: MaterialButton = view.findViewById(R.id.button_rating)
        buttonRating.setOnClickListener {
            findNavController().navigate(R.id.action_movieDetailsFragment_to_rateFragment)
        }

        val buttonTopics: MaterialButton = view.findViewById(R.id.button_topics)
        buttonTopics.setOnClickListener {
            findNavController().navigate(R.id.action_movieDetailsFragment_to_topicsFragment)
        }


        val cardReviews: LinearLayout = view.findViewById(R.id.reviews_section)
        cardReviews.setOnClickListener {
            findNavController().navigate(R.id.action_movieDetailsFragment_to_listReviewsFragment)
            }
        return view
    }
}
