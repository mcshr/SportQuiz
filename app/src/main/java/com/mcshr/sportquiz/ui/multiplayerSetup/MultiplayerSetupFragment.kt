package com.mcshr.sportquiz.ui.multiplayerSetup

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.mcshr.sportquiz.R
import com.mcshr.sportquiz.databinding.FragmentMultiplayerSetupBinding
import com.mcshr.sportquiz.domain.entity.QuizMode
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MultiplayerSetupFragment : Fragment() {

    private var _binding: FragmentMultiplayerSetupBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MultiplayerSetupViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMultiplayerSetupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.btnStartGame.setOnClickListener {
            val nickname = binding.etNickname.text.toString().trim()
            val selectedMode = when (binding.radioGroup.checkedRadioButtonId) {
                R.id.rbEmoji -> QuizMode.EMOJI
                R.id.rbRiddle -> QuizMode.RIDDLE
                R.id.rbTest -> QuizMode.TEST
                else -> null
            }

            if (nickname.isBlank()) {
                Toast.makeText(requireContext(), "Введите никнейм", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (selectedMode == null) {
                Toast.makeText(requireContext(), "Выберите режим", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewModel.saveNickname(nickname)

            val action = MultiplayerSetupFragmentDirections
                .actionMultiplayerSetupFragmentToMultiplayerQuizFragment(selectedMode.name)
            findNavController().navigate(action)
        }

        viewModel.nickname.observe(viewLifecycleOwner) {
            binding.etNickname.setText(it)
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
