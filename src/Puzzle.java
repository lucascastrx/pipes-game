import busca.BuscaLargura;
import busca.BuscaProfundidade;
import busca.Estado;
import busca.Nodo;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Puzzle implements Estado {
    private Pipe[][] pipes;
    private int gridSize;

    private final int BOTTOM = 1;
    private final int TOP = 2;
    private final int LEFT = 3;
    private final int RIGHT = 4;

    public Puzzle(Pipe[][] pipes) {
        this.pipes = pipes;
        this.gridSize = pipes[0].length;
    }

    @Override
    public String getDescricao() {
        return "Jogo de Puzzle";
    }

    public boolean ehMeta(){

        if (!validaBordas())
            return false;

        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize ; j++) {
                if (!validaCanoPontasInternas(pipes[i][j],new int[]{i,j}))
                    return false;
            }
        }

        Pipe[][] canosPreenchidos = fluirAgua();
//        Puzzle finalPuzzle = new Puzzle(canosPreenchidos);
//        finalPuzzle.imprimeCanos();
        for (Pipe[] line: canosPreenchidos){
            for (Pipe p : line) {
                if (!p.isFilled)
                    return false;
            }
        }

        return true;

    }

    @Override
    public int custo() {
        return 1;
    }

    @Override
    public List<Estado> sucessores() {

        List<Estado> suc = new LinkedList<>();

        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                Pipe[][] newSucPipes = makePipesClone();
                newSucPipes[i][j].rotate(true);
                Puzzle novoPuzzle = new Puzzle(newSucPipes);

                // otimizacao para evitar cantos invalidos
                int count_valida = 0;
                while (!novoPuzzle.validaCanoBordas(i,j)
                        && count_valida < 4) {
                    count_valida++;
                    newSucPipes[i][j].rotate(true);
                    novoPuzzle = new Puzzle(newSucPipes);
                }

                suc.add(novoPuzzle);
            }

        }

        return suc;
    }

    /**
     * verifica se um estado e igual a outro
     */
    public boolean equals(Object o) {
        if (o instanceof Puzzle) {
            Puzzle e = (Puzzle)o;
            for (int i = 0; i < gridSize; i++) {
                for (int j = 0; j < gridSize; j++) {
                    if (!e.pipes[i][j].equals(this.pipes[i][j]))
                        return false;
                }
            }
            return true;
        }
        return false;
    }

    /**
     * retorna o hashCode desse estado
     */
    public int hashCode() {

        String str = "";
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                str += this.pipes[i][j];
            }
        }
        return (str).hashCode();
    }


    /**
     * confere se os as pontas de canos das bordas do puzzle nao apontam para a borda
     * @return
     */
    private boolean validaBordas(){

        for (int i = 0; i < gridSize; i++) {

            // pontas dos canos validos padrao
            boolean top = true;
            boolean bottom = true;

            // se primeira linha, parte de cima nao pode existir
            if (i == 0)
                top = false;

            // se ultima linha, parte de baixo nao pode existir
            if (i == (gridSize-1))
                bottom = false;

            for (int j = 0; j < gridSize; j++) {
                boolean left = true;
                boolean right = true;

                // se primeira coluna, lado da esquerda nao pode existir
                if (j == 0)
                    left = false;

                // se ultima coluna,  lado da direita nao pode existir
                if (j == (gridSize-1))
                    right = false;

                // valida extremidades do cano
                if (pipes[i][j].bottom && !bottom)
                    return  false;

                if (pipes[i][j].top && !top)
                    return  false;

                if (pipes[i][j].left && !left)
                    return  false;

                if (pipes[i][j].right && !right)
                    return  false;

            }

        }
        return true;
    }

    /**
     * confere se os as pontas de canos das bordas do puzzle nao apontam para a borda
     * @param i
     * @param j
     * @return
     */
    private boolean validaCanoBordas(int i, int j){

        // pontas dos canos validos padrao
        boolean top = true;
        boolean bottom = true;

        boolean left = true;
        boolean right = true;


        // se primeira linha, parte de cima nao pode existir
        if (i == 0)
            top = false;

        // se ultima linha, parte de baixo nao pode existir
        if (i == (gridSize-1))
            bottom = false;

        // se primeira coluna, lado da esquerda nao pode existir
        if (j == 0)
            left = false;

        // se ultima coluna,  lado da direita nao pode existir
        if (j == (gridSize-1))
            right = false;

        // valida extremidades do cano
        if (pipes[i][j].bottom && !bottom)
            return  false;

        if (pipes[i][j].top && !top)
            return  false;

        if (pipes[i][j].left && !left)
            return  false;

        if (pipes[i][j].right && !right)
            return  false;


        return true;
    }

    /**
     * metodo que chama os passos para conferir se os as pontas de canos internas do puzzle estao conectadas a ponta de cano adjacente em cada direcao
     * @param centralPipe
     * @param pipePosition
     * @return
     */
    private boolean validaCanoPontasInternas(Pipe centralPipe, int[] pipePosition) {
        if (!validaCanoPontasInternas(centralPipe,pipePosition, BOTTOM))
            return false;
        if (!validaCanoPontasInternas(centralPipe,pipePosition, TOP))
            return false;
        if (!validaCanoPontasInternas(centralPipe,pipePosition, LEFT))
            return false;
        if (!validaCanoPontasInternas(centralPipe,pipePosition, RIGHT))
            return false;
        return true;
    }

    /**
     * confere se os as pontas de canos internas do puzzle estao conectadas a ponta de cano adjacente em cada direcao
     * @param centralPipe
     * @param pipePosition
     * @param stepDirection
     * @return
     */
    private boolean validaCanoPontasInternas(Pipe centralPipe, int[] pipePosition, int stepDirection){

        if (centralPipe == null)
            return false;

        if (pipePosition[0] >= gridSize
                || pipePosition[1] >= gridSize
                || pipePosition[0] < 0
                || pipePosition[1] < 0)
            return false;

        if (stepDirection == TOP && pipePosition[0] > 0) {
            int[] topPipePosition = pipePosition.clone();
            topPipePosition[0] = pipePosition[0] - 1;
            topPipePosition[1] = pipePosition[1];
            Pipe topPipe = pipes[topPipePosition[0]][topPipePosition[1]];
            if (!(centralPipe.top == topPipe.bottom)) {
                return false;
            }
        }

        if (stepDirection == LEFT  && pipePosition[1] > 0) {
            int[] leftPipePosition = pipePosition.clone();
            leftPipePosition[0] = pipePosition[0];
            leftPipePosition[1] = pipePosition[1] - 1;
            Pipe leftPipe = pipes[leftPipePosition[0]][leftPipePosition[1]];
            if (!(centralPipe.left == leftPipe.right)) {
                return false;
            }
        }

        if (stepDirection == BOTTOM && pipePosition[0] < gridSize-1) {
            int[] bottomPipePosition = pipePosition.clone();
            bottomPipePosition[0] = pipePosition[0] + 1;
            bottomPipePosition[1] = pipePosition[1];
            Pipe bottomPipe = pipes[bottomPipePosition[0]][bottomPipePosition[1]];
            if (!(centralPipe.bottom == bottomPipe.top)) {
                return false;
            }
        }

        if (stepDirection == RIGHT && pipePosition[1] < gridSize-1) {
            int[] rightPipePosition = pipePosition.clone();
            rightPipePosition[0] = pipePosition[0];
            rightPipePosition[1] = pipePosition[1] + 1;
            Pipe rightPipe = pipes[rightPipePosition[0]][rightPipePosition[1]];
            if (!(centralPipe.right == rightPipe.left)){
                return false;
            }
        }

        return true;
    }

    /**
     * metodo auxiliar para criar um clone e evitar alterar os objetos originais do tipo Pipe da matrix
     * @return
     */
    private Pipe[][] makePipesClone(){
        Pipe[][] newPipes = new Pipe[gridSize][gridSize];
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                Pipe p = pipes[i][j];
                newPipes[i][j] = new Pipe(p.isFilled,p.bottom,p.top,p.left,p.right);
            }

        }
        return newPipes;
    }


    /**
     * metodo que inicia a chamada que flui a agua pelo cano central que possui a agua inicialmente
     * @return Pipe[][] canosComAgua
     */
    private Pipe[][] fluirAgua(){
        Pipe[][] canosComAgua =  makePipesClone();;
        Pipe centralPipe = canosComAgua[getCenterPosition()][getCenterPosition()];
        int[] centralPipePosition = new int[]{getCenterPosition(),getCenterPosition()};

        if (!centralPipe.isFilled)
            return this.pipes;

        if (centralPipe.bottom)
            canosComAgua = fluirAguaVizinho(centralPipe,centralPipePosition, BOTTOM, canosComAgua);
        if (centralPipe.top)
            canosComAgua = fluirAguaVizinho(centralPipe,centralPipePosition, TOP, canosComAgua);
        if (centralPipe.left)
            canosComAgua = fluirAguaVizinho(centralPipe,centralPipePosition, LEFT, canosComAgua);
        if (centralPipe.right)
            canosComAgua = fluirAguaVizinho(centralPipe,centralPipePosition, RIGHT, canosComAgua);


        return canosComAgua;
    }

    /**
     * metodo que enche os canos adjacentes conectados ao cano central que possui agua
     * @param centralPipe
     * @param pipePosition
     * @param stepDirection
     * @param canosComAgua
     * @return
     */
    public Pipe[][] fluirAguaVizinho(Pipe centralPipe, int[] pipePosition, int stepDirection, Pipe[][] canosComAgua){


        if (centralPipe == null)
            return canosComAgua;

        if (!centralPipe.isFilled)
            return canosComAgua;

        if (pipePosition == null)
            return canosComAgua;

        if (stepDirection == TOP && centralPipe.top && pipePosition[0] > 0) {
            int[] topPipePosition = pipePosition.clone();
            topPipePosition[0] = pipePosition[0] - 1;
            topPipePosition[1] = pipePosition[1];
            Pipe topPipe = canosComAgua[topPipePosition[0]][topPipePosition[1]];
            if (topPipe.bottom && !topPipe.isFilled) {
                topPipe.setFilled(true);
                canosComAgua = fluirAguaVizinho(topPipe, topPipePosition, TOP, canosComAgua);
                canosComAgua = fluirAguaVizinho(topPipe, topPipePosition, LEFT, canosComAgua);
                canosComAgua = fluirAguaVizinho(topPipe, topPipePosition, RIGHT, canosComAgua);
            }
        }

        if (stepDirection == LEFT && centralPipe.left && pipePosition[1] > 0) {
            int[] leftPipePosition = pipePosition.clone();
            leftPipePosition[0] = pipePosition[0];
            leftPipePosition[1] = pipePosition[1] - 1;
            Pipe leftPipe = canosComAgua[leftPipePosition[0]][leftPipePosition[1]];
            if (leftPipe.right && !leftPipe.isFilled) {
                leftPipe.setFilled(true);
                canosComAgua = fluirAguaVizinho(leftPipe, leftPipePosition, BOTTOM, canosComAgua);
                canosComAgua = fluirAguaVizinho(leftPipe, leftPipePosition, TOP, canosComAgua);
                canosComAgua = fluirAguaVizinho(leftPipe, leftPipePosition, LEFT, canosComAgua);
            }
        }

        if (stepDirection == BOTTOM && centralPipe.bottom && pipePosition[0] < gridSize-1) {
            int[] bottomPipePosition = pipePosition.clone();
            bottomPipePosition[0] = pipePosition[0] + 1;
            bottomPipePosition[1] = pipePosition[1];
            Pipe bottomPipe = canosComAgua[bottomPipePosition[0]][bottomPipePosition[1]];
            if (bottomPipe.top && !bottomPipe.isFilled) {
                bottomPipe.setFilled(true);
                canosComAgua = fluirAguaVizinho(bottomPipe, bottomPipePosition, BOTTOM, canosComAgua);
                canosComAgua = fluirAguaVizinho(bottomPipe, bottomPipePosition, LEFT, canosComAgua);
                canosComAgua = fluirAguaVizinho(bottomPipe, bottomPipePosition, RIGHT, canosComAgua);
            }
        }

        if (stepDirection == RIGHT && centralPipe.right && pipePosition[1] < gridSize-1) {
            int[] rightPipePosition = pipePosition.clone();
            rightPipePosition[0] = pipePosition[0];
            rightPipePosition[1] = pipePosition[1] + 1;
            Pipe rightPipe = canosComAgua[rightPipePosition[0]][rightPipePosition[1]];
            if (rightPipe.left && !rightPipe.isFilled){
                rightPipe.setFilled(true);
                canosComAgua = fluirAguaVizinho(rightPipe, rightPipePosition, BOTTOM, canosComAgua);
                canosComAgua = fluirAguaVizinho(rightPipe, rightPipePosition, TOP, canosComAgua);
                canosComAgua = fluirAguaVizinho(rightPipe, rightPipePosition, RIGHT, canosComAgua);
            }
        }


        return canosComAgua;
    }

    /**
     * chamada de impressao do metodo de string
     */
    public void imprimeCanos(){
        System.out.println(toString());
    }

    /**
     * cria string representando matriz em linhas com canos
     * @return
     */
    @Override
    public String toString() {
        String str = "";
        for (int i = 0; i < gridSize; i++) {
            str += "\n";
            for (int j = 0; j < gridSize; j++) {
                str += this.pipes[i][j].toString();
            }
        }
        return str;
    }

    /**
     * a posicao do cano central, que possui a agua
     * @return
     */
    public int getCenterPosition(){
        return ((int) gridSize/2);
    }


}
