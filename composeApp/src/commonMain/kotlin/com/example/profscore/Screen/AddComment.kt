package com.example.profscore.Screen

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.profscore.ViewModel
import com.example.profscore.data.Tag


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddComment(navController: NavController, viewModel: ViewModel, profId: String?) {
    /*BackHandler {
        viewModel.updateQuality(0)
        navController.navigateUp() // Navigate back
    }*/
    val course = remember { mutableStateOf("") }
    val gpa = remember { mutableStateOf("") }
    val wouldTakeAgain = remember { mutableStateOf(true) }
    val difficulty = remember { mutableStateOf(3) }
    val quality by viewModel.quality.collectAsState()
    val attendance = remember { mutableStateOf(true) }
    val selectedTags = remember { mutableStateOf<Set<Tag>>(emptySet()) }
    val tags = listOf(
        Tag.HELPFUL,
        Tag.ENGAGING,
        Tag.CARING,
        Tag.ORGANIZED,
        Tag.CHALLENGING,
        Tag.CLEAR_EXPLANATIONS,
        Tag.FAIR_GRADING,
        Tag.STRICT_ATTENDANCE
    )
    val comments = remember { mutableStateOf("") }
    val focusState = remember { mutableStateOf(false) }
    val focusStateSecond = remember { mutableStateOf(false) }
    val difficulties = listOf(
        "Very Hard" to 5,
        "Hard" to 4,
        "Moderate" to 3,
        "Easy" to 2,
        "Very Easy" to 1
    )

    Column(
        modifier = Modifier
            .padding(16.dp).fillMaxSize().imePadding()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().wrapContentHeight(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = {
                viewModel.updateQuality(0)
                navController.navigateUp()
            }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
            Text("Post", modifier = Modifier.clickable {
                viewModel.addUserComment(
                    profId = profId ?: "",
                    gpa = gpa.value,
                    wouldTakeAgain = wouldTakeAgain.value,
                    difficulty = difficulty.value.toInt(),
                    quality = quality,
                    attendance = attendance.value,
                    course = course.value,
                    tags = selectedTags.value.toList(),
                    comments = comments.value
                )
                viewModel.readThreeComments(profId ?: "")
                viewModel.updateQuality(0)
                navController.navigateUp()
            })
        }


        AnimatedRatingBar(
            rating = quality,
            onRatingChanged = { newRating ->
                viewModel.updateQuality(newRating)
            },
            modifier = Modifier.padding(16.dp).fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))


        Box(
            modifier = Modifier
                .border(
                    width = 1.dp,
                    color = if (focusState.value) Color.Blue else Color.DarkGray,
                    shape = RoundedCornerShape(8.dp)
                )
                .background(Color.Transparent)
        ) {
            TextField(value = comments.value,
                onValueChange = { comments.value = it },
                colors = TextFieldDefaults.textFieldColors(

                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledTextColor = colorScheme.onSurface,
                    disabledIndicatorColor = Color.Transparent,
                    containerColor = Color.Transparent,

                    ),
                placeholder = { Text("Describe your experience") },
                modifier = Modifier.background(color = colorScheme.surface, shape = RoundedCornerShape(8.dp))
                    .fillMaxWidth().onFocusChanged { focusState.value = it.isFocused })
        }
        Spacer(modifier = Modifier.height(8.dp))

        Text("Write Course Code")
        Box(
            modifier = Modifier
                .border(
                    width = 1.dp,
                    color = if (focusStateSecond.value) Color.Blue else Color.DarkGray,
                    shape = RoundedCornerShape(8.dp)
                )
                .background(Color.Transparent)
        ) {
            TextField(value = course.value,
                onValueChange = { course.value = it },
                colors = TextFieldDefaults.textFieldColors(

                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledTextColor = colorScheme.onSurface,
                    disabledIndicatorColor = Color.Transparent,
                    containerColor = Color.Transparent,

                    ),
                modifier = Modifier.background(color = colorScheme.surface, shape = RoundedCornerShape(8.dp))
                    .fillMaxWidth().onFocusChanged { focusStateSecond.value = it.isFocused })
        }
        Spacer(modifier = Modifier.height(8.dp))

        Text("How difficult was this professor?")

        LazyRow(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(difficulties) { (label, level) ->
                val backgroundColor by animateColorAsState(
                    targetValue = if (difficulty.value == level) Color.Green else Color.LightGray
                )
                val contentColor by animateColorAsState(
                    targetValue = Color.White
                )
                Button(
                    onClick = { difficulty.value = level },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = backgroundColor,
                        contentColor = contentColor
                    ),
                    modifier = Modifier
                        .height(48.dp)
                ) {
                    Text(text = label)
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))

        Text("Would you take this professor again?")

        Box(
            modifier = Modifier.fillMaxWidth().wrapContentHeight().padding(16.dp)
                .clip(shape = RoundedCornerShape(40))
                .background(color = Color.LightGray)
        ) {
            val yesButtonColor by animateColorAsState(
                targetValue = if (wouldTakeAgain.value) Color.Green else Color.LightGray
            )
            val noButtonColor by animateColorAsState(
                targetValue = if (wouldTakeAgain.value) Color.LightGray else Color.Green
            )
            Row {
                Button(
                    onClick = {
                        wouldTakeAgain.value = true
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor =yesButtonColor
                    ),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Yes")
                }
                Button(
                    onClick = {
                        wouldTakeAgain.value = false
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = noButtonColor
                    ),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("No")
                }
            }


        }

        Spacer(modifier = Modifier.height(8.dp))

        Text("Was attendance mandatory?")
        Box(
            modifier = Modifier.fillMaxWidth().wrapContentHeight().padding(16.dp)
                .clip(shape = RoundedCornerShape(40))
                .background(color = Color.LightGray)
        ) {
            val yesButtonColorAttendance by animateColorAsState(
                targetValue = if (attendance.value) Color.Green else Color.LightGray
            )
            val noButtonColorAttendance by animateColorAsState(
                targetValue = if (attendance.value) Color.LightGray else Color.Green
            )
            Row {
                Button(
                    onClick = {
                        attendance.value = true
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = yesButtonColorAttendance
                    ), modifier = Modifier.weight(1f)
                ) {
                    Text("Yes")
                }
                Button(
                    onClick = {
                        attendance.value = false
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = noButtonColorAttendance
                    ), modifier = Modifier.weight(1f)
                ) {
                    Text("No")
                }
            }


        }

        Spacer(modifier = Modifier.height(12.dp))

        Text("Select up to 3 tags")
        Spacer(modifier = Modifier.height(8.dp))
        LazyRow {
            items(tags) { tag ->
                EachTag(
                    tag = tag,
                    isSelected = selectedTags.value.contains(tag),
                    onTagSelected = { selectedTag, isSelected ->
                        val updatedTags = if (isSelected) {
                            selectedTags.value + selectedTag
                        } else {
                            selectedTags.value - selectedTag
                        }
                        selectedTags.value = updatedTags
                    }
                )
            }
        }
    }
}

@Composable
fun EachTag(
    tag: Tag,
    isSelected: Boolean,
    onTagSelected: (Tag, Boolean) -> Unit
) {
    Box(
        modifier = Modifier.padding(4.dp)
            .border(
                width = 1.dp,
                color = Color.DarkGray,
                shape = RoundedCornerShape(30)
            )
            .clip(RoundedCornerShape(30))
            .background(color = if (isSelected) Color.Green else Color.White)
            .height(40.dp)
            .width(140.dp)
            .clickable { onTagSelected(tag, !isSelected) },
        contentAlignment = Alignment.Center
    ) {
        Text(
            tag.displayName,
            color = Color.DarkGray,
            modifier = Modifier.align(Alignment.Center).padding(start = 2.dp, end = 2.dp),
            fontSize = 14.sp
        )
    }
}