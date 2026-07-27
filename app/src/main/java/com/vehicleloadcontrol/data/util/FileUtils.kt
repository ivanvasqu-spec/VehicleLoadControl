package com.vehicleloadcontrol.data.util

import java.io.File

object FileUtils {
    fun getFileName(path: String): String {
        return File(path).name
    }

    fun getFileExtension(path: String): String {
        val file = File(path)
        return file.extension
    }

    fun isValidPdfFile(path: String): Boolean {
        val file = File(path)
        return file.exists() && file.extension.equals("pdf", ignoreCase = true)
    }
}
