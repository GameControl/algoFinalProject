import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Collections;
import java.lang.Comparable;

/*
 * Tree:
 * Class of Edges that represent a tree.
 * It is constructed from a Newick string and stored as an adjacency list.
 */
public class Tree implements Comparable<Tree>{

  // Newick string that represents tree
  private String myName;
  private ArrayList<Edge> edges;
  private double myScore;

  private static boolean isGreedy;
  private static HashMap<String, ArrayList<Quartet> > myQuartets;
  private static int mySize;

  public Tree(int size, String name, HashMap<String, ArrayList<Quartet> > quartets, boolean greedy){
    isGreedy = greedy;
    mySize = size;
    myName = name;
    myQuartets = quartets;
    edges = new ArrayList<Edge>();
    HashMap<Integer, ArrayList<Integer> > adjList = new HashMap<Integer, ArrayList<Integer>>();
    int[] internalCount = {0};
    startParse(adjList, myName, mySize, internalCount);
    for(Integer i = size+1; i < ((internalCount[0]*2)-1); i++){
      Edge testEdge = new Edge(adjList, i, size, myName, myQuartets, greedy);
      edges.add(testEdge);
    }
    // Score a tree once at construction
    myScore = 0.0;
    for(Edge e: edges){
      myScore += e.getScore();
    }
  }

  // Private constructor for looking at neighbors
  private Tree(String newTree){
    this(mySize, newTree, myQuartets, isGreedy);
  }

  public double getScore(){return myScore;}

  // Asks each edge for the trees they can make by using NNI and gets the best
  public Tree findBestNeighbor(){
    ArrayList<Edge> options = new ArrayList<Edge>();
    for(Edge e: edges){
      options.addAll(e.getNeighbor());
    }
    ArrayList<Tree> treeOptions = new ArrayList<Tree>();
    for(Edge e: options){
      treeOptions.add(new Tree(e.getTree()));
    }
    Collections.sort(treeOptions);
    if(!treeOptions.isEmpty()){
      Tree best = treeOptions.get(0);
      if(best.compareTo(this) < 0)
        return best;
    }
    return this;
  }

  // A driver method for parsing the tree
  private static void startParse(Map<Integer, ArrayList<Integer> > adjList, String tree, int taxaCount, int[] internalCount){
    int[] vCount = {0};
    internalCount[0] = countInternal(tree);
    vCount[0] = internalCount[0] + 1;
    parseTree(tree, internalCount[0], vCount, adjList);

    /* Puts leafs in the adjacency list
    ArrayList<Integer> dummy = new ArrayList<Integer>();
    dummy.add(null);
    dummy.add(null);
    for(int i = 0; i <= internalCount[0]; i++){
      adjList.put(i , new ArrayList<Integer>(dummy));
    }*/

    labelParents(adjList, internalCount[0]);
    fixRoot(adjList, internalCount[0]);
  }

  /*
   * Removes the "traditional" root of the tree, and adds an edge between its children.
   * This modification is to allow us to generalize our approach.
   */
  private static void fixRoot(Map<Integer, ArrayList<Integer> > adjList, int internalCount){
    Integer rootName = internalCount * 2;
    ArrayList<Integer> root = adjList.get(rootName);
    ArrayList<Integer> child = adjList.get(root.get(0));
    child.remove(2);
    child.add(root.get(1));
    child = adjList.get(root.get(1));
    child.remove(2);
    child.add(root.get(0));
    adjList.remove(rootName);
  }

  /*
   * Recurses through the Newick string Post-order and builds an adjacency list to represent the tree. This method only adds
   * internal vertices to the adjacency list.
   */
  private static String parseTree(String str, int internalCount, int[] vCount, Map<Integer, ArrayList<Integer> > adjList){
    Integer left = null;
    Integer right = null;

    // Look for left subtree
    if(str.substring(0,1).equals("(")) {
      str = str.substring(1);
      // Subtree case
      if(str.substring(0,1).equals("(")) {
        str = parseTree(str, internalCount, vCount, adjList);
      }
      // Leaf case
      else {
        int i = str.indexOf(",");
        left = Integer.valueOf(str.substring(0,i));
        str = str.substring(i);
        // Add leaf to the adjacency list
        ArrayList<Integer> dummy = new ArrayList<Integer>();
        dummy.add(null);
        dummy.add(null);
        adjList.put(left, dummy);
      }
    }
    
    // Look for right subtree
    if(str.substring(0,1).equals(",")) {
      str = str.substring(1);
      // Subtree case
      if(str.substring(0,1).equals("(")) {
        str = parseTree(str, internalCount, vCount, adjList);
      }
      // Leaf case
      else {
        int i = str.indexOf(")");
        right = Integer.valueOf(str.substring(0,i));
        str = str.substring(i);
        // Add leaf to the adjacency list
        ArrayList<Integer> dummy = new ArrayList<Integer>();
        dummy.add(null);
        dummy.add(null);
        adjList.put(right, dummy);
      }
    }

    if(str.substring(0,1).equals(")")) {
      str = str.substring(1);
      ArrayList<Integer> a = new ArrayList<Integer>();

      // Add left and right children for internal vertices with subtrees underneath them
      if(left==null && right==null){
        int[] answer = {0};
        labelInternalChild(vCount[0]-1, answer, internalCount, adjList);
        int rcCount = answer[0];
        a.add(vCount[0] - rcCount - 1);
        a.add(vCount[0]-1);
      }
      else {
        // Add left child
        if(left==null)
          a.add(vCount[0]-1);
        else
          a.add(left);
        // Add right child
        if(right==null)
          a.add(vCount[0]-1);
        else
          a.add(right);
      }
      // Add an internal vertex to the adjacency list
      adjList.put(vCount[0], a);
      vCount[0]++;
    }
    return str;
  }

  // Labels the parents of all vertices in the adjacency list.
  private static void labelParents(Map<Integer, ArrayList<Integer> > adjList, int internalCount){
    for (Map.Entry<Integer, ArrayList<Integer> > entry : adjList.entrySet()) {
      Integer key = entry.getKey();
      if(!(entry.getValue().get(0) == null)){
        Integer lc = entry.getValue().get(0);
        if(adjList.containsKey(lc))
          adjList.get(lc).add(key);
      }
      if(!(entry.getValue().get(1) == null)){
        Integer rc = entry.getValue().get(1);
        if(adjList.containsKey(rc))
          adjList.get(rc).add(key);
        if(key == internalCount *2)
          entry.getValue().add(null);
      }
    }
  }

  // Computes a count for the number of internal vertices in the tree
  private static int countInternal(String tree){
  	int count = 0;
  	while(tree.length() > 1){
	  	if(tree.substring(0,1).equals(","))
	  		count++;
	  	tree = tree.substring(1);
  	}
  	return count;
  }

  // A helper method for labeling a case where an internal vertex has subtrees for both its children.
  private static void labelInternalChild(Integer node, int[] depth, int internalCount, Map<Integer, ArrayList<Integer> > adjList){
    if(node > internalCount){
      Integer leftChild = adjList.get(node).get(0);
      Integer rightChild = adjList.get(node).get(1);
      depth[0]++;
      labelInternalChild(leftChild, depth, internalCount, adjList);
      labelInternalChild(rightChild, depth, internalCount, adjList);
    }
  }

  // (For Debug) Print out the adjacency list
  private static void printAdjList(Map<Integer, ArrayList<Integer>> l) {
  	System.out.println("\n");
  	System.out.println("Adjacency List:");
  	for (Map.Entry<Integer, ArrayList<Integer>> entry : l.entrySet()) {
  		Integer key = entry.getKey();
      System.out.println(key + " (" + entry.getValue().get(0) + "," 
        + entry.getValue().get(1) + ") (" + entry.getValue().get(2) + ")");
  	}
  }

  @Override
  public String toString(){
    return "Tree: " + myName + "\nTree Score: " + this.myScore;
  }

  @Override
  public int compareTo(Tree t) {
    double diff = this.myScore - t.myScore;
    if(diff == 0.0)
      return 0;
    if(diff > 0.0)
      return -1;
    return 1;
  }

}