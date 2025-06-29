package com.dungz.drinkreminder.ui.widget

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dungz.drinkreminder.R
import com.dungz.drinkreminder.ui.theme.NormalTextStyle
import com.dungz.drinkreminder.ui.theme.TitleTextStyle
import com.dungz.drinkreminder.ui.theme.blueBackgroundColor
import com.dungz.drinkreminder.ui.theme.borderColor
import com.dungz.drinkreminder.utilities.convertTimeStringToInts
import com.dungz.drinkreminder.utilities.countdownFlow
import java.time.LocalTime

enum class InformationCardType {
    DRINK, RELAX_EYES, EXERCISE
}

@Composable
fun InformationCard(
    modifier: Modifier = Modifier,
    type: InformationCardType,
    onCardClick: () -> Unit = {},
    onTimeEnd: () -> Unit,
    startTime: String = "",
    icon: @Composable () -> Unit = {},
    buttonInCardClicked: () -> Unit = {},
) {
    val startTime = startTime.convertTimeStringToInts()
    val invokeTime = LocalTime.of(startTime.first, startTime.second)
    val timeLeft = countdownFlow(startTime.first, startTime.second).collectAsState(initial = 0)

    if (timeLeft.value == 0) {
        onTimeEnd.invoke()
    }
    Card(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 12.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = blueBackgroundColor
        ),
        modifier = modifier
            .height(148.dp)
            .fillMaxWidth()
            .clickable {
                onCardClick.invoke()
            }
            .border(0.75.dp, color = borderColor, shape = RoundedCornerShape(15.dp)),
        shape = RoundedCornerShape(15.dp)
    ) {
        Column {

            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(start = 6.dp, top = 12.dp)
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                icon()
                Column(
                    Modifier
                        .padding(start = 24.dp, top = 12.dp)
                ) {
                    Text(
                        text =
                            when (type) {
                                InformationCardType.DRINK -> stringResource(R.string.drink_incoming)
                                InformationCardType.EXERCISE -> stringResource(R.string.exercise_incoming)
                                InformationCardType.RELAX_EYES -> stringResource(R.string.eye_incoming)
                            }, style = TitleTextStyle
                    )
                    Spacer(Modifier.height(8.dp))
                    Text("In coming $invokeTime", style = TitleTextStyle)
                    Spacer(Modifier.height(8.dp))
                    Text("${timeLeft.value}", style = NormalTextStyle)
                }
            }
            Box(
                Modifier
                    .padding(bottom = 12.dp, end = 12.dp)
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                OnlyClickButton(
                    modifier = Modifier.align(Alignment.BottomEnd),
                    text = "Check this",
                    onClick = buttonInCardClicked,
                )
            }
        }
    }
}

@Preview
@Composable
fun tess() {
//    InformationCard(Modifier.height(160.dp), {}, startMinute = 20, startHour = 16, onTimeEnd = {})
    InformationCard(
        type = InformationCardType.DRINK,
        onTimeEnd = { },
    )

}
