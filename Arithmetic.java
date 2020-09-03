//Thomas Hunt: 4646221
// 12/02/2020

import java.util.Scanner;
import java.util.ArrayList;
import java.math.BigInteger;
import java.lang.StringBuilder;

/**
 *
 * @author hunth702
 */
public class Arithmetic {
  private static ArrayList<String> steps = new ArrayList<String>(); // used to store the steps for + or * for left to right
  private static ArrayList<String> values = new ArrayList<String>(); // gets the input values
  private static ArrayList<String> targets = new ArrayList<String>(); // used for reading in the target valus
  
  
  public static void main(String[] args) {
    
    //Stores each different input of values, and target/type and adds them into two array list
    //So index i for values will be the values corresponding to the target located in targets i
    Scanner scan = new Scanner(System.in);
    while(scan.hasNextLine()){
      values.add(scan.nextLine());
      targets.add(scan.nextLine());
      
    }
    //loops through each recorded input and solves for each
    for(int i = 0; i < values.size(); i++){
      //System.out.println("values: " + values.get(i));
      //System.out.println("targets: " + targets.get(i));
      calculate(values.get(i), targets.get(i));
      
    }
  }
  
  /**
   * @param values A string of space seperated numbers
   * @param targets, A string containing a target value and either L or N
   * method extracts the input values and converts them into an array of ints
   * extracts the target value and whether the order of operations is normal or left to right
   * then solves depending on order of operations type
   */
  private static void calculate(String values, String targets){
    String[] vals = values.split(" ");
    String[] targs  = targets.split(" ");
    int[] nums = new int[vals.length];
    boolean hasOne = false;
    if(values.contains("1")){
      hasOne = true;
    }
      
    
    for(int i = 0; i < vals.length ; i++){
      nums[i] = Integer.parseInt(vals[i]);
    }
    int target = Integer.parseInt(targs[0]);
    String order = targs[1];
    
    // if order of operations is normal do normal
    if(order.equals("N")){
      normal(nums, target, hasOne);
      return;
    }
    // if order of operations is left to right do left to right
    if (order.equals("L")){
      System.out.println(leftToRight(nums, target, hasOne));
   }
    else{
      System.out.println("input error, no specified order of operations");
    }
  }
  
  /**
   * @param nums, an array of integers
   * @param target, and int value representing the target value
   * Create a Binary search tree to find the operations needed to reach target value
   * Will return impossible if the target can't be reached
   */
  private static String leftToRight(int[] nums, int target, boolean hasOnes){
    
    //Minimum and Maximum cases
    // If there is a one then don't calculate min and max
    // If there is no one do calacualte the min and max
    if(hasOnes == false){
      int min = nums[0];
      BigInteger max = BigInteger.valueOf(nums[0]);
      for(int i = 1; i < nums.length ; i++){
        min += nums[i];
        max = max.multiply(BigInteger.valueOf(nums[i]));
      }
      if(min > target){
        return "L " + target + " impossible";
      }
      
      if(max.compareTo(BigInteger.valueOf(target)) == -1){
        return "L " + target + " impossible";
      }
    }
    
    //System.out.println(nums.length);
    
    // creates a root node for bst, that conatins the value of the first number 
    Node node = new Node(nums[0]);
    node.level = 0;
    
   
    try{
    buildTree(nums, node, 1, target);
    }
    catch(FinishException e){ // used to end building the tree early
    }
    
    
    if(steps.size() == 0 || nums.length == 0){ // target is impossible to reach
      return "L " + target + " impossible";
    }
    else{
    // appends all the numbers and the operations into a string to be returned for output
    StringBuilder result = new StringBuilder();
    //System.out.println(steps.size() + " " + nums.length);
    for(int i = 0; i < steps.size(); i++){
      result.append(Integer.toString(nums[i]));
      result.append(" ");
      result.append(steps.get(i));
      result.append(" ");
    }
    result.append(Integer.toString(nums[nums.length - 1]));
    steps.clear();
    return "L " + target + " " + result;
    }
  }
    

    
 // Calls do Normal
  private static void normal(int[] nums, int target, boolean hasOne){
    //if there is a 1 in the input, then dont check min and max
    
    if(hasOne == false){
      int min = nums[0];
      BigInteger max = BigInteger.valueOf(nums[0]);
      for(int i = 1; i < nums.length ; i++){
        min += nums[i];
        max = max.multiply(BigInteger.valueOf(nums[i]));
      }
      if(min > target){
        System.out.println("N " + target + " impossible");
      }
      
      else if(max.compareTo(BigInteger.valueOf(target)) == -1){
        System.out.println("N " + target + " impossible");
      }
      //else{
        
        //doNormal(nums, 1, target, nums[0], 0, Integer.toString(nums[0]));
      //}
    }
    
    if (!doNormal(nums, 1, target, nums[0], 0, Integer.toString(nums[0]))) {
      System.out.println("N " + target + " impossible");
    }
  }
  
  /**
   * @param nums an array of integers containing the numbers from input
   * @param step an int used to keep track of current step in the recursion
   * @param target the target value you're trying to reach
   * @param current the current value based on previous addition/multiplication steps
   * @param leftMult keeps track of the most recent Multiplication step for keeping track of order of opertaions
   * @param expr the current string representation of the steps taken
   * This is a recursive method used to calculate the steps to reach a target value using given numbers
   * Determines where to do addition and multiplication
   * Takes orders of operations into account
   * Does not used a BST like in left to right as it would be harder keeping track of the order of operations as with
   * a tree
   * Is a boolean method so that you can reduce the number of steps it takes, eg if doing additions gets the result then
   * it wont bother doing any recursive multiplication steps making it more effiecent
   */
  private static boolean doNormal(int[] nums, int step,  int target, int current, int leftMult, String expr){
    //if gone over thetarget
    if (current > target) {
      return false;
    }
    
    //base case
    if(step == nums.length){
      //if we have reached the taget on the last step, print the steps taken
      if(current == target){
       System.out.println("N " + target + " " + expr);
       return true;
      }
      
      //otherwise is impossible
      return false;
    }
    //Recusrive addition step
    if (doNormal(nums, step + 1, target, current + nums[step], 0, expr + " + " + nums[step] )) {
      return true;
    }
    
    //Recursive multiplication step, for when the previous step was an addition
    //left Mult gets set to zero after an addition step
    if(leftMult == 0){
      if (doNormal(nums, step + 1, target, current + (nums[step-1] * nums[step]) - nums[step-1], nums[step] * nums[step -1], expr + " * " + nums[step])) {
        return true;
      }
    }
    // Recursive multiplication step for when the previous step was multiplication
    // leftMult contains value of the multipication to the left of the current step
    // will add the next step and the leftMult to current, and subtract the left mult so that the multiplication represents the current value that
    // would be within the brackets, based on previous multiplicaton
    if(leftMult != 0){
      if (doNormal(nums, step + 1, target, current + (leftMult * nums[step]) - leftMult, leftMult * nums[step], expr + " * " + nums[step])) {
        return true;
      }
    }
    
    //impossible
    return false;
    
  }
 /* private static void buildTree(int sum, int previous, int[] nums, int target, String expr){
    if(sum + previous > target){
      return;
  */
 
  /**
   * Builds a binary search tree with left branches being additions and right brancehs being multiplication
   * throws finish exception so tree can be stopped early
   */
  private static void buildTree(int[] nums, Node node,int i, int target) throws FinishException{
   
   
    
    //if this is true then target value not reached
     if(i == nums.length){
       if(node.key == target){
        //calls add steps
        addSteps(node, target, nums);
        //stops any more of tree being built
        throw new FinishException("");
      
    }
       else{
      return;
       }
    }
    // check if plus is valid or not
    if((node.key + nums[i]) > target){
      return;
    }
    // add next number current value
    else{
    node.left = new Node(node.key + nums[i]);
    node.left.parent = node;
    node.left.level = node.level + 1;
    
    buildTree(nums, node.left, i + 1, target);
    }
    
    // check if multiplying is valid or not
    if((node.key * nums[i]) > target){
      
    }
    //add value to right
    else{
    node.right = new Node(node.key * nums[i]);
    node.right.parent = node;
    node.right.level = node.level + 1;
    
    buildTree(nums, node.right, i+ 1, target);
    }
    
    
    
  }
  
 
    
  
  //Once the target is reached, reverse back through the tree by looking at it's parents and recording the steps along the way
  private static void addSteps(Node node, int target, int[] nums){
    //System.out.println(node.parent.level);
    if(node.parent == null){
      return;
    }
    if(node.parent.key == target - nums[node.level]){
      steps.add("+");
      //System.out.println("hello");
      addSteps(node.parent, target - nums[node.level], nums);
    }
    else if(node.parent.key == target / nums[node.level]){
      steps.add("*");
     // System.out.print("howdy");
      addSteps(node.parent, target / nums[node.level], nums);
    }
    
  }
    
  
  // Used to stop a BST from continuing to build
  static class FinishException extends Exception{
    public FinishException(String message){
      super(message);
    }
  }
}
