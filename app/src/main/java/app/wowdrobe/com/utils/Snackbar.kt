package app.wowdrobe.com.utils

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.wowdrobe.com.ui.theme.CardColor
import app.wowdrobe.com.ui.theme.appBackground
import app.wowdrobe.com.ui.theme.textColor

@Composable
fun DefaultSnackbar(
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit = { }
) {
    SnackbarHost(
        hostState = snackbarHostState,
        snackbar = { data ->
            Snackbar(
                modifier = Modifier.padding(16.dp),
                content = {
                    Text(
                        text = data.message,
                        style = MaterialTheme.typography.body2,
                        color = appBackground
                    )
                },
                action = {
                    data.actionLabel?.let { actionLabel ->
                        TextButton(onClick = onDismiss, colors = ButtonDefaults.buttonColors(
                            backgroundColor = CardColor
                        )) {
                            Text(
                                text = actionLabel,
                                color = appBackground,
                                style = MaterialTheme.typography.body2,
                            )
                        }
                    }
                },
                backgroundColor = textColor.copy(0.8f),
                elevation = 10.dp,
                shape = RoundedCornerShape(10.dp)
            )
        },
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(Alignment.Bottom)
    )
}