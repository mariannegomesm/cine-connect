package com.example.cine_connect.ui.screens.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cine_connect.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso

class ProfileFragment : Fragment() {

    private lateinit var nameTextField: TextView
    private lateinit var emailTextField: TextView
    private lateinit var birthdateTextField: TextView
    private lateinit var descriptionTextField: TextView
    private lateinit var profileImageView: ImageView
    private lateinit var favoritesRecyclerView: RecyclerView

    private lateinit var favoritesAdapter: FavoritesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)


        nameTextField = view.findViewById(R.id.NomeTextField)
        emailTextField = view.findViewById(R.id.EmailTextField)
        descriptionTextField = view.findViewById(R.id.userDescribeTextField)
        profileImageView = view.findViewById(R.id.profilePhoto)


        favoritesRecyclerView = view.findViewById(R.id.favoritesRecyclerView)
        favoritesRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)


        val favoriteMovies = listOf(
            "https://image.tmdb.org/t/p/w500/abcdef.jpg",
            "",
            "https://image.tmdb.org/t/p/w500/xyz.jpg"
        )

        favoritesAdapter = FavoritesAdapter(favoriteMovies)
        favoritesRecyclerView.adapter = favoritesAdapter


        loadUserData()


        val updateProfileButton = view.findViewById<Button>(R.id.update_profile)
        updateProfileButton.setOnClickListener {

            findNavController().navigate(R.id.action_profileFragment_to_editProfileFragment)
        }

        return view
    }

    private fun loadUserData() {
        val db = FirebaseFirestore.getInstance()
        val currentUser = FirebaseAuth.getInstance().currentUser

        currentUser?.let { user ->
            val userId = user.uid

            db.collection("users").document(userId).get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        val name = document.getString("nome")
                        val email = document.getString("email")
                        val description = document.getString("descrição")
                        val profileImageUrl = document.getString("profileImageUrl")

                        nameTextField.setText(name)
                        emailTextField.setText(email)
                        descriptionTextField.setText(description)

                        profileImageUrl?.let {
                            Picasso.get().load(it).into(profileImageView)
                        }
                    }
                }
                .addOnFailureListener { e -> e.printStackTrace() }
        }
    }
}
