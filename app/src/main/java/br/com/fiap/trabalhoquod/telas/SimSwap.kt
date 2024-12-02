package br.com.fiap.trabalhoquod.telas

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentActivity
import android.content.Context

class SimSwap : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                FormularioTelefoneScreen { telefone, operadora ->

                    Toast.makeText(
                        this,
                        "Telefone: $telefone, Operadora: $operadora",
                        Toast.LENGTH_LONG
                    ).show()


                    val intent = Intent(this, ScoreAntiFraude::class.java)
                    startActivity(intent)
                }
            }
        }
    }
}

@Composable
fun FormularioTelefoneScreen(onSubmit: (String, String) -> Unit) {
    var telefone by remember { mutableStateOf("") }
    var operadora by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var showError by remember { mutableStateOf(false) }

    // Lista com as operadoras principais no Brasil
    val operadoras = listOf("Vivo", "Claro", "TIM", "Oi")

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OutlinedTextField(
                    value = telefone,
                    onValueChange = { telefone = it },
                    label = { Text("Número de Telefone") },
                    placeholder = { Text("Ex.: (11) 98765-4321") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    modifier = Modifier.fillMaxWidth()
                )

                // Menu suspenso para escolher a operadora
                Box(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = operadora,
                        onValueChange = {},
                        label = { Text("Operadora") },
                        modifier = Modifier.fillMaxWidth(),
                        readOnly = true,
                        trailingIcon = {
                            IconButton(onClick = { expanded = !expanded }) {
                                Icon(
                                    imageVector = Icons.Filled.ArrowDropDown,
                                    contentDescription = null
                                )
                            }
                        }
                    )
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        operadoras.forEach { opcao ->
                            DropdownMenuItem(
                                text = { Text(opcao) },
                                onClick = {
                                    operadora = opcao
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                // Botão para submeter a verificação
                Button(
                    onClick = {
                        if (telefone.isBlank() || operadora.isBlank()) {
                            showError = true
                        } else {
                            onSubmit(telefone, operadora)
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Submeter Verificação")
                }
            }
        }
    }

    // Exibe Snackbar caso ocorra erro
    if (showError) {
        Snackbar(
            action = {
                TextButton(onClick = { showError = false }) {
                    Text("OK")
                }
            },
            modifier = Modifier.padding(16.dp)
        ) {
            Text("Preencha todos os campos!")
        }
    }
}


@Preview(showSystemUi = true)
@Composable
fun FormularioTelefoneScreenPreview() {
    MaterialTheme {
        FormularioTelefoneScreen { telefone, operadora ->
            println("Telefone: $telefone, Operadora: $operadora")
        }
    }
}
