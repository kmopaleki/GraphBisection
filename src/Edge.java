/**
 * Created with IntelliJ IDEA.
 * User: Kevin
 * Date: 8/28/12
 * Time: 11:27 AM
 * To change this template use File | Settings | File Templates.
 */
public class Edge {
   private Integer xVertex;
   private Integer yVertex;

    public Edge(Integer xVertex, Integer s2Vertex) {
        this.xVertex = xVertex;
        this.yVertex = s2Vertex;
    }

    public Integer getxVertex() {
        return xVertex;
    }

    public void setxVertex(Integer xVertex) {
        this.xVertex = xVertex;
    }

    public Integer getyVertex() {
        return yVertex;
    }

    public void setyVertex(Integer yVertex) {
        this.yVertex = yVertex;
    }
}
