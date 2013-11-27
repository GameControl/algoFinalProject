import java.util.ArrayList;
import java.util.Collections;

public class Quartet{

  Integer left1;
  Integer left2;
  Integer right1;
  Integer right2;
  String name;
  float weight;

  public Quartet(int a, int b, int c, int d, float wgt){
    left1 = Integer.valueOf(a);
    left2 = Integer.valueOf(b);
    right1 = Integer.valueOf(c);
    right2 = Integer.valueOf(d);
    weight = wgt;
    ArrayList<Integer> nameMaker = new ArrayList<Integer>();
    nameMaker.add(left1);
    nameMaker.add(left2);
    nameMaker.add(right1);
    nameMaker.add(right2);
    Collections.sort(nameMaker);
    name = "";
    for(Integer s: nameMaker){
      name += ":" + s.toString();
    }
  }

  public boolean satisfies(ArrayList<Integer> left, ArrayList<Integer> right){
    return (left.contains(left1) && left.contains(left2)) && (right.contains(right1) && right.contains(right2));
  }

  public Integer[] above(){
    Integer[] output = {left1, left2};
    return output;
  }

  public Integer[] below(){
    Integer[] output = {right1, right2};
    return output;
  }

  public String getName(){
    return name;
  }

  @Override
  public String toString(){
    return "((" +left1.toString() + "," + left2.toString() + "),("+ right1.toString() + "," + right2.toString()+ "))";
  }

}