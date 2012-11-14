import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Kmo
 * Date: 9/29/12
 * Time: 5:11 PM
 * To change this template use File | Settings | File Templates.
 */
public class Graph {
    private ArrayList<Edge> edgeArrayList;
    private ArrayList<Node> adjList;
    private int graphSize;

    public Graph(ArrayList<Edge> edgeArrayList,int graphSize) {
        this.edgeArrayList = edgeArrayList;
        this.adjList = new ArrayList<Node>();
        this.graphSize = graphSize;
    }

    public ArrayList<Edge> getEdgeArrayList() {
        return edgeArrayList;
    }

    public void setEdgeArrayList(ArrayList<Edge> edgeArrayList) {
        this.edgeArrayList = edgeArrayList;
    }

    public ArrayList<Node> getAdjList() {
        return adjList;
    }

    public void setAdjList(ArrayList<Node> adjList) {
        this.adjList = adjList;
    }

    public void buildAdjList(ArrayList<Boolean> bitString){
//        build each node
        adjList.add(new Node(-99999));
        for(int i = 1; i<graphSize; i++){
            Node node = new Node(i);
            adjList.add(node);
        }

//        build the adj list
        for(int i = 0; i<edgeArrayList.size()-1; i++){
            if(edgeArrayList.get(i).getxVertex()>0){
                //No point in adding edges that have been cut
                if(bitString.get(edgeArrayList.get(i).getxVertex()).equals(bitString.get(edgeArrayList.get(i).getyVertex()))){
                    adjList.get(edgeArrayList.get(i).getxVertex()).getNodeList().add(edgeArrayList.get(i).getyVertex());
                    adjList.get(edgeArrayList.get(i).getyVertex()).getNodeList().add(edgeArrayList.get(i).getxVertex());
                }
            }
        }
    }

    /**
     *
     * This is a BFS algorithm to count the graphs left behind
     *
     */

    public int graphCount(){
        int graphCount = 0;
        int nodesAccountedFor = 0;

        LinkedList<Node> fifoQueue = new LinkedList<Node>();
        //Even if the BFS has finished, that doesnt mean that all nodes are accounted for
        while (nodesAccountedFor<graphSize){
            //Find the first index that has not been colored black
            int index = rootIndex();
            adjList.get(index).incrementColor();
            fifoQueue.add(adjList.get(index));
            nodesAccountedFor++;
            while(!fifoQueue.isEmpty()){
                Node node = new Node(fifoQueue.get(0).getId(),fifoQueue.get(0).getNodeList(),fifoQueue.get(0).getColor());
                fifoQueue.pop();
                for(int i = 0; i<node.getNodeList().size(); i++){
                    if(adjList.get(node.getNodeList().get(i)).getColor()==0){
                        adjList.get(node.getNodeList().get(i)).incrementColor();
                        fifoQueue.add(adjList.get(node.getNodeList().get(i)));

                    }
                }
                adjList.get(node.getId()).incrementColor();
                nodesAccountedFor++;

            }
            graphCount++;



        }
        return graphCount;
    }

    private int rootIndex() {
        int counter = 1;
        while(adjList.get(counter).getColor()>0){
            counter++;
            if(counter==graphSize&&adjList.get(counter).getColor()>0){
                break;
            }
        }
        return counter;
    }

}
