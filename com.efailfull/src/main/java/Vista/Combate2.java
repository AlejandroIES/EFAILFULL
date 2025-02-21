package Vista;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Combate2 extends JPanel {

	private static int ancho;
	private static int alto;
	private static int margenAlto;
	private static int margenAncho;
	private JLabel descripcionCartaLabel;
	private JButton usarCartaButton;
	private static boolean cartaSeleccionada = false;
	private static int cartaActiva;
	public static int cartasEnMano = 8;
	private JPanel cartasPanel;

	public Combate2() {
		setBackground(Color.WHITE);
		ancho = Menu.ancho;
		alto = Menu.alto;
		Insets insets = getInsets();
		margenAlto = insets.bottom;
		margenAncho = insets.left;
		setLayout(null); // Usamos layout nulo para posiciones personalizadas

		// MAZO V2
		// Mazo (parte superior izquierda)
		JLabel mazoLabel = new JLabel("Mazo", SwingConstants.CENTER);
		mazoLabel.setBounds(margenAncho + ancho / 64, margenAlto + alto / 24, ancho / 16, alto / 6);
		mazoLabel.setOpaque(true);
		mazoLabel.setBackground(Color.DARK_GRAY);
		add(mazoLabel);

		// Cartas Quemadas (parte superior derecha)
		JLabel quemadasLabel = new JLabel("Quemadas", SwingConstants.CENTER);
		quemadasLabel.setBounds(ancho * 10 / 12, alto / 3, ancho / 16, alto / 6);
		quemadasLabel.setOpaque(true);
		quemadasLabel.setBackground(Color.BLACK);
		quemadasLabel.setForeground(Color.WHITE);
		add(quemadasLabel);

		// Cartas Descartadas (parte inferior derecha)
		JLabel descartadasLabel = new JLabel("Descartadas", SwingConstants.CENTER);
		descartadasLabel.setBounds(ancho * 10 / 12, 2 * alto / 3, ancho / 16, alto / 6);
		descartadasLabel.setOpaque(true);
		descartadasLabel.setBackground(Color.GRAY);
		add(descartadasLabel);

		// CARTAS
		// -----------------------------------------------------------------------------------------------------------------

		// Cartas (en la parte inferior centrada)
		cartasPanel = new JPanel();
		cartasPanel.setLayout(null); // No layout para posiciones personalizadas
		cartasPanel.setBounds(ancho / 4, alto * 7 / 10, ancho / 2, alto / 4);
		cartasPanel.setOpaque(true);
		add(cartasPanel);

		robarMano();

		// JUGADOR
		// -----------------------------------------------------------------------------------------------------------------
		// ENERGÍA JUGADOR V1
		JLabel energiaJugadorLabel = new JLabel("Energía Jugador", SwingConstants.CENTER);
		int alturaEnergia = ancho / 16;
		energiaJugadorLabel.setBounds(margenAncho, 7 * alto / 8 - alturaEnergia - margenAlto, ancho / 16,
				alturaEnergia);
		energiaJugadorLabel.setOpaque(true);
		energiaJugadorLabel.setBackground(Color.CYAN);
		add(energiaJugadorLabel);

		// IMAGEN JUGADOR V1
		JLabel jugadorLabel = new JLabel("Jugador", SwingConstants.CENTER);
		int alturaJugador = 5 * alto / 8;
		jugadorLabel.setBounds(margenAncho + ancho / 12, alto - alturaJugador - margenAlto, ancho / 4, alturaJugador);
		jugadorLabel.setOpaque(true);
		jugadorLabel.setBackground(Color.BLUE);
		add(jugadorLabel);

		// ESTADOS JUGADOR V1
		JLabel estadosJugadorLabel = new JLabel("Estados Jugador", SwingConstants.CENTER);
		estadosJugadorLabel.setBounds(margenAncho + ancho / 12, alto - alturaJugador - margenAlto - alto / 16,
				ancho / 4, alto / 24);
		estadosJugadorLabel.setOpaque(true);
		estadosJugadorLabel.setBackground(Color.YELLOW);
		add(estadosJugadorLabel);

		// SALUD JUGADOR V1
		JLabel saludJugadorLabel = new JLabel("Salud Jugador", SwingConstants.CENTER);
		saludJugadorLabel.setBounds(margenAncho + ancho / 12,
				alto - alturaJugador - margenAlto - alto / 16 - alto / 24 - alto / 64, ancho / 4, alto / 24);
		saludJugadorLabel.setOpaque(true);
		saludJugadorLabel.setBackground(Color.GREEN);
		add(saludJugadorLabel);

		// ENEMIGO
		// -----------------------------------------------------------------------------------------------------------------
		// SALUD ENEMIGO V1
		JLabel saludEnemigoLabel = new JLabel("Salud Enemigo", SwingConstants.CENTER);
		saludEnemigoLabel.setBounds(margenAncho + 3 * ancho / 8, margenAlto + alto / 24, ancho / 8, alto / 24);
		saludEnemigoLabel.setOpaque(true);
		saludEnemigoLabel.setBackground(Color.RED);
		add(saludEnemigoLabel);

		// ESTADOS ENEMIGO V1
		JLabel estadosEnemigoLabel = new JLabel("Estados Enemigo", SwingConstants.CENTER);
		estadosEnemigoLabel.setBounds(margenAncho + 2 * ancho / 8, margenAlto + 2 * alto / 24 + alto / 64, ancho / 4,
				alto / 24);
		estadosEnemigoLabel.setOpaque(true);
		estadosEnemigoLabel.setBackground(Color.ORANGE);
		add(estadosEnemigoLabel);

		// INTENCIÓN ENEMIGO V1
		JLabel intencionEnemigoLabel = new JLabel("Intención Enemigo", SwingConstants.CENTER);
		intencionEnemigoLabel.setBounds(margenAncho + 3 * ancho / 8, margenAlto + 3 * alto / 24 + 2 * alto / 64,
				ancho / 8, alto / 24);
		intencionEnemigoLabel.setOpaque(true);
		intencionEnemigoLabel.setBackground(Color.MAGENTA);
		add(intencionEnemigoLabel);

		// IMAGEN ENEMIGO V1
		JLabel enemigoLabel = new JLabel("Enemigo", SwingConstants.CENTER);
		enemigoLabel.setBounds(margenAncho + ancho / 2 + alto / 64, margenAlto + alto / 24, ancho / 4, alto / 2);
		enemigoLabel.setOpaque(true);
		enemigoLabel.setBackground(Color.PINK);
		add(enemigoLabel);

		// -----------------------------------------------------------------------------------------------------------------

		// Botón "Usar Carta" (parte inferior derecha)
		usarCartaButton = new JButton("Usar Carta");
		usarCartaButton.setEnabled(false); // Inicialmente deshabilitado
		usarCartaButton.setBounds(ancho * 9 / 12, alto * 9 / 10, ancho / 8, alto / 12);
		add(usarCartaButton);

		usarCartaButton.addActionListener(e -> {
			if (cartaSeleccionada) {
				JOptionPane.showMessageDialog(null, "Usaste la carta seleccionada.");
				deseleccionarCarta();
			}
		});

		// Hacer clic en cualquier lugar que no sea una carta deselecciona las cartas
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				deseleccionarCarta();
			}
		});
	}

	// Método para seleccionar una carta
	private void seleccionarCarta(JLabel cartaLabel, int posicion) {
		if (descripcionCartaLabel != null)
		remove(descripcionCartaLabel);
		
		cartaActiva = posicion;
		cartaSeleccionada = true;
		// Descripción Carta (centrado)
		descripcionCartaLabel = new JLabel("", SwingConstants.CENTER);
		descripcionCartaLabel.setBounds(ancho / 4, 7 * alto / 20, ancho / 2, alto / 3);
		descripcionCartaLabel.setOpaque(true);
		add(descripcionCartaLabel);
		descripcionCartaLabel
				.setText("Descripción de la carta seleccionada.\nHaz clic en 'Usar Carta' para utilizarla.");
		descripcionCartaLabel.setBackground(Color.LIGHT_GRAY);
		usarCartaButton.setEnabled(true);
	}

	// Método para deseleccionar la carta
	private void deseleccionarCarta() {
		cartaSeleccionada = false;
		remove(descripcionCartaLabel);
		repaint();
		usarCartaButton.setEnabled(false);
	}

	private void robarMano() {
		for (int i = 0; i < cartasEnMano; i++) {
			JLabel cartaLabel = new JLabel("Carta " + (i + 1), SwingConstants.CENTER);
			cartaLabel.setBounds(i * ancho / (2 * cartasEnMano), 0, ancho / (2 * cartasEnMano), alto / 4);
			cartaLabel.setOpaque(true);
			cartaLabel.setBackground(Color.GREEN);
			int posicionCarta = i;
			
			// Evento para seleccionar la carta y actualizar la descripción
			cartaLabel.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					if (cartaSeleccionada && cartaActiva == posicionCarta) {
						deseleccionarCarta();
					} else
						seleccionarCarta(cartaLabel, posicionCarta);
				}
			});

			cartasPanel.add(cartaLabel);
		}
	}

	// Método principal para mostrar la ventana
	public static void main(String[] args) {
		JFrame ventana = new JFrame("Interfaz de Combate");
		ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		ventana.setSize(1200, 800); // Ajusta el tamaño de la ventana
		ventana.setLocationRelativeTo(null); // Centra la ventana
		ventana.add(new Combate2());
		ventana.setVisible(true);
	}
}
