import java.util.Scanner;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.File;
import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.io.IOException;

/*
 * MWQT:
 * Driver class for our Maximum Weight Quartet Compatibility algorithm.
 * Iterates over the algorithm a given number of times and writes verbose 
 * output to the specified output file.
 *
 * I/O:
 * Input is given in a text file of quartet trees with assigned weights.
 * Output is a locally optimal tree which satisfies a maximum weight subset of
 * the input quartets. 
 */
public class MWQT{

  private static final String QMC_EXE = "/qmc/genTreeAndQuartets-Linux-64";

  /*
   * We used this to switch between random and non random trees for testing.
   * QMC takes a 1 for random and 0 for not.
   */
  private static final String RANDOM = "1";

  /* 
   * A boolean value for determining if we are greedy with edge neighbors
   * or tree neighbors. We are greedy with edges by default.
   */
  private static boolean greedy = true;

  // The number of iterations to run the algorithm 
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
    try{
      FileWriter fw = new FileWriter(args[1]);

      StringBuilder outputBuilder = new StringBuilder();
      File inFile = new File(args[0]);
      int count = taxaCount(inFile);
      HashMap<String, ArrayList<Quartet> > quartets = getQuartets(inFile);
      Tree ourBest = null;
      long totalTime = 0;
      double totalImprovement = 0.0;
      double bestsImprovement = 0.0;

      // Do multiple iterations over many different random starting trees
      for(int i = 0; i < iterations; i++){
        outputBuilder.append("--------------ITERATION " + (i+1) + "--------------\n");
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
        Tree myTree = new Tree(count, treeNewick, quartets, greedy);
        if(ourBest==null)
          ourBest = myTree;
        double originalScore = myTree.getScore();
        outputBuilder.append("Input tree: " + treeNewick + "\n");
        outputBuilder.append("Initial Score: " + originalScore + "\n\n");
        Tree newTree = myTree;
        double myScore = originalScore; 
        double newScore = myScore;
        long startTime = System.nanoTime();
        long myTime = startTime;
        long newTime = startTime;
        int steps = 0;

        /*
         * do{
         * generate a tree with QMC
         * manipulate to find local optima
         * } while new tree is better than old tree
         * score the results
         */
        do{
          steps++;
          myTime = newTime;
          myTree = newTree;
          myScore = newScore;
          newTree = myTree.findBestNeighbor();
          newScore = newTree.getScore();
          newTime = System.nanoTime();
          long deltaTime = newTime - myTime;
          double deltaScore = (newScore/myScore - 1.0);
          outputBuilder.append("Step Number: " + steps + "\n");
          outputBuilder.append("Time Spent: " + (deltaTime/1000000000.0) + " seconds\n");
          outputBuilder.append("New Tree:\n" + newTree + "\n");
          outputBuilder.append("Score Improvement: " + deltaScore + "\n\n");
        // This is the second greedy choice we make, no matter what greedy is set to. We only consider the best tree for our next step
        }while(myTree.compareTo(newTree) > 0);

        // Return the best
        if(myTree.compareTo(ourBest) < 0){
          ourBest = myTree;
          bestsImprovement = (ourBest.getScore()/originalScore - 1.0);
        }
        long deltaTime = newTime - startTime;
        totalTime += deltaTime;
        double deltaScore = (myTree.getScore()/originalScore - 1.0);
        totalImprovement += deltaScore;
        outputBuilder.append("TOTAL STEPS: " + steps + "\n");
        outputBuilder.append("TOTAL TIME: " + ((newTime - startTime)/1000000000.0) + " seconds\n");
        outputBuilder.append("Final tree: " + myTree + "\n");
        outputBuilder.append("Total Score Improvement: " + deltaScore + "\n\n");
        fw.append(outputBuilder.toString());
        outputBuilder = new StringBuilder();
      }
      outputBuilder.append("===========================================================\n");
      outputBuilder.append("RESULTS FROM " + iterations + " ITERATION(S):\n");
      outputBuilder.append("AVERAGE TIME PER RUN: " + (totalTime/iterations)/1000000000.0 + " seconds\n");
      outputBuilder.append("AVERAGE IMPROVEMENT: " + totalImprovement/iterations + "\n");
      outputBuilder.append("BEST " + ourBest + "\n");
      outputBuilder.append("BEST'S IMPROVEMENT: " + bestsImprovement + "\n");
      fw.append(outputBuilder.toString());
      outputBuilder = new StringBuilder();
      fw.close();
    }
    catch (Exception e) {
      e.printStackTrace();
    } 
  }

  public static boolean getGreedy(){
    return greedy;
  }

  // Breaks the tree into just numbers. We need to know the taxa count for making tree.
  public static int taxaCount(File input){
    int maxSoFar = 0;
    try{
      Scanner in = new Scanner(input);
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
      in.close();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    return maxSoFar;
  }

  // Calls qmc for a random starting tree
  public static String callQMC(int taxa){
    taxa++;
    String cwd = System.getProperty("user.dir");
    String cmd = cwd + QMC_EXE + " " + taxa + " 0 0 "+ RANDOM + "\n";
    try{
      Process p = Runtime.getRuntime().exec(cmd, null, new File(cwd + "/qmc/"));
      p.waitFor();
    } catch(Exception e){
      e.printStackTrace();
    }
    return cwd + "/qmc/tree-" + taxa + ".dat";
  }

  // Builds a map of the quartets
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
    in.close();
    return data;
  }

}


