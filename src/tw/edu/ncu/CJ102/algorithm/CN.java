package tw.edu.ncu.CJ102.algorithm;

import edu.uci.ics.jung.graph.Graph;

/**
 * implement Common Neighbor algorithm
 * @author TingWen
 *
 * @param <V> Node
 * @param <E> Edge
 */
public class CN<V,E> implements LinkPrediction<V, E> {
	
	Graph<V,E> graph;
	/**
	 * input the graph to compute
	 * @param g : graph you want to compute
	 */
	public CN(Graph<V,E> g){
		this.graph = g;
		
	}
	//Should I compute every Node score? or compute as predict demand?
	
	@Override
	public double predict(V target, V goal) {
		double index = 0;
		
		for(V n:this.graph.getNeighbors(target)){
			for(V n2: this.graph.getNeighbors(goal)){
				if(n.equals(n2)){
					index++;
				}
			}
		}
		
		return index;
	}

}
