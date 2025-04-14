package com.example.mangasmemorygame

import android.os.Bundle
import android.provider.Telephony
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

@Composable
fun InitialScreen(
    participante1: Participante,
    participante2: Participante,
    onParticipante1Change: (Participante) -> Unit,
    onParticipante2Change: (Participante) -> Unit,
    tamanhoTabuleiro: Int?,
    onSelecionarTamanho: (Int) -> Unit,
    onIniciarJogo: () -> Unit
){
    Column(
        modifier = Modifier.fillMaxSize().padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Manga’s Memory Game",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(32.dp))

        // PARTICIPANTE 1
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = participante1.nome,
                onValueChange = { onParticipante1Change(participante1.copy(nome = it)) },
                label = { Text("Participante 1") },
                modifier = Modifier.weight(1f)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = { onParticipante1Change(participante1.copy(cor = "AZUL")) },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Blue),
                modifier = Modifier.size(48.dp)
            ) {}

            Spacer(modifier = Modifier.width(4.dp))

            Button(
                onClick = { onParticipante1Change(participante1.copy(cor = "VERM")) },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                modifier = Modifier.size(48.dp)
            ) {}
        }

        Spacer(modifier = Modifier.height(16.dp))

        // PARTICIPANTE 2
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = participante2.nome,
                onValueChange = { onParticipante2Change(participante2.copy(nome = it)) },
                label = { Text("Participante 2") },
                modifier = Modifier.weight(1f)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = { onParticipante2Change(participante2.copy(cor = "AZUL")) },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Blue),
                modifier = Modifier.size(48.dp)
            ) {}

            Spacer(modifier = Modifier.width(4.dp))

            Button(
                onClick = { onParticipante2Change(participante2.copy(cor = "VERM")) },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                modifier = Modifier.size(48.dp)
            ) {}
        }

        Text("Escolha o tamanho do tabuleiro:", fontSize = 18.sp)
        Spacer(modifier = Modifier.height(8.dp))

        listOf(4, 6, 8, 10).forEach { tamanho ->
            val isSelecionado = tamanhoTabuleiro == tamanho

            Button(
                onClick = { onSelecionarTamanho(tamanho) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                colors = ButtonDefaults.buttonColors(
                        containerColor = if (isSelecionado) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.secondaryContainer,
                contentColor = if (isSelecionado) Color.White else Color.Black
            ),
            ) {
                Text("${tamanho}x$tamanho")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onIniciarJogo,
            enabled = participante1.nome.isNotBlank() &&
                    participante2.nome.isNotBlank() &&
                    tamanhoTabuleiro != null &&
                    participante1.cor.isNotBlank() &&
                    participante2.cor.isNotBlank() &&
                    participante1.cor != participante2.cor,
            modifier = Modifier.fillMaxWidth().height(56.dp)
        ) {
            Text("Iniciar Jogo", fontSize = 18.sp)
        }
    }
}

