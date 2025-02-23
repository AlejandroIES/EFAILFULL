package Objetos;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Carta extends JPanel {

	private static int ancho;
	private static int alto;

	// Labels de la carta
	private String tipo;
	private JLabel costeLabel;
	private JLabel nombreLabel;
	private JLabel elementoLabel;
	private JLabel claseLabel;
	private JLabel descripcionLabel;
	private JLabel potenciaLabel;
	private JLabel efectosLabel;
	private Image imagenFondo;

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g); // Pinta los componentes hijos normalmente

		// Escalar la imagen al tamaño del panel (carta)
		if (imagenFondo != null) {
			g.drawImage(imagenFondo, 0, 0, getWidth(), getHeight(), this);
		}
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public JLabel getCosteLabel() {
		return costeLabel;
	}

	public void setCosteLabel(JLabel costeLabel) {
		this.costeLabel = costeLabel;
	}

	public JLabel getNombreLabel() {
		return nombreLabel;
	}

	public void setNombreLabel(JLabel nombreLabel) {
		this.nombreLabel = nombreLabel;
	}

	public JLabel getElementoLabel() {
		return elementoLabel;
	}

	public void setElementoLabel(JLabel elementoLabel) {
		this.elementoLabel = elementoLabel;
	}

	public JLabel getClaseLabel() {
		return claseLabel;
	}

	public void setClaseLabel(JLabel claseLabel) {
		this.claseLabel = claseLabel;
	}

	public JLabel getDescripcionLabel() {
		return descripcionLabel;
	}

	public void setDescripcionLabel(JLabel descripcionLabel) {
		this.descripcionLabel = descripcionLabel;
	}

	public JLabel getPotenciaLabel() {
		return potenciaLabel;
	}

	public void setPotenciaLabel(JLabel potenciaLabel) {
		this.potenciaLabel = potenciaLabel;
	}

	public JLabel getEfectosLabel() {
		return efectosLabel;
	}

	public void setEfectosLabel(JLabel efectosLabel) {
		this.efectosLabel = efectosLabel;
	}

	public Image getImagenFondo() {
		return imagenFondo;
	}

	public void setImagenFondo(Image imagenFondo) {
		this.imagenFondo = imagenFondo;
	}

	/**
	 * 
	 * @param anchoCarta
	 * @param altoCarta
	 * @param nombre
	 * @param clase
	 * @param coste
	 * @param elemento
	 * @param descripcion
	 * @param potencia
	 * @param efectos
	 */
	public Carta(int anchoCarta, int altoCarta, String nombre, String clase, String coste, String elemento,
			String descripcion, String potencia, String tipo, String efectos) {
		ancho = anchoCarta;
		alto = altoCarta;
		this.tipo = tipo;

		// Establecer layout nulo para poder posicionar los elementos manualmente
		setLayout(null);
		setPreferredSize(new Dimension(ancho, alto));
		imagenFondo = new ImageIcon(
				"C:\\Users\\alegu\\git\\repository\\com.efailfull\\src\\main\\resources\\cartaBase.png").getImage(); // Cambia
																														// la
																														// ruta
																														// aquí

		// Coste
		costeLabel = new JLabel(coste, SwingConstants.CENTER);
		costeLabel.setBounds(ancho / 32, alto / 32, ancho / 8, alto / 8);
		costeLabel.setOpaque(false);
		costeLabel.setForeground(Color.WHITE);
		add(costeLabel);

		// Nombre
		nombreLabel = new JLabel(
				"<html><body style='text-align: center;'>" + nombre.replaceAll("\n", "<br>") + "</body></html>",
				SwingConstants.CENTER);
		nombreLabel.setBounds(ancho / 4, alto / 30, ancho / 2, alto / 12);
		nombreLabel.setOpaque(false);
		nombreLabel.setForeground(Color.WHITE);
		nombreLabel.setFont(new Font("Merryweather", Font.BOLD, 9)); // Tamaño de fuente ajustado
		add(nombreLabel);

		// Elemento
		elementoLabel = new JLabel(elemento, SwingConstants.RIGHT);
		elementoLabel.setBounds(6 * ancho / 8 - ancho / 32, alto / 32, ancho / 4, alto / 8);
		elementoLabel.setOpaque(false);
		elementoLabel.setForeground(Color.WHITE);
		add(elementoLabel);

		// Clase
		claseLabel = new JLabel("Clase", SwingConstants.CENTER);
		claseLabel.setBounds(ancho / 4, alto / 6, ancho / 2, alto / 4);
		claseLabel.setOpaque(false);
		claseLabel.setForeground(Color.WHITE);
		add(claseLabel);

		// Descripción
		descripcionLabel = new JLabel(
				"<html><body style='text-align: center;'>" + descripcion.replaceAll("\n", "<br>") + "</body></html>",
				SwingConstants.CENTER);
		descripcionLabel.setBounds(ancho / 8, alto / 2, 3 * ancho / 4, alto / 5);
		descripcionLabel.setOpaque(false);
		descripcionLabel.setForeground(Color.WHITE);
		descripcionLabel.setVerticalAlignment(SwingConstants.TOP); // Alineación superior para el texto
		descripcionLabel.setHorizontalAlignment(SwingConstants.CENTER); // Alineación centrada para el texto
		descripcionLabel.setFont(new Font("Merryweather", Font.BOLD, 9)); // Tamaño de fuente ajustado

		// Establecer el tamaño del label para que el texto pueda envolverse
		descripcionLabel.setPreferredSize(new Dimension(3 * ancho / 4, alto / 6)); // Dimensiones del JLabel
		descripcionLabel.setMinimumSize(new Dimension(3 * ancho / 4, alto / 6)); // Tamaño mínimo para el salto de línea
		add(descripcionLabel);

		// Potencia
		potenciaLabel = new JLabel(potencia, SwingConstants.CENTER);
		potenciaLabel.setBounds(ancho / 4, 2 * alto / 3 + alto / 24, ancho / 2, alto / 12);
		potenciaLabel.setOpaque(false);
		potenciaLabel.setForeground(Color.WHITE);
		add(potenciaLabel);

		// Efectos
		efectosLabel = new JLabel(efectos, SwingConstants.CENTER);
		efectosLabel.setBounds(ancho / 4, 5 * alto / 6, ancho / 2, alto / 12);
		efectosLabel.setOpaque(false);
		efectosLabel.setForeground(Color.WHITE);
		add(efectosLabel);

		/*
		 * 
		 * // Arrastrar para mover la carta addMouseListener(new MouseAdapter() { public
		 * void mousePressed(MouseEvent e) { requestFocus(); } });
		 * addMouseMotionListener(new MouseAdapter() { public void
		 * mouseDragged(MouseEvent e) { Point location = getLocation();
		 * setLocation(location.x + e.getX(), location.y + e.getY()); } });
		 * 
		 */
	}

	public static void main(String[] args) {
		JFrame ventana = new JFrame("Pantalla con Cartas");
		ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Establecer un tamaño fijo para la ventana basado en 6 cartas de ancho y 2 de
		// alto
		int anchoCarta = 150; // Ancho de la carta
		int altoCarta = 250; // Alto de la carta
		int separacion = 20; // Espacio entre las cartas

		// La ventana tiene que ser lo suficientemente grande para 6 cartas de ancho y 2
		// de alto
		int anchoPantalla = 6 * anchoCarta + 7 * separacion; // 6 cartas + 7 espacios
		int altoPantalla = 2 * altoCarta + 3 * separacion; // 2 cartas + 3 espacios

		ventana.setSize(anchoPantalla, altoPantalla);
		ventana.setLayout(null); // Layout absoluto para que podamos posicionar las cartas libremente

		// Crear una carta (la primera) y añadirla en una posición inicial
		Carta carta = new Carta(anchoCarta, altoCarta, "Porrazo", "Caballero", "2", "N",
				"No todos los días se corre la misma suerte, a veces toca mamar, es lo que toca", "12", "1",
				"-Def (2)");

		// Posicionar la carta en un lugar específico de la ventana
		carta.setBounds(separacion, separacion, anchoCarta, altoCarta); // Posición (x, y)

		// Añadir la carta a la ventana
		ventana.add(carta);

		ventana.setVisible(true);
	}
}
