import java.util.ArrayList;
import java.util.HashMap;
import java.lang.Comparable;
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

public class Edge implements Comparable<Edge>{

  private static HashMap<String, ArrayList<Quartet> > quartets = null;
  private String name;
  private ArrayList<Integer> a;
  private ArrayList<Integer> b;
  private ArrayList<Integer> c;
  private ArrayList<Integer> d;
  private String structure;
  private String bSubtree;
  private String cSubtree;
  private String dSubtree;
  private double value;
  private boolean greedy;

  public Edge(HashMap<Integer, ArrayList<Integer> > adjList, Integer bottomVertex, int leafCount, String ogTree, HashMap<String, ArrayList<Quartet> > quartetSet, boolean isGreedy){
    greedy = isGreedy;
    quartets = quartetSet;
    a = new ArrayList<Integer>();
    b = new ArrayList<Integer>();
    c = new ArrayList<Integer>();
    d = new ArrayList<Integer>();
    value = 0;
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
    changeScore();
    setSubstrings(ogTree);

  }

  public Edge(Edge input){
    this.name = input.name;
    this.a = new ArrayList<Integer>(input.a);
    this.b = new ArrayList<Integer>(input.b);
    this.c = new ArrayList<Integer>(input.c);
    this.d = new ArrayList<Integer>(input.d);
    this.structure = input.structure;
    this.bSubtree = input.bSubtree;
    this.cSubtree = input.cSubtree;
    this.dSubtree = input.dSubtree;
    this.value = input.value;
  }

//if swapType == true, switch B and C
//if swapType == false, switch B and D
  private Edge(Edge input, boolean swapType){
    this.name = input.name;
    this.a = new ArrayList<Integer>(input.a);
    this.structure = input.structure;
    if(swapType){
      this.d = new ArrayList<Integer>(input.d);
      this.dSubtree = input.dSubtree;
      this.b = new ArrayList<Integer>(input.c);
      this.c = new ArrayList<Integer>(input.b);
      this.bSubtree = input.cSubtree;
      this.cSubtree = input.bSubtree;
    }
    else{
      this.cSubtree = input.cSubtree;
      this.c = new ArrayList<Integer>(input.c);
      this.b = new ArrayList<Integer>(input.d);
      this.d = new ArrayList<Integer>(input.b);
      this.bSubtree = input.dSubtree;
      this.dSubtree = input.bSubtree;
    }
    changeScore();
  }



  public double getScore(){return value;}

//true adds
//false subtracts
  public void changeScore(){
    this.value = 0;
    for(String s: quartets.keySet()){
      for(Quartet q: quartets.get(s)){
        if(this.satisfies(q)){
          this.value += q.getWeight();
        }
      }
    }
  }

  public String getTree(){
    String newTree = new String(structure);
//    System.out.println(newTree);
    int b = newTree.indexOf('B');
    newTree = newTree.substring(0, b) + bSubtree + newTree.substring(b+1);
//    System.out.println(newTree);

    int c = newTree.indexOf('C');
    newTree = newTree.substring(0, c) + cSubtree + newTree.substring(c+1);
//    System.out.println(newTree);

    int d = newTree.indexOf('D');
    newTree = newTree.substring(0, d) + dSubtree + newTree.substring(d+1);
//    System.out.println(newTree);

    return newTree;
  }

  private static void listMaker(ArrayList<Integer> bucket , HashMap<Integer, ArrayList<Integer> > adjList, Integer node){
    ArrayList<Integer> myNode = adjList.get(node);
    if(myNode.get(0) == null)
      bucket.add(node);
    else{
      listMaker(bucket, adjList, myNode.get(0));
      listMaker(bucket, adjList, myNode.get(1));
    }
  }

  public String toString(){
    return "EDGE: " + name + "\n" + "a: " + a+ "\n" + "b: " + b+ "\n" + "c: " + c+ "\n" + "d: " + d + "\n" + "Score: " + value + "\n";
  }

  public boolean satisfies(Quartet quartet){
    Integer[] below = quartet.below();
    Integer[] above = quartet.above();
    if((c.contains(below[0]) && d.contains(below[1])) || (c.contains(below[1]) && d.contains(below[0]))){
        return (a.contains(above[0]) && b.contains(above[1])) || (a.contains(above[1]) && b.contains(above[0]));
      }
    else if((c.contains(above[0]) && d.contains(above[1])) || (c.contains(above[1]) && d.contains(above[0]))){
        return (a.contains(below[0]) && b.contains(below[1])) || (a.contains(below[1]) && b.contains(below[0]));
      }
    return false;
  }

  public ArrayList<Edge> getNeighbor(){
    Edge swapC = new Edge(this, true);
    Edge swapD = new Edge(this, false);
    ArrayList<Edge> output = new ArrayList<Edge>();
    if(greedy){
      if(!(this.value >= swapC.value && this.value >= swapD.value)){
        if(swapC.value > swapD.value){
          output.add(swapC);
        }
        else
          output.add(swapD);
      }
    }
    else{
      output.add(swapC);
      output.add(swapD);
    }
    // System.out.println(this);
    // System.out.println(swapC);
    // System.out.println(swapD);
    return output;

  }

  private void setSubstrings(String intree){
    String tree = new String(intree);
    int[] index = {0,0};

//    System.out.println("Initial Tree: " + tree);
//    System.out.println("Set A: " + a);
//    System.out.println("Set B: " + b);
//    System.out.println("Set C: " + c);
//    System.out.println("Set D: " + d);
    subtreeHelper(b , tree, index);
    /*if(index[1] == -1 ||index[0] == -1){
      System.out.println(b);
      System.out.println(tree);

      System.out.println("NEGATIVE VALS!!!!!");
    }*/
    bSubtree = tree.substring(index[0] , index[1]);
    tree = tree.substring(0,index[0]) + "B" + tree.substring(index[1]);

//    System.out.println("BTree: " + tree);
//    System.out.println("bSubtree: " + bSubtree);
    subtreeHelper(c , tree, index);
    cSubtree = tree.substring(index[0] , index[1]);
    tree = tree.substring(0,index[0]) + "C" + tree.substring(index[1]);

//    System.out.println("CTree: " + tree);
//    System.out.println("cSubtree: " + cSubtree);
    subtreeHelper(d , tree, index);
    dSubtree = tree.substring(index[0] , index[1]);
    tree = tree.substring(0,index[0]) + "D" + tree.substring(index[1]);

//    System.out.println("DTree: " + tree);
//    System.out.println("dSubtree: " + dSubtree);
    structure = tree;
  }

  private void printData(String in, int[] nums){
    System.out.println("TREE: " + in);
    System.out.println("Start: " + nums[0] + "   End: " + nums[1]);
  }


  // indices[0] = the index of the first leaf for the given set
  // indices[1] = the index of the last leaf for the given set
  private void subtreeHelper(ArrayList<Integer> set, String tree, int[] indices){
    indices[0] = Integer.MAX_VALUE;
    indices[1] = -1;
    for(Integer i : set){
      int locA = tree.indexOf("(" + i + ",");
      int locB = tree.indexOf("," + i + ")");
      if(locA == -1){
        if(locB < indices[0])
          indices[0] = locB;
        if(locB > indices[1])
          indices[1] = locB;
      }
      else{
        if(locA < indices[0])
          indices[0] = locA;
        if(locA > indices[1])
          indices[1] = locA;
      }
    }
    //indicies on either side of just the number
    indices[1]++;
    indices[0]++;

    //find last digit of last number
    while(tree.charAt(indices[1])!= ',' && tree.charAt(indices[1])!= ')' && tree.charAt(indices[1])!= ';'){
      indices[1]++;
    }
    //if a leaf, we are done
    if(set.size()==1){
      return;
    }
    indices[1]--;
//    System.out.println("CharAT0: " + tree.charAt(indices[0]));
//    System.out.println("CharAT1: " + tree.charAt(indices[1]));
    //ok now the fun part, get the subtree
    int open = 0;
    int closed = 0;
    boolean alreadyOpen = false;
    for(int i = indices[0]; i <= indices[1]; i++){
      if(tree.charAt(i)==')'){
        if(alreadyOpen){
          open--;
          alreadyOpen = (open > 1);
        }
        else{
          closed++;
        }

      }
      if(tree.charAt(i)=='('){
        alreadyOpen = true;
        open++;
      }
    }
//    System.out.println("open: " + open);
//    System.out.println("closed: " + closed);
    // if(open == 0 || closed == 0)
    //   throw new RuntimeException();
//    System.out.println("indice 0: " + indices[0]);
//    System.out.println("indice 1: " + indices[1]);
    indices[0] -= (closed + 1);
    indices[1] += (open + 2 );
//    System.out.println("indice 0: " + indices[0]);
//    System.out.println("indice 1: " + indices[1]);
    // if(set.size()>3){
    //   indices[0]--;
    //   indices[1]++;
    // }
    if((tree.charAt(indices[1]-1) == ',') || (tree.charAt(indices[0]) == ',')){
//      System.out.println("COMMA FOUND!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
      indices[0]++;
      indices[1]--;

    }

  }

  @Override
  public int compareTo(Edge e) {
    double diff = this.value - e.value;
    if(diff == 0.0)
      return 0;
    if(diff > 0.0)
      return 1;
    return -1;

  }
}
