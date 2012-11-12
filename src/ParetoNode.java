import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Kmo
 * Date: 9/14/12
 * Time: 1:01 AM
 * To change this template use File | Settings | File Templates.
 */
public class ParetoNode {
    private ArrayList<Boolean> bitString;
    private Double fitnessValue;
    private boolean beenSelectedSon;
    private boolean beenParentSelected;
    private int minNumerator;
    private int maxDenominator;
    private int nonDomLevel;

    public ParetoNode() {
        this.bitString = new ArrayList<Boolean>();
        this.beenSelectedSon = false;
        this.beenParentSelected = false;
    }

    public ParetoNode(ArrayList<Boolean> bitString, boolean beenSelectedSon) {
        this.bitString = bitString;
        this.beenSelectedSon = beenSelectedSon;
        this.beenParentSelected = false;
    }

    public ParetoNode(ArrayList<Boolean> bitString, double fitnessValue){
        this.bitString = bitString;
        this.fitnessValue = fitnessValue;
        this.beenParentSelected = false;
    }

    public ParetoNode(ArrayList<Boolean> bitString, Double fitnessValue, boolean beenSelectedSon) {
        this.bitString = bitString;
        this.fitnessValue = fitnessValue;
        this.beenSelectedSon = beenSelectedSon;
        this.beenParentSelected = false;
    }

    public ParetoNode(Double fitnessValue, int minNumerator,
                      int maxDenominator, int nonDomLevel,
                      ArrayList<Boolean> bitString) {
        this.fitnessValue = fitnessValue;
        this.minNumerator = minNumerator;
        this.maxDenominator = maxDenominator;
        this.nonDomLevel = nonDomLevel;
        this.bitString = bitString;
    }

    public ParetoNode(ArrayList<Boolean> bitString, Double fitnessValue,
                      int minNumerator, int maxDenominator,
                      int nonDomCount, int nonDomLevel) {
        this.bitString = bitString;
        this.fitnessValue = fitnessValue;
        this.minNumerator = minNumerator;
        this.maxDenominator = maxDenominator;
        this.nonDomLevel = nonDomCount;
    }

    public ParetoNode(ArrayList<Boolean> bitString, Double fitnessValue,
                      boolean beenSelectedSon, boolean beenParentSelected,
                      int minNumerator, int maxDenominator, int nonDomLevel) {
        this.bitString = bitString;
        this.fitnessValue = fitnessValue;
        this.beenSelectedSon = beenSelectedSon;
        this.beenParentSelected = beenParentSelected;
        this.minNumerator = minNumerator;
        this.maxDenominator = maxDenominator;
        this.nonDomLevel = nonDomLevel;
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

    public int getMinNumerator() {
        return minNumerator;
    }

    public void setMinNumerator(int minNumerator) {
        this.minNumerator = minNumerator;
    }

    public int getMaxDenominator() {
        return maxDenominator;
    }

    public void setMaxDenominator(int maxDenominator) {
        this.maxDenominator = maxDenominator;
    }

    public int getNonDomLevel() {
        return nonDomLevel;
    }

    public void setNonDomLevel(int nonDomLevel) {
        this.nonDomLevel = nonDomLevel;
    }

    private boolean cutChecker(Integer s1, Integer s2,ArrayList<Boolean> bitSet){

        if(bitSet.get(s1)!=bitSet.get(s2)){
            return true;
        }
        return false;

    }

    public void setFitnessValueAndNumDem(ArrayList<Boolean> bitString, EaGraph graph,String fitnessFunction,Double penaltyScalar) {

        this.nonDomLevel = 0;
        if(fitnessFunction.equals("Original")){
            int numEdgesCut = 0;
            //look for cuts

            for(int m=1; m<graph.getEdgeList().size(); m++){
                if(cutChecker(graph.getEdgeList().get(m).getxVertex(),
                        graph.getEdgeList().get(m).getyVertex(),bitString)){
                    //increment the number of edges cut
                    numEdgesCut++;

                }
            }
            this.minNumerator = numEdgesCut;

            int s1counter = 0;
            int s2counter = 0;
            for(int j = 1; j<bitString.size(); j++){

                if(bitString.get(j)==false){
                    s1counter++;
                }else if(bitString.get(j)==true){
                    s2counter++;
                }
            }

            this.maxDenominator = Math.min(s1counter,s2counter);

            if(numEdgesCut>0){
                double edgesCut = (double)numEdgesCut;
                double s1count = (double)s1counter;
                double s2count = (double)s2counter;
                double minCutRatio;

                //finds the minCutRatio
                minCutRatio = ((edgesCut)/(Math.min(s2count,s1count)));

                this.fitnessValue = minCutRatio;

            }


        }else if(fitnessFunction.equals("Constraint")){
            int numEdgesCut = 0;
            //look for cuts

            for(int m=1; m<graph.getEdgeList().size(); m++){
                if(cutChecker(graph.getEdgeList().get(m).getxVertex(),
                        graph.getEdgeList().get(m).getyVertex(),bitString)){
                    //increment the number of edges cut
                    numEdgesCut++;

                }
            }
            this.minNumerator = numEdgesCut;


            int s1counter = 0;
            int s2counter = 0;
            for(int j = 1; j<bitString.size(); j++){

                if(bitString.get(j)==false){
                    s1counter++;
                }else if(bitString.get(j)==true){
                    s2counter++;
                }
            }

            this.maxDenominator = Math.min(s1counter,s2counter);

            if(numEdgesCut>0){
                double edgesCut = (double)numEdgesCut;
                double s1count = (double)s1counter;
                double s2count = (double)s2counter;
                double minCutRatio;

                //finds the minCutRatio
                minCutRatio = ((edgesCut)/(Math.min(s2count,s1count)));

                this.fitnessValue = minCutRatio;


            }

            //Different graph that makes use of an adjacency list rather than an edgelist
            Graph graph1 = new Graph(graph.getEdgeList(),graph.getSize());
            graph1.buildAdjList(bitString);
            //We get the amount of graphs that are in existance after the cuts have been made
            int graphCount = graph1.graphCount();
            double penalty = ((double)penaltyScalar);
            if(graphCount>2){
                //If we have a graphCount of 2, then it is optimal, so no need for a penalty,
                //but if we have more than 2 we assign a penalty equal to the penaltyscalar x graphCount
                this.fitnessValue = this.fitnessValue + penalty*((double)graphCount);
            }

        }
    }
}
