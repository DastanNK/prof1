package com.example.profscore.Screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.profscore.ViewModel
import com.example.profscore.data.UserComments

@Composable
fun AllComments(navController: NavController, viewModel: ViewModel, profId: String?) {
    val allCommentDataEachProf = viewModel.commentData.value
    Column(modifier = Modifier.fillMaxSize()) {
        IconButton(onClick = {
            navController.navigateUp()
        }) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Back")
        }
        LazyColumn(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(allCommentDataEachProf) { allCommentDataEachProf ->
                Row(modifier = Modifier.fillMaxWidth()) {
                    EachComments(allCommentDataEachProf, viewModel)

                }
            }
        }
    }

}

@Composable
fun EachComments(commentsData: UserComments, viewModel: ViewModel) {
    val helpful = remember { mutableStateOf(commentsData.helpful ?: 0) }
    val notHelpful = remember { mutableStateOf(commentsData.notHelpful ?: 0) }
    LaunchedEffect(commentsData.helpful, commentsData.notHelpful) {
        helpful.value = commentsData.helpful ?: 0
        notHelpful.value = commentsData.notHelpful ?: 0
    }
    var isLiked by remember { mutableStateOf(false) }
    var isDisliked by remember { mutableStateOf(false) }
    Column(
        //horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth().padding(12.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Column(modifier = Modifier.weight(1f)) {
                Text("Quality", color = Color.Gray, fontSize = 14.sp)
                Text(commentsData.quality.toString(), fontSize = 14.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text("Difficulty", color = Color.Gray, fontSize = 14.sp)
                Text(commentsData.difficulty.toString(), fontSize = 14.sp)
            }
            Column(modifier = Modifier.padding(start = 16.dp).weight(3f)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(commentsData.course.toString(), modifier = Modifier.weight(1f), fontSize = 16.sp)
                    Text(commentsData.date.toString(), modifier = Modifier.weight(1f), fontSize = 14.sp)
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text("Attendance: ${if (commentsData.attendance == true) "Mandatory" else "Not mandatory"}", fontSize = 14.sp)
                Spacer(modifier = Modifier.height(8.dp))


                LazyRow( modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(commentsData.tags ?: listOf()) { tag ->
                        Box(
                            modifier = Modifier.background(color = Color.Transparent, shape = RoundedCornerShape(40)).border(width = 1.dp, shape = RoundedCornerShape(40), color = MaterialTheme.colorScheme.primary)
                                .padding(horizontal = 12.dp, vertical = 4.dp)
                                .wrapContentHeight().wrapContentWidth(),
                        ) {
                            Text(tag.displayName, color = MaterialTheme.colorScheme.onBackground
                                , fontSize = 14.sp)

                        }
                    }
                }

            }
        }
        var isExpanded by remember { mutableStateOf(false) }

        Text(
            text = commentsData.comments.toString(),
            maxLines = if (isExpanded) Int.MAX_VALUE else 3, // Expand to show all lines if clicked
            overflow = if (isExpanded) TextOverflow.Clip else TextOverflow.Ellipsis, // Show ellipsis when not expanded
            modifier = Modifier.clickable {
                isExpanded = !isExpanded // Toggle expanded state on click
            }.padding(top = 4.dp), fontSize = 14.sp
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(helpful.value.toString() + " people found this helpful", fontSize = 14.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Was this review helpful?", fontSize = 14.sp)
            Row {
                TextButton(
                    onClick = {
                        if (isLiked) {
                            helpful.value--
                            isLiked = false
                        } else {
                            helpful.value++
                            if (isDisliked) {
                                notHelpful.value--
                                isDisliked = false
                            }
                            isLiked = true
                        }
                        viewModel.addHelpfulness(
                            commentsData.id.toString(),
                            helpful.value,
                            notHelpful.value,
                            commentsData.profId.toString()
                        )
                    }, modifier = Modifier
                        .border(
                            BorderStroke(1.dp, if (isLiked) MaterialTheme.colorScheme.primary else Color.Gray),
                            shape = RoundedCornerShape(20)
                        ).background(
                            color = if (isLiked) MaterialTheme.colorScheme.primary else Color.Transparent, // Background color based on state
                            shape = RoundedCornerShape(20)
                        ).width(48.dp).height(28.dp), // Keep the fixed height
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text(
                        textAlign = TextAlign.Center,
                        text = "Yes", fontSize = 12.sp, color = Color.Black
                        //color = if (isLiked) Color.Blue else Color.Gray
                    )
                }
                //Text(helpful.value.toString())

                Spacer(modifier = Modifier.width(16.dp))

                TextButton(
                    onClick = {
                        if (isDisliked) {
                            notHelpful.value--
                            isDisliked = false
                        } else {
                            notHelpful.value++
                            if (isLiked) {
                                helpful.value--
                                isLiked = false
                            }
                            isDisliked = true
                        }
                        viewModel.addHelpfulness(
                            commentsData.id.toString(),
                            helpful.value,
                            notHelpful.value,
                            commentsData.profId.toString()
                        )
                    },
                    modifier = Modifier
                        .border(
                            BorderStroke(1.dp, if (isDisliked) MaterialTheme.colorScheme.primary else Color.Gray),
                            shape = RoundedCornerShape(20)
                        ).background(
                            color = if (isDisliked) MaterialTheme.colorScheme.primary else Color.Transparent,
                            shape = RoundedCornerShape(20)
                        ).width(48.dp).height(28.dp),
                    contentPadding = PaddingValues(0.dp),
                ) {
                    Text(//textAlign = TextAlign.Center,
                        text = "No", fontSize = 12.sp, color = Color.Black
                        //color = if (isDisliked) Color.Blue else Color.Gray
                    )
                }
            }

            //Text(notHelpful.value.toString())
        }
    }
}
