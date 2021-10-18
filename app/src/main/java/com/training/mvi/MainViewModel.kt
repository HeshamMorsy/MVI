package com.training.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

/** this class is used to process user intents and reduce the result to be rendered on MainActivity **/
class MainViewModel : ViewModel(){
    // intent channel to take user intents
    val intentChannel = Channel<MainIntent>(Channel.UNLIMITED)
    // state flow to help MainActivity render the result
    private val _viewState = MutableStateFlow<MainViewState>(MainViewState.Idle)
    val state: StateFlow<MainViewState> get() = _viewState
    // number to be incremented
    var number = 0

    init {
        process()
    }


    private fun process(){
        viewModelScope.launch {
            intentChannel.consumeAsFlow().collect {
                when(it){
                    is MainIntent.AddNumber -> addNumber()
                }
            }
        }
    }

    private fun addNumber(){
        viewModelScope.launch {
            _viewState.value =
                try{
                    MainViewState.Number(number++)
                }catch (e: Exception){
                    MainViewState.Error(e.message.toString())
                }
        }
    }

}