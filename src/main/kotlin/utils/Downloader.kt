package utils

import java.io.File
import java.net.URL

class Downloader(private val url: String, private val outputFile: File) {

    fun download() {
        val okFile = outputFile.parentFile!!.resolve(outputFile.name + ".ok")
        if (okFile.exists() && outputFile.exists() && outputFile.length() > 0) {
            Log.d("File has downloaded to ${outputFile.absolutePath}")
            return
        }

        try {
            Log.i("Downloading file: $url to ${outputFile.absolutePath}")
            val website = URL(url)
            val connection = website.openConnection()
            connection.getInputStream().use { input ->
                outputFile.parentFile?.mkdirs()

                outputFile.outputStream().use { output ->
                    input.copyTo(output)
                }
            }
            Log.i("Downloaded to ${outputFile.absolutePath}")

            if (!okFile.exists()) {
                okFile.createNewFile()
            }
        } catch (e: Exception) {
            Log.d("Error downloading file: ${e.message}")
            throw RuntimeException("Download $url failed", e)
        }
    }

}