import java.util.ArrayList;

public class Edge{

  ArrayList<Integer> above;
  ArrayList<Integer> below;

  public Edge(ArrayList<Integer> under, int total){
    above = new ArrayList<Integer>();
    below = new ArrayList<Integer>();
    for(Integer i = 0; i <=total; i++){
      if(under.contains(i)){
        below.add(i);
      }
      else
        above.add(i);
    }
  }

}