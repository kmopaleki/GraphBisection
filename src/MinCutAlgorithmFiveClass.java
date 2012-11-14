import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

/**
 * User: Kevin
 * Date: 11/10/12
 */
public class MinCutAlgorithmFiveClass {

    private int populationSize;
    private Long randomSeed;
    private int kSurvival;
    private int numParents;
    private int kParent;
    private int graphSample;
    private int partitionSample;
    private String fitnessFunction;
    private String survivalAlg;
    private String survivalStrat;
    private String selectionAlg;
    private String mutationAlg;
    private String recombAlg;
    private String logFile;
    private String solutionFile;
    private int penaltyScalar;
    private int bitFlipProb;
    private int numChildren;
    private int graphSize;
    private int nPoint;
    private int nCrossNum;
    private int numberOfEvals;
    private int numberOfRuns;
    private int termNum;
    private Random randomValue;

    public MinCutAlgorithmFiveClass() {
    }

    public MinCutAlgorithmFiveClass(int populationSize, int kSurvival,
                                    Long randomSeed, int numParents,
                                    int kParent, int graphSample,
                                    int partitionSample, String fitnessFunction,
                                    String survivalAlg, String survivalStrat,
                                    String selectionAlg, String mutationAlg,
                                    String recombAlg, String logFile,
                                    String solutionFile, int penaltyScalar,
                                    int bitFlipProb, int numChildren,
                                    int graphSize, int nPoint,
                                    int nCrossNum, int numberOfEvals,
                                    int numberOfRuns, int termNum) {
        this.populationSize = populationSize;
        this.kSurvival = kSurvival;
        this.randomValue = new Random(randomSeed);
        this.numParents = numParents;
        this.kParent = kParent;
        this.graphSample = graphSample;
        this.partitionSample = partitionSample;
        this.fitnessFunction = fitnessFunction;
        this.survivalAlg = survivalAlg;
        this.survivalStrat = survivalStrat;
        this.selectionAlg = selectionAlg;
        this.mutationAlg = mutationAlg;
        this.recombAlg = recombAlg;
        this.logFile = logFile;
        this.solutionFile = solutionFile;
        this.penaltyScalar = penaltyScalar;
        this.bitFlipProb = bitFlipProb;
        this.numChildren = numChildren;
        this.graphSize = graphSize;
        this.nPoint = nPoint;
        this.nCrossNum = nCrossNum;
        this.numberOfEvals = numberOfEvals;
        this.numberOfRuns = numberOfRuns;
        this.termNum = termNum;
    }

    /**
     * The Haus algorithm
     */
    public void COEA() throws IOException {
        GraphNode bestGraph;
        PartNode bestPartition;
        FileWriter theLogFile = new FileWriter(logFile,true);
        FileWriter theSolutionFile = new FileWriter(solutionFile,true);
        BufferedWriter outLog = new BufferedWriter(theLogFile);
        BufferedWriter solutionLog = new BufferedWriter(theSolutionFile);

        outLog.write("Result Log" + "\r\n");
        outLog.write("Number of Runs: " + numberOfRuns + " Number of Evaluations per Run: "
                    + numberOfEvals + "\r\n");
        outLog.write("Population Size: " + populationSize + "\r\n");
        outLog.write("# of Parents: " + numParents + "\r\n");
        outLog.write("# of Children: " + numChildren + "\r\n");
        outLog.write("k Parent: " + kParent + "\r\n");
        outLog.write("k Survival " + kSurvival + "\r\n");
        outLog.write("n for N Point CrossOver: " + nCrossNum + "\r\n");
        outLog.write("N termination constant: " + termNum + "\r\n");
        outLog.write("Probability of a bit Flip: " + bitFlipProb + "\r\n");
        outLog.write("Selection Alg: " + selectionAlg + "\r\n");
        outLog.write("Reproduction Alg: " + recombAlg + "\r\n");
        outLog.write("Surivivor Selection Alg: " + survivalAlg + "\r\n");
        outLog.write("Fitness Function: " + fitnessFunction + "\r\n");
        outLog.write("Result Log"+"\r\n");

        double localbest = 0.000;
        double localAverage = 0.000;

        for(int i = 0; i<numberOfRuns; i++){
            //INITIALIZE THE POPULATION
            ArrayList<PartNode> partitionPopulation = new ArrayList<PartNode>();
            ArrayList<GraphNode> graphPopulation = new ArrayList<GraphNode>();

            for(int j = 0; j<populationSize; j++){
                partitionPopulation.add(new PartNode(getBitStrings(graphSize)));
                graphPopulation.add(new GraphNode(graphSize,randomValue));
            }

            //Evaluate Initial Population
            for(int j = 0; j<populationSize;j++){
                partitionPopulation.get(j).setFitnessValue(partitionFitness(partitionPopulation.get(j),graphPopulation));
                graphPopulation.get(j).setFitnessValue(graphFitness(graphPopulation.get(j),partitionPopulation));
            }
            int evalCounter = 0;
            int termCounter = 0;
            double prevLocalBest = 0.0000;
            double currentLocalBest;
            outLog.write("Run #: " + (i+1)+"\r\n");
            while(evalCounter<numberOfEvals&&!termCondition(termCounter)){
                System.out.println("Run #" + i + "Eval # " + evalCounter);
                ArrayList<PartNode> partition_mating_pool = new ArrayList<PartNode>();
                ArrayList<PartNode> partition_spawning_pool = new ArrayList<PartNode>();
                ArrayList<GraphNode> graph_spawning_pool = new ArrayList<GraphNode>();
                ArrayList<GraphNode> graph_mating_pool = new ArrayList<GraphNode>();

                //SELECT PARENTS
                if(selectionAlg.equals("Tournament")){
                    kParentTournamentSelection(partition_mating_pool,partitionPopulation,graph_mating_pool,graphPopulation);
                }else if(selectionAlg.equals("TournamentNo")){
                    kParentTournamentSelectionNo(partition_mating_pool,partitionPopulation,graph_mating_pool,graphPopulation);

                }else if(selectionAlg.equals("UniformRandom")){

                }

                //RECOMBINATION
                if(recombAlg.equals("Uniform")){
                    uniformCrossOver(partition_spawning_pool,partition_mating_pool);
                    graphCrossOver(graph_spawning_pool,graph_mating_pool);

                }else if(recombAlg.equals("nPoint")){
                    nPointCrossover(partition_spawning_pool,partition_mating_pool);
                    graphCrossOver(graph_spawning_pool,graph_mating_pool);
                }

//                //MUTATE
                bitFlipMutation(partition_spawning_pool);
                edgeSwapMutation(graph_spawning_pool);
//
                //EVALUATE NEW KIDS
                for(int k = 0; k<partition_spawning_pool.size();k++){
                    partition_spawning_pool.get(k).setFitnessValue(partitionFitness(partition_spawning_pool.get(k),
                            graphPopulation));
                }
                for(int k = 0; k<graph_spawning_pool.size();k++){
                    graph_spawning_pool.get(k).setFitnessValue(graphFitness(graph_spawning_pool.get(k),partitionPopulation));
                }
//
//                //SURVIVAL SELECTION
                if(survivalStrat.equals("Plus")){
                    graphPopulation.addAll(graph_spawning_pool);
                    partitionPopulation.addAll(partition_spawning_pool);
                }else if(survivalStrat.equals("Comma")){

                }

                if(survivalAlg.equals("Tournament")){
                   kSurivalTournamentSelection(partitionPopulation,graphPopulation);

                }else if(survivalAlg.equals("Truncation")){
                    truncation(partitionPopulation,graphPopulation);

                }else if(survivalAlg.equals("TournamentNo")){
                    kSurivalTournamentSelectionNoR(partitionPopulation,graphPopulation);
                }

                if(evalCounter%numChildren==0){

                }

                evalCounter++;
            }
        }

        outLog.close();
        solutionLog.close();
    }

    /**
     * Parent Selection Algorithms
     */


    /**
     * Tournament Selection without Replacement, this algorithm will perform this type of Parent
     * Selection for both the graphPopulation and the PartitionPopulation.
     *
     * @param mating_pool
     * @param population
     * @param graphMatingPool
     * @param graphPopulation
     */
    private void kParentTournamentSelectionNo(ArrayList<PartNode> mating_pool, ArrayList<PartNode> population,
                                              ArrayList<GraphNode>graphMatingPool,ArrayList<GraphNode> graphPopulation) {
        /**
         * For Partitions
         */
        //Set all parentselected flags to false
        for(int i = 0; i<population.size(); i++){
            population.get(i).setBeenParentSelected(false);
        }

        //Get our N parents
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
                if(population.get(index).getFitnessValue()<population.get(current_best).getFitnessValue()){
                    current_best = index;
                }
            }
            population.get(current_best).setBeenParentSelected(true);
            mating_pool.add(population.get(current_best));

        }

        /**
         * For Graphs
         */

        for(int i = 0; i<population.size(); i++){
            graphPopulation.get(i).setBeenParentSelected(false);
        }

        while(graphMatingPool.size()<numParents){
            int current_best = randomValue.nextInt(graphPopulation.size());
            while(graphPopulation.get(current_best).isBeenParentSelected()){
                current_best = randomValue.nextInt(graphPopulation.size());
            }
            for(int i = 0; i<kParent-1; i++){
                int index = randomValue.nextInt(graphPopulation.size());
                while(graphPopulation.get(index).isBeenParentSelected()){
                    index = randomValue.nextInt(graphPopulation.size());
                }
                if(graphPopulation.get(index).getFitnessValue()<graphPopulation.get(current_best).getFitnessValue()){
                    current_best = index;
                }
            }
            graphPopulation.get(current_best).setBeenParentSelected(true);
            graphMatingPool.add(graphPopulation.get(current_best));

        }

    }

    /**
     * Tournament Selection With Replacement, this type of parent selection will select parents
     * from both the GraphPopulation and the PartitionPopulation.
     *
     * @param mating_pool
     * @param population
     * @param graphMatingPool
     * @param graphPopulation
     */

    private void kParentTournamentSelection(ArrayList<PartNode> mating_pool, ArrayList<PartNode> population,
                                              ArrayList<GraphNode>graphMatingPool,ArrayList<GraphNode> graphPopulation) {
        /**
         * For Partitions
         */

        //Get our N parents
        while(mating_pool.size()<numParents){
            int current_best = randomValue.nextInt(population.size());
            for(int i = 0; i<kParent-1; i++){
                int index = randomValue.nextInt(population.size());
                if(population.get(index).getFitnessValue()<population.get(current_best).getFitnessValue()){
                    current_best = index;
                }
            }
            mating_pool.add(population.get(current_best));

        }

        /**
         * For Graphs
         */

        while(graphMatingPool.size()<numParents){
            int current_best = randomValue.nextInt(graphPopulation.size());
            for(int i = 0; i<kParent-1; i++){
                int index = randomValue.nextInt(graphPopulation.size());
                if(graphPopulation.get(index).getFitnessValue()<graphPopulation.get(current_best).getFitnessValue()){
                    current_best = index;
                }
            }
            graphMatingPool.add(graphPopulation.get(current_best));

        }

    }

    /**
     *  Uniform Random Parent Selection
     * @param mating_pool
     * @param population
     * @param graphMatingPool
     * @param graphPopulation
     */

    private void kRandomParentSelection(ArrayList<PartNode> mating_pool, ArrayList<PartNode> population,
                                        ArrayList<GraphNode>graphMatingPool,ArrayList<GraphNode> graphPopulation) {

        while(mating_pool.size()<numParents){
            mating_pool.add(population.get(randomValue.nextInt(population.size())));
        }

        while(graphMatingPool.size()<numParents){
            graphMatingPool.add(graphPopulation.get(randomValue.nextInt(graphPopulation.size())));
        }
    }

    /**
     * Survival Selection Algorithms
     */

    /**
     *Tournament Selection with Replacement
     *
     * @param population
     * @param graphPopulation
     */

    private void kSurivalTournamentSelection(ArrayList<PartNode> population,ArrayList<GraphNode> graphPopulation ){
        /**
         * For Partitions
         */
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
                if(population.get(indices.get(killIndex)).getFitnessValue()>population.get(indices.get(counter)).getFitnessValue()){
                    killIndex = counter;
                }
                counter++;
            }

            population.remove(killIndex);
        }

        /**
         * For Graphs
         */

        while(graphPopulation.size()>populationSize){
            ArrayList<Integer> indices = new ArrayList<Integer>();
            for(int i = 0; i<kSurvival; i++){
                int random =randomValue.nextInt(graphPopulation.size());
                while(isAlreadyPicked(indices,random)){
                    random =randomValue.nextInt(graphPopulation.size());
                }
            }
            //Find worst of 4 and kill it
            int killIndex = 0;
            int counter = 0;
            while(counter<indices.size()){
                if(graphPopulation.get(indices.get(killIndex)).getFitnessValue()>graphPopulation.get(indices.get(counter)).getFitnessValue()){
                    killIndex = counter;
                }
                counter++;
            }

            graphPopulation.remove(killIndex);
        }


    }

    /**
     * Tournament Selection without Replacement
     * @param population
     * @param graphPopulation
     */
    private void kSurivalTournamentSelectionNoR(ArrayList<PartNode> population,ArrayList<GraphNode> graphPopulation ){
        /**
         * For Partitions
         */
        for(int i = 0; i<population.size(); i++){
            population.get(i).setBeenSelectedSon(false);
        }

        while(population.size()>populationSize){
            int killIndex = randomValue.nextInt(population.size());
            while (population.get(killIndex).isBeenSelectedSon()){
                killIndex = randomValue.nextInt(population.size());
            }
            for(int i = 0; i<kSurvival; i++){
               int index = randomValue.nextInt(population.size());
               while(population.get(index).isBeenSelectedSon()){
                   index = randomValue.nextInt(population.size());
               }
               if(population.get(index).getFitnessValue()>population.get(killIndex).getFitnessValue()){
                   killIndex = index;
               }
            }
            population.remove(killIndex);
        }



        /**
         * For Graphs
         */
        for(int i = 0; i<graphPopulation.size(); i++){
            graphPopulation.get(i).setBeenSelectedSon(false);
        }

        while(graphPopulation.size()>populationSize){

            int killIndex = randomValue.nextInt(graphPopulation.size());
            while (graphPopulation.get(killIndex).isBeenSelectedSon()){
                killIndex = randomValue.nextInt(graphPopulation.size());
            }
            for(int i = 0; i<kSurvival; i++){
                int index = randomValue.nextInt(graphPopulation.size());
                while(graphPopulation.get(index).isBeenSelectedSon()){
                    index = randomValue.nextInt(graphPopulation.size());
                }
                if(graphPopulation.get(index).getFitnessValue()>graphPopulation.get(killIndex).getFitnessValue()){
                    killIndex = index;
                }
            }
            graphPopulation.remove(killIndex);
        }
    }

    /**
     * Truncation Survival Selection
     *
     * @param population
     * @param populationGraph
     */
    private void truncation(ArrayList<PartNode> population, ArrayList<GraphNode> populationGraph) {
        /**
         * For the Partitions
         */


        ArrayList<PartNode> nextGen = new ArrayList<PartNode>();
        //find best index
        while(nextGen.size()<populationSize){
            int save_index = 0;
            for(int i = 0; i<population.size(); i++){
                if(population.get(save_index).getFitnessValue()<population.get(i).getFitnessValue()){
                    save_index = i;
                }
            }

            nextGen.add(population.get(save_index));
            population.remove(save_index);
        }
        population.clear();
        population.addAll(nextGen);


        /**
         * For the Graphs
         */

        ArrayList<GraphNode> nextGraphGen = new ArrayList<GraphNode>();
        //find best index
        while(nextGen.size()<populationSize){
            int save_index = 0;
            for(int i = 0; i<populationGraph.size(); i++){
                if(populationGraph.get(save_index).getFitnessValue()<populationGraph.get(i).getFitnessValue()){
                    save_index = i;
                }
            }

            nextGraphGen.add(populationGraph.get(save_index));
            populationGraph.remove(save_index);
        }
        populationGraph.clear();
        populationGraph.addAll(nextGraphGen);

    }

    private void randomSurvival(ArrayList<PartNode> population,ArrayList<GraphNode> graphPopulation) {
        //In this one we will kill randomly until we achieve the population size
        while(population.size()>populationSize){
            population.remove(randomValue.nextInt(population.size()));
        }

        //In this one we will kill randomly until we achieve the population size
        while(graphPopulation.size()>populationSize){
            graphPopulation.remove(randomValue.nextInt(graphPopulation.size()));
        }


    }

    /**
     * Recombination Algorithms
     */

    /**
     *  N point Crossover
     *
     * @param spawning_pool
     * @param mating_pool
     */
    private void nPointCrossover(ArrayList<PartNode> spawning_pool,ArrayList<PartNode> mating_pool)
    {
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
            PartNode child = new PartNode();
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

    /**
     * Uniform Crossover
     *
     * @param spawning_pool
     * @param mating_pool
     */
    private void uniformCrossOver(ArrayList<PartNode> spawning_pool,ArrayList<PartNode> mating_pool){

        /**
         * For Partitions
         */

        int childCounter = 0;
        while(spawning_pool.size()<numChildren){
            int random1 = randomValue.nextInt(mating_pool.size());
//            PartNode parent1 = new PartNode(mating_pool.get(random1).getBitString(),mating_pool.get(random1).getFitnessValue());
            PartNode parent1 = mating_pool.get(random1);
            int random2 = randomValue.nextInt(mating_pool.size());
            while(random2==random1){
                random2 = randomValue.nextInt(mating_pool.size());
            }
//            PartNode parent2 = new PartNode(mating_pool.get(random2).getBitString(), mating_pool.get(random2).getFitnessValue());
            PartNode parent2 = mating_pool.get(random2);

            //Create Child 1
            PartNode child1 = new PartNode();
            child1.setBitString(new ArrayList<Boolean>());
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
            spawning_pool.add(child1);
            childCounter++;
        }

    }

    /**
     * Graph Crossover - 1 point for graphs basically
     */
    private void graphCrossOver(ArrayList<GraphNode> spawning_pool,ArrayList<GraphNode> mating_pool){
        while(spawning_pool.size()<numChildren){
            int random1 = randomValue.nextInt(mating_pool.size());
            GraphNode parent1 = mating_pool.get(random1);
            int random2 = randomValue.nextInt(mating_pool.size());
            while(random2==random1){
                random2 = randomValue.nextInt(mating_pool.size());
            }
            GraphNode parent2 = mating_pool.get(random2);
            ArrayList<Edge> edges = new ArrayList<Edge>();
            for(int i = 0; i<nPoint; i++){
               edges.add(parent1.getEdgeList().get(i));
            }
            for(int j = nPoint; j<parent2.getEdgeList().size(); j++){

                if(alreadyexists(parent2.getEdgeList().get(j).getxVertex(),parent2.getEdgeList().get(j).getyVertex(),edges)){
                    //Create a brand new edge with elements from parent 1
                    int x = parent1.getEdgeList().get(j).getxVertex();
                    int y = parent1.getEdgeList().get(j).getyVertex();
                    while(alreadyexists(x,y,edges)){
                        y = randomValue.nextInt(graphSize);
                        while(y==x){
                            y = randomValue.nextInt(graphSize);
                        }
                    }
                    edges.add(new Edge(x,y));
                }else{
                    edges.add(parent2.getEdgeList().get(j));
                }
            }

            spawning_pool.add(new GraphNode(graphSize,edges));
        }

    }

    /**
     * Mutation Algorithms
     */

    /**
     * Bit Flip Mutation - for partitions
     * @param spawning_pool
     */
    private void bitFlipMutation(ArrayList<PartNode> spawning_pool){
        for(int i = 0; i<spawning_pool.size(); i++){
            for(int j = 0; j<spawning_pool.get(i).getBitString().size(); j++){
                int rando = randomValue.nextInt(100);
                if(rando <= bitFlipProb){
                    if(spawning_pool.get(i).getBitString().get(j)){
                        spawning_pool.get(i).getBitString().set(j,false);
                    }else if(!spawning_pool.get(i).getBitString().get(j)){
                        spawning_pool.get(i).getBitString().set(j,true);
                    }

                }
            }
        }
    }

    /**
     * Edge Swap Mutation For graphs, this form of mutation, takes an edge that exists between two vertices,
     * and moves it to another pair of vertices
     * @param graphs_spawning_pool
     */
    private void edgeSwapMutation(ArrayList<GraphNode> graphs_spawning_pool){
        for(int i = 0; i<graphs_spawning_pool.size(); i++){
            for(int j = 0; j<graphs_spawning_pool.get(i).getEdgeList().size(); j++){
                int rando = randomValue.nextInt(100);
                if(rando <= bitFlipProb){
                    int index = j;
                    int x = graphs_spawning_pool.get(i).getEdgeList().get(j).getxVertex();
                    int y = randomValue.nextInt(graphSize);
                    while(alreadyexists(x,y,graphs_spawning_pool.get(i).getEdgeList())){
                        y = randomValue.nextInt(graphSize);
                    }

                    graphs_spawning_pool.get(i).getEdgeList().remove(j);
                    graphs_spawning_pool.get(i).getEdgeList().add(index,new Edge(x,y));
                }
            }
        }
    }


    /**
     * Fitness Functions
     */
    private double graphFitness(GraphNode graphNode,ArrayList<PartNode> partitionPopulation){
        ArrayList<PartNode> samplePopulation = new ArrayList<PartNode>();
        //Pick 5 random people from the partition Population
        for(int i = 0; i<partitionSample; i++){
            samplePopulation.add(partitionPopulation.get(randomValue.nextInt(partitionPopulation.size())));
        }

        //Calculate the average fitness of this sample population
        double minCutSum = 0.00;
        for(int i = 0; i<samplePopulation.size(); i++){
            minCutSum = minCutSum + getMinCutRatio(samplePopulation.get(i),graphNode);
        }
        //Now that we have the sum, we average the total and invert it.  This will give us the fitnes
        //of the graph, the higher the average of the sum, the better value of a fitness we will get when we invert it

        minCutSum = minCutSum/((double)partitionSample);
        double fitnessValue = 1/minCutSum;
        return fitnessValue;
    }

    private double partitionFitness(PartNode partition,ArrayList<GraphNode> graphPopulation){
        ArrayList<GraphNode> samplePopulation = new ArrayList<GraphNode>();
        //Pick n random people from the population to create a sample population
        for(int i = 0; i<graphSample; i++){
            samplePopulation.add(graphPopulation.get(randomValue.nextInt(graphPopulation.size())));
        }

        //Calculate the average fitness of this sample population
        double minCutSum = 0.00;
        for(int i = 0; i<samplePopulation.size(); i++){
            minCutSum = minCutSum + getMinCutRatio(partition,samplePopulation.get(i));
        }

        //Now that we have the sum, we average the total.  This will give us the fitness
        //of the partition, the lower the average of the sum, the better value of a fitness we will get
        double fitnessValue =minCutSum/((double)graphSample);
        return fitnessValue;
    }

    private double getMinCutRatio(PartNode partNode,GraphNode graphNode){

        double fitnessValue = 0.000;
        if(fitnessFunction.equals("Original")){
            int numEdgesCut = 0;
            //look for cuts

            for(int m=1; m<graphNode.getEdgeList().size();m++){
                if(cutChecker(graphNode.getEdgeList().get(m).getxVertex(),
                        graphNode.getEdgeList().get(m).getyVertex(),partNode.getBitString())){
                    //increment the number of Edges Cut
                    numEdgesCut++;
                }
            }

            int s1counter = 0;
            int s2counter = 0;
            for(int j = 1; j<partNode.getBitString().size(); j++){

                if(partNode.getBitString().get(j)==false){
                    s1counter++;
                }else if(partNode.getBitString().get(j)==true){
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

            for(int m=1; m<graphNode.getEdgeList().size(); m++){
                if(cutChecker(graphNode.getEdgeList().get(m).getxVertex(),
                        graphNode.getEdgeList().get(m).getyVertex(),partNode.getBitString())){
                    //increment the number of edges cut
                    numEdgesCut++;

                }
            }

            int s1counter = 0;
            int s2counter = 0;
            for(int j = 1; j<partNode.getBitString().size(); j++){

                if(partNode.getBitString().get(j)==false){
                    s1counter++;
                }else if(partNode.getBitString().get(j)==true){
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

            Graph graph1 = new Graph(graphNode.getEdgeList(),graphNode.getSize()+1);
            graph1.buildAdjList(partNode.getBitString());
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

    /**
     * Helper Functions
     */

    private boolean isAlreadyPicked(ArrayList<Integer> kIndices, int index) {
        for(int i = 0; i<kIndices.size(); i++){
            if(kIndices.get(i).equals(index)){
                return true;
            }
        }
        return false;
    }
    private boolean cutChecker(Integer s1, Integer s2,ArrayList<Boolean> bitSet){

        if(bitSet.get(s1)!=bitSet.get(s2)){
            return true;
        }
        return false;

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

    private boolean alreadyexists(int x,int y,ArrayList<Edge> edgesList){
        for(int i = 0; i<edgesList.size(); i++){
            if(edgesList.get(i).getxVertex()==x&&edgesList.get(i).getyVertex()==y){
                return true;
            }
            if(edgesList.get(i).getxVertex()==y&&edgesList.get(i).getyVertex()==x){
                return true;
            }
        }

        return false;

    }

    private boolean isTimeToFlip(int i,ArrayList<Integer> nPoints){
        for(int j = 0; j<nPoints.size();j++){
            if(nPoints.get(j).equals(i)){
                return true;
            }
        }
        return  false;
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

}

