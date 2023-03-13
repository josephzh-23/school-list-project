package com.example.myapplication.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.myapplication.*
import com.example.myapplication.Data.SchoolSATScore
import com.example.myapplication.Util.toast
import com.example.myapplication.databinding.FragmentSchoolSatBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SATFragment : Fragment() {

    private lateinit var binding: FragmentSchoolSatBinding

    private val args: SATFragmentArgs by navArgs()
    private val viewModel: EducationsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentSchoolSatBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Use the school dbn to fetch SAT data
        args.dbn.takeIfNotEmpty {
            viewModel.getSATScoreBySchool(it)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {

            viewModel.scores.observe(viewLifecycleOwner) { uiState ->
                uiState?.run { render(this) }
            }
        }
    }

    private fun render(uiState: Resource<SchoolSATScore>) {
        when (uiState) {
            is Resource.Loading<SchoolSATScore> -> {
                onLoad()
            }
            is Resource.Success<SchoolSATScore> -> {
                onSuccess(uiState)
            }
            is Resource.Error<SchoolSATScore> -> {
                onError(uiState)
            }
        }
    }

    private fun onLoad() = with(binding) {
        progressBar.setVisible()
    }

    private fun onSuccess(ui: Resource.Success<SchoolSATScore>) = with(binding) {
        progressBar.setGone()
        ui.data?.apply {
            schoolName.text = school_name
            satMathAvgScore.text = "Average SAT Math score : $sat_math_avg_score"
            satWritingAvgScore.text = "Average SAT Writing score : $sat_writing_avg_score"
            numSatTakers.text = "Number of SAT takers : $num_of_sat_test_takers"
        }
    }

    private fun onError(uiState: Resource.Error<SchoolSATScore>) = with(binding) {
        progressBar.setGone()
        toast(uiState.message.toString())
    }
}

