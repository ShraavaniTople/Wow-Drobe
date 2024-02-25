package app.wowdrobe.com.tags

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.with
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.wowdrobe.com.reportwaste.ReportWasteViewModel
import app.wowdrobe.com.ui.theme.CardTextColor
import app.wowdrobe.com.ui.theme.appBackground
import app.wowdrobe.com.ui.theme.monteBold
import app.wowdrobe.com.ui.theme.textColor
import coil.compose.AsyncImage

@OptIn(ExperimentalFoundationApi::class, ExperimentalAnimationApi::class)
@Composable
fun TagsScreen(reportWasteViewModel: ReportWasteViewModel) {
    val searchText by reportWasteViewModel.searchText.collectAsState()
    val tags by reportWasteViewModel.tags.collectAsState()
    val isSearching by reportWasteViewModel.isSearching.collectAsState()
    val seconds by reportWasteViewModel.tagsSearch.collectAsState(initial = "")
    var isTyping by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    val lazyListState = rememberLazyListState()
    val isAtTop by remember {
        derivedStateOf {
            lazyListState.firstVisibleItemIndex == 0 &&
                    lazyListState.firstVisibleItemScrollOffset == 0
        }
    }

    LaunchedEffect(key1 = isAtTop) {
        if (!isAtTop) {
            focusManager.clearFocus()
            isTyping = false
            try {
                lazyListState.animateScrollToItem(0)
            } catch (e: Exception) {

            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxHeight(0.95f)
            .fillMaxWidth()
            .background(appBackground)
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = 35.dp,
                        bottom = 20.dp,
                        start = 10.dp,
                        end = 20.dp
                    ),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier
                        .padding(top = 0.dp, start = 0.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Tag it",
                            color = textColor,
                            fontSize = 25.sp,
                            fontFamily = monteBold,
                        )
                        Spacer(modifier = Modifier.height(5.dp))
                        Text(
                            text = "Help us identify what you are selling",
                            color = Color.LightGray,
                            fontSize = 13.sp,
                            fontFamily = monteBold,
                        )
                    }
                    Spacer(modifier = Modifier.width(20.dp))
                    AnimatedVisibility(
                        visible = reportWasteViewModel.tagsList.size > 0,
                        enter = slideInHorizontally(
                            initialOffsetX = { it },
                            animationSpec = tween(durationMillis = 500)
                        ) + fadeIn(),
                        exit = slideOutVertically(
                            targetOffsetY = { it },
                            animationSpec = tween(durationMillis = 500)
                        ) + fadeOut()
                    ) {
                        if (reportWasteViewModel.tagsList.size > 0) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                AsyncImage(
                                    reportWasteViewModel.tagsList[0].image,
                                    modifier = Modifier
                                        .size(50.dp)
                                        .padding(2.dp),
                                    contentScale = ContentScale.Crop,
                                    contentDescription = "",
                                )
                                Spacer(modifier = Modifier.width(5.dp))
                                Text(
                                    text = if (reportWasteViewModel.tagsList.size > 1) {
                                        "Mixed Type"
                                    } else {
                                        reportWasteViewModel.tagsList[0].name.substringBefore(" ")
                                    },
                                    color = Color.LightGray,
                                    fontSize = 13.sp,
                                    fontFamily = monteBold,
                                )
                                Spacer(modifier = Modifier.width(5.dp))
                            }
                        }
                    }
//                Card(
//                    backgroundColor = CardColor,
//                    shape = RoundedCornerShape(10.dp),
//                    modifier = Modifier.padding(end = 10.dp)
//                ) {
//                }
                }
            }
            Spacer(modifier = Modifier.height(5.dp))
            TextField(
                value = searchText,
                onValueChange = {
                    reportWasteViewModel.onSearchTextChange(it)
                    isTyping = true
                },
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = appBackground,
                    focusedIndicatorColor = appBackground,
                    unfocusedIndicatorColor = appBackground,
                    disabledIndicatorColor = textColor,
                    errorIndicatorColor = textColor,
                ),
                label = {
                    if (!isTyping && searchText == "") {
                        AnimatedContent(
                            targetState = seconds ,
                            transitionSpec = {
                                slideIntoContainer(
                                    towards = AnimatedContentScope.SlideDirection.Up,
                                    animationSpec = tween(durationMillis = 500)
                                ) + fadeIn() with slideOutOfContainer(
                                    towards = AnimatedContentScope.SlideDirection.Up,
                                    animationSpec = tween(durationMillis = 500)
                                ) + fadeOut()
                            }, label = ""
                        ) { targetCount ->
                            Text(
                                text = "Search $targetCount",
                                color = textColor,
                                fontSize = 20.sp,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .align(Alignment.CenterHorizontally)
                            )

                        }
                    }
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = "Search",
                        tint = CardTextColor
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester)
                    .height(80.dp)
                    .padding(horizontal = 10.dp)
                    .shadow(50.dp, shape = RoundedCornerShape(10.dp)),
                maxLines = 1,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Search
                )
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        if (isSearching) {
            Box(modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(10.dp),
                modifier = Modifier,
                state = lazyListState
            ) {
                items(tags, key = {
                    it.name
                }) { group ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp)
                            .animateItemPlacement()
                    ) {
                        Text(
                            text = group.name,
                            color = textColor,
                            fontSize = 15.sp,
                            fontFamily = monteBold,
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        LazyRow(
                            contentPadding = PaddingValues(10.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            items(group.tags) { tag ->
                                TagItem(
                                    item = tag,
                                    modifier = Modifier
                                        .clickable(
                                            interactionSource = remember { MutableInteractionSource() },
                                            indication = null,
                                            onClick = {
                                                if (reportWasteViewModel.tagsList.contains(tag)) {
                                                    reportWasteViewModel.tagsList.remove(tag)
                                                } else {
                                                    reportWasteViewModel.tagsList.add(tag)
                                                }
                                            }
                                        ),
                                    isSelected = reportWasteViewModel.tagsList.contains(tag)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                }
            }

        }
    }
}


@Composable
fun TagItem(
    modifier: Modifier = Modifier,
    item: Tag,
    isSelected: Boolean = false
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .padding(end = 7.dp)
    ) {
        AsyncImage(
            item.image,
            modifier = Modifier
                .size(50.dp)
                .padding(2.dp),
            contentScale = ContentScale.Crop,
            contentDescription = "",
            colorFilter = if (isSelected)
                ColorFilter.tint(
                    textColor
                ) else null
        )
        Spacer(modifier = Modifier.width(5.dp))
        Column(verticalArrangement = Arrangement.Center) {
            Text(
                text = item.name.substringBefore(" "),
                color = if (isSelected)
                    textColor
                else
                    Color.Gray,
                fontSize = 12.sp,
                softWrap = true
            )
            Spacer(modifier = Modifier.height(2.dp))
            if (item.name.contains(" ")) {
                Text(
                    text = item.name.substringAfter(" "),
                    color = if (isSelected)
                        textColor
                    else
                        Color.Gray,
                    fontSize = 12.sp,
                    softWrap = true
                )
            }
        }
    }
}


