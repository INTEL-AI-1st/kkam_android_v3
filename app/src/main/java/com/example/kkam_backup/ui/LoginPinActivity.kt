package com.example.kkam_backup.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.kkam_backup.R
import com.example.kkam_backup.data.model.LoginResult
import com.example.kkam_backup.util.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginPinActivity : AppCompatActivity() {

    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        etEmail    = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        btnLogin   = findViewById(R.id.btnLogin)

        btnLogin.setOnClickListener {
            val email    = etEmail.text.toString().trim()
            val password = etPassword.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "이메일과 비밀번호를 모두 입력하세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            lifecycleScope.launch {
                try {
                    // user 테이블에서 uid, use_yn만 조회 → List<LoginResult> 반환
                    val list: List<LoginResult> = withContext(Dispatchers.IO) {
                        SupabaseClient.client
                            .postgrest
                            .from("user")
                            .select()
                            .decodeList<LoginResult>()
                    }

                    when {
                        list.isEmpty() -> {
                            // 계정 없음
                            runOnUiThread {
                                Toast.makeText(
                                    this@LoginPinActivity,
                                    "계정이 없습니다.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                        else -> {
                            val result = list.first()
                            if (!result.use_yn) {
                                // 승인 대기
                                runOnUiThread {
                                    Toast.makeText(
                                        this@LoginPinActivity,
                                        "승인되지 않은 계정입니다.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            } else {
                                // 로그인 성공
                                runOnUiThread {
                                    Toast.makeText(
                                        this@LoginPinActivity,
                                        "로그인 성공! uid=${result.uid}",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    startActivity(Intent(this@LoginPinActivity, MainActivity::class.java))
                                    finish()
                                }
                            }
                        }
                    }

                } catch (e: Exception) {
                    // 네트워크/HTTP 오류
                    runOnUiThread {
                        Toast.makeText(
                            this@LoginPinActivity,
                            "로그인 중 오류 발생: ${e.localizedMessage}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }
}
