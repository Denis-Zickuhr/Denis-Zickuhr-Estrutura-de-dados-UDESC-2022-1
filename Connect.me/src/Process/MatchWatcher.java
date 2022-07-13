package Process;

import DataStructure.CircularlyLinkedList;
import Model.BlockEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MatchWatcher {

    private String[][] puzzle;
    private String[][] movimentosValidos;
    private String ordem;
    private int depth;
    private final List<String> blockPool = new ArrayList<>(16);

    public String[][] getPuzzle() {
        return puzzle;
    }

    public MatchWatcher(BlockEntity[] blockEntity){
        watch(blockEntity);
    }

    public void watch(BlockEntity[] blockEntity) {
        puzzle = convertPuzzle(blockEntity);
        blockPool.clear();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                switch (movimentosValidos[i][j]) {
                    case "1" -> blockPool.add("red");
                    case "2" -> blockPool.add("blue");
                    case "3" -> blockPool.add("orange");
                    case "4" -> blockPool.add("green");
                }
            }
        }
        System.out.println(blockPool);
    }

    public boolean win(){
        int upperBound = 3, lowerBound = 0;
        if (this.puzzle != null){
            for (int i = 0; i < 4; i++){
                for (int j = 0; j < 4; j++) {


                    int right = 1 + j, left = -1 + j, up = -1 + i, down = 1 + i;

                    // Verificação axial 1, valida peças a direita e acima
                    if (axialVerification(
                            i,
                            j,
                            up >= lowerBound,
                            right <= upperBound,
                            right,
                            up,
                            new int[]{0, 2, 2, 4}))
                        return false;

                    // Verificação axial 2, valida peças a esquerda e abaixo
                    if (axialVerification(
                            i,
                            j,
                            down <= upperBound,
                            left >= lowerBound,
                            left,
                            down,
                            new int[]{2, 0, 4, 2}))
                        return false;
                }
            }
        }
        return true;
    }

    private boolean axialVerification(int i, int j, boolean goUpperBound, boolean goLowerBound,
                                      int horizontal, int vertical, int[] directionSet) {
        int block;
        int pairBlock;

        // Verifica o eixo Vertical
        block = Integer.parseInt(puzzle[i][j].substring(directionSet[0], directionSet[0]+1));
        if (goUpperBound & block != 0){
            pairBlock = Integer.parseInt(puzzle[vertical][j].substring(directionSet[1], directionSet[1]+1));
            if (block != pairBlock){
                return true;
            }
        }else if (block != 0){
            return true;
        }

        // Verifica o eixo Horizontal
        block = Integer.parseInt(puzzle[i][j].substring(directionSet[0]+1, directionSet[2]));
        if (goLowerBound & block != 0){
            pairBlock = Integer.parseInt(puzzle[i][horizontal].substring(directionSet[1]+1, directionSet[3]));
            if (block != pairBlock){
                return true;
            }

        }else if (block != 0){
            return true;
        }
        return false;
    }

    public String[][] getMovimentosValidos() {
        return movimentosValidos;
    }

    public String getOrdem() {
        return ordem;
    }

    private String[][] convertPuzzle(BlockEntity[] puzzle){

        int counter = 0;
        this.puzzle = new String[4][4];
        this.movimentosValidos = new String[4][4];
        this.ordem = "";
        for (int i = 0; i < puzzle.length / 4; i++){
            for (int j = 0; j < puzzle.length / 4; j++) {

                if (puzzle[counter] != null) {
                    this.puzzle[i][j] = puzzle[counter].getTubes().first().toString();
                    puzzle[counter].getTubes().rotate();
                    this.puzzle[i][j] += puzzle[counter].getTubes().first().toString();
                    puzzle[counter].getTubes().rotate();
                    this.puzzle[i][j] += puzzle[counter].getTubes().first().toString();
                    puzzle[counter].getTubes().rotate();
                    this.puzzle[i][j] += puzzle[counter].getTubes().first().toString();
                    puzzle[counter].getTubes().rotate();
                    switch (puzzle[counter].getType()) {
                        case "red" -> {
                            this.movimentosValidos[i][j] = "1";
                            this.ordem += "0";
                        }
                        case "blue" -> {
                            this.movimentosValidos[i][j] = "2";
                            this.ordem += "0";
                            depth++;
                        }
                        case "orange" -> {
                            this.movimentosValidos[i][j] = "3";
                            this.ordem += "1";
                            depth++;
                        }
                        case "green" -> {
                            this.movimentosValidos[i][j] = "4";
                            this.ordem += "1";
                        }
                    }

                }else{
                    this.puzzle[i][j] = "0000";
                    this.movimentosValidos[i][j] = "0";
                    this.ordem += "0";
                }
                counter++;
            }
        }
        return this.puzzle;
    }

    public int getDepth() {
        if (depth == 1){
            return 4;
        }
        if (depth == 0){
            return 1;
        }
        return (int)Math.pow(depth, 4);
    }

    public String getAsSolution(String[][] puzzle, String order) {

        StringBuilder sol = new StringBuilder("{");
        int counter = 0;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                int rel = Integer.parseInt(String.valueOf(order.charAt(counter)))-1;
                if (rel != -1) {
                    String numbers = puzzle[i][j].replaceAll("", ";").replaceFirst(";", "");
                    sol.append("[").append(numbers).append(blockPool.get(rel)).append("]");
                    if (counter != 15)
                        sol.append(", ");
                }else if (counter != 15) {
                    sol.append("null, ");
                }else{
                    sol.append("null");
                }
                counter++;
            }
        }
        sol.append("}");

        return sol.toString().replaceAll(" ", "");

    }
}
