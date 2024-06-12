package com.settasit.onepen

import android.animation.ValueAnimator
import android.view.animation.LinearInterpolator
import androidx.compose.animation.*
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import coil.size.Size
import com.google.android.filament.Engine
import com.google.ar.core.Anchor
import com.google.ar.core.Config
import com.google.ar.core.Frame
import dev.romainguy.kotlin.math.Float3
import io.github.sceneview.*
import io.github.sceneview.ar.*
import io.github.sceneview.ar.arcore.createAnchorOrNull
import io.github.sceneview.ar.arcore.isValid
import io.github.sceneview.ar.node.AnchorNode
import io.github.sceneview.loaders.MaterialLoader
import io.github.sceneview.loaders.ModelLoader
import io.github.sceneview.model.ModelInstance
import io.github.sceneview.node.CubeNode
import io.github.sceneview.node.ModelNode
import kotlinx.coroutines.delay

@Composable
fun Activity_3(
    modelValueOne: Int,
    modelValueTwo: Int
) {
    val modelEndValue = 4 * modelValueOne + modelValueTwo
    Surface(color = MaterialTheme.colors.background) {
        Box(modifier = Modifier.fillMaxSize()) {
            ARScreen(modelEndValue = modelEndValue)
            GifImageWithControl()
        }
    }
}

@Composable
fun ARScreen(modelEndValue: Int) {
    val childNodes = rememberNodes()
    val engine = rememberEngine()
    val materialLoader = rememberMaterialLoader(engine = engine)
    val modelInstances = remember { mutableListOf<ModelInstance>() }
    val modelLoader = rememberModelLoader(engine = engine)
    var frame by remember { mutableStateOf<Frame?>(value = null) }
    var isVisible by remember { mutableStateOf(value = false) }
    var planeRenderer by remember { mutableStateOf(value = false) }
    LaunchedEffect(key1 = Unit) {
        delay(timeMillis = (5.0 * 1000).toLong())
        isVisible = true
    }
    ARScene(
        modifier = Modifier.fillMaxSize(),
        engine = engine,
        modelLoader = modelLoader,
        sessionConfiguration = { session, config ->
            config.depthMode = when (session.isDepthModeSupported(Config.DepthMode.AUTOMATIC)) {
                true -> Config.DepthMode.AUTOMATIC
                else -> Config.DepthMode.DISABLED
            }
            config.lightEstimationMode = Config.LightEstimationMode.DISABLED
        },
        planeRenderer = planeRenderer,
        childNodes = childNodes,
        onSessionUpdated = { _, updatedFrame ->
            frame = updatedFrame
        },
        onGestureListener = rememberOnGestureListener(onSingleTapConfirmed = { motionEvent, node ->
            node?.destroy()
            if (node == null) {
                val hitResults = frame?.hitTest(
                    motionEvent.x,
                    motionEvent.y
                )
                hitResults?.firstOrNull {
                    it.isValid(
                        point = false,
                        depthPoint = false
                    )
                }?.createAnchorOrNull()
                ?.let { anchor ->
                    planeRenderer = false
                    childNodes += createAnchorNode(
                        anchor = anchor,
                        engine = engine,
                        materialLoader = materialLoader,
                        modelEndValue = modelEndValue,
                        modelInstances = modelInstances,
                        modelLoader = modelLoader
                    )
                }
            }
        })
    )
    AnimatedVisibility(
        visible = isVisible && childNodes.isEmpty(),
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
                Text(
                    text = "Tap anywhere to place your\nAR krathong.",
                    modifier = Modifier.align(alignment = Alignment.BottomStart),
                    color = colorResource(id = R.color.YELLOW),
                    fontWeight = FontWeight.Bold
                )
            }
        }
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
                    text = "Be free to capture a screenshot.",
                    modifier = Modifier.align(alignment = Alignment.BottomStart),
                    color = colorResource(id = R.color.YELLOW)
                )
            }
        }
    }
}

fun createAnchorNode(
    anchor: Anchor,
    engine: Engine,
    materialLoader: MaterialLoader,
    modelEndValue: Int,
    modelInstances: MutableList<ModelInstance>,
    modelLoader: ModelLoader
): AnchorNode {
    val anchorNode = AnchorNode(
        engine = engine,
        anchor = anchor
    )
    val modelNode = ModelNode(
        modelInstance = modelInstances.apply {
            if (isEmpty()) {
                var assetFileLocation = ""
                when (modelEndValue) {
                    0 -> assetFileLocation = "models/model-1-1-1.glb"
                    1 -> assetFileLocation = "models/model-1-1-2.glb"
                    2 -> assetFileLocation = "models/model-1-1-3.glb"
                    3 -> assetFileLocation = "models/model-1-1-4.glb"
                    4 -> assetFileLocation = "models/model-1-2-1.glb"
                    5 -> assetFileLocation = "models/model-1-2-2.glb"
                    6 -> assetFileLocation = "models/model-1-2-3.glb"
                    7 -> assetFileLocation = "models/model-1-2-4.glb"
                    8 -> assetFileLocation = "models/model-1-3-1.glb"
                    9 -> assetFileLocation = "models/model-1-3-2.glb"
                    10 -> assetFileLocation = "models/model-1-3-3.glb"
                    11 -> assetFileLocation = "models/model-1-3-4.glb"
                    12 -> assetFileLocation = "models/model-1-4-1.glb"
                    13 -> assetFileLocation = "models/model-1-4-2.glb"
                    14 -> assetFileLocation = "models/model-1-4-3.glb"
                    15 -> assetFileLocation = "models/model-1-4-4.glb"
                }
                this += modelLoader.createInstancedModel(
                    assetFileLocation = assetFileLocation,
                    count = 1
                )
            }
        }.removeLast(),
        scaleToUnits = 0.25f
    ).apply {
        isEditable = true
    }
    val boundingBoxNode = CubeNode(
        engine = engine,
        size = modelNode.extents,
        center = modelNode.center,
        materialInstance = materialLoader.createColorInstance(color = Color.White.copy(alpha = 0f))
    ).apply {
        isVisible = false
    }
    modelNode.addChildNode(node = boundingBoxNode)
    anchorNode.addChildNode(node = modelNode)
    listOf(
        modelNode,
        anchorNode
    ).forEach {
        it.onEditingChanged = { editingTransforms ->
            boundingBoxNode.isVisible = editingTransforms.isNotEmpty()
        }
    }
    val rotateAnimator = ValueAnimator.ofFloat(
        0f,
        360f
    ).apply {
        duration = (30.0 * 1000).toLong()
        interpolator = LinearInterpolator()
        repeatCount = ValueAnimator.INFINITE
        addUpdateListener { valueAnimator ->
            val rotationAngle = valueAnimator.animatedValue as Float
            modelNode.rotation = Float3(
                x = 0f,
                y = rotationAngle,
                z = 0f
            )
        }
    }
    val translateYAnimator = ValueAnimator.ofFloat(
        0f,
        0.02f
    ).apply {
        duration = (1.0 * 1000).toLong()
        repeatCount = ValueAnimator.INFINITE
        repeatMode = ValueAnimator.REVERSE
        addUpdateListener { valueAnimator ->
            val translateY = valueAnimator.animatedValue as Float
            modelNode.position = Float3(
                x = modelNode.position.x,
                y = translateY,
                z = modelNode.position.z
            )
        }
    }
    translateYAnimator.start()
    rotateAnimator.start()
    return anchorNode
}

@Composable
fun GifImageWithControl() {
    var gifVisible by remember { mutableStateOf(false) }
    LaunchedEffect(key1 = Unit) {
        delay((1.0 * 1000).toLong())
        gifVisible = true
        delay((3.0 * 1000).toLong())
        gifVisible = false
    }
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        AnimatedVisibility(
            visible = gifVisible,
            enter = fadeIn(animationSpec = tween(durationMillis = (0.5 * 1000).toInt())),
            exit = fadeOut(animationSpec = tween(durationMillis = (0.5 * 1000).toInt()))
        ) {
            GifImage()
        }
    }
}

@Composable
fun GifImage(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val imageLoader = ImageLoader.Builder(context).components {
            add(ImageDecoderDecoder.Factory())
        }.build()
    Image(
        painter = rememberAsyncImagePainter(ImageRequest.Builder(context).data(data = R.drawable.coaching).apply(
            block = {
                size(Size.ORIGINAL)
            }).build(),
            imageLoader = imageLoader
        ),
        contentDescription = null,
        modifier = modifier.fillMaxWidth(),
    )
}