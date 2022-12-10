import containers.trees.AVLTree;
import containers.trees.BinaryTree;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.function.Consumer;

public class Main {

    public static void main(String[] args) throws IOException {
//        BinaryTree<Integer> binaryTree = new BinaryTree<>();
//        BinaryTree<Integer>.BinaryTreeIterator binaryTreeIterator = binaryTree.getIterator();
//        System.out.println(binaryTreeIterator.isRoot());
//        System.out.println(binaryTreeIterator.isNull());
//        System.out.println("---");
//        binaryTreeIterator.setNode(2);
//        binaryTreeIterator.gotoLeft();
//        binaryTreeIterator.setNode(3);
//        binaryTreeIterator.gotoRight();
//        binaryTreeIterator.setNode(4);
//        binaryTreeIterator.gotoParent();
//        binaryTreeIterator.gotoLeft();
//        binaryTreeIterator.setNode(1);
//        binaryTreeIterator.gotoParent();
//        binaryTreeIterator.gotoParent();
//        binaryTreeIterator.gotoRight();
//        binaryTreeIterator.setNode(60);
//        binaryTreeIterator.gotoRight();
//        binaryTreeIterator.setNode(0);
//        binaryTreeIterator.gotoRight();
//        binaryTreeIterator.setNode(0);
//        binaryTreeIterator.gotoParent();
//        binaryTreeIterator.removeNode();
//        binaryTreeIterator.gotoParent();
//        binaryTreeIterator.setData(50);
//        binaryTreeIterator.gotoLeft();
//        binaryTreeIterator.setNode(990);
//        binaryTreeIterator.gotoParent();
//        binaryTreeIterator.removeLeft();
//        binaryTree.traverse(integer -> System.out.println(integer + " "), BinaryTree.TraversalType.BREADTH);
//        System.out.println("---");
//        System.out.println(binaryTree.getSize());

//        BinaryTree.parseTree("(8 (9 (5)) (1))").traverse(integer -> System.out.println(integer + " "), BinaryTree.TraversalType.BREADTH);

//        AVLTree<Integer> avl = new AVLTree<>();
//        avl.insert(1);
//        avl.insert(2);
//        avl.insert(3);
//        avl.insert(2);
//        avl.insert(5);
//        avl.insert(4);
//        avl.insert(9);
//        avl.insert(8);
//        avl.insert(7);
//        avl.remove(3);
//        avl.remove(20);
//        avl.insert(55);
//        avl.insert(66);
//        for (int i = 100; i < 200; i++)
//            avl.insert(i);
//        avl.insert(99);
//        avl.insert(-11);
//        for (int i = 400; i >= 300; i--)
//            avl.insert(i);
//        avl.insert(500);
//        avl.insert(-600);
//        avl.insert(700);
//        avl.traverse(i -> System.out.println(i + " "), BinaryTree.TraversalType.DEPTH_INORDER);
//        System.out.println("---");
//        System.out.println(avl.getSize() + " " + avl.getHeight());

        String s = new BufferedReader(new FileReader("src/tree.txt")).readLine();
        BinaryTree<Integer> tree = BinaryTree.parseTree(s);
        tree.traverse(i -> System.out.println(i + " "), BinaryTree.TraversalType.DEPTH);
        System.out.println("---");

        AVLTree<Integer> avl = new AVLTree<>();

        tree.traverse(avl::insert);

        avl.traverse(System.out::println, BinaryTree.TraversalType.BREADTH);
        System.out.println("---");
        avl.traverse(System.out::println, BinaryTree.TraversalType.DEPTH_PREORDER);
        System.out.println("---");
        avl.traverse(System.out::println, BinaryTree.TraversalType.DEPTH_INORDER);
        System.out.println("---");
        avl.traverse(System.out::println, BinaryTree.TraversalType.DEPTH_POSTORDER);
        System.out.println("---");
        System.out.println(avl.getSize() + " " + avl.getHeight());
    }
}