import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created with IntelliJ IDEA.
 * User: Kevin
 * Date: 8/28/12
 * Time: 12:37 AM
 * To change this template use File | Settings | File Templates.
 */
public class EaGraph {

    private int size;
    private int numEdges;
    private ArrayList<Integer> listOfEdges;
    private ArrayList<Edge> edgeList;

    public EaGraph() {
    }

    public void readFromFile(File aFile) throws FileNotFoundException {
        Scanner s = new Scanner(aFile);
        listOfEdges = new ArrayList<Integer>();
        int counter = 0;
        while(s.hasNext()){
            if(counter==0){
                setSize(Integer.parseInt(s.next()));
            }else if(counter==1){
                setNumEdges(Integer.parseInt(s.next()));
            }else{
            listOfEdges.add(Integer.parseInt(s.next()));
            }
            counter++;

        }
        buildEdgeList();
        s.close();
    }
    
    private void buildEdgeList(){
        ArrayList<Integer> xList = new ArrayList<Integer>();
        ArrayList<Integer> yList = new ArrayList<Integer>();
        edgeList = new ArrayList<Edge>();
        edgeList.add(new Edge(-1, -1));
        for(Integer i = 0; i< listOfEdges.size(); i++){
            if(i%2==0){
                xList.add(listOfEdges.get(i));
            }else{
                yList.add(listOfEdges.get(i));
            }

        }

        for(int i = 0; i<xList.size(); i++){
            Edge newEdge = new Edge(xList.get(i),yList.get(i));
            edgeList.add(newEdge);
        }

    }

   /*
        Below this line is nothing but simple getter and setter functions
    */
    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getNumEdges() {
        return numEdges;
    }

    public void setNumEdges(int numEdges) {
        this.numEdges = numEdges;
    }

    public ArrayList<Integer> getListOfEdges() {
        return listOfEdges;
    }

    public void setListOfEdges(ArrayList<Integer> listOfEdges) {
        this.listOfEdges = listOfEdges;
    }

    public ArrayList<Edge> getEdgeList() {
        return edgeList;
    }

    public void setEdgeList(ArrayList<Edge> edgeList) {
        this.edgeList = edgeList;
    }
}
