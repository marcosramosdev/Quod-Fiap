package br.com.fiap.trabalhoquod.telas

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import br.com.fiap.trabalhoquod.service.DocumentoService
import coil.compose.rememberImagePainter
import kotlinx.coroutines.launch

class AnaliseDocumentos : ComponentActivity() {

    private var selectedImageUri by mutableStateOf<Uri?>(null)
    private lateinit var documentoService: DocumentoService

    private val getImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            selectedImageUri = uri
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        documentoService = DocumentoService(this)

        setContent {
            MaterialTheme {
                AnaliseDocumentosScreen(
                    imageUri = selectedImageUri,
                    onSelectImage = { openImagePicker() },
                    onSubmit = { enviarDocumento() }
                )
            }
        }
    }

    private fun openImagePicker() {
        getImage.launch("image/*")
    }

    private fun enviarDocumento() {
        selectedImageUri?.let { uri ->
            lifecycleScope.launch {
                try {
                    val resultado = documentoService.enviarDocumento(uri)

                    if (resultado.isSuccess) {
                        Toast.makeText(this@AnaliseDocumentos,
                            "Documento enviado com sucesso!", Toast.LENGTH_SHORT).show()
                        navigateToScore()
                    } else {
                        Toast.makeText(this@AnaliseDocumentos,
                            "Falha ao validar documento: ${resultado.exceptionOrNull()?.message}",
                            Toast.LENGTH_LONG).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(this@AnaliseDocumentos,
                        "Erro ao processar documento: ${e.message}",
                        Toast.LENGTH_LONG).show()
                }
            }
        } ?: Toast.makeText(this,
            "Por favor, selecione uma imagem do documento",
            Toast.LENGTH_SHORT).show()
    }

    private fun navigateToScore() {
        val intent = Intent(this, ScoreAntiFraude::class.java)
        startActivity(intent)
    }
}

@Composable
fun AnaliseDocumentosScreen(
    imageUri: Uri? = null,
    onSelectImage: () -> Unit,
    onSubmit: () -> Unit
) {
    var loading by remember { mutableStateOf(false) }

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Análise de Documentos",
                fontSize = 24.sp,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            imageUri?.let {
                Image(
                    painter = rememberImagePainter(it),
                    contentDescription = "Documento",
                    modifier = Modifier
                        .size(200.dp)
                        .padding(bottom = 16.dp)
                )
            } ?: Text(text = "Nenhuma imagem selecionada")

            Button(onClick = onSelectImage) {
                Text("Selecione uma Foto do Documento")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    loading = true
                    onSubmit()
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = imageUri != null && !loading
            ) {
                Text("Enviar para Validação")
            }

            if (loading) {
                CircularProgressIndicator(modifier = Modifier.padding(top = 16.dp))
            }
        }
    }
}