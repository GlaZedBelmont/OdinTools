package de.langerhans.odintools.appsettings

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import de.langerhans.odintools.R
import de.langerhans.odintools.models.ControllerStyle.*
import de.langerhans.odintools.models.L2R2Style.*
import de.langerhans.odintools.models.NoChange
import de.langerhans.odintools.ui.composables.DeleteConfirmDialog
import de.langerhans.odintools.ui.composables.LargeDropdownMenu
import de.langerhans.odintools.ui.composables.OdinTopAppBar

@Composable
fun AppOverridesScreen(
    viewModel: AppOverridesViewModel = hiltViewModel(),
    navigateBack: () -> Unit
) {
    val uiState: AppOverridesUiModel by viewModel.uiState.collectAsState()
    val app = uiState.app ?: return run {
        // Shouldn't happen
    }

    Scaffold(topBar = { OdinTopAppBar(deviceVersion = uiState.deviceVersion) }) { contentPadding ->
        LaunchedEffect(uiState.navigateBack) {
            if (uiState.navigateBack) navigateBack()
        }

        if (uiState.showDeleteConfirmDialog) {
            DeleteConfirmDialog(
                onDelete = { viewModel.deleteConfirmed() },
                onDismiss = { viewModel.deleteDismissed() }
            )
        }

        Row(
            modifier = Modifier
                .fillMaxHeight()
                .padding(contentPadding)
                .padding(16.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .weight(0.3f)
                    .align(Alignment.CenterVertically)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = rememberDrawablePainter(drawable = app.appIcon),
                            contentDescription = null,
                            modifier = Modifier
                                .size(72.dp)
                                .padding(bottom = 8.dp)
                        )
                        Text(
                            text = app.appName,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }
                }
                Button(
                    onClick = { viewModel.saveClicked() },
                    enabled = uiState.hasUnsavedChanges,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                ) {
                    Text(text = stringResource(id = R.string.save))
                }
                FilledTonalButton(
                    onClick = navigateBack,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = if (uiState.isNewApp) 0.dp else 8.dp)
                ) {
                    Text(text = stringResource(id = R.string.cancel))
                }
                if (uiState.isNewApp.not()) {
                    OutlinedButton(
                        onClick = { viewModel.deleteClicked() },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            color = Color.Red,
                            text = stringResource(id = R.string.deleteOverride)
                        )
                    }
                }
            }
            Divider(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxHeight()
                    .width(1.dp)
            )
            Column(
                modifier = Modifier.weight(0.7f)
            ) {
                OverrideSpinnerRow(
                    label = R.string.controllerStyle,
                    spinnerItems = listOf(
                        NoChange.KEY to stringResource(id = NoChange.textRes),
                        Odin.id to stringResource(id = Odin.textRes),
                        Xbox.id to stringResource(id = Xbox.textRes),
                        Disconnect.id to stringResource(id = Disconnect.textRes),
                    ),
                    initialSelection = uiState.app?.controllerStyle?.id ?: NoChange.KEY,
                    onSelectionChanged = { viewModel.controllerStyleSelected(it) },
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                OverrideSpinnerRow(
                    label = R.string.l2r2mode,
                    spinnerItems = listOf(
                        NoChange.KEY to stringResource(id = NoChange.textRes),
                        Analog.id to stringResource(id = Analog.textRes),
                        Digital.id to stringResource(id = Digital.textRes),
                        Both.id to stringResource(id = Both.textRes),
                    ),
                    initialSelection = uiState.app?.l2r2Style?.id ?: NoChange.KEY,
                    onSelectionChanged = { viewModel.l2R2StyleSelected(it) },
                )
            }
        }
    }
}

@Composable
fun Spinner(
    options: List<Pair<String, String>>,
    initialSelection: String,
    onSelectionChanged: (key: String) -> Unit
) {
    val initial = options.indexOfFirst { it.first == initialSelection }.coerceAtLeast(0)
    var selectedIndex by remember { mutableIntStateOf(initial) }

    LargeDropdownMenu(
        items = options,
        onItemSelected = { index: Int, item: Pair<String, String> ->
            selectedIndex = index
            onSelectionChanged(item.first)
        },
        selectedIndex = selectedIndex,
        selectedItemToString = { it.second }
    )
}

@Composable
fun OverrideSpinnerRow(
    @StringRes label: Int,
    spinnerItems: List<Pair<String, String>>,
    initialSelection: String,
    onSelectionChanged: (key: String) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Text(
            text = stringResource(id = label),
            modifier = Modifier
                .weight(1f)
                .padding(end = 16.dp)
        )
        Spinner(
            options = spinnerItems,
            initialSelection = initialSelection,
            onSelectionChanged = onSelectionChanged
        )
    }
}
