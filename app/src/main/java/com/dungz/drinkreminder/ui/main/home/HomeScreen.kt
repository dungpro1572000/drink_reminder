package com.dungz.drinkreminder.ui.main.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dungz.drinkreminder.R
import com.dungz.drinkreminder.ui.theme.TitleTextStyle
import com.dungz.drinkreminder.ui.theme.blueBackgroundColor
import com.dungz.drinkreminder.ui.theme.primaryButtonColor
import com.dungz.drinkreminder.ui.theme.whiteColor
import com.dungz.drinkreminder.ui.widget.InformationCard
import com.dungz.drinkreminder.ui.widget.OnlyClickButton

@Composable
fun HomeScreen() {
    Column(
        Modifier
            .fillMaxSize()
            .background(blueBackgroundColor)
            .padding(horizontal = 24.dp, vertical = 16.dp)
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

        InformationCard(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp),
            startHour = 16,
            startMinute = 25,
            onTimeEnd = {},
            rightComponent = {
                Image(modifier = Modifier.size(128.dp), painter = painterResource(R.drawable.water_drop), contentDescription = "")
            },
            inCardButton = {
                OnlyClickButton(text = "Check this", onClick = {})
            }
        )
    }
}

@Preview
@Composable
fun testHome() {
    HomeScreen()
}