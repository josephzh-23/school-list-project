package com.example.myapplication.ui

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.Adapter.LoadMoreAdapter
import com.example.myapplication.EducationsViewModel
import com.example.myapplication.SchoolAdapter
import com.example.myapplication.Util.toast
import com.example.myapplication.databinding.FragmentSchoolListBinding
import com.example.myapplication.setGone
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class SchoolListFragment : Fragment() {


    private val viewModel: EducationsViewModel by viewModels()

    lateinit var loadAdapter: LoadMoreAdapter

    @Inject
    lateinit var schoolAdapter: SchoolAdapter

    private lateinit var binding: FragmentSchoolListBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment

        viewModel.createConnectionMonitor()
        binding = FragmentSchoolListBinding.inflate(layoutInflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            rlSchools.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = schoolAdapter
            }

            schoolAdapter.setOnItemClickListener {
                val direction =
                    SchoolListFragmentDirections.actionSchoolListFragmentToSchoolSATFragment(
                        it.dbn ?: ""
                    )
                findNavController().navigate(direction)
            }

            // Showing warning msg if not connected
            if (!isNetworkConnected()) {
                errorText.text = "please connect to wifi/data to see list of schools"
            }


            // Observe loading state
            lifecycleScope.launch {
                schoolAdapter.loadStateFlow.collect {
                    val state = it.refresh
                    prgBar.isVisible = state is LoadState.Loading
                }
            }

            loadAdapter = LoadMoreAdapter()
            loadAdapter.retry = { schoolAdapter.retry() }
            viewModel.checkConnection?.observe(viewLifecycleOwner) {
                loadAdapter.isNetworkAvailable = it

                // If user is connected to wifi
                if (it) {
                    errorText.setGone()

                    // Case 1: if user opens app and it's not connected to internet
                    if (schoolAdapter.itemCount == 0) {

                        viewModel.launchPagingSource()
                        lifecycleScope.launch {
                            repeatOnLifecycle(Lifecycle.State.STARTED) {
                                if (viewModel.checkConnection?.value == true) {
                                    viewModel.schoolList.collect {
                                        schoolAdapter.submitData(it)
                                    }
                                }
                            }
                        }

                        // Case 2: when some items are already loaded and
                        // user turns off wifi and then later restores wifi connection
                        // Update the last row of the recyclerview to show the retry
                        // button
                    } else {
                        schoolAdapter.notifyItemChanged(schoolAdapter.itemCount)
                    }

                    // If user turns off wifi
                } else {
                    toast(
                        "Please turn on wifi/data to reconnect to get more data ",
                        duration = Toast.LENGTH_LONG
                    )
                }
            }

            rlSchools.adapter = schoolAdapter.withLoadStateFooter(loadAdapter)
        }
    }

    private fun isNetworkConnected(): Boolean {
        val cm =
            requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.activeNetworkInfo != null
    }
}