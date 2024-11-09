package com.example.profscore.Screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.profscore.ViewModel

@Composable
fun Syllabus(navController: NavController, viewModel: ViewModel){
    val syllabusList=viewModel.givenProfessorData.value
    Column (modifier = Modifier.fillMaxSize().padding(8.dp)){
        IconButton(onClick = {
            navController.navigateUp()
        }) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Back")
        }
        if(syllabusList?.syllabus!=null){
            LazyColumn (modifier = Modifier.padding(8.dp).padding(start = 8.dp)) {
                items(syllabusList.syllabus){list->
                    HyperlinkText(list.courseName.toString(), list.link.toString())
                }
            }
        }
    }


}

@Composable
fun HyperlinkText(text: String, url: String) {
    val context = LocalContext.current
    val annotatedString = AnnotatedString.Builder().apply {
        withStyle(
            style = SpanStyle(
               // color = MaterialTheme.colorScheme.primary,
                textDecoration = TextDecoration.Underline,
                fontSize = 16.sp
            )
        ) {
            append(text)
        }
    }.toAnnotatedString()

    ClickableText(
        text = annotatedString,
        onClick = {
            // Open the URL when clicked
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            context.startActivity(intent)
        }
    )
}