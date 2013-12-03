import java.io.*;
import java.util.*;
import java.lang.*;

public class Tree{
  
  ArrayList<Edge> edges;

  // Adjacency List
  public static Map<Integer, ArrayList<String>> adjList = null;
  private static int internalCount = 0;
  private static int vCount = 0;

  public Tree(int size, Scanner inFile){
  }

  // B switch with C == 0
  // B switch with D == 1

  public Tree(Edge e, Tree tIn, int switchType){
  }

  public static void startParse(String file, int taxaCount){
  	System.out.println("TaxaCount: " + taxaCount);
  	adjList = new HashMap<Integer, ArrayList<String>>();
  	try {
        Scanner scanner = new Scanner(new File(file));
        String tree = scanner.nextLine();
        System.out.println("Input tree: " + tree);
        internalCount = countInternal(tree);
        System.out.println("InternalCount: " + internalCount);
        vCount = internalCount + 1;
        String s = parseTree(tree, taxaCount+1);
        scanner.close();
    } 
    catch (FileNotFoundException e) {
        e.printStackTrace();
    }
    printAdjList(adjList);
  }

  private static String parseTree(String str, int count){
  	System.out.println("\nCurrent str: " + str);
  	System.out.println("Current count: " + count);
  	String left = "";
  	String right = "";

 	  if(str.substring(0,1).equals("(")) {
      System.out.println("Found '('");
      str = str.substring(1);
      System.out.println("Moved curser forward: " + str);
      if(str.substring(0,1).equals("(")) {
        System.out.println("Left node requires recursion");
        //str = str.substring(1);
        str = parseTree(str, count+1);
      }
      else {
        System.out.println("Left node is a leaf: " + str.charAt(0));
        left = str.substring(0,1);
        str = str.substring(1);
      }
    }
  	
  	if(str.substring(0,1).equals(",")) {
  		System.out.println("Found ','");
  		str = str.substring(1);
  		System.out.println("Moved curser forward: " + str);
  		if(str.substring(0,1).equals("(")) {
  			System.out.println("Right node requires recursion");
  			//str = str.substring(1);
  			str = parseTree(str, count);
  		}
      if(!str.substring(0,1).equals(",") && !str.substring(0,1).equals("(") && !str.substring(0,1).equals(")") && !str.substring(0,1).equals(";")){
    		System.out.println("Right node is a leaf: " + str.charAt(0));
    		int i = str.indexOf(")");
        right = str.substring(0,i);
        str = str.substring(i);
      }
  	}

  	if(str.substring(0,1).equals(")")) {
  		System.out.println("Found ')'");
  		System.out.println("lc: " + left);
  		System.out.println("rc: " + right);
  		str = str.substring(1);
  		ArrayList<String> a = new ArrayList<String>();

  		// Check for root first
  		if(str.substring(0,1).equals(";")) {
  			addRoot();
  			return "At root!";
  		}

      if(left.equals("") && right.equals("")){
        a.add("" + (vCount - labelInternalChild((Integer)(vCount-1), 1) - 1));
        a.add("" + (vCount-1));
      }
      else {
    		if(left.equals(""))
    			a.add("" + (vCount-1));
        else
    			a.add(left);

        if(right.equals(""))
          a.add("" + (vCount-1));
    		else
          a.add(right);
      }

  		// Check children of root
  		if(leftChildCheck(str))
  			a.add("" + (internalCount*2));
  		else
  			a.add("" + (vCount+1));

  		adjList.put(vCount, a);
      vCount++;
  		//str = parseTree(str, count+1);
  	}

  	if(str.substring(0,1).equals(";")) {
  		System.out.println("Found ';'");
  	}

    System.out.println("**Returning: " + str);
  	return str;
  }

  private static boolean leftChildCheck(String str){
  	int lp = 0;
  	int rp = 0;
  	while(str.length() > 0){
	  	if(str.substring(0,1).equals("("))
	  		lp++;
	  	if(str.substring(0,1).equals(")"))
	  		rp++;
	  	str = str.substring(1);
  	}
  	if((rp-lp) == 1)
  		return true;
  	else
  		return false;
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

  private static void addRoot(){
  	ArrayList<String> a = new ArrayList<String>();
  	for (Map.Entry<Integer, ArrayList<String>> entry : adjList.entrySet()) {
  		Integer key = entry.getKey();
  		String parent = entry.getValue().get(2);
  		if(!adjList.containsKey(Integer.parseInt(parent)))
  			a.add("" + key);
  	}
  	a.add("");
  	adjList.put((internalCount*2), a);
  }

  private static int labelInternalChild(Integer node, int depth){
    Integer leftChild = Integer.parseInt(adjList.get(node).get(0));
    Integer rightChild = Integer.parseInt(adjList.get(node).get(1));

    // Base Case
    if(leftChild <= internalCount && rightChild <= internalCount)
      return depth;

    if(leftChild > internalCount && rightChild > internalCount)
      return labelInternalChild(leftChild, depth+1) + labelInternalChild(rightChild, depth+1);

    if(leftChild > internalCount)
      return labelInternalChild(leftChild, depth+1);
    else
      return labelInternalChild(rightChild, depth+1);
  }

  // (For Debug) Print out the adjacency list
  static void printAdjList(Map<Integer, ArrayList<String>> l) {
  	System.out.println("\n");
  	System.out.println("Adjacency List:");
  	for (Map.Entry<Integer, ArrayList<String>> entry : l.entrySet()) {
  		Integer key = entry.getKey();
      System.out.println(key + " (" + entry.getValue().get(0) + "," 
        + entry.getValue().get(1) + ") (" + entry.getValue().get(2) + ")");
  	}
  }
}