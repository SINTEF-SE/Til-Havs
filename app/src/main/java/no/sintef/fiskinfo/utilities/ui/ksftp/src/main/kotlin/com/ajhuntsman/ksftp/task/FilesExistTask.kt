package com.ajhuntsman.ksftp.task

import com.ajhuntsman.ksftp.FilePair
import com.ajhuntsman.ksftp.SftpConnectionParameters
import com.jcraft.jsch.SftpException

/**
 * Checks for the existence of one or more remote files.
 */
internal class FilesExistTask(sftpConnectionParameters: SftpConnectionParameters, filePairs: List<FilePair>) : BaseTask(sftpConnectionParameters, filePairs) {

    override fun doWork(): Boolean {
        return checkFiles()
    }

    @Throws(Exception::class)
    private fun checkFiles(): Boolean {
        if (filePairs.isEmpty()) {
            return true
        }

        try {
            val startTime = System.currentTimeMillis()

            // Check every file
            var remotePath: String
            for (filePair in filePairs) {
                remotePath = filePair.sourceFilePath
                if (remotePath.isEmpty()) {
                    continue
                }

                try {
                    sftpChannel?.ls(remotePath)
                } catch (e: SftpException) {
                    return false
                }

            }

            return true
        } catch (e: Exception) {
            throw e
        }

    }
}