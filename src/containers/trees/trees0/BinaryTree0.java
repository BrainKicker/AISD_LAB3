package containers.trees.trees0;

import containers.ArrayList;
import containers.Stack;

import java.util.function.Consumer;

public class BinaryTree0<T> {

    protected static class Node<T> {

        public T data;
        public Node<T> left, right;

        public Node(T data) {
            this.data = data;
        }

        public Node(T data, Node<T> left, Node<T> right) {
            this.data = data;
            this.left = left;
            this.right = right;
        }

        public Node<T> getLeft() {
            return left;
        }

        public void setLeft(Node<T> node) {
            left = node;
        }

        public Node<T> getRight() {
            return right;
        }

        public void setRight(Node<T> node) {
            right = node;
        }

        public static <T> void traverse(Node<T> node, Consumer<T> callback) {
            if (node == null)
                return;
            callback.accept(node.data);
            traverse(node.left, callback);
            traverse(node.right, callback);
        }

        public void traverse(Consumer<T> callback) {
            traverse(this, callback);
        }
    }

    public class BinaryTreeIterator {

        protected final ArrayList<Node<T>> ancestors = new ArrayList<>();
        protected final ArrayList<Boolean> dirs = new ArrayList<>(); // true |-> left, false |-> right
        protected Node<T> curNode;

        private BinaryTreeIterator() {
            curNode = root;
        }

        public boolean isRoot() {
            return curNode == root;
        }

        public boolean isNull() {
            return curNode == null;
        }

        public boolean hasLeft() {
            return !isNull() && curNode.left != null;
        }

        public boolean hasRight() {
            return !isNull() && curNode.right != null;
        }

        public T getData() throws NullPointerException {
            return curNode.data;
        }

        public BinaryTreeIterator setNode(T data) {
            if (isNull()) {
                if (isRoot()) {
                    curNode = root = new Node<>(data);
                    size = 1;
                } else {
                    Node<T> parent = ancestors.getLast();
                    boolean isLeft = dirs.getLast();
                    if (isLeft)
                        curNode = parent.left = new Node<>(data);
                    else
                        curNode = parent.right = new Node<>(data);
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

        public BinaryTreeIterator setData(T data) throws NullPointerException {
            curNode.data = data;
            return this;
        }

        public BinaryTreeIterator removeNode() {
            if (isNull())
                throw new NullPointerException();
            if (isRoot()) {
                curNode = root = null;
                size = 0;
            } else {
                final int[] numOfRemovedNodes = {0};
                Node.traverse(curNode, t -> numOfRemovedNodes[0]++);
                Node<T> parent = ancestors.getLast();
                boolean isLeft = dirs.getLast();
                if (isLeft)
                    curNode = parent.left = null;
                else
                    curNode = parent.right = null;
                size -= numOfRemovedNodes[0];
            }
            return this;
        }

        public BinaryTreeIterator removeLeft() throws NullPointerException {
            gotoLeft();
            removeNode();
            gotoParent();
            return this;
        }

        public BinaryTreeIterator removeRight() throws NullPointerException {
            gotoRight();
            removeNode();
            gotoParent();
            return this;
        }

        public BinaryTreeIterator gotoLeft() throws NullPointerException {
            if (curNode == null)
                throw new NullPointerException();
            ancestors.add(curNode);
            dirs.add(true);
            curNode = curNode.left;
            return this;
        }

        public BinaryTreeIterator gotoRight() throws NullPointerException {
            if (curNode == null)
                throw new NullPointerException();
            ancestors.add(curNode);
            dirs.add(false);
            curNode = curNode.right;
            return this;
        }

        public BinaryTreeIterator gotoParent() {
            if (isRoot())
                throw new RuntimeException();
            curNode = ancestors.popLast();
            dirs.removeLast();
            return this;
        }

        public BinaryTreeIterator traverse(Consumer<T> callback) {
            Node.traverse(curNode, callback);
            return this;
        }
    }

    public static containers.trees.BinaryTree<Integer> parseTree(String str) {
        containers.trees.BinaryTree<Integer> binaryTree = new containers.trees.BinaryTree<>();
        containers.trees.BinaryTree<Integer>.BinaryTreeIterator iterator = binaryTree.getIterator();
        Stack<Integer> states = new Stack<>(); // 0 - not added data, 1 - added data, not added left, 2 - added left, not added right, 3 - added right
        states.push(0);
        if (str.charAt(0) == '(' && str.charAt(str.length() - 1) == ')')
            str = str.substring(1, str.length() - 1);
        for (int i = 0; i < str.length();) {
            char c = str.charAt(i);
            if (Character.isWhitespace(c)) {
                i++;
                continue;
            }
            if (c == '(') {
                if (states.top() == 1)
                    iterator.gotoLeft();
                else if (states.top() == 2)
                    iterator.gotoRight();
                else
                    throw new IllegalArgumentException("mistake at position " + i);
                states.push(0);

                i++;
            } else if (c == ')') {
                states.pop();
                iterator.gotoParent();
                int state = states.pop();
                if (state != 1 && state != 2)
                    throw new IllegalArgumentException("mistake at position " + i);
                states.push(state + 1);

                i++;
            } else if (Character.isDigit(c) || c == '-' || c == '+') {
                if (states.top() != 0)
                    throw new IllegalArgumentException("unexpected number at position " + i);

                int j = i + 1;
                while (j < str.length() && Character.isDigit(str.charAt(j)))
                    j++;
                int n = Integer.parseInt(str.substring(i, j));
                iterator.setNode(n);

                states.pop();
                states.push(1);

                i = j;
            } else if (Character.isLetter(c)) {
                if (states.top() != 1 && states.top() != 2)
                    throw new IllegalArgumentException("unexpected letter at position " + i);

                int j = i + 1;
                while (j < str.length() && Character.isLetter(str.charAt(j)))
                    j++;
                String word = str.substring(i, j);
                if (!word.equalsIgnoreCase("null"))
                    throw new IllegalArgumentException("unknown word, expected null on position " + i);

                int state = states.pop();
                if (state != 1 && state != 2)
                    throw new IllegalArgumentException("mistake at position " + i);
                states.push(state + 1);

                i = j;
            }
        }
        return binaryTree;
    }

    private Node<T> root;
    private int size;

    public BinaryTree0() {
    }

    public BinaryTree0(T rootData) {
        this.root = new Node<>(rootData);
        this.size = 1;
    }

    public int getSize() {
        return size;
    }

    public BinaryTreeIterator getIterator() {
        return new BinaryTreeIterator();
    }

    public void traverse(Consumer<T> callback) {
        Node.traverse(root, callback);
    }
}