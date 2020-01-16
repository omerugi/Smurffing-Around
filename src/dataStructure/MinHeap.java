package dataStructure;

import java.util.ArrayList;
import java.util.List;

import elements.Edge;
import elements.Fruit;


public class MinHeap{

	
	public class node{
		double getPathDist;
		Object [] path;	
		Fruit fruit;
		
		public node(Object [] path, Fruit fruit) {
			this.getPathDist = (double) path[0];
			this.path = path;	
			this.fruit = fruit;
		}

		public double getPathDist() {
			return (double) path[0];
		}

		public List<Edge> getPath() {
			return (List<Edge>) path[1];
		}
		
		public Fruit getFruit() {
			return fruit;
		}

	}
	
	ArrayList<node> heap;

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

	private ArrayList<node> getList() {
		return heap;
	}

	public void add(Object [] path, Fruit fruit) {

		node new_e = new node(path,fruit);
		add_heapfyup(new_e);
	}

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

	public node pop() {

		node temp = heap.get(0);
		heap.set(0, heap.get(heap.size()-1));
		heap.set(heap.size()-1, temp);
		node pop = heap.remove(heap.size()-1);
		heapfy_down();
		return pop;
	}

	private void swap(int a ,int b) {
		node temp = heap.get(a);
		heap.set(a, heap.get(b));
		heap.set(b, temp);
	}

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

	public boolean isEmpty() {
		return heap.isEmpty();
	}

	public node peek() {
		return heap.get(0);
	}
	
	public Fruit fruitpeek() {
		return heap.get(0).getFruit();
	}
	
	public double lenPeek() {
		return heap.get(0).getPathDist();
	}
}