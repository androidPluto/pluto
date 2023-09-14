package com.pluto.plugin.libinterface

import android.app.Application
import java.io.File
import java.io.IOException
import java.util.concurrent.atomic.AtomicLong

class FilesInterface(application: Application) {
    private val uniqueIdGenerator = AtomicLong()
    private val directory: File = application.applicationContext.filesDir

    fun createFile(filename: String = "pluto-${uniqueIdGenerator.getAndIncrement()}"): File? = try {
        File(directory, filename).apply {
            if (exists() && !delete()) {
                throw IOException("Failed to delete file $this")
            }
            parentFile?.mkdirs()
            if (!createNewFile()) {
                throw IOException("File $this already exists")
            }
        }
    } catch (e: IOException) {
        IOException("An error occurred while creating a Pluto file", e).printStackTrace()
        null
    }
}
