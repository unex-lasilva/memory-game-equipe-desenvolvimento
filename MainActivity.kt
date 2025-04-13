package com.example.mangasmemorygame

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import com.example.mangasmemorygame.ui.theme.MangasMemoryGameTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MangasMemoryGameTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    MainScreen()
                }
            }
        }
    }
}

@Composable
fun MainScreen() {
    var jogoIniciado by remember { mutableStateOf(false) }
    var participante1 by remember { mutableStateOf(Participante("","")) }
    var participante2 by remember { mutableStateOf(Participante("","")) }
    var tamanhoTabuleiro by remember { mutableStateOf<Int?>(null) }

    if (jogoIniciado && tamanhoTabuleiro != null) {
        GameScreen(
            participante1 = participante1,
            participante2 = participante2,
            tamanhoTabuleiro = tamanhoTabuleiro!!
        )
    } else {

        InitialScreen(
            participante1 = participante1,
            participante2 = participante2,
            onParticipante1Change = { participante1 = it },
            onParticipante2Change = { participante2 = it },
            tamanhoTabuleiro = tamanhoTabuleiro,
            onSelecionarTamanho = { tamanhoTabuleiro = it },
            onIniciarJogo = { jogoIniciado = true }
        )
    }
}


data class Participante(
    val nome: String,
    val cor: String // Pode ser "AZUL" ou "VERM"
)

//