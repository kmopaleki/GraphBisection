import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Kmo
 * Date: 10/20/12
 * Time: 6:40 PM
 * To change this template use File | Settings | File Templates.
 */
public class ParetoFront {
    private ArrayList<ParetoNode> paretoPopulation = new ArrayList<ParetoNode>();
    private double averageFitness;

    public ParetoFront() {
    }

    public ParetoFront(ArrayList<ParetoNode> paretoPopulation, double averageFitness) {
        this.paretoPopulation = paretoPopulation;
        this.averageFitness = averageFitness;
    }

    public ArrayList<ParetoNode> getParetoPopulation() {
        return paretoPopulation;
    }

    public void setParetoPopulation(ArrayList<ParetoNode> paretoPopulation) {
        this.paretoPopulation = paretoPopulation;
    }

    public double getAverageFitness() {
        return averageFitness;
    }

    public void setAverageFitness(double averageFitness) {
        this.averageFitness = averageFitness;
    }
}
