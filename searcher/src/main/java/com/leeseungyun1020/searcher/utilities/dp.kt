package com.leeseungyun1020.searcher.utilities

import android.content.Context
import kotlin.math.roundToInt

fun Int.dp(context: Context) = (this * context.resources.displayMetrics.density).roundToInt()