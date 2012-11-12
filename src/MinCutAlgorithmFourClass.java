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
        ArrayList<ParetoNode> bestSolutionArrayList = new ArrayList<ParetoNode>();
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

        double localbest = 0.000;
        double localaverage = 0.0000;

        ParetoFront paretoFront = new ParetoFront();
        for(int i = 0; i<numberOfRuns; i++){
            //Generate Initial Population
            ArrayList<ParetoNode> population = new ArrayList<ParetoNode>();
            int childcounter = populationSize;

            for(int k = 0; k<populationSize; k++){
                ArrayList<Boolean> bitSet = getBitStrings(numVertices);
                population.add(new ParetoNode(bitSet,false));
            }

            //Calculate Fitness Level for each member of the population
            for(int j = 0; j<populationSize;j++){
                population.get(j).setFitnessValueAndNumDem(population.get(j).getBitString(),graph,fitnessFunction,
                (double)penaltyScalar);
            }
            assignLevelOfNonDomAndSort(population);
            int evalCounter = 0;
            int termCounter = 0;
            double prevLocalBest = 0.0000;
            double currentLocalBest;
            outLog.write("Run #: " + (i+1)+"\r\n");

            if(i==0){
                paretoFront.setAverageFitness(getAverageFitness(population));
                paretoFront.setParetoPopulation(population);
            }
            while(evalCounter<numberOfEvals&&!termCondition(termCounter)){
                //Select Parents
                //set selected flags to false
                System.out.println("Run # " + i + " Eval #" + evalCounter);
                setSelectedFlagsToFalse(population);
                ArrayList<ParetoNode> mating_pool = new ArrayList<ParetoNode>();
                if(selectionAlg.equals("Tournament")){
                    kParentNonDomTournamentSelection(mating_pool,population);
                }else if(selectionAlg.equals("TournamentNo")){
                    kParentNonDomTournamentSelectionNo(mating_pool, population);
                }else if(selectionAlg.equals("UniformRandom")){
                    kRandomParentSelection(mating_pool,population);
                }

                //Create Children
                ArrayList<ParetoNode> spawningPool = new ArrayList<ParetoNode>();
                if(ReproductionAlg.equals("Uniform")){
                    UniformCrossover(mating_pool,spawningPool);
                }else if(ReproductionAlg.equals("nPoint")){
                    nPointCrossOver(spawningPool,mating_pool);
                }

                //Mutate Offspring
                bitFlipMutation(spawningPool);

                //Assign fitness Value to children
                for(int k = 0; k<spawningPool.size(); k++){
                    spawningPool.get(k).setFitnessValueAndNumDem(spawningPool.get(k).getBitString(),graph,fitnessFunction,
                          (double)penaltyScalar);
                }

                //Combine Population + Children
                if(survivorStrat.equals("Plus")){
                    population.addAll(spawningPool);
                    assignLevelOfNonDomAndSort(population);
                }else if(survivorStrat.equals("Comma")){
                    population.clear();
                    population.addAll(spawningPool);

                    //now we need to make sure that the population size is still 200
                    //so we duplicate children to fill it up.
                    while(population.size()<populationSize){
                        int randomIndex = randomValue.nextInt(population.size());
                        ParetoNode newMember = population.get(randomIndex);
                        population.add(newMember);
                    }
                    assignLevelOfNonDomAndSort(population);


                }

                //Survival Selection
                if(survivorAlg.equals("UniformRandom")){
                    randomSurvival(population);

                }else if(survivorAlg.equals("Tournament")){
                    kSurvivalNonDomTournamentSelection(population);

                }else if(survivorAlg.equals("TournamentNo")){
                    kSurivalNonDomTournamentSelectionNo(population);
                }else if(survivorAlg.equals("Truncation")){
                    truncation(population);
                }
                assignLevelOfNonDomAndSort(population);

                if(evalCounter%numChildren==0){
                    localbest = getBestFitness(population);
                    localaverage = getAverageFitness(population);
                    outLog.write(childcounter + "\t" + (-1)*localaverage + "\t"+(-1)*localbest + "\r\n");
                    childcounter = childcounter+numChildren;

                }
                evalCounter++;

                if(evalCounter==numberOfEvals){
                    if(getAverageFitness(population)<paretoFront.getAverageFitness()){

                        paretoFront.setAverageFitness(getAverageFitness(population));
                        paretoFront.setParetoPopulation(population);
                    }
                }

            }

        }

        //Write Pareto Front to solution File
        assignLevelOfNonDomAndSort(paretoFront.getParetoPopulation());
        for(int i = 0; i<paretoFront.getParetoPopulation().size();i++){
            if(paretoFront.getParetoPopulation().get(i).getNonDomLevel()==1){
                solutionLog.write(String.valueOf(paretoFront.getParetoPopulation().get(i).getMinNumerator()) + "\t"
                    + String.valueOf(paretoFront.getParetoPopulation().get(i).getMaxDenominator()) + "\t"
                    + paretoFront.getParetoPopulation().get(i).getFitnessValue() + "\t" +
                    getBestSolutionBitString(paretoFront.getParetoPopulation().get(i)) + "\r\n"
                    );
            }
        }

        outLog.close();
        solutionLog.close();
    }

    private void setSelectedFlagsToFalse(ArrayList<ParetoNode> population) {
        for(int i = 0; i<population.size(); i++){
            population.get(i).setBeenParentSelected(false);
            population.get(i).setBeenSelectedSon(false);
        }
    }

    private void kParentNonDomTournamentSelectionNo(ArrayList<ParetoNode> mating_pool, ArrayList<ParetoNode> population) {
        while(mating_pool.size()<numParents){
            int current_best = randomValue.nextInt(population.size());
            while(population.get(current_best).isBeenParentSelected()){
                current_best = randomValue.nextInt(population.size());
            }
            for(int i = 0; i<kParent-1; i++){
                int index = randomValue.nextInt(population.size());
                while(population.get(index).isBeenParentSelected()){
                    index = randomValue.nextInt(population.size());
                }
                if(population.get(index).getNonDomLevel()<population.get(current_best).getNonDomLevel()){
                    current_best = index;
                }
                if(population.get(index).getNonDomLevel()== population.get(current_best).getNonDomLevel()){
                    if(population.get(index).getFitnessValue()<population.get(current_best).getFitnessValue()){
                        current_best = index;
                    }
                }
            }

            mating_pool.add(population.get(current_best));

        }

    }


    private void kParentNonDomTournamentSelection(ArrayList<ParetoNode> mating_pool,ArrayList<ParetoNode> population){
        while(mating_pool.size()<numParents){
            int currentbest = randomValue.nextInt(population.size());
            for(int i = 0; i<kParent-1; i++){
                int index = randomValue.nextInt(population.size());
                if(population.get(index).getNonDomLevel()<population.get(currentbest).getNonDomLevel()){
                    currentbest = index;
                }
                if(population.get(index).getNonDomLevel()== population.get(currentbest).getNonDomLevel()){
                    if(population.get(index).getFitnessValue()<population.get(currentbest).getFitnessValue()){
                        currentbest = index;
                    }
                }
            }
            mating_pool.add(population.get(currentbest));
        }
    }


    private void kRandomParentSelection(ArrayList<ParetoNode> mating_pool, ArrayList<ParetoNode> population) {

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

    private double getBestFitness(ArrayList<ParetoNode> population) {
        double bestFitness = population.get(0).getFitnessValue();
        for(int i = 1; i<population.size(); i++){
            if(population.get(i).getFitnessValue()<bestFitness){
                bestFitness = population.get(i).getFitnessValue();
            }

        }

        return bestFitness;
    }

    private ParetoNode getBestSolution(ArrayList<ParetoNode> bestSolutionArrayList) {
        int current_best = 0;
        for(int i = 0; i<bestSolutionArrayList.size(); i++){
            if(bestSolutionArrayList.get(current_best).getFitnessValue()>bestSolutionArrayList.get(i).getFitnessValue()){
                current_best = i;
            }
        }

        return bestSolutionArrayList.get(current_best);

    }

    private String getBestSolutionBitString(ParetoNode bestSolution){
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

    private void truncation(ArrayList<ParetoNode> population) {
        //Sort the population based on fitness

        ArrayList<ParetoNode> nextGen = new ArrayList<ParetoNode>();
        //find best index
        while(nextGen.size()<populationSize){
            int current_best = 0;
            for(int i = 0; i<population.size(); i++){
                if(population.get(current_best).getNonDomLevel()<population.get(i).getNonDomLevel()){
                    current_best = i;
                }
            }

            nextGen.add(population.get(current_best));
            population.remove(current_best);
        }
        population.clear();
        population.addAll(nextGen);

    }

    private void randomSurvival(ArrayList<ParetoNode> population) {
        //In this one we will kill random people until we achieve the population size
        while(population.size()>populationSize){
            population.remove(randomValue.nextInt(population.size()));
        }

    }



    private double getAverageFitness(ArrayList<ParetoNode> population) {
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


    private void kSurivalNonDomTournamentSelectionNo(ArrayList<ParetoNode> population){
        while(population.size()>populationSize){
            int killIndex = randomValue.nextInt(population.size());
            while(population.get(killIndex).isBeenSelectedSon()){
                killIndex = randomValue.nextInt(population.size());
            }
            for(int i = 0; i<kSurvival; i++){
                int index = randomValue.nextInt(population.size());
                while(population.get(index).isBeenSelectedSon()){
                    index = randomValue.nextInt(population.size());
                }
                if(population.get(index).getNonDomLevel()>population.get(killIndex).getNonDomLevel()){
                    killIndex = index;
                }
                if(population.get(index).getNonDomLevel()== population.get(killIndex).getNonDomLevel()){
                    if(population.get(index).getFitnessValue()>population.get(killIndex).getFitnessValue()){
                        killIndex = index;
                    }
                }
            }
            population.remove(killIndex);
        }
    }

    private void kSurvivalNonDomTournamentSelection(ArrayList<ParetoNode> population) {
        while(population.size()>populationSize){
            int killIndex = randomValue.nextInt(population.size());
            for(int i = 0; i<kSurvival; i++){
                int index = randomValue.nextInt(population.size());
                if(population.get(index).getNonDomLevel()>population.get(killIndex).getNonDomLevel()){
                    killIndex = index;
                }
                if(population.get(index).getNonDomLevel()== population.get(killIndex).getNonDomLevel()){
                    if(population.get(index).getFitnessValue()>population.get(killIndex).getFitnessValue()){
                        killIndex = index;
                    }
                }
            }
            population.remove(killIndex);
        }

    }

    private boolean isAlreadyPicked(int index, ArrayList<ParetoNode> population) {
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

    private void UniformCrossover(ArrayList<ParetoNode> parentPool,
                                  ArrayList<ParetoNode>spawningPool){
        int childCounter = 0;
        while(spawningPool.size()<numChildren){
            int random1 = randomValue.nextInt(parentPool.size());
            ParetoNode parent1 = parentPool.get(random1);
            int random2 = randomValue.nextInt(parentPool.size());
            while(random2==random1){
                random2 = randomValue.nextInt(parentPool.size());
            }
            ParetoNode parent2 = parentPool.get(random2);

            //Create Child 1
            ParetoNode child1 = new ParetoNode();
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

    private void nPointCrossOver(ArrayList<ParetoNode> spawning_pool, ArrayList<ParetoNode> mating_pool){
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
            ParetoNode child = new ParetoNode();
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

    private void bitFlipMutation(ArrayList<ParetoNode> spawningPool){
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

    private void assignLevelOfNonDomAndSort(ArrayList<ParetoNode> sortingPopulation){
        ArrayList<ParetoNode> tempPop = new ArrayList<ParetoNode>();
        //Set all levels to 0
        for(int i = 0;i<sortingPopulation.size();i++){
            sortingPopulation.get(i).setNonDomLevel(0);
        }
        int levelCounter = 1;
        while(sortingPopulation.size()>0){

            for(int i=0; i<sortingPopulation.size(); i++){
                boolean isDominated = false;
                for(int j = 0; j<sortingPopulation.size(); j++){
                    if(sortingPopulation.get(j).getMinNumerator()<sortingPopulation.get(i).getMinNumerator()){
                        if(sortingPopulation.get(j).getMaxDenominator()>=sortingPopulation.get(i).getMaxDenominator()){
                            isDominated = true;
                        }
                    }
                    if(sortingPopulation.get(j).getMaxDenominator()>=sortingPopulation.get(i).getMaxDenominator()){
                        if(sortingPopulation.get(j).getMinNumerator()<sortingPopulation.get(i).getMinNumerator()){
                            isDominated = true;
                        }
                    }
                }

                if(!isDominated){
                    sortingPopulation.get(i).setNonDomLevel(levelCounter);
                }
            }

            //Remove all nonZero levels from population
            for(int i = 0; i<sortingPopulation.size(); i++){
                if(sortingPopulation.get(i).getNonDomLevel()>0){
                    tempPop.add(sortingPopulation.get(i));
                    sortingPopulation.remove(i);
                }
            }
            levelCounter++;
        }
        sortingPopulation.addAll(tempPop);

        mergeSort(sortingPopulation,0,sortingPopulation.size()-1);
    }

    private void mergeSort(ArrayList<ParetoNode> sortingPopulation, int lo, int n){
        int low = lo;
        int high = n;
        if(low >= high){
            return;
        }

        int middle = (low + high)/2;
        mergeSort(sortingPopulation, low,middle);
        mergeSort(sortingPopulation, middle+1,high);
        int end_low = middle;
        int start_high = middle+1;

        while((low <= end_low)&&(start_high<=high)){
            if(sortingPopulation.get(low).getNonDomLevel()<sortingPopulation.get(start_high).getNonDomLevel()){
                low++;
            }
            else{
                ParetoNode temp = sortingPopulation.get(start_high);
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
