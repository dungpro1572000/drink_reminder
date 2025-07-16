package com.dungz.drinkreminder.ui.onboarding

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dungz.drinkreminder.activity.MainActivityViewModel
import com.dungz.drinkreminder.ui.theme.BoldTitleTextStyle
import com.dungz.drinkreminder.ui.theme.LocalBaseColorScheme
import com.dungz.drinkreminder.ui.theme.NormalTextStyle
import com.dungz.drinkreminder.ui.theme.TextInPrimaryButton
import com.dungz.drinkreminder.ui.widget.DefaultButton
import com.dungz.drinkreminder.ui.widget.ListIndicatorWidget
import kotlinx.coroutines.launch

@Composable
fun OnboardingScreen(viewModel: MainActivityViewModel, onStartMain: () -> Unit = {}) {

    val isInFirstAppState = viewModel.isInApp.collectAsState()
    Log.d("DungNT354", "isInFirstAppState: ${isInFirstAppState}")
    if (!isInFirstAppState.value) {
        onStartMain.invoke()
    }
    Box(
        Modifier
            .fillMaxSize()
            .background(color = LocalBaseColorScheme.current.whiteColor)
    ) {
        val pagerState = rememberPagerState(
            initialPage = 0,
            pageCount = { 3 },
        )
        val scope = rememberCoroutineScope()

        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            contentPadding = PaddingValues(0.dp),
            pageSize = PageSize.Fill,

            verticalAlignment = Alignment.CenterVertically,
        ) { page ->
            BoardingScreen(OnBoardingState.defaultList[pagerState.currentPage]) {
                if (page == 2) {
                    onStartMain.invoke()
                } else {
                    scope.launch {
                        pagerState.animateScrollToPage(pagerState.currentPage + 1)
                    }
                }
            }
        }
    }
}

@Composable
fun BoardingScreen(state: OnBoardingState, onButtonClick: () -> Unit) {
    Column(
        Modifier
            .padding(horizontal = 48.dp, vertical = 48.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 4.dp),
            text = stringResource(state.title), style = BoldTitleTextStyle
        )
        Text(
            text = stringResource(state.content),
            style = NormalTextStyle,
            textAlign = TextAlign.Center
        )
        ListIndicatorWidget(
            currentIndex = state.currentPage,
            index = OnBoardingState.defaultList.size
        )
        DefaultButton(modifier = Modifier.fillMaxWidth(), onClick = onButtonClick) {
            Text("Next", style = TextInPrimaryButton)
        }
    }
}