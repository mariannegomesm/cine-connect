package com.example.cine_connect.ui.screens.topics

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.cine_connect.R
import com.google.android.material.button.MaterialButton


class TopicsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_topics, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val buttonTopics: MaterialButton = view.findViewById(R.id.add_topics)
        buttonTopics.setOnClickListener{
            findNavController().navigate(R.id.action_topicsFragment_to_topicCreateFragment)
        }
    }
}
