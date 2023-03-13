package com.example.myapplication.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
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
                it.setGone()
                retry?.invoke()
                binding.errorText.setGone()
            }
        }

        fun setData(state: LoadState) {
            binding.apply {
                prgBarLoadMore.isVisible = state is LoadState.Loading
                btnRetry.setGone()

                if (loadState is LoadState.Error) {
                    errorText.setVisible()
                    errorText.text = (loadState as LoadState.Error).error.localizedMessage

                    btnRetry.isVisible = loadState is LoadState.Error
                    if (!isNetworkAvailable) btnRetry.setGone()

                    // Clearing the error text
                    if (isNetworkAvailable && btnRetry.isVisible) {
                        errorText.text = ""
                    }
                }

            }
        }
    }
}