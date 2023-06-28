package com.ajhuntsman.ksftp.task

import com.ajhuntsman.ksftp.FilePair
import com.ajhuntsman.ksftp.SftpConnectionParameters
import com.jcraft.jsch.SftpException
import java.io.File

/**
 * Renames one or more remote files.
 */
internal class RenameTask(sftpConnectionParameters: SftpConnectionParameters, filePairs: List<FilePair>) : BaseTask(sftpConnectionParameters, filePairs) {

    override fun doWork(): Boolean {
        return renameFiles()
    }

    @Throws(Exception::class)
    private fun renameFiles(): Boolean {
        if (filePairs.isEmpty()) {
            return true
        }

        var success: Boolean = true
        try {
            val startTime = System.currentTimeMillis()

            // Hold onto the present working directory
            val pwd = sftpChannel?.pwd()

            // Rename every file
            var oldRemotePath: String
            var newRemotePath: String
            for (filePair in filePairs) {
                oldRemotePath = filePair.sourceFilePath
                newRemotePath = filePair.destinationFilePath

                if (oldRemotePath.isEmpty()) {
                    continue
                }

                // Lazily create the directory structure
                val remoteDirectoryPath = newRemotePath.substring(0, newRemotePath.lastIndexOf(File.separator))
                try {
                    sftpChannel?.cd(remoteDirectoryPath)
                } catch (e: SftpException) {
                    sftpChannel?.mkdir(remoteDirectoryPath)
                } finally {
                    // Get back to our starting directory
                    sftpChannel?.cd(pwd)
                }

                try {
                    sftpChannel?.rename(oldRemotePath, newRemotePath)
                } catch (e: SftpException) {
                    success = false
                }

            }

            return success
        } catch (e: Exception) {
            throw e
        }

    }
}