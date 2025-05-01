package com.example.kkam_backup.util

import java.io.ByteArrayOutputStream
import java.io.InputStream
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.net.HttpURLConnection
import java.net.URL

class MjpegInputStream(private val urlString: String) {
    private var connection: HttpURLConnection? = null
    private var inputStream: InputStream? = null

    fun connect(): Boolean {
        val url = URL(urlString)
        connection = (url.openConnection() as HttpURLConnection).apply {
            readTimeout = 5000
            connectTimeout = 5000
            setRequestProperty("Connection", "Keep-Alive")
            doInput = true
            connect()
        }
        inputStream = connection!!.inputStream
        return connection!!.responseCode == HttpURLConnection.HTTP_OK
    }

    fun readFrame(): Bitmap? {
        val start = byteArrayOf(0xFF.toByte(), 0xD8.toByte()) // JPEG SOI
        val end   = byteArrayOf(0xFF.toByte(), 0xD9.toByte()) // JPEG EOI

        val buffer = ByteArrayOutputStream()
        val stream = inputStream ?: return null

        // scan until start marker
        var prev = stream.read()
        var cur  = stream.read()
        while (!(prev == start[0].toInt() && cur == start[1].toInt())) {
            prev = cur
            cur  = stream.read()
        }
        buffer.write(start)
        // read until end marker
        while (!(prev == end[0].toInt() && cur == end[1].toInt())) {
            buffer.write(cur)
            prev = cur
            cur  = stream.read()
        }
        buffer.write(end)
        val bytes = buffer.toByteArray()
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    }

    fun close() {
        inputStream?.close()
        connection?.disconnect()
    }
}
