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
        int limiteSuperior = 3, limiteInferior = 0;
        if (this.puzzle != null){
            for (int i = 0; i < 4; i++){
                for (int j = 0; j < 4; j++) {


                    int left = 1 + j, right = -1 + j, up = -1 + i, down = 1 + i;

                    // Verificação axial 1, valida peças a esquerda e acima
                    if (axialVerification(
                            i,
                            j,
                            up >= limiteInferior,
                            left <= limiteSuperior,
                            left,
                            up,
                            0,
                            2,
                            2,
                            4))
                        return false;

                    // Verificação axial 2, valida peças a direita e abaixo
                    if (axialVerification(
                            i,
                            j,
                            down <= limiteSuperior,
                            right >= limiteInferior,
                            right,
                            down,
                            2,
                            0,
                            4,
                            2))
                        return false;
                }
            }
        }
        return true;
    }

    private boolean axialVerification(int i, int j, boolean limiteVerticalValido, boolean limiteHorizontalValido, int horizontal, int vertical, int i2, int i4, int i6, int i7) {
        int block;
        int pairBlock;

        // Verifica o eixo Vertical
        block = Integer.parseInt(puzzle[i][j].substring(i2, i2+1));
        if (limiteVerticalValido & block != 0){
            pairBlock = Integer.parseInt(puzzle[vertical][j].substring(i4, i4+1));
            if (block != pairBlock){
                return true;
            }
        }else if (block != 0){
            return true;
        }

        // Verifica o eixo Horizontal
        block = Integer.parseInt(puzzle[i][j].substring(i2+1, i6));
        if (limiteHorizontalValido & block != 0){
            pairBlock = Integer.parseInt(puzzle[i][horizontal].substring(i4+1, i7));
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
