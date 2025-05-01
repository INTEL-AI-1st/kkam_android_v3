package com.example.kkam_backup.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.kkam_backup.R
import com.example.kkam_backup.util.NotificationHelper          // ★ util 사용
import com.example.kkam_backup.util.NotificationHelper.CHANNEL_ID
import com.google.android.material.appbar.MaterialToolbar

class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "MainActivity"
        private const val REQ_NOTIF = 100                      // 권한 요청 코드
        private const val ALERT_THRESHOLD = 0.80f              // ← 80 % 기준
    }

    /* --- RTSP/HTTP 스트림 호스트 --- */
    private val cameraHostTop = "192.168.219.176"
    private val cameraHostBottom = "192.168.219.56"
    private val cameraPort = 5000

    /* --- UI & 핸들러 --- */
    private lateinit var topWebView: WebView
    private lateinit var bottomWebView: WebView
    private val handler = Handler(Looper.getMainLooper())

    /* --- 알림 중복 방지 플래그 --- */
    private var hasAlertedTop = false
    private var hasAlertedBottom = false

    /* -------------------------------------------------- */
    /* lifecycle                                           */
    /* -------------------------------------------------- */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /* 1️⃣ 채널 생성 (앱 최초 실행 1회) */
        NotificationHelper.createChannel(this)

        /* 2️⃣ 레이아웃 세팅 */
        setContentView(R.layout.activity_main)

        /* 3️⃣ 툴바 */
        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        /* 4️⃣ WebView 2개 */
        topWebView = findViewById<WebView>(R.id.streamTop).apply {
            settings.javaScriptEnabled = true
            webViewClient = WebViewClient()
            loadUrl("http://$cameraHostTop:$cameraPort")
        }
        bottomWebView = findViewById<WebView>(R.id.streamBottom).apply {
            settings.javaScriptEnabled = true
            webViewClient = WebViewClient()
            loadUrl("http://$cameraHostBottom:$cameraPort")
        }

        /* 5️⃣ Android 13+ 알림 권한 체크 */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                REQ_NOTIF
            )
        } else {
            startMonitoring()                                 // 권한 이미 있으면 바로 시작
        }
    }

    /* 권한 요청 결과 */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQ_NOTIF) {
            val granted = grantResults.isNotEmpty() &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED
            if (granted) {
                Toast.makeText(this, "알림 권한 허용됨", Toast.LENGTH_SHORT).show()
                /* 권한 테스트 알림 */
                NotificationHelper.showHeadsUp(
                    context = this,
                    id = 999,
                    title = "권한 테스트",
                    message = "이 알림이 보이면 정상!"
                )
                startMonitoring()
            } else {
                Toast.makeText(this, "알림 권한 거부됨", Toast.LENGTH_LONG).show()
            }
        }
    }

    /* 메뉴 */
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean =
        when (item.itemId) {
            R.id.action_settings -> {
                startActivity(Intent(this, SettingsActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    /* -------------------------------------------------- */
    /* WebView 분석 주기                                  */
    /* -------------------------------------------------- */
    private fun startMonitoring() = handler.post(checkBoth)

    private val checkBoth: Runnable = object : Runnable {
        override fun run() {
            Log.d(TAG, "▶ WebView 확률 스캔")
            if (!hasAlertedTop)     checkProbability(topWebView,     1001, "CAM1")
            if (!hasAlertedBottom)  checkProbability(bottomWebView,  1002, "CAM2")
            handler.postDelayed(this, 5_000)
        }
    }

    private fun checkProbability(view: WebView, notifId: Int, tag: String) {
        val js = """
            (function () {
              var e = document.querySelector("#probText");
              return e ? e.innerText : "";
            })();
        """.trimIndent()

        view.evaluateJavascript(js) { raw ->
            val cleaned = raw.trim('"')                // "85.4%" → 85.4
            Log.d(TAG, "[$tag] JS 결과 = $cleaned")

            val pct = cleaned.replace("%", "").toFloatOrNull()
            if (pct != null && pct / 80f >= ALERT_THRESHOLD) {
                if (tag == "CAM1") hasAlertedTop = true else hasAlertedBottom = true

                NotificationHelper.showHeadsUp(
                    context = this,
                    id = notifId,
                    title = "$tag: 이상 행동 감지!",
                    message = "확률 ${"%.1f".format(pct)}%"
                )
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(checkBoth)
    }
}
