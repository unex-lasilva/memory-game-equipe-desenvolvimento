data class Participante(var nome: String, var cor: String)
var pontosPart01 = 0
var pontosPart02 = 0
var vitoriasPart01 = 0
var vitoriasPart02 = 0
var erros : Int = 0
var tamanhoTabuleiro : Int = 0

fun main() {
    // tamanho dos tabuleiros 4, 6, 8, 10
    val tamanhos : Map<Int, Int> = mapOf(
        1 to 4,
        2 to 6,
        3 to 8,
        4 to 10,
    )
    var escolhaTamanho : Int = 0

    // Perguntar tamanho do tabuleiro para jogador
    while (!tamanhos.containsKey(escolhaTamanho)) {
        print("Escolha o tamanho do tabuleiro:\n1- (4x4)\n2- (6x6)\n3- (8x8)\n4- (10x10)\n> ")
        escolhaTamanho = readln().toInt()
        if ( !tamanhos.containsKey(escolhaTamanho)) {
            print("Por favor, escolha uma das opções de tamanho de tabuleiro disponíveis.\n")
        }
    }
    tamanhoTabuleiro = tamanhos.get(escolhaTamanho)!!


    // Gerar tabuleiro
    val cartasTotais = tamanhoTabuleiro * tamanhoTabuleiro

    val cartas = mutableListOf<String>()
    // 25% são azul..
    // 25% vermelho..
    // 1 é preto..
    // O resto é AMARELO..
    val azuis     = cartasTotais / 4
    val vermelhas = cartasTotais / 4
    val amarelo   = cartasTotais - (azuis + vermelhas + 1)  // 1 = preta

    for (i in 0..< azuis) {
        cartas.add("AZUL")
    }
    for (i in 0..< vermelhas) {
        cartas.add("VERM")
    }
    for (i in 0..< amarelo) {
        cartas.add("AMRL")
    }
    cartas.add("PRET")
    cartas.shuffle()

    // Gerar participantes
    val participantes = getParticipantes()
    val part1 = participantes[0]
    val part2 = participantes[1]

    // JOGO
    var continuarJogando = true
    while (continuarJogando == true) {
        menuPrincipal()
        rodarJogo(part1, part2, cartas)
        print("\nVocê gostaria de continuar jogando?\n> ")
        val escolhaContinuar = readln().lowercase()
        continuarJogando = escolhaContinuar != "n"
    }
    println("Até mais!")
}


fun getParticipantes(): Array<Participante> {
    print("Participante 1, escolha um nome.\n> ")
    var nome1 = readln().trim().uppercase()
    var escolhaCor = ""
    while (escolhaCor != "v" && escolhaCor != "a") {
        print("Participante 1, escolha uma cor : [V]ermelha ou [A]zul.\n> ")
        escolhaCor = readln().lowercase()
    }
    val cor1 = if (escolhaCor == "a") "AZUL" else "VERM"
    println("Participante 1, você ficou com a cor ${cor1}")

    print("Participante 2, escolha um nome.\n> ")
    var nome2 = readln().trim().uppercase()
    val cor2 = if (cor1 == "AZUL") "VERM" else "AZUL"
    println("Participante 2, você ficou com a cor ${cor2}")

    if (nome1.isEmpty()) {
        nome1 = "PARTICIPANTE01"
    }
    if (nome2.isEmpty()) {
        nome2 = "PARTICIPANTE02"
    }
    return(arrayOf(Participante(nome1, cor1), Participante(nome2, cor2)))
}

fun menuPrincipal() {
    var jogoIniciado : Boolean = false
    while (!jogoIniciado) {
        println("==================================")
        println("1. Iniciar\n2. Pontuação\n3. Regras\n4. Sair")
        print("==================================\n> ")
        var escolhaMenu = readln().toInt()

        while (escolhaMenu <= 0 || escolhaMenu > 4) {
            print("Escolha uma opção válida!\n> ")
            escolhaMenu = readln().toInt()
        }

        when (escolhaMenu) {
            1 -> {
                jogoIniciado = true
            }
            2 -> {
                printarPontuacao()
            }
            3 -> {
                printarRegras()
            }
            4 -> {
                sairJogo()
            }
        }
    }
}
fun printarPontuacao() {
    println("Pontos do Participante 01 : ${pontosPart01}")
    println("Vitórias do Participante 01 : ${vitoriasPart01}")
    println("Pontos do Participante 02 : ${pontosPart02}")
    println("Vitórias do Participante 02 : ${vitoriasPart02}")
    Thread.sleep(2000)
}
fun printarRegras() {
    println("Você deve tentar encontrar um par de cartas da mesma cor")
    Thread.sleep(1000)
    println("Sua cor = 5 pontos,\nAmarelo = 1 ponto\nCor do inimigo = 1 ponto")
    Thread.sleep(4000)
    println("Se você encontrar um par com cor do seu inimigo e errar, você perderá 2 pontos")
    Thread.sleep(4000)
    println("Se você encontrar uma carta de fundo preto, perde o jogo.")
    Thread.sleep(4000)
    println("Se você informar uma posição de carta invalida mais de 3 vezes, você perderá a vez")
    Thread.sleep(4000)
    println("Boa sorte!")
    Thread.sleep(1000)
}
fun sairJogo() {
    println("ADEUS")
    kotlin.system.exitProcess(0)
}

fun rodarJogo(part1 : Participante, part2 : Participante, tabuleiro : List<String>) {
    var cartasViradas = mutableListOf<Int>()
    var cartasSelecionadas = mutableListOf<Int>()
    var turnoPart01 = true
    var jogoFinalizado = false

    while (!jogoFinalizado) {
        desenharTela(part1, part2, cartasViradas, cartasSelecionadas, tabuleiro)

        println("PARTICIPANTE : ${if (turnoPart01) part1.nome else part2.nome}")
        val linhaEscolha = getPosicaoValida("DIGITE A POSIÇÃO DA LINHA DA CARTA QUE DESEJA REVELAR:\n> ", tamanhoTabuleiro)
        val colunaEscolha = getPosicaoValida("DIGITE A POSIÇÃO DA COLUNA DA CARTA QUE DESEJA REVELAR:\n> ", tamanhoTabuleiro)

        // Calcular posição
        val posEmTabuleiro = (colunaEscolha - 1) + (tamanhoTabuleiro * (linhaEscolha - 1))
        if (posEmTabuleiro in cartasViradas) {
            println("Carta já virada!")
            continue
        }

        if (erros > 3) {
            // Pular turno
            println("Você perdeu o seu turno por ter escolhido uma posição inválida mais de 3 vezes!")
            Thread.sleep(1000)

            turnoPart01 = !turnoPart01
            erros = 0
            continue
        }

        // Virar carta
        cartasSelecionadas.add(posEmTabuleiro)
        if (cartasSelecionadas.size >= 2) {
            desenharTela(part1, part2, cartasViradas, cartasSelecionadas, tabuleiro)
            val pontos = checarCartas(cartasSelecionadas, tabuleiro, if (turnoPart01) part1.cor else part2.cor)
            if (pontos > 0) {
                cartasViradas.addAll(cartasSelecionadas)
            }

            if (turnoPart01) {
                pontosPart01 = maxOf(0, pontosPart01 + pontos)
            } else {
                pontosPart02 = maxOf(0, pontosPart02 + pontos)
            }

            // carta preta
            if (pontos <= -5) {
                println("Você perdeu o jogo por ter selecionado uma carta preta!")
                Thread.sleep(1000)
                if (turnoPart01) {
                    println("${part2.nome}, você ganhou!")
                    vitoriasPart02 ++
                } else {
                    println("${part1.nome}, você ganhou!")
                    vitoriasPart01 ++
                }
                jogoFinalizado = true
                continue
            }
            // proximo turno
            if (pontos < 0) {
                turnoPart01 = !turnoPart01
            }
            cartasSelecionadas.clear()

            if (cartasViradas.size == tabuleiro.size) {
                println("Todas as cartas foram viradas, o jogo terminou!")
                Thread.sleep(1000)
                if (pontosPart01 > pontosPart02) {
                    println("${part1.nome} ganhou o jogo!")
                    vitoriasPart01 ++
                } else if (pontosPart02 > pontosPart01) {
                    println("${part2.nome} ganhou o jogo!")
                    vitoriasPart02 ++
                } else {
                    println("O jogo terminou num empate!")
                }
                Thread.sleep(1000)
                jogoFinalizado = true
            }
        }
    }
}
fun desenharTela(part1 : Participante, part2 : Participante, cartasViradas : List<Int>, cartasSelecionadas : List<Int>, tabuleiro : List<String>) {
    // desenhar tela
    println("${part1.nome} - ${pontosPart01} pontos   --   ${part2.nome} - ${pontosPart02} pontos")

    var barraSuperior: String = "|   |"
    for (i in 0..<tamanhoTabuleiro) {
        barraSuperior += "|  ${i + 1}   |"
    }
    println(barraSuperior)
    // fileiras
    for (fil in 0..<tamanhoTabuleiro) {
        var fileira = "| ${fil + 1} |"
        // colunas
        for (col in 0..<tamanhoTabuleiro) {
            // Carta virada ou não
            val cartaIndex = (col) + (tamanhoTabuleiro * fil)
            if (cartaIndex in cartasViradas || cartaIndex in cartasSelecionadas) {
                fileira += "| ${tabuleiro[cartaIndex]} |"
            } else {
                fileira += "| *??* |"
            }
        }
        fileira += "\n"
        print(fileira)
    }
}

fun getPosicaoValida(prompt: String, tamanhoTabuleiro: Int): Int {
    var input: Int
    do {
        print(prompt)
        input = readln().toInt()
        if (input < 1 || input > tamanhoTabuleiro) {
            print("Por favor, escolha uma posição válida entre 1 e ${tamanhoTabuleiro}!!\n> ")
            erros++
        }
    } while (input < 1 || input > tamanhoTabuleiro)
    return input
}
fun checarCartas(selecoes : List<Int>, tabuleiro : List<String>, cor : String): Int {
    val index1 = selecoes[0]
    val index2 = selecoes[1]
    if (tabuleiro[index1] == tabuleiro[index2]) {
        println("ACERTOU!")
        Thread.sleep(1000)

        return when (tabuleiro[index1]) {
            cor -> {
                println("GANHOU 5 PONTOS! CONTINUE JOGANDO.")
                5
            }
            else -> {
                println("GANHOU 1 PONTO! CONTINUE JOGANDO.")
                1
            }
        }
    } else {
        return when (tabuleiro[index1]) {
            "PRET" -> {
                println("PERDEU! VOCÊ SELECIONOU UMA CARTA PREDA, TERMINANDO O JOGO...")
                Thread.sleep(1000)
                -5
            }
            else -> {
                println("ERROU! PERDEU 2 PONTOS. PASSE A VEZ PRO OUTRO!")
                Thread.sleep(1000)
                return -2
            }
        }
    }

}

