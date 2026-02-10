package com.example.wishlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.util.Locale

class WishlistAdapter(
    private val items: MutableList<WishItem>,
    private val onItemClicked: (WishItem) -> Unit,
    private val onItemLongPressed: (Int) -> Unit
) : RecyclerView.Adapter<WishlistAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTv: TextView = itemView.findViewById(R.id.nameTv)
        val priceTv: TextView = itemView.findViewById(R.id.priceTv)
        val urlTv: TextView = itemView.findViewById(R.id.urlTv)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.wishlist_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.nameTv.text = item.name
        holder.priceTv.text = String.format(Locale.US, "$%.2f", item.price)
        holder.urlTv.text = item.url

        holder.itemView.setOnClickListener {
            onItemClicked(item)
        }

        holder.itemView.setOnLongClickListener {
            onItemLongPressed(holder.bindingAdapterPosition)
            true
        }
    }

    override fun getItemCount(): Int = items.size
}