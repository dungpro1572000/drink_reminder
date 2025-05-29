import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun SimpleEye() {
    Canvas(modifier = Modifier.size(200.dp)) {
        val centerX = size.width / 2
        val centerY = size.height / 2

        // Draw the eyeball (white part)
        drawCircle(
            color = Color.White,
            center = Offset(centerX, centerY),
            radius = 80.dp.toPx(),
            style = Stroke(width = 2.dp.toPx()) // Outline for the eyeball
        )

        // Draw the iris (colored part)
        drawCircle(
            color = Color.Blue, // You can change this color
            center = Offset(centerX, centerY),
            radius = 40.dp.toPx()
        )

        // Draw the pupil (black part)
        drawCircle(
            color = Color.Black,
            center = Offset(centerX, centerY),
            radius = 20.dp.toPx()
        )

        // Optional: Draw a highlight on the pupil for shine
        drawCircle(
            color = Color.White,
            center = Offset(centerX + 10.dp.toPx(), centerY - 10.dp.toPx()),
            radius = 5.dp.toPx()
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SimpleEyePreview() {
    SimpleEye()
}