package com.example.mangasmemorygame

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.os.postDelayed
import com.example.mangasmemorygame.ui.theme.MangasMemoryGameTheme
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.logging.Handler

@Composable
fun GameScreen(participante1: Participante, participante2: Participante, tamanhoTabuleiro: Int) {
    val cartasViradas = remember { mutableStateListOf<Int>() }
    val cartasSelecionadas = remember { mutableStateListOf<Int>() }
    var turnoPart01 by remember { mutableStateOf(true) }
    var pontosPart01 by remember { mutableStateOf(0) }
    var pontosPart02 by remember { mutableStateOf(0) }
    var jogoFinalizado by remember { mutableStateOf(false) }
    var vencedor by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text("Tabuleiro: ${tamanhoTabuleiro}x$tamanhoTabuleiro", fontSize = 20.sp)
        Text("Pontuação : ${pontosPart01}x${pontosPart02}", fontSize = 24.sp)

        val totalCartas = tamanhoTabuleiro * tamanhoTabuleiro
        val cartas = remember { gerarCartasAleatorias(totalCartas) }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "$participante1 vs $participante2",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(16.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(tamanhoTabuleiro),  // número de colunas
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(cartas.size) { index ->
                    val carta = cartas[index]
                    val estaVisivel = index in cartasViradas || index in cartasSelecionadas

                    val corBotao = when {
                        !estaVisivel -> Color.Gray
                        carta == "AZUL" -> Color.Blue
                        carta == "VERM" -> Color.Red
                        carta == "AMRL" -> Color.Yellow
                        carta == "PRET" -> Color.Black
                        else -> Color.LightGray
                    }

                    Button(
                        onClick = {
                            if (index !in cartasViradas && index !in cartasSelecionadas) {

                                cartasSelecionadas.add(index)

                                if (cartasSelecionadas.size == 2) {
                                    val i1 = cartasSelecionadas[0]
                                    val i2 = cartasSelecionadas[1]
                                    val c1 = cartas[i1]
                                    val c2 = cartas[i2]

                                    if (c1 == c2) {
                                        val corJogador =
                                            if (turnoPart01) participante1.cor else participante2.cor
                                        val pontos = if (c1 == corJogador) 5 else 1

                                        if (turnoPart01) {
                                            pontosPart01 += pontos
                                        } else {
                                            pontosPart02 += pontos
                                        }

                                        cartasViradas.addAll(cartasSelecionadas)
                                    } else {
                                        if (c1 == "PRET") {
                                            vencedor =
                                                if (turnoPart01) participante2.nome else participante1.nome
                                            jogoFinalizado = true
                                            cartasViradas.addAll(cartasSelecionadas)
                                        } else {
                                            turnoPart01 = !turnoPart01
                                        }
                                    }

                                    // Limpa a seleção independente do resultado
                                    cartasSelecionadas.clear()
                                }
                            }
                        },
                        modifier = Modifier
                            .aspectRatio(1f)
                            .fillMaxWidth()
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = corBotao),

                    ) {
                        if (estaVisivel) {
                            Text(carta)
                        } else {
                            Text("*")
                        }
                    }
                }
            }

            if (!jogoFinalizado) {
                val turno = "Turno de ${if (turnoPart01) participante1.nome else participante2.nome}"
                Text(
                    text = turno,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            } else {
                Text(
                    text = "O vencedor é: $vencedor",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }

        }
    }
}




fun gerarCartasAleatorias(cartasTotais: Int): List<String> {
        val cartas = mutableListOf<String>()
        val azuis = cartasTotais / 4
        val vermelhas = cartasTotais / 4
        val amarelas = cartasTotais - (azuis + vermelhas + 1) // 1 preta

        repeat(azuis)     { cartas.add("AZUL") }
        repeat(vermelhas) { cartas.add("VERM") }
        repeat(amarelas)  { cartas.add("AMRL") }
        cartas.add("PRET")

        cartas.shuffle()
        return cartas
}