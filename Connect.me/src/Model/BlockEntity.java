package Model;

import DataStructure.CircularlyLinkedList;
import org.jetbrains.annotations.NotNull;

/**
 *
 * @author Denis
 */
public class BlockEntity extends BaseEntity{

    private CircularlyLinkedList tubes;
    private final String type;

    public BlockEntity(@NotNull CircularlyLinkedList tubes, @NotNull String type) {
        if (tubes.size() == 4) {
            this.tubes = tubes;
            if (typeIsValid(type)) {
                this.type = type;
            }else{
                throw new IllegalArgumentException("type is invalid");
            }
        }else{
            throw new IllegalArgumentException("tubes.length != 4");
        }
    }

    public CircularlyLinkedList getTubes() {
        return tubes;
    }

    public String getType() {
        return type;
    }

    public boolean RotateBlock(@NotNull String type){
        if(!(type.equals("blue") | type.equals("orange"))){
            System.out.println("Block " + type + " cannot be rotated!");
            return false;
        }else{
            tubes.rotate();
            return true;
        }
    }

    @Override
    public void setID(int ID) {
        super.setID(ID);
    }

    @Override
    public int getID() {
        return super.getID();
    }

    @Override
    public String toString() {
        return super.toString();
    }

    private boolean typeIsValid(String type){
        boolean isValid;
        switch(type){
            case "red":
            case "orange":
            case "blue":
            case "green":
                isValid = true; break;
            default: isValid = false;
        }
        return isValid;
    }

}
