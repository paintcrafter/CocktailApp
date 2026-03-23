package pl.solvro.cocktails.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import pl.solvro.cocktails.ui.theme.ThemeMode
import pl.solvro.cocktails.viewmodel.ThemeViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun SettingsScreen(
    themeViewModel: ThemeViewModel,
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Ustawienia motywu") },
                navigationIcon = {
                    FilledTonalIconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Wróć"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Wybierz motyw aplikacji")

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = themeViewModel.themeMode == ThemeMode.SYSTEM,
                    onClick = { themeViewModel.changeThemeMode(ThemeMode.SYSTEM) },
                    label = { Text("Systemowy") }
                )

                FilterChip(
                    selected = themeViewModel.themeMode == ThemeMode.LIGHT,
                    onClick = { themeViewModel.changeThemeMode(ThemeMode.LIGHT) },
                    label = { Text("Jasny") }
                )

                FilterChip(
                    selected = themeViewModel.themeMode == ThemeMode.DARK,
                    onClick = { themeViewModel.changeThemeMode(ThemeMode.DARK) },
                    label = { Text("Ciemny") }
                )
            }
        }
    }
}