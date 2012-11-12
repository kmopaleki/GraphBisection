import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Kmo
 * Date: 9/14/12
 * Time: 1:01 AM
 * To change this template use File | Settings | File Templates.
 */
public class PartNode {
    private ArrayList<Boolean> bitString;
    private Double fitnessValue;
    private boolean beenSelectedSon;
    private boolean beenParentSelected;

    public PartNode() {
        this.beenParentSelected = false;
        this.beenSelectedSon = false;
    }

    public PartNode(ArrayList<Boolean> bitString) {
        this.bitString = bitString;
        this.beenParentSelected = false;
        this.beenSelectedSon = false;
    }

    public PartNode(ArrayList<Boolean> bitString, boolean beenSelectedSon,
                    boolean beenParentSelected) {
        this.bitString = bitString;
        this.beenSelectedSon = beenSelectedSon;
        this.beenParentSelected = beenParentSelected;
    }

    public PartNode(ArrayList<Boolean> bitString, Double fitnessValue,
                    boolean beenSelectedSon, boolean beenParentSelected) {
        this.bitString = bitString;
        this.fitnessValue = fitnessValue;
        this.beenSelectedSon = beenSelectedSon;
        this.beenParentSelected = beenParentSelected;
    }

    public ArrayList<Boolean> getBitString() {
        return bitString;
    }

    public void setBitString(ArrayList<Boolean> bitString) {
        this.bitString = bitString;
    }

    public Double getFitnessValue() {
        return fitnessValue;
    }

    public void setFitnessValue(Double fitnessValue) {
        this.fitnessValue = fitnessValue;
    }

    public boolean isBeenSelectedSon() {
        return beenSelectedSon;
    }

    public void setBeenSelectedSon(boolean beenSelectedSon) {
        this.beenSelectedSon = beenSelectedSon;
    }

    public boolean isBeenParentSelected() {
        return beenParentSelected;
    }

    public void setBeenParentSelected(boolean beenParentSelected) {
        this.beenParentSelected = beenParentSelected;
    }
}
