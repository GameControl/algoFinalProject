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

/*
 * Edge:
 * A set representation of an edge as represented by the wonderful ASCII art above.
 * Sets {A,B,C,D} are lists of all leaves beneath neighboring edges. Each edge has a value
 * equal to the sum of the weights of all quartets it satisfies.
 */
public class Edge implements Comparable<Edge>{

  private static boolean greedy;
  private static HashMap<String, ArrayList<Quartet> > quartets;
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

  public Edge(HashMap<Integer, ArrayList<Integer> > adjList, Integer bottomVertex, int leafCount, String ogTree, HashMap<String, ArrayList<Quartet> > quartetSet, boolean isGreedy){
    greedy = isGreedy;
    quartets = quartetSet;
    value = 0;

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
    changeScore();
    setSubstrings(ogTree);
  }

  /*
   * Performs the NNI swap:
   * if swapType == true, switch B and C
   * if swapType == false, switch B and D
   */
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

  public double getScore(){ return value; }

  // Evaluates this edge's score based off the sum of the weights of quartets it satisfies.
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

  // Returns the Newick string format of this edge's tree.
  public String getTree(){
    String newTree = new String(structure);

    int b = newTree.indexOf('B');
    newTree = newTree.substring(0, b) + bSubtree + newTree.substring(b+1);

    int c = newTree.indexOf('C');
    newTree = newTree.substring(0, c) + cSubtree + newTree.substring(c+1);

    int d = newTree.indexOf('D');
    newTree = newTree.substring(0, d) + dSubtree + newTree.substring(d+1);

    return newTree;
  }

  // A recursive method which adds leafs to the given bucket "set".
  private static void listMaker(ArrayList<Integer> bucket , HashMap<Integer, ArrayList<Integer> > adjList, Integer node){
    ArrayList<Integer> myNode = adjList.get(node);
    if(myNode.get(0) == null)
      bucket.add(node);
    else{
      listMaker(bucket, adjList, myNode.get(0));
      listMaker(bucket, adjList, myNode.get(1));
    }
  }

  // Computes if this edge satisfies the given quartet.
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

  /* 
   * Generates neighbor lists and has 2 types of output.
   * greedy is a big part of eliminating cases that are not better for 
   * this edge. This greedy choice is made to reduce runtime.
   * 1) if greedy, then only returns the swap that is best for this edge.
   *    (returns an empty list if neither is better for the edge)
   * 2) if not, then returns both swaps that could be done around this edge
   */
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
    return output;

  }

  // Initializes substring variables for each tree
  private void setSubstrings(String intree){
    String tree = new String(intree);
    int[] index = {0,0};

    subtreeHelper(b , tree, index);
    bSubtree = tree.substring(index[0] , index[1]);
    tree = tree.substring(0,index[0]) + "B" + tree.substring(index[1]);

    subtreeHelper(c , tree, index);
    cSubtree = tree.substring(index[0] , index[1]);
    tree = tree.substring(0,index[0]) + "C" + tree.substring(index[1]);

    subtreeHelper(d , tree, index);
    dSubtree = tree.substring(index[0] , index[1]);
    tree = tree.substring(0,index[0]) + "D" + tree.substring(index[1]);

    structure = tree;
  }

  // Debug printout
  private void printData(String in, int[] nums){
    System.out.println("TREE: " + in);
    System.out.println("Start: " + nums[0] + "   End: " + nums[1]);
  }

  // Gets the indices of the stubtree induced from a set {a,b,c,d}
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
    // indicies on either side of just the number
    indices[1]++;
    indices[0]++;

    // find last digit of last number
    while(tree.charAt(indices[1])!= ',' && tree.charAt(indices[1])!= ')' && tree.charAt(indices[1])!= ';'){
      indices[1]++;
    }
    // if a leaf, we are done
    if(set.size()==1){
      return;
    }
    indices[1]--;
    // ok now the fun part, get the subtree
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

    indices[0] -= (closed + 1);
    indices[1] += (open + 2 );

    // Unknown bug bandaid, fixes an odd case where the subtree ends up with extra
    // characters on both sides including a comma
    if((tree.charAt(indices[1]-1) == ',') || (tree.charAt(indices[0]) == ',')){
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

  public String toString(){
    return "EDGE: " + name + "\n" + "a: " + a+ "\n" + "b: " + b+ "\n" + "c: " + c+ "\n" + "d: " + d + "\n" + "Score: " + value + "\n";
  }
}
