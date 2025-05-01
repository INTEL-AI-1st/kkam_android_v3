package com.example.kkam_backup.util

import android.content.Context
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner

import java.util.concurrent.Executors

/**
 * CameraX 프리뷰 + 분석(Analyzer) 세팅 헬퍼
 */
object CameraXHelper {
    /**
     * PreviewView 를 연결하고 Analyzer 를 설정합니다.
     *
     * @param lifecycleOwner Activity 또는 Fragment
     * @param context       context (주로 requireContext() 또는 applicationContext)
     * @param previewView   레이아웃의 PreviewView
     * @param analyzer      프레임을 받아 처리할 ImageAnalysis.Analyzer
     */
    fun setup(
        lifecycleOwner: LifecycleOwner,
        context: Context,
        previewView: PreviewView,
        analyzer: ImageAnalysis.Analyzer
    ) {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            // 1. Preview 유즈케이스
            val preview = Preview.Builder()
                .build()
                .also { it.setSurfaceProvider(previewView.surfaceProvider) }

            // 2. Analysis 유즈케이스
            val analysis = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()
                .also { it.setAnalyzer(Executors.newSingleThreadExecutor(), analyzer) }

            // 3. 바인딩 (카메라2 후면)
            cameraProvider.bindToLifecycle(
                lifecycleOwner,
                CameraSelector.DEFAULT_BACK_CAMERA,
                preview,
                analysis
            )
        }, ContextCompat.getMainExecutor(context))
    }
}
