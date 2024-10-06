package extractor

import utils.Log
import java.io.File
import java.io.RandomAccessFile

class InstallerVersionExtractor(private val installerFile: File) {

    fun getVersion(): String {
        val byteCount = 31 // Number of bytes to read
        val lastBytes = readLastBytes(installerFile, byteCount)

        val lastBytesHex = lastBytes.joinToString("") { "%02x".format(it) }
        Log.d("Last $byteCount bytes: $lastBytesHex")

        if (!lastBytesHex.endsWith("5506d5d1")) {
            throw Exception("Invalid installer")
        }

        val versionStr = lastBytesHex.substring(24, 24 + 14)
        Log.d("version: ${hexToAscii(versionStr)}")

        val hashStr = lastBytesHex.take(16)
        Log.d("hash: ${hexToAscii(hashStr)}")

        return hexToAscii(hashStr)
    }

    private fun readLastBytes(installerFile: File, byteCount: Int): ByteArray {
        val bytes = ByteArray(byteCount)

        RandomAccessFile(installerFile, "r").use { raf ->
            // Move to the position byteCount before the end
            raf.seek(installerFile.length() - byteCount)
            // Read the bytes into the array
            raf.readFully(bytes)
        }

        return bytes
    }

    private fun hexToAscii(hex: String): String {
        val output = StringBuilder()

        for (i in hex.indices step 2) {
            val str = hex.substring(i, i + 2)
            val decimal = str.toInt(16)
            output.append(decimal.toChar())
        }

        return output.toString()
    }

}