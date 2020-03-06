package com.onix.avlib.demo.util

import java.io.BufferedReader
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

object IoUtil {

    fun getSyncHttp(urlString: String): String {
        return try {
            val connection = (URL(urlString).openConnection() as HttpURLConnection).apply {
                readTimeout = 2000
                connectTimeout = 2000
                requestMethod = "GET"
                doInput = true
                connect()
            }

            (connection.content as InputStream).bufferedReader()
                .use(BufferedReader::readText)

        } catch (e: Exception) {
            ""
        }
    }

}
