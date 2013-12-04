import java.io.*;
import java.util.*;
import java.lang.*;

public class Tree{
  
  ArrayList<Edge> edges;

  // Adjacency List

  public Tree(int size, String inFile){
    Map<Integer, ArrayList<Integer> > adjList = new HashMap<Integer, ArrayList<Integer>>();
    startParse(adjList, inFile, size);
//    ArrayList<Integer> nodeList = adjList.getKey();
//    for()







  }

  // B switch with C == 0
  // B switch with D == 1

  public Tree(Edge e, Tree tIn, int switchType){
  }

  private static void startParse(Map<Integer, ArrayList<Integer> > adjList, String file, int taxaCount){
    int internalCount = 0;
    int[] vCount = {0};
    System.out.println("TaxaCount: " + taxaCount);
    try {
        Scanner scanner = new Scanner(new File(file));
        String tree = scanner.nextLine();
        System.out.println("Input tree: " + tree);
        internalCount = countInternal(tree);
        vCount[0] = internalCount + 1;
        parseTree(tree, internalCount, vCount, adjList);
        scanner.close();
    } 
    catch (FileNotFoundException e) {
        e.printStackTrace();
    }
    ArrayList<Integer> dummy = new ArrayList<Integer>();
    dummy.add(null);
    dummy.add(null);
    for(int i = 0; i <= internalCount; i++){
      adjList.put(i , new ArrayList<Integer>(dummy));
    }
    labelParents(adjList, internalCount);
    printAdjList(adjList);
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
}