package com.ajhuntsman.ksftp.task

import com.ajhuntsman.ksftp.FilePair
import com.ajhuntsman.ksftp.SftpConnectionParameters
import com.jcraft.jsch.SftpException
import java.io.File

/**
 * Uploads one or more files.
 */
internal class UploadTask(sftpConnectionParameters: SftpConnectionParameters, filePairs: List<FilePair>) : BaseTask(sftpConnectionParameters, filePairs) {

    override fun doWork(): Boolean {
        return uploadFiles()
    }

    @Throws(Exception::class)
    private fun uploadFiles(): Boolean {
        if (filePairs.isEmpty()) {
            return true
        }

        try {
            val startTime = System.currentTimeMillis()

            // Hold onto the present working directory
            val pwd = sftpChannel?.pwd()

            // Upload every file
            var localFilePath: String
            var remoteFilePath: String
            for (filePair in filePairs) {
                localFilePath = filePair.sourceFilePath
                remoteFilePath = filePair.destinationFilePath

                if (localFilePath.isEmpty()) {
                    continue
                }

                val localFile = File(localFilePath)
                if (!localFile.isFile) {
                    continue
                }

                // Lazily create the directory structure
                val remoteDirectoryPath = remoteFilePath.substring(0, remoteFilePath.lastIndexOf(File.separator))
                try {
                    sftpChannel?.cd(remoteDirectoryPath)
                } catch (e: SftpException) {
                    sftpChannel?.mkdir(remoteDirectoryPath)
                } finally {
                    // Get back to our starting directory
                    sftpChannel?.cd(pwd)
                }

                // Upload the file
                sftpChannel?.put(localFilePath, remoteFilePath)
            }

            return true
        } catch (e: Exception) {
            throw e
        }

    }
}