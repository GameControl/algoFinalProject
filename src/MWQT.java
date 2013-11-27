import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;
import java.util.ArrayList;

import java.util.HashMap;
import java.io.FileNotFoundException;
import java.io.IOException;


public class MWQT{

  private static final String QMC_EXE = "/qmc/genTreeAndQuartets-Linux-64";

  public static void main(String[] args){

    File inFile = new File(args[0]);
    int count = taxaCount(inFile);
    HashMap<String, ArrayList<Quartet> > quartets = getQuartets(inFile);
    File qmcFile = new File(callQMC(count));
    //for(String s: quartets.keySet()){
      //System.out.println(s + " : " + quartets.get(s));
      
    //}
    readQMC(qmcFile);


    //do{
      //generate a tree with QMC
      //manipulate to find local optima
    //}while you want more
    //score the results
    //return the best
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

    //do something with input

  }


  public static String callQMC(int taxa){
    taxa++;
    String cwd = System.getProperty("user.dir");
    String cmd = cwd + QMC_EXE + " " + taxa + " 0 0 1\n";
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


