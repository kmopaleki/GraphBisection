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
        Scanner s = new Scanner(inFile);
         int populationSize = 0;
        Long randomSeed = 0L;
         int kSurvival = 0;
         int numParents = 0;
         int kParent = 0;
         int graphSample = 0;
         int partitionSample=0;
         String fitnessFunction = null;
         String survivalAlg = null;
         String survivalStrat = null;
         String selectionAlg = null;
         String mutationAlg = null;
         String recombAlg = null;
         String logFile = null;
         String solutionFile = null;
         String graphSolutionFile = null;
         int penaltyScalar=0;
         int bitFlipProb=0;
         int numChildren=0;
         int graphSize=0;
         int nPoint=0;
         int nCrossNum=0;
         int numberOfEvals=0;
         int numberOfRuns=0;
         int termNum=0;
        int counter = 0;

        while(s.hasNext()){
            if(counter == 0){
                populationSize = Integer.parseInt(s.next());
            }else if(counter ==1){
                randomSeed = Long.parseLong(s.next());
            }else if(counter == 2){
                kSurvival = Integer.parseInt(s.next());
            }else if(counter == 3){
                numParents = Integer.parseInt(s.next());
            }else if(counter == 4){
                kParent = Integer.parseInt(s.next());
            }else if(counter == 5){
                graphSample = Integer.parseInt(s.next());
            }else if(counter == 6){
                partitionSample = Integer.parseInt(s.next());
            }else if(counter == 7){
                fitnessFunction = s.next();
            }else if(counter == 8){
                survivalAlg = s.next();
            }else if(counter == 9){
                survivalStrat = s.next();
            }else if(counter ==10){
                selectionAlg = s.next();
            }else if(counter == 11){
                mutationAlg = s.next();
            }else if(counter == 12){
                recombAlg = s.next();
            }else if(counter == 13){
                logFile = s.next();
            }else if(counter == 14){
                solutionFile = s.next();
            }else if(counter == 15){
                penaltyScalar = Integer.parseInt(s.next());
            }else if(counter == 16){
                bitFlipProb = Integer.parseInt(s.next());
            }else if(counter == 17){
                numChildren = Integer.parseInt(s.next());
            }else if(counter == 18){
                graphSize = Integer.parseInt(s.next());
            }else if(counter == 19){
                nPoint = Integer.parseInt(s.next());
            }else if(counter == 20){
                nCrossNum = Integer.parseInt(s.next());
            }else if(counter == 21){
                numberOfEvals = Integer.parseInt(s.next());
            }else if(counter == 22){
                numberOfRuns = Integer.parseInt(s.next());

            }else if(counter == 23){
                termNum = Integer.parseInt(s.next());
            }else if(counter == 24){
                graphSolutionFile = s.next();
            }
            
            counter++;
        }
        s.close();

        //ASSIGNMENT 1E CRAP

        //Create an instance of the mincut Algorithm
        MinCutAlgorithmFiveClass minCutAlgorithmFiveClass = new MinCutAlgorithmFiveClass(populationSize,kSurvival,
                randomSeed,numParents,kParent,graphSample,partitionSample,fitnessFunction,survivalAlg,
                survivalStrat,selectionAlg,mutationAlg,recombAlg,logFile,solutionFile,penaltyScalar,bitFlipProb,
                numChildren,graphSize,nPoint,nCrossNum,numberOfEvals,numberOfRuns,termNum,graphSolutionFile);


        minCutAlgorithmFiveClass.COEA();
        System.out.println("finished");

    }
}
