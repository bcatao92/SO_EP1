package escalonador;

import java.io.*;

public class Leitura {
    private int prioridade;
    private String nome;
    private String[] instrucoes;
    private BCP bcp;

    public Leitura(String numArquivo) {
        try {
            instrucoes = new String[21]; // inicializando o array que guarda o texto do programa

            BufferedReader in = new BufferedReader(new FileReader(numArquivo + ".txt"));
            String Line = in.readLine();
            nome = Line;// nome do arquivo

            // lê o programa
            Line = in.readLine();
            for (int i = 0; Line != null; i++) {
                instrucoes[i] = Line;
                Line = in.readLine();
            }
            prioridade = lePrioridade(numArquivo);
            // System.out.println(prioridade + nome);
            bcp = new BCP(nome, instrucoes, prioridade);
            in.close();
        } catch (FileNotFoundException e) {
            System.out.println("File nome not found");
            System.out.println(numArquivo + ".txt");
        } catch (IOException e) {
            System.out.println("Invalid File format");
        }
    }

    private int lePrioridade(String numArquivo) throws IOException {
        BufferedReader in;
        in = new BufferedReader(new FileReader("prioridades.txt"));
        int prioridade = 0;
        String Line = new String();
        for (int i = 0; i < Integer.parseInt(numArquivo); i++) {
            Line = in.readLine();
            prioridade = Integer.parseInt(Line);
        }
        in.close();
        return prioridade;
    }

    public BCP getBCP() {
        return this.bcp;
    }
}
