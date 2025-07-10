package com.example.keygencetestapp.pages.stockOutPage.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Badge
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.keygencetestapp.R
import com.example.keygencetestapp.database.entities.ProcessOrderStatus


@Composable
fun StatusBadge(
    modifier: Modifier = Modifier, status: ProcessOrderStatus,
    content: @Composable () -> Unit = {}
) {
    // Background Color -> Content Color
    val colors: Pair<Color, Color> = when (status) {
        ProcessOrderStatus.COMPLETED -> Color(0xFFE8FFF3) to Color(0xFF17B963)
        else -> Color(0xFFFFF4E5) to Color(0xFFFF8800)
    }
    Badge(
        modifier = modifier
            .padding(top = 8.dp)
            .background(colors.first, shape = RoundedCornerShape(12.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp),
        containerColor = Color.Transparent,
        contentColor = colors.second
    ) {
        content()
    }
}

@Composable
@Preview
fun NewStatus(modifier: Modifier = Modifier) {
    StatusBadge(status = ProcessOrderStatus.NEW, modifier = modifier) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(2.dp)) {
            Icon(painterResource(R.drawable.bolt_24dp_1f1f1f_fill0_wght400_grad0_opsz24), null, Modifier.size(20.dp))
            Text("New")
        }
    }
}

@Composable
@Preview
fun CompletedStatus(modifier: Modifier = Modifier) {
    StatusBadge(status = ProcessOrderStatus.COMPLETED, modifier = modifier) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(2.dp)) {
            Icon(painterResource(R.drawable.check_24dp_1f1f1f_fill0_wght600_grad0_opsz24), null,Modifier.size(20.dp))
            Spacer(Modifier.width(2.dp))
            Text("Completed")
        }
    }
}

@Composable
@Preview
fun PreviewBadge() {
    Surface {
        StatusBadge(status = ProcessOrderStatus.NEW){
            Icon(painterResource(R.drawable.calendar_edit), null)
            Text("Jejej")
        }
    }
}