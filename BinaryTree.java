/**
 * BinaryTree class
 */
public class BinaryTree {
    private class TreeNode {
        Expense data;
        TreeNode left, right;
		/**
		 * contructs a treenode with given expense object
		 *@param Expense data 
		 */
        public TreeNode(Expense data) {
            this.data = data;
            this.left = this.right = null;
        }
    }

    private TreeNode root;

    /**Add an expense to the binary tree
     * @param Expense expense
     * 
     */
    public void addExpense(Expense expense) {
        root = addRecursive(root, expense);
    }
	/**
	 * Arecursive helper method to add an expense to the tree
	 * @param TreeNode node current node in tree
	 * @param Expense expense object to add
	 * @return updated node after insertions
	 * 
	 */
    private TreeNode addRecursive(TreeNode node, Expense expense) {
        if (node == null) return new TreeNode(expense);
        if (expense.compareTo(node.data) < 0) node.left = addRecursive(node.left, expense);
        else if (expense.compareTo(node.data) > 0) node.right = addRecursive(node.right, expense);
        return node;
    }
    
	 public void clear() {
        root = null;
    }
    /** In-order traversal to display expenses
     * 
     */
    public void displayInOrder() {
        inOrderRecursive(root);
    }
	/**
	 * @param TreeNode node , the current node in the tree 
	 */
    private void inOrderRecursive(TreeNode node) {
        if (node != null) {
            inOrderRecursive(node.left);
            System.out.println(node.data);
            inOrderRecursive(node.right);
        }
    }
}
