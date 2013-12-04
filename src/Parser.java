public class Parser{
  
  public static void main(){

  }

  public static String parse(String input, int nodeCount){
    int[] names = {nodeCount};
    ArrayList<String> adjList = new ArrayList<String>();
    parseHelper(adjList, input, names);
    return adjList;
  }

  public static String parseHelper(ArrayList<String> bucket, String input, int[] count){
    if(input.charAt(0)!='(')
      return "(" + input + ")"
    int myComma = findMyComma(input);
    int myCloser = findMyCloser(input, myComma);
  }

  private static int findMyComma(String input){
    int openGroups = 0;
    for(int i = 1; i< input.size() ; i++){
      char current = input.charAt(i);
      if(current == '(')
        openGroups++;
      else if(current == ')')
        openGroups--;
      else if(current == ',') && (openGroups == 0)
        return i;
    }
    return -1;
  }

  private static int findMyCloser(String input, int start){
    int openGroups = 0;
    for(int i = start; i< input.size() ; i++){
      char current = input.charAt(i);
      if(current == '(')
        openGroups++;
      else if(current == ')'){
        if(openGroups != 0)
          openGroups--;
        else
          return i;
    }
    return -1;

  }



}