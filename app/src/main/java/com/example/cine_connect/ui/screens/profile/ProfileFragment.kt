package com.example.cine_connect.ui.screens.profile

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.cine_connect.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ProfileFragment : Fragment() {

    private lateinit var imageView: ImageView
    private lateinit var fabEdit: FloatingActionButton
    private val storage = FirebaseStorage.getInstance()
    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val PICK_IMAGE_REQUEST = 71

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        imageView = view.findViewById(R.id.imageView2)
        fabEdit = view.findViewById(R.id.fab_edit_profile)

        loadProfileImage()

        fabEdit.setOnClickListener {
            openImageChooser()
        }

        return view
    }

    private fun loadProfileImage() {
        val user = auth.currentUser
        user?.let {
            val photoUrl = user.photoUrl
            if (photoUrl != null) {
                Glide.with(this)
                    .load(photoUrl)
                    .into(imageView)
            }
        }
    }

    private fun openImageChooser() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && data != null) {
            val imageUri: Uri = data.data!!
            uploadImageToFirebase(imageUri)
        }
    }

    private fun uploadImageToFirebase(imageUri: Uri) {
        val user = auth.currentUser
        val storageReference: StorageReference = storage.reference.child("profile_pictures/${user?.uid}")

        val uploadTask = storageReference.putFile(imageUri)

        uploadTask.addOnSuccessListener {
            storageReference.downloadUrl.addOnSuccessListener { uri ->
                updateProfilePicture(uri.toString())
                Toast.makeText(requireContext(), "Foto de perfil atualizada com sucesso!", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener { e ->
            Toast.makeText(requireContext(), "Falha ao atualizar foto: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateProfilePicture(photoUrl: String) {
        val user = auth.currentUser
        val profileUpdates = UserProfileChangeRequest.Builder()
            .setPhotoUri(Uri.parse(photoUrl))
            .build()

        user?.updateProfile(profileUpdates)
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    updateUserProfileInFirestore(photoUrl)
                } else {
                    Toast.makeText(requireContext(), "Erro ao atualizar perfil", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun updateUserProfileInFirestore(photoUrl: String) {
        val user = auth.currentUser
        val userRef = firestore.collection("users").document(user?.uid ?: return)

        userRef.update("photoUrl", photoUrl)
            .addOnSuccessListener {
            }
            .addOnFailureListener { e ->
                Toast.makeText(requireContext(), "Falha ao atualizar no Firestore: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
