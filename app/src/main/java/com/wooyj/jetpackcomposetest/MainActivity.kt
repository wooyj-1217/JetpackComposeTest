package com.wooyj.jetpackcomposetest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DocumentScanner
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.*
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import androidx.constraintlayout.compose.*
import coil.compose.rememberImagePainter
import com.wooyj.jetpackcomposetest.ui.theme.JetpackComposeTestTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JetpackComposeTestTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    LayoutMain()
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    JetpackComposeTestTheme {
        LayoutMain()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LayoutMain() {
    Scaffold(
        topBar = { HeaderContent() }
    ) { innerPadding ->
        BodyContent(
            Modifier
                .padding(innerPadding)
                .padding(8.dp)
        )
    }

}


@Composable
fun HeaderContent() {
    SmallTopAppBar(
        title = {
            Text(text = "?????????")
        },
        actions = {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(Icons.Filled.Favorite, contentDescription = null)
            }
        }
    )
}


@Composable
fun BodyContent(modifier: Modifier = Modifier) {
//    Column(modifier = modifier.padding(8.dp)) {
//        Text("column")
//        Text("column")
//        Text("column")
//        Text("column")
//        Text("column")
//        Text("column")
////        SimpleList()
////        LazyList()
//    }
//    MyOwnColumn(modifier = modifier.padding(8.dp)) {
//        Text("MyOwnColumn")
//        Text("MyOwnColumn")
//        Text("MyOwnColumn")
//        Text("MyOwnColumn")
//        Text("MyOwnColumn")
//        Text("MyOwnColumn")
//
//    }
//    Row(modifier = modifier.horizontalScroll(rememberScrollState())) {
    Row(
        modifier = modifier
//            .background(color = Color.LightGray, shape = RectangleShape)
            .background(color = Color.LightGray)
            .padding(16.dp)
            .size(200.dp)
            .horizontalScroll(rememberScrollState())
    ) {
        StaggeredGrid {
            for (topic in topics) {
                Chip(modifier = Modifier.padding(8.dp), text = topic.first, count = topic.second)
            }
        }
    }

}

// List??? column, row??? ??????.
@Composable
fun SimpleList() {
    val scrollState = rememberScrollState()
    Column(Modifier.verticalScroll(scrollState)) {
        repeat(100) {
            Text("Item #$it")
        }
    }
}


// Jetpack Compose??? LazyColumn??? Android ?????? RecyclerView??? ??????.
@Composable
fun LazyList() {
//    val scrollState = rememberScrollState()
    val listSize = 100
    // state??? ????????? ?????? ??????????????????.
    val scrollState = rememberLazyListState()
    // ????????? ???????????? ???????????? ????????? ?????????
    val coroutineScope = rememberCoroutineScope()
//    Column(Modifier.verticalScroll(scrollState)) {
    Column {
        Row {
            Button(onClick = {
                coroutineScope.launch {
                    scrollState.animateScrollToItem(0)
                }
            }) {
                Text("????????? ?????????")
            }
            Button(onClick = {
                coroutineScope.launch {
                    scrollState.animateScrollToItem(listSize - 1)
                }
            }) {
                Text("????????? ?????????")
            }
        }
        LazyColumn(state = scrollState) {
//        repeat(100) {
            items(100) {
                // DSL : ???????????? ??????(DSL).
                // items : ?????? ????????? ????????? ????????? ??? ??????. LazyColumn?????? LazyRow?????? ?????????.
//            Text("Item #$it")
                ImageListItem(index = it)
            }
        }
    }
}

// ?????? RecyclerView??? ViewHolder ??????
@Composable
fun ImageListItem(index: Int) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Image(
            painter = rememberImagePainter(data = "https://developer.android.com/images/brand/Android_Robot.png"),
            contentDescription = "Android Logo",
            modifier = Modifier.size(50.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(text = "Item #$index", style = MaterialTheme.typography.titleSmall)
    }
}


// ???????????? ?????????
fun Modifier.firstBaselineToTop(
    firstBaselineToTop: Dp
) = this.then(
    layout { measurable, constraints ->

        val placeable = measurable.measure(constraints)

        // ??????????????? ????????? baseline??? ????????? ????????? ??????
        check(placeable[FirstBaseline] != AlignmentLine.Unspecified)
        val firstBaseline = placeable[FirstBaseline]

        // padding??? ????????? ?????? ??????????????? ????????? - ????????? baseline ???
        val placeableY = firstBaselineToTop.roundToPx() - firstBaseline
        val height = placeable.height + placeableY
        layout(placeable.width, height) {
            placeable.placeRelative(0, placeableY)
        }
    }
)

// BaselineHeight + paddingTop = 32dp
@Preview
@Composable
fun TextWithPaddingToBaselinePreview() {
    JetpackComposeTestTheme {
        Text("????????????", Modifier.firstBaselineToTop(32.dp))
    }
}

// paddingTop = 32dp
@Preview
@Composable
fun TextWithNormalPaddingPreview() {
    JetpackComposeTestTheme {
        Text("????????????", Modifier.padding(top = 32.dp))
    }
}


/**
 *
 * ?????? ???????????? ?????????.
 *
 * - Compose UI??? ?????? ?????? ????????? ??????. (???????????? ????????? ?????? ?????? ????????? ???????????? ?????? ?????? ????????? ??? ??? ?????? ????????? ?????? ??????.)
 * - ???????????? ????????? ??????????????? ????????? ?????? ??? ??????.. ??????, ?????? ??????..
 * - constraint ????????? ??????/?????? width, height??? ??????.
 *
 * */
@Composable
fun MyOwnColumn(modifier: Modifier, content: @Composable () -> Unit) {
    Layout(
        modifier = modifier,
        content = content
    ) { measurables, constraints ->

        val placeables = measurables.map { measurable ->
            measurable.measure(constraints)
        }

        var yPosition = 0

        layout(constraints.maxWidth, constraints.maxHeight) {
            // Place children
            placeables.forEach { placeable ->
                placeable.placeRelative(0, yPosition)
                yPosition += placeable.height
            }
        }
    }
}


/**
 *
 *  ????????? ?????? ???????????? ?????????
 *
 *
 *
 *
 */
@Composable
fun StaggeredGrid(
    modifier: Modifier = Modifier,
    rows: Int = 3,
    content: @Composable () -> Unit
) {
    Layout(
        modifier = modifier,
        content = content
    ) { measurables, constraints ->

        // Keep track of the width of each row
        val rowWidths = IntArray(rows) { 0 }

        // Keep track of the max height of each row
        val rowHeights = IntArray(rows) { 0 }

        // Don't constrain child views further, measure them with given constraints
        // List of measured children
        val placeables = measurables.mapIndexed { index, measurable ->
            // Measure each child
            val placeable = measurable.measure(constraints)

            // Track the width and max height of each row
            val row = index % rows
            rowWidths[row] += placeable.width
            rowHeights[row] = Math.max(rowHeights[row], placeable.height)

            placeable
        }

        // Grid's width is the widest row
        val width = rowWidths.maxOrNull()
            ?.coerceIn(constraints.minWidth.rangeTo(constraints.maxWidth)) ?: constraints.minWidth

        // Grid's height is the sum of the tallest element of each row
        // coerced to the height constraints
        val height = rowHeights.sumOf { it }
            .coerceIn(constraints.minHeight.rangeTo(constraints.maxHeight))

        // Y of each row, based on the height accumulation of previous rows
        val rowY = IntArray(rows) { 0 }
        for (i in 1 until rows) {
            rowY[i] = rowY[i - 1] + rowHeights[i - 1]
        }

        // Set the size of the parent layout
        layout(width, height) {
            // x co-ord we have placed up to, per row
            val rowX = IntArray(rows) { 0 }

            placeables.forEachIndexed { index, placeable ->
                val row = index % rows
                placeable.placeRelative(
                    x = rowX[row],
                    y = rowY[row]
                )
                rowX[row] += placeable.width
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Chip(modifier: Modifier = Modifier, text: String, count: Int) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(0.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(60.dp, 60.dp)
                    .background(color = MaterialTheme.colorScheme.secondary)
            )
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Text(
                    text = text, style = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                )
                Spacer(modifier = Modifier.height(2.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.DocumentScanner,
                        contentDescription = null,
                        Modifier.size(8.dp),
                        tint = Color.Gray
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = count.toString(), style = TextStyle(
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Light,
                            color = Color.Gray
                        )
                    )
                }
            }
        }
    }

}

@Preview
@Composable
fun ChipPreview() {
    JetpackComposeTestTheme {
        Chip(text = "?????????", count = 8)
    }
}

val topics = listOf(
    Pair("Arts & Crafts", 121),
    Pair("Beauty", 80),
    Pair("Books", 42),
    Pair("Business", 78),
    Pair("Comics", 30),
    Pair("Culinary", 118),
    Pair("Design", 45),
    Pair("Fashion", 92),
    Pair("Film", 23),
    Pair("History", 58),
    Pair("Maths", 90),
    Pair("Music", 222),
    Pair("People", 231),
    Pair("Philosophy", 20),
    Pair("Religion", 24),
    Pair("Social sciences", 37),
    Pair("Technology", 403),
    Pair("TV", 424),
    Pair("Writing", 34),
)


/**
 *
 * LayoutModifier.
 * >> ?????? ????????? ?????????.
 * Inheritance from an interface with '@JvmDefault' members is only allowed with -Xjvm-default option
 *
 */

//@Stable
//fun Modifier.padding(all: Dp) = this.then(
//    PaddingModifier(
//        start = all, top = all, end = all, bottom = all, rtlAware = true
//    )
//)
//
//// LayoutModifier??? ????????? private class
//// Implementation detail
//private class PaddingModifier(
//    val start: Dp = 0.dp,
//    val top: Dp = 0.dp,
//    val end: Dp = 0.dp,
//    val bottom: Dp = 0.dp,
//    val rtlAware: Boolean,
//) : LayoutModifier {
//
//    override fun MeasureScope.measure(
//        measurable: Measurable,
//        constraints: Constraints
//    ): MeasureResult {
//
//        val horizontal = start.roundToPx() + end.roundToPx()
//        val vertical = top.roundToPx() + bottom.roundToPx()
//
//        val placeable = measurable.measure(constraints.offset(-horizontal, -vertical))
//
//        val width = constraints.constrainWidth(placeable.width + horizontal)
//        val height = constraints.constrainHeight(placeable.height + vertical)
//        return layout(width, height) {
//            if (rtlAware) {
//                placeable.placeRelative(start.roundToPx(), top.roundToPx())
//            } else {
//                placeable.place(start.roundToPx(), top.roundToPx())
//            }
//        }
//    }
//}


@Composable
fun ConstraintLayoutContent() {
    ConstraintLayout {
//        val (button, text) = createRefs()
        val (button1, button2, text) = createRefs()

        Button(onClick = {}, modifier = Modifier.constrainAs(button1) {
            top.linkTo(parent.top, margin = 16.dp)
        }) {
            Text("??????1")
        }

        val barrier = createEndBarrier(button1, text)
        val chainRefs = createHorizontalChain(button1, button2, chainStyle = ChainStyle.Packed)
        constrain(chainRefs) {
            start.linkTo(parent.start)
            end.linkTo(parent.end)
        }

        Button(onClick = {}, modifier = Modifier.constrainAs(button2) {
            top.linkTo(parent.top, margin = 16.dp)
//            start.linkTo(barrier)

        }) {
            Text("??????2")
        }
        Text("?????????", Modifier.constrainAs(text) {
            top.linkTo(button1.bottom, margin = 16.dp)
            centerAround(button1.end)
        })

//        Button(onClick = {}, modifier = Modifier.constrainAs(button) {
//            top.linkTo(parent.top, margin = 16.dp)
//        }) {
//            Text("??????")
//        }
//        Text("?????????", Modifier.constrainAs(text){
//            top.linkTo(button.bottom, margin = 16.dp)
//            centerHorizontallyTo(parent)
//        })

    }
}


@Composable
fun LargeConstraintLayout() {
    ConstraintLayout {
        val text = createRef()
        val guideline = createGuidelineFromStart(fraction = 0.5f)
        Text(text = "???????????????????????????????????????????????????????????????????????????????????????????????????", Modifier.constrainAs(text) {
            linkTo(start = guideline, end = parent.end)
            width = Dimension.preferredWrapContent.atLeast(50.dp)
        })

    }
}


@Composable
fun DecoupledConstraintLayout() {
    BoxWithConstraints {
        val constraints = if (maxWidth < maxHeight) {
            decoupledConstraints(16.dp)
        } else {
            decoupledConstraints(32.dp)
        }

        ConstraintLayout(constraints) {
            Button(
                onClick = {},
                modifier = Modifier.layoutId("button")
            ) {
                Text(text = "??????")
            }
            Text("?????????", Modifier.layoutId("text"))
        }

    }
}


private fun decoupledConstraints(margin: Dp): ConstraintSet {
    return ConstraintSet {
        val button = createRefFor("button")
        val text = createRefFor("text")

        constrain(button) {
            top.linkTo(parent.top, margin = margin)
        }
        constrain(text) {
            top.linkTo(button.bottom, margin = margin)
        }
    }
}


@Preview
@Composable
fun ConstraintLayoutContentPreview() {
    JetpackComposeTestTheme {
//        ConstraintLayoutContent()
//        LargeConstraintLayout()
        DecoupledConstraintLayout()
    }
}


@Composable
fun TwoTexts(modifier: Modifier = Modifier, text1: String, text2: String) {
    // ??? ???????????? ???????????? ???????????? ?????? ??? ?????? ?????? ??????
    Row(modifier = modifier.height(IntrinsicSize.Min)) {
        Text(
            text = text1,
            modifier = Modifier
                .weight(1f)
                .padding(start = 4.dp)
                .wrapContentWidth(Alignment.Start)
        )
        Divider(
            color = Color.Cyan, modifier = Modifier
                .fillMaxHeight()
                .width(1.dp)
        )
        Text(
            text = text2,
            modifier = Modifier
                .weight(1f)
                .padding(start = 4.dp)
                .wrapContentWidth(Alignment.End)
        )
    }
}

@Preview
@Composable
fun TwoTextsPreview() {
    JetpackComposeTestTheme {
        Surface {
            TwoTexts(text1 = "?????????1", text2 = "?????????2")
        }
    }
}