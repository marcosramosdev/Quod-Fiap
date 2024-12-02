package br.com.fiap.trabalhoquod.telas

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.fiap.trabalhoquod.MainActivity
import kotlin.random.Random

class ScoreAntiFraude : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ScoreScreen()
        }
    }
}

@Composable
fun ScoreScreen() {
    var score by remember { mutableStateOf(generateRandomScore()) }
    var message by remember { mutableStateOf(getMessageForScore(score)) }

    LaunchedEffect(score) {
        message = getMessageForScore(score)
    }

    val backgroundColor = getBackgroundColorForScore(score)
    val textColor = if (score <= 199) Color.White else Color.Black
    val context = LocalContext.current

    Surface(modifier = Modifier.fillMaxSize().background(backgroundColor)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Seu Score: $score",
                fontSize = 32.sp,
                color = Color.Black,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = message,
                fontSize = 20.sp,
                color = Color.Black,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(32.dp))

            Button(onClick = {
                val intent = Intent(context, MainActivity::class.java)
                context.startActivity(intent)
            }) {
                Text("Voltar para Tela Inicial")
            }
        }
    }
}

fun getBackgroundColorForScore(score: Int): Color {
    return when (score) {
        in 0..199 -> Color.Red
        in 200..399 -> Color.Yellow
        in 400..599 -> Color(0xFFFFD700)
        in 600..799 -> Color(0xFF008000)
        in 800..1000 -> Color.Green
        else -> Color.Gray
    }
}

fun generateRandomScore(): Int {
    return Random.nextInt(0, 1001)
}

fun getMessageForScore(score: Int): String {
    return when (score) {
        in 0..99 -> "Score Baixo. 😢"
        in 100..199 -> "Score Insuficiente. "
        in 200..299 -> "Score Mediano. Mais informações são necessárias."
        in 300..399 -> "Score razoável"
        in 400..499 -> "Score acima da média. Boa chance de aprovação."
        in 500..599 -> "Score bom. Tudo certo até agora."
        in 600..699 -> "Score ótimo. Você está no caminho certo."
        in 700..799 -> "Excelente score. Aprovação quase garantida!"
        in 800..899 -> "Muito bom! Seu score é excelente!"
        in 900..1000 -> "Excelente! Score máximo 😁!"
        else -> "Score inválido."
    }
}

@Preview(showBackground = true)
@Composable
fun ScoreScreenPreview() {
    ScoreScreen()
}

