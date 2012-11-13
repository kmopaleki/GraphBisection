import java.util.ArrayList;
import java.util.Random;

/**
 * User: Kevin
 * Date: 11/11/12
 */
public class GraphNode {

    private int size;
    private ArrayList<Edge> edgeList;
    private Double fitnessValue;
    private boolean beenSelectedSon;
    private boolean beenParentSelected;
    private int numEdges;
    private Random random;



    public GraphNode(int size,Random random) {
        this.size = size;
        this.edgeList = createEdgeList(size);
        this.beenParentSelected = false;
        this.beenSelectedSon=false;
        this.numEdges = 40;
        this.random = random;
    }

    public GraphNode(int size,ArrayList<Edge> edgeList) {
        this.size = size;
        this.edgeList = edgeList;
        this.beenParentSelected = false;
        this.beenSelectedSon=false;

    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public ArrayList<Edge> getEdgeList() {
        return edgeList;
    }

    public void setEdgeList(ArrayList<Edge> edgeList) {
        this.edgeList = edgeList;
    }

    public Double getFitnessValue() {
        return fitnessValue;
    }

    public void setFitnessValue(Double fitnessValue) {
        this.fitnessValue = fitnessValue;
    }

    public boolean isBeenSelectedSon() {
        return beenSelectedSon;
    }

    public void setBeenSelectedSon(boolean beenSelectedSon) {
        this.beenSelectedSon = beenSelectedSon;
    }

    public boolean isBeenParentSelected() {
        return beenParentSelected;
    }

    public void setBeenParentSelected(boolean beenParentSelected) {
        this.beenParentSelected = beenParentSelected;
    }

    private ArrayList<Edge> createEdgeList(int size){
        ArrayList<Edge> edgeArrayList = new ArrayList<Edge>();
        for(int i = 0; i<size; i++){
            for(int j = 0; j<numEdges; j++){

                int y_edge = random.nextInt(size);
                while(y_edge==i||alreadyexists(i,y_edge,edgeArrayList)){
                    y_edge = random.nextInt(size);
                }

                Edge edge = new Edge(i,y_edge);
                edgeArrayList.add(edge);
            }

        }
        return edgeArrayList;
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

    public int getNumEdges() {
        return numEdges;
    }

    public void setNumEdges(int numEdges) {
        this.numEdges = numEdges;
    }

    public Random getRandom() {
        return random;
    }

    public void setRandom(Random random) {
        this.random = random;
    }
}
