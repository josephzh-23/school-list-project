package com.example.myapplication.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.ItemLoadingBinding
import com.example.myapplication.setGone
import com.example.myapplication.setVisible


class LoadMoreAdapter : LoadStateAdapter<LoadMoreAdapter.ViewHolder>() {

    var retry: (() -> Unit)? = null
    private lateinit var binding: ItemLoadingBinding
    var isNetworkAvailable: Boolean = false

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): ViewHolder {
        binding = ItemLoadingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(retry)

    }

    override fun onBindViewHolder(holder: ViewHolder, loadState: LoadState) {
        holder.setData(loadState)
    }

    inner class ViewHolder(retry: (() -> Unit)? = null) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.btnRetry.setOnClickListener {
                binding.errorText.text = ""
                it.setGone()
                retry?.invoke()
            }
        }

        fun setData(state: LoadState) {
            binding.apply {


                when (state) {
                    // When connection is restored this viewholder
                    // will have a error loading status
                    is LoadState.Error -> {
                        if (isNetworkAvailable) {
                            btnRetry.setVisible()
                            errorText.setGone()
                            prgBarLoadMore.setGone()
                        }
                    }
                    is LoadState.Loading -> {
                        btnRetry.setGone()
                        errorText.setVisible()
                        prgBarLoadMore.setVisible()
                    }
                    else -> {}
                }
            }
        }
    }
}