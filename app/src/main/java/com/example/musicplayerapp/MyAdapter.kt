package com.example.musicplayerapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MyAdapter constructor(var onItemClick: ((SongData) -> Unit)) :
    RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

    private var list: List<SongData> = ArrayList()
    public fun setData(list: List<SongData>) {
        this.list = list;
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyAdapter.MyViewHolder, position: Int) {
        holder.bindingData(list.get(position))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class MyViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val itemText: TextView = itemView.findViewById(R.id.tvSongName);

        fun bindingData(songData: SongData) {
            itemText.text = songData.title
        }

        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(list[adapterPosition])
            }
        }
    }
}