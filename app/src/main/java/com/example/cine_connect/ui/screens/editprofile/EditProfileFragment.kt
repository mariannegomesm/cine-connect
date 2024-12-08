package com.example.cine_connect.ui.screens.editprofile

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.example.cine_connect.R
import com.example.cine_connect.databinding.FragmentEditProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso

class EditProfileFragment : Fragment() {

    private lateinit var binding: FragmentEditProfileBinding
    private var profileImageUri: Uri? = null

    private val imagePickerLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val imageUri = result.data?.data
                imageUri?.let {
                    binding.editProfilePhoto.setImageURI(it)
                    profileImageUri = it
                }
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadUserData()

        binding.updateProfileButton.setOnClickListener {
            updateUserProfile()
        }

        // Configuração do clique na ImageView para trocar a imagem
        binding.changeImageButton.setOnClickListener {
            pickImageFromGallery()
        }
    }

    private fun updateUserProfile() {
        val db = FirebaseFirestore.getInstance()
        val currentUser = FirebaseAuth.getInstance().currentUser

        currentUser?.let { user ->
            val userId = user.uid

            val updatedName = binding.inputNome.text.toString()
            val updatedEmail = binding.inputEmail.text.toString()
            val updatedBirthdate = binding.inputBirthdate.text.toString()
            val updatedDescription = binding.descriptionEditText.text.toString()

            val updatedData = mutableMapOf<String, Any>(
                "nome" to updatedName,
                "email" to updatedEmail,
                "dataNascimento" to updatedBirthdate,
                "descrição" to updatedDescription
            )

            profileImageUri?.let {
                updatedData["profileImageUrl"] = it.toString()
            }

            db.collection("users").document(userId).update(updatedData)
                .addOnSuccessListener {
                    Toast.makeText(
                        requireContext(),
                        "Perfil atualizado com sucesso!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                .addOnFailureListener { e ->
                    e.printStackTrace()
                    Toast.makeText(
                        requireContext(),
                        "Falha ao atualizar o perfil",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.e("EditProfileFragment", "Error updating profile", e)
                }
        }
    }

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK).apply {
            type = "image/*"
        }
        imagePickerLauncher.launch(intent)
    }

    private fun loadUserData() {
        val db = FirebaseFirestore.getInstance()
        val currentUser = FirebaseAuth.getInstance().currentUser

        currentUser?.let { user ->
            val userId = user.uid

            db.collection("users").document(userId).get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        Log.d("EditProfileFragment", "Document data: ${document.data}")
                        val name = document.getString("nome")
                        val email = document.getString("email")
                        val birthdate = document.getString("dataNascimento")
                        val description = document.getString("descrição")
                        val profileImageUrl = document.getString("profileImageUrl")

                        Log.d("EditProfileFragment", "Name: $name, Email: $email, Birthdate: $birthdate, Description: $description, ProfileImageUrl: $profileImageUrl")

                        binding.inputNome.setText(name)
                        binding.inputEmail.setText(email)
                        binding.inputBirthdate.setText(birthdate)
                        binding.descriptionEditText.setText(description)

                        profileImageUrl?.let {
                            Picasso.get().load(it).into(binding.editProfilePhoto)
                        }
                    } else {
                        Log.d("EditProfileFragment", "Document does not exist")
                    }
                }
                .addOnFailureListener { e ->
                    e.printStackTrace()
                    Log.e("EditProfileFragment", "Failed to load user data", e)
                }
        } ?: run {
            Log.d("EditProfileFragment", "No current user found")
        }
    }
}
