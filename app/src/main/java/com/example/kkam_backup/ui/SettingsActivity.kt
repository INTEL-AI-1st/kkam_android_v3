package com.example.kkam_backup.ui

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceFragmentCompat
import com.example.kkam_backup.R
import com.google.android.material.appbar.MaterialToolbar

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        // 툴바 설정
        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        // 뒤로가기(업) 버튼 활성화
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // 뒤로가기 아이콘 클릭 시
        toolbar.setNavigationOnClickListener { finish() }

        // PreferenceFragment 로딩
        supportFragmentManager.beginTransaction()
            .replace(R.id.settings_container, PrefsFragment())
            .commit()
    }

    // 툴바 홈(←) 클릭 처리
    override fun onOptionsItemSelected(item: MenuItem): Boolean =
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    class PrefsFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.preferences, rootKey)
        }
    }
}
