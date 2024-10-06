package utils

import java.io.File
import java.util.zip.ZipFile

fun File.unzip(destDir: String) {
    val destDirFile = File(destDir)
    if (!destDirFile.exists()) {
        destDirFile.mkdirs()
    }

    Log.i("unzip $absolutePath to $destDir")
    ZipFile(this).use { zip ->
        zip.entries().asSequence().forEach { entry ->
            val entryDest = File(destDir, entry.name)
            if (entry.isDirectory) {
                entryDest.mkdirs()
            } else {
                entryDest.parentFile.mkdirs()
                zip.getInputStream(entry).use { input ->
                    entryDest.outputStream().use { output ->
                        input.copyTo(output)
                    }
                }
            }
        }
    }
}

fun File.listRegularFiles(): List<File> {
    if (this.isFile) return listOf(this)
    return this.listFiles()?.flatMap { it.listRegularFiles() }?.toList() ?: listOf()
}
