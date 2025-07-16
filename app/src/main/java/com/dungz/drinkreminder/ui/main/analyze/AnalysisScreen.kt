package com.dungz.drinkreminder.ui.main.analyze

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel

enum class AnalysisType {
    EYES, DRINK, EXERCISE
}

@Composable
fun AnalysisScreen(type: AnalysisType, viewModel: AnalysisViewModel = hiltViewModel()) {
    LaunchedEffect(Unit) {
        viewModel.loadData()
    }

    val state = viewModel.recordDataFlow.collectAsState()
    Column {
        state.value.forEach {
            Text("Data :${it.date} ${it.exerciseTime} ${it.drinkTime} ${it.eyesRelaxTime}")
        }
    }
}