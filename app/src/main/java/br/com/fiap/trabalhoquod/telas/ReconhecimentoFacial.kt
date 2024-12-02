package br.com.fiap.trabalhoquod.telas

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class ReconhecimentoFacial : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ReconhecimentoFacialScreen(
                onRequestCameraPermission = { checkAndRequestCameraPermission() }
            )
        }
    }

    private fun checkAndRequestCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            == PackageManager.PERMISSION_GRANTED
        ) {
            openCamera()
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                CAMERA_PERMISSION_REQUEST_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera()
            } else {
                Toast.makeText(this, "Permissão negada.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (intent.resolveActivity(packageManager) == null) {
            startActivityForResult(intent, CAMERA_CAPTURE_REQUEST_CODE)
        } else {
            Toast.makeText(this, "Câmera não disponível.", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CAMERA_CAPTURE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val photo = data?.extras?.get("data") as? Bitmap
            if (photo != null) {
                navigateToScoreAntiFraude(photo)
            } else {
                Toast.makeText(this, "Erro ao capturar a imagem.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun navigateToScoreAntiFraude(photo: Bitmap) {
        val intent = Intent(this, ScoreAntiFraude::class.java).apply {
            putExtra("photo_bitmap", photo)
        }
        startActivity(intent)
    }

    companion object {
        private const val CAMERA_PERMISSION_REQUEST_CODE = 101
        private const val CAMERA_CAPTURE_REQUEST_CODE = 102
    }
}

@Composable
fun ReconhecimentoFacialScreen(onRequestCameraPermission: () -> Unit) {
    MaterialTheme {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center // Centraliza o conteúdo
            ) {
                Button(
                    onClick = onRequestCameraPermission,
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text("Abrir Câmera")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ReconhecimentoFacialScreenPreview() {
    ReconhecimentoFacialScreen(onRequestCameraPermission = {})
}
