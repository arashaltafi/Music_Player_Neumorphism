package com.arash.altafi.musicplayer

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.slider.Slider
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.play_list.*
import soup.neumorphism.ShapeType
import java.util.*

class MainActivity : AppCompatActivity(), onClickItemMusic {

    var mediaPlayer: MediaPlayer? = null
    var musicList: List<MusicModel> = MusicModel.list
    var timer: Timer? = null
    var itemIndex: Int = 0
    var musicState: MusicState = MusicState.STOP
    var adaper: MusicAdapter? = null
    lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    var isCheckBt: Boolean = false
    var isChange: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init()
    }

    private fun init() {

        bottomSheetBehavior = BottomSheetBehavior.from(cr_play_list)

        adaper = MusicAdapter(musicList, this)
        recy_music.adapter = adaper
        recy_music.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        musicPlay(musicList.get(0))

        back_application.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        fab_playlist.setOnClickListener {
            if (isCheckBt) {
                // collapsed => close
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                isCheckBt = false
            } else {
                // expanded => open
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                isCheckBt = true
            }
        }


        slider_music.addOnSliderTouchListener(object : Slider.OnSliderTouchListener {
            override fun onStartTrackingTouch(slider: Slider) {
                if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_COLLAPSED) {
                    isChange = true
                }
            }

            override fun onStopTrackingTouch(slider: Slider) {
                if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_COLLAPSED) {
                    isChange = false
                    mediaPlayer!!.seekTo(slider.value.toInt())
                }
            }
        })


        fab_next.setOnClickListener {
            onNext()
        }

        fab_previous.setOnClickListener {
            onBack()
        }

        fab_play.setOnClickListener {
            when (musicState) {
                MusicState.PLAY -> {
                    mediaPlayer!!.pause()
                    musicState = MusicState.PAUSE
                    fab_play.setImageResource(R.drawable.ic_baseline_play_arrow_24)
                    fab_play.setShapeType(ShapeType.FLAT)
                }
                MusicState.STOP, MusicState.PAUSE -> {
                    mediaPlayer!!.start()
                    musicState = MusicState.PLAY
                    fab_play.setImageResource(R.drawable.ic_baseline_pause_24)
                    fab_play.setShapeType(ShapeType.FLAT)
                }
                else -> ""
            }
        }
    }

    enum class MusicState {
        PLAY, PAUSE, STOP
    }

    fun onNext() {
        timer!!.cancel()
        timer!!.purge()
        mediaPlayer!!.release()

        if (itemIndex < musicList.size - 1) {
            itemIndex++
        } else {
            itemIndex = 0
        }
        musicPlay(musicList.get(itemIndex))
        fab_play.setImageResource(R.drawable.ic_baseline_pause_24)
        fab_play.setShapeType(ShapeType.FLAT)
    }

    fun onBack() {
        timer!!.cancel()
        timer!!.purge()
        mediaPlayer!!.release()

        if (itemIndex == 0) {
            itemIndex = musicList.size - 1
        } else {
            itemIndex--
        }
        musicPlay(musicList.get(itemIndex))
        fab_play.setImageResource(R.drawable.ic_baseline_pause_24)
        fab_play.setShapeType(ShapeType.FLAT)
    }

    fun musicPlay(musicModel: MusicModel) {
        adaper!!.notifyMusic(musicModel)
        slider_music.value = 0.0f
        txt_position.text = "00:00"
        mediaPlayer = MediaPlayer.create(this, musicModel.musicRes)
        mediaPlayer!!.setOnPreparedListener {
            mediaPlayer!!.start()
            timer = Timer()
            timer!!.schedule(object : TimerTask() {
                override fun run() {
                    runOnUiThread {
                        if (!isChange) {
                            slider_music.value = mediaPlayer!!.currentPosition.toFloat()
                        }
                        txt_position.text =
                            MusicModel.convertMillisToString(mediaPlayer!!.currentPosition.toLong())
                    }
                }
            }, 1000, 1000)

        }
        musicState = MusicState.PLAY
        slider_music.valueTo = mediaPlayer!!.duration.toFloat()
        txt_duration.text = MusicModel.convertMillisToString(mediaPlayer!!.duration.toLong())
        Picasso.get().load(musicModel.coverRes).into(img_music)
        txt_name_music.setText(musicModel.name)
        txt_name_singer.setText(musicModel.artist)
    }

    override fun onResume() {
        super.onResume()
        mediaPlayer!!.start()
        musicState = MusicState.PLAY
    }

    override fun onPause() {
        super.onPause()
        mediaPlayer!!.pause()
        musicState = MusicState.PAUSE
    }

    override fun onDestroy() {
        super.onDestroy()
        timer!!.cancel()
        timer!!.purge()
        mediaPlayer!!.release()
        mediaPlayer = null
    }

    override fun onClick(musicModel: MusicModel, position: Int) {
        timer!!.cancel()
        timer!!.purge()
        mediaPlayer!!.release()
        itemIndex = position
        musicPlay(musicList.get(itemIndex))
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
    }

}