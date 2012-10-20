import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Kmo
 * Date: 9/14/12
 * Time: 12:35 AM
 * To change this template use File | Settings | File Templates.
 */
public class MinCutAlgorithmFourClass {

    private Integer numberOfRuns;
    private Long randomGenSeed;
    private Random randomValue;
    private Integer numberOfEvals;
    private String solutionFile;
    private String logFile;
    private int populationSize;
    private int numChildren;
    private int numParents;
    private int kParent;
    private int kSurvival;
    private int nCrossNum;
    private int termNum;
    private int numVertices;
    private int bitFlipProb;
    private String selectionAlg;
    private String ReproductionAlg;
    private String survivorAlg;
    private String fitnessFunction;
    private String survivorStrat;
    private int penaltyScalar;


    public MinCutAlgorithmFourClass() {
    }

    public MinCutAlgorithmFourClass(int numberOfRuns, long randomGenSeed,
                                    int numberOfEvals,
                                    String solutionFile, String logFile,
                                    int populationSize, int numChildren,
                                    int numParents, int kParent,
                                    int kSurvival, int nCrossNum, int termNum,
                                    int numVertices, int bitFlipProb,
                                    String selectionAlg, String reproductionAlg, String survivorAlg,
                                    String fitnessFunction, String survivorStrat, int penaltyScalar) {
        this.numberOfRuns = numberOfRuns;
        this.randomGenSeed = randomGenSeed;
        this.randomValue = new Random(randomGenSeed);
        this.numberOfEvals = numberOfEvals;
        this.solutionFile = solutionFile;
        this.logFile = logFile;
        this.populationSize = populationSize;
        this.numChildren = numChildren;
        this.numParents = numParents;
        this.kParent = kParent;
        this.kSurvival = kSurvival;
        this.nCrossNum = nCrossNum;
        this.termNum = termNum;
        this.numVertices = numVertices;
        this.bitFlipProb = bitFlipProb;
        this.selectionAlg = selectionAlg;
        this.ReproductionAlg = reproductionAlg;
        this.survivorAlg = survivorAlg;
        this.fitnessFunction = fitnessFunction;
        this.survivorStrat = survivorStrat;
        this.penaltyScalar = penaltyScalar;
    }

    public void NSGA(EaGraph graph, String dataFileName)throws IOException{
        ArrayList<MemberNode> bestSolutionArrayList = new ArrayList<MemberNode>();
        FileWriter theLogFile = new FileWriter(logFile,true);
        FileWriter theSolutionFile = new FileWriter(solutionFile,true);
        BufferedWriter outLog = new BufferedWriter(theLogFile);
        BufferedWriter solutionLog = new BufferedWriter(theSolutionFile);

        outLog.write(dataFileName+"\r\n");

        outLog.write("Random Seed: " + randomGenSeed.toString()+"\r\n");
        outLog.write("Number of Runs: " + numberOfRuns.toString() + " Number of Evaluations per Run: "
                + numberOfEvals.toString()+"\r\n");
        outLog.write("Population Size: " + populationSize + "\r\n");
        outLog.write("# of Parents: " + numParents + "\r\n");
        outLog.write("# of Children: " + numChildren + "\r\n");
        outLog.write("k Parent: " + kParent + "\r\n");
        outLog.write("k Survival " + kSurvival + "\r\n");
        outLog.write("n for N Point CrossOver: " + nCrossNum + "\r\n");
        outLog.write("N termination constant: " + termNum + "\r\n");
        outLog.write("Probability of a bit Flip: " + bitFlipProb + "\r\n");
        outLog.write("Selection Alg: " + selectionAlg + "\r\n");
        outLog.write("Reproduction Alg: " + ReproductionAlg + "\r\n");
        outLog.write("Surivivor Selection Alg: " + survivorAlg + "\r\n");
        outLog.write("Fitness Function: " + fitnessFunction + "\r\n");
        outLog.write("Result Log"+"\r\n");
        for(int i = 0; i<1; i++){
            //Generate Initial Population
            ArrayList<MemberNode> population = new ArrayList<MemberNode>();

            for(int k = 0; k<populationSize; k++){
                ArrayList<Boolean> bitSet = getBitStrings(numVertices);
                population.add(new MemberNode(bitSet,false));
            }

            //Calculate Fitness Level for each member of the population
            for(int j = 0; j<populationSize;j++){
                population.get(j).setFitnessValueAndNumDem(population.get(j).getBitString(),graph,fitnessFunction,
                (double)penaltyScalar);
            }
            assignLevelOfNonDomAndSort(population);

            for(int j = 0; j<numberOfEvals; j++){
                //Select Parents
                ArrayList<MemberNode> mating_pool = new ArrayList<MemberNode>();
                if(selectionAlg.equals("Tournament")){
                    kParentNonDomTournamentSelection(mating_pool,population);
                }else if(selectionAlg.equals("TournamentNo")){
                    kParentNonDomTournamentSelectionNo(mating_pool, population);
                }else if(selectionAlg.equals("UniformRandom")){
                    kRandomParentSelection(mating_pool,population);
                }

                //Create Children
                ArrayList<MemberNode> spawningPool = new ArrayList<MemberNode>();
                if(ReproductionAlg.equals("Uniform")){
                    UniformCrossover(mating_pool,spawningPool);
                }else if(ReproductionAlg.equals("nPoint")){
                    nPointCrossOver(spawningPool,mating_pool);
                }

                //Mutate Offspring
                bitFlipMutation(spawningPool);

                //Assign fitness Value to children
                for(int k = 0; k<spawningPool.size(); k++){
                    spawningPool.get(j).setFitnessValueAndNumDem(spawningPool.get(j).getBitString(),graph,fitnessFunction,
                          (double)penaltyScalar);
                }
                //Combine Population + Children

                population.addAll(spawningPool);
                assignLevelOfNonDomAndSort(population);

                //Survival Selection
                ArrayList<MemberNode> nextGeneration = new ArrayList<MemberNode>();
                if(survivorAlg.equals("UniformRandom")){
                    randomSurvival(population);

                }else if(survivorAlg.equals("Tournament")){
                    kSurvivalNonDomTournamentSelection(population,nextGeneration);

                }else if(survivorAlg.equals("TournamentNo")){
                    kSurivalNonDomTournamentSelectionNo(population,nextGeneration);
                }
                assignLevelOfNonDomAndSort(population);

            }

        }
    }

    private void kParentNonDomTournamentSelectionNo(ArrayList<MemberNode> mating_pool, ArrayList<MemberNode> population) {
        int parentCounter = 0;
        while(parentCounter<numParents){

        }

    }


    private void kParentNonDomTournamentSelection(ArrayList<MemberNode> mating_pool,ArrayList<MemberNode> population){

    }


    private void kRandomParentSelection(ArrayList<MemberNode> mating_pool, ArrayList<MemberNode> population) {

        while(mating_pool.size()<numParents){
            mating_pool.add(population.get(randomValue.nextInt(population.size())));
        }
    }

    private boolean termCondition(int evalCounter) {
        if(termNum==0){
            return false;

        }else{
            if(evalCounter==termNum){
                return true;
            }
            return false;
        }
    }

    private double getBestFitness(ArrayList<MemberNode> population) {
        double bestFitness = population.get(0).getFitnessValue();
        for(int i = 1; i<population.size(); i++){
            if(population.get(i).getFitnessValue()<bestFitness){
                bestFitness = population.get(i).getFitnessValue();
            }

        }

        return bestFitness;
    }

    private MemberNode getBestSolution(ArrayList<MemberNode> bestSolutionArrayList) {
        int current_best = 0;
        for(int i = 0; i<bestSolutionArrayList.size(); i++){
            if(bestSolutionArrayList.get(current_best).getFitnessValue()>bestSolutionArrayList.get(i).getFitnessValue()){
                current_best = i;
            }
        }

        return bestSolutionArrayList.get(current_best);

    }

    private String getBestSolutionBitString(MemberNode bestSolution){
        String result = "";

        for(int i = 0; i<bestSolution.getBitString().size(); i++){
            if(bestSolution.getBitString().get(i)){
                result= result + "1";
            }else if(!bestSolution.getBitString().get(i)){
                result = result + "0";
            }
        }
        return result;
    }

    private void truncation(ArrayList<MemberNode> population) {
        //Sort the population based on fitness
        ArrayList<MemberNode> nextGen = new ArrayList<MemberNode>();
        //find best index
        while(nextGen.size()<populationSize){
            int current_best = 0;
            for(int i = 0; i<population.size(); i++){
                if(population.get(current_best).getFitnessValue()>population.get(i).getFitnessValue()){
                    current_best = i;
                }
            }

            nextGen.add(population.get(current_best));
            population.remove(current_best);
        }
        population.clear();
        population.addAll(nextGen);

    }

    private void randomSurvival(ArrayList<MemberNode> population) {
        //In this one we will kill random people until we achieve the population size
        while(population.size()>populationSize){
            population.remove(randomValue.nextInt(population.size()));
        }

    }



    private double getAverageFitness(ArrayList<MemberNode> population) {
        double fitnessValueSum = 0.00000;

        for(int i = 0; i<population.size(); i++){
            fitnessValueSum = fitnessValueSum + population.get(i).getFitnessValue();
        }

        return fitnessValueSum/population.size();
    }

    public ArrayList<Boolean> getBitStrings(int size){
        ArrayList<Boolean> bitSet = new ArrayList<Boolean>();
        for(int i = 0; i<size+1; i++){
            int rando = randomValue.nextInt(2);
            if(rando==1){
                bitSet.add(true);
            }else if(rando==0){
                bitSet.add(false);
            }
        }
        return bitSet;
    }

    private void kParentTournamentSelection(ArrayList<MemberNode> possibleParentPop, ArrayList<MemberNode> population){
    }

    private void kSurivalNonDomTournamentSelectionNo(ArrayList<MemberNode> population, ArrayList<MemberNode> nextGeneration){
    }

    private void kSurvivalNonDomTournamentSelection(ArrayList<MemberNode> population,ArrayList<MemberNode> nextGeneration) {

    }

    private boolean isAlreadyPicked(int index, ArrayList<MemberNode> population) {
        if(population.get(index).getBeenSelectedSon()){
            return true;
        }
        return false;
    }

    private boolean isAlreadyPicked(ArrayList<Integer> kIndices, int index) {
        for(int i = 0; i<kIndices.size(); i++){
            if(kIndices.get(i).equals(index)){
                return true;
            }
        }
        return false;
    }

    private void UniformCrossover(ArrayList<MemberNode> parentPool,
                                  ArrayList<MemberNode>spawningPool){
        int childCounter = 0;
        while(spawningPool.size()<numChildren){
            int random1 = randomValue.nextInt(parentPool.size());
//            MemberNode parent1 = new MemberNode(parentPool.get(random1).getBitString(),parentPool.get(random1).getFitnessValue());
            MemberNode parent1 = parentPool.get(random1);
            int random2 = randomValue.nextInt(parentPool.size());
            while(random2==random1){
                random2 = randomValue.nextInt(parentPool.size());
            }
//            MemberNode parent2 = new MemberNode(parentPool.get(random2).getBitString(), parentPool.get(random2).getFitnessValue());
            MemberNode parent2 = parentPool.get(random2);

            //Create Child 1
            MemberNode child1 = new MemberNode();
            if((numChildren - childCounter) > 0){
                for(int i = 0; i<parent1.getBitString().size(); i++){
                    double probability = ((double)(randomValue.nextInt(populationSize)))/((double)populationSize);
                    if(probability<=0.5){
                        child1.getBitString().add(parent1.getBitString().get(i));
                    }else{
                        child1.getBitString().add(parent2.getBitString().get(i));
                    }

                }

            }
            spawningPool.add(child1);
            childCounter++;
        }
    }

    private void nPointCrossOver(ArrayList<MemberNode> spawning_pool, ArrayList<MemberNode> mating_pool){
        while(spawning_pool.size()<numChildren){
            ArrayList<Integer> randomInts = new ArrayList<Integer>();
            for(int i = 0; i<nCrossNum; i++){
                int index = randomValue.nextInt(mating_pool.get(0).getBitString().size());
                while(isAlreadyPicked(randomInts,index)){
                    index = randomValue.nextInt(mating_pool.get(0).getBitString().size());
                }
                randomInts.add(index);
            }

            boolean  flipper = false;
            //Pick two parents
            int index1= randomValue.nextInt(mating_pool.size());
            int index2= randomValue.nextInt(mating_pool.size());

            //Create a child
            MemberNode child = new MemberNode();
            while(index1==index2){
                index2 = randomValue.nextInt(mating_pool.size());
            }

            for(int i = 0; i<mating_pool.get(index1).getBitString().size(); i++){
                //setflipper
                if(isTimeToFlip(i,randomInts)){
                    if(flipper){
                        flipper = false;
                    }else if(!flipper){
                        flipper = true;
                    }
                }
                if(!flipper){
                    child.getBitString().add(mating_pool.get(index1).getBitString().get(i));
                }else if(flipper){
                    child.getBitString().add(mating_pool.get(index2).getBitString().get(i));
                }
            }

            spawning_pool.add(child);

        }

    }

    private void bitFlipMutation(ArrayList<MemberNode> spawningPool){
        for(int i = 0; i<spawningPool.size(); i++){
            for(int j = 0; j<spawningPool.get(i).getBitString().size(); j++){
                int rando = randomValue.nextInt(100);
                if(rando <= bitFlipProb){
                    if(spawningPool.get(i).getBitString().get(j)){
                        spawningPool.get(i).getBitString().set(j,false);
                    }else if(!spawningPool.get(i).getBitString().get(j)){
                        spawningPool.get(i).getBitString().set(j,true);
                    }

                }
            }
        }

    }

    private boolean isTimeToFlip(int i,ArrayList<Integer> nPoints){
        for(int j = 0; j<nPoints.size();j++){
            if(nPoints.get(j).equals(i)){
                return true;
            }
        }
        return  false;
    }

    private void assignLevelOfNonDomAndSort(ArrayList<MemberNode> sortingPopulation){
        //AssignLevel of NonDomination
        //The more members of the population this dominates, the higher
        //level of non-domination
        for(int i =0 ;i <sortingPopulation.size(); i++){
            int nonDomCount = 0;
            for(int j = 0; j<sortingPopulation.size(); j++){
                if(dominates(sortingPopulation.get(i), sortingPopulation.get(j))){
                    ++nonDomCount;
                }
            }
            sortingPopulation.get(i).setNonDomCount(nonDomCount);
        }

        //Sort
        mergeSort(sortingPopulation,0,sortingPopulation.size()-1);

        //Assign Levels based on index in population after sort
        for(int i=0; i<sortingPopulation.size(); i++){
            sortingPopulation.get(i).setNonDomLevel(i+1);
        }



    }

    private boolean dominates(MemberNode memberNode, MemberNode memberNode1) {

        if(memberNode.getMinNumerator()<memberNode1.getMinNumerator()){
            if(memberNode.getMaxDenominator()>memberNode1.getMaxDenominator()){
                return true;
            }
        }
        return false;
    }

    private void mergeSort(ArrayList<MemberNode> sortingPopulation, int lo,int n){
        int low = lo;
        int high = n;
        if(low >= high){
            return;
        }

        int middle = (low + high)/2;
        mergeSort(sortingPopulation,low,middle);
        mergeSort(sortingPopulation,middle+1,high);
        int end_low = middle;
        int start_high = middle+1;
        while((lo <= end_low)&&(start_high<=high)){
            if(sortingPopulation.get(low).getNonDomCount()>sortingPopulation.get(start_high).getNonDomCount()){
                low++;
            }else{
                MemberNode temp = sortingPopulation.get(start_high);
                for(int k = start_high-1; k>=low; k--){
                    sortingPopulation.set(k+1,sortingPopulation.get(k));
                }
                sortingPopulation.set(low,temp);
                low++;
                end_low++;
                start_high++;
            }
        }

    }


    /**
     * Below this line is nothing but getters and setters
     */
}
