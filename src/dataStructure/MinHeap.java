package dataStructure;

import java.util.ArrayList;
import java.util.List;

import elements.Edge;
import elements.Fruit;

/**
 * MinHeap Data Structure: 
 * 
 * this data structure will contain node obj.
 * and sort them by the node and the fruits weight. 
 * 
 * will support the MinHeap methods: 
 * push , pop , peek .
 * uses heapyup & heapyfudown for rearranging positions in the tree. 
 * 
 */

public class MinHeap{


	/**
	 * node class contains the fields: 
	 * 
	 * Double getPathDist - will hold the path valued
	 * Array of Objects   - the path itself 
	 * Fruit 			  - The fruit which the path is set to. 
	 */
	public class node{
		private double getPathDist;
		private Object [] path;	
		private Fruit fruit;
		/**
		 * Constructor
		 */
		public node(Object [] path, Fruit fruit) {
			this.getPathDist = (double) path[0];
			this.path = path;	
			this.fruit = fruit;
		}
		/**
		 * returns the path valued.
		 * @return path value in Double. 
		 */
		public double getPathDist() {
			return (double) path[0];
		}
		/**
		 * @return the path itself in edges 
		 */
		public List<Edge> getPath() {
			return (List<Edge>) path[1];
		}
		/**
		 * @return the fruit the path sets to. 
		 */
		public Fruit getFruit() {
			return fruit;
		}

	}


	//will holds the path. 
	private ArrayList<node> heap;

	public MinHeap () {
		heap = new ArrayList<node>();
	}

	public MinHeap (MinHeap a) {
		heap = new ArrayList<node>();
		ArrayList<node> temp = a.getList();
		for (int i = 0; i < temp.size(); i++) {
			heap.add(temp.get(i));
		}
	}
	/**
	 * @return the heap containing the paths.  
	 */
	private ArrayList<node> getList() {
		return heap;
	}

	public void add(Object [] path, Fruit fruit) {

		node new_e = new node(path,fruit);
		add_heapfyup(new_e);
	}
	/**
	 * @param new_e adding vertex to the heap.
	 */
	private void add_heapfyup(node new_e) {

		heap.add(new_e);
		int index = heap.size()-1;

		while(index > 0) {
			int parent = (index-1)/2;

			if(heap.get(index).getPathDist() < heap.get(parent).getPathDist()) {
				node temp = heap.get(parent);
				heap.set(parent, heap.get(index));
				heap.set(index, temp);
				index = parent;
			}
			else {
				return;
			}
		}

	}
	/**
	 * @return the top shortest path in the heap. 
	 */
	public node pop() {

		node temp = heap.get(0);
		heap.set(0, heap.get(heap.size()-1));
		heap.set(heap.size()-1, temp);
		node pop = heap.remove(heap.size()-1);
		heapfy_down();
		return pop;
	}
	/**
	 * simple nodes swap method. 
	 */
	private void swap(int a ,int b) {
		node temp = heap.get(a);
		heap.set(a, heap.get(b));
		heap.set(b, temp);
	}
	/**
	 * heapify down method. 
	 * rearranging the heap. 
	 */
	private void heapfy_down() {

		int right 		=2;
		int left 		=1;
		int movingindex =0;
		int end 		=heap.size()-1; 


		while(movingindex != end && right < end && left < end) {

			if(heap.get(movingindex).getPathDist() > heap.get(left).getPathDist())
			{
				if(heap.get(left).getPathDist() < heap.get(right).getPathDist())
				{
					swap(movingindex,left);
					movingindex = left; 
					left = (movingindex*2)+1;
					right= (movingindex*2)+2;
				}
				else if(heap.get(right).getPathDist() < heap.get(left).getPathDist())
				{
					swap(movingindex,right);
					movingindex = right; 
					left = (movingindex*2)+1;
					right= (movingindex*2)+2;
				}
				else 
				{
					swap(movingindex,left);
					movingindex = left; 
					left = (movingindex*2)+1;
					right= (movingindex*2)+2;
				}

			} else return; 
		}

		if(left<end) 
		{
			if(heap.get(left).getPathDist() < heap.get(movingindex).getPathDist())
			{swap(movingindex,left);}
		}
	}
	/**
	 * prints the heap variables. 
	 */
	public String toString() {
		String print = "[";
		for (int i = 0; i <heap.size(); i++) {
			print+=heap.get(i).path;
			if(i != heap.size()-1) {
				print+=",";
			}
		}
		print+="]";
		return print;
	}
	/**
	 * @return boolean indicates if the heap is empty or not.
	 */
	public boolean isEmpty() {
		return heap.isEmpty();
	}
	/**
	 * @return a peek to the top element in the heap. 
	 */
	public node peek() {
		return heap.get(0);
	}
	/**
	 * @return the fruit of the path in the top path in the heap.
	 */
	public Fruit fruitpeek() {
		return heap.get(0).getFruit();
	}
	
	/**
	 * @return path length (value);
	 */
	public double lenPeek() {
		return heap.get(0).getPathDist();
	}
}