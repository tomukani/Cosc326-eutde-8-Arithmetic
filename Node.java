//Thomas Hunt: 4646221
// 12/02/2020

public class Node {
 int key;
 int level;
 Node left, right, parent;
 
 public Node(int item){
  key = item; // current value of the node
  left = null; 
  right = null;
  parent = null;
  level = 0; // keeps track of the leaf level of the node within the tree
  
 }
}
