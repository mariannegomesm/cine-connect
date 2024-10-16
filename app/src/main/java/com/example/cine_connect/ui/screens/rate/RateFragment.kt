package com.example.cine_connect.ui.screens.rate

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.cine_connect.R
import com.google.android.material.button.MaterialButton

class RateFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_rate, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val buttonRating: MaterialButton = view.findViewById(R.id.button_rating)
        buttonRating.setOnClickListener {
            findNavController().navigate(R.id.action_rateFragment_to_homeFragment)
        }
    }


}