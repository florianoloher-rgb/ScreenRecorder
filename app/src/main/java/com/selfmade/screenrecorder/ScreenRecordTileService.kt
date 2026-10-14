package com.selfmade.screenrecorder

import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.service.quicksettings.Tile
import android.service.quicksettings.TileService

class ScreenRecordTileService : TileService() {

    override fun onStartListening() { super.onStartListening(); refresh() }

    override fun onClick() {
        super.onClick()
        if (RecordingService.isRecording) {
            val stop = Intent(this, RecordingService::class.java)
                .setAction(RecordingService.ACTION_STOP)
            startService(stop)
        } else {
            val open = Intent(this, MainActivity::class.java)
                .setAction(MainActivity.ACTION_START_FROM_TILE)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                val pi = PendingIntent.getActivity(
                    this, 0, open,
                    PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
                startActivityAndCollapse(pi)
            } else {
                @Suppress("DEPRECATION")
                startActivityAndCollapse(open)
            }
        }
        refresh()
    }

    private fun refresh() {
        val t = qsTile ?: return
        t.state = if (RecordingService.isRecording) Tile.STATE_ACTIVE else Tile.STATE_INACTIVE
        t.label = getString(R.string.tile_label)
        t.updateTile()
    }
}
