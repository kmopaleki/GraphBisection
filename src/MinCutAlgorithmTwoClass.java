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
public class MinCutAlgorithmTwoClass {

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


    public MinCutAlgorithmTwoClass() {
    }

    public MinCutAlgorithmTwoClass(int numberOfRuns, long randomGenSeed,
                                    int numberOfEvals,
                                   String solutionFile, String logFile,
                                   int populationSize, int numChildren,
                                   int numParents, int kParent,
                                   int kSurvival, int nCrossNum, int termNum,
                                   int numVertices, int bitFlipProb,
                                   String selectionAlg, String reproductionAlg, String survivorAlg,
                                   String fitnessFunction,String survivorStrat,int penaltyScalar) {
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

    public void minCutAlgorithmTwo(EaGraph graph,String dataFileName) throws IOException {

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


        for(int i = 0; i<numberOfRuns; i++){
            //INITIALIZE THE POPULATION
            ArrayList<ParetoNode> population = new ArrayList<ParetoNode>();
            int childcounter = populationSize;

            for(int j = 0; j<populationSize; j++){
                ArrayList<Boolean> bitSet = getBitStrings(numVertices);
                population.add(new ParetoNode(bitSet,false));
            }

            //EVALUATE INITIAL POPULATION
            for(int j = 0; j<populationSize; j++ ){
                population.get(j).setFitnessValue(getFitnessValue(population.get(j).getBitString(),graph));
            }

            int evalCounter = 0;
            int termCounter = 0;
            double prevLocalBest = 0.0000;
            double currentLocalBest;
            outLog.write("Run #: " + (i+1)+"\r\n");
            while(evalCounter<numberOfEvals&&!termCondition(termCounter)){
                System.out.println("Run #" + i + "Eval # " + evalCounter);
                ArrayList<ParetoNode> mating_pool = new ArrayList<ParetoNode>();
                ArrayList<ParetoNode> spawning_pool = new ArrayList<ParetoNode>();
                //SELECT PARENTS
                if(selectionAlg.equals("Tournament")){
                    kParentTournamentSelection(mating_pool, population);
                }else if(selectionAlg.equals("TournmanentNo")){
                    kParentTournamentSelectionNo(mating_pool,population);
                }else if(selectionAlg.equals("Proportion")){
                    fitnessProportionalSelection(population,mating_pool);
                }else if(selectionAlg.equals("UniformRandom")){
                    kRandomParentSelection(mating_pool,population);
                }

                //RECOMBINATION
                if(ReproductionAlg.equals("Uniform")){
                    UniformCrossover(mating_pool,spawning_pool);

                }else if(ReproductionAlg.equals("nPoint")){
                    nPointCrossOver(spawning_pool,mating_pool);

                }

                //MUTATE
                bitFlipMutation(spawning_pool);

                //EVALUATE NEW KIDS
                for(int k = 0; k<spawning_pool.size(); k++){
                    spawning_pool.get(k).setFitnessValue(getFitnessValue(spawning_pool.get(k).getBitString(),graph));
                }


                //SURVIVAL SELECTION
                if(survivorStrat.equals("Plus")){
                    population.addAll(spawning_pool);
                }else if(survivorStrat.equals("Comma")){
                    population.clear();
                    population.addAll(spawning_pool);

                    //now we need to make sure that the population size is still 200
                    //so we duplicate children to fill it up.
                    while(population.size()<populationSize){
                        int randomIndex = randomValue.nextInt(population.size());
                        ParetoNode newMember = new ParetoNode(population.get(randomIndex).getBitString(),population.get(randomIndex).getFitnessValue());
                        population.add(newMember);
                    }

                }



                if(survivorAlg.equals("Tournament")){
                    kSurivalTournamentSelection(population);
                }else if(survivorAlg.equals("Truncation")){
                    truncation(population);
                }else if(survivorAlg.equals("UniformRandom")){
                    randomSurvival(population);
                }else if(survivorAlg.equals("Proportional")){
                    fitnessProportionalSurvivalSelection(population);
                }else if(survivorAlg.equals("TournamentR")){
                    ArrayList<ParetoNode> survivors = new ArrayList<ParetoNode>();
                    kSurvivalTournamentSelectionR(population,survivors);
                    population.clear();
                    population.addAll(survivors);

                }

                if(evalCounter%numChildren==0){
                    localbest = getBestFitness(population);
                    localaverage = getAverageFitness(population);
                    outLog.write(childcounter + "\t" + (-1)*localaverage + "\t" + (-1)*localbest + "\r\n");
                    childcounter = childcounter + numChildren;

                }
                currentLocalBest = getBestFitness(population);
                if(currentLocalBest==prevLocalBest){
                    termCounter++;
                }
                else if(currentLocalBest!=prevLocalBest){
                    prevLocalBest = currentLocalBest;
                    termCounter = 0;
                }

                evalCounter++;
            }

            bestSolutionArrayList.add(getBestSolution(population));
        }

        solutionLog.write(getBestSolutionBitString(getBestSolution(bestSolutionArrayList)));

        outLog.close();
        solutionLog.close();

    }




    private void kParentTournamentSelectionNo(ArrayList<ParetoNode> mating_pool, ArrayList<ParetoNode> population) {
        int parentCounter = 0;
        while(parentCounter<numParents){
            ArrayList<ParetoNode> tempPop = new ArrayList<ParetoNode>();
            ArrayList<Integer> tempIndexPop = new ArrayList<Integer>();
            for(int i = 0; i<kParent; i++){
                int index = randomValue.nextInt(population.size());
                while(population.get(index).isBeenParentSelected()){
                    index = randomValue.nextInt(population.size());
                }
                tempPop.add(population.get(randomValue.nextInt(population.size())));
                tempIndexPop.add(index);
            }

            int current_best = 0;
            for(int i = 0; i<tempPop.size(); i++){
                if(tempPop.get(current_best).getFitnessValue()>tempPop.get(i).getFitnessValue()){
                    current_best = i;
                }
            }

            if((double)randomValue.nextInt(population.size())<(0.6)*((double)population.size())){
                mating_pool.add(tempPop.get(current_best));
            }else{
                int index = randomValue.nextInt(tempPop.size());
                while(index==current_best){
                    index= randomValue.nextInt(tempPop.size());
                }
                mating_pool.add(tempPop.get(index));
                population.get(tempIndexPop.get(index)).setBeenParentSelected(true);
            }
            parentCounter++;
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

    private double getFitnessValue(ArrayList<Boolean> bitString,EaGraph graph) {
        double fitnessValue = 0.000000;

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

             int s1counter = 0;
             int s2counter = 0;
             for(int j = 1; j<bitString.size(); j++){

                if(bitString.get(j)==false){
                    s1counter++;
                }else if(bitString.get(j)==true){
                    s2counter++;
                }
             }

             if(numEdgesCut>0){
                double edgesCut = (double)numEdgesCut;
                double s1count = (double)s1counter;
                double s2count = (double)s2counter;
                double minCutRatio;

                //finds the minCutRatio
                minCutRatio = ((edgesCut)/(Math.min(s2count,s1count)));

                fitnessValue = minCutRatio;
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

            int s1counter = 0;
            int s2counter = 0;
            for(int j = 1; j<bitString.size(); j++){

                if(bitString.get(j)==false){
                    s1counter++;
                }else if(bitString.get(j)==true){
                    s2counter++;
                }
            }

            if(numEdgesCut>0){
                double edgesCut = (double)numEdgesCut;
                double s1count = (double)s1counter;
                double s2count = (double)s2counter;
                double minCutRatio;

                //finds the minCutRatio
                minCutRatio = ((edgesCut)/(Math.min(s2count,s1count)));

                fitnessValue = minCutRatio;


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
                fitnessValue = fitnessValue + penalty*((double)graphCount);
            }

        }
        return fitnessValue;

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

    private boolean cutChecker(Integer s1, Integer s2,ArrayList<Boolean> bitSet){

        if(bitSet.get(s1)!=bitSet.get(s2)){
            return true;
        }
        return false;

    }

    private void kParentTournamentSelection(ArrayList<ParetoNode> possibleParentPop, ArrayList<ParetoNode> population){
        int parentCounter = 0;
        while(parentCounter<numParents){
            ArrayList<ParetoNode> tempPop = new ArrayList<ParetoNode>();
            for(int i = 0; i<kParent; i++){
                tempPop.add(population.get(randomValue.nextInt(population.size())));
            }

            int current_best = 0;
            for(int i = 0; i<tempPop.size(); i++){
                if(tempPop.get(current_best).getFitnessValue()>tempPop.get(i).getFitnessValue()){
                    current_best = i;
                }
            }

            if((double)randomValue.nextInt(population.size())<(0.6)*((double)population.size())){
                possibleParentPop.add(tempPop.get(current_best));
            }else{
                int index = randomValue.nextInt(tempPop.size());
                while(index==current_best){
                    index= randomValue.nextInt(tempPop.size());
                }
                possibleParentPop.add(tempPop.get(index));
            }
            parentCounter++;
        }
    }

    private void fitnessProportionalSelection(ArrayList<ParetoNode> population, ArrayList<ParetoNode> parents){
        //WE are going to sort the population based on fitness
        ArrayList<ParetoNode> nextGen = new ArrayList<ParetoNode>();
        //find best index
        while(nextGen.size()<((0.6)*(double)populationSize)){
            int current_best = 0;
            for(int i = 0; i<population.size(); i++){
                if(population.get(current_best).getFitnessValue()>population.get(i).getFitnessValue()){
                    current_best = i;
                }
            }

            nextGen.add(population.get(current_best));
            population.remove(current_best);
        }

        ArrayList<Integer> indices = new ArrayList<Integer>();
        //split the top 20% of the population into the 60% fitness proportion, and the rest
        //into the bottom 40%

        for(int i = 0; i<numParents; i++){
            Integer proportionSelection = randomValue.nextInt(100);
            if(proportionSelection <= 70){

                Integer randomIndex = randomValue.nextInt(nextGen.size());
                 while(isAlreadyPicked(indices,randomIndex)){
                     randomIndex = randomValue.nextInt(nextGen.size());
                 }
                indices.add(randomIndex);
                parents.add(nextGen.get(randomIndex));
            }else{
                Integer randIndex = randomValue.nextInt(population.size());
                parents.add(population.get(randIndex));
            }

        }

        population.addAll(nextGen);
    }

    private void fitnessProportionalSurvivalSelection(ArrayList<ParetoNode> population){
        //WE are going to sort the population based on fitness
        ArrayList<ParetoNode> nextGen = new ArrayList<ParetoNode>();
        ArrayList<ParetoNode> survivors = new ArrayList<ParetoNode>();
        //find best index
        while(nextGen.size()<((0.6)*(double)populationSize)){
            int current_best = 0;
            for(int i = 0; i<population.size(); i++){
                if(population.get(current_best).getFitnessValue()>population.get(i).getFitnessValue()){
                    current_best = i;
                }
            }

            nextGen.add(population.get(current_best));
            population.remove(current_best);
        }

        ArrayList<Integer> indices = new ArrayList<Integer>();
        //split the top 20% of the population into the 60% fitness proportion, and the rest
        //into the bottom 40%

        while(survivors.size()<populationSize){
            Integer proportionSelection = randomValue.nextInt(100);
            if(proportionSelection <= 70){

                Integer randomIndex = randomValue.nextInt(nextGen.size());
                indices.add(randomIndex);
                survivors.add(nextGen.get(randomIndex));
            }else{
                Integer randIndex = randomValue.nextInt(population.size());
                survivors.add(population.get(randIndex));
            }

        }
        population.clear();
        population.addAll(survivors);
    }

    private void kSurivalTournamentSelection(ArrayList<ParetoNode> population){
        //Pick 4 random indices
        while(population.size()>populationSize){
            ArrayList<Integer> indices = new ArrayList<Integer>();
            for(int i = 0; i<kSurvival; i++){
               int random =randomValue.nextInt(population.size());
               while(isAlreadyPicked(indices,random)){
                    random =randomValue.nextInt(population.size());
                }
            }
            //Find worst of 4 and kill it
            int killIndex = 0;
            int counter = 0;
            while(counter<indices.size()){
                if(population.get(indices.get(killIndex)).getFitnessValue()<population.get(indices.get(counter)).getFitnessValue()){
                    killIndex = counter;
                }
                counter++;
            }

            population.remove(killIndex);
        }
    }

    private void kSurvivalTournamentSelectionR(ArrayList<ParetoNode> population,ArrayList<ParetoNode> nextGeneration) {
        while(nextGeneration.size()<populationSize){
            ArrayList<Integer> indices = new ArrayList<Integer>();
            for(int i = 0; i<kSurvival; i++){
                int random =randomValue.nextInt(population.size());
                while(isAlreadyPicked(indices,random)){
                    random =randomValue.nextInt(population.size());
                }
            }
            int survivor = 0;
            int counter = 0;
            while(counter<indices.size()){
                if(population.get(indices.get(survivor)).getFitnessValue()>population.get(indices.get(counter)).getFitnessValue()){
                    survivor = counter;
                }
                counter++;
            }
            nextGeneration.add(population.get(survivor));

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
//            ParetoNode parent1 = new ParetoNode(parentPool.get(random1).getBitString(),parentPool.get(random1).getFitnessValue());
            ParetoNode parent1 = parentPool.get(random1);
            int random2 = randomValue.nextInt(parentPool.size());
            while(random2==random1){
                random2 = randomValue.nextInt(parentPool.size());
            }
//            ParetoNode parent2 = new ParetoNode(parentPool.get(random2).getBitString(), parentPool.get(random2).getFitnessValue());
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



    /**
     * Below this line is nothing but getters and setters
     */
}
