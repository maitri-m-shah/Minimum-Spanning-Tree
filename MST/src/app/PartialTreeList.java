package app;
import structures.MinHeap;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

import structures.Arc;
import structures.Graph;
import structures.MinHeap;
import structures.PartialTree;
import structures.Vertex;
import structures.MinHeap;
/**
 * Stores partial trees in a circular linked list
 * 
 */
public class PartialTreeList implements Iterable<PartialTree> {
    
	/**
	 * Inner class - to build the partial tree circular linked list 
	 * 
	 */
	public static class Node {
		/**
		 * Partial tree
		 */
		public PartialTree tree;
		
		/**
		 * Next node in linked list
		 */
		public Node next;
		
		/**
		 * Initializes this node by setting the tree part to the given tree,
		 * and setting next part to null
		 * 
		 * @param tree Partial tree
		 */
		public Node(PartialTree tree) {
			this.tree = tree;
			next = null;
		}
	}

	/**
	 * Pointer to last node of the circular linked list
	 */
	private Node rear;
	
	/**
	 * Number of nodes in the CLL
	 */
	private int size;
	
	/**
	 * Initializes this list to empty
	 */
    public PartialTreeList() {
    	rear = null;
    	size = 0;
    }

    /**
     * Adds a new tree to the end of the list
     * 
     * @param tree Tree to be added to the end of the list
     */
    public void append(PartialTree tree) {
    	Node ptr = new Node(tree);
    	if (rear == null) {
    		ptr.next = ptr;
    	} else {
    		ptr.next = rear.next;
    		rear.next = ptr;
    	}
    	rear = ptr;
    	size++;
    }

    /**
	 * Initializes the algorithm by building single-vertex partial trees
	 * 
	 * @param graph Graph for which the MST is to be found
	 * @return The initial partial tree list
	 */
	public static PartialTreeList initialize(Graph graph) {
		//write out what each step is finna do :))
		//create new partial tree list, everything is set to null/0 
		PartialTreeList ans = new PartialTreeList();
		//traverse through the graph's vertex and add them to the list (eventually)
		for(int i=0; i<graph.vertices.length; i++) {
			
			PartialTree ptree = new PartialTree(graph.vertices[i]);
			MinHeap<Arc> p = ptree.getArcs();
			
			 //iterate through the neighbors
			 while(graph.vertices[i].neighbors!=null) {
				 //Insert all of the arcs (edges) connected to v into P. The lower the weight on an arc, the higher its priority.
				 // Add the partial tree T to the list L.		 
				
				 Arc a = new Arc(graph.vertices[i], graph.vertices[i].neighbors.vertex, graph.vertices[i].neighbors.weight);
				 p.insert(a);
				 graph.vertices[i].neighbors=graph.vertices[i].neighbors.next;
				 
			 }
			 ans.append(ptree);
		}
		
		return ans;
	}
	
	/**
	 * Executes the algorithm on a graph, starting with the initial partial tree list
	 * for that graph
	 * 
	 * @param ptlist Initial partial tree list
	 * @return Array list of all arcs that are in the MST - sequence of arcs is irrelevant
	 */
	public static ArrayList<Arc> execute(PartialTreeList ptlist) {
		ArrayList<Arc> ans = new ArrayList<Arc>();
		if(ptlist==null) {
			return null;
		}
		
		while(ptlist.size>1) {
			//remove the first partial tree, 
			PartialTree temp = ptlist.remove();
			//create a heap 
			MinHeap<Arc> pqx = temp.getArcs(); //.getArcs returns the arc of lowest weight
			Arc x = pqx.deleteMin(); //"Remove the highest-priority arc from PQX"
			
			Vertex ver1 = x.getv2(); //points to second endpoint of arc
			Vertex ver2 = ver1.getRoot(); 
			Vertex ver3 = temp.getRoot();
			
			while(ver2==ver3 && !pqx.isEmpty()) { //while the roots of x.v1Root and x.v2Root are equal
				x = pqx.deleteMin(); //If v2 and v1 belongs to PTX, go back to Step 4 and pick the next highest priority arc
			}
			
			ans.add(x);
			
			if(pqx.isEmpty()){
				break;
			}
			
			PartialTree pty = ptlist.removeTreeContaining(ver1); 
			temp.merge(pty);
			ptlist.append(temp);
					
		}
		
		return ans;
	}
	
    /**
     * Removes the tree that is at the front of the list.
     * 
     * @return The tree that is removed from the front
     * @throws NoSuchElementException If the list is empty
     */
    public PartialTree remove() 
    throws NoSuchElementException {
    			
    	if (rear == null) {
    		throw new NoSuchElementException("list is empty");
    	}
    	PartialTree ret = rear.next.tree;
    	if (rear.next == rear) {
    		rear = null;
    	} else {
    		rear.next = rear.next.next;
    	}
    	size--;
    	return ret;
    		
    }

    /**
     * Removes the tree in this list that contains a given vertex.
     * 
     * @param vertex Vertex whose tree is to be removed
     * @return The tree that is removed
     * @throws NoSuchElementException If there is no matching tree
     */
    public PartialTree removeTreeContaining(Vertex vertex) 
    throws NoSuchElementException {
    	if(rear == null){
			throw new NoSuchElementException("there is nothing in the cll");
		}
    	Node prev= rear;
    	Node ptr= rear.next;
    	if(size==1) {
    		PartialTree temp = rear.tree;
    		if(rear.tree.getRoot().equals(vertex.getRoot())) { //if the last node in the CLL's root is equal to the vertex.getRoot than we have found a match because the size is 1
    			size--;
    			rear=null;
    			return temp;
    		}
    		else {
    			throw new NoSuchElementException("there is only one element and it is not equal to vertex.getRoot() so element is not found");
    		}
    	}
    	
    	do {
    		if(ptr.tree.getRoot().equals(vertex.getRoot())) {
    			prev.next=ptr.next;//remove tree containing vertex
    			size--; //decrement size
    			
    				if(ptr==rear) {
    					rear = prev; //you need to modify the rear since ptr is being deleted 
    				}
    				return ptr.tree;
    		}
    		prev=ptr; //each time you need to move the pointers
    		ptr=ptr.next;
    	}
    	while(prev!=rear); //while prev is not equal to rear
    	
    	throw new NoSuchElementException("not found");
     }
    
    /**
     * Gives the number of trees in this list
     * 
     * @return Number of trees
     */
    public int size() {
    	return size;
    }
    
    /**
     * Returns an Iterator that can be used to step through the trees in this list.
     * The iterator does NOT support remove.
     * 
     * @return Iterator for this list
     */
    public Iterator<PartialTree> iterator() {
    	return new PartialTreeListIterator(this);
    }
    
    private class PartialTreeListIterator implements Iterator<PartialTree> {
    	
    	private PartialTreeList.Node ptr;
    	private int rest;
    	
    	public PartialTreeListIterator(PartialTreeList target) {
    		rest = target.size;
    		ptr = rest > 0 ? target.rear.next : null;
    	}
    	
    	public PartialTree next() 
    	throws NoSuchElementException {
    		if (rest <= 0) {
    			throw new NoSuchElementException();
    		}
    		PartialTree ret = ptr.tree;
    		ptr = ptr.next;
    		rest--;
    		return ret;
    	}
    	
    	public boolean hasNext() {
    		return rest != 0;
    	}
    	
    	public void remove() 
    	throws UnsupportedOperationException {
    		throw new UnsupportedOperationException();
    	}
    	
    }
}

