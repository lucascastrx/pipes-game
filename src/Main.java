import busca.BuscaLargura;
import busca.BuscaProfundidade;
import busca.Nodo;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        Puzzle puzzle;
        puzzle = new Puzzle(readPuzzlePipesFromFile("pipe_example_1"));
        // puzzle = new Puzzle(readPuzzlePipesFromFile("pipe_example_2"));
        // puzzle = new Puzzle(readPuzzlePipesFromFile("pipe_example_3"));
        //puzzle = new Puzzle(readPuzzlePipesFromFile("cant_solve_example"));

        System.out.println("//////////////////");
        System.out.println("BUSCA EM LARGURA");
        puzzle.imprimeCanos();
        System.out.println("//////////////////");
        Nodo n;

        n = new BuscaLargura<Puzzle>().busca(puzzle);
        if (n == null) {
            System.out.println("sem solucao!");
        } else {
            System.out.println("solucao:\n" + n.montaCaminho() + "\n\n");
            System.out.println(n.getProfundidade());
        }
        System.out.println("---- FIM BUSCA EM LARGURA ----");

        System.out.println("//////////////////");
        System.out.println("BUSCA EM PROFUNDIDADE");
        System.out.println("//////////////////");
        try {
            n = new BuscaProfundidade<Puzzle>().busca(puzzle);
            if (n == null) {
                System.out.println("sem solucao!");
            } else {
                System.out.println("solucao:\n" + n.montaCaminho() + "\n\n");
                System.out.println(n.getProfundidade());
            }
            System.out.println("---- FIM BUSCA EM PROFUNDIDADE ----");
        }
        catch (Exception e){
            System.out.println("Erro");
        }




    }
    private static Pipe[][] readPuzzlePipesFromFile(String fileName){
        List<Pipe> pipesList = new ArrayList<Pipe>();
        int gridSize = 0;

        try {
            File file = new File("./src/files/"+fileName+".txt");
            FileReader reader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(reader);
            String line = bufferedReader.readLine();
            int line_count = 1;
            while (line !=null){
                if (line.contains("filled")){
                    line = bufferedReader.readLine();
                    continue;
                }
                line_count++;
                if (!line.contains("true"))
                    throw new Exception ("Erro na leitura do arquivo, canos configurados de forma errada! Linha "+line_count);
                String[] line_split = line.split(";");
                Pipe pipe = new Pipe(line_split[0].equals("true"),line_split[1].equals("true"),line_split[2].equals("true"),line_split[3].equals("true"),line_split[4].equals("true"));
                pipesList.add(pipe);
                line = bufferedReader.readLine();
            }
            reader.close();
            gridSize = (int) Math.sqrt(pipesList.size());
            if(gridSize <= 0)
                throw new Exception ("Erro na leitura do arquivo, canos configurados de forma errada! Linha " + line_count);
            if(Math.sqrt(pipesList.size())%1 != 0)
                throw new Exception ("Erro na quantidade de linhas, de canos, no arquivo. Total: " + pipesList.size());
            if(!pipesList.get((int)pipesList.size()/2).isFilled)
                throw new Exception ("Erro na leitura do arquivo, cano central vazio. Veja linha: " +(int)(pipesList.size())/2 );
        } catch (FileNotFoundException ex){
            System.out.println("Arquivo nao encontrado!\nErro: " + ex.getMessage());
        } catch (IOException e) {
            System.out.println("Arquivo na leitura do arquivo!\nErro: " + e.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }
        Pipe[][] pipesInput = new Pipe[gridSize][gridSize];
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                pipesInput[i][j] = pipesList.get(i*gridSize+j);
            }
        }

        return pipesInput;
    }
}
