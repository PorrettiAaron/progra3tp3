package model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class Solver {

	private List<Map.Entry<Integer, Double>> verticesOrdenadosPorPeso;
	private double pesoMaximoObtenido;
	private Set<Integer> clique;
	private Grafo grafo;

	public Solver() {
		clique = new HashSet<Integer>();
	}
	
	public void resolverCliqueMaximaPorPeso(Grafo grafo) {
		this.grafo = grafo;
		clique.clear();
		ordenarVertices();
        pesoMaximoObtenido = verticesOrdenadosPorPeso.stream()
        		.filter(entry -> puedeAgregarAlaClique(entry.getKey()))
        		.map(Map.Entry::getValue)
                .reduce(0.0, Double::sum);
	}

	private void ordenarVertices() {
		verticesOrdenadosPorPeso = new ArrayList<>(grafo.getVertices().entrySet());
		verticesOrdenadosPorPeso.sort((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()));
	}

	private boolean puedeAgregarAlaClique(int vertice) {
		for (int v : clique) {
			if (!grafo.getAristas().get(v).contains(vertice)) {
				return false;
			}
		}
		clique.add(vertice);
		return true;
	}
	

	public double getPesoMaximoObtenido() {
		return pesoMaximoObtenido;
	}
	
	public List<Entry<Integer, Double>> getVerticesOrdenadosPorPeso() {
		return verticesOrdenadosPorPeso;
	}

	public Set<Integer> getClique() {
		return clique;
	}

}
