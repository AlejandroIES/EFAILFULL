package Vista;

import javax.swing.*;

import BDD.Utilidades;
import Controlador.ControladorCombate;
import Modelo.Partida;
import Objetos.Carta;
import Objetos.Efecto;
import Objetos.Marca;
import Objetos.Modificador;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class Combate extends JPanel {

	public static int ancho;
	public static int alto;
	public int alturaJugador;
	public int margenAlto;
	public int margenAncho;
	public JButton infoJugadorButton;
	public JButton infoEnemigoButton;
	//public JLabel quemadasLabel;
	//public JLabel descartadasLabel;
	public JLabel infoJugadorLabel;
	public JLabel nivelJugadorLabel;
	public JLabel energiaJugadorLabel;
	public JLabel jugadorLabel;
	public JPanel efectosJugadorPanel;
	public JLabel saludJugadorLabel;
	public JLabel bloqueoJugadorLabel;
	public JLabel infoEnemigoLabel;
	public JLabel nivelEnemigoLabel;
	public JLabel nombreEnemigoLabel;
	public JLabel saludEnemigoLabel;
	public JLabel bloqueoEnemigoLabel;
	public JPanel efectosEnemigoPanel;
	public JLabel intencionEnemigoLabel;
	public JLabel enemigoLabel;
	public JPanel descripcionCartaLabel;
	public JButton usarCartaButton;
	public JButton finTurnoButton;
	public static boolean cartaSeleccionada = false;
	public int cartaActiva;
	public int cartasEnMano = 7;
	public int cartasPorTurno = 7;
	public int cartasDescartadas = 0;
	public int cartasDesterradas = 0;
	public static JPanel cartasPanel;
	public static Combate instanciaCombate;
	private ControladorCombate controladorCombate;

	public Combate() {

		if (Partida.batallaActiva) {
			instanciaCombate = this;
			controladorCombate = new ControladorCombate();

			setBackground(Color.WHITE);
			ancho = Menu.ancho;
			alto = Menu.alto;
			margenAlto = alto / 32;
			margenAncho = 0;
			setLayout(null);
			
			/*

			// Cartas Quemadas (parte superior derecha)
			quemadasLabel = new JLabel(cartasDescartadas + "", SwingConstants.CENTER);
			quemadasLabel.setBounds(ancho * 10 / 12 + ancho / 16, 2 * alto / 12, ancho / 16, alto / 6);
			quemadasLabel.setOpaque(true);
			quemadasLabel.setBackground(Color.GRAY);
			quemadasLabel.setForeground(Color.WHITE);
			add(quemadasLabel);

			// Cartas Descartadas (parte inferior derecha)
			descartadasLabel = new JLabel(cartasDesterradas + "", SwingConstants.CENTER);
			descartadasLabel.setBounds(ancho * 10 / 12 + ancho / 16, 5 * alto / 12, ancho / 16, alto / 6);
			descartadasLabel.setOpaque(true);
			descartadasLabel.setBackground(Color.LIGHT_GRAY);
			add(descartadasLabel);
			
			*/

			// CARTAS
			cartasPanel = new JPanel();
			cartasPanel.setLayout(null); // No layout para posiciones personalizadas
			cartasPanel.setBounds(ancho / 4 + ancho / 16, 5 * alto / 8, ancho / 2, alto / 4);
			cartasPanel.setOpaque(true);
			add(cartasPanel);

			// JUGADOR
			energiaJugadorLabel = new JLabel("Energía Jugador", SwingConstants.CENTER);
			int alturaEnergia = ancho / 16;
			energiaJugadorLabel.setBounds(margenAncho + ancho / 64, 7 * alto / 8 - alturaEnergia - margenAlto,
					ancho / 16, alturaEnergia);
			energiaJugadorLabel.setOpaque(true);
			energiaJugadorLabel.setBackground(Color.CYAN);
			add(energiaJugadorLabel);

			jugadorLabel = new JLabel("Jugador", SwingConstants.CENTER);
			alturaJugador = 5 * alto / 8;
			jugadorLabel.setBounds(margenAncho + ancho / 12 + ancho / 64, alto - alturaJugador - alto / 12 - margenAlto,
					ancho / 4, alturaJugador);
			jugadorLabel.setOpaque(true);
			jugadorLabel.setBackground(Color.BLUE);
			add(jugadorLabel);

			// Panel para los estados del jugador
			efectosJugadorPanel = new JPanel();
			efectosJugadorPanel.setBounds(margenAncho + ancho / 12 + ancho / 64,
					alto - alturaJugador - alto / 12 - margenAlto - alto / 16, ancho / 4, alto / 24);
			efectosJugadorPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
			efectosJugadorPanel.setBackground(null);
			add(efectosJugadorPanel);

			saludJugadorLabel = new JLabel("Salud Jugador", SwingConstants.LEFT);
			saludJugadorLabel.setBounds(margenAncho + ancho / 12 + ancho / 64,
					alto - alturaJugador - alto / 12 - margenAlto - alto / 16 - alto / 32 - alto / 64, ancho / 8,
					alto / 24);
			saludJugadorLabel.setOpaque(true);
			saludJugadorLabel.setBackground(Color.GREEN);
			add(saludJugadorLabel);

			nivelJugadorLabel = new JLabel("Nivel: " + Partida.getPersonaje().getNivel(), SwingConstants.LEFT);
			nivelJugadorLabel.setBounds(margenAncho + ancho / 12 + ancho / 64,
					alto - alturaJugador - alto / 12 - margenAlto - alto / 8 - alto / 24, ancho / 16, alto / 24);
			nivelJugadorLabel.setOpaque(true);
			nivelJugadorLabel.setBackground(null);
			add(nivelJugadorLabel);

			infoJugadorButton = new JButton("Estado");
			infoJugadorButton.setBounds(margenAncho + ancho / 12 + ancho / 64 + ancho / 4 - ancho / 16,
					alto - alturaJugador - alto / 12 - margenAlto - alto / 8 - alto / 24, ancho / 16, alto / 24);
			add(infoJugadorButton);

			bloqueoJugadorLabel = new JLabel("", SwingConstants.LEFT);
			bloqueoJugadorLabel.setBounds(margenAncho + ancho / 12 + ancho / 64 + ancho / 8,
					alto - alturaJugador - alto / 12 - margenAlto - alto / 16 - alto / 32 - alto / 64, ancho / 8,
					alto / 24);
			bloqueoJugadorLabel.setOpaque(true);
			bloqueoJugadorLabel.setForeground(Color.LIGHT_GRAY);
			add(bloqueoJugadorLabel);

			// ENEMIGO
			saludEnemigoLabel = new JLabel("Salud Enemigo", SwingConstants.RIGHT);
			saludEnemigoLabel.setBounds(margenAncho + ancho / 2, margenAlto, ancho / 8, alto / 24);
			saludEnemigoLabel.setOpaque(true);
			saludEnemigoLabel.setBackground(Color.RED);
			add(saludEnemigoLabel);

			bloqueoEnemigoLabel = new JLabel("", SwingConstants.RIGHT);
			bloqueoEnemigoLabel.setBounds(margenAncho + 3 * ancho / 8, margenAlto, ancho / 8, alto / 24);
			bloqueoEnemigoLabel.setOpaque(true);
			bloqueoEnemigoLabel.setForeground(Color.LIGHT_GRAY);
			add(bloqueoEnemigoLabel);

			// Panel para los estados del enemigo
			efectosEnemigoPanel = new JPanel();
			efectosEnemigoPanel.setBounds(margenAncho + ancho / 2 - ancho / 8, margenAlto + alto / 32 + alto / 64,
					ancho / 4, alto / 24);
			efectosEnemigoPanel.setBackground(null);
			efectosEnemigoPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
			add(efectosEnemigoPanel);

			intencionEnemigoLabel = new JLabel("Intención Enemigo", SwingConstants.RIGHT);
			intencionEnemigoLabel.setBounds(margenAncho + ancho / 2, margenAlto + 2 * alto / 32 + 2 * alto / 64,
					ancho / 8, alto / 24);
			intencionEnemigoLabel.setOpaque(true);
			intencionEnemigoLabel.setBackground(null);
			add(intencionEnemigoLabel);

			enemigoLabel = new JLabel("Enemigo", SwingConstants.CENTER);
			enemigoLabel.setBounds(margenAncho + ancho / 2 + ancho / 8 + alto / 64, margenAlto, ancho / 4, alto / 2);
			enemigoLabel.setOpaque(true);
			enemigoLabel.setBackground(Color.PINK);
			add(enemigoLabel);

			nivelEnemigoLabel = new JLabel("Nivel: " + Partida.getEnemigoActual().getNivel(), SwingConstants.LEFT);
			nivelEnemigoLabel.setBounds(margenAncho + ancho / 2 + ancho / 8 + alto / 64,
					margenAlto + alto / 2 + alto / 64, ancho / 16, alto / 24);
			nivelEnemigoLabel.setOpaque(true);
			nivelEnemigoLabel.setBackground(null);
			add(nivelEnemigoLabel);

			nombreEnemigoLabel = new JLabel("Nombre Enemigo", SwingConstants.CENTER);
			nombreEnemigoLabel.setBounds(margenAncho + ancho / 2 + ancho / 8 + ancho / 16 + alto / 64,
					margenAlto + alto / 2 + alto / 64, ancho / 8, alto / 24);
			nombreEnemigoLabel.setOpaque(true);
			nombreEnemigoLabel.setBackground(Color.PINK);
			add(nombreEnemigoLabel);

			infoEnemigoButton = new JButton("Estado");
			infoEnemigoButton.setBounds(margenAncho + ancho / 2 + ancho / 4 + ancho / 16 + alto / 64,
					margenAlto + alto / 2 + alto / 64, ancho / 16, alto / 24);
			add(infoEnemigoButton);

			// Botón "Usar Carta"
			usarCartaButton = new JButton("Usar Carta");
			usarCartaButton.setEnabled(false);
			usarCartaButton.setBounds(ancho * 10 / 12, 8 * alto / 12, ancho / 8, alto / 12);
			add(usarCartaButton);

			// Botón "Fin Turno"
			finTurnoButton = new JButton("Fin Turno");
			finTurnoButton.setEnabled(true);
			finTurnoButton.setBounds(ancho * 10 / 12, 9 * alto / 12 + alto / 32, ancho / 8, alto / 12);
			add(finTurnoButton);

			// Hacer clic en cualquier lugar que no sea una carta deselecciona las cartas
			addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					controladorCombate.deseleccionarCarta();
				}
			});

			controladorCombate.combateSetup();
		}

	}

	public void actualizarMano() {
		// Limpia las cartas actualmente visibles en el panel
		cartasPanel.removeAll();

		// Itera sobre las cartas en la mano
		for (int i = 0; i < cartasEnMano; i++) {
			String idCarta = Partida.mano.get(i); // Obtiene el ID de la carta
			Carta carta = Utilidades.devuelveCarta(idCarta, ancho / (2 * cartasEnMano), alto / 4);

			// Establece la posición y tamaño de la carta en el panel
			carta.setBounds(i * ancho / (2 * cartasEnMano), 0, ancho / (2 * cartasEnMano), alto / 4);

			int posicionCarta = i;

			// Agrega el listener para gestionar la selección/deselección
			carta.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					if (cartaSeleccionada && cartaActiva == posicionCarta) {
						controladorCombate.deseleccionarCarta();
					} else {
						controladorCombate.seleccionarCarta(carta, posicionCarta, idCarta);
					}
				}
			});

			// Agrega la carta al panel
			cartasPanel.add(carta);
		}

		// Actualiza la interfaz
		cartasPanel.repaint();
		cartasPanel.revalidate();
	}

	public void pintaEfectos(boolean esJugador) {
		// Seleccionar el panel y la lista de efectos según el booleano
		JPanel efectosPanel = esJugador ? efectosJugadorPanel : efectosEnemigoPanel;
		List<Efecto> efectos = esJugador ? Partida.getPersonaje().getEfectos()
				: Partida.getEnemigoActual().getEfectos();

		// Limpiar el panel antes de añadir nuevos iconos
		efectosPanel.removeAll();

		for (Efecto efecto : efectos) {
			String ruta = "C:\\Users\\alegu\\eclipse-workspace\\com.efailfull\\src\\main\\resources\\estados\\";
			if (efecto instanceof Modificador) {

				Modificador mod = (Modificador) efecto;
				String upOrDown = "up";

				if (mod.getVariacionPlana() < 0 || mod.getPorcentaje() < 0) {
					upOrDown = "down";
				}

				switch (mod.getEstadistica()) {

				case "Ataque":
					ruta = ruta.concat("Attack").concat(upOrDown).concat(".png");
					break;

				case "Defensa":
					ruta = ruta.concat("Defense").concat(upOrDown).concat(".png");
					break;

				case "Bloqueo":
					ruta = ruta.concat("Block").concat(".png");
					break;

				case "Vida":
					ruta = ruta.concat("Maxhp").concat(upOrDown).concat(".png");
					break;

				case "Curacion":
					ruta = ruta.concat("Hpregen").concat(".png");
					break;

				case "Probabilidad de Critico":
					ruta = ruta.concat("Critrate").concat(upOrDown).concat(".png");
					break;

				case "Dano Critico":
					ruta = ruta.concat("Critdmg").concat(upOrDown).concat(".png");
					break;

				case "Energia":
					ruta = ruta.concat("Energygainturn").concat(upOrDown).concat(".png");
					break;
				}

				ImageIcon icono = new ImageIcon(
						new ImageIcon(ruta).getImage().getScaledInstance(alto / 32, alto / 32, Image.SCALE_SMOOTH));
				JLabel efectoLabel = new JLabel(icono);

				String descripcion = mod.getEstadistica() + " (" + upOrDown + ")";

				// Verificar cuál de los valores no es 0
				if (mod.getVariacionPlana() != 0) {
					if (mod.getVariacionPlana() > 0) {
						descripcion += ": +" + mod.getVariacionPlana();
					} else {
						descripcion += ": " + mod.getVariacionPlana();
					}
				} else if (mod.getPorcentaje() != 0) {
					if (mod.getPorcentaje() > 0) {
						descripcion += ": +" + mod.getPorcentaje() + "%";
					} else {
						descripcion += ": " + mod.getPorcentaje() + "%";
					}
				}

				descripcion += " por " + mod.getDuracion()[0] + " " + mod.getDuracion()[1];
				if (!mod.getDuracion()[0].equals("1")) {
					if (mod.getDuracion()[1].equals("accion")) {
						descripcion += "e";
					}
					descripcion += "s";
				}

				efectoLabel.setToolTipText(descripcion);

				efectosPanel.add(efectoLabel);

			} else if (efecto instanceof Marca) {
				Marca mar = (Marca) efecto;

				ruta = ruta.concat("TraitChange.png");

				ImageIcon icono = new ImageIcon(
						new ImageIcon(ruta).getImage().getScaledInstance(alto / 32, alto / 32, Image.SCALE_SMOOTH));
				JLabel efectoLabel = new JLabel(icono);

				String descripcion = mar.getNombre() + ": " + mar.getDescripcion();

				descripcion += " por " + mar.getDuracion()[0] + " " + mar.getDuracion()[1];
				if (!mar.getDuracion()[0].equals("1")) {
					if (mar.getDuracion()[1].equals("accion")) {
						descripcion += "e";
					}
					descripcion += "s";
				}

				efectoLabel.setToolTipText(descripcion);

				efectosPanel.add(efectoLabel);
			}
		}

		// Actualizar el panel para reflejar los cambios
		efectosPanel.revalidate();
		efectosPanel.repaint();
	}

}
