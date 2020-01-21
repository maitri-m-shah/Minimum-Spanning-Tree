package structures;

public class PartialTree {
  
	/**
	 * The root of the partial tree
	 */
	private Vertex root;
    
	/**
	 * The arcs included in this partial tree
	 */
	private MinHeap<Arc> arcs;

	/**
	 * Initializes this partial tree with given vertex
	 * 
	 * @param vertex Vertex used to initialize the tree
	 */
    public PartialTree(Vertex vertex) {
    	root = vertex;
    	arcs = new MinHeap<Arc>();
    }

    /**
     * Merges another partial tree into this partial tree
     * 
     * @param other The partial tree to be merged with this tree.
     */
    public void merge(PartialTree other)	{
    	other.root.parent = root;
    	arcs.merge(other.arcs);
    }
    
    /**
     * Returns the root of this tree.
     * 
     * @return Root of tree
     */
    public Vertex getRoot() {
    	return root;
    }
    
    /**
     * Returns the priority-ordered arc set of this tree. The lower the weight of an arc,
     * the higher its priority.
     * 
     * @return Priority-ordered arc set.
     */
    public MinHeap<Arc> getArcs() {
    	return arcs;
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString() {
    	String ret = "Vertices: " + root.toString();
    	ret += "  PQ: " + arcs;
    	return ret;
    }
}
