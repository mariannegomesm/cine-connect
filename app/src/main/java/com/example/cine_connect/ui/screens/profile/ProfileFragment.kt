package com.example.cine_connect.ui.screens.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cine_connect.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso

class ProfileFragment : Fragment() {

    private lateinit var nameTextField: EditText
    private lateinit var emailTextField: EditText
    private lateinit var birthdateTextField: EditText
    private lateinit var descriptionTextField: EditText
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
        birthdateTextField = view.findViewById(R.id.BirthdateTextField)
        descriptionTextField = view.findViewById(R.id.userDescribeTextField)
        profileImageView = view.findViewById(R.id.imageView2)


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

            // Buscar dados do usuário no Firestore
            db.collection("users").document(userId).get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        val name = document.getString("nome")
                        val email = document.getString("email")
                        val birthdate = document.getString("dataNascimento")
                        val description = document.getString("descrição")
                        val profileImageUrl = document.getString("profileImageUrl")

                        // Preencher os campos de perfil
                        nameTextField.setText(name)
                        emailTextField.setText(email)
                        birthdateTextField.setText(birthdate)
                        descriptionTextField.setText(description)

                        // Carregar a imagem de perfil usando Picasso
                        profileImageUrl?.let {
                            Picasso.get().load(it).into(profileImageView)
                        }
                    }
                }
                .addOnFailureListener { e -> e.printStackTrace() }
        }
    }
}
