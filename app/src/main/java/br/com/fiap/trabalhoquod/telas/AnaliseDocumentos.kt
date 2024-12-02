package br.com.fiap.trabalhoquod.telas

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter

class AnaliseDocumentos : ComponentActivity() {

    private val getImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            selectedImageUri = uri
        }
    }

    var selectedImageUri by mutableStateOf<Uri?>(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                AnaliseDocumentosScreen(
                    onSelectImage = { openImagePicker() },
                    onSubmit = { navigateToScore() }
                )
            }
        }
    }

    // Função para abrir a galeria para selecionar uma imagem
    private fun openImagePicker() {
        getImage.launch("image/*")
    }

    private fun navigateToScore() {
        val intent = Intent(this, ScoreAntiFraude::class.java)
        startActivity(intent)
    }
}

@Composable
fun AnaliseDocumentosScreen(onSelectImage: () -> Unit, onSubmit: () -> Unit) {
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
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

            // Exibe a imagem selecionada
            selectedImageUri?.let {
                Image(
                    painter = rememberImagePainter(it),
                    contentDescription = "Documento",
                    modifier = Modifier
                        .size(200.dp)
                        .padding(bottom = 16.dp)
                )
            } ?: Text(text = "Nenhuma imagem selecionada")

            // Botão para selecionar a foto do documento
            Button(onClick = onSelectImage) {
                Text("Selecione uma Foto do Documento")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botão para submeter a verificação
            Button(
                onClick = {
                    loading = true
                    onSubmit()
                    loading = false
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Submeter Verificação")
            }

            if (loading) {
                CircularProgressIndicator(modifier = Modifier.padding(top = 16.dp))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AnaliseDocumentosScreenPreview() {
    AnaliseDocumentosScreen(
        onSelectImage = {},
        onSubmit = {}
    )
}
