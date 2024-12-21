package com.main

import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.security.MessageDigest
import java.util.stream.Collectors
import kotlin.io.path.Path

class FolderSync {

    private fun findFiles(path: Path): Set<String>{
        if (!Files.isDirectory(path)) throw IllegalArgumentException("Путь должен быть папкой")

        val walk = Files.walk(path)
        return walk.filter { p: Path -> !Files.isDirectory(p) }
                .map { p -> p.toString() }
                .collect(Collectors.toSet())
    }

    @OptIn(ExperimentalStdlibApi::class)
    fun File.md5(): String {
        val md = MessageDigest.getInstance("MD5")
        val digest = md.digest(this.readBytes())
        return digest.toHexString()
    }

    private fun fileEquality(initialFile: String, targetFile: String): Boolean{
        val initial = File(initialFile)
        val target = File(targetFile)
        return initial.md5() == target.md5()
    }

    fun syncFolder(initialPath: String, targetPath: String, replaceUnequalFiles: Boolean = false): Int{
        val initialFiles = findFiles(Path(initialPath))
        val targetFiles = findFiles(Path(targetPath))

        var filesSynced = 0

        initialFiles.forEach { file ->
            if (!targetFiles.contains(targetPath + file.replace(initialPath,""))) {
                val initialFile = File(file)
                val target = targetPath + "/" + file.replace(initialPath,"")
                initialFile.copyTo(File(target))
                filesSynced++
            } else if (!fileEquality(file, targetPath + file.replace(initialPath,""))
                && replaceUnequalFiles){
                val initialFile = File(file)
                val target = targetPath + "/" + file.replace(initialPath,"")
                initialFile.copyTo(File(target), overwrite = true)
                filesSynced++
            }
        }

        return filesSynced
    }
}