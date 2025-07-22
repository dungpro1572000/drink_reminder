package com.dungz.drinkreminder.ui.main.home

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.dungz.drinkreminder.R
import com.dungz.drinkreminder.framework.receiver.AlarmReceiver
import com.dungz.drinkreminder.framework.sync.alarm.setUpAlarm
import com.dungz.drinkreminder.ui.theme.BoldTitleTextStyle
import com.dungz.drinkreminder.ui.theme.InstructionTextStyle
import com.dungz.drinkreminder.ui.theme.LocalBaseColorScheme
import com.dungz.drinkreminder.ui.theme.NormalTextStyle
import com.dungz.drinkreminder.ui.theme.TextInPrimaryButton
import com.dungz.drinkreminder.ui.theme.TitleTextStyle
import com.dungz.drinkreminder.ui.widget.DefaultButton
import com.dungz.drinkreminder.ui.widget.InformationCard
import com.dungz.drinkreminder.ui.widget.InformationCardType
import com.dungz.drinkreminder.utilities.AppConstant
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    navigateToPage: () -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val showBottomSheetState = rememberSaveable {
        mutableStateOf(true)
    }
    val homeScreenState = viewModel.homeScreenState.collectAsState()
    val bottomSheetState = rememberModalBottomSheetState()

    LaunchedEffect(Unit) {
        coroutineScope.launch {

        }
    }
    if (showBottomSheetState.value) {
        ModalBottomSheet(
            onDismissRequest = { showBottomSheetState.value = false },
            sheetState = bottomSheetState,
        ) {
            Box(modifier = Modifier.padding(start = 12.dp)) {
                Image(
                    painter = painterResource(R.drawable.icon_confetti),
                    contentDescription = null,
                    modifier = Modifier
                        .align(
                            Alignment.TopEnd
                        )
                        .zIndex(2f),
                )
                Image(
                    painterResource(R.drawable.icon_trophy),
                    contentDescription = null,
                    modifier = Modifier
                        .align(
                            Alignment.BottomStart
                        )
                        .graphicsLayer(rotationZ = 25f)
                        .zIndex(2f)
                )
                Column(
                    Modifier
                        .padding(top = 50.dp)
                        .fillMaxWidth()
                        .height(340.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(LocalBaseColorScheme.current.secondaryColor)
                        .padding(top = 45.dp, end = 25.dp, start = 12.dp)
                        .zIndex(1f)
                ) {
                    Text("Congratulation!", style = BoldTitleTextStyle)
                    Spacer(Modifier.height(24.dp))
                    Text(
                        "You have completed this, keep it up!",
                        style = TitleTextStyle
                    )
                    Spacer(Modifier.height(12.dp))
                    Text(
                        "You can get everything in life you want if you will just help enough other people get what they want.",
                        style = NormalTextStyle
                    )
                }
            }
        }
    }
    Column(
        Modifier
            .fillMaxSize()
            .background(LocalBaseColorScheme.current.blueBackgroundColor)
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
                    containerColor = LocalBaseColorScheme.current.whiteColor,
                    contentColor = LocalBaseColorScheme.current.primaryColor
                ),
                onClick = {
                    val intent = Intent(context, AlarmReceiver::class.java).apply {
                        action = AppConstant.ALARM_ACTION_RECEIVER
                        `package` = "com.dungz.drinkreminder"
                    }
                    context.setUpAlarm(intent = intent)
                },
            ) { Icon(Icons.Filled.Notifications, contentDescription = "") }

        }
        Spacer(Modifier.height(24.dp))
        InformationCard(
            buttonInCardClicked = {
                showBottomSheetState.value = true
                viewModel.saveRecordExercise()
            },
            type = InformationCardType.EXERCISE,
            incomingTime = homeScreenState.value.exerciseTime,
            timeLeft = homeScreenState.value.exerciseTimeLeft,
            icon = {
                Icon(
                    modifier = Modifier.padding(12.dp),
                    painter = painterResource(R.drawable.heart_beat),
                    tint = LocalBaseColorScheme.current.primaryColor,
                    contentDescription = null
                )
            },
            isChecked = homeScreenState.value.isCheckedExercise,
        )
        Spacer(Modifier.height(12.dp))
        InformationCard(
            buttonInCardClicked = {
                showBottomSheetState.value = true
                viewModel.saveRecordDrink()

            },
            type = InformationCardType.DRINK,
            icon = {
                Icon(
                    modifier = Modifier.padding(12.dp),
                    painter = painterResource(R.drawable.water_drop),
                    tint = LocalBaseColorScheme.current.primaryColor,
                    contentDescription = null
                )
            },
            incomingTime = homeScreenState.value.drinkTime,
            timeLeft = homeScreenState.value.drinkTimeLeft,
            isChecked = homeScreenState.value.isCheckedDrink
        )
        Spacer(Modifier.height(12.dp))
        InformationCard(
            type = InformationCardType.RELAX_EYES,
            buttonInCardClicked = {
                showBottomSheetState.value = true
                viewModel.saveRecordEyesRelax()
            },
            icon = {
                Icon(
                    modifier = Modifier.padding(12.dp),
                    painter = painterResource(R.drawable.icon_eyes),
                    tint = LocalBaseColorScheme.current.primaryColor,
                    contentDescription = null
                )
            },
            isChecked = homeScreenState.value.isCheckedEyes,
            incomingTime = homeScreenState.value.eyesRelaxTime,
            timeLeft = homeScreenState.value.eyesRelaxTimeLeft
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