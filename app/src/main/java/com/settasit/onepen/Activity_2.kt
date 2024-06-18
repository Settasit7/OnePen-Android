package com.settasit.onepen

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
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
import kotlinx.coroutines.launch

@SuppressLint("UnrememberedMutableState")
@Composable
fun Activity_2(
    navController: NavController,
    modelStateOne: MutableState<Int>,
    modelStateTwo: MutableState<Int>
) {
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
        val childNodes = mutableStateListOf(
            ModelNode(modelInstance = modelLoader.createModelInstance(assetFileLocation = "models/model-0-0-0.glb"))
        )
        val coroutineScope = rememberCoroutineScope()
        var isVisibleOne by remember { mutableStateOf(value = false) }
        var isVisibleTwo by remember { mutableStateOf(value = false) }
        var modelFeature by remember { mutableIntStateOf(value = 0) }
        LaunchedEffect(key1 = Unit) {
            delay(timeMillis = (1.0 * 1000).toLong())
            isVisibleOne = true
        }
        LaunchedEffect(modelFeature) {
            if (modelFeature != 0) {
                childNodes.add(ModelNode(
                    modelInstance = modelLoader.createModelInstance(assetFileLocation = "models/model-0-0-1.glb"),
                    scaleToUnits = 0.70f
                ))
                when (modelFeature) {
                    1 -> childNodes.add(ModelNode(
                        modelInstance = modelLoader.createModelInstance(assetFileLocation = "models/model-0-1-1.glb"),
                        scaleToUnits = 1.00f
                    ))
                    in 2..4 -> {
                        when (modelStateOne.value) {
                            0 -> childNodes.add(ModelNode(
                                modelInstance = modelLoader.createModelInstance(assetFileLocation = "models/model-0-1-1.glb"),
                                scaleToUnits = 1.00f
                            ))
                            1 -> childNodes.add(ModelNode(
                                modelInstance = modelLoader.createModelInstance(assetFileLocation = "models/model-0-1-2.glb"),
                                scaleToUnits = 0.90f
                            ))
                            2 -> childNodes.add(ModelNode(
                                modelInstance = modelLoader.createModelInstance(assetFileLocation = "models/model-0-1-3.glb"),
                                scaleToUnits = 0.95f
                            ))
                            3 -> childNodes.add(ModelNode(
                                modelInstance = modelLoader.createModelInstance(assetFileLocation = "models/model-0-1-4.glb"),
                                scaleToUnits = 0.85f
                            ))
                        }
                        when (modelFeature) {
                            2 -> childNodes.add(ModelNode(
                                modelInstance = modelLoader.createModelInstance(assetFileLocation = "models/model-0-2-1.glb"),
                                scaleToUnits = 0.65f
                            ))
                            in 3..4 -> {
                                when (modelStateTwo.value) {
                                    0 -> childNodes.add(ModelNode(
                                        modelInstance = modelLoader.createModelInstance(assetFileLocation = "models/model-0-2-1.glb"),
                                        scaleToUnits = 0.65f
                                    ))
                                    1 -> childNodes.add(ModelNode(
                                        modelInstance = modelLoader.createModelInstance(assetFileLocation = "models/model-0-2-2.glb"),
                                        scaleToUnits = 0.65f
                                    ))
                                    2 -> childNodes.add(ModelNode(
                                        modelInstance = modelLoader.createModelInstance(assetFileLocation = "models/model-0-2-3.glb"),
                                        scaleToUnits = 0.70f
                                    ))
                                    3 -> childNodes.add(ModelNode(
                                        modelInstance = modelLoader.createModelInstance(assetFileLocation = "models/model-0-2-4.glb"),
                                        scaleToUnits = 0.65f
                                    ))
                                }
                                when (modelFeature) {
                                    3 -> childNodes.add(ModelNode(
                                        modelInstance = modelLoader.createModelInstance(assetFileLocation = "models/model-0-3-1.glb"),
                                        scaleToUnits = 0.75f
                                    ))
                                    4 -> childNodes.add(ModelNode(
                                        modelInstance = modelLoader.createModelInstance(assetFileLocation = "models/model-0-3-1.glb"),
                                        scaleToUnits = 0.75f
                                    ))
                                }
                            }
                        }
                    }
                }
            }
        }
        LaunchedEffect(modelStateOne.value) {
            if (modelFeature != 0) {
                childNodes.add(ModelNode(
                    modelInstance = modelLoader.createModelInstance(assetFileLocation = "models/model-0-0-1.glb"),
                    scaleToUnits = 0.70f
                ))
                when (modelStateOne.value) {
                    0 -> childNodes.add(ModelNode(
                        modelInstance = modelLoader.createModelInstance(assetFileLocation = "models/model-0-1-1.glb"),
                        scaleToUnits = 1.00f
                    ))
                    1 -> childNodes.add(ModelNode(
                        modelInstance = modelLoader.createModelInstance(assetFileLocation = "models/model-0-1-2.glb"),
                        scaleToUnits = 0.90f
                    ))
                    2 -> childNodes.add(ModelNode(
                        modelInstance = modelLoader.createModelInstance(assetFileLocation = "models/model-0-1-3.glb"),
                        scaleToUnits = 0.95f
                    ))
                    3 -> childNodes.add(ModelNode(
                        modelInstance = modelLoader.createModelInstance(assetFileLocation = "models/model-0-1-4.glb"),
                        scaleToUnits = 0.85f
                    ))
                }
            }
        }
        LaunchedEffect(modelStateTwo.value) {
            if (modelFeature != 0) {
                childNodes.add(ModelNode(
                    modelInstance = modelLoader.createModelInstance(assetFileLocation = "models/model-0-0-1.glb"),
                    scaleToUnits = 0.70f
                ))
                when (modelStateOne.value) {
                    0 -> childNodes.add(ModelNode(
                        modelInstance = modelLoader.createModelInstance(assetFileLocation = "models/model-0-1-1.glb"),
                        scaleToUnits = 1.00f
                    ))
                    1 -> childNodes.add(ModelNode(
                        modelInstance = modelLoader.createModelInstance(assetFileLocation = "models/model-0-1-2.glb"),
                        scaleToUnits = 0.90f
                    ))
                    2 -> childNodes.add(ModelNode(
                        modelInstance = modelLoader.createModelInstance(assetFileLocation = "models/model-0-1-3.glb"),
                        scaleToUnits = 0.95f
                    ))
                    3 -> childNodes.add(ModelNode(
                        modelInstance = modelLoader.createModelInstance(assetFileLocation = "models/model-0-1-4.glb"),
                        scaleToUnits = 0.85f
                    ))
                }
                when (modelStateTwo.value) {
                    0 -> childNodes.add(ModelNode(
                        modelInstance = modelLoader.createModelInstance(assetFileLocation = "models/model-0-2-1.glb"),
                        scaleToUnits = 0.65f
                    ))
                    1 -> childNodes.add(ModelNode(
                        modelInstance = modelLoader.createModelInstance(assetFileLocation = "models/model-0-2-2.glb"),
                        scaleToUnits = 0.65f
                    ))
                    2 -> childNodes.add(ModelNode(
                        modelInstance = modelLoader.createModelInstance(assetFileLocation = "models/model-0-2-3.glb"),
                        scaleToUnits = 0.70f
                    ))
                    3 -> childNodes.add(ModelNode(
                        modelInstance = modelLoader.createModelInstance(assetFileLocation = "models/model-0-2-4.glb"),
                        scaleToUnits = 0.65f
                    ))
                }
            }
        }
        Scene(
            modifier = Modifier.fillMaxSize(),
            engine = engine,
            modelLoader = modelLoader,
            cameraNode = cameraNode,
            childNodes = childNodes,
            onFrame = {
                centerNode.rotation = cameraRotation
                cameraNode.lookAt(targetNode = centerNode)
            }
        )
        when (modelFeature) {
            0 -> {
                childNodes.add(ModelNode(
                    modelInstance = modelLoader.createModelInstance(assetFileLocation = "models/model-0-0-1.glb"),
                    scaleToUnits = 0.70f
                ))
                modelStateOne.value = 0
                modelStateTwo.value = 0
            }
            in 1..2 -> {
                Box(modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = (0.04 * LocalConfiguration.current.screenWidthDp).dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = (0.44 * LocalConfiguration.current.screenHeightDp).dp),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.Top
                    ) {
                        Surface(
                            modifier = Modifier
                                .height(height = 96.dp)
                                .width(width = 48.dp),
                            shape = RoundedCornerShape(size = 24.dp),
                            color = Color.Transparent
                        ) {
                            Row(
                                modifier = Modifier
                                    .clickable(onClick = {
                                        isVisibleTwo = false
                                        coroutineScope.launch {
                                            delay(timeMillis = (0.5 * 1000).toLong())
                                            when (modelFeature) {
                                                1 -> when (modelStateOne.value) {
                                                    0 -> modelStateOne.value = 3
                                                    in 1..3 -> modelStateOne.value -= 1
                                                }
                                                2 -> when (modelStateTwo.value) {
                                                    0 -> modelStateTwo.value = 3
                                                    in 1..3 -> modelStateTwo.value -= 1
                                                }
                                            }
                                            isVisibleTwo = true
                                        }
                                    }),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.chevron_compact_left),
                                    contentDescription = null,
                                    modifier = Modifier.size(56.dp)
                                )
                            }
                        }
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = (0.44 * LocalConfiguration.current.screenHeightDp).dp),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.Top
                    ) {
                        Surface(
                            modifier = Modifier
                                .height(height = 96.dp)
                                .width(width = 48.dp),
                            shape = RoundedCornerShape(size = 24.dp),
                            color = Color.Transparent
                        ) {
                            Row(
                                modifier = Modifier
                                    .clickable(onClick = {
                                        isVisibleTwo = false
                                        coroutineScope.launch {
                                            delay(timeMillis = (0.5 * 1000).toLong())
                                            when (modelFeature) {
                                                1 -> when (modelStateOne.value) {
                                                    in 0..2 -> modelStateOne.value += 1
                                                    3 -> modelStateOne.value = 0
                                                }
                                                2 -> when (modelStateTwo.value) {
                                                    in 0..2 -> modelStateTwo.value += 1
                                                    3 -> modelStateTwo.value = 0
                                                }
                                            }
                                            isVisibleTwo = true
                                        }
                                    }),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.chevron_compact_right),
                                    contentDescription = null,
                                    modifier = Modifier.size(56.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
        AnimatedVisibility(
            visible = isVisibleOne,
            enter = fadeIn(animationSpec = tween(
                durationMillis = (0.25 * 1000).toInt(),
                easing = LinearEasing
            )),
            exit = fadeOut(animationSpec = tween(
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
                    var text = ""
                    when (modelFeature) {
                        0 -> text = "First, prepare a 6-inch diameter\nbanana stem cut for the base."
                        1 -> text = "Next, choose a banana leaf\nfolding style."
                        2 -> text = "Then, pick a blessing flower\nto decorate."
                        3 -> text = "Finally, add a candle and\nincense sticks."
                        4 -> text = "Your krathong in now ready to join\nthe Loy Krathong Festival."
                    }
                    Text(
                        text = text,
                        modifier = Modifier.align(alignment = Alignment.BottomStart),
                        color = colorResource(id = R.color.YELLOW),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
        AnimatedVisibility(
            visible = isVisibleTwo,
            enter = fadeIn(animationSpec = tween(
                durationMillis = (0.25 * 1000).toInt(),
                easing = LinearEasing
            )),
            exit = fadeOut(animationSpec = tween(
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
                    var text = ""
                    when (modelFeature) {
                        1 -> when (modelStateOne.value) {
                            0 -> text = "Lotus Petal"
                            1 -> text = "Lady Finger"
                            2 -> text = "Garuda Nail"
                            3 -> text = "Gardenia Roll"
                        }
                        2 -> when (modelStateTwo.value) {
                            0 -> text = "Marigold: for career and wealth"
                            1 -> text = "Rose: for love and desire"
                            2 -> text = "Orchid: for health and well-being"
                            3 -> text = "Lotus: for success and prosperity"
                        }
                        4 -> text = "Let's join in AR!"
                    }
                    Text(
                        text = text,
                        modifier = Modifier.align(alignment = Alignment.BottomStart),
                        color = colorResource(id = R.color.YELLOW)
                    )
                }
            }
        }
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(vertical = (0.14 * LocalConfiguration.current.screenHeightDp).dp)
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.Bottom
            ) {
                Surface(
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .height(height = 48.dp)
                        .width(width = 96.dp),
                    shape = RoundedCornerShape(size = 24.dp),
                    color = Color.Transparent,
                    border = BorderStroke(
                        width = 1.dp,
                        color = colorResource(id = R.color.YELLOW)
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .clickable(onClick = {
                                if (modelFeature != 0) {
                                    isVisibleOne = false
                                    isVisibleTwo = false
                                    coroutineScope.launch {
                                        delay(timeMillis = (0.5 * 1000).toLong())
                                        isVisibleOne = true
                                        modelFeature = 0
                                    }
                                }
                            }),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_delete_outline_24),
                            contentDescription = null,
                            tint = colorResource(id = R.color.YELLOW)
                        )
                    }
                }
                Surface(
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .height(height = 48.dp)
                        .width(width = 96.dp),
                    shape = RoundedCornerShape(size = 24.dp),
                    color = colorResource(id = R.color.YELLOW)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .clickable(onClick = {
                                when (modelFeature) {
                                    in 0..3 -> {
                                        isVisibleOne = false
                                        isVisibleTwo = false
                                        coroutineScope.launch {
                                            delay(timeMillis = (0.5 * 1000).toLong())
                                            isVisibleOne = true
                                            isVisibleTwo = true
                                            modelFeature += 1
                                        }
                                    }
                                    4 -> navController.navigate(route = "Activity 3")
                                }
                            }),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        var painter: Painter = painterResource(id = R.drawable.baseline_chevron_right_24)
                        when (modelFeature) {
                            0 -> painter = painterResource(id = R.drawable.baseline_chevron_right_24)
                            in 1..3 -> painter = painterResource(id = R.drawable.baseline_check_24)
                            4 -> painter = painterResource(id = R.drawable.baseline_view_in_ar_24)
                        }
                        Icon(
                            painter = painter,
                            contentDescription = null,
                            tint = Color.Black
                        )
                    }
                }
            }
        }
    }
}