package view;

import org.openstreetmap.gui.jmapviewer.Coordinate;
import org.openstreetmap.gui.jmapviewer.JMapViewer;
import org.openstreetmap.gui.jmapviewer.interfaces.ICoordinate;
import org.openstreetmap.gui.jmapviewer.MapPolygonImpl;


import javax.swing.*;
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
import java.lang.Integer;
import java.lang.NumberFormatException;

import java.io.Serializable;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;

import model.Grafo;

public class Mapa implements Serializable {
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
    public Mapa() {
        g = new Grafo();
        points = new HashMap<>();
        frame = new JFrame("Creando Regiones");
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
		
		mapViewer.setDisplayPosition(new Coordinate(0.0, -30.0), mapViewer.getTileController().getTileSource().getMaxZoom());

		mapPanel.add(mapViewer, BorderLayout.CENTER);

		JPanel buttonPanel = new JPanel();

		JButton newAristaButton = new JButton("Agregar arista");
		buttonPanel.add(newAristaButton);

		JButton btnCrearRegiones = new JButton("Crear regiones");
		buttonPanel.add(btnCrearRegiones);

		JButton btnVerGrafo = new JButton("Ver Grafo");
		buttonPanel.add(btnVerGrafo);

		JButton deleteAristaButton = new JButton("Eliminar arista");
		buttonPanel.add(deleteAristaButton);

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
				JDialog dialog = new JDialog(frame, "Agregue su vertice", true);
				dialog.getContentPane().setLayout(new GridLayout(4, 2));
				
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
		
		
		// ----------- Manejo de boton para agregar aristass --------------

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
		
		btnCrearRegiones.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					String regiones = JOptionPane.showInputDialog(frame, "Ingrese la cantidad de regiones");
				} 
				
				catch (NumberFormatException ex) {
					JOptionPane.showMessageDialog(frame, "Por favor ingrese un valor numérico la cantidad de regiones");

				} 
				
				catch (Exception e1) {
					JOptionPane.showMessageDialog(frame, e1.getMessage());
				}
			}
		});

		
		// ----------- Manejo de boton para ver aristas y pesos  --------------
		
		btnVerGrafo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				
			}

		});

		
		frame.getContentPane().add(mapPanel, BorderLayout.CENTER);
		frame.getContentPane().add(buttonPanel, BorderLayout.SOUTH);
		frame.setVisible(true);
	}



	private void inicializarFrame() {
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(800, 600);
		frame.setLocationRelativeTo(null);
		
		JPanel titlePanel = new JPanel();
		JLabel titleLabel = new JLabel("Agregue sus vértices... ");
		titleLabel.setFont(new Font("Times New Roman", Font.PLAIN, 32));
		titlePanel.add(titleLabel);		
		frame.getContentPane().add(titlePanel, BorderLayout.NORTH);

	}

	
	private void mostrarMensajeDeBienvenida() {
		String mensajeBienvenida = "Bienvenido al programa Diseñando regiones.\n\n" +
                "Para marcar un punto en el mapa, simplemente haz clic en la ubicación deseada.\n" +
                "Luego, se te pedirá que ingreses un nombre para el punto.\n" +
                "Puedes marcar varios puntos de esta manera.\n\n" +
                "Para generar regiones, todos los puntos deben estar conectados \npor al menos un índice de similitud.\n" +
                "Asegúrate de haber marcado todos los puntos que deseas incluir en las regiones.\n\n" +
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
    	Mapa ms = null;
  	
        try {
        	FileInputStream fis = new FileInputStream("mapa.txt");
        	ObjectInputStream in = new ObjectInputStream(fis);
        	ms = (Mapa) in.readObject();
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
        	FileOutputStream fos = new FileOutputStream("mapa.txt");
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
		SwingUtilities.invokeLater(Mapa::new);
	}
}
