package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class Grafo implements Serializable {

	private static final long serialVersionUID = 1L;
	private Map<Integer, Double> vertices;
	private Map<Integer, HashSet<Integer>> aristas;
	private List<Map.Entry<Integer, Double>> verticesOrdenadosPorPeso;
	private Set<Integer> clique;

	private double pesoMaximoObtenido;

	public Grafo() {
		vertices = new HashMap<Integer, Double>();
		aristas = new HashMap<Integer, HashSet<Integer>>();
		clique = new HashSet<Integer>();
	}

	public void addVertice(Integer id, Double peso) {
		validarVerticeAceptable(id);
		validarQueNoExistaVertice(id);
		validarPesoPositivo(peso);
		vertices.put(id, peso);
		aristas.put(id, new HashSet<Integer>());
	}

	public void addArista(Integer idUno, Integer idOtro) {
		validarVerticesDistintos(idUno, idOtro);
		validarQueExistaVertice(idUno);
		validarQueExistaVertice(idOtro);
		aristas.get(idUno).add(idOtro);
		aristas.get(idOtro).add(idUno);
	}

	public void deleteArista(Integer idUno, Integer idOtro) {
		validarVerticesDistintos(idUno, idOtro);
		validarQueExistaArista(idUno, idOtro);
		aristas.get(idUno).remove(idOtro);
		aristas.get(idOtro).remove(idUno);
	}

	public void obtenerCliqueMaximaPorPeso() {
		ordenarVertices();
        pesoMaximoObtenido = verticesOrdenadosPorPeso.stream()
        		.filter(entry -> puedeAgregarAlaClique(entry.getKey()))
        		.map(Map.Entry::getValue)
                .reduce(0.0, Double::sum);
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
		clique.add(vertice);
		return true;
	}
	
	public Map<Integer, HashSet<Integer>> getAristas() {
		return aristas;
	}

	public Map<Integer, Double> getVertices() {
		return vertices;
	}
	
	public List<Entry<Integer, Double>> getVerticesOrdenadosPorPeso() {
		return verticesOrdenadosPorPeso;
	}
	
	public Set<Integer> getClique() {
		return clique;
	}
	

	public double getPesoMaximoObtenido() {
		return pesoMaximoObtenido;
	}

	/**
	 * Para prueba de consola
	 */
	public void printGrafo() {
		System.out.println("Grafo:");
		System.out.println("------");
		for (Integer id : vertices.keySet()) {
			System.out.print("Vertice: " + id + " Aristas -> ");
			for (Integer idA : aristas.get(id)) {
				System.out.print(idA + " ");
			}
			System.out.println();
		}
		System.out.println("Los vertices ordenados por peso -> "+ verticesOrdenadosPorPeso);
		System.out.println("Los vertices de la clique -> " + clique);
		System.out.println("El peso de la clique -> " + pesoMaximoObtenido);
	}

	/**
	 * Validaciones
	 */

	private void validarVerticesDistintos(Integer idUno, Integer idOtro) {
		if (idUno == idOtro)
			throw new IllegalArgumentException("Los vertices deben ser distintos");
	}

	private void validarVerticeAceptable(Integer vertice) {
		if (vertice == null)
			throw new IllegalArgumentException("El Id del Vertice no puede se NULL");
		if (vertice <= 0)
			throw new IllegalArgumentException("El Id del Vertice debe ser un entero positivo");
	}

	private void validarQueExistaVertice(Integer vertice) {
		if (!vertices.containsKey(vertice))
			throw new IllegalArgumentException("El Vertice " + vertice + " no existe en el grafo");
	}

	public void validarQueNoExistaVertice(Integer vertice) {
		if (vertices.containsKey(vertice)) {
			double peso = vertices.get(vertice);
			throw new IllegalArgumentException("El Vertice " + vertice + " existe en el grafo con peso: " + peso);
		}
	}

	private void validarPesoPositivo(double peso) {
		if (peso <= 0)
			throw new IllegalArgumentException("El peso del vertice debe ser positivo");
	}

	private void validarQueExistaArista(Integer idUno, Integer idOtro) {
		if (!aristas.get(idUno).contains(idOtro))
			throw new IllegalArgumentException("No existe arista entre " + idUno + " y " + idOtro);
	}

}
