package com.ajhuntsman.ksftp.task

import com.ajhuntsman.ksftp.FilePair
import com.ajhuntsman.ksftp.SftpConnectionParameters

/**
 * Downloads one or more files.
 */
internal class DownloadTask(sftpConnectionParameters: SftpConnectionParameters, filePairs: List<FilePair>) : BaseTask(sftpConnectionParameters, filePairs) {

    override fun doWork(): Boolean {
        return downloadFiles()
    }

    @Throws(Exception::class)
    private fun downloadFiles(): Boolean {
        if (filePairs.isEmpty()) {
            return true
        }

        val startTime = System.currentTimeMillis()
        try {
            // Download every file
            var localFilePath: String
            var remoteFilePath: String
            for (filePair in filePairs) {
                localFilePath = filePair.sourceFilePath
                remoteFilePath = filePair.destinationFilePath

                if (localFilePath.isEmpty()) {
                    continue
                }

                // Download the file
                sftpChannel?.get(remoteFilePath, localFilePath)
            }

            return true
        } catch (e: Exception) {
            throw e
        }

    }
}