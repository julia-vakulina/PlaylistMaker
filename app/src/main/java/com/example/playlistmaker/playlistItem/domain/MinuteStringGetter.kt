package com.example.playlistmaker.playlistItem.domain

class MinuteStringGetter {
    fun getMinuteString(number: Int): String {
        val minute = "минут"
        val a = "а"
        val y = "ы"
        return when (number) {
            1 -> "$number $minute$a"
            in 2..4 -> "$number $minute$y"
            else -> "$number $minute"
        }
    }
}