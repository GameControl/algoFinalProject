import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;
import java.util.ArrayList;

import java.util.HashMap;
import java.io.FileNotFoundException;
import java.io.IOException;


public class MWQT{

  private static final String QMC_EXE = "/qmc/genTreeAndQuartets-Linux-64";
  private static final String RANDOM = "1";
  private static boolean greedy = true;
  private static int iterations = 1;

  public static void main(String[] args){
    if(args.length > 2){
      int input = Integer.valueOf(args[2]);
      if(input > 1)
        iterations = input;
    }
    if(args.length >3){
      if(args[3].equals("-p"))
        greedy = false;
    }
    File inFile = new File(args[0]);
    int count = taxaCount(inFile);
    HashMap<String, ArrayList<Quartet> > quartets = getQuartets(inFile);
    for(int i = 0; i < iterations; i++){
      File qmcFile = new File(callQMC(count));
      String treeNewick = "";
      try {
        Scanner scanner = new Scanner(new File(callQMC(count)));
        treeNewick = scanner.nextLine();
        scanner.close();
      } 
      catch (FileNotFoundException e) {
          e.printStackTrace();
      }
      System.out.println("Input tree: " + treeNewick);
      Tree myTree = new Tree(count, treeNewick, quartets, greedy);
      double originalScore = myTree.getScore();
      System.out.println("Initial Score: " + originalScore);
      System.out.println();
      Tree newTree = myTree;
      double myScore = originalScore; 
      double newScore = myScore;
      long startTime = System.nanoTime();
      long myTime = startTime;
      long newTime = startTime;
      int steps = 0;
      //do{
        //generate a tree with QMC
        //manipulate to find local optima
      //}while you want more
      //score the results
      do{
        steps++;
        myTime = newTime;
        myTree = newTree;
        myScore = newScore;
        newTree = myTree.findBestNeighbor();
        newScore = newTree.getScore();
        newTime = System.nanoTime();
        System.out.println("Step Number: " + steps);
        System.out.println("Time Spent: " + (((double)(newTime - myTime))/1000000000) + " seconds");
        System.out.println("Score: " + newScore);
        System.out.println("Score Improvement: " + (newScore/myScore - 1.0));
        System.out.println("New Tree:\n" + newTree);
        System.out.println();
      }while(myTree.compareTo(newTree) > 0);
      //return the best
      System.out.println();
      System.out.println("TOTAL STEPS: " + steps);
      System.out.println("TOTAL TIME: " + (((double)(newTime - startTime))/1000000000) + " seconds");
      System.out.println("Final tree: " + myTree);
      System.out.println("Final Tree Score: " + myTree.getScore());
      System.out.println("Total Score Improvement: " + (myScore/originalScore - 1.0));
    }
  }

  public static boolean getGreedy(){
    return greedy;
  }

  public static int taxaCount(File input){
    Scanner in = null;
    try{
      in = new Scanner(input);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    int maxSoFar = 0;
    while(in.hasNextLine()){
      String taxa = in.nextLine().split(";")[0];
      taxa = taxa.replaceAll("\\(","");
      taxa = taxa.replaceAll("\\)","");
      String[] numbers = taxa.split(",");
      for(String s: numbers){
        if(maxSoFar < Integer.valueOf(s))
          maxSoFar = Integer.valueOf(s);
      }
    }
    return maxSoFar;
  }

  public static void readQMC(File inFile){
    Scanner qmc = null;
    try{
      qmc = new Scanner(inFile);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    String line = qmc.nextLine();

    //do something with input

  }


  public static String callQMC(int taxa){
    taxa++;
    String cwd = System.getProperty("user.dir");
    String cmd = cwd + QMC_EXE + " " + taxa + " 0 0 "+ RANDOM + "\n";
    //System.out.println(cmd);
    try{
      Process p = Runtime.getRuntime().exec(cmd, null, new File(cwd + "/qmc/"));
      p.waitFor();
    } catch(Exception e){
      e.printStackTrace();
    }
    return cwd + "/qmc/tree-" + taxa + ".dat";
  }

  private static HashMap<String, ArrayList<Quartet> > getQuartets(File inFile){
    Scanner in = null;
    try{
      in = new Scanner(inFile);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    HashMap<String, ArrayList<Quartet> > data = new HashMap<String, ArrayList<Quartet> >();
    while(in.hasNextLine()){
      String line = in.nextLine();
      String[] divided = line.split(";");
      float wgt = Float.valueOf(divided[1].trim());
      line = divided[0];
      line = line.replaceAll("\\(","");
      line = line.replaceAll("\\)","");
      divided = line.split(",");
      Quartet qt = new Quartet(Integer.valueOf(divided[0]),Integer.valueOf(divided[1]),Integer.valueOf(divided[2]),Integer.valueOf(divided[3]), wgt);
      ArrayList<Quartet> list;
      if(data.containsKey(qt.getName())){
        list = data.get(qt.getName());
      }
      else{
        list = new ArrayList<Quartet>();
      }
        list.add(qt);
        data.put(qt.getName(), list);
    }
    return data;
  }

}


