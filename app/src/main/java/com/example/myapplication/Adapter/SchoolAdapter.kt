package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.Data.SchoolItem
import com.example.myapplication.databinding.ItemSchoolBinding
import java.util.*
import javax.inject.Inject


class SchoolAdapter @Inject() constructor() :
    PagingDataAdapter<SchoolItem, SchoolAdapter.ViewHolder>(differCallback) {

    companion object {
        val differCallback = object : DiffUtil.ItemCallback<SchoolItem>() {
            override fun areItemsTheSame(oldItem: SchoolItem, newItem: SchoolItem): Boolean {
                return oldItem.dbn == newItem.dbn
            }

            override fun areContentsTheSame(oldItem: SchoolItem, newItem: SchoolItem): Boolean {
                return oldItem == newItem
            }
        }
    }

    private lateinit var binding: ItemSchoolBinding
    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        binding = ItemSchoolBinding.inflate(inflater, parent, false)
        context = parent.context
        return ViewHolder()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position)!!)
        holder.setIsRecyclable(false)
    }

    inner class ViewHolder : RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(item: SchoolItem) {
            binding.apply {
                with(item) {
                    schoolName.text = school_name ?: ""
                    school_sports?.takeIfNotEmpty {
                        schoolSports.setVisible()
                        schoolSports.text = "School sports : $it"
                    }
                    website?.takeIfNotEmpty {
                        schoolWebsite.setVisible()
                        schoolWebsite.text = "School website : $it"
                    }
                    root.setOnClickListener {
                        onItemClickListener?.let {
                            it(item)
                        }
                    }
                }
            }
        }
    }

    private var onItemClickListener: ((SchoolItem) -> Unit)? = null

    fun setOnItemClickListener(listener: (SchoolItem) -> Unit) {
        onItemClickListener = listener
    }
}


