package com.training.mvi

/**
 * this class is used to represent user intents (actions) in MainActivity
 */
sealed class MainIntent {
    // add number
    object AddNumber : MainIntent()
}