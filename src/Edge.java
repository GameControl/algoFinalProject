import java.util.ArrayList;
import java.util.Map;

/*
           A
            \
             \
              topVertex
              /\
             /  this Edge
            /    \
           B      bottomVertex
                  /\
                 /  \
                C    D
*/

public class Edge{

  String name;
  ArrayList<Integer> a;
  ArrayList<Integer> b;
  ArrayList<Integer> c;
  ArrayList<Integer> d;


  public Edge(Map<Integer, ArrayList<Integer> > adjList, Integer bottomVertex, int leafCount){
    a = new ArrayList<Integer>();
    b = new ArrayList<Integer>();
    c = new ArrayList<Integer>();
    d = new ArrayList<Integer>();
    Integer topVertex = adjList.get(bottomVertex).get(2);
    name = topVertex + "|" + bottomVertex;
    Integer bnode;
    ArrayList<Integer> topData = adjList.get(topVertex);
    if(topData.get(1).equals(bottomVertex))
      bnode = topData.get(0);
    else
      bnode = topData.get(1);

    listMaker(c, adjList, adjList.get(bottomVertex).get(0));
    listMaker(d, adjList, adjList.get(bottomVertex).get(1));
    listMaker(b, adjList, bnode);

    for(Integer i = 0; i <= leafCount; i++){
      if(!(c.contains(i) || d.contains(i) || b.contains(i)))
        a.add(i);
    }
  }

  private static void listMaker(ArrayList<Integer> bucket , Map<Integer, ArrayList<Integer> > adjList, Integer node){
    ArrayList<Integer> myNode = adjList.get(node);
    if(myNode.get(0) == null)
      bucket.add(node);
    else{
      listMaker(bucket, adjList, myNode.get(0));
      listMaker(bucket, adjList, myNode.get(1));
    }
  }

  public String toString(){
    return "EDGE: " + name + "\n" + "a: " + a+ "\n" + "b: " + b+ "\n" + "c: " + c+ "\n" + "d: " + d + "\n";
  }

  public boolean satisfies(Quartet quartet){
    Integer[] below = quartet.below();
    Integer[] above = quartet.above();
    if((c.contains(below[0]) && d.contains(below[1])) || (c.contains(below[1]) && d.contains(below[0]))){
        return (a.contains(above[0]) && b.contains(above[1])) || (a.contains(above[1]) && b.contains(above[0]));
      }
    return false;
  }
}