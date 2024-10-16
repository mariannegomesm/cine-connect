package com.example.cine_connect.ui.screens.topiccreate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.cine_connect.R
import com.google.android.material.button.MaterialButton

class TopicCreateFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_topic_create, container, false)

         val postButton = view.findViewById<MaterialButton>(R.id.button_topic)
        postButton.setOnClickListener {
            findNavController().navigate(R.id.action_topicCreateFragment_to_homeFragment)
        }

        return view
    }
}
