package com.nishantdev961.exoplayer

import android.app.Application
import android.net.Uri
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.ExoPlayerLibraryInfo
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.extractor.ExtractorsFactory
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.trackselection.TrackSelector
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.BandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import kotlinx.android.synthetic.main.item.view.*
import java.lang.Exception
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection.Factory as Factory1

class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

    private lateinit var exoPlayer:SimpleExoPlayer
    private lateinit var playerView:PlayerView

    fun bind(application: Application, data:Member){

        itemView.item_name.text = data.name

        playerView = itemView.findViewById(R.id.item_exoplayer)

        try {
            var bandwidthMeter: BandwidthMeter = DefaultBandwidthMeter.Builder(application)
                    .build()
            var trackSelector: TrackSelector = DefaultTrackSelector(application)
            exoPlayer = SimpleExoPlayer.Builder(application)
                    .setTrackSelector(trackSelector)
                    .build()

            val uri: Uri = Uri.parse(data.videoUrl)

            val dataSourceFactory = DefaultHttpDataSourceFactory("video");

            val mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(MediaItem.fromUri(uri))
            playerView.player = exoPlayer
            exoPlayer.setMediaSource(mediaSource)
            exoPlayer.prepare()
            exoPlayer.playWhenReady = false
        }
        catch (e: Exception){

            Log.d("ERR", "exoplayer error ho gya :( + ${e.toString()}")
        }

    }
}