// 경로: app/src/main/java/com/example/kkam_backup/domain/usecase/DetectAnomalyUseCase.kt
package com.example.kkam_backup.domain.usecase

import com.example.kkam_backup.domain.AnomalyDetector
import java.nio.ByteBuffer

/**
 * AnomalyDetector 를 래핑하는 UseCase
 */
class DetectAnomalyUseCase(
    private val detector: AnomalyDetector
) {
    operator fun invoke(input: ByteBuffer): Boolean =
        detector.detect(input)
}
