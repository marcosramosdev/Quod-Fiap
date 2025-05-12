package br.com.fiap.trabalhoquod.service

import android.content.Context
import android.net.Uri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream

class DocumentoService(private val context: Context) {

    private val client = OkHttpClient()
    private val baseUrl = "http://192.168.15.102:8080"

    suspend fun enviarDocumento(uri: Uri): Result<Boolean> = withContext(Dispatchers.IO) {
        try {
            // Converter Uri para File
            val inputStream = context.contentResolver.openInputStream(uri)
            val file = File(context.cacheDir, "documento_temp.jpg")

            FileOutputStream(file).use { outputStream ->
                inputStream?.copyTo(outputStream)
            }

            // Criar corpo da requisição multipart
            val requestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
            val multipartBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("documento", file.name, requestBody)
                .build()

            // Criar e executar requisição
            val request = Request.Builder()
                .url("$baseUrl/api/documentos/validar")
                .post(multipartBody)
                .build()

            val response = client.newCall(request).execute()

            if (response.isSuccessful) {
                Result.success(true)
            } else {
                Result.failure(Exception("Erro ao enviar documento: ${response.code}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}