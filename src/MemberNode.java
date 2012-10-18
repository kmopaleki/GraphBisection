import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Kmo
 * Date: 9/14/12
 * Time: 1:01 AM
 * To change this template use File | Settings | File Templates.
 */
public class MemberNode {
    private ArrayList<Boolean> bitString;
    private Double fitnessValue;
    private boolean beenSelectedSon;
    private boolean beenParentSelected;

    public MemberNode() {
        this.bitString = new ArrayList<Boolean>();
        this.beenSelectedSon = false;
        this.beenParentSelected = false;
    }

    public MemberNode(ArrayList<Boolean> bitString,boolean beenSelectedSon) {
        this.bitString = bitString;
        this.beenSelectedSon = beenSelectedSon;
        this.beenParentSelected = false;
    }

    public MemberNode(ArrayList<Boolean> bitString,double fitnessValue){
        this.bitString = bitString;
        this.fitnessValue = fitnessValue;
        this.beenParentSelected = false;
    }

    public MemberNode(ArrayList<Boolean> bitString, Double fitnessValue, boolean beenSelectedSon) {
        this.bitString = bitString;
        this.fitnessValue = fitnessValue;
        this.beenSelectedSon = beenSelectedSon;
        this.beenParentSelected = false;
    }

    public double getFitnessValue() {
        return fitnessValue;
    }

    public void setFitnessValue(double fitnessValue) {
        this.fitnessValue = fitnessValue;
    }

    public ArrayList<Boolean> getBitString() {
        return bitString;
    }

    public void setBitString(ArrayList<Boolean> bitString) {
        this.bitString = bitString;
    }

    public boolean getBeenSelectedSon() {
        return beenSelectedSon;
    }

    public void setBeenSelectedSon(boolean beenSelectedSon) {
        this.beenSelectedSon = beenSelectedSon;
    }

    public void setBeenParentSelected(boolean beenParentSelected) {
        this.beenParentSelected = beenParentSelected;
    }

    public boolean isBeenSelectedSon() {
        return beenSelectedSon;
    }

    public boolean isBeenParentSelected() {
        return beenParentSelected;
    }

    public void setFitnessValue(Double fitnessValue) {
        this.fitnessValue = fitnessValue;
    }
}
