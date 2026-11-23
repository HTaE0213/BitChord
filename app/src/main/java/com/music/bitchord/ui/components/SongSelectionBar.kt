package com.music.bitchord.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.music.bitchord.R
import com.music.bitchord.data.model.Song
import com.music.bitchord.ui.SongSelection
import com.music.bitchord.ui.icons.SfIcons

/** The actions available for the current page's selected songs. */
class SongSelectionActions(
    val onToggle: (Song) -> Unit,
    val onSelectAll: (List<Song>) -> Unit,
    val onPlayNext: (List<Song>) -> Unit,
    val onAddToQueue: (List<Song>) -> Unit,
    val onAddToPlaylist: ((List<Song>) -> Unit)?,
    val onRemove: ((List<Song>) -> Unit)?,
    val onDone: () -> Unit,
)

/**
 * A compact action bar that stays below the top chrome. Actions scroll
 * horizontally on narrow phones instead of shrinking touch targets.
 */
@Composable
fun SongSelectionBar(
    selection: SongSelection,
    songs: List<Song>,
    actions: SongSelectionActions,
    modifier: Modifier = Modifier,
) {
    val picked = selection.of(songs)
    val allPicked = songs.isNotEmpty() && picked.size == songs.size
    val ink = MaterialTheme.colorScheme.onSurfaceVariant
    val accent = MaterialTheme.colorScheme.primary
    val shape = RoundedCornerShape(14.dp)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
            .shadow(10.dp, shape)
            .background(MaterialTheme.colorScheme.surfaceVariant, shape)
            .padding(horizontal = 2.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        BarButton(SfIcons.Close, stringResource(R.string.done), ink, true, actions.onDone)
        Text(
            text = "${picked.size} ${stringResource(R.string.selected)}",
            style = MaterialTheme.typography.titleSmall,
            color = ink,
            maxLines = 1,
            modifier = Modifier.widthIn(min = 68.dp),
        )
        Row(
            modifier = Modifier.weight(1f).horizontalScroll(rememberScrollState()),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            BarButton(SfIcons.Check, stringResource(R.string.select_all), ink, !allPicked) {
                actions.onSelectAll(songs)
            }
            BarButton(SfIcons.PlayNext, stringResource(R.string.play_next), accent, picked.isNotEmpty()) {
                actions.onPlayNext(picked)
            }
            BarButton(SfIcons.PlayLast, stringResource(R.string.add_to_queue), accent, picked.isNotEmpty()) {
                actions.onAddToQueue(picked)
            }
            actions.onAddToPlaylist?.let { add ->
                BarButton(SfIcons.AddToPlaylist, stringResource(R.string.add_to_playlist), accent, picked.isNotEmpty()) {
                    add(picked)
                }
            }
            actions.onRemove?.let { remove ->
                BarButton(SfIcons.RemoveFromPlaylist, stringResource(R.string.remove_from_playlist), DESTRUCTIVE, picked.isNotEmpty()) {
                    remove(picked)
                }
            }
        }
    }
}

@Composable
private fun BarButton(
    icon: ImageVector,
    label: String,
    tint: Color,
    enabled: Boolean,
    onClick: () -> Unit,
) {
    IconButton(onClick = onClick, enabled = enabled) {
        Icon(
            icon,
            contentDescription = label,
            tint = if (enabled) tint else tint.copy(alpha = 0.35f),
            modifier = Modifier.size(22.dp),
        )
    }
}

private val DESTRUCTIVE = Color(0xFFFF453A)
