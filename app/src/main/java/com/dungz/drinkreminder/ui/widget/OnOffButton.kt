package com.dungz.drinkreminder.ui.widget

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.dungz.drinkreminder.ui.theme.disableButtonColor
import com.dungz.drinkreminder.ui.theme.primaryButtonColor
import com.dungz.drinkreminder.ui.theme.textInPrimaryButtonColor

enum class ButtonState {
    On, Off
}

data class DefaultButtonState(
    val buttonState: ButtonState = ButtonState.On,
    val isEnable: Boolean = true,
)

@Composable
fun DefaultButton(
    modifier: Modifier = Modifier,
    state: DefaultButtonState = DefaultButtonState(),
    onClick: () -> Unit = {},
    content: @Composable () -> Unit,
) {
    Button(
        modifier = modifier, onClick = onClick, colors = ButtonDefaults.buttonColors(
            containerColor = if (state.buttonState == ButtonState.On) primaryButtonColor else disableButtonColor,
            contentColor = textInPrimaryButtonColor
        )
    ) {
        content()
    }
}