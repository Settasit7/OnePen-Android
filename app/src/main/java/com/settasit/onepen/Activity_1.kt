package com.settasit.onepen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import io.github.sceneview.*
import io.github.sceneview.animation.Transition.animateRotation
import io.github.sceneview.math.Position
import io.github.sceneview.math.Rotation
import io.github.sceneview.node.ModelNode
import kotlinx.coroutines.delay

@Composable
fun Activity_1(navController: NavController) {
    Box(modifier = Modifier.fillMaxSize()) {
        val engine = rememberEngine()
        val modelLoader = rememberModelLoader(engine = engine)
        val cameraNode = rememberCameraNode(engine = engine).apply {
            position = Position(
                y = 1.8f,
                z = 3.0f
            )
        }
        val cameraTransition = rememberInfiniteTransition(label = "CameraTransition")
        val cameraRotation by cameraTransition.animateRotation(
            initialValue = Rotation(y = 360.0f),
            targetValue = Rotation(y = 0.0f),
            animationSpec = infiniteRepeatable(animation = tween(
                durationMillis = 30 * 1000,
                easing = LinearEasing
            ))
        )
        val centerNode = rememberNode(engine = engine).addChildNode(node = cameraNode)
        var isVisibleOne by remember { mutableStateOf(value = false) }
        var isVisibleTwo by remember { mutableStateOf(value = false) }
        var isVisibleThr by remember { mutableStateOf(value = false) }
        LaunchedEffect(key1 = Unit) {
            delay(timeMillis = (1.0 * 1000).toLong())
            isVisibleOne = true
        }
        LaunchedEffect(key1 = Unit) {
            delay(timeMillis = (5.5 * 1000).toLong())
            isVisibleTwo = true
        }
        LaunchedEffect(key1 = Unit) {
            delay(timeMillis = (7.5 * 1000).toLong())
            isVisibleThr = true
        }
        Scene(
            modifier = Modifier.fillMaxSize(),
            engine = engine,
            modelLoader = modelLoader,
            cameraNode = cameraNode,
            childNodes = listOf(
                ModelNode(
                    modelInstance = modelLoader.createModelInstance(assetFileLocation = "models/model-1-1-1.glb"),
                    scaleToUnits = 1.00f
                )
            ),
            onFrame = {
                centerNode.rotation = cameraRotation
                cameraNode.lookAt(targetNode = centerNode)
            }
        )
        AnimatedVisibility(
            visible = isVisibleOne,
            enter = fadeIn(animationSpec = tween(
                durationMillis = (0.25 * 1000).toInt(),
                easing = LinearEasing
            ))
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(modifier = Modifier
                    .fillMaxHeight()
                    .padding(vertical = (0.32 * LocalConfiguration.current.screenHeightDp).dp)
                    .width(width = 216.dp)
                ) {
                    if (isVisibleOne) {
                        TypewriterText(
                            texts = listOf(
                                "OnePen: An alternative for joining\nThailand's Loy Krathong Festival."
                            ),
                            modifier = Modifier.align(alignment = Alignment.BottomStart)
                        )
                    }
                }
            }
        }
        AnimatedVisibility(
            visible = isVisibleTwo,
            enter = fadeIn(animationSpec = tween(
                durationMillis = (0.25 * 1000).toInt(),
                easing = LinearEasing
            ))
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(modifier = Modifier
                    .fillMaxHeight()
                    .padding(vertical = (0.26 * LocalConfiguration.current.screenHeightDp).dp)
                    .width(width = 216.dp)
                ) {
                    Text(
                        text = "Let's alleviate water pollution.",
                        modifier = Modifier.align(alignment = Alignment.BottomStart),
                        color = colorResource(id = R.color.YELLOW)
                    )
                }
            }
        }
        AnimatedVisibility(
            visible = isVisibleThr,
            enter = fadeIn(animationSpec = tween(
                durationMillis = (0.25 * 1000).toInt(),
                easing = LinearEasing
            ))
        ) {
            Box(modifier = Modifier
                .fillMaxSize()
                .padding(vertical = (0.14 * LocalConfiguration.current.screenHeightDp).dp)
            ) {
                Surface(
                    modifier = Modifier
                        .align(alignment = Alignment.BottomCenter)
                        .height(height = 48.dp)
                        .width(width = 96.dp),
                    shape = RoundedCornerShape(size = 24.dp),
                    color = colorResource(id = R.color.YELLOW)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .clickable(onClick = {
                                navController.navigate(route = "Activity 2")
                            }),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_chevron_right_24),
                            contentDescription = null,
                            tint = Color.Black
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TypewriterText(
    texts: List<String>,
    modifier: Modifier
) {
    var textIndex by remember { mutableStateOf(value = 0) }
    var textToDisplay by remember { mutableStateOf(value = "") }
    LaunchedEffect(key1 = texts) {
        while (textIndex < texts.size) {
            texts[textIndex].forEachIndexed { charIndex, _ ->
                textToDisplay = texts[textIndex].substring(
                    startIndex = 0,
                    endIndex = charIndex + 1
                )
                delay(timeMillis = (0.05 * 1000).toLong())
            }
            textIndex += 1
        }
    }
    Text(
        text = textToDisplay,
        modifier = modifier,
        color = colorResource(id = R.color.YELLOW),
        fontWeight = FontWeight.Bold
    )
}