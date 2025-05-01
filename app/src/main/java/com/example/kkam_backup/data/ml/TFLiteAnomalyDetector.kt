// 파일 경로: app/src/main/java/com/example/kkam_backup/data/ml/TFLiteAnomalyDetector.kt
package com.example.kkam_backup.data.ml

import android.content.Context
import com.example.kkam_backup.domain.AnomalyDetector
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.support.common.FileUtil
import java.nio.ByteBuffer

/**
 * TensorFlow Lite 모델(.tflite)로 이상행동을 감지하는 구현체
 */
class TFLiteAnomalyDetector(
    context: Context,
    modelFileName: String = "model.tflite"
) : AnomalyDetector {
    private val interpreter: Interpreter

    init {
        val mappedFile = FileUtil.loadMappedFile(context, modelFileName)
        interpreter = Interpreter(mappedFile)
    }

    override fun detect(input: ByteBuffer): Boolean {
        val output = Array(1) { FloatArray(1) }
        interpreter.run(input, output)
        return output[0][0] > 0.7f
    }
}