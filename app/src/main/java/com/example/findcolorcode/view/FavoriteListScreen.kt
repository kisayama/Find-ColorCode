package com.example.findcolorcode.view

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController

@Composable
fun FavoriteListScreen(navController: NavController, viewModel: ViewModel){
    Greeting()
}

@Composable
private fun Greeting (){
    Text(text = "I'm FavoriteListScreen")
}