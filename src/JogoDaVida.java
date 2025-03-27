
import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

public class JogoDaVida {

    private static final int TAMANHO = 10;
    private static final char VIVO = 'O';
    private static final char MORTO = '.';
    private char[][] tabuleiro = new char[TAMANHO][TAMANHO];

    // Inicializa o tabuleiro com todas as células mortas
    public JogoDaVida() {
        for (int i = 0; i < TAMANHO; i++) {
            for (int j = 0; j < TAMANHO; j++) {
                tabuleiro[i][j] = MORTO;
            }
        }
    }

    // Preenche o tabuleiro aleatoriamente com células vivas ou mortas.
    // Usa Random.nextBoolean() para decidir se cada célula será VIVO ('O') ou MORTO ('.').
    public void iniciarAleatorio() {
        Random random = new Random();
        for (int i = 0; i < TAMANHO; i++) {
            for (int j = 0; j < TAMANHO; j++) {
                tabuleiro[i][j] = random.nextBoolean() ? VIVO : MORTO;
            }
        }
    }

    // Permite ao usuário definir manualmente as células vivas.
    public void configurarManual(Scanner scanner) {
        System.out.println("Digite as coordenadas das células vivas (linha coluna). Digite -1 -1 para finalizar:");

        while (true) {
            int linha = scanner.nextInt();
            int coluna = scanner.nextInt();

            if (linha == -1 && coluna == -1) {
                break;
            }
            if (linha >= 0 && linha < TAMANHO && coluna >= 0 && coluna < TAMANHO) {
                tabuleiro[linha][coluna] = VIVO;
            } else {
                System.out.println("Coordenada fora dos limites! Tente novamente.");
            }
        }
    }

    // Exibe o estado atual do tabuleiro no console.
    public void imprimir() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < TAMANHO; i++) {
            for (int j = 0; j < TAMANHO; j++) {
                sb.append(tabuleiro[i][j]).append(" ");
            }
            sb.append("\n");
        }
        System.out.print(sb.toString());
        System.out.println();
    }

    // Verifica se há pelo menos uma célula viva no tabuleiro.
    public boolean temCelulasVivas() {
        for (int i = 0; i < TAMANHO; i++) {
            for (int j = 0; j < TAMANHO; j++) {
                if (tabuleiro[i][j] == VIVO) {
                    return true;
                }
            }
        }
        return false;
    }

    // Calcula a próxima geração do tabuleiro aplicando as regras do Jogo da Vida.
    public void atualizar() {
        char[][] novoTabuleiro = new char[TAMANHO][TAMANHO];

        for (int i = 0; i < TAMANHO; i++) {
            for (int j = 0; j < TAMANHO; j++) {
                int vizinhosVivos = contarVizinhosVivos(i, j);

                novoTabuleiro[i][j] = aplicarRegras(tabuleiro[i][j], vizinhosVivos);
            }
        }

        tabuleiro = novoTabuleiro;
    }

    // Sobrevive com 2, 3 ou 4 vizinhos
    // Renasce com exatamente 3 ou 5 vizinhos
    // Morre em outros casos
    private char aplicarRegras(char estadoAtual, int vizinhosVivos) {
        if (estadoAtual == VIVO) {
            if (vizinhosVivos == 2 || vizinhosVivos == 3 || vizinhosVivos == 4) {
                return VIVO;
            }
            return MORTO;
        } else {
            if (vizinhosVivos == 3 || vizinhosVivos == 5) {
                return VIVO;
            }
            return MORTO;
        }
    }

    // Conta quantas células vivas existem ao redor de uma célula específica.
    private int contarVizinhosVivos(int linha, int coluna) {
        int contador = 0;

        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                int novaLinha = linha + i;
                int novaColuna = coluna + j;

                if (i == 0 && j == 0) {
                    continue;
                }
                if (novaLinha >= 0 && novaLinha < TAMANHO && novaColuna >= 0 && novaColuna < TAMANHO) {
                    if (tabuleiro[novaLinha][novaColuna] == VIVO) {
                        contador++;
                    }
                }
            }
        }

        return contador;
    }

    // Conta o número total de células vivas no tabuleiro.
    public int contarCelulasVivas() {
        int count = 0;
        for (int i = 0; i < TAMANHO; i++) {
            for (int j = 0; j < TAMANHO; j++) {
                if (tabuleiro[i][j] == VIVO) {
                    count++;
                }
            }
        }
        return count;
    }

    public static void main(String[] args) throws InterruptedException {
        Scanner scanner = new Scanner(System.in);
        JogoDaVida jogo = new JogoDaVida();

        System.out.println("Deseja iniciar de forma aleatória? (s/N)");
        String escolha;
        while (true) {
            escolha = scanner.nextLine().trim().toLowerCase();
            if (escolha.isEmpty()) {
                escolha = "n";
                break;
            }
            if (escolha.equals("s") || escolha.equals("n")) {
                break;
            }
            System.out.println("Por favor, digite 's' para sim ou ENTER/'n' para não:");
        }

        if (escolha.equals("s")) {
            jogo.iniciarAleatorio();
            System.out.println("Modo aleátorio selecionado.");
        } else {
            System.out.println("Modo manual selecionado.");
            jogo.configurarManual(scanner);
        }

        System.out.println("Tabuleiro inicial:");
        jogo.imprimir();

        if (!jogo.temCelulasVivas()) {
            System.out.println("Nenhuma célula viva no tabuleiro. Simulação encerrada.");
            scanner.close();
            return;
        }

        int maxGeracoes = 0;
        while (true) {
            System.out.println("Digite o número máximo de gerações (maior que 0):");
            try {
                maxGeracoes = scanner.nextInt();
                if (maxGeracoes > 0) {
                    break;
                } else {
                    System.out.println("Erro: O número deve ser maior que zero.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Erro: Por favor, digite apenas números inteiros.");
            } finally {
                scanner.nextLine(); // Sempre limpa o buffer
            }
        }
        scanner.nextLine();

        System.out.println("Pressione ENTER para iniciar a simulação...");
        scanner.nextLine();

        for (int i = 0; i < maxGeracoes; i++) {
            System.out.println("Geração " + (i + 1) + " - Células vivas: " + jogo.contarCelulasVivas());
            jogo.atualizar();
            jogo.imprimir();

            if (!jogo.temCelulasVivas()) {
                System.out.println("Todas as células morreram. Simulação encerrada.");
                break;
            }

            Thread.sleep(500);
        }

        scanner.close();
    }
}
