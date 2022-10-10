package escalonador;

/* Classe que representa a Tabela de Processos
 * A tabela de processos possui duas listas de processos, prontos e bloqueados 
 */

public class TabelaProcessos {
	static sortedlist prontos = new sortedlist();
	static sortedlist bloqueados = new sortedlist();	

	public static void adicionaBlocoProntos(BCP bloco) {
		prontos.insertSorted(bloco);
	}
	
	public static void adicionaBlocoBloqueados (BCP bloco) {
		bloqueados.insertSorted(bloco);
	}
	
	public static BCP removePrimeiroProntos() {
		return prontos.remove(0);
	}
	
	public static BCP removePrimeiroBloqueados () {
		return bloqueados.remove(0);
	}
}
