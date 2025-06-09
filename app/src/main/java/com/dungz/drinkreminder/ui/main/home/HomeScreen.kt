package com.dungz.drinkreminder.ui.main.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.compose.LottieAnimation
import com.dungz.drinkreminder.R
import com.dungz.drinkreminder.ui.theme.InstructionTextStyle
import com.dungz.drinkreminder.ui.theme.TextInPrimaryButton
import com.dungz.drinkreminder.ui.theme.TitleTextStyle
import com.dungz.drinkreminder.ui.theme.blueBackgroundColor
import com.dungz.drinkreminder.ui.theme.primaryButtonColor
import com.dungz.drinkreminder.ui.theme.whiteColor
import com.dungz.drinkreminder.ui.widget.DefaultButton
import com.dungz.drinkreminder.ui.widget.InformationCard
import com.dungz.drinkreminder.ui.widget.InformationCardType
import com.dungz.drinkreminder.ui.widget.LottieComponent
import com.dungz.drinkreminder.ui.widget.OnlyClickButton
import com.dungz.drinkreminder.ui.widget.ScaleComponent

@Composable
fun HomeScreen(
    navigateToPage: () -> Unit
) {
    Column(
        Modifier
            .fillMaxSize()
            .background(blueBackgroundColor)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Hello", style = TitleTextStyle)
            IconButton(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(45.dp)),
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = whiteColor,
                    contentColor = primaryButtonColor
                ),
                onClick = {},
            ) { Icon(Icons.Filled.Notifications, contentDescription = "") }

        }
        Spacer(Modifier.height(24.dp))
        InformationCard(
            modifier = Modifier
                .fillMaxWidth()
                .background(primaryButtonColor)
                .height(160.dp),
            startHour = 16,
            startMinute = 25,
            onTimeEnd = {},
            rightComponent = {
                ScaleComponent(
                    modifier = Modifier.size(78.dp),
                    animationDuration = 2500,
                    content = {
                        Image(
                            painter = painterResource(R.drawable.heart_beat),
                            contentDescription = ""
                        )
                    })
            },
            type = InformationCardType.EXERCISE,
            inCardButton = {
                OnlyClickButton(text = "Check this", onClick = {})
            }
        )
        Spacer(Modifier.height(12.dp))
        InformationCard(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp),
            startHour = 16,
            startMinute = 25,
            onTimeEnd = {},
            rightComponent = {
                Image(
                    modifier = Modifier.size(78.dp),
                    painter = painterResource(R.drawable.water_drop),
                    contentDescription = ""
                )
            },
            type = InformationCardType.DRINK,
            inCardButton = {
                OnlyClickButton(text = "Check this", onClick = {})
            }
        )
        Spacer(Modifier.height(12.dp))
        InformationCard(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp),
            startHour = 16,
            startMinute = 25,
            onTimeEnd = {},
            rightComponent = {
                LottieComponent(
                    modifier = Modifier
                        .height(78.dp)
                        .width(78.dp),
                    resId = R.raw.lottie_eye
                )
            },
            type = InformationCardType.RELAX_EYES,
            inCardButton = {
                OnlyClickButton(text = "Check this", onClick = {})
            }
        )
        Spacer(Modifier.height(24.dp))
        DefaultButton(
            modifier = Modifier
                .width(194.dp),
            onClick = {
                navigateToPage.invoke()
            }) {
            Text("Set up Time", style = TextInPrimaryButton)
        }
        Spacer(Modifier.height(12.dp))
        Text(
            text = "Keep your chaledge , keep your mind, focus on your health",
            style = InstructionTextStyle
        )
    }
}

@Preview
@Composable
fun testHome() {
    HomeScreen({})
}