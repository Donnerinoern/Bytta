package prj.edu.bytta.Chat

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.rememberSwipeableState
import androidx.compose.material.swipeable
import androidx.compose.material3.Card
import androidx.compose.material3.CardElevation
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollDispatcher
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import prj.edu.bytta.UserRow
import prj.edu.bytta.xd
import kotlin.math.roundToInt
import prj.edu.bytta.R

class KontakterView {

    @Composable
    fun søkIKontakter() {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp, horizontal = 20.dp)
                .clip(RoundedCornerShape(percent = 35))
                .padding(15.dp)
        ) {
            Icon(
                Icons.Default.Search,
                contentDescription = null,
                modifier = Modifier.padding(10.dp)
            )
            Text(stringResource(R.string.search_contacts))
        }
    }

    @SuppressLint("RememberReturnType")
    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    fun Kontakter(navController: NavController) {
        val swipeState = rememberSwipeableState(1f)
        val onNewDelta: (Float) -> Float = {
            delta -> swipeState.performDrag(delta)
        }
        val anchor = mapOf(0f to 0f)
        val nestedScrollDispatcher = remember { NestedScrollDispatcher()}
        val nestedScrollConnection = remember {
            object: NestedScrollConnection {
                override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                    if (available.y > 0) {
                        return Offset.Zero
                    }
                    val vertical = available.y
                    val weConsumed = onNewDelta(vertical)
                    return Offset(x = 0f, y = weConsumed)
                }

                override fun onPostScroll(
                    consumed: Offset,
                    available: Offset,
                    source: NestedScrollSource
                ): Offset {
                    if (available.y < 0) {
                        return Offset.Zero
                    }
                    val vertical = available.y
                    val weConsumed = onNewDelta(vertical)
                    return Offset(x = 0f, y = weConsumed)
                }
                override suspend fun onPostFling(
                    consumed: Velocity,
                    available: Velocity,
                ): Velocity {
                    swipeState.performFling(available.y)
                    return available
                }
            }
        }
        val progress: Float = (swipeState.offset.value)
        Box(
            Modifier
                .fillMaxWidth()
                .nestedScroll(nestedScrollConnection, nestedScrollDispatcher)
                .swipeable(
                    state = swipeState,
                    orientation = Orientation.Vertical,
                    thresholds = { _, _ -> FractionalThreshold(0.4f) },
                    anchors = anchor
                )
                .background(Color.DarkGray)
        ) {
            Column(
              Modifier.alpha(progress)
            ){
                søkIKontakter()
                Toolbar(onButtonClicked = {navController.navigate("messages")})
            }
            val currentOffset: Dp = with(LocalDensity.current) {
                swipeState.offset.value.roundToInt().coerceAtLeast(0).toDp()
            }
            KontaktListe(currentOffset)
        }
    }

    @Composable
    private fun Toolbar(onButtonClicked: () -> Unit) {
        Row(
            Modifier
                .padding(top = 15.dp)
                .padding(horizontal = 20.dp, vertical = 15.dp)
        ) {
            Text(
                stringResource(R.string.contacts_name),
                fontSize = 20.sp,
                modifier = Modifier.weight(1f, fill = true),
                fontWeight = FontWeight.Medium
            )
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(5.dp))
                    .size(25.dp)
                    .background(Color.Blue)
                    .clickable(onClick = onButtonClicked),
                contentAlignment = Alignment.Center,
            ) {
                Icon(Icons.Default.Add, null)
            }
        }
    }
    @Composable
    fun KontaktListe(topOffset: Dp) {
        val users = remember { xd(size = 5)}
        val state: LazyListState = rememberLazyListState()


        LazyColumn(
            state = state,
            modifier = Modifier
                .fillMaxWidth(),
            contentPadding = PaddingValues(vertical = 20.dp, horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(users) { user ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),

                ) {
                    UserRow(user)
                }
            }
        }
    }
}
