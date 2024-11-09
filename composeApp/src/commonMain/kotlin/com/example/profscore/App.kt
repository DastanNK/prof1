package com.example.profscore

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.profscore.Screen.AddComment
import com.example.profscore.Screen.AddProfessor
import com.example.profscore.Screen.AllComments
import com.example.profscore.Screen.AllSimilarProfessor
import com.example.profscore.Screen.Professor
import com.example.profscore.Screen.SingleProfessor
import com.example.profscore.Screen.Syllabus
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

import profscore.composeapp.generated.resources.Res
import profscore.composeapp.generated.resources.compose_multiplatform

@Composable
@Preview
fun App() {
    MaterialTheme {
        val navController= rememberNavController()
        //val viewModel = hiltViewModel<ViewModel>()
        val viewModel:ViewModel= viewModel()
        if(viewModel.inProgress.value){
            CommonProgressSpiner()
        }
        NavHost(navController = navController, startDestination =Screens.Professor.route){
            composable(Screens.Professor.route){
                Professor(navController,viewModel)
            }
            composable(Screens.AddProfessor.route){
                AddProfessor(navController, viewModel)
            }
            composable(Screens.SingleProfessor.route){navBackStackEntry ->
                SingleProfessor(navController, viewModel, navBackStackEntry.arguments?.getString("profId"))
            }
            composable(Screens.AddComment.route){navBackStackEntry ->
                AddComment(navController, viewModel, navBackStackEntry.arguments?.getString("profId"))
            }
            composable(Screens.AllComments.route){navBackStackEntry ->
                AllComments(navController, viewModel, navBackStackEntry.arguments?.getString("profId"))
            }
            composable(Screens.AllSimilarProfessor.route){
                AllSimilarProfessor(navController, viewModel)
            }
            composable(Screens.Syllabuses.route){
                Syllabus(navController, viewModel)
            }
        }
    }
}