package extractor

import utils.Downloader
import utils.Log
import utils.unzip
import java.io.File

class MetadataExtractor(private val pluginVersion: String, private val pluginDownloadUrl: String) {

    private val outputDir = File("./output/$pluginVersion")

    fun extract(): MetaData {
        val androidPluginDir = downloadAndroidPlugin()

        val pluginJavaVersionHash = PluginJavaVersionExtractor(androidPluginDir).getVersion()

        val androidPluginResourcesVersion = AndroidPluginResourcesVersionExtractor(androidPluginDir).getVersion()

        Log.d("pluginJavaVersionHash: $pluginJavaVersionHash, androidPluginResourcesVersion: $androidPluginResourcesVersion")

        val androidPluginResourcesDir = downloadAndroidPluginResources(androidPluginResourcesVersion)
        val installerBinFile = androidPluginResourcesDir.resolve("plugins/android/resources/installer/x86_64/installer")
        val installerVersion = InstallerVersionExtractor(installerBinFile).getVersion()

        Log.d("installerVersion: $installerVersion")

        return MetaData(pluginVersion, pluginJavaVersionHash, androidPluginResourcesVersion, installerVersion)
    }

    private fun downloadAndroidPlugin(): File {
        val androidPluginDownloadFile = File("$outputDir/android.zip")
        Downloader(pluginDownloadUrl, androidPluginDownloadFile).download()

        val androidPluginDir = File("$outputDir/android")
        androidPluginDownloadFile.unzip(androidPluginDir.absolutePath)

        return androidPluginDir
    }

    private fun downloadAndroidPluginResources(androidPluginResourcesVersion: String): File {
        val fileName = "android-plugin-resources-$androidPluginResourcesVersion.zip"
        val androidPluginResourcesDownloadFile = outputDir.resolve(fileName)
        val androidPluginResourcesVersionUrl =
            "https://cache-redirector.jetbrains.com/intellij-dependencies/org/jetbrains/intellij/deps/android/tools/base/android-plugin-resources/${androidPluginResourcesVersion}/$fileName"
        Downloader(
            androidPluginResourcesVersionUrl,
            androidPluginResourcesDownloadFile
        ).download()

        val androidPluginResourcesDir = File("$outputDir/android-plugin-resources")
        androidPluginResourcesDownloadFile.unzip(androidPluginResourcesDir.absolutePath)

        return androidPluginResourcesDir
    }

    data class MetaData(
        val pluginVersion: String,
        val pluginJavaVersion: String,
        val androidPluginResourcesVersion: String,
        val installerVersion: String
    )
}