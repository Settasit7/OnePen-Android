package com.settasit.onepen

import android.animation.ValueAnimator
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.os.Build
import android.os.Environment
import android.os.Handler
import android.os.HandlerThread
import android.provider.MediaStore
import android.view.PixelCopy
import android.view.SurfaceView
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import androidx.compose.animation.*
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
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
import java.io.File
import java.io.IOException

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
    val context = LocalContext.current
    val engine = rememberEngine()
    val materialLoader = rememberMaterialLoader(engine = engine)
    val modelInstances = remember { mutableListOf<ModelInstance>() }
    val modelLoader = rememberModelLoader(engine = engine)
    var arView by remember { mutableStateOf<View?>(value = null) }
    var frame by remember { mutableStateOf<Frame?>(value = null) }
    var isVisibleOne by remember { mutableStateOf(value = false) }
    var isVisibleTwo by remember { mutableStateOf(value = false) }
    var planeRenderer by remember { mutableStateOf(value = false) }
    LaunchedEffect(key1 = Unit) {
        delay(timeMillis = (4.5 * 1000).toLong())
        isVisibleOne = true
    }
    LaunchedEffect(key1 = Unit) {
        delay(timeMillis = (5.5 * 1000).toLong())
        isVisibleTwo = true
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
        }),
        onViewUpdated = {
            arView = this
        }
    )
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
                .padding(vertical = (0.32 * LocalConfiguration.current.screenHeightDp).dp)
                .width(width = 216.dp)
            ) {
                Text(
                    text = "Tap anywhere to place your AR krathong.",
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
    AnimatedVisibility(
        visible = isVisibleOne,
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
                            val surfaceView = arView?.let { findSurfaceView(it) }
                            surfaceView?.let {
                                captureAndSaveScreenshot(context = context, arView = it)
                            }
                        }),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_camera_alt_24),
                        contentDescription = null,
                        tint = Color.Black
                    )
                }
            }
        }
    }
}

fun findSurfaceView(view: View): SurfaceView? {
    if (view is SurfaceView) {
        return view
    }
    if (view is ViewGroup) {
        for (i in 0 until view.childCount) {
            val surfaceView = findSurfaceView(view.getChildAt(i))
            if (surfaceView != null) {
                return surfaceView
            }
        }
    }
    return null
}

fun captureAndSaveScreenshot(context: Context, arView: SurfaceView) {
    val bitmap = Bitmap.createBitmap(arView.width, arView.height, Bitmap.Config.ARGB_8888)
    val handlerThread = HandlerThread("PixelCopier").apply { start() }
    PixelCopy.request(arView, bitmap, { result ->
        if (result == PixelCopy.SUCCESS) {
            try {
                saveBitmapToDisk(context, bitmap)
            } catch (_: IOException) {

            }
        }
        handlerThread.quitSafely()
    }, Handler(handlerThread.looper))
}

fun saveBitmapToDisk(context: Context, bitmap: Bitmap) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        val resolver = context.contentResolver
        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "AR_Screenshot_${System.currentTimeMillis()}.jpg")
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/Screenshots")
        }
        val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
        uri?.let { param ->
            resolver.openOutputStream(param).use { outputStream ->
                outputStream?.let {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
                }
            }
        }
    } else {
        val picturesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        val screenshotFile = File(picturesDir, "AR_Screenshot_${System.currentTimeMillis()}.jpg")
        screenshotFile.outputStream().use {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
            MediaScannerConnection.scanFile(context, arrayOf(screenshotFile.absolutePath), null, null)
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
        }.removeAt(modelInstances.apply {
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
        }.lastIndex),
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