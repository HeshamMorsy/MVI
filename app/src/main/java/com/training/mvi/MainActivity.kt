package com.training.mvi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * This class is used to send user intents and to render the results from MainViewModel
 */
class MainActivity : AppCompatActivity() {
    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        render()
    }

    fun sendIntent(view: View){
        lifecycleScope.launchWhenStarted {
            viewModel.intentChannel.send(MainIntent.AddNumber)
        }
    }

    private fun render(){
        lifecycleScope.launch {
            viewModel.state.collect {
                when(it){
                    is MainViewState.Idle -> number_text_view.text = "Idle"
                    is MainViewState.Number -> number_text_view.text = it.number.toString()
                    is MainViewState.Error -> number_text_view.text = it.error
                }
            }
        }
    }
}