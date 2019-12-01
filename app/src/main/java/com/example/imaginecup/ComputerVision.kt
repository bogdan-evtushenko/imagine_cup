package com.example.imaginecup

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.graphics.Bitmap
import android.os.AsyncTask
import android.text.TextUtils
import android.widget.Toast
import com.google.gson.Gson
import edmt.dev.edmtdevcognitivevision.Rest.VisionServiceException
import edmt.dev.edmtdevcognitivevision.VisionServiceClient
import edmt.dev.edmtdevcognitivevision.VisionServiceRestClient
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream

class ComputerVision {

    var visionServiceClient: VisionServiceClient = VisionServiceRestClient(API_KEY, API_LINK)

    fun fetchInformationAboutPhoto(context: Context, bitmap: Bitmap, onComplete: (String) -> Unit) {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        val inputStream: InputStream = ByteArrayInputStream(outputStream.toByteArray())
        /*val result =
            visionServiceClient.analyzeImage(inputStream, arrayOf("Description"), arrayOf(""))
        val jsonResult = Gson().toJson(result)
        val analysisResult = Gson().fromJson(jsonResult, AnalysisResult::class.java)
        var resultText = ""
        for (caption in analysisResult.description.captions) {
            resultText += caption
        }
        println("First result : $resultText")
*/

        val visionTask: AsyncTask<InputStream?, String?, String?> =
            @SuppressLint("StaticFieldLeak")
            object : AsyncTask<InputStream?, String?, String?>() {
                var progressDialog = ProgressDialog(context)
                override fun onPreExecute() {
                    progressDialog.show()
                }

                override fun doInBackground(vararg inputStreams: InputStream?): String? {
                    try {
                        println("Here")
                        publishProgress("Reconizing...")
                        val features =
                            arrayOf("Description", "Objects", "Faces", "Tags", "Categories")
                        val details = arrayOf<String>()
                        val result =
                            visionServiceClient.analyzeImage(inputStreams[0], features, details)
                        return Gson().toJson(result)
                    } catch (e: IOException) {
                        e.printStackTrace()
                    } catch (e: VisionServiceException) {
                        e.printStackTrace()
                    }
                    return ""
                }

                override fun onPostExecute(s: String?) {
                    if (TextUtils.isEmpty(s)) {
                        Toast.makeText(
                            context,
                            "API Return Empty Result",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        progressDialog.dismiss()
                        /*
                        val result: AnalysisResult =
                            Gson().fromJson(s, AnalysisResult::class.java)
                        val resultText = StringBuilder()*/
                        /*for (caption in result.categories) resultText.append(
                            caption.detail
                        )*/
                        onComplete(s!!)
                    }
                }

                override fun onProgressUpdate(vararg values: String?) {
                    progressDialog.setMessage(values[0])
                }
            }
        visionTask.execute(inputStream)
    }

    companion object {
        private const val API_KEY = "fa03032744164f8eaee7dd9bc40dfc8c"
        private const val API_LINK = "https://bohdan.cognitiveservices.azure.com/vision/v1.0"
    }

}