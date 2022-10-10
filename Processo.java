package escalonador;
//TESTE
/* Classe que representa um Processo
 * Um processo possui um nome, uma lista de instruções para serem executadas e um estado 
 */
	class BCP implements Comparable<BCP>{
	private int PC; //Program Counter
	private int estado; //0 = bloqueado 1 = pronto
	private int prioridade;
	private int creditos;
	private int X,Y;
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
	public int compareTo(BCP bcp) {
		if(this.creditos > bcp.creditos) return 1;
		else if(this.creditos == bcp.creditos) return 0;
		else return -1;
	}
}
	
public class Processo {
	static final char PRONTO = 'p';
	static final char EXECUTANDO = 'e';
	static final char BLOQUEADO = 'b';
	final String [] instrucao; 
	String nome;
	private char estado;
	
	public Processo(String nome, String[] instrucao, char estado) {
		this.nome = nome;
		this.setEstado(estado);
		this.instrucao = instrucao;
	}

	public char getEstado() {
		return estado;
	}

	public void setEstado(char estado) {
		this.estado = estado;
	}
	
	
}
