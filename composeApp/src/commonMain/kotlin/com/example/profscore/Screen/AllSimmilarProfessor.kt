package com.example.profscore.Screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.profscore.ViewModel

@Composable
fun AllSimilarProfessor(navController: NavController, viewModel: ViewModel) {
    val allSimilarProfessor = viewModel.allProfDataSimilar.value
    Column (modifier = Modifier.fillMaxSize().padding(8.dp)) {
        IconButton(onClick = {
            navController.navigateUp()
        }) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Back")
        }
        LazyVerticalGrid(GridCells.Fixed(2), modifier = Modifier.padding(8.dp).padding(start = 8.dp)) {
            allSimilarProfessor.forEach { (course, profList) ->
                items(profList) { profData ->
                    SimilarBox(profData, course, navController, viewModel)
                }
            }

        }
    }
}