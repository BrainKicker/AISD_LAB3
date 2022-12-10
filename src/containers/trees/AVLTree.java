package containers.trees;

public class AVLTree<T extends Comparable<T>> extends BinaryTree<T> {

    private static class AVLNode<T> extends Node<T> {

        public int height;

        public static <T> int height(AVLNode<T> node) {
            if (node == null)
                return 0;
            return node.height;
        }

        public AVLNode(T data) {
            super(data);
        }

        public AVLNode(T data, AVLNode<T> left, AVLNode<T> right) {
            super(data, left, right);
            fixHeight();
        }

        public int getHeight() {
            return height;
        }

        public void fixHeight() {
            int leftHeight = height((AVLNode<T>) left);
            int rightHeight = height((AVLNode<T>) right);
            height = Math.max(leftHeight, rightHeight) + 1;
        }

        public void computeHeight() {
            if (left != null)
                ((AVLNode<T>) left).computeHeight();
            if (right != null)
                ((AVLNode<T>) right).computeHeight();
            fixHeight();
        }

        public int getDiff() {
            return height((AVLNode<T>) left) - height((AVLNode<T>) right);
        }
    }

    public class ReadOnlyAVLTreeIterator extends FindingAVLTreeIterator {
        @Override
        public FindingAVLTreeIterator setNode(T data) {
            throw new IllegalAccessError();
        }
        @Override
        public BinaryTree<T>.BinaryTreeIterator setData(T data) throws NullPointerException {
            throw new IllegalAccessError();
        }
        @Override
        public BinaryTree<T>.BinaryTreeIterator removeNode() {
            throw new IllegalAccessError();
        }
        @Override
        public BinaryTree<T>.BinaryTreeIterator removeLeft() throws NullPointerException {
            throw new IllegalAccessError();
        }
        @Override
        public BinaryTree<T>.BinaryTreeIterator removeRight() throws NullPointerException {
            throw new IllegalAccessError();
        }
    }

    private class FindingAVLTreeIterator extends BinaryTreeIterator {

        @Override
        public FindingAVLTreeIterator setNode(T data) {
            if (isNull()) {
                if (isRoot()) {
                    curNode = root = new AVLNode<>(data);
                    size = 1;
                } else {
                    AVLNode<T> parent = (AVLNode<T>) ancestors.getLast();
                    boolean isLeft = dirs.getLast();
                    if (isLeft)
                        curNode = parent.left = new AVLNode<>(data);
                    else
                        curNode = parent.right = new AVLNode<>(data);
                    size++;
                }
            } else {
                final int[] numOfChilds = {0};
                Node.traverse(curNode.left, t -> numOfChilds[0]++);
                Node.traverse(curNode.right, t -> numOfChilds[0]++);
                curNode.left = curNode.right = null;
                curNode.data = data;
                size -= numOfChilds[0];
            }
            return this;
        }

        public FindingAVLTreeIterator find(T data) {
            while (curNode != null && !curNode.data.equals(data)) {
                if (curNode.data.compareTo(data) < 0)
                    gotoRight();
                else
                    gotoLeft();
            }
            return this;
        }
    }

    private class AVLTreeIterator extends FindingAVLTreeIterator {

        public void insert(T data) {
            find(data);
            if (!isNull())
                return;
            setNode(data);
            balance();
        }

        public void remove(T data) {
            find(data);
            if (isNull())
                return;
            findPrevValuePasteAndRemove(data);
            balance();
        }

        private void findPrevValuePasteAndRemove(T data) {
            if (!hasLeft()) {
                if (!hasRight()) {
                    removeNode();
                } else {
                    curNode.data = curNode.right.data;
                    removeRight();
                    ((AVLNode<T>) curNode).fixHeight();
                }
            } else {
                Node<T> whereToPaste = curNode;
                gotoLeft();
                find(data);
                if (isNull())
                    gotoParent();
                whereToPaste.data = getData();
                if (hasLeft()) {
                    curNode.data = curNode.left.data;
                    removeLeft();
                    ((AVLNode<T>) curNode).fixHeight();
                } else {
                    removeNode();
                }
            }
            if (!isRoot()) {
                gotoParent();
                ((AVLNode<T>) curNode).fixHeight();
            }
        }

        private void balance() {
            if (isNull())
                if (isRoot())
                    return;
                else
                    gotoParent();
            int diff = ((AVLNode<T>) curNode).getDiff();
            if (diff == 2) { // left bigger
                if (((AVLNode<T>) curNode.left).getDiff() == 1) { // left bigger
                    rightRotation();
                } else { // right bigger
                    bigRightRotation();
                }
            } else if (diff == -2) { // right bigger
                if (((AVLNode<T>) curNode.right).getDiff() == 1) { // left bigger
                    bigLeftRotation();
                } else { // right bigger
                    leftRotation();
                }
            } else {
                ((AVLNode<T>) curNode).fixHeight();
            }
            if (isRoot())
                return;
            gotoParent();
            balance();
        }

        private void rightRotation() {
            if (!isRoot()) {
                AVLNode<T> parent = (AVLNode<T>) ancestors.getLast();
                boolean isLeft = dirs.getLast();
                AVLNode<T> secondNode;
                if (isLeft)
                    secondNode = (AVLNode<T>) (parent.left = curNode.left);
                else
                    secondNode = (AVLNode<T>) (parent.right = curNode.left);
                curNode.left = secondNode.right;
                ((AVLNode<T>) curNode).fixHeight();
                secondNode.right = curNode;
                secondNode.fixHeight();
                curNode = secondNode;
            } else {
                root = curNode.left;
                curNode.left = root.right;
                ((AVLNode<T>) curNode).fixHeight();
                root.right = curNode;
                ((AVLNode<T>) root).fixHeight();
                curNode = root;
            }
        }

        private void leftRotation() {
            if (!isRoot()) {
                AVLNode<T> parent = (AVLNode<T>) ancestors.getLast();
                boolean isLeft = dirs.getLast();
                AVLNode<T> secondNode;
                if (isLeft)
                    secondNode = (AVLNode<T>) (parent.left = curNode.right);
                else
                    secondNode = (AVLNode<T>) (parent.right = curNode.right);
                curNode.right = secondNode.left;
                ((AVLNode<T>) curNode).fixHeight();
                secondNode.left = curNode;
                secondNode.fixHeight();
                curNode = secondNode;
            } else {
                root = curNode.right;
                curNode.right = root.left;
                ((AVLNode<T>) curNode).fixHeight();
                root.left = curNode;
                ((AVLNode<T>) root).fixHeight();
                curNode = root;
            }
        }

        private void bigRightRotation() {
            if (!isRoot()) {
                AVLNode<T> parent = (AVLNode<T>) ancestors.getLast();
                boolean isLeft = dirs.getLast();
                AVLNode<T> secondNode = (AVLNode<T>) curNode.left;
                AVLNode<T> thirdNode = (AVLNode<T>) secondNode.right;
                secondNode.right = thirdNode.left;
                secondNode.fixHeight();
                curNode.left = thirdNode.right;
                ((AVLNode<T>) curNode).fixHeight();
                thirdNode.left = secondNode;
                thirdNode.right = curNode;
                thirdNode.fixHeight();
                if (isLeft)
                    curNode = parent.left = thirdNode;
                else
                    curNode = parent.right = thirdNode;
            } else {
                AVLNode<T> secondNode = (AVLNode<T>) curNode.left;
                AVLNode<T> thirdNode = (AVLNode<T>) secondNode.right;
                secondNode.right = thirdNode.left;
                secondNode.fixHeight();
                curNode.left = thirdNode.right;
                ((AVLNode<T>) curNode).fixHeight();
                thirdNode.left = secondNode;
                thirdNode.right = curNode;
                thirdNode.fixHeight();
                root = thirdNode;
            }
        }

        private void bigLeftRotation() {
            if (!isRoot()) {
                AVLNode<T> parent = (AVLNode<T>) ancestors.getLast();
                boolean isLeft = dirs.getLast();
                AVLNode<T> secondNode = (AVLNode<T>) curNode.right;
                AVLNode<T> thirdNode = (AVLNode<T>) secondNode.left;
                secondNode.left = thirdNode.right;
                secondNode.fixHeight();
                curNode.right = thirdNode.left;
                ((AVLNode<T>) curNode).fixHeight();
                thirdNode.right = secondNode;
                thirdNode.left = curNode;
                thirdNode.fixHeight();
                if (isLeft)
                    curNode = parent.left = thirdNode;
                else
                    curNode = parent.right = thirdNode;
            } else {
                AVLNode<T> secondNode = (AVLNode<T>) curNode.right;
                AVLNode<T> thirdNode = (AVLNode<T>) secondNode.left;
                secondNode.left = thirdNode.right;
                secondNode.fixHeight();
                curNode.right = thirdNode.left;
                ((AVLNode<T>) curNode).fixHeight();
                thirdNode.right = secondNode;
                thirdNode.left = curNode;
                thirdNode.fixHeight();
                root = thirdNode;
            }
        }
    }

    public AVLTree() {
        super();
    }

    public AVLTree(T rootData) {
        this.root = new AVLNode<>(rootData);
        this.size = 1;
    }

    @Override
    public ReadOnlyAVLTreeIterator getIterator() {
        return new ReadOnlyAVLTreeIterator();
    }

    private AVLTreeIterator getWritableIterator() {
        return new AVLTreeIterator();
    }

    public void insert(T data) {
        getWritableIterator().insert(data);
    }

    public void remove(T data) {
        getWritableIterator().remove(data);
    }

    public ReadOnlyAVLTreeIterator find(T data) {
        return (ReadOnlyAVLTreeIterator) getIterator().find(data);
    }

    public int getHeight() {
        return AVLNode.height(((AVLNode<T>) root));
    }
}