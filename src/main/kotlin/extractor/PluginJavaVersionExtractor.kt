package extractor

import org.objectweb.asm.ClassReader
import org.objectweb.asm.tree.ClassNode
import org.objectweb.asm.tree.LdcInsnNode
import utils.Log
import utils.listRegularFiles
import java.io.File
import java.util.jar.JarFile

class PluginJavaVersionExtractor(private val pluginPath: File) {

    fun getVersion(): String {
        val files = pluginPath.listRegularFiles().filter { it.extension == "jar" }
        files.forEach { file ->
            val jarFile = JarFile(file)
            val entry = jarFile.getJarEntry("com/android/tools/deployer/Version.class")
            if (entry != null) {
                Log.d("find $entry in ${file.absolutePath}")

                val classReader = ClassReader(jarFile.getInputStream(entry))
                val classNode = ClassNode()
                classReader.accept(classNode, 0)

                classNode.methods.forEach { method ->
                    method.instructions.forEach { insn ->
                        if (insn is LdcInsnNode) {
                            Log.d("find ldc ${insn.cst} in ${file.absolutePath}")

                            return insn.cst.toString()
                        }
                    }
                }
            }
        }

        throw Exception("Version not found")
    }

}