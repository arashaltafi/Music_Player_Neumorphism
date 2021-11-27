package com.arash.altafi.musicplayer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.squareup.picasso.Picasso

class MusicAdapter(val musicList : List<MusicModel> , val onClickItemMusic : onClickItemMusic) : RecyclerView.Adapter<MusicAdapter.viewHolder>() {

    var itemPosition: Int = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        var view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_music, parent, false)
        return viewHolder(view)
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        holder.bindMusic(musicList.get(position))
    }

    override fun getItemCount(): Int {
        return musicList.size
    }

    fun notifyMusic(musicModel: MusicModel) {
        val indexItem = musicList.indexOf(musicModel)
        if (indexItem != -1) {
            if (indexItem != itemPosition) {
                notifyItemChanged(itemPosition)
                itemPosition = indexItem
                notifyItemChanged(itemPosition)
            }
        }
    }

    inner class viewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val imgMusic: ImageView = itemView.findViewById(R.id.img_music_list)
        val txtName: TextView = itemView.findViewById(R.id.tv_music_name)
        val txtArtist: TextView = itemView.findViewById(R.id.tv_music_artist)
        val frMusic: FrameLayout = itemView.findViewById(R.id.fr_music)
        val animationView: LottieAnimationView = itemView.findViewById(R.id.animation_view)

        fun bindMusic(musicModel: MusicModel) {
            Picasso.get().load(musicModel.coverRes).into(imgMusic)
            txtArtist.text = musicModel.artist
            txtName.text = musicModel.name
            if (adapterPosition == itemPosition) {
                animationView.visibility = View.VISIBLE
            }
            else {
                animationView.visibility = View.GONE
            }
            itemView.setOnClickListener {
                onClickItemMusic.onClick(musicModel , adapterPosition)
            }
        }

    }

}

interface onClickItemMusic {
    fun onClick(musicModel: MusicModel , position: Int)
}