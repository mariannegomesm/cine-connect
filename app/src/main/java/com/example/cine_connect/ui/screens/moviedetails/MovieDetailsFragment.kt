package com.example.cine_connect.ui.screens.moviedetails

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.cine_connect.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.bottomnavigation.BottomNavigationView

class MovieDetailsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_moviedetails, container, false)

        // Carregar a imagem se a URL estiver presente
        val imageUrl = arguments?.getString("imageUrl")
        if (imageUrl != null) {
            loadImage(view, imageUrl)
        } else {
            Log.e("MovieDetailsFragment", "imageUrl is null")
        }

        // Configurar botões de navegação
        setupNavigationButtons(view)

        // Configurar botões de exclusão
        setupDeleteButton(view, R.id.button_delete_topic) 
        setupDeleteButton(view, R.id.button_delete_topic2)

        // Configurar seção de revisões
        setupReviewsSection(view)

        return view
    }

    private fun loadImage(view: View, imageUrl: String) {
        val imageView = view.findViewById<ImageView>(R.id.imageView)
        Glide.with(this).load(imageUrl).into(imageView)
    }

    private fun setupNavigationButtons(view: View) {
        val buttonRating: MaterialButton = view.findViewById(R.id.button_rating)
        buttonRating.setOnClickListener {
            findNavController().navigate(R.id.action_movieDetailsFragment_to_rateFragment)
        }

        val buttonTopics: MaterialButton = view.findViewById(R.id.button_topics)
        buttonTopics.setOnClickListener {
            findNavController().navigate(R.id.action_movieDetailsFragment_to_topicsFragment)
        }
    }

    private fun setupReviewsSection(view: View) {
        val cardReviews: LinearLayout = view.findViewById(R.id.reviews_section)
        cardReviews.setOnClickListener {
            findNavController().navigate(R.id.action_movieDetailsFragment_to_listReviewsFragment)
        }
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

    override fun onResume() {
        super.onResume()
        activity?.findViewById<BottomNavigationView>(R.id.nav_view)?.visibility = View.VISIBLE
    }

    override fun onPause() {
        super.onPause()
        activity?.findViewById<BottomNavigationView>(R.id.nav_view)?.visibility = View.GONE
    }
}
