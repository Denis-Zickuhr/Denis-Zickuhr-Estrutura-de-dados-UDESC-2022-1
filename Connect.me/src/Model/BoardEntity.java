package Model;

import java.util.ArrayList;

public class BoardEntity extends BaseEntity{

    private BlockEntity[][] blocks = new BlockEntity[4][4];

    /**
     *
     * @author Denis
     */
    public BoardEntity(ArrayList<BlockEntity> blocks){

    }

    public void addBlock(int x, int y, BlockEntity block){
        blocks[x][y] = block;
    }

    @Override
    public String toString() {
        return super.toString();
    }

}
