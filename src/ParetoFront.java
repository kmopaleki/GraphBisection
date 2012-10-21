import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Kmo
 * Date: 10/20/12
 * Time: 6:40 PM
 * To change this template use File | Settings | File Templates.
 */
public class ParetoFront {
    private ArrayList<MemberNode> paretoPopulation = new ArrayList<MemberNode>();
    private double averageFitness;

    public ParetoFront() {
    }

    public ParetoFront(ArrayList<MemberNode> paretoPopulation, double averageFitness) {
        this.paretoPopulation = paretoPopulation;
        this.averageFitness = averageFitness;
    }

    public ArrayList<MemberNode> getParetoPopulation() {
        return paretoPopulation;
    }

    public void setParetoPopulation(ArrayList<MemberNode> paretoPopulation) {
        this.paretoPopulation = paretoPopulation;
    }

    public double getAverageFitness() {
        return averageFitness;
    }

    public void setAverageFitness(double averageFitness) {
        this.averageFitness = averageFitness;
    }
}
