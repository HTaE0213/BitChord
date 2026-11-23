package com.music.bitchord

import com.music.bitchord.data.model.Song
import com.music.bitchord.ui.SongSelection
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class SongSelectionTest {
    private fun song(id: String, entryId: String? = null) =
        Song(videoId = id, title = "Song $id", artist = "Artist", thumbnailUrl = null, setVideoId = entryId)

    @Test
    fun togglingAddsAndRemovesASong() {
        val song = song("a")
        val selected = SongSelection("VLplaylist").toggled(song)
        assertTrue(song in selected)
        assertFalse(song in selected.toggled(song))
    }

    @Test
    fun selectedSongsFollowPageOrderRatherThanTapOrder() {
        val songs = listOf(song("a"), song("b"), song("c"))
        val selected = SongSelection("VLplaylist")
            .toggled(songs[2])
            .toggled(songs[0])
        assertEquals(listOf("a", "c"), selected.of(songs).map { it.videoId })
    }

    @Test
    fun duplicatePlaylistEntriesRemainIndependentlySelectable() {
        val first = song("same-video", "entry-1")
        val second = song("same-video", "entry-2")
        val selected = SongSelection("VLplaylist").toggled(first)
        assertTrue(first in selected)
        assertFalse(second in selected)
        assertEquals(listOf("entry-1"), selected.of(listOf(first, second)).map { it.setVideoId })
    }

    @Test
    fun selectAllIncludesEveryEntry() {
        val songs = listOf(song("same-video", "entry-1"), song("same-video", "entry-2"))
        assertEquals(2, SongSelection("VLplaylist").withAll(songs).of(songs).size)
    }
}
