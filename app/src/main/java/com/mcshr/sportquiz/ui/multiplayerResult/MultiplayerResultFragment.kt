package com.mcshr.sportquiz.ui.multiplayerResult

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.mcshr.sportquiz.BuildConfig
import com.mcshr.sportquiz.R
import com.mcshr.sportquiz.databinding.FragmentMultiplayerResultBinding
import com.mcshr.sportquiz.ui.utils.formatPlayerName


class MultiplayerResultFragment : Fragment() {

    private var _binding: FragmentMultiplayerResultBinding? = null
    private val binding
        get() = _binding ?: throw RuntimeException("FragmentMultiplayerResultBinding is null")

    private val args: MultiplayerResultFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMultiplayerResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val room = args.room
        val playerNumber = args.playerNumber

        val myScore = if (playerNumber == 1) room.player1?.score ?: 0 else room.player2?.score ?: 0
        val opponentScore =
            if (playerNumber == 1) room.player2?.score ?: 0 else room.player1?.score ?: 0


        val resultText = when {
            myScore > opponentScore -> getString(R.string.win)
            myScore < opponentScore -> getString(R.string.lose)
            else -> getString(R.string.draw)
        }

        binding.tvResult.text = resultText
        binding.tvYourScore.text = myScore.toString()
        binding.tvOpponentScore.text = opponentScore.toString()
        binding.tvPlayer1.text = formatPlayerName(room.player1)
        binding.tvPlayer2.text = formatPlayerName(room.player2)
        binding.btnHome.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.btnRetry.setOnClickListener {
            val action = MultiplayerResultFragmentDirections.actionMultiplayerResultFragmentToMultiplayerSetupFragment()
            findNavController().navigate(action)
        }

        loadBannerAd()
    }

    private fun loadBannerAd() {
        val adView = AdView(requireContext()).apply {
            setAdSize(AdSize.BANNER)
            adUnitId = BuildConfig.AD_BANNER_ID
        }

        binding.adView.removeAllViews()
        binding.adView.addView(adView)

        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}