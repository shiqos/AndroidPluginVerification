package extractor

import utils.Log
import utils.listRegularFiles
import java.io.File
import java.util.jar.JarFile

class AndroidPluginResourcesVersionExtractor(private val pluginPath: File) {

    fun getVersion(): String {
        val files = pluginPath.listRegularFiles().filter { it.extension == "jar" }
        files.forEach { file ->
            val jarFile = JarFile(file)

            jarFile.entries().asIterator().forEach { entry ->
                if (entry.name.contains("versions.properties") ) {
                    Log.d("find $entry in ${file.absolutePath}")
                    val lines = jarFile.getInputStream(entry).reader().readLines()
                    lines.forEach {
                        if (it.contains("android-plugin-resources=")) {
                            return it.substringAfter("android-plugin-resources=").trim()
                        }
                    }
                }

                if (entry.name.contains("studio-platform.properties") ) {
                    Log.d("find $entry in ${file.absolutePath}")
                    val lines = jarFile.getInputStream(entry).reader().readLines()
                    lines.forEach {
                        if (it.contains("version=")) {
                            return it.substringAfter("version=").trim()
                        }
                    }
                }
            }
        }

        throw Exception("Version not found")
    }
}