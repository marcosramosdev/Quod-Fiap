package br.com.fiap.trabalhoquod.service

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.core.net.toUri
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

class ReconhecimentoFacialService(private val context: Context) {

    private val faceDetector = FaceDetection.getClient(
        FaceDetectorOptions.Builder()
            .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
            .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
            .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
            .enableTracking()
            .build()
    )

    suspend fun detectarRosto(bitmap: Bitmap): Result<Boolean> = withContext(Dispatchers.IO) {
        try {
            val image = InputImage.fromBitmap(bitmap, 0)
            val faces = faceDetector.process(image).await()

            // Verificar se pelo menos um rosto foi detectado
            if (faces.size > 0) {
                val face = faces[0]


                val eulerY = face.headEulerAngleY // Inclinação para os lados
                val eulerX = face.headEulerAngleX // Inclinação para cima ou para baixo

                val rostoDeFronte = Math.abs(eulerY.toFloat()) < 20f && Math.abs(eulerX.toFloat()) < 20f


                val olhoEsquerdoAberto = face.leftEyeOpenProbability ?: 0f > 0.7f
                val olhoDireitoAberto = face.rightEyeOpenProbability ?: 0f > 0.7f
                val olhosAbertos = olhoEsquerdoAberto && olhoDireitoAberto


                val sorrindo = face.smilingProbability ?: 0f > 0.7f

                // Retornar sucesso se o rosto estiver bem posicionado e com os olhos abertos
                if (rostoDeFronte && olhosAbertos) {
                    Result.success(true)
                } else {
                    Result.failure(Exception("Posição do rosto ou expressão facial inadequada"))
                }
            } else {
                Result.failure(Exception("Nenhum rosto detectado na imagem"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    suspend fun salvarImagemRostoCadastrado(bitmap: Bitmap): Result<Uri> = withContext(Dispatchers.IO) {
        try {
            val file = File(context.filesDir, "rosto_cadastrado.jpg")

            FileOutputStream(file).use { outputStream ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream)
            }

            Result.success(file.toUri())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}