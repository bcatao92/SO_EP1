package escalonador;

import static escalonador.TabelaProcessos.bloqueados;
import static escalonador.TabelaProcessos.prontos;

import java.io.FileOutputStream;
import java.io.PrintStream;

public class Escalonador {

	private static int quantum;
	private static int interrupcoes = 0;
	private static int instrucoes = 0;
	private static int contQuantuns = 0;
	private final static int processos = 10;

	public static void main(String[] args) throws Exception {
		Leitura leitura = new Leitura();
		int cont;
		boolean termino
		boolean suspenso
		String instrucao

		quantum = leitura.getQuantum();

		// criando o arquivo LOG
		if (quantum < 10) {
			System.setOut(new PrintStream( new FileOutputStream("log"+quantum + ".txt")));
		} else {
			System.setOut(new PrintStream( new FileOutputStream("log"+quantum + ".txt")));
		}


		for (BCP bcp : prontos) {
			System.out.println("Carregando processo " + bcp.processo.nome);
		}

		while (prontos.size() > 0 || bloqueados.size() > 0) {
			if (prontos.size() > 0) { // Se tiver algum processo pronto

				cont = 0;
				termino = false;
				suspenso = false;

				BCP atual = TabelaProcessos.tiraPrimeiroPronto();
				atual.consomeCredito();
				atual.processo.setEstado(Estado.EXECUTANDO);
				System.out.println("Executando processo " + atual.processo.nome);

				while (atual != null && cont < quantum) {
					instrucao = atual.processo.instrucao[atual.PC];

					if (instrucao.equals("E/S")) {
						if (atual.flag == 0) {
							System.out.println("E/S iniciada em " +atual.processo.nome);
							entradaSaida(atual);
							suspenso = true;
							break;
						} else {
							atual.flag = 0;
						}
					}

					else if (instrucao.equals("SAIDA")) {
						System.out.println(atual.processo.nome + " terminou. Sendo, X=" + atual.registradorX + " e Y=" + atual.registradorY);
						prontos.remove(atual);
						suspenso = true;
						termino = true;
						break;
					}

					else if (instrucao.contains("X=")) {
						atual.registradorX = Integer.parseInt(instrucao.substring(2));
					}

					else if (instrucao.contains("Y=")) {
						atual.registradorY = Integer.parseInt(instrucao.substring(2));
					}

					atual.PC++;
					cont++;
				}

				if (!termino) {
					if (cont <= 1) {
						System.out.println("Interrompendo processo "	+ atual.processo.nome + " após " + cont + " instrução");
					} else {
						System.out.println("Interrompendo processo "	+ atual.processo.nome + " após " + cont + " instruções");
					}
				}

				interrupcoes++;
				contQuantuns++;
				instrucoes += cont;

				if (!parou) {
					atual.processo.setEstado(Processo.PRONTO);
					TabelaProcessos.adicionaBlocoProntos(atual);
				}
			}

			decrementaBloqueados(); //decrementa a espera de cada processo da lista de bloqueados
			verificaZeroEspera(); //verifica se tem algum processo na lista de bloqueados com 0 de espera
			verificaZeroCreditosProntos(); //verifica se todos os créditos da lista de prontos é 0, se sim já seta com a prioridade
		}

		System.out.println("media de trocas: " + mediaTrocas(interrompido));
		System.out.println("media de instruções: " + mediaInstrucao(instrucaoQuantum, numQuantum));

		System.out.println("quantum: " + quantum);
	}

	private static double mediaTrocas (double interrompido) {
		return interrompido / totalProcessos;
	}

	private static double mediaInstrucao (double instrucaoQuantum, double numQuantum) {
		return instrucaoQuantum / numQuantum;
	}

	private static void entradaSaida(BCP processo) {
		processo.processo.setEstado(Processo.BLOQUEADO);
		TabelaProcessos.adicionaBlocoBloqueados(processo); //adiciona o processo à lista de bloqueados
		int index = bloqueados.indexOf(processo);
		bloqueados.get(index).espera = 2; //atualiza a espera do processo para 2
		bloqueados.get(index).flag = 1; //atualiza o flag para 1, ou seja, a instrução já foi executada
	}

	private static boolean verificaZeroCreditosProntos() {
		for (BCP p : prontos) {
			if (p.credito != 0)	return false; //se algum crédito for diferente de 0, já retorna falso
		}
		//se todos os créditos forem iguais a 0, atualiza todos os créditos à sua prioridade correspondente
		for (BCP p : prontos) {
			p.credito = p.prioridade;
		}
		return true;
	}

	private static void decrementaBloqueados() {
		for (BCP b : bloqueados) {
			if (b.espera > 0) b.espera--; //diminui 1 da espera
		}
	}

	private static void verificaZeroEspera() {
		int cont = 0;

		//conta quantos processos tem a espera igual a zero
		for (BCP b : bloqueados) {
			if (b.espera == 0) {
				b.processo.setEstado(Processo.PRONTO);
				TabelaProcessos.adicionaBlocoProntos(b);
				cont++;
			} else break;
		}

		//remove todos os bloqueados com espera igual a zero
		for (int i = 0; i < cont; i++) {
			TabelaProcessos.removePrimeiroBloqueados();
		}
	}
}
