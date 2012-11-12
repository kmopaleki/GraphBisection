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

    public MinCutAlgorithmFiveClass() {
    }

    public void Biscect(){

    }

    private void setSelectedFlagsToFalse(ArrayList<PartNode> population) {
        for(int i = 0; i<population.size(); i++){
            population.get(i).setBeenParentSelected(false);
            population.get(i).setBeenSelectedSon(false);
        }
    }

    /**
     * Parent Selection Algorithms
     */

    private void kParentTournamentSelectionNo(ArrayList<PartNode> mating_pool, ArrayList<PartNode> population) {
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

    }

    /**
     * Survival Selection Algorithms
     * @param population
     */

    private void kSurivalTournamentSelection(ArrayList<PartNode> population){
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
    }

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

    private boolean isAlreadyPicked(ArrayList<Integer> kIndices, int index) {
        for(int i = 0; i<kIndices.size(); i++){
            if(kIndices.get(i).equals(index)){
                return true;
            }
        }
        return false;
    }
}

