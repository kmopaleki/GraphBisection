import org.omg.CORBA.PUBLIC_MEMBER;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Kmo
 * Date: 9/29/12
 * Time: 5:12 PM
 * To change this template use File | Settings | File Templates.
 */
public class Node{
    private int id;
    private ArrayList<Integer> nodeList;
    private int color; // 0 - White, 1 - Gray, 2 - Black

    public Node(int id, ArrayList<Integer> nodeList) {
        this.id = id;
        this.nodeList = nodeList;
        this.color = 0;
    }

    public Node(int id) {
        this.id = id;
        this.nodeList = new ArrayList<Integer>();
        this.color = 0;
    }

    public Node(int id, ArrayList<Integer> nodeList, int color) {
        this.id = id;
        this.nodeList = nodeList;
        this.color = color;
    }

    public Node() {
        this.nodeList = new ArrayList<Integer>();

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<Integer> getNodeList() {
        return nodeList;
    }

    public void setNodeList(ArrayList<Integer> nodeList) {
        this.nodeList = nodeList;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void incrementColor(){
        this.color = color + 1;
    }
}
