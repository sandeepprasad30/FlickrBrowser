package com.sandeep.flickrbrowser

import android.os.AsyncTask
import android.util.Log
import java.io.IOException
import java.net.MalformedURLException
import java.net.URL

enum class DownloadStatus {
    OK, IDLE, NOT_INITIALIZED, FAILED_OR_EMPTY, PERMISSION_ERROR, ERROR
}

class GetRawData(private val listener: MainActivity) : AsyncTask <String, Void, String>() {

    private val TAG = "GetRawData"
    private var downloadStatus = DownloadStatus.IDLE
    // private var listener: MainActivity? = null

    interface OnDownalodComplete {
        fun onDownloadComplete(data: String, status: DownloadStatus)
    }

    override fun onPostExecute(result: String) {
        // super.onPostExecute(result)
        Log.d(TAG, "OnPostexecute called")
        listener.onDownloadComplete(result, downloadStatus)
    }

    /*fun setDownloadCompleteListener(callbackObject: MainActivity){
        listener = callbackObject
    }*/

    override fun doInBackground(vararg params: String?): String {
        if (params[0] == null) {
            downloadStatus = DownloadStatus.NOT_INITIALIZED
        }

        try {
            downloadStatus = DownloadStatus.OK
            return URL(params[0]).readText()
        } catch (e: Exception) {
            val errorMessage: String = when (e) {
                is MalformedURLException -> {
                    downloadStatus = DownloadStatus.NOT_INITIALIZED
                    "donloadXML:  invalid url ${e.message}"
                }
                is IOException -> {
                    downloadStatus = DownloadStatus.FAILED_OR_EMPTY
                    "donloadXML:  IOException ${e.message}"
                }
                is SecurityException -> {
                    e.printStackTrace()
                    downloadStatus = DownloadStatus.PERMISSION_ERROR
                    "downloadXML: SecurityException needs permission ${e.message}"
                }
                else -> {
                    downloadStatus = DownloadStatus.ERROR
                    "downloadXML: unknown error ${e.message}"
                }
            }

            Log.e(TAG, errorMessage)

            return errorMessage

        }
    }
}