package model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;


public class Grafo implements Serializable {

	private static final long serialVersionUID = 1L;
	private Map<Integer, Double> vertices;
	private Map<Integer, HashSet<Integer>> aristas;

	public Grafo() {
		vertices = new HashMap<Integer, Double>();
		aristas = new HashMap<Integer, HashSet<Integer>>();
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
	
	public Map<Integer, HashSet<Integer>> getAristas() {
		return aristas;
	}

	public Map<Integer, Double> getVertices() {
		return vertices;
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
