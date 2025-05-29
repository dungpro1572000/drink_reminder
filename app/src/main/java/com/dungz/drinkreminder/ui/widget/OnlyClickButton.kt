package com.dungz.drinkreminder.ui.widget

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dungz.drinkreminder.ui.theme.TextInPrimaryButton
import com.dungz.drinkreminder.ui.theme.TitleTextStyle
import com.dungz.drinkreminder.ui.theme.normalTextColor
import com.dungz.drinkreminder.ui.theme.whiteColor

@Composable
fun OnlyClickButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    text: String = "Check this"
) {
    Button(
        modifier = modifier,
        onClick = onClick, shape = RoundedCornerShape(25.dp), colors = ButtonDefaults.buttonColors(
            contentColor = normalTextColor, containerColor = whiteColor
        )
    ) {
        Text(text, style = TitleTextStyle)
    }
}