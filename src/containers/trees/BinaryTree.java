package containers.trees;

import containers.ArrayList;
import containers.Stack;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.function.Consumer;

public class BinaryTree<T> {

    public enum TraversalType {
        DEFAULT, // depth preorder left to right
        BREADTH, // left to right
        BREADTH_LEFT_TO_RIGHT,
        BREADTH_RIGHT_TO_LEFT,
        DEPTH, // preorder left to right
        DEPTH_LEFT_TO_RIGHT, // preorder
        DEPTH_RIGHT_TO_LEFT, // preorder
        DEPTH_PREORDER, // left to right
        DEPTH_INORDER, // left to right
        DEPTH_POSTORDER, // left to right
        DEPTH_PREORDER_LEFT_TO_RIGHT,
        DEPTH_PREORDER_RIGHT_TO_LEFT,
        DEPTH_INORDER_LEFT_TO_RIGHT,
        DEPTH_INORDER_RIGHT_TO_LEFT,
        DEPTH_POSTORDER_LEFT_TO_RIGHT,
        DEPTH_POSTORDER_RIGHT_TO_LEFT
    }

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

        public static <T> void traverse(Node<T> node, Consumer<T> callback) {
            if (node == null)
                return;
            node.traverse(callback);
        }

        public static <T> void traverse(Node<T> node, Consumer<T> callback, TraversalType traversalType) {
            if (node == null)
                return;
            node.traverse(callback, traversalType);
        }

        public void traverse(Consumer<T> callback) {
            traverse(callback, TraversalType.DEFAULT);
        }

        public void traverse(Consumer<T> callback, TraversalType traversalType) {
            switch (traversalType) {
                case BREADTH, BREADTH_LEFT_TO_RIGHT -> traverseBreadthLeftToRight(callback);
                case BREADTH_RIGHT_TO_LEFT -> traverseBreadthRightToLeft(callback);
                case DEFAULT, DEPTH, DEPTH_LEFT_TO_RIGHT, DEPTH_PREORDER, DEPTH_PREORDER_LEFT_TO_RIGHT -> traverseDepthPreorderLeftToRight(callback);
                case DEPTH_RIGHT_TO_LEFT, DEPTH_PREORDER_RIGHT_TO_LEFT -> traverseDepthPreorderRightToLeft(callback);
                case DEPTH_INORDER, DEPTH_INORDER_LEFT_TO_RIGHT -> traverseDepthInorderLeftToRight(callback);
                case DEPTH_INORDER_RIGHT_TO_LEFT -> traverseDepthInorderRightToLeft(callback);
                case DEPTH_POSTORDER, DEPTH_POSTORDER_LEFT_TO_RIGHT -> traverseDepthPostorderLeftToRight(callback);
                case DEPTH_POSTORDER_RIGHT_TO_LEFT -> traverseDepthPostorderRightToLeft(callback);
                default -> throw new Error("unknown traversal type");
            }
        }
        private void traverseBreadthLeftToRight(Consumer<T> callback) {
            Deque<Node<T>> queue = new ArrayDeque<>();
            queue.add(this);
            while (!queue.isEmpty()) {
                Node<T> cur = queue.pop();
                callback.accept(cur.data);
                if (cur.left != null)
                    queue.add(cur.left);
                if (cur.right != null)
                    queue.add(cur.right);
            }
        }
        private void traverseBreadthRightToLeft(Consumer<T> callback) {
            Deque<Node<T>> queue = new ArrayDeque<>();
            queue.add(this);
            while (!queue.isEmpty()) {
                Node<T> cur = queue.pop();
                callback.accept(cur.data);
                if (cur.right != null)
                    queue.add(cur.right);
                if (cur.left != null)
                    queue.add(cur.left);
            }
        }
        private void traverseDepthPreorderLeftToRight(Consumer<T> callback) {
            Stack<Node<T>> stack = new Stack<>();
            stack.push(this);
            while (!stack.empty()) {
                Node<T> cur = stack.pop();
                callback.accept(cur.data);
                if (cur.right != null)
                    stack.push(cur.right);
                if (cur.left != null)
                    stack.push(cur.left);
            }
        }
        private void traverseDepthPreorderRightToLeft(Consumer<T> callback) {
            Stack<Node<T>> stack = new Stack<>();
            stack.push(this);
            while (!stack.empty()) {
                Node<T> cur = stack.pop();
                callback.accept(cur.data);
                if (cur.left != null)
                    stack.push(cur.left);
                if (cur.right != null)
                    stack.push(cur.right);
            }
        }
        private void traverseDepthInorderLeftToRight(Consumer<T> callback) {
            Stack<Node<T>> stack = new Stack<>();
            Node<T> cur = this;
            while (!stack.empty() || cur != null) {
                if (cur != null) {
                    stack.push(cur);
                    cur = cur.left;
                } else {
                    cur = stack.pop();
                    callback.accept(cur.data);
                    cur = cur.right;
                }
            }
        }
        private void traverseDepthInorderRightToLeft(Consumer<T> callback) {
            Stack<Node<T>> stack = new Stack<>();
            Node<T> cur = this;
            while (!stack.empty() || cur != null) {
                if (cur != null) {
                    stack.push(cur);
                    cur = cur.right;
                } else {
                    cur = stack.pop();
                    callback.accept(cur.data);
                    cur = cur.left;
                }
            }
        }
        private void traverseDepthPostorderLeftToRight(Consumer<T> callback) {
            Stack<Node<T>> stack = new Stack<>();
            Node<T> cur = this;
            Node<T> lastVisitedNode = null;
            while (!stack.empty() || cur != null) {
                if (cur != null) {
                    stack.push(cur);
                    cur = cur.left;
                } else {
                    Node<T> topNode = stack.top();
                    if (topNode.right != null && lastVisitedNode != topNode.right) {
                        cur = topNode.right;
                    } else {
                        callback.accept(topNode.data);
                        lastVisitedNode = stack.pop();
                    }
                }
            }
        }
        private void traverseDepthPostorderRightToLeft(Consumer<T> callback) {
            Stack<Node<T>> stack = new Stack<>();
            Node<T> cur = this;
            Node<T> lastVisitedNode = null;
            while (!stack.empty() || cur != null) {
                if (cur != null) {
                    stack.push(cur);
                    cur = cur.right;
                } else {
                    Node<T> topNode = stack.top();
                    if (topNode.left != null && lastVisitedNode != topNode.left) {
                        cur = topNode.left;
                    } else {
                        callback.accept(topNode.data);
                        lastVisitedNode = stack.pop();
                    }
                }
            }
        }
    }

    public class BinaryTreeIterator {

        protected final ArrayList<Node<T>> ancestors = new ArrayList<>();
        protected final ArrayList<Boolean> dirs = new ArrayList<>(); // true |-> left, false |-> right
        protected Node<T> curNode;

        protected BinaryTreeIterator() {
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

        public BinaryTreeIterator gotoRoot() {
            if (isRoot())
                return this;
            curNode = root;
            ancestors.clear();
            dirs.clear();
            return this;
        }

        public BinaryTreeIterator traverse(Consumer<T> callback) {
            Node.traverse(curNode, callback);
            return this;
        }

        public BinaryTreeIterator traverse(Consumer<T> callback, TraversalType traversalType) {
            Node.traverse(curNode, callback, traversalType);
            return this;
        }
    }

    public static BinaryTree<Integer> parseTree(String str) {
        BinaryTree<Integer> binaryTree = new BinaryTree<>();
        BinaryTree<Integer>.BinaryTreeIterator iterator = binaryTree.getIterator();
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

    protected Node<T> root;
    protected int size;

    public BinaryTree() {
    }

    public BinaryTree(T rootData) {
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

    public void traverse(Consumer<T> callback, TraversalType traversalType) {
        Node.traverse(root, callback, traversalType);
    }
}