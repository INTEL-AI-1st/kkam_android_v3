package com.example.kkam_backup.util

import com.example.kkam_backup.BuildConfig
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest

object SupabaseClient {
    private val SUPABASE_URL: String = BuildConfig.SUPABASE_URL
    private val SUPABASE_KEY: String = BuildConfig.SUPABASE_KEY

    val client by lazy {
        createSupabaseClient(
            supabaseUrl = SUPABASE_URL,
            supabaseKey = SUPABASE_KEY
        ) {
            install(Postgrest)
        }
    }
}
