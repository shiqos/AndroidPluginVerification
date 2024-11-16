import extractor.MetadataExtractor
import utils.Log

fun main() {
    Log.setLogLevel(Log.INFO)

//    verify("241.17011.79", "https://plugins.jetbrains.com/plugin/download?rel=true&updateId=544295")

//    verify("242.23339.11", "https://plugins.jetbrains.com/plugin/download?rel=true&updateId=608438")
//    verify("242.21829.142", "https://plugins.jetbrains.com/plugin/download?rel=true&updateId=595106")
//    verify("242.23726.38", "https://plugins.jetbrains.com/plugin/download?rel=true&updateId=619530")
//    verify("242.23726.103", "https://plugins.jetbrains.com/plugin/download?rel=true&updateId=622869")

//    verify("243.15521.24", "https://plugins.jetbrains.com/plugin/download?rel=true&updateId=605440")
    verify("243.21565.214", "https://plugins.jetbrains.com/plugin/download?rel=true&updateId=634449")
}

private fun verify(pluginVersion: String, pluginDownloadUrl: String) {
    val metadata = MetadataExtractor(pluginVersion, pluginDownloadUrl).extract()
    Log.i(metadata.toString())

    if (metadata.pluginJavaVersion != metadata.installerVersion) {
        Log.e("Version mismatch. pluginJavaVersion: ${metadata.pluginJavaVersion}, installerVersion: ${metadata.installerVersion}")
    } else {
        Log.i("Version matched. pluginJavaVersion: ${metadata.pluginJavaVersion}, installerVersion: ${metadata.installerVersion}")
    }
}
