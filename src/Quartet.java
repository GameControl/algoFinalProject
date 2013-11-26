import java.util.ArrayList;

public class Quartet{

  Integer left1;
  Integer left2;
  Integer right1;
  Integer right2;

  float weight;

  public Quartet(int a, int b, int c, int d, float wgt){
    left1 = Integer.valueOf(a);
    left2 = b;
    right1 = c;
    right2 = d;
    weight = wgt;
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



}