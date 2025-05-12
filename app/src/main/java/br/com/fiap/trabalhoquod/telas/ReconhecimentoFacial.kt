package br.com.fiap.trabalhoquod.telas

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import br.com.fiap.trabalhoquod.service.ReconhecimentoFacialService
import kotlinx.coroutines.launch
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class ReconhecimentoFacial : ComponentActivity() {

    private lateinit var cameraExecutor: ExecutorService
    private lateinit var imageCapture: ImageCapture
    private lateinit var reconhecimentoFacialService: ReconhecimentoFacialService
    private var previewView: PreviewView? = null
    private var cameraInitialized = false

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            Handler(Looper.getMainLooper()).postDelayed({
                startCamera()
            }, 300)
        } else {
            Toast.makeText(
                this,
                "Permissão de câmera é necessária para reconhecimento facial",
                Toast.LENGTH_LONG
            ).show()
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        cameraExecutor = Executors.newSingleThreadExecutor()
        reconhecimentoFacialService = ReconhecimentoFacialService(this)
        imageCapture = ImageCapture.Builder().build()

        setContent {
            MaterialTheme {
                ReconhecimentoFacialScreen(
                    onCapturarFoto = { capturarFoto() }
                )
            }
        }

        // Verificamos a permissão após o setContent
        Handler(Looper.getMainLooper()).postDelayed({
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                startCamera()
            } else {
                requestPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }, 300)
    }

    private fun startCamera() {
        if (previewView == null || cameraInitialized) {
            return
        }

        cameraInitialized = true
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        val mainExecutor = ContextCompat.getMainExecutor(this)

        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder().build()

            // Conectar previewView ao preview
            preview.setSurfaceProvider(previewView?.surfaceProvider)

            val cameraSelector = CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_FRONT)
                .build()

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this,
                    cameraSelector,
                    preview,
                    imageCapture
                )
            } catch (e: Exception) {
                Toast.makeText(
                    this,
                    "Erro ao iniciar câmera: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }, mainExecutor)
    }

    private fun capturarFoto() {
        val bitmap = previewView?.bitmap

        bitmap?.let {
            lifecycleScope.launch {
                try {
                    val resultado = reconhecimentoFacialService.detectarRosto(it)

                    if (resultado.isSuccess) {
                        // Salvar o rosto capturado
                        val uriRosto = reconhecimentoFacialService.salvarImagemRostoCadastrado(it)

                        Toast.makeText(
                            this@ReconhecimentoFacial,
                            "Reconhecimento facial realizado com sucesso!",
                            Toast.LENGTH_SHORT
                        ).show()

                        navigateToNext()
                    } else {
                        Toast.makeText(
                            this@ReconhecimentoFacial,
                            "Falha no reconhecimento: ${resultado.exceptionOrNull()?.message}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(
                        this@ReconhecimentoFacial,
                        "Erro ao processar reconhecimento: ${e.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        } ?: Toast.makeText(
            this,
            "Erro ao capturar imagem",
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun navigateToNext() {
        val intent = Intent(this, ScoreAntiFraude::class.java)
        startActivity(intent)
    }

    @Composable
    fun ReconhecimentoFacialScreen(
        onCapturarFoto: () -> Unit
    ) {
        var loading by remember { mutableStateOf(false) }

        Surface(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Reconhecimento Facial",
                    fontSize = 24.sp,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                AndroidView(
                    factory = { context ->
                        PreviewView(context).apply {
                            previewView = this
                            // Se a câmera ainda não foi inicializada, tente inicializar agora
                            if (!cameraInitialized && ContextCompat.checkSelfPermission(
                                    context,
                                    Manifest.permission.CAMERA
                                ) == PackageManager.PERMISSION_GRANTED
                            ) {
                                Handler(Looper.getMainLooper()).post {
                                    startCamera()
                                }
                            }
                        }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .padding(vertical = 16.dp)
                )

                Text(
                    text = "Posicione seu rosto no centro da câmera",
                    modifier = Modifier.padding(vertical = 16.dp)
                )

                Button(
                    onClick = {
                        loading = true
                        onCapturarFoto()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !loading
                ) {
                    Text("Capturar")
                }

                if (loading) {
                    CircularProgressIndicator(modifier = Modifier.padding(top = 16.dp))
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }
}