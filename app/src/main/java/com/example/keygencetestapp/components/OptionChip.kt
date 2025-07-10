package com.example.keygencetestapp.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import compose.icons.TablerIcons
import compose.icons.tablericons.Check
import compose.icons.tablericons.Unlink

@Composable
fun OptionFilter(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isSelected: Boolean,
) {

    val chipShape = if (isSelected) {
        MaterialTheme.shapes.medium
    } else {
        FilterChipDefaults.shape
    }

    FilterChip(
        selected = isSelected,
        onClick = { onClick() },
        shape = chipShape,
        label = {
                Text(label)
        },
        leadingIcon = {
            Crossfade(
                targetState = isSelected,
                animationSpec = spring(),
                label = "IconCrossfade"
            ) { selected ->
                if (selected) {
                    Icon(
                        imageVector = TablerIcons.Check,
                        contentDescription = "Selected",
                        Modifier.size(FilterChipDefaults.IconSize)
                    )
                } else {
                    Icon(
                        imageVector = icon,
                        contentDescription = "Not Selected",
                        Modifier.size(FilterChipDefaults.IconSize)
                    )
                }
            }
        },
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun OptionFilterPreview() {
    OptionFilter(
        icon = TablerIcons.Unlink,
        label = "Sample Option",
        onClick = { /*TODO*/ },
        isSelected = false
    )
}

@Preview(showBackground = true)
@Composable
fun OptionFilterSelectedPreview() {
    OptionFilter(
        icon = TablerIcons.Unlink,
        label = "Sample Option",
        onClick = { /*TODO*/ },
        isSelected = true
    )
}

