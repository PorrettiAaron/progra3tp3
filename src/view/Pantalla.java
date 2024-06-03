package view;

import org.openstreetmap.gui.jmapviewer.Coordinate;
import org.openstreetmap.gui.jmapviewer.JMapViewer;
import org.openstreetmap.gui.jmapviewer.interfaces.ICoordinate;
import org.openstreetmap.gui.jmapviewer.MapPolygonImpl;


import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.lang.Integer;
import java.lang.NumberFormatException;

import java.io.Serializable;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;

import model.Grafo;

public class Pantalla implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private JFrame frame;
	private JMapViewer mapViewer;
	private Grafo g;
	private Map<Integer, Map<String, Double>> points;
	private List<MapPolygonImpl> mapLines = new ArrayList<>();
	
	private Integer contVertices;

	/**
	 * @wbp.parser.entryPoint
	 */
    public Pantalla() {
        g = new Grafo();
        points = new HashMap<>();
        frame = new JFrame("Clicke Maximo");
        mapViewer = new JMapViewer();
        mapViewer.setZoomControlsVisible(false);
        contVertices = 0;
		mostrarMensajeDeBienvenida();
    }

	private void initialize() {
		
		inicializarFrame();
		dibujarAristas();
		
		// ----------- Agregar botones y mapa --------------
		
		JPanel mapPanel = new JPanel(new BorderLayout());
		
		mapViewer.setDisplayPosition(new Coordinate(-30.0, 3.05), mapViewer.getTileController().getTileSource().getMaxZoom());

		mapPanel.add(mapViewer, BorderLayout.CENTER);

		JPanel buttonPanel = new JPanel();

		JButton newAristaButton = new JButton("Agregar Arco");
		buttonPanel.add(newAristaButton);

		JButton deleteAristaButton = new JButton("Eliminar Arco");
		buttonPanel.add(deleteAristaButton);

		JButton btnClique = new JButton("Crear Clique Maximo");
		buttonPanel.add(btnClique);

		JButton btnVerGrafo = new JButton("Ver Nodos");
		buttonPanel.add(btnVerGrafo);

		mapViewer.addMouseListener(new MouseAdapter() {
		    @Override
		    public void mouseWheelMoved(MouseWheelEvent e) {
		        e.consume();
		    }
		});
		
		// ----------- Manejo de clicks en el mapa para agregar vertices --------------
		
		
		mapViewer.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				JDialog dialog = new JDialog(frame, "Agregue su Nodo", true);
				dialog.getContentPane().setLayout(new GridLayout(3, 2));
				
				JTextField pesoField = new JTextField();
				dialog.getContentPane().add(new JLabel("Peso:"));
				dialog.getContentPane().add(pesoField);
				
				JButton confirmButton = new JButton("Confirmar");
				
				
				confirmButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent a) {
						
						try {
							
							String textoPeso = pesoField.getText();
							Double peso = Double.parseDouble(textoPeso);
							
							Integer number = incrementarContadorVerticesYDevolverValor();
							
							g.addVertice(number, peso);
							agregarPuntoAlMapa(e, number);
							
							dialog.dispose();
						} 
						
						catch (NumberFormatException ex) {
							JOptionPane.showMessageDialog(frame, "Por favor ingrese un valor numérico para el peso.");
						} 
						
						catch (Exception ex) {
							JOptionPane.showMessageDialog(frame, ex.getMessage());
						}
					}


				});
				
				dialog.getContentPane().add(confirmButton);
				dialog.setSize(300, 150);
				dialog.setLocationRelativeTo(frame);
				dialog.setVisible(true);
	
			}

		});
		
		
		// ----------- Manejo de boton para agregar arcos --------------

		newAristaButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				
				// ----------- Agregar botones a la ventana de dialogo --------------	
				
				JDialog dialog = new JDialog(frame, "Seleccionar vertices", true);
				dialog.getContentPane().setLayout(new GridLayout(4, 2));
				
				List<Integer> pointNames = new ArrayList<>(points.keySet());
				
				JComboBox<Integer> pointComboBox1 = new JComboBox<>(pointNames.toArray(new Integer[0]));
				dialog.getContentPane().add(new JLabel("Vertice 1:"));
				dialog.getContentPane().add(pointComboBox1);
				
				JComboBox<Integer> pointComboBox2 = new JComboBox<>(pointNames.toArray(new Integer[0]));
				dialog.getContentPane().add(new JLabel("Vertice 2:"));
				dialog.getContentPane().add(pointComboBox2);
				
				JButton confirmButton = new JButton("Confirmar");
				
				
				// ----------- Funcionalidad de agregar la arista  --------------
				
				confirmButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						
						try {
							agregarAristaAlGrafoYMapa(pointComboBox1, pointComboBox2);
							dialog.dispose();
						} 
						
						catch (Exception ex) {
							JOptionPane.showMessageDialog(frame, ex.getMessage());
						}
					}
				});
				
				dialog.getContentPane().add(confirmButton);
				dialog.setSize(300, 150);
				dialog.setLocationRelativeTo(frame);
				dialog.setVisible(true);
			}
		});

		
		// ----------- Manejo de boton para eliminar indice de similaridad --------------
		
		deleteAristaButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				// ----------- Agregar botones a la ventana de dialogo --------------	
				
				JDialog dialog = new JDialog(frame, "Seleccione su punto", true);
				dialog.getContentPane().setLayout(new GridLayout(4, 2));
				List<Integer> pointNames = new ArrayList<>(points.keySet());
				
				JComboBox<Integer> pointComboBox1 = new JComboBox<>(pointNames.toArray(new Integer[0]));
				dialog.getContentPane().add(new JLabel("Punto 1:"));
				dialog.getContentPane().add(pointComboBox1);
				
				JComboBox<Integer> pointComboBox2 = new JComboBox<>(pointNames.toArray(new Integer[0]));
				dialog.getContentPane().add(new JLabel("Punto 2:"));
				dialog.getContentPane().add(pointComboBox2);
				
				JButton confirmButton = new JButton("Confirmar");
				
				
				// ----------- Funcionalidad de eliminar la arista  --------------
				
				confirmButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						
						try {
							eliminarAristaDelGrafoYMapa(pointComboBox1, pointComboBox2);
							dialog.dispose();
						} 
						
						catch (Exception ex) {
							JOptionPane.showMessageDialog(frame, ex.getMessage());
						}
					}
				});
				
				dialog.getContentPane().add(confirmButton);
				dialog.setSize(300, 150);
				dialog.setLocationRelativeTo(frame);
				dialog.setVisible(true);
			}
		});

		
		// ----------- Manejo de boton para crear regiones --------------
		
		btnClique.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				g.obtenerCliqueMaximaPorPeso();
				
				List<Entry<Integer, Double>>  verticesOrdenados = g.getVerticesOrdenadosPorPeso();
				Set<Integer> clique = g.getClique();
				double pesoMaxObtenido = g.getPesoMaximoObtenido();
				
				mostrarCliqueEnVentana(verticesOrdenados, clique, pesoMaxObtenido);
				
			}
		});

		
		// ----------- Manejo de boton para ver aristas y pesos  --------------
		
		btnVerGrafo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Map<Integer, Double> vertices = g.getVertices();
				mostrarGrafoEnVentana(vertices);
			}

		});

		
		frame.getContentPane().add(mapPanel, BorderLayout.CENTER);
		frame.getContentPane().add(buttonPanel, BorderLayout.SOUTH);
		frame.setVisible(true);
	}
	
    private static void mostrarGrafoEnVentana(Map<Integer, Double> vertices) {
        JFrame frame = new JFrame("Nodos del Grafo");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(400, 300);
        
        String[] columnNames = {"Nodo", "Peso"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(model);

        for (Map.Entry<Integer, Double> entry : vertices.entrySet()) {
            Integer nodo = entry.getKey();
            Double peso = entry.getValue();
            Object[] row = {nodo, peso};
            model.addRow(row);
        }
        
        JScrollPane scrollPane = new JScrollPane(table);
        frame.add(scrollPane, BorderLayout.CENTER);

        frame.setVisible(true);
    }


    private static void mostrarCliqueEnVentana(List<Entry<Integer, Double>> verticesOrdenados, Set<Integer> clique, double pesoMaxObtenido) {
        JFrame frame = new JFrame("Detalles del Clique Máximo");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(600, 400);


        JPanel verticesPanel = new JPanel(new BorderLayout());
        JLabel verticesLabel = new JLabel("Vértices Ordenados por Peso:");
        verticesLabel.setFont(new Font("Arial", Font.BOLD, 16));

        String[] columnNames = {"Nombre", "Peso"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(model);


        table.setRowHeight(25);
        TableColumnModel columnModel = table.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(100);
        columnModel.getColumn(1).setPreferredWidth(100);


        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));

        for (Entry<Integer, Double> entry : verticesOrdenados) {
            Integer nombre = entry.getKey();
            Double peso = entry.getValue();
            Object[] row = {nombre, peso};
            model.addRow(row);
        }

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(250, 150));
        verticesPanel.add(verticesLabel, BorderLayout.NORTH);
        verticesPanel.add(scrollPane, BorderLayout.CENTER);


        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        JLabel cliqueLabelTitle = new JLabel("Clique Máximo:");
        JLabel cliqueLabel = new JLabel(clique.toString());
        JLabel pesoMaxLabelTitle = new JLabel("Peso Máximo Obtenido:");
        JLabel pesoMaxLabel = new JLabel(String.valueOf(pesoMaxObtenido));


        cliqueLabelTitle.setFont(new Font("Arial", Font.BOLD, 16));
        cliqueLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        pesoMaxLabelTitle.setFont(new Font("Arial", Font.BOLD, 16));
        pesoMaxLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        infoPanel.add(cliqueLabelTitle);
        infoPanel.add(cliqueLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 10))); 
        infoPanel.add(pesoMaxLabelTitle);
        infoPanel.add(pesoMaxLabel);


        frame.setLayout(new BorderLayout());
        frame.add(verticesPanel, BorderLayout.CENTER);
        frame.add(infoPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }
    

	private void inicializarFrame() {
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(800, 600);
		frame.setLocationRelativeTo(null);
		
		JPanel titlePanel = new JPanel();
		JLabel titleLabel = new JLabel("Agregue sus nodos... ");
		titleLabel.setFont(new Font("Times New Roman", Font.PLAIN, 32));
		titlePanel.add(titleLabel);		
		frame.getContentPane().add(titlePanel, BorderLayout.NORTH);

	}

	
	private void mostrarMensajeDeBienvenida() {
		String mensajeBienvenida = "Bienvenido al programa para generar una clique maxima.\n\n" +
                "Para marcar un nuevo nodo, simplemente haz clic en la pantalla.\n" +
                "Luego, se te pedirá que ingreses un peso para el nodo.\n" +
                "Puedes marcar varios nodos de esta manera.\n\n"+
                "Haz clic en \"Ok\" para empezar.";

        JTextArea textArea = new JTextArea(mensajeBienvenida);
        textArea.setEditable(false);
        textArea.setLineWrap(false);
        textArea.setWrapStyleWord(true);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JScrollPane(textArea), BorderLayout.CENTER);

        JButton valoresPorDefectoButton = new JButton("Usar valores por defecto");
        valoresPorDefectoButton.addActionListener(e -> {
            leerSerializacion();
            dibujarAristas();
            initialize();
            
            Window window = SwingUtilities.getWindowAncestor(panel);
            window.dispose();
        });
        panel.add(valoresPorDefectoButton, BorderLayout.SOUTH);

        JButton aceptarButton = new JButton("Aceptar");
        aceptarButton.addActionListener(e -> {
            initialize();
            Window window = SwingUtilities.getWindowAncestor(panel);
            window.dispose();
        });

        JOptionPane optionPane = new JOptionPane(panel, JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, new Object[]{aceptarButton}, aceptarButton);
        JDialog dialog = optionPane.createDialog("Instrucciones");
        dialog.setVisible(true);
	}
	
	
	
	
	private void agregarPuntoAlMapa(MouseEvent e, Integer number) {
		Point point = e.getPoint();
		ICoordinate iCoord = mapViewer.getPosition(point.x, point.y);
		double lat = iCoord.getLat();
        double lon = iCoord.getLon();

        Map<String, Double> coord = new HashMap<>();
        coord.put("lat", lat);
        coord.put("lon", lon);

        points.put(number, coord);
        
        MapMarkerDotWithLabel marker = new MapMarkerDotWithLabel(new Coordinate(lat, lon), String.valueOf(number));
		mapViewer.addMapMarker(marker);
		
	}
	
	
	private void agregarAristaAlGrafoYMapa(JComboBox<Integer> pointComboBox1, JComboBox<Integer> pointComboBox2) {
		Integer punto1 = (Integer) pointComboBox1.getSelectedItem();
		Integer punto2 = (Integer) pointComboBox2.getSelectedItem();
		
		g.addArista(punto1, punto2);
		JOptionPane.showMessageDialog(frame, "Punto 1: " + punto1 + "\n" + "Punto 2: " + punto2);
		dibujarAristas();
	}

	
	private void eliminarAristaDelGrafoYMapa(JComboBox<Integer> pointComboBox1, JComboBox<Integer> pointComboBox2) {
		Integer punto1 = (Integer) pointComboBox1.getSelectedItem();
		Integer punto2 = (Integer) pointComboBox2.getSelectedItem();
		g.deleteArista(punto1, punto2);
		JOptionPane.showMessageDialog(frame, "Se eliminó la arista entre punto 1: " + punto1 + "\n" + " y punto 2: " + punto2);
		dibujarAristas();
	}
	
	
	private void dibujarAristas() {
	    for (MapPolygonImpl line : mapLines) {
	        mapViewer.removeMapPolygon(line);
	    }

	    mapLines.clear();

	    Map<Integer, HashSet<Integer>> aristas = g.getAristas();

	    for (Map.Entry<Integer, HashSet<Integer>> entry : aristas.entrySet()) {
	        Integer punto1Id = entry.getKey();
	        Map<String, Double> coord1 = points.get(punto1Id);
	        if (coord1 != null) {
	            for (Integer punto2Id : entry.getValue()) {
	                Map<String, Double> coord2 = points.get(punto2Id);
	                if (coord2 != null) {
	                    List<Coordinate> coordinates = new ArrayList<>();
	                    coordinates.add(new Coordinate(coord1.get("lat"), coord1.get("lon")));
	                    coordinates.add(new Coordinate(coord2.get("lat"), coord2.get("lon")));
	                    coordinates.add(new Coordinate(coord1.get("lat"), coord1.get("lon")));

	                    MapPolygonImpl line = new MapPolygonImpl(coordinates);
	                    mapViewer.addMapPolygon(line);
	                    mapLines.add(line);
	                }
	            }
	        }
	    }
	}
	
	
	private void dibujarPuntos() {
	    for (Map.Entry<Integer, Map<String, Double>> entry : points.entrySet()) {
	    	Integer number = entry.getKey();
	        Map<String, Double> coord = entry.getValue();
	        double lat = coord.get("lat");
	        double lon = coord.get("lon");
	        
	        MapMarkerDotWithLabel marker = new MapMarkerDotWithLabel(new Coordinate(lat, lon), String.valueOf(number));
	        mapViewer.addMapMarker(marker);
	    }
	}
	
	
	public void leerSerializacion() {
    	Pantalla ms = null;
  	
        try {
        	FileInputStream fis = new FileInputStream("clique.txt");
        	ObjectInputStream in = new ObjectInputStream(fis);
        	ms = (Pantalla) in.readObject();
        	this.g = ms.g;
        	this.points = ms.points;
        	this.dibujarPuntos();
        	in.close();
        } catch (Exception ex) {
            ex.getMessage();
        }
        
    }
    
	
	public void serializar() {
        try {
        	FileOutputStream fos = new FileOutputStream("clique.txt");
        	ObjectOutputStream out = new ObjectOutputStream(fos);
        	out.writeObject(this);
        	out.close();
        } catch (Exception ex) {
            ex.getMessage();
        }
    } 
	
	private int incrementarContadorVerticesYDevolverValor() {
		this.contVertices += 1;
		return this.contVertices;
	}


	public static void main(String[] args) {
		SwingUtilities.invokeLater(Pantalla::new);
	}
}
