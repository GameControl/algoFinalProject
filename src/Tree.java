import java.io.*;
import java.util.*;
import java.lang.*;

public class Tree implements Comparable<Tree>{
  
  private ArrayList<Edge> edges;
  private String myName;
  private double myScore;

  private static boolean isGreedy;
  private static HashMap<String, ArrayList<Quartet> > myQuartets = null;
  private static int mySize;
  // Adjacency List

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
    myScore = treeScore();
  }

  public Tree(String newTree){
    this(mySize, newTree, myQuartets, isGreedy);
  }

  public ArrayList<Edge> getEdgeSet(){
    return edges;
  }

  public Tree findBestNeighbor(){
    ArrayList<Edge> options = new ArrayList<Edge>();
    for(Edge e: edges){
      options.addAll(e.getNeighbor());
    }
    ArrayList<Tree> treeOptions = new ArrayList<Tree>();
    for(Edge e: options){
//      System.out.println(e.getTree());
      treeOptions.add(new Tree(e.getTree()));
    }
    Collections.sort(treeOptions);
    //System.out.println(treeOptions);
    if(!treeOptions.isEmpty()){
      Tree best = treeOptions.get(0);
      if(best.compareTo(this) < 0)
        return best;
    }
    return this;
  }

  public double getScore(){return myScore;}

  private double treeScore(){
    double score = 0.0;
    for(Edge e: edges){
      score += e.getScore();
    }
    return score;
  }

  private static void startParse(Map<Integer, ArrayList<Integer> > adjList, String tree, int taxaCount, int[] internalCount){
    int[] vCount = {0};
    internalCount[0] = countInternal(tree);
    vCount[0] = internalCount[0] + 1;
    parseTree(tree, internalCount[0], vCount, adjList);
    ArrayList<Integer> dummy = new ArrayList<Integer>();
    dummy.add(null);
    dummy.add(null);
    for(int i = 0; i <= internalCount[0]; i++){
      adjList.put(i , new ArrayList<Integer>(dummy));
    }
    labelParents(adjList, internalCount[0]);
    fixRoot(adjList, internalCount[0]);
  }

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

  private static String parseTree(String str, int internalCount, int[] vCount, Map<Integer, ArrayList<Integer> > adjList){
    Integer left = null;
    Integer right = null;

    if(str.substring(0,1).equals("(")) {
      str = str.substring(1);
      if(str.substring(0,1).equals("(")) {
        str = parseTree(str, internalCount, vCount, adjList);
      }
      else {
        int i = str.indexOf(",");
        left = Integer.valueOf(str.substring(0,i));
        str = str.substring(i);
      }
    }
    
    if(str.substring(0,1).equals(",")) {
      str = str.substring(1);
      if(str.substring(0,1).equals("(")) {
        str = parseTree(str, internalCount, vCount, adjList);
      }
      if(!str.substring(0,1).equals(",") && !str.substring(0,1).equals("(") && !str.substring(0,1).equals(")") && !str.substring(0,1).equals(";")){
        int i = str.indexOf(")");
        right = Integer.valueOf(str.substring(0,i));
        str = str.substring(i);
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
      adjList.put(vCount[0], a);
      vCount[0]++;
    }
    return str;
  }

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


  private static int countInternal(String tree){
  	int count = 0;
  	while(tree.length() > 1){
	  	if(tree.substring(0,1).equals(","))
	  		count++;
	  	tree = tree.substring(1);
  	}
  	return count;
  }

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
    return "Tree: " + myName + "\nTree Score: " + this.treeScore() ;
  }

  @Override
  public int compareTo(Tree t) {
    double diff = this.treeScore() - t.treeScore();
    if(diff == 0.0)
      return 0;
    if(diff > 0.0)
      return -1;
    return 1;

  }

}