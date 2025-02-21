package Vista;

import javax.swing.*;

import BDD.Utilidades;
import Modelo.Partida;
import Objetos.Carta;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Cartas extends JPanel {

	private static int ancho = Menu.ancho;
	private static int alto = Menu.alto;

	private static JPanel contenidoPanel; // Panel interno desplazable
	private JScrollPane scrollPane;

	private static JLabel inventarioLabel;
	private static JPanel inventarioPanel;
	private static JLabel porRobarLabel;
	private static JPanel porRobarPanel;
	private static JLabel descartadasLabel;
	private static JPanel descartadasPanel;
	private static JLabel desterradasLabel;
	private static JPanel desterradasPanel;

	public static List<Carta> inventario = new ArrayList<>();
	public static List<Carta> porRobar = new ArrayList<>();
	public static List<Carta> descartadas = new ArrayList<>();
	public static List<Carta> desterradas = new ArrayList<>();

	public static int cartaAncho, cartaAlto, paddingAncho, paddingAlto;
	public static Cartas instanciaCartas;

	public Cartas() {
		cartaAncho = ancho / 9;
		cartaAlto = alto / 3;
		paddingAncho = ancho / 42;
		paddingAlto = alto / 42;

		// Configurar el panel principal
		setLayout(new BorderLayout());

		// Crear el panel desplazable
		contenidoPanel = new JPanel();
		contenidoPanel.setLayout(null); // Sin layout para posicionamiento personalizado
		contenidoPanel.setPreferredSize(new Dimension(ancho, alto * 3)); // Aumentar altura para desplazamiento

		// Inicializar las secciones
		iniciaInventario();
		iniciaPorRobar();
		iniciaDescartadas();
		// iniciaDesterradas();

		// Envolver el contenido en un JScrollPane
		scrollPane = new JScrollPane(contenidoPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.getVerticalScrollBar().setUnitIncrement(16); // Velocidad de desplazamiento

		// Añadir el JScrollPane al panel principal
		add(scrollPane, BorderLayout.CENTER);
		instanciaCartas = this;
	}

	private void iniciaInventario() {
		inventario.clear();

		// Crear cartas y añadirlas al inventario
		for (int i = 0; i < Partida.mazo.size(); i++) {
			inventario.add(Utilidades.devuelveCarta(Partida.mazo.get(i), cartaAncho, cartaAlto));
		}

		// Label del inventario
		inventarioLabel = new JLabel("Inventario:", SwingConstants.LEFT);
		inventarioLabel.setBounds(paddingAncho, paddingAlto, ancho / 2, alto / 32);
		inventarioLabel.setOpaque(true);
		inventarioLabel.setBackground(Color.GRAY);
		contenidoPanel.add(inventarioLabel);

		// Panel del inventario
		inventarioPanel = new JPanel(null);
		inventarioPanel.setBounds(paddingAncho, paddingAlto * 3, ancho - 2 * paddingAncho,
				(1 + inventario.size() / 6) * (cartaAlto + paddingAlto));
		inventarioPanel.setOpaque(true);

		// Añadir cartas al panel del inventario
		for (int i = 0; i < inventario.size(); i++) {
			Carta carta = inventario.get(i);
			carta.setBounds((i % 6) * (cartaAncho + paddingAncho) + paddingAncho, (i / 6) * (cartaAlto + paddingAlto),
					cartaAncho, cartaAlto);
			inventarioPanel.add(carta);
		}

		contenidoPanel.add(inventarioPanel);
	}

	private void iniciaPorRobar() {
		porRobar.clear();
		// Crear cartas y añadirlas a la lista por robar
		for (int i = 0; i < Partida.porRobar.size(); i++) {
			porRobar.add(Utilidades.devuelveCarta(Partida.porRobar.get(i), cartaAncho, cartaAlto));
		}

		// Label de "Por robar"
		porRobarLabel = new JLabel("Por robar:", SwingConstants.LEFT);
		porRobarLabel.setBounds(paddingAncho, inventarioPanel.getY() + inventarioPanel.getHeight() + paddingAlto,
				ancho / 2, alto / 32);
		porRobarLabel.setOpaque(true);
		porRobarLabel.setBackground(Color.GRAY);
		contenidoPanel.add(porRobarLabel);

		// Panel "Por robar"
		porRobarPanel = new JPanel(null);
		porRobarPanel.setBounds(paddingAncho, porRobarLabel.getY() + porRobarLabel.getHeight() + paddingAlto,
				ancho - 2 * paddingAncho, (1 + porRobar.size() / 6) * (cartaAlto + paddingAlto));
		porRobarPanel.setOpaque(true);

		// Añadir cartas al panel "Por robar"
		for (int i = 0; i < porRobar.size(); i++) {
			Carta carta = porRobar.get(i);
			carta.setBounds((i % 6) * (cartaAncho + paddingAncho) + paddingAncho, (i / 6) * (cartaAlto + paddingAlto),
					cartaAncho, cartaAlto);
			porRobarPanel.add(carta);
		}

		contenidoPanel.add(porRobarPanel);
	}

	public void refrescaPorRobar() {
		porRobar.clear();
		porRobarPanel.removeAll();
		// Crear cartas y añadirlas a la lista por robar
		for (int i = 0; i < Partida.porRobar.size(); i++) {
			porRobar.add(Utilidades.devuelveCarta(Partida.porRobar.get(i), cartaAncho, cartaAlto));
		}

		// Label de "Por robar"
		porRobarLabel.setBounds(paddingAncho, inventarioPanel.getY() + inventarioPanel.getHeight() + paddingAlto,
				ancho / 2, alto / 32);

		// Panel "Por robar"
		porRobarPanel.setBounds(paddingAncho, porRobarLabel.getY() + porRobarLabel.getHeight() + paddingAlto,
				ancho - 2 * paddingAncho, (1 + porRobar.size() / 6) * (cartaAlto + paddingAlto));

		// Añadir cartas al panel "Por robar"
		for (int i = 0; i < porRobar.size(); i++) {
			Carta carta = porRobar.get(i);
			carta.setBounds((i % 6) * (cartaAncho + paddingAncho) + paddingAncho, (i / 6) * (cartaAlto + paddingAlto),
					cartaAncho, cartaAlto);
			porRobarPanel.add(carta);
		}

		this.repaint();
	}

	private void iniciaDescartadas() {

		// Label de "Descartadas"
		descartadasLabel = new JLabel("Descartadas:", SwingConstants.LEFT);
		descartadasLabel.setBounds(paddingAncho, porRobarPanel.getY() + porRobarPanel.getHeight() + paddingAlto,
				ancho / 2, alto / 32);
		descartadasLabel.setOpaque(true);
		descartadasLabel.setBackground(Color.GRAY);
		contenidoPanel.add(descartadasLabel);

		// Panel "Descartadas"
		descartadasPanel = new JPanel(null);
		descartadasPanel.setBounds(paddingAncho, descartadasLabel.getY() + descartadasLabel.getHeight() + paddingAlto,
				ancho - 2 * paddingAncho, (1 + descartadas.size() / 6) * (cartaAlto + paddingAlto));
		descartadasPanel.setOpaque(true);

		contenidoPanel.add(descartadasPanel);

	}

	public void refrescaDescartadas() {
		descartadas.clear();
		descartadasPanel.removeAll();
		// Crear cartas y añadirlas a la lista por robar
		for (int i = 0; i < Partida.descartadas.size(); i++) {
			descartadas.add(Utilidades.devuelveCarta(Partida.descartadas.get(i), cartaAncho, cartaAlto));
		}

		// Label de "Descartadas"
		descartadasLabel.setBounds(paddingAncho, porRobarPanel.getY() + porRobarPanel.getHeight() + paddingAlto,
				ancho / 2, alto / 32);

		// Panel "Descartadas"
		descartadasPanel.setBounds(paddingAncho, descartadasLabel.getY() + descartadasLabel.getHeight() + paddingAlto,
				ancho - 2 * paddingAncho, (1 + descartadas.size() / 6) * (cartaAlto + paddingAlto));

		// Añadir cartas al panel "Descartadas"
		for (int i = 0; i < descartadas.size(); i++) {
			Carta carta = descartadas.get(i);
			carta.setBounds((i % 6) * (cartaAncho + paddingAncho) + paddingAncho, (i / 6) * (cartaAlto + paddingAlto),
					cartaAncho, cartaAlto);
			descartadasPanel.add(carta);
		}

		this.repaint();

	}

	private void refrescaDesterradas() {
		// Similar al método iniciaPorRobar(), pero para desterradas
		// Implementar lógica y añadir el panel en posición adecuada
	}

	public void moverCartaADescartes(Combate combate, int posicion) {
		Partida.descartadas.add(Partida.mano.remove(posicion));
		this.refrescaDescartadas();
		combate.cartasEnMano--;

	}

	public void moverCartaAMano(Combate combate) {

		if (Partida.porRobar.size() == 0) {

			int cantidad = Partida.descartadas.size();

			for (int i = 0; i < cantidad; i++) {
				Partida.porRobar.add(Partida.descartadas.remove(0));
			}

			Collections.shuffle(Partida.porRobar);
		}
		Partida.mano.add(Partida.porRobar.remove(0));
		this.refrescaPorRobar();
		this.refrescaDescartadas();
		combate.cartasEnMano++;

	}

	public void reiniciaMazo() {

		inventario.clear();
		porRobar.clear();
		Partida.descartadas.clear();
		Partida.mano.clear();

		refrescaDescartadas();
		refrescaPorRobar();

		// Crear cartas y añadirlas al inventario
		for (int i = 0; i < Partida.mazo.size(); i++) {
			inventario.add(Utilidades.devuelveCarta(Partida.mazo.get(i), cartaAncho, cartaAlto));
		}

		// Añadir cartas al panel del inventario
		for (int i = 0; i < inventario.size(); i++) {
			Carta carta = inventario.get(i);
			carta.setBounds((i % 6) * (cartaAncho + paddingAncho) + paddingAncho, (i / 6) * (cartaAlto + paddingAlto),
					cartaAncho, cartaAlto);
			inventarioPanel.add(carta);
		}

		this.revalidate();
		this.repaint();

	}

	/*
	 * 
	 * public void moverCartaAMano(int posicion) {
	 * Partida.mano.add(Partida.porRobar.remove(posicion));
	 * this.refrescaDescartadas(); Combate.cartasEnMano++; }
	 * 
	 */
}
