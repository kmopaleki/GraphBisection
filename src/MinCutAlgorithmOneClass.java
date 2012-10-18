import java.io.*;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: Kevin
 * Date: 8/29/12
 * Time: 3:14 PM
 * To change this template use File | Settings | File Templates.
 */
public class MinCutAlgorithmOneClass {
    private Integer numberOfRuns;
    private Long randomGenSeed;
    private File filePath;
    private Random randomValue;
    private Integer numberOfEvals;
    private String solutionFile;
    private String logFile;


    public MinCutAlgorithmOneClass(Integer numberOfRuns, Long randomGenSeed,
                                   File filePath,Integer numberOfEvals,
                                   String logFile, String solutionFile) {
        this.numberOfRuns = numberOfRuns;
        this.randomGenSeed = randomGenSeed;
        this.randomValue = new Random(randomGenSeed);
        this.numberOfEvals = numberOfEvals;
        this.filePath = filePath;
        this.logFile = logFile;
        this.solutionFile = solutionFile;
    }

    public void minCutAlgorithmOne(EaGraph graph,String dataFileName) throws IOException {
        int numEdgesCut = 0;
        FileWriter theLogFile = new FileWriter(logFile,true);
        FileWriter theSolutionFile = new FileWriter(solutionFile,true);
        BufferedWriter outLog = new BufferedWriter(theLogFile);
        BufferedWriter solutionLog = new BufferedWriter(theSolutionFile);
        outLog.write(dataFileName+"\r\n");
        
        outLog.write("Random Seed: " + randomGenSeed.toString()+"\r\n");
        outLog.write("Number of Runs: " + numberOfRuns.toString() + " Number of Evaluations per Run: "
                + numberOfEvals.toString()+"\r\n");
        outLog.write("Result Log"+"\r\n");

        for(int i = 0; i<numberOfRuns; i++){
            outLog.write("Run #" + (i+1)+"\r\n");
            solutionLog.write("Run #: " + (i+1)+"\r\n");
            double currentBest = 9999999.99;
            ArrayList<Boolean> bestBitSet = new ArrayList<Boolean>();

            for(int n = 0; n<numberOfEvals; n++){
                System.out.println("Run # "+i+" Eval "+n);
                numEdgesCut = 0;
                ArrayList<Boolean> newBitSet = new ArrayList<Boolean>();
                generateBitSet(newBitSet,randomValue,graph.getSize()+1);


                //look for cuts

                for(int m = 1; m<graph.getEdgeList().size();m++){

                    //if in the EdgeList, the two vertices that are connected
                    //are of difference subgraphs, then a cut will be performed hear
                    if(cutChecker(graph.getEdgeList().get(m).getxVertex(),
                            graph.getEdgeList().get(m).getyVertex(),newBitSet)){
                        //increment the number of edges cut by 1
                        numEdgesCut++;
                    }
                }

                int s1counter = 0;  //Subgraph1 vertex counter
                int s2counter = 0; //Subgraph2 vertex counter
                for(int j = 1; j<newBitSet.size(); j++){
                    //If in the bitString (ArrayList of true or false)
                    //we encounter a false (0) value, we increment the s1 counter by 1
                    //else we have encountered true(1) in the bitString, and we must
                    //increment the s2 counter by 1
                    if(newBitSet.get(j)==false){
                        s1counter++;
                    }
                    else if(newBitSet.get(j)==true){
                        s2counter++;
                    }
                }
                //we must make sure the number of edges cut is 0, so we never dividing by 0,
                //0 edges cut means 0/min(0,rest of vertices), which causes program to
                //throw arithmetic exceptions
                if(numEdgesCut>0){
                    double edgesCut = (double)numEdgesCut;
                    //find the min cut ratio
                    double s1count = (double)s1counter;
                    double s2count = (double)s2counter;
                    double minCutRatio;

                    //finds the min cut ratio
                    minCutRatio =((edgesCut)/(Math.min(s2count,s1count)));

                    if(minCutRatio<currentBest){
                        currentBest = minCutRatio;
                        outLog.write((n+1) + "\t" +((-1)*minCutRatio)+"\r\n");
                        setArrayListsEqual(bestBitSet,newBitSet);
                    }
                    if(n==numberOfEvals-1){
                        solutionLog.write(getBitSetString(bestBitSet,graph.getSize()+1)+"\r\n");
                    }
                }
            }
        }
        outLog.close();
        solutionLog.close();


    }

    private void setArrayListsEqual(ArrayList<Boolean> bestBitSet, ArrayList<Boolean> newBitSet) {
        bestBitSet.clear();
        for(int i = 0; i<newBitSet.size(); i++){
            bestBitSet.add(newBitSet.get(i));
        }
    }

    private boolean cutChecker(Integer s1, Integer s2,ArrayList<Boolean> bitSet){

        if(bitSet.get(s1)!=bitSet.get(s2)){
            return true;
        }
        return false;

    }

    private void generateBitSet(ArrayList<Boolean> bitSet, Random random, int size){

        for(int i = 0; i<size; i++){
            Integer rando = random.nextInt(2);
            if(rando==1){
                bitSet.add(i, true);
            }else if(rando==0){
                bitSet.add(i, false);
            }
        }

    }

    public String getBitSetString(ArrayList<Boolean> theBitSet, int size){
        String bitString = "";
        for(int i = 1; i<theBitSet.size(); i++){
            if(theBitSet.get(i)){
                bitString = bitString + "1";

            }
            else if(!theBitSet.get(i)){
                bitString = bitString + "0";
            }
        }
        return bitString;
    }
}
