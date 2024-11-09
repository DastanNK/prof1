package com.example.profscore.Screen

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.profscore.Screens
import com.example.profscore.ViewModel
import com.example.profscore.data.ProfData
import kotlinx.coroutines.launch

@Composable
fun SingleProfessor(navController: NavController, viewModel: ViewModel, profId: String?) {
    val givenProf = viewModel.givenProfessorData.value
    val threeComments = viewModel.threeCommentData.value
    val profSimilar: Map<String, List<ProfData>> = viewModel.profDataSimilar.value
    val one = remember { mutableStateOf(0.0) }
    val two = remember { mutableStateOf(0.0) }
    val three = remember { mutableStateOf(0.0) }
    val four = remember { mutableStateOf(0.0) }
    val five = remember { mutableStateOf(0.0) }
    val text = viewModel.queryText.collectAsState()
    BackHandler {
        viewModel.onQueryUpdate(text.value)
        navController.navigate(Screens.Professor.route) // Navigate back
    }

    if (givenProf?.quantity != 0 && givenProf != null) {
        one.value = givenProf.oneDifficulty / givenProf.quantity.toDouble()
        two.value = givenProf.twoDifficulty / givenProf.quantity.toDouble()
        three.value = givenProf.threeDifficulty / givenProf.quantity.toDouble()
        four.value = givenProf.fourDifficulty / givenProf.quantity.toDouble()
        five.value = givenProf.fiveDifficulty / givenProf.quantity.toDouble()
    } else {
        one.value = 0.0
        two.value = 0.0
        three.value = 0.0
        four.value = 0.0
        five.value = 0.0
    }
    Column(modifier = Modifier.fillMaxSize().padding(8.dp)) {
        Row(modifier = Modifier.fillMaxWidth().wrapContentHeight()) {
            Row(modifier = Modifier.weight(1f)) {
                IconButton(onClick = {
                    viewModel.onQueryUpdate(text.value)
                    navController.navigate(Screens.Professor.route)
                }) {
                    Icon(Icons.Default.Clear, contentDescription = "Back")
                }
            }
            Row(modifier = Modifier.weight(15f), horizontalArrangement = Arrangement.Center) {
                Text(givenProf?.profName.toString(), fontSize = 38.sp, fontWeight = FontWeight.ExtraBold)
            }
        }

        Row(modifier = Modifier.fillMaxWidth().wrapContentHeight(), horizontalArrangement = Arrangement.Center) {
            Column(modifier = Modifier.padding(start = 12.dp, end = 16.dp).weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(givenProf?.averageQuality.toString(), fontSize = 32.sp, fontWeight = FontWeight.Bold)
                    Icon(Icons.Default.Star, contentDescription = null)
                }
                Text("${givenProf?.quantity ?: 0} reviews", fontSize = 20.sp, fontWeight = FontWeight.Normal)
            }
            Box(
                modifier = Modifier
                    .height(72.dp) // Take up the full height of the parent
                    .width(2.dp) // Set the width of the divider
                    .background(color = Color.LightGray) // Set the color of the divider
            )
            Column(modifier = Modifier.padding(start = 12.dp, end = 16.dp).weight(1f)) {
                Text(
                    "${givenProf?.averageWouldTakeAgain?.toInt().toString()}%",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )
                Text("Would take again", fontSize = 16.sp, fontWeight = FontWeight.Normal)
            }
            Box(
                modifier = Modifier
                    .height(72.dp) // Take up the full height of the parent
                    .width(2.dp) // Set the width of the divider
                    .background(color = Color.LightGray) // Set the color of the divider
            )
            Column(modifier = Modifier.padding(start = 16.dp, end = 12.dp).weight(1f)) {
                Text(givenProf?.averageDifficulty.toString(), fontSize = 28.sp, fontWeight = FontWeight.Bold)
                Text("Level of Difficulty", fontSize = 16.sp, fontWeight = FontWeight.Normal)

            }
        }
        if(!givenProf?.syllabus.isNullOrEmpty()){
            Text("Syllabus", modifier = Modifier.clickable {
                navController.navigate(Screens.Syllabuses.route)
            })

        }
        Column(horizontalAlignment = Alignment.Start, modifier = Modifier.padding(top = 16.dp, bottom = 16.dp)) {

            Text("Rate ${givenProf?.profName.toString()}")
            val quality by viewModel.quality.collectAsState()
            AnimatedRatingBar(
                rating = quality,
                onRatingChanged = { newRating ->
                    viewModel.updateQuality(newRating)
                    navController.navigate(Screens.AddComment.createRoute(profId ?: ""))
                },
                modifier = Modifier.padding(16.dp).fillMaxWidth()
            )
            Text("Write a review", modifier = Modifier.clickable {
                navController.navigate(Screens.AddComment.createRoute(profId ?: ""))
            })
        }

        LazyColumn(
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
            //horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth().clickable {
                        viewModel.readAllComments(profId.toString())
                        navController.navigate(Screens.AllComments.createRoute(profId.toString()))
                    },
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Rating and reviews", fontSize = 16.sp)
                    Icon(Icons.Default.ArrowForward, contentDescription = null)
                }
                EachRatingDistribution(1, one.value)
                EachRatingDistribution(2, two.value)
                EachRatingDistribution(3, three.value)
                EachRatingDistribution(4, four.value)
                EachRatingDistribution(5, five.value)
            }

            items(threeComments) { threeComment ->
                Row(modifier = Modifier.fillMaxWidth().padding(top = 8.dp)) {
                    EachComments(threeComment, viewModel)
                }

            }
            item {
                Text("See all reviews", fontSize = 16.sp, modifier = Modifier.clickable {
                    viewModel.readAllComments(profId.toString())
                    navController.navigate(Screens.AllComments.createRoute(profId.toString()))
                })
            }

            item {
                if (!profSimilar.isNullOrEmpty()) {
                    Spacer(modifier = Modifier.height(24.dp))
                    Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth().clickable {
                        viewModel.readAllComments(profId.toString())
                        navController.navigate(Screens.AllSimilarProfessor.route)
                    }) {
                        Text("Similar professor")
                        Icon(Icons.Default.ArrowForward, contentDescription = null)
                    }

                    LazyRow {
                        profSimilar.forEach { (course, profList) ->
                            items(profList) { profData ->
                                SimilarBox(profData, course, navController, viewModel)
                            }
                        }
                    }
                }

            }
        }
    }
}

@Composable
fun SimilarBox(profSimilar: ProfData, course: String, navController: NavController, viewModel: ViewModel) {

    Row(
        modifier = Modifier.wrapContentHeight().fillMaxWidth().clickable {
            viewModel.readThreeComments(profId = profSimilar.id.toString())
            viewModel.retrieveGivenProf(profSimilar.id.toString())
            navController.navigate(Screens.SingleProfessor.createRoute(profSimilar.id.toString()))

        }.padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.padding(end = 8.dp)) {
            Text(profSimilar.profName.toString(), fontSize = 18.sp)
            Text(profSimilar.averageDifficulty.toString(), fontSize = 14.sp)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(profSimilar.averageQuality.toString(), fontSize = 14.sp)
                Icon(Icons.Default.Star, contentDescription = null)
            }
        }
        Text(course, fontSize = 16.sp)
    }

}

@Composable
fun EachRatingDistribution(starCount: Int, number: Double) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Star icon and label
        Text("$starCount stars", modifier = Modifier.width(60.dp), fontSize = 16.sp)
        Spacer(modifier = Modifier.width(8.dp))

        // Background bar for the rating distribution
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(14.dp)
                .clip(RoundedCornerShape(6.dp))
                .background(Color.LightGray)
        ) {
            // Foreground bar showing percentage
            Box(
                modifier = Modifier
                    .fillMaxWidth((number * 1f).toFloat())
                    .height(20.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(MaterialTheme.colorScheme.primary)
            )
        }
    }
}

@Composable
fun AnimatedRatingBar(
    rating: Int,
    onRatingChanged: (Int) -> Unit,
    modifier: Modifier = Modifier,
    starCount: Int = 5
) {
    val isDarkTheme = isSystemInDarkTheme()
    val starColorFilled = if (isDarkTheme) PlayStoreAccentColorDark else PlayStoreAccentColorLight
    val starColorEmpty = if (isDarkTheme) PlayStoreTextColorDark else PlayStoreTextColorLight
    var hoverRating by remember { mutableStateOf(0) }
    val scope = rememberCoroutineScope()

    Row(modifier = modifier, horizontalArrangement = Arrangement.SpaceBetween) {
        for (i in 1..starCount) {
            val filled = i <= (if (hoverRating > 0) hoverRating else rating)
            val scale by animateFloatAsState(
                targetValue = if (filled) 1.2f else 1f,
                animationSpec = tween(durationMillis = 300)
            )

            Icon(
                imageVector = if (filled) Icons.Outlined.Star else Icons.Outlined.Star,
                contentDescription = "Star",
                tint = if (filled) starColorFilled else starColorEmpty,
                modifier = Modifier
                    .size(40.dp)
                    .graphicsLayer(scaleX = scale, scaleY = scale)
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onTap = {
                                onRatingChanged(i)
                            },
                            onPress = {
                                scope.launch {
                                    hoverRating = i
                                    tryAwaitRelease()
                                    hoverRating = 0
                                }
                            }
                        )
                    }
            )
        }
    }
}