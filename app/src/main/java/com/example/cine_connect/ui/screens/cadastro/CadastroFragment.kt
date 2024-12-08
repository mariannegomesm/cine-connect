// CadastroFragment.kt
package com.example.cine_connect.ui.cadastro

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.cine_connect.R
import com.example.cine_connect.auth.AuthHelper
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.textfield.TextInputEditText
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.launch

class CadastroFragment : Fragment() {

    private lateinit var nomeInput: TextInputEditText
    private lateinit var emailInput: TextInputEditText
    private lateinit var dataNascimentoInput: TextInputEditText
    private lateinit var senhaInput: TextInputEditText
    private lateinit var confirmeSenhaInput: TextInputEditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_cadastro, container, false)

        nomeInput = view.findViewById(R.id.input_nome)
        emailInput = view.findViewById(R.id.input_email)
        dataNascimentoInput = view.findViewById(R.id.input_data_nascimento)
        senhaInput = view.findViewById(R.id.input_senha)
        confirmeSenhaInput = view.findViewById(R.id.input_confirme_senha)

        val cadastrarButton: Button = view.findViewById(R.id.cadastrarButton)
        cadastrarButton.setOnClickListener {
            registerUser()
        }

        val cadastrotologintext: TextView = view.findViewById(R.id.Cadastrotologintext)
        cadastrotologintext.setOnClickListener {
            findNavController().navigate(R.id.action_cadastroFragment4_to_loginFragment)
        }

        return view
    }

    private fun registerUser() {
        val nome = nomeInput.text.toString().trim()
        val email = emailInput.text.toString().trim()
        val dataNascimento = dataNascimentoInput.text.toString().trim()
        val senha = senhaInput.text.toString().trim()
        val confirmeSenha = confirmeSenhaInput.text.toString().trim()

        if (nome.isEmpty() || email.isEmpty() || dataNascimento.isEmpty() || senha.isEmpty() || confirmeSenha.isEmpty()) {
            Toast.makeText(requireContext(), "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show()
            return
        }

        if (senha != confirmeSenha) {
            Toast.makeText(requireContext(), "As senhas não coincidem.", Toast.LENGTH_SHORT).show()
            return
        }

        lifecycleScope.launch {
            val uid = AuthHelper.registerUser(email, senha)
            if (uid != null) {
               try {
                    AuthHelper.userCadastroInfo(uid, nome, dataNascimento, email)
                    Toast.makeText(requireContext(), "Cadastro realizado com sucesso!", Toast.LENGTH_SHORT).show()


                    findNavController().navigate(R.id.action_cadastroFragment4_to_loginFragment)
                } catch (e: Exception) {
                    Toast.makeText(requireContext(), "Erro ao salvar informações: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(requireContext(), "Falha ao cadastrar. Tente novamente.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        hideBottomNavigation()
        hideToolbar()
    }

    override fun onPause() {
        super.onPause()
        showBottomNavigation()
        showToolbar()
    }

    private fun hideBottomNavigation() {
        activity?.findViewById<BottomNavigationView>(R.id.nav_view)?.visibility = View.GONE
    }

    private fun showBottomNavigation() {
        activity?.findViewById<BottomNavigationView>(R.id.nav_view)?.visibility = View.VISIBLE
    }

    private fun hideToolbar() {
        (activity as? AppCompatActivity)?.supportActionBar?.hide()
    }

    private fun showToolbar() {
        (activity as? AppCompatActivity)?.supportActionBar?.show()
    }
}
