package br.com.fiap.trabalhoquod.telas

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import java.util.concurrent.Executor

class ReconhecimentoBiometrico : FragmentActivity() {

    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var biometricPromptInfo: BiometricPrompt.PromptInfo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!isBiometricSupported()) {
            Toast.makeText(this, "Biometria não suportada ou não configurada.", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        executor = ContextCompat.getMainExecutor(this)
        biometricPrompt = BiometricPrompt(this, executor, object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                navigateToResultadoAutenticacao("Autenticação bem-sucedida!")
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                Toast.makeText(this@ReconhecimentoBiometrico, "Falha na autenticação. Tente novamente.", Toast.LENGTH_SHORT).show()
            }

            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                Toast.makeText(this@ReconhecimentoBiometrico, "Erro na autenticação: $errString", Toast.LENGTH_SHORT).show()
            }
        })

        biometricPromptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Reconhecimento Biométrico")
            .setSubtitle("Autentique-se com sua biometria")
            .setNegativeButtonText("Cancelar")
            .build()

        setContent {
            ReconhecimentoBiometricoScreen { startBiometricAuthentication() }
        }
    }

    private fun isBiometricSupported(): Boolean {
        val biometricManager = BiometricManager.from(this)
        return when (biometricManager.canAuthenticate()) {
            BiometricManager.BIOMETRIC_SUCCESS -> true
            else -> false
        }
    }

    private fun startBiometricAuthentication() {
        biometricPrompt.authenticate(biometricPromptInfo)
    }

    private fun navigateToResultadoAutenticacao(result: String) {
        println(result)
        val intent = Intent(this, ScoreAntiFraude::class.java)
        startActivity(intent)
    }
}

@Composable
fun ReconhecimentoBiometricoScreen(onStartBiometricAuthentication: () -> Unit) {
    MaterialTheme {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    onClick = onStartBiometricAuthentication,
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text("Iniciar Reconhecimento Biométrico")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ReconhecimentoBiometricoScreenPreview() {
    ReconhecimentoBiometricoScreen(onStartBiometricAuthentication = {})
}
