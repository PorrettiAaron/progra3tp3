package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Grafo {

	private Map<Integer, Double> vertices;
	private Map<Integer, HashSet<Integer>> aristas;
	private List<Map.Entry<Integer, Double>> verticesOrdenadosPorPeso;
	private Set<Integer> clique;

	public Grafo() {
		vertices = new HashMap<Integer, Double>();
		aristas = new HashMap<Integer, HashSet<Integer>>();
		clique = new HashSet<Integer>();
	}

	public void addVertice(Integer id, Double peso) {
		if (!vertices.containsKey(id)) {
			vertices.put(id, peso);
			aristas.put(id, new HashSet<Integer>());
		} else {
			System.out.println("El Vertice ya existe");
		}
	}

	public void addArista(Integer idUno, Integer idOtro) {
		if (vertices.containsKey(idUno) && vertices.containsKey(idOtro)) {
			aristas.get(idUno).add(idOtro);
			aristas.get(idOtro).add(idUno);
		} else {
			System.out.println("Uno de los vertices no existe en el grafo");
		}
	}
	
	public void deleteArista(Integer idUno, Integer idOtro) {
		return;
	}

	public Map<Integer, Double> getVertices() {
		return vertices;
	}

	public void setVertices(Map<Integer, Double> vertices) {
		this.vertices = vertices;
	}

	public Map<Integer, HashSet<Integer>> getAristas() {
		return aristas;
	}

	public void setAristas(Map<Integer, HashSet<Integer>> aristas) {
		this.aristas = aristas;
	}

	public List<Map.Entry<Integer, Double>> getVerticesOrdenadosPorPeso() {
		return verticesOrdenadosPorPeso;
	}

	public void setVerticesOrdenadosPorPeso(List<Map.Entry<Integer, Double>> verticesOrdenadosPorPeso) {
		this.verticesOrdenadosPorPeso = verticesOrdenadosPorPeso;
	}

	public Set<Integer> getClique() {
		return clique;
	}

	public void setClique(Set<Integer> clique) {
		this.clique = clique;
	}

	public void obtenerCliqueMaximaPorPeso() {
		ordenarVertices();
		clique.clear();
		for (Map.Entry<Integer, Double> entry : verticesOrdenadosPorPeso) {
			int vertice = entry.getKey();
			if (puedeAgregarAlaClique(vertice)) {
				clique.add(vertice);
			}
		}
	}

	private void ordenarVertices() {
		verticesOrdenadosPorPeso = new ArrayList<>(vertices.entrySet());
		verticesOrdenadosPorPeso.sort((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()));
	}

	private boolean puedeAgregarAlaClique(int vertice) {
		for (int v : clique) {
			if (!aristas.get(v).contains(vertice)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Para prueba de consola
	 */
	public void printGrafo() {
		for (Integer id : vertices.keySet()) {
			System.out.print("Vertice: " + id + " Aristas -> ");
			for (Integer idA : aristas.get(id)) {
				System.out.print(idA + " ");
			}
			System.out.println();
		}
		System.out.println(verticesOrdenadosPorPeso);
		System.out.println(clique);
	}
}
