package com.example.kkam_backup.util

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/**
 * 코루틴 디스패처를 중앙에서 관리합니다.
 */
object CoroutineDispatchers {
    val Main: CoroutineDispatcher    = Dispatchers.Main
    val IO: CoroutineDispatcher      = Dispatchers.IO
    val Default: CoroutineDispatcher = Dispatchers.Default
}
