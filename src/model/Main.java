package model;

public class Main {

	public static void main(String[] args) {
		Grafo grafo = new Grafo();
        
        // Agregar vértices con sus pesos
        grafo.addVertice(1, 3.5);
        grafo.addVertice(2, 2.1);
        grafo.addVertice(3, 4.8);
        grafo.addVertice(4, 1.9);
        grafo.addVertice(5, 5.0);
        
        // Agregar aristas
        grafo.addArista(1, 2);
        grafo.addArista(1, 3);
        grafo.addArista(2, 3);
        grafo.addArista(2, 4);
        grafo.addArista(3, 4);
        grafo.addArista(3, 5);
        grafo.addArista(1, 5);
        
        grafo.obtenerCliqueMaximaPorPeso();
        grafo.printGrafo();
	}
}
