package escalonador;


public class BCP implements Comparable<BCP>{
	private int PC; //Program Counter
	private int estado; //0 = bloqueado 1 = pronto
	private int prioridade;
	private int creditos;
	private int X,Y;
	private int flag;
	private String [] instrucoes;
	private String nome;

	public BCP(String nome, String [] instrucoes, int prioridade) {
		PC = 0;
		X = 0;
		Y = 0;
		estado = 1;
		this.nome = nome;
		this.instrucoes = instrucoes;
		this.prioridade = prioridade;
		this.creditos = prioridade;
	}

	public consomeCredito() {
		creditos--;
	}

	public int compareTo(BCP bcp) {
		if(this.creditos > bcp.creditos) return 1;
		if(this.creditos == bcp.creditos) return 0;
		return -1;
	}
	public void print() {
		for(int i = 0; i < instrucoes.length; i++) System.out.println(instrucoes[i]);
	}
}
