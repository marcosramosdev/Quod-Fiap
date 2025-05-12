package br.com.fiap.trabalhoquod.service

import android.content.Context
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class BiometriaService(private val context: Context) {

    // Verifica se o dispositivo suporta autenticação biométrica
    fun verificarDisponibilidade(): Boolean {
        val biometricManager = BiometricManager.from(context)
        return when (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG)) {
            BiometricManager.BIOMETRIC_SUCCESS -> true
            else -> false
        }
    }

    // Exibe o prompt de autenticação biométrica
    suspend fun autenticarUsuario(activity: FragmentActivity): Result<Boolean> =
        suspendCancellableCoroutine { continuation ->
            val executor = ContextCompat.getMainExecutor(context)

            val callback = object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    continuation.resume(Result.success(true))
                }

                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    continuation.resume(Result.failure(Exception("Erro na autenticação: $errString")))
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    // Não finalizamos a continuation aqui, pois o usuário pode tentar novamente
                }
            }

            val promptInfo = BiometricPrompt.PromptInfo.Builder()
                .setTitle("Autenticação Biométrica")
                .setSubtitle("Confirme sua identidade")
                .setNegativeButtonText("Cancelar")
                .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG)
                .build()

            val biometricPrompt = BiometricPrompt(activity, executor, callback)
            biometricPrompt.authenticate(promptInfo)

            continuation.invokeOnCancellation {
                // Cancela o prompt de autenticação se o coroutine for cancelado
                biometricPrompt.cancelAuthentication()
            }
        }
}