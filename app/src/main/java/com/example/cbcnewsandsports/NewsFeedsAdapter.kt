package com.example.cbcnewsandsports

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cbcnewsandsports.databinding.RowItemBinding
import com.example.cbcnewsandsports.model.NewsFeedModel

class NewsFeedsAdapter : RecyclerView.Adapter<NewsFeedsAdapter.ViewHolder>(),Filterable {

    var list = ArrayList<NewsFeedModel>()
    var listFiltered = ArrayList<NewsFeedModel>()


    inner class ViewHolder(val binding: RowItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RowItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.apply {
            txtHeadLine.text = listFiltered[position].title
            txtDate.text = listFiltered[position].readablePublishedAt
            Glide
                .with(imageView.context)
                .load(listFiltered[position].typeAttributes.imageLarge)
                .centerCrop()
                .into(imageView)

        }
    }

    override fun getItemCount(): Int {
        return listFiltered.size
    }

    fun refreshList(updateList: ArrayList<NewsFeedModel>) {
        list.clear()
        listFiltered.clear()
        list.addAll(updateList)
        listFiltered.addAll(updateList)
    }


    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charString = constraint?.toString() ?: ""
                if (charString.isEmpty()) listFiltered = list else {
                    val filteredList = ArrayList<NewsFeedModel>()
                    list
                        .filter {
                            (it.type.contains(constraint!!))

                        }
                        .forEach { filteredList.add(it) }
                    listFiltered = filteredList

                }
                return FilterResults().apply { values = listFiltered }
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {

                listFiltered = if (results?.values == null)
                    ArrayList()
                else
                    results.values as ArrayList<NewsFeedModel>
                notifyDataSetChanged()
            }
        }
    }
}
