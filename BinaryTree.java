//Thomas Hunt: 4646221
// 12/02/2020


public class BinaryTree {
 Node root;
 
 BinaryTree(){
  root = null;
 }
 
 BinaryTree(Node node){
   root = node;
 }
 
 public void printInorder(Node node){
  if(node == null){
   return;
  }
  printInorder(node.left);
  System.out.println(node.key);
  printInorder(node.right);
 }
}
