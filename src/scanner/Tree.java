package scanner;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Represents a tree node. Each node stores a value and a map of subtrees.
 * @param <E> the type of the value stored in each node
 * @author Agastya Ravuri
 * @version 1.26.2024
 */
public class Tree<E>
{
    E value;
    Map<E, Tree<E>> subtrees;

    /**
     * Constructor for a tree.
     * @param value the value stored in the tree
     * @param subtrees a set of all subtrees of the tree
     */
    public Tree(E value, Set<Tree<E>> subtrees)
    {
        this.value = value;
        this.subtrees = subtrees == null ? null :
                                subtrees
                                .stream()
                                .collect(Collectors.toMap(Tree::getValue, Function.identity()));
    }

    /**
     * Gets the value stored in the tree.
     * @return the value in the tree.
     */
    public E getValue()
    {
        return value;
    }

    /**
     * Gets the direct subtrees of the tree.
     * @return a map, from the value in a subtree to the subtree itself.
     */
    public Map<E, Tree<E>> getSubtrees()
    {
        return subtrees;
    }

    /**
     * Checks if a value exists in the direct subtree nodes.
     *
     * @param val the value.
     * @return the value in the tree
     */
    public boolean contains(E val)
    {
        return subtrees.containsKey(val);
    }

    /**
     * Gets the subtree with value val.
     *
     * @param val the value to search for.
     * @return the subtree.
     */
    public Tree<E> get(E val)
    {
        return subtrees.get(val);
    }

    /**
     * Adds a subtree to the tree.
     * Replaces the tree if there is already a subtree with value subtree.value.
     *
     * @param subtree the subtree to add.
     */
    public void add(Tree<E> subtree)
    {
        subtrees.put(subtree.value, subtree);
    }

    /**
     * Returns a leaf node with a value.
     * @param value the value to put in this tree.
     * @return a tree with no subtrees.
     * @param <E> the type of the tree.
     */
    public static <E> Tree<E> of(E value)
    {
        return new Tree<>(value, null);
    }

    /**
     * Converts a tree to a string in the format
     * "value": {
     *     subtree #1,
     *     subtree #1,
     *     etc.
     * }
     */
    @Override
    public String toString()
    {
        String ret = "\"" + value + "\": {\n\t";
        if (subtrees != null)
        {
            for (Tree<E> e : subtrees.values())
            {
                ret += e == null ? "" : e.toString().replace("\n", "\n\t");
            }
        }
        ret += "}\n";
        return ret;
    }

}
