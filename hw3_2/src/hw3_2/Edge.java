package hw3_2;

public class Edge implements Comparable<Edge> {

	int index;
	int d;
	
	public Edge(int index, int d){
		this.index = index;
		this.d = d;
	}

	@Override
	public int compareTo(Edge o) {	
		if(this.d > o.d){
			return 1;
		} else if(this.d <o.d){
			return -1;
		} else {
			return 0;
		}
	}
}
