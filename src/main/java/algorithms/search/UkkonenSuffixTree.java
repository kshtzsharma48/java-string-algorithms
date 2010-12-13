package algorithms.search;

import com.google.common.collect.Maps;

import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A Java implementation of Ukkonen's algorithm for building a Suffix Tree.
 * <p/>
 * Adapted from source at <a href="http://illya-keeplearning.blogspot.com/2009/04/suffix-trees-java-ukkonens-algorithm.html">
 * http://illya-keeplearning.blogspot.com/2009/04/suffix-trees-java-ukkonens-algorithm.html</a>.
 */
public class UkkonenSuffixTree {

    public static UkkonenSuffixTree forString(CharSequence text) {
        return new UkkonenSuffixTree(text);
    }

    private final CharSequence text;
    private final Node root;

    private UkkonenSuffixTree(CharSequence text) {
        this.text = checkNotNull(text);
        this.root = new Node(this);

        Suffix active = new Suffix(root, 0, -1);
        for (int i = 0; i < text.length(); i++) {
            addPrefix(active, i);
        }
    }

    public int indexOf(CharSequence str) {
        checkNotNull(str);

        if (str.length() == 0) {
            return 0;
        } else if (str.length() > text.length()) {
            return -1;
        }

        int index = -1;
        Node node = root;

        for (int i = 0; i < str.length();) {
            if ((node == null) || (i == text.length())) {
                return -1;
            }

            Edge edge = node.findEdge(str.charAt(i));
            if (edge == null) {
                return -1;
            }

            index = edge.getBeginIndex() - i;
            i++;

            for (int j = edge.getBeginIndex() + 1; j <= edge.getEndIndex(); j++, i++) {
                if (i == str.length()) {
                    break;
                }
                if (text.charAt(j) != str.charAt(i)) {
                    return -1;
                }
            }
            node = edge.getEndNode();
        }
        return index;
    }

    private void addPrefix(Suffix active, int endIndex) {
        Node lastParentNode = null;
        Node parentNode;

        while (true) {
            Edge edge;
            parentNode = active.getOriginNode();

            if (active.isExplicit()) {
                edge = active.getOriginNode().findEdge(text.charAt(endIndex));
                if (edge != null) {
                    break;
                }
            } else {
                edge = active.getOriginNode().findEdge(text.charAt(active.getBeginIndex()));
                int span = active.getSpan();
                if (text.charAt(edge.getBeginIndex() + span + 1) == text.charAt(endIndex)) {
                    break;
                }
                parentNode = edge.splitEdge(active);
            }

            Edge newEdge = new Edge(endIndex, text.length() - 1, parentNode);
            newEdge.insert();
            updateSuffixNode(lastParentNode, parentNode);
            lastParentNode = parentNode;

            if (active.getOriginNode() == root) {
                active.incrementBeginIndex();
            } else {
                active.changeOriginNode();
            }
            active.canonize();
        }
        updateSuffixNode(lastParentNode, parentNode);
        active.incrementEndIndex();
        active.canonize();
    }

    private void updateSuffixNode(Node node, Node suffixNode) {
        if ((node != null) && (node != root)) {
            node.setSuffixNode(suffixNode);
        }
    }

    private CharSequence getText() {
        return text;
    }

    private static class Suffix {
        private Node originNode;
        private int beginIndex;
        private int endIndex;

        private Suffix(Node originNode, int beginIndex, int endIndex) {
            this.originNode = originNode;
            this.beginIndex = beginIndex;
            this.endIndex = endIndex;
        }

        private boolean isExplicit() {
            return beginIndex > endIndex;
        }

        private void canonize() {
            if (!isExplicit()) {
                Edge edge = originNode.findEdge(originNode.charAt(beginIndex));

                int edgeSpan = edge.getSpan();
                while (edgeSpan <= getSpan()) {
                    beginIndex += edgeSpan + 1;
                    originNode = edge.getEndNode();
                    if (beginIndex <= endIndex) {
                        edge = edge.getEndNode().findEdge(originNode.charAt(beginIndex));
                        edgeSpan = edge.getSpan();
                    }
                }
            }
        }

        private int getSpan() {
            return endIndex - beginIndex;
        }

        private Node getOriginNode() {
            return originNode;
        }

        private int getBeginIndex() {
            return beginIndex;
        }

        private void incrementBeginIndex() {
            beginIndex++;
        }

        private void changeOriginNode() {
            originNode = originNode.getSuffixNode();
        }

        private void incrementEndIndex() {
            endIndex++;
        }
    }

    private static class Edge {
        private int beginIndex;
        private int endIndex;
        private Node startNode;
        private Node endNode;

        private Edge(int beginIndex, int endIndex, Node startNode) {
            this.beginIndex = beginIndex;
            this.endIndex = endIndex;
            this.startNode = startNode;
            this.endNode = new Node(startNode, null);
        }

        private Node splitEdge(Suffix suffix) {
            remove();
            Edge newEdge = new Edge(beginIndex, beginIndex + suffix.getSpan(), suffix.getOriginNode());
            newEdge.insert();
            newEdge.endNode.setSuffixNode(suffix.getOriginNode());
            beginIndex += suffix.getSpan() + 1;
            startNode = newEdge.getEndNode();
            insert();
            return newEdge.getEndNode();
        }

        private void insert() {
            startNode.addEdge(beginIndex, this);
        }

        private void remove() {
            startNode.removeEdge(beginIndex);
        }

        private int getSpan() {
            return endIndex - beginIndex;
        }

        private int getBeginIndex() {
            return beginIndex;
        }

        private int getEndIndex() {
            return endIndex;
        }

        private Node getEndNode() {
            return endNode;
        }
    }

    private static class Node {

        private UkkonenSuffixTree suffixTree;

        private Node suffixNode;
        private final Map<Character, Edge> edges = Maps.newTreeMap();

        private Node(Node node, Node suffixNode) {
            this.suffixTree = node.suffixTree;
            this.suffixNode = suffixNode;
        }

        private Node(UkkonenSuffixTree suffixTree) {
            this.suffixTree = suffixTree;
            this.suffixNode = null;
        }

        private Character charAt(int index) {
            //noinspection UnnecessaryBoxing
            return Character.valueOf(suffixTree.getText().charAt(index));
        }

        private void addEdge(int charIndex, Edge edge) {
            edges.put(charAt(charIndex), edge);
        }

        private void removeEdge(int charIndex) {
            edges.remove(charAt(charIndex));
        }

        private Edge findEdge(char ch) {
            //noinspection UnnecessaryBoxing
            return edges.get(Character.valueOf(ch));
        }

        private Node getSuffixNode() {
            return suffixNode;
        }

        private void setSuffixNode(Node suffixNode) {
            this.suffixNode = suffixNode;
        }
    }
}
