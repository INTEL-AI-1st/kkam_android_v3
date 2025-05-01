package com.example.kkam_backup.data.model

import kotlinx.serialization.Serializable

@Serializable
data class LoginResult(
    val uid: String,
    val use_yn: Boolean
)
