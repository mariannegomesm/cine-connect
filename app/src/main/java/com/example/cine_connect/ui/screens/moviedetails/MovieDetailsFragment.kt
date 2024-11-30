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
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.cine_connect.R
import com.example.cine_connect.data.models.MovieDetailsResponse
import com.example.cine_connect.network.TmdbApiClient
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch

class MovieDetailsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_moviedetails, container, false)


        val imageUrl = arguments?.getString("imageUrl")
        imageUrl?.let { loadImage(view, it) }


        setupNavigationButtons(view)



        // setupDeleteButton(view, R.id.button_delete_topic)
        // setupDeleteButton(view, R.id.button_delete_topic2)


        setupReviewsSection(view)


        val movieId = arguments?.getInt("movieId") ?: -1
        if (movieId != -1) {
            fetchMovieDetails(movieId)
        } else {
            Log.e("MovieDetailsFragment", "ID do filme inválido")
        }

        return view
    }

    private fun loadImage(view: View, imageUrl: String) {
        val imageView = view.findViewById<ImageView>(R.id.imageView)
        Glide.with(this).load(imageUrl).into(imageView)
    }

    private fun setupNavigationButtons(view: View) {
        val buttonRating: MaterialButton = view.findViewById(R.id.button_rating)
        buttonRating.setOnClickListener {

            val movieId = arguments?.getInt("movieId") ?: 0
            val posterUrl = arguments?.getString("imageUrl") ?: ""
            val movieTitle = arguments?.getString("movieTitle") ?: ""

            val bundle = Bundle().apply {
                putInt("movieId", movieId)
                putString("posterUrl", posterUrl)
                putString("movieTitle", movieTitle)
            }
            findNavController().navigate(R.id.action_movieDetailsFragment_to_rateFragment, bundle)
        }

        val buttonTopics: MaterialButton = view.findViewById(R.id.button_topics)

        buttonTopics.setOnClickListener {
            val movieId = arguments?.getInt("movieId") ?: 0
            val posterUrl = arguments?.getString("imageUrl") ?: ""
            val movieTitle = arguments?.getString("movieTitle") ?: ""

            val bundle = Bundle().apply {
                putInt("movieId", movieId)
                putString("posterUrl", posterUrl)
                putString("movieTitle", movieTitle)
            }

            findNavController().navigate(R.id.action_movieDetailsFragment_to_topicsFragment, bundle)
        }
    }

    private fun setupReviewsSection(view: View) {
        val cardReviews: LinearLayout = view.findViewById(R.id.reviews_section)
        cardReviews.setOnClickListener {
            val movieId = arguments?.getInt("movieId") ?: 0
            val posterUrl = arguments?.getString("imageUrl") ?: ""
            val movieTitle = arguments?.getString("movieTitle") ?: ""

                    val bundle = Bundle().apply{
                    putInt("movieId", movieId)
                    putString("posterUrl", posterUrl)
                    putString("movieTitle", movieTitle)
                }
                findNavController().navigate(R.id.action_movieDetailsFragment_to_listReviewsFragment, bundle)
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

    private fun fetchMovieDetails(movieId: Int) {
        lifecycleScope.launch {
            try {
                val apiKey = "06ebff010c5d4faf628283ca3f1ac421"
                val movieDetails = TmdbApiClient.apiService.getMovieDetails(movieId, apiKey, "pt-BR")
                updateUI(movieDetails)
            } catch (e: Exception) {
                Log.e("MovieDetailsFragment", "Erro ao buscar detalhes do filme: ${e.message}")
            }
        }
    }

    private fun updateUI(details: MovieDetailsResponse) {
        val titleTextView = view?.findViewById<TextView>(R.id.titlemovie)
        val ratingTextView = view?.findViewById<TextView>(R.id.describemovie)
        val descriptionTextView = view?.findViewById<TextView>(R.id.descricaotext)

        titleTextView?.text = details.title
        ratingTextView?.text = getString(R.string.rating_text, details.vote_average.toString())
        descriptionTextView?.text = details.overview
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