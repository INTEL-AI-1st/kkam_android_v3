package com.example.kkam_backup.domain

import java.nio.ByteBuffer

/**
 * CCTV 프레임(ByteBuffer)을 받아
 * 이상행동 여부를 반환하는 도메인 인터페이스
 */
interface AnomalyDetector {
    fun detect(input: ByteBuffer): Boolean
}
