package com.skeletonviewer.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.skeletonviewer.presentation.viewmodels.SettingsViewModel
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onNavigateBack: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val config by viewModel.config.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.resetToDefaults() }) {
                        Icon(Icons.Default.Refresh, "Reset to defaults")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item { Spacer(modifier = Modifier.height(8.dp)) }

            item {
                SettingsSection(title = "Display") {
                    SwitchSetting(
                        label = "Show Skeleton",
                        description = "Draw body connection lines",
                        checked = config.showSkeleton,
                        onCheckedChange = viewModel::updateShowSkeleton
                    )
                    HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
                    SwitchSetting(
                        label = "Show Landmarks",
                        description = "Draw body landmark points",
                        checked = config.showLandmarks,
                        onCheckedChange = viewModel::updateShowLandmarks
                    )
                    HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
                    SwitchSetting(
                        label = "Show FPS",
                        description = "Display frame rate counter",
                        checked = config.showFps,
                        onCheckedChange = viewModel::updateShowFps
                    )
                    HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
                    SwitchSetting(
                        label = "Show Labels",
                        description = "Display landmark name labels",
                        checked = config.showLabels,
                        onCheckedChange = viewModel::updateShowLabels
                    )
                }
            }

            item {
                SettingsSection(title = "Appearance") {
                    ColorPickerSetting(
                        label = "Skeleton Color",
                        currentColor = Color(config.skeletonColor),
                        colors = PRESET_SKELETON_COLORS,
                        onColorSelected = viewModel::updateSkeletonColor
                    )
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                    SliderSetting(
                        label = "Line Width",
                        value = config.lineWidth,
                        valueRange = 1f..10f,
                        steps = 8,
                        displayValue = "${config.lineWidth.roundToInt()}px",
                        onValueChange = viewModel::updateLineWidth
                    )
                    HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
                    SliderSetting(
                        label = "Landmark Size",
                        value = config.landmarkRadius,
                        valueRange = 4f..20f,
                        steps = 15,
                        displayValue = "${config.landmarkRadius.roundToInt()}px",
                        onValueChange = viewModel::updateLandmarkRadius
                    )
                }
            }

            item {
                SettingsSection(title = "Detection") {
                    SliderSetting(
                        label = "Detection Confidence",
                        value = config.detectionConfidence,
                        valueRange = 0.1f..0.9f,
                        steps = 7,
                        displayValue = "${(config.detectionConfidence * 100).roundToInt()}%",
                        onValueChange = viewModel::updateDetectionConfidence
                    )
                    HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
                    SliderSetting(
                        label = "Tracking Confidence",
                        value = config.trackingConfidence,
                        valueRange = 0.1f..0.9f,
                        steps = 7,
                        displayValue = "${(config.trackingConfidence * 100).roundToInt()}%",
                        onValueChange = viewModel::updateTrackingConfidence
                    )
                }
            }

            item {
                SettingsSection(title = "Camera") {
                    SwitchSetting(
                        label = "Default Front Camera",
                        description = "Start with front-facing camera",
                        checked = config.defaultFrontCamera,
                        onCheckedChange = viewModel::updateDefaultFrontCamera
                    )
                }
            }

            item { Spacer(modifier = Modifier.height(32.dp)) }
        }
    }
}

@Composable
private fun SettingsSection(
    title: String,
    content: @Composable () -> Unit
) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 8.dp)
        )
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                content()
            }
        }
    }
}

@Composable
private fun SwitchSetting(
    label: String,
    description: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = label, style = MaterialTheme.typography.bodyLarge)
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}

@Composable
private fun SliderSetting(
    label: String,
    value: Float,
    valueRange: ClosedFloatingPointRange<Float>,
    steps: Int,
    displayValue: String,
    onValueChange: (Float) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = label, style = MaterialTheme.typography.bodyLarge)
            Text(
                text = displayValue,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
        }
        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = valueRange,
            steps = steps,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun ColorPickerSetting(
    label: String,
    currentColor: Color,
    colors: List<Long>,
    onColorSelected: (Long) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = label, style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            colors.forEach { colorLong ->
                val color = Color(colorLong)
                val isSelected = currentColor == color
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(color)
                        .border(
                            width = if (isSelected) 3.dp else 0.dp,
                            color = Color.White,
                            shape = CircleShape
                        )
                        .clickable { onColorSelected(colorLong) }
                )
            }
        }
    }
}

private val PRESET_SKELETON_COLORS = listOf(
    0xFF00FF41L,
    0xFF00E5FFL,
    0xFFFF4444L,
    0xFFFFEB3BL,
    0xFFE040FBL,
    0xFFFF9800L,
    0xFFFFFFFFL
)
