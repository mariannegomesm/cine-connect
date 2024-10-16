package com.example.cine_connect.ui.screens.moviedetails

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.cine_connect.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class MovieDetailsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_moviedetails, container, false)

        val imageUrl = arguments?.getString("imageUrl")
        if (imageUrl != null) {
            loadImage(view, imageUrl)
        } else {
            Log.e("MovieDetailsFragment", "imageUrl is null")
        }

        setupDeleteButton(view, R.id.button_delete_topic) 
        setupDeleteButton(view, R.id.button_delete_topic2)

        return view
    }

    private fun loadImage(view: View, imageUrl: String) {
        val imageView = view.findViewById<ImageView>(R.id.imageView)
        Glide.with(this).load(imageUrl).into(imageView)
    }

    private fun setupDeleteButton(view: View, buttonId: Int) {
        view.findViewById<Button>(buttonId).setOnClickListener {
            showConfirmationDialog { deleteTopic() }
        }
    }

    private fun showConfirmationDialog(onConfirm: () -> Unit) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Confirmar Exclusão")
            .setMessage("Você tem certeza que deseja excluir este tópico?")
            .setPositiveButton("Excluir") { dialog: DialogInterface, _: Int ->
                onConfirm()
                dialog.dismiss()
            }
            .setNegativeButton("Cancelar") { dialog: DialogInterface, _: Int ->
                dialog.dismiss()
            }
            .show()
    }

    private fun deleteTopic() {
        Log.d("MovieDetailsFragment", "Tópico excluído")
    }
}
