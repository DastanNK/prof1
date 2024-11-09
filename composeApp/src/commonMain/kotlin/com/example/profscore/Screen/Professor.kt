package com.example.profscore.Screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.profscore.ProfessorSearch
import com.example.profscore.Screens
import com.example.profscore.ViewModel
import com.example.profscore.data.ProfData

@Composable
fun Professor(navController: NavController, viewModel: ViewModel) {
    val professorData = viewModel.profData.value
    val text = viewModel.queryText.collectAsState()

    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Row(modifier = Modifier.fillMaxWidth().wrapContentHeight()) {
            ProfessorSearch(viewModel)
        }
        LazyColumn {
            items(professorData) { professorData ->
                EachProfessorList(professorData, navController, viewModel)

            }
            if (viewModel.moreProf.value && text.value.isEmpty()) {
                item {
                    Button(
                        onClick = {
                            viewModel.quantityProfessor.value += 10
                            viewModel.onReadUpdate(viewModel.quantityProfessor.value)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent,
                            contentColor = Color.Black
                        ), border = BorderStroke(
                            width = 1.dp,
                            color = Color.DarkGray
                        )
                    ) {
                        Text("Load More")
                    }
                }
            }
            item {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Don't see the professor you're looking for?")
                    Text("Add a Professor", modifier = Modifier.clickable {
                        navController.navigate(Screens.AddProfessor.route)
                    })
                }

            }
        }

    }

}

@Composable
fun EachProfessorList(profData: ProfData, navController: NavController, viewModel: ViewModel) {
    Box(
        modifier = Modifier.wrapContentHeight().fillMaxWidth()
            .clickable {
                viewModel.readThreeComments(profId = profData.id.toString())
                viewModel.retrieveGivenProf(profData.id.toString())
                navController.navigate(Screens.SingleProfessor.createRoute(profData.id.toString()))
            }) {
        Row(modifier = Modifier.fillMaxSize().padding(top = 8.dp, bottom = 8.dp)) {
            Column(
                modifier = Modifier.fillMaxHeight().padding(start = 16.dp).padding(8.dp).weight(2f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Quality")
                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .height(40.dp)
                        .width(40.dp)
                        .background(
                            color =
                            if (profData.averageQuality > 3.99) Color.Green
                            else if (3.99 >= profData.averageQuality && profData.averageQuality >= 2.99) Color.Yellow
                            else if (2.99 > profData.averageQuality && profData.averageQuality >= 0.1) Color.Red
                            else Color.Gray
                        ), contentAlignment = Alignment.Center
                ) {
                    Text(profData.averageQuality.toString())
                }
                Text("${profData.quantity ?: 0} ratings")
            }
            Column(modifier = Modifier.fillMaxHeight().padding(8.dp).weight(5f)) {
                Text(profData.profName.toString(), fontWeight = FontWeight.Bold, fontSize = 20.sp)
                Text(
                    profData.department.toString(),
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(top = 8.dp, bottom = 8.dp)
                )
                Row {
                    Text(
                        profData.averageDifficulty.toString(),
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text("level of difficulty", fontWeight = FontWeight.Normal, fontSize = 16.sp)
                }

            }

        }

    }
}