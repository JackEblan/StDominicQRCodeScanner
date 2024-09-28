package st.dominic.qrcodescanner.core.designsystem.component

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import st.dominic.qrcodescanner.R

@Composable
fun ShimmerImage(
    modifier: Modifier = Modifier,
    imageUrl: String?,
) {
    var isLoading by remember { mutableStateOf(true) }

    var isError by remember { mutableStateOf(false) }

    val imageLoader = rememberAsyncImagePainter(
        model = imageUrl,
        onState = { state ->
            isLoading = state is AsyncImagePainter.State.Loading
            isError = state is AsyncImagePainter.State.Error
        },
    )
    val isLocalInspection = LocalInspectionMode.current

    val transition = rememberInfiniteTransition(label = "Transition")

    val lightGrayAnimation by transition.animateColor(
        initialValue = Color.LightGray.copy(alpha = 0.2f),
        targetValue = Color.LightGray,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 800),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "LightGrayAnimation",
    )

    Image(
        modifier = modifier.drawBehind {
            drawRect(
                color = if (isLoading) lightGrayAnimation else Color.Transparent,
                size = size,
            )
        },
        contentScale = ContentScale.Crop,
        painter = if (isError.not() && isLocalInspection.not()) {
            imageLoader
        } else {
            painterResource(R.drawable.ic_placeholder)
        },
        contentDescription = null,
    )
}
