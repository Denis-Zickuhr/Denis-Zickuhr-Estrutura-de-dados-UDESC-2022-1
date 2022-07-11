package Process;

import Model.BlockEntity;

public class MatchWatcher {

    private String[][] puzzle;
    private BlockEntity[] blockEntity;

    public MatchWatcher(BlockEntity[] blockEntity){
        watch(blockEntity);
    }

    public MatchWatcher(String[][] doubleAxisPuzzle){
        watch(doubleAxisPuzzle);
    }

    public void watch(BlockEntity[] blockEntity) {
        this.blockEntity = blockEntity;
        puzzle = convertPuzzle(this.blockEntity);
    }

    public void watch(String[][] doubleAxisPuzzle) {
        this.puzzle = doubleAxisPuzzle;
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

    public boolean puzzleIsConexo(String[][] puzzle){
        int upperBound = 3, lowerBound = 0;
        if (puzzle != null){
            for (int i = 0; i < 4; i++){
                for (int j = 0; j < 4; j++) {

                    int solidoes = 0;
                    int adjacencias = 0;

                    int right = 1 + j, left = -1 + j, up = -1 + i, down = 1 + i;
                if(!puzzle[i][j].equals("0000")){
                    if (up >= lowerBound) {
                        adjacencias++;
                        if (puzzle[up][j].equals("0000")) {
                            solidoes++;
                        }
                    }
                    if (right <= upperBound) {
                        adjacencias++;
                        if (puzzle[i][right].equals("0000")) {
                            solidoes++;
                        }
                    }
                    if (down <= upperBound) {
                        adjacencias++;
                        if (puzzle[down][j].equals("0000")) {
                            solidoes++;
                        }
                    }
                    if (left >= lowerBound) {
                        adjacencias++;
                        if (puzzle[i][left].equals("0000")) {
                            solidoes++;
                        }
                    }
                }
                    if(adjacencias == solidoes & solidoes != 0){return false;}
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
            System.out.println(pairBlock);
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

    private String[][] convertPuzzle(BlockEntity[] puzzle){

        int counter = 0;
        this.puzzle = new String[4][4];
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
                }else{
                    this.puzzle[i][j] = "0000";
                }
                counter++;
            }
        }
        return this.puzzle;
    }

}
