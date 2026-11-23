package com.music.bitchord.ui

import com.music.bitchord.data.model.Song

/**
 * A selection belongs to one detail page. Playlist rows use setVideoId so two
 * occurrences of the same YouTube song remain independently selectable.
 */
data class SongSelection(
    val browseId: String,
    private val keys: Set<String> = emptySet(),
) {
    val count: Int get() = keys.size

    operator fun contains(song: Song): Boolean = song.selectionKey() in keys

    fun toggled(song: Song): SongSelection {
        val key = song.selectionKey()
        return copy(keys = if (key in keys) keys - key else keys + key)
    }

    fun withAll(songs: List<Song>): SongSelection =
        copy(keys = songs.mapTo(LinkedHashSet()) { it.selectionKey() })

    /** Resolves selected rows in the page's displayed order, not tap order. */
    fun of(songs: List<Song>): List<Song> = songs.filter { it.selectionKey() in keys }

    private fun Song.selectionKey(): String =
        setVideoId?.takeIf { it.isNotBlank() }?.let { "entry:$it" } ?: "video:$videoId"
}
