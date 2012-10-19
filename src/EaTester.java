import java.io.*;
import java.util.Random;
import java.util.Scanner;

/**
 * Created with IntelliJ IDEA.
 * User: Kevin
 * Date: 8/27/12
 * Time: 8:30 PM
 * To change this template use File | Settings | File Templates.
 */
public class EaTester {
    public static void main(String[] args) throws IOException {
        /**
         * Initialize necessary variables
         */
        //UNCOMMENT BELOW THIS
        File inFile = new File(args[0]);
        File graphFile = null;
        Long randomSeed = null;
        Integer numberOfRuns = null;
        Integer numOfEvals = null;
        Integer populationSize = null;
        int numParents = 0;
        int numChildren = 0;
        int kSelection = 0;
        int kSurvival = 0;
        int nCrossPoint = 0;
        int nTermination = 0;
        String logFile = null;
        String solutionFile = null;
        String selectionAlg = null;
        String recombAlg = null;
        String survivorAlg = null;
        String fitnessFunction = null;
        String survivorStrat = null;
        Scanner s = new Scanner(inFile);
        int counter = 0;
        int bitFlipProb = 0;
        int penaltyScalar = 0;

        while(s.hasNext()){
            if(counter == 0){
                graphFile = new File(s.next());
            }else if(counter == 1){
                randomSeed = Long.parseLong(s.next());
            }else if(counter == 2){
                numberOfRuns = Integer.parseInt(s.next());
            }else if(counter == 3){
                numOfEvals = Integer.parseInt(s.next());
            }else if(counter == 4){
                logFile = s.next();
            }else if(counter == 5){
                solutionFile = s.next();
            }else if(counter == 6){
                populationSize = Integer.parseInt(s.next());
            }else if(counter ==7){
                numParents = Integer.parseInt(s.next());
            }else if(counter == 8){
                numChildren = Integer.parseInt(s.next());
            }else if(counter ==9){
                kSelection = Integer.parseInt(s.next());
            }else if(counter == 10){
                kSurvival = Integer.parseInt(s.next());
            }else if(counter == 11){
                nCrossPoint = Integer.parseInt(s.next());
            }else if(counter == 12){
                nTermination = Integer.parseInt(s.next());
            }else if(counter == 13){
                bitFlipProb = Integer.parseInt(s.next());
            }else if(counter == 14){
                selectionAlg = s.next();
            }else if(counter == 15){
                recombAlg = s.next();
            }else if(counter == 16){
                survivorAlg = s.next();
            }else if(counter == 17){
                fitnessFunction = s.next();
            }else if(counter == 18){
                survivorStrat = s.next();
            }else if(counter == 19){
                penaltyScalar = Integer.parseInt(s.next());
            }
            counter++;
        }

        //ASSIGNMENT 1D CRAP

        //Create a Graph
        EaGraph eaGraph = new EaGraph();
        eaGraph.readFromFile(graphFile);

        //Create an instance of the MinCutAlgorithm
        MinCutAlgorithmFourClass minCutAlgorithmFourClass = new MinCutAlgorithmFourClass(numberOfRuns,randomSeed,
                                numOfEvals,solutionFile,logFile,populationSize,numChildren,numParents,kSelection,kSurvival,
                                nCrossPoint,nTermination,eaGraph.getSize(),bitFlipProb,selectionAlg,recombAlg,survivorAlg,
                                fitnessFunction,survivorStrat,penaltyScalar);

        //perform the mincut Algorithm
        minCutAlgorithmFourClass.minCutAlgorithmTwo(eaGraph,graphFile.getName());

        System.out.println("finished");

    }
}
