package com.example.profscore.Screen

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.profscore.ViewModel

@Composable
fun AddProfessor(navController: NavController, viewModel: ViewModel) {
    var name by remember { mutableStateOf("") }
    var department by remember { mutableStateOf("") }
    val isFormFilled = name.isNotEmpty() && department.isNotEmpty()


    Column(
        modifier = Modifier.fillMaxSize().padding(8.dp)
    ) {
        Text("Add a Professor", fontWeight = FontWeight.Bold, fontSize = 32.sp)
        Text("Please use the search bar above to make sure that the professor does not already exist at this school.", modifier = Modifier.padding(top=16.dp, bottom = 16.dp))
        Text("Professor's Name", modifier = Modifier.padding(top=16.dp))
        TextField(
            value = name,
            onValueChange = { name = it },
            maxLines = 1,
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.White,
                // Set the underline color to transparent
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent
            ),
            modifier = Modifier.padding(top=16.dp, bottom = 16.dp).border(2.dp, Color.Black, RoundedCornerShape(12.dp))
        )
        Text("Field of Study")
        TextField(
            value = department,
            onValueChange = { department = it },
            maxLines = 1,
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.White,
                // Set the underline color to transparent
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent
            ),
            modifier = Modifier.padding(top=16.dp, bottom = 16.dp).border(2.dp, Color.Black, RoundedCornerShape(12.dp))
        )

        Button(
            onClick = {
                viewModel.addProf(profName = name, department = department)
                navController.navigateUp()
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isFormFilled) Color.Blue else Color.Gray
            ),
            enabled = isFormFilled
        ) {
            Text("Add Professor")
        }
    }
}