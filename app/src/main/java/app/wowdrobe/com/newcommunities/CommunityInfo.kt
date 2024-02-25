package app.wowdrobe.com.newcommunities

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.wowdrobe.com.communities.CommunitiesViewModel
import app.wowdrobe.com.ui.theme.CardColor
import app.wowdrobe.com.ui.theme.CardTextColor
import app.wowdrobe.com.ui.theme.appBackground
import app.wowdrobe.com.ui.theme.textColor
import coil.compose.AsyncImage

@Composable
fun CommunityInfo(communitiesViewModel: CommunitiesViewModel) {

    Column(modifier = Modifier
        .fillMaxHeight(0.96f)
        .fillMaxWidth()
        .verticalScroll(rememberScrollState())
        .background(appBackground)
        ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 15.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = communitiesViewModel.communitiesTitle.value,
                color = textColor,
                fontSize = 30.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 15.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically

        ) {
//        ProfileImage(
//            imageUrl = community[page].image,
//            modifier = Modifier
//                .fillMaxWidth()
//                .weight(1f)
//                .fillMaxHeight(if (viewModel.expandedState.value < 0.9f) 0.35f else 1f)
//                .clip(RoundedCornerShape(30.dp))
////                                            .draggable(
////                                                state = draggableState,
////                                                orientation = Orientation.Vertical,
////                                                startDragImmediately = true,
////                                            )
//                .then(
//                    if (viewModel.expandedState.value < 0.9f)
//                        Modifier
//                    else Modifier.rotate(-90f)
//                ),
//            contentScale = ContentScale.FillBounds,
//        )

            AsyncImage(
                model = communitiesViewModel.communititesImage.value,
                contentDescription = "",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .size(250.dp)

            )

        }


        Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp)) {

            CommunityColumnCard(text = "Location", text2 = communitiesViewModel.communitiesLocation.value)
            CommunityColumnCard(text = "Time", text2 = communitiesViewModel.communitiesTime.value)

            Column(modifier = Modifier.padding(vertical = 10.dp)) {
                Text(text = "Description", fontSize = 15.sp)
                Text(
                    communitiesViewModel.communitesDescription.value,
                    fontSize = 15.sp
                )
            }

        }

        Card(
            modifier = Modifier
                .padding(horizontal = 110.dp),
            shape = RoundedCornerShape(25.dp),
            backgroundColor = CardColor,
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = "Register",
                    modifier = Modifier.padding(vertical = 12.dp, horizontal = 3.dp),
                    color = CardTextColor
                )


            }
        }
        Spacer(modifier = Modifier.height(100.dp))
    }


}


@Composable
fun CommunityColumnCard(text: String, text2: String) {

    Column(modifier = Modifier.padding(vertical = 6.dp)) {
        Text(text = text, fontSize = 15.sp)
        Text(text = text2, fontSize = 22.sp)
    }
}