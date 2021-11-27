package com.arash.altafi.musicplayer

import java.util.*
import kotlin.collections.ArrayList

class MusicModel {

    var id : Int = 0
    var name : String ?= null
    var artist : String ?= null
    var coverRes : Int = 0
    var musicRes : Int = 0

    companion object {
        val list : List<MusicModel>
        get() {
            val musicList : MutableList<MusicModel> = ArrayList()
            val music1 = MusicModel()
            music1.artist = "رضا بهرام"
            music1.name = "هوای دل"
            music1.coverRes = R.drawable.havayedel
            music1.musicRes = R.raw.havayedel

            val music2 = MusicModel()
            music2.artist = "رضا بهرام"
            music2.name = "شهر خالی"
            music2.coverRes = R.drawable.shahrkhali
            music2.musicRes = R.raw.shahrkhali

            val music3 = MusicModel()
            music3.artist = "رضا بهرام"
            music3.name = "از عشق بگو"
            music3.coverRes = R.drawable.azeshghbegoo
            music3.musicRes = R.raw.azeshghbegoo

            val music4 = MusicModel()
            music4.artist = "رضا بهرام"
            music4.name = "گل عشق"
            music4.coverRes = R.drawable.goleeshgh
            music4.musicRes = R.raw.goleeshgh


            musicList.add(music1)
            musicList.add(music2)
            musicList.add(music3)
            musicList.add(music4)
            return musicList
        }

        fun convertMillisToString(durationInMillis : Long) : String {
            val second = durationInMillis / 1000 % 60
            val minute = durationInMillis / (1000 * 60) % 60
            return String.format(Locale.US , "%02d:%02d" , minute , second)
        }
    }

}