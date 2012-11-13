import java.util.ArrayList;
import java.util.Random;

/**
 * User: Kevin
 * Date: 11/10/12
 */
public class MinCutAlgorithmFiveClass {

    private int populationSize;
    private int kSurvival;
    private Random randomValue;
    private int numParents;
    private int kParent;
    private int graphSample;
    private int partitionSample;
    private String fitnessFunction;
    private String survivalAlg;
    private String selectionAlg;
    private String mutationAlg;
    private int penaltyScalar;
    private int bitFlipProb;

    public MinCutAlgorithmFiveClass() {
    }

    public void COEA(){

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

    private void kRandomParentSelection(ArrayList<ParetoNode> mating_pool, ArrayList<ParetoNode> population,
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
     * @param graph_spawning_pool
     * @param graph_mating_pool
     */
    private void nPointCrossover(ArrayList<PartNode> spawning_pool,ArrayList<PartNode> mating_pool,
                                 ArrayList<GraphNode> graph_spawning_pool, ArrayList<GraphNode>graph_mating_pool)
    {

    }

    /**
     * Uniform Crossover
     *
     * @param spawning_pool
     * @param mating_pool
     * @param graph_spawning_pool
     * @param graph_mating_pool
     */
    private void uniformCrossOver(ArrayList<PartNode> spawning_pool,ArrayList<PartNode> mating_pool,
                                  ArrayList<GraphNode> graph_spawning_pool, ArrayList<GraphNode>graph_mating_pool){

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
     * Edge-Loss or Gain Mutation For graphs
     * @param graphs_spawning_pool
     */
    private void edgeLossGainMutation(ArrayList<GraphNode> graphs_spawning_pool){

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
            minCutSum = minCutSum + getMinCutRatio(samplePopulation.get(i).getBitString(),graphNode);
        }
        //Now that we have the sum, we average the total and invert it.  This will give us the fitnes
        //of the graph, the higher the average of the sum, the better value of a fitness we will get when we invert it

        minCutSum = minCutSum/((double)partitionSample);
        double fitnessValue = 1/minCutSum;
        return fitnessValue;
    }

    private double partitionFitness(ArrayList<Boolean> partition,ArrayList<GraphNode> graphPopulation){
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

    private double getMinCutRatio(ArrayList<Boolean> bitString,GraphNode graphNode){

        double fitnessValue = 0.000;
        if(fitnessFunction.equals("Original")){
            int numEdgesCut = 0;
            //look for cuts

            for(int m=1; m<graphNode.getEdgeList().size();m++){
                if(cutChecker(graphNode.getEdgeList().get(m).getxVertex(),
                        graphNode.getEdgeList().get(m).getyVertex(),bitString)){
                    //increment the number of Edges Cut
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

            for(int m=1; m<graphNode.getEdgeList().size(); m++){
                if(cutChecker(graphNode.getEdgeList().get(m).getxVertex(),
                        graphNode.getEdgeList().get(m).getyVertex(),bitString)){
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

            Graph graph1 = new Graph(graphNode.getEdgeList(),graphNode.getSize());
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



}

