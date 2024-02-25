package app.wowdrobe.com.communities.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.wowdrobe.com.communities.CommunitiesViewModel
import app.wowdrobe.com.communities.data.Drives
import app.wowdrobe.com.ui.theme.CardTextColor

val cleanlinessDrives = listOf(
    Drives("2023-09-15 09:00 AM", "City Park", "Park Cleanup Drive"),
    Drives("2023-09-20 10:30 AM", "Riverfront Park", "River Cleanup Campaign"),
    Drives("2023-09-25 08:45 AM", "Beachfront Area", "Beach Cleanup Initiative"),
    Drives("2023-10-02 09:15 AM", "Local School Grounds", "School Yard Cleanup"),
    Drives("2023-10-10 11:00 AM", "Community Center", "Community Cleanup Event"),
    Drives("2023-10-15 10:00 AM", "City Streets", "Street Cleaning Drive"),
    Drives("2023-10-20 03:00 PM", "Local Park", "Green Park Restoration"),
    Drives("2023-11-05 02:30 PM", "Lakefront Area", "Lake Cleanup Day"),
    Drives("2023-11-10 09:45 AM", "Residential Area", "Neighborhood Cleanup"),
    Drives("2023-11-15 08:00 AM", "Wildlife Reserve", "Wildlife Conservation Drive"),
    Drives("2023-12-01 10:15 AM", "Historical Site", "Heritage Site Cleanup"),
    Drives("2023-12-05 11:30 AM", "Botanical Garden", "Botanical Garden Restoration"),
    Drives("2023-12-10 08:30 AM", "Local Riverbank", "Riverbank Cleanup Initiative"),
    Drives("2023-12-20 03:45 PM", "City Square", "Downtown Cleanup Day"),
    Drives("2023-12-25 10:45 AM", "Local Zoo", "Zoo Conservation Drive")
)


@Composable
fun LazyCard(
    list: List<Drive>,
    viewModel: CommunitiesViewModel
) {
    LazyColumn(
        contentPadding = PaddingValues(
            top = 30.dp,
            bottom = 100.dp,
            start = 10.dp,
            end = 10.dp
        )
    ) {
        items(list) {
            CardItem(it)
        }
    }
}

@Composable
fun CardItem(drives: Drive? = null) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = 16.dp,
                end = 20.dp,
                top = 8.dp,
                bottom = 8.dp
            ),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(horizontalAlignment = Alignment.Start) {
            Text(
                text = drives?.name ?: "",
                fontSize = 15.sp,
                color = CardTextColor,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Row {
                Text(
                    text = drives?.location ?: "",
                    fontSize = 13.sp,
                    color = CardTextColor,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Spacer(modifier = Modifier.width(7.dp))
                Text(
                    text = "• ${drives?.status?.name}",
                    fontSize = 13.sp,
                    color = if (drives?.status == DriveStatus.UPCOMING) Color(0xFF8BC34A)
                    else Color(0xFFF05353),
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
            }
        }
        Text(
            text = drives?.time.toString(),
            fontSize = 10.sp,
            color = CardTextColor,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 4.dp),
            softWrap = true
        )
    }
}