public class TwoFourTree {
    private class TwoFourTreeItem {
        int values = 1;
        int value1 = 0;                             // always exists.
        int value2 = 0;                             // exists iff the node is a 3-node or 4-node.
        int value3 = 0;                             // exists iff the node is a 4-node.
        boolean isLeaf = true;

        TwoFourTreeItem parent = null;              // parent exists iff the node is not root.
        TwoFourTreeItem leftChild = null;           // left and right child exist iff the note is a non-leaf.
        TwoFourTreeItem rightChild = null;
        TwoFourTreeItem centerChild = null;         // center child exists iff the node is a non-leaf 3-node.
        TwoFourTreeItem centerLeftChild = null;     // center-left and center-right children exist iff the node is a non-leaf 4-node.
        TwoFourTreeItem centerRightChild = null;

        public boolean isTwoNode() {

            if (values == 1) {
                hasChildren(this);
                return true;
            } else
                return false;
        }

        public boolean isThreeNode() {

            if (values == 2) {
                hasChildren(this);
                return true;
            } else
                return false;
        }

        public boolean isFourNode() {

            if (values == 3) {
                hasChildren(this);
                return true;
            } else
                return false;
        }

        public boolean isRoot() {
            if (parent == null) {
                return true;
            }
            return false;
        }

        public TwoFourTreeItem(int v1) {
            value1 = v1;
            values = 1;
        }

        public TwoFourTreeItem(int v1, int v2) {
            if (v1 <= v2) {
                value1 = v1;
                value2 = v2;
                values = 2;
            } else {
                value1 = v2;
                value2 = v1;
                values = 2;
            }

        }

        public TwoFourTreeItem(int v1, int v2, int v3) {
            //value 1 <= 2
            if (v1 <= v2) {
                if (v2 <= v3) {
                    value1 = v1;
                    value2 = v2;
                    value3 = v3;
                    values = 3;
                } else if (v1 <= v3) {
                    value1 = v1;
                    value2 = v3;
                    value3 = v2;
                    values = 3;
                } else {
                    value1 = v3;
                    value2 = v1;
                    value3 = v2;
                    values = 3;
                }


            }
            //value 1 > 2
            else {
                if (v1 <= v3) {
                    value1 = v2;
                    value2 = v1;
                    value3 = v3;
                    values = 3;
                } else if (v3 <= v2) {
                    value1 = v3;
                    value2 = v2;
                    value3 = v1;
                    values = 3;
                } else {
                    value1 = v2;
                    value2 = v3;
                    value3 = v1;
                    values = 3;
                }
            }
        }

        private void printIndents(int indent) {
            for (int i = 0; i < indent; i++) System.out.printf("  ");
        }

        public void printInOrder(int indent) {
            if (!isLeaf) leftChild.printInOrder(indent + 1);
            printIndents(indent);
            System.out.printf("%d\n", value1);
            if (isThreeNode()) {
                if (!isLeaf) centerChild.printInOrder(indent + 1);
                printIndents(indent);
                System.out.printf("%d\n", value2);
            } else if (isFourNode()) {
                if (!isLeaf) centerLeftChild.printInOrder(indent + 1);
                printIndents(indent);
                System.out.printf("%d\n", value2);
                if (!isLeaf) centerRightChild.printInOrder(indent + 1);
                printIndents(indent);
                System.out.printf("%d\n", value3);
            }
            if (!isLeaf) rightChild.printInOrder(indent + 1);
        }
    }

    TwoFourTreeItem root = null;

    public boolean addValue(int value) {
        //suppose this is a new tree, add to root
        if (root == null) {
            root = new TwoFourTreeItem(value);
            return true;
        }

        //what if we have a root, and it had no child nodes?
        if (root.isLeaf) {
            //check if values 2 and 3 exist. If so value 2 is the new root and 1,3 become children nodes.
            // Then we continue with adding current value.
            if (root.isFourNode()) {
                split(root);
            } else if (root.isThreeNode()) {
                if (value <= root.value1) {
                    root.value3 = root.value2;
                    root.value2 = root.value1;
                    root.value1 = value;

                } else if (value <= root.value2) {
                    root.value3 = root.value2;
                    root.value2 = value;
                } else {
                    root.value3 = value;
                }

                root.values++;
                return true;
            } else {
                if (value <= root.value1) {
                    root.value2 = root.value1;
                    root.value1 = value;
                } else {
                    root.value2 = value;
                }
                root.values++;
                return true;
            }
        }
        //traverse the tree till we find where to put the value
        TwoFourTreeItem found = traverseDown(value);
        //System.out.println("\n Reached after traversal ");

        //check if it is a 2 node
        if (found.values == 1) {
            if (value < found.value1) {
                found.value2 = found.value1;
                found.value1 = value;
            } else {
                found.value2 = value;
            }
            found.values++;
        }

        //else it is a 3 node
        else {
            if (value <= found.value1) {
                found.value3 = found.value2;
                found.value2 = found.value1;
                found.value1 = value;

            } else if (value <= found.value2) {
                found.value3 = found.value2;
                found.value2 = value;
            } else {
                found.value3 = value;
            }
            found.values++;
        }

        return false;
    }

    //return node if we found the node to insert item at
    public TwoFourTreeItem traverseDown(int item) {

        TwoFourTreeItem walker;
        walker = root;


        while (!walker.isLeaf) {
            //check where item should go
            if (walker.isTwoNode()) {

                if (item < walker.value1) {
                    walker = walker.leftChild;
                }

                else {
                    walker = walker.rightChild;
                }

            }

            else if (walker.isThreeNode()) {

                if (item < walker.value1) {
                    walker = walker.leftChild;
                }

                else if (item < walker.value2) {
                    walker = walker.centerChild;
                }

                else {
                    walker = walker.rightChild;
                }
            }

            //if we find a four node we split it
            else if (walker.isFourNode()) {

                if (!walker.isLeaf && !walker.isRoot()) {
                    split(walker);
                    walker = walker.parent;
                }

                else {
                    split(walker);
                }
            }

        }

        //after traversal Check if we need to split the leaf node
        if (walker.isFourNode()) {
            if(walker.isLeaf){
                split(walker);
                walker = walker.parent;

                if (walker.isTwoNode()) {
                    if (item < walker.value1) {
                        walker = walker.leftChild;
                    }

                    else {
                        walker = walker.rightChild;
                    }

                }

                else if (walker.isThreeNode()) {
                    if (item < walker.value1) {
                        walker = walker.leftChild;
                    }

                    else if (item < walker.value2) {
                        walker = walker.centerChild;
                    }

                    else {
                        walker = walker.rightChild;
                    }
                }

                else{
                    if (item < walker.value1) {
                        walker = walker.leftChild;
                    }

                    else if (item < walker.value2) {
                        walker = walker.centerLeftChild;
                    }

                    else if(item < walker.value3){
                        walker = walker.centerRightChild;
                    }

                    else{
                        walker = walker.rightChild;
                    }
                }
            }

            else {
                split(walker);
            }
        }

        return walker;
    }

    //split 4-node
    //returns true if split
    public boolean split(TwoFourTreeItem node) {

        //if node to split is root then we make 2 children and pass on their respective values
        TwoFourTreeItem leftNode, rightNode;
        if (node.isRoot()) {
            leftNode = new TwoFourTreeItem(node.value1);
            rightNode = new TwoFourTreeItem(node.value3);

            //if root doesn't have children, set leftNode and 2 parent = root
            leftNode.parent = node;
            rightNode.parent = node;


            leftNode.leftChild = node.leftChild;
            leftNode.rightChild = node.centerLeftChild;

            rightNode.leftChild = node.centerRightChild;
            rightNode.rightChild = node.rightChild;

            node.leftChild = leftNode;
            node.rightChild = rightNode;
            node.centerRightChild = null;
            node.centerLeftChild = null;


            node.value1 = node.value2;
            node.value2 = 0;
            node.value3 = 0;
            node.values = 1;

            if (!node.isLeaf) {
                leftNode.isLeaf = false;
                rightNode.isLeaf = false;

                leftNode.leftChild.parent = leftNode;
                leftNode.rightChild.parent = leftNode;

                rightNode.leftChild.parent = rightNode;
                rightNode.rightChild.parent = rightNode;

            }

            node.isLeaf = false;
            root.isLeaf = false;

            return true;

        }

        //send middle value to parent node.
        int send = node.value2;
        boolean isLeft = node.parent.leftChild == node;
        boolean isRight = node.parent.rightChild == node;
        boolean isCenter = node.parent.centerChild == node;

        if (node.parent.isTwoNode()) {
            //move value 2 up to parent first, then split the node
            if (send <= node.parent.value1) {
                node.parent.value2 = node.parent.value1;
                node.parent.value1 = send;

            } else {
                node.parent.value2 = send;

            }

            //split node now.
            //let's deal if the current node is a leaf node first
            if (node.isLeaf && isLeft) {
                //move value 3 to center child
                rightNode = new TwoFourTreeItem(node.value3);
                node.parent.centerChild = rightNode;
                rightNode.parent = node.parent;

                //set current code's values 2 and 3 to 0
                updateChild(node);

                //node.isLeaf = false;
                return true;
            } else if (node.isLeaf && isRight) {
                //move value 3 to center child
                leftNode = new TwoFourTreeItem(node.value1);
                node.parent.centerChild = leftNode;
                leftNode.parent = node.parent;
                node.value1 = node.value3;

                //set current code's values 2 and 3 to 0
                updateChild(node);

                // node.isLeaf = false;
                return true;
            }
            //if it isn't a leaf node
            else if (isLeft) {
                //create new node for the parents left child to be
                rightNode = new TwoFourTreeItem(node.value3);

                //fill leftNode node's children
                rightNode.leftChild = node.centerRightChild;
                rightNode.rightChild = node.rightChild;



                //have parent point to appropriate nodes
                node.centerRightChild.parent = rightNode;
                node.rightChild.parent = rightNode;
                node.parent.centerChild = rightNode;
                rightNode.parent = node.parent;

                //node by the end must be a two node
                node.rightChild = node.centerLeftChild;

                if(!node.isLeaf){
                    rightNode.isLeaf = false;
                }

                //set node to a Two node format
                updateChild(node);

                //node.isLeaf = false;
                return true;
            } else {
                //create new node for the parents left child to be
                leftNode = new TwoFourTreeItem(node.value1);

                //fill leftNode node's children
                leftNode.leftChild = node.leftChild;
                leftNode.rightChild = node.centerLeftChild;

                //node by the end must be a two node
                node.leftChild= node.centerRightChild;

                //have parent point to appropriate nodes
                node.parent.centerChild = leftNode;
                node.parent.rightChild = node;
                leftNode.parent = node.parent;

                //set node to a Two node format
                node.value1 = node.value3;
                if(!node.isLeaf){
                    leftNode.isLeaf = false;
                }

                updateChild(node);

                // node.isLeaf = false;

                return true;
            }


        }

        else if (node.parent.isThreeNode()) {
            //move value 2 up to head first then split
            if (send <= node.parent.value1) {
                node.parent.value3 = node.parent.value2;
                node.parent.value2 = node.parent.value1;
                node.parent.value1 = send;
            } else if (send <= node.parent.value2) {
                node.parent.value3 = node.parent.value2;
                node.parent.value2 = send;
            } else {
                node.parent.value3 = send;
            }

            /////////////////////different section///////////////////////

            if (node.isLeaf && isLeft) {
                //leftNode will be center left child and current node will stay left
                rightNode = new TwoFourTreeItem(node.value3);
                node.parent.centerLeftChild = rightNode;
                node.parent.centerRightChild = node.parent.centerChild;
                node.parent.centerChild = null;
                rightNode.parent = node.parent;

                //set current code's values 2 and 3 to 0
                node.value2 = 0;
                node.value3 = 0;
                node.values = 1;
                node.parent.values++;

                // node.isLeaf = false;
                return true;
            }

            else if (node.isLeaf && isRight) {
                //rightNode will be center right child and current node will still be the right child
                leftNode = new TwoFourTreeItem(node.value1);
                node.parent.centerRightChild = leftNode;
                node.parent.centerLeftChild = node.parent.centerChild;
                node.parent.centerChild = null;
                leftNode.parent = node.parent;
                node.value1 = node.value3;

                //set current code's values 2 and 3 to 0
                node.value2 = 0;
                node.value3 = 0;
                node.values = 1;
                node.parent.values++;

                //node.isLeaf = false;
                return true;
            }

            else if (node.isLeaf && isCenter) {
                //move value 3 to center right child
                rightNode = new TwoFourTreeItem(node.value3);
                node.parent.centerLeftChild = node;
                node.parent.centerRightChild = rightNode;
                node.parent.centerChild = null;
                rightNode.parent = node.parent;

                updateChild(node);
                return true;

            }
            //has children nodes
            else if (isLeft) {
                //move value 3 to center left child
                rightNode = new TwoFourTreeItem(node.value3);
                node.parent.centerLeftChild = rightNode;
                rightNode.parent = node.parent;

                //since our node is a 4-node we need to move the centerRightChild and RightChild to the new node
                rightNode.leftChild = node.centerRightChild;
                rightNode.rightChild = node.rightChild;

                //make node's children point to center left child
                node.centerRightChild.parent = rightNode;
                node.rightChild.parent = rightNode;

                //reassign node's center left child to be its right
                node.rightChild = node.centerLeftChild;

                node.parent.centerRightChild = node.parent.centerChild;
                node.parent.centerChild = null;

                updateChild(node);
            } else if (isRight) {
                //move value 1 to center right child
                leftNode = new TwoFourTreeItem(node.value1);
                node.parent.centerRightChild = leftNode;
                leftNode.parent = node.parent;

                leftNode.leftChild = node.leftChild;
                leftNode.rightChild = node.centerLeftChild;

                node.centerLeftChild.parent = leftNode;
                node.leftChild.parent = leftNode;

                node.leftChild = node.centerRightChild;
                node.value1 = node.value3;

                //move parent's center child to centerLeft child
                node.parent.centerLeftChild = node.parent.centerChild;
                node.parent.centerChild = null;

                //update the current node
                updateChild(node);

                //node.isLeaf = false;
                return true;
            }
            //node is a Center child
            else {
                //create new node
                rightNode = new TwoFourTreeItem(node.value3);
                node.parent.centerLeftChild = node;
                node.parent.centerRightChild = rightNode;
                node.parent.centerChild = null;
                rightNode.parent = node.parent;

                //set new node's children
                rightNode.leftChild = node.centerRightChild;
                rightNode.rightChild = node.rightChild;

                //set node's children parent pointer properly


                node.rightChild = node.centerLeftChild;

                if(!node.isLeaf){
                    node.centerRightChild.parent = rightNode;
                    node.rightChild.parent = rightNode;
                    rightNode.isLeaf = false;
                }

                updateChild(node);

                return true;
            }

        }

        return false;
    }

    public void updateChild(TwoFourTreeItem node) {

        node.centerRightChild = null;
        node.centerLeftChild = null;
        node.centerChild = null;

        node.value2 = 0;
        node.value3 = 0;
        node.values = 1;
        node.parent.values++;
        hasChildren(node);
    }

    public void hasChildren(TwoFourTreeItem node){
        if(node.leftChild != null && node.rightChild != null) {
            node.isLeaf = false;
        }
    }

    //traverse the tree to check if it already contains a certain value
    public boolean hasValue(int value) {
        TwoFourTreeItem walker;
        walker = root;


        while (!walker.isLeaf) {
            //check where item should go
            if (walker.isTwoNode()) {
                if(value == walker.value1)
                    return true;

                if (value < walker.value1) {
                    walker = walker.leftChild;
                }

                else {
                    walker = walker.rightChild;
                }

            }

            else if (walker.isThreeNode()) {
                if(value == walker.value1 || value == walker.value2)
                    return true;

                if (value < walker.value1) {
                    walker = walker.leftChild;
                }

                else if (value < walker.value2) {
                    walker = walker.centerChild;
                }

                else {
                    walker = walker.rightChild;
                }
            }

            //if we find a four node we split it
            else if (walker.isFourNode()) {
                if(value == walker.value1 || value == walker.value2 || value == walker.value3)
                    return true;

                if (value < walker.value1) {
                    walker = walker.leftChild;
                }

                else if (value < walker.value2) {
                    walker = walker.centerLeftChild;
                }

                else if(value < walker.value3){
                    walker = walker.centerRightChild;
                }
                else{
                    walker = walker.rightChild;
                }
            }

        }

        if(value == walker.value1 || value == walker.value2 || value == walker.value3)
            return true;

        return false;
    }

    //deletes a node with a certain value
    public boolean deleteValue(int value) {
        return false;
    }

    public void printInOrder() {
        if (root != null) root.printInOrder(0);
    }

    public TwoFourTree() {

    }
}
