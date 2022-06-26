import busca.BuscaLargura;
import busca.BuscaProfundidade;
import busca.Nodo;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class App {
    private JButton btCarregar;
    private JButton btLargura;
    private JButton btProfundidade;
    private JButton btSobre;
    private JPanel panelPipes;
    private JPanel panelMain;
    private JTextArea taImprimeCanos;
    private JScrollPane scrollPane;
    private  Puzzle puzzle;
    public App() {
        btCarregar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JPanel pipesPanel = new JPanel();
                puzzle = new Puzzle(readPuzzlePipesFromFile("pipe_example_1"));
                JOptionPane.showMessageDialog(null,"Arquivo pipe_example_1.txt foi lido");
                taImprimeCanos.append(puzzle.toString());
            }
        });
        btLargura.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                taImprimeCanos.setText("");
                String str =
                        "\n//////////////////\n" +
                        "|BUSCA EM LARGURA|\n" +
                        puzzle.toString() +
                        "\n//////////////////\n";
                Nodo n;

                n = new BuscaLargura<Puzzle>().busca(puzzle);
                if (n == null) {
                    str += "sem solucao!";
                } else {
                    str = "solucao:\n" + n.montaCaminho() + "\n\n";
                    str += "\n Profundidade: " + n.getProfundidade();
                }
                str += "---- FIM BUSCA EM LARGURA ----";

                taImprimeCanos.append(str);
            }
        });
        btProfundidade.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                taImprimeCanos.setText("");
                String str ="\n//////////////////\n" +
                                "|BUSCA EM PROFUNDIDADE|\n" +
                                puzzle.toString() +
                                "\n//////////////////\n";
                Nodo n;
                try {
                    n = new BuscaProfundidade<Puzzle>().busca(puzzle);
                    if (n == null) {
                        str += "sem solucao!";
                    } else {
                        str += "solucao:\n" + n.montaCaminho() + "\n\n";
                        str += "\n Profundidade: " + n.getProfundidade();
                    }
                    str += "---- FIM BUSCA EM PROFUNDIDADE ----";
                    taImprimeCanos.append(str);
                }
                catch (Exception ex){
                    JOptionPane.showMessageDialog(null,"Erro: "+ ex.getMessage());
                }
            }
        });
        btSobre.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String str = "Solu\u00E7\u00E3o de Jogo Puzzle por \u00E1rvores, usando m\u00E9todo de busca por largura e profundidade." +
                        "\nAlunos:" +
                        "\n   Lucas De Lima Teixeira"+
                        "\n   Thiago Farias";
                JOptionPane.showMessageDialog(null, str);
            }
        });
    }

    public static void main(String[] args) {

        JFrame frame = new JFrame("App");
        frame.setContentPane(new App().panelMain);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    private Pipe[][] readPuzzlePipesFromFile(String fileName){
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
