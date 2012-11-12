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

    public GraphNode(int size) {
        this.size = size;
        this.edgeList = createEdgeList(size);
    }

    public GraphNode(int size,ArrayList<Edge> edgeList) {
        this.size = size;
        this.edgeList = edgeList;
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

    private ArrayList<Edge> createEdgeList(int size){
        //lets pick a random size
        Random random = new Random(654987321);
        ArrayList<Edge> edgeArrayList = new ArrayList<Edge>();

        for(int i = 0; i<size; i++){
            int numEdges = random.nextInt(size-1);
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




}
