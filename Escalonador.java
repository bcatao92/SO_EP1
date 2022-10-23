package escalonador;

import static escalonador.TabelaProcessos.bloqueados;
import static escalonador.TabelaProcessos.prontos;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

public class escalonador {

    private static int quantum;
    private static int interrupcoes = 0;
    private static int instrucoes = 0;
    private static int contQuantuns = 0;
    private final static int processos = 10;

    public static void main(String[] args) throws Exception {
        Leitura leitura = new Leitura(processos);
        quantum = leitura.getQuantum();

        criarLog(quantum);

        for (BCP bcp : prontos) {
            System.out.println("Carregando processo " + bcp.getNome());
        }

        while (prontos.size() > 0 || bloqueados.size() > 0) {
            administraProcesso(TabelaProcessos.removePrimeiroPronto(), quantum);
            decrementaEsperaBloqueados();
            verificaEspera();
            verificaCreditosProntos();
        }
        // TESTE
        System.out.println("media de trocas: " + interrupcoes / processos);

        System.out.println("media de instruções: " + instrucoes / contQuantuns);

        System.out.println("quantum: " + quantum);

    }

    private static void criarLog(int quantum) throws FileNotFoundException {
        if (quantum < 10) {
            System.setOut(new PrintStream(new FileOutputStream("log0" + quantum + ".txt")));
        } else {
            System.setOut(new PrintStream(new FileOutputStream("log" + quantum + ".txt")));
        }
    }

    /*
     * Essa função é responsável por processar cada instrução do programa, além de
     * também
     * definir se o programa foi ou não colocado na fila de bloeuqdos devido a uma
     * chamada de E/S
     */
    private static void administraProcesso(BCP atual, int quantum) {
        int cont = 0;
        atual.termino = false;
        atual.suspenso = false;
        atual.consomeCredito();
        atual.estado = 2;
        System.out.println("Executando processo " + atual.getNome());
        while (cont < quantum) {
            administraInstrucao(atual);
            atual.PC++;
            cont++;
        }
        if (atual.termino) {
            if (cont < 2) {
                System.out.println("Interrompendo " + atual.getNome() + " após " + cont + " instrução");
            } else {
                System.out.println("Interrompendo " + atual.getNome() + " após " + cont + " instruções");
            }
        }

        interrupcoes++;
        contQuantuns++;
        instrucoes += cont;

        if (atual.suspenso) {
            atual.estado = 0;
            TabelaProcessos.adicionaBlocoProntos(atual);
        }
    }

    /*
     * Essa função é responsável por processar cada instrução do programa, além de
     * também
     * definir se o programa foi ou não colocado na fila de bloeuqdos devido a uma
     * chamada de E/S
     */
    private static void administraInstrucao(BCP atual) {
        String instrucao = atual.instrucoes[atual.PC];

        // Processa instruções de E/S
        if (instrucao.equals("E/S")) {
            if (!atual.executado) {
                System.out.println("E/S iniciada em " + atual.nome);
                entradaSaida(atual);
                atual.suspenso = true;
            } else {
                atual.executado = false;
            }
        }

        else if (instrucao.equals("FIM")) {
            System.out.println(atual.getNome() + " terminou. Sendo, X=" + atual.X + " e Y="
                    + atual.Y);
            prontos.remove(atual);
            atual.suspenso = true;
            atual.termino = true;
        }

        // Para instruções que utilizam o registrador X
        else if (instrucao.contains("X=")) {
            atual.X = Integer.parseInt(instrucao.substring(2));
        }

        // Para instruções que utilizam o registrador Y
        else if (instrucao.contains("Y=")) {
            atual.Y = Integer.parseInt(instrucao.substring(2));
        }
    }

    private static void entradaSaida(BCP atual) {
        atual.estado = 0;
        TabelaProcessos.adicionaBlocoBloqueados(atual);
        int i = bloqueados.indexOf(atual);
        bloqueados.get(i).espera = 2;
        bloqueados.get(i).executado = true;
    }

    private static boolean verificaCreditosProntos() {
        for (BCP p : prontos) {
            if (p.getCreditos() != 0)
                return false;
        }
        for (BCP p : prontos) {
            p.setCreditos(p.getPrioridade());
        }
        return true;
    }

    private static void decrementaEsperaBloqueados() {
        for (BCP b : bloqueados) {
            if (b.getEspera() > 0)
                b.setEspera(b.getEspera() - 1);
        }
    }

    private static void verificaEspera() {
        int cont = 0;

        for (BCP b : bloqueados) {
            if (b.getEspera() != 0) {
                break;
            }
            b.estado = 1;
            TabelaProcessos.adicionaBlocoProntos(b);
            cont++;
        }

        for (int i = 0; i < cont; i++) {
            TabelaProcessos.removePrimeiroBloqueado();
        }
    }
}
