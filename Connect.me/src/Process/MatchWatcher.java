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
        return false;
    }

    private String[][] convertPuzzle(BlockEntity[] puzzle){

        int counter = 0;
        this.puzzle = new String[4][4];
        for (int i = 0; i < puzzle.length / 4; i++){
            for (int j = 0; j < puzzle.length / 4; j++) {

                if (puzzle[counter] != null) {
                    this.puzzle[i][j] = puzzle[counter].getTubes().first() + ";";
                    puzzle[counter].getTubes().rotate();
                    this.puzzle[i][j] += puzzle[counter].getTubes().first() + ";";
                    puzzle[counter].getTubes().rotate();
                    this.puzzle[i][j] += puzzle[counter].getTubes().first() + ";";
                    puzzle[counter].getTubes().rotate();
                    this.puzzle[i][j] += puzzle[counter].getTubes().first() + ";";
                    puzzle[counter].getTubes().rotate();
                    this.puzzle[i][j] += puzzle[counter].getType();
                }else{
                    this.puzzle[i][j] = null;
                }
                counter++;
            }
        }
        return null;
    }

}
