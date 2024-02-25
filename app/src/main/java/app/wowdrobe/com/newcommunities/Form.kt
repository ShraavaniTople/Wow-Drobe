package app.wowdrobe.com.newcommunities

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddHomeWork
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.wowdrobe.com.communities.ui.RegistrationForm
import app.wowdrobe.com.firebase.firestore.updateCommunitiesToFirebase
import app.wowdrobe.com.login.TextFieldWithIcons
import app.wowdrobe.com.ui.theme.CardColor
import app.wowdrobe.com.ui.theme.CardTextColor
import app.wowdrobe.com.ui.theme.appBackground
import app.wowdrobe.com.ui.theme.textColor
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Form(modalSheetState: ModalBottomSheetState) {
    val context = LocalContext.current

    var name by remember {
        mutableStateOf(TextFieldValue(""))
    }
    var location by remember {
        mutableStateOf(TextFieldValue(""))
    }
    var number by remember {
        mutableStateOf(TextFieldValue(""))
    }
    var date by remember {
        mutableStateOf(TextFieldValue(""))
    }
    var time by remember {
        mutableStateOf(TextFieldValue(""))
    }
    var organised by remember {
        mutableStateOf(TextFieldValue(""))
    }
    var description by remember {
        mutableStateOf(TextFieldValue(""))
    }
    var motivation by remember {
        mutableStateOf(TextFieldValue(""))
    }



    Column(
        modifier = Modifier
            .fillMaxHeight(0.95f)
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
                text = "Add an Event",
                color = textColor,
                fontSize = 30.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 15.dp, vertical = 15.dp)
        ) {
            Text(text = "Name of the community")

            TextFieldWithIcons(
                textValue = "Name",
                placeholder = "Name",
                icon = Icons.Filled.AddHomeWork,
                mutableText = name,
                onValueChanged = {
                    name = it
                },
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Go,
            )

            Text(text = "Location")
            TextFieldWithIcons(
                textValue = "Location",
                placeholder = "Location",
                icon = Icons.Filled.AddHomeWork,
                mutableText = location,
                onValueChanged = {
                    location = it
                },
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Go,
            )


            Text(text = "Date")
            TextFieldWithIcons(
                textValue = "Date",
                placeholder = "Date",
                icon = Icons.Filled.AddHomeWork,
                mutableText = date,
                onValueChanged = {
                    date = it
                },
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Go,
            )

            Text(text = "Time")
            TextFieldWithIcons(
                textValue = "Time",
                placeholder = "Time",
                icon = Icons.Filled.AddHomeWork,
                mutableText = time,
                onValueChanged = {
                    time = it
                },
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Go,
            )

            Text(text = "Organised by")
            TextFieldWithIcons(
                textValue = "Organised by",
                placeholder = "Organised by",
                icon = Icons.Filled.AddHomeWork,
                mutableText = organised,
                onValueChanged = {
                    organised = it
                },
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Go,
            )

            Text(text = "Description")
            TextFieldWithIcons(
                textValue = "Description",
                placeholder = "Description",
                icon = Icons.Filled.AddHomeWork,
                mutableText = description,
                onValueChanged = {
                    description = it
                },
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Go,
            )

            Text(text = "Why  do you want to be a part?")
            TextFieldWithIcons(
                textValue = "Motivation",
                placeholder = "Motivation",
                icon = Icons.Filled.AddHomeWork,
                mutableText = motivation,
                onValueChanged = {
                    motivation = it
                },
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Go,
            )

            val coroutineScope = rememberCoroutineScope()
            Card(
                modifier = Modifier
                    .padding(horizontal = 110.dp)
                    .clickable(interactionSource = MutableInteractionSource(), indication = null) {
                        if (name.text.isEmpty() || location.text.isEmpty() || date.text.isEmpty() || time.text.isEmpty() || organised.text.isEmpty() || description.text.isEmpty() || motivation.text.isEmpty()) {
                            return@clickable
                        }
                        updateCommunitiesToFirebase(
                            RegistrationForm(
                                dateOfEstablishment = date.text,
                                activeRegion = location.text,
                                name = name.text,
                                description = description.text,
                                motivation = motivation.text,
                                time = time.text,
                                location = location.text
                            )
                        )
                        coroutineScope.launch {
                            if (modalSheetState.isVisible){
                                modalSheetState.hide()
                            }
                            date = TextFieldValue("")
                            location = TextFieldValue("")
                            name = TextFieldValue("")
                            description = TextFieldValue("")
                            motivation = TextFieldValue("")
                            time = TextFieldValue("")
                            organised = TextFieldValue("")


                        }

                    },
                shape = RoundedCornerShape(25.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = CardColor),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(
                        text = "Submit",
                        color = CardTextColor,
                        modifier = Modifier.padding(vertical = 12.dp, horizontal = 3.dp)
                    )


                }
            }
        }


        Spacer(modifier = Modifier.height(100.dp))
    }
}