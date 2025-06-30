package com.mcshr.sportquiz.ui.utils

import com.mcshr.sportquiz.domain.entity.Player


fun formatPlayerName(player: Player?): String {
    return player?.let {
        if (it.name.length > 10) {
            it.name.take(10) + "..."
        } else {
            it.name
        }
    } ?: "-"
}
