package com.example.keygencetestapp.pages.HomePage.components

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.keygencetestapp.R

@Composable
fun UserProfile(modifier: Modifier = Modifier) {
    Surface {
        Box {
            Icon(imageVector = ImageVector.vectorResource(R.drawable.heroicons_inbox_arrow_down), null)

        }
    }
}

@Preview
@Composable
fun UserProfilePreview() {
    Surface {
        UserProfile()
    }
}