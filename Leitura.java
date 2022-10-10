package escalonador;
import java.io.*;

public class Leitura{
	private int prioridade;
	private String name;
	private String [] instrucoes;
	private BCP bcp;
	public Leitura(int numArquivo) {
		try {
			instrucoes = new String[21]; //inicializando o array que guarda o texto do programa
			
			
			BufferedReader in = new BufferedReader(new FileReader(numArquivo + ".txt"));
			String Line = in.readLine();
			name = Line;//nome do arquivo
			
			//lê o programa
			Line = in.readLine();
			for(int i = 0;  Line != null; i++) {
				instrucoes[i] = Line;
				Line = in.readLine();
			}
			prioridade = lePrioridade(numArquivo);
			BCP bcp = new BCP(name, instrucoes, prioridade);
		} catch (FileNotFoundException e) {
			System.out.println("File name not found");
		} catch (IOException e) {
			System.out.println("Invalid File format");
		}
	}
	private int lePrioridade(int numArquivo) throws IOException {
		BufferedReader in;
		in = new BufferedReader(new FileReader("prioridades.txt"));
		int prioridade = 0;
		String Line = new String();
		for(int i = 0; i < numArquivo; i++) {
			Line = in.readLine();
			prioridade = Integer.parseInt(Line);
		}
		in.close();
		return prioridade;
	}
	public BCP getBCP() {
		return bcp;
	}
}