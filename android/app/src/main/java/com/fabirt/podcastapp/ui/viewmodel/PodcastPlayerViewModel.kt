package com.fabirt.podcastapp.ui.viewmodel

import android.support.v4.media.MediaBrowserCompat
import androidx.lifecycle.ViewModel
import com.fabirt.podcastapp.constant.K
import com.fabirt.podcastapp.data.service.MediaPlayerServiceConnection
import com.fabirt.podcastapp.domain.model.Episode
import com.fabirt.podcastapp.util.isPlayEnabled
import com.fabirt.podcastapp.util.isPlaying
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PodcastPlayerViewModel @Inject constructor(
    private val serviceConnection: MediaPlayerServiceConnection
) : ViewModel() {

    val currentPlayingEpisode = serviceConnection.currentPlayingEpisode
    val playbackState = serviceConnection.playbackState

    val podcastisPlaying: Boolean
        get() = playbackState.value?.isPlaying == true

    fun playPodcast(episodes: List<Episode>, currentEpisode: Episode) {
        serviceConnection.playPodcast(episodes)
        serviceConnection.transportControls.playFromMediaId(currentEpisode.id, null)
    }

    fun tooglePlaybackState() {
        when {
            playbackState.value?.isPlaying == true -> {
                serviceConnection.transportControls.pause()
            }

            playbackState.value?.isPlayEnabled == true -> {
                serviceConnection.transportControls.play()
            }
        }
    }

    fun stopPlayback() {
        serviceConnection.transportControls.stop()
    }

    override fun onCleared() {
        super.onCleared()
        serviceConnection.unsubscribe(
            K.MEDIA_ROOT_ID,
            object : MediaBrowserCompat.SubscriptionCallback() {})
    }
}