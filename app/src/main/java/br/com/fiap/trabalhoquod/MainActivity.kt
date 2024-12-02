package br.com.fiap.trabalhoquod

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.fiap.trabalhoquod.telas.AnaliseDocumentos
import br.com.fiap.trabalhoquod.telas.AnaliseFormulario
import br.com.fiap.trabalhoquod.telas.ReconhecimentoBiometrico
import br.com.fiap.trabalhoquod.telas.ReconhecimentoFacial
import br.com.fiap.trabalhoquod.telas.SimSwap

import br.com.fiap.trabalhoquod.ui.theme.TrabalhoQuodTheme

class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            TrabalhoQuodTheme {
                Scaffold (){
                    HomeScreen(
                        onNavigateToFaceBiometric = { navigateToActivity(ReconhecimentoFacial::class.java) },
                        onNavigateToVerificarDocumentos = { navigateToActivity(AnaliseDocumentos::class.java) },
                        onNavigatetoBiometria = { navigateToActivity(ReconhecimentoBiometrico::class.java) },
                        onNavigateToSimSwap = { navigateToActivity(SimSwap::class.java) },
                        onNavigateToFormulario = { navigateToActivity(AnaliseFormulario::class.java) }
                    )
                }

            }
        }
    }
    private fun navigateToActivity(activityClass: Class<*>) {
        val intent = Intent(this, activityClass)
        startActivity(intent)
    }
}

@Composable
fun HomeScreen(
    onNavigateToFaceBiometric: () -> Unit,
    onNavigatetoBiometria: () -> Unit,
    onNavigateToVerificarDocumentos: () -> Unit,
    onNavigateToSimSwap: () -> Unit,
    onNavigateToFormulario: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Button(
            onClick = onNavigateToFormulario,
            modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)
        ) {
            Text("Formulario de Cadastro")
        }

        Button(
            onClick = onNavigateToFaceBiometric,
            modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)
        ) {
            Text("Reconhecimento Facial")
        }

        Button(
            onClick = onNavigatetoBiometria,
            modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)
        ) {
            Text("Reconhecimento Biométrico")
        }


        Button(
            onClick = onNavigateToSimSwap,
            modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)
        ) {
            Text("Verficação Chip")
        }

        Button(
            onClick = onNavigateToVerificarDocumentos,
            modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)
        ) {
            Text("Verficação Documentos")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen(
        onNavigateToFaceBiometric = { println("Navegar para Biometria Facial") },
        onNavigatetoBiometria = { println("Navegar para Biometria Digital") },
        onNavigateToVerificarDocumentos = { println("Navegar para Análise de Documentos") },
        onNavigateToSimSwap = { println("Navegar para SIM SWAP") },
        onNavigateToFormulario = { println("Navegar para Autenticação Cadastral") }
    )
}
