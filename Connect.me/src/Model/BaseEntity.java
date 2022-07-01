package Model;

/**
 *
 * @author Denis
 */
public class BaseEntity {

    protected int ID;

    public void setID(int ID) {
        if (ID >= 0){
            this.ID = ID;
        }
    }

    public int getID(){
        return ID;
    }


    public String toString() {
        return "BaseEntity{" + "ID=" + ID + '}';
    }
}