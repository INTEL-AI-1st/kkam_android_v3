package com.example.kkam_backup.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kkam_backup.domain.usecase.DetectAnomalyUseCase
import com.example.kkam_backup.util.CoroutineDispatchers
import kotlinx.coroutines.launch
import java.nio.ByteBuffer

class AnomalyViewModel(
    private val detectAnomaly: DetectAnomalyUseCase
) : ViewModel() {

    private val _isAnomaly = MutableLiveData(false)
    val isAnomaly: LiveData<Boolean> = _isAnomaly

    private val _probability = MutableLiveData(0f)
    val probability: LiveData<Float> = _probability

    /**
     * 프레임(ByteBuffer) 을 받아서 비동기로 이상 여부, 확률을 계산합니다.
     */
    fun analyzeFrame(input: ByteBuffer) {
        viewModelScope.launch(CoroutineDispatchers.Default) {
            // 예: UseCase 내부에서 Float 확률을 반환하도록 수정하면 좋습니다.
            val result = detectAnomaly(input)
            _isAnomaly.postValue(result)
            // 확률 정보도 있으면 postValue(_probability, …) 추가
        }
    }
}
