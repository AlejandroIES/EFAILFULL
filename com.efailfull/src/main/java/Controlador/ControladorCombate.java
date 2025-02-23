package Controlador;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Image;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

import BDD.Utilidades;
import Modelo.Partida;
import Objetos.Carta;
import Objetos.Efecto;
import Objetos.Enemigo;
import Objetos.Marca;
import Objetos.Modificador;
import Objetos.Personaje;
import Objetos.Ataque;
import Vista.Cartas;
import Vista.Combate;
import Vista.Mapa;
import Vista.Menu;
import Vista.NuevaPartida;
import Vista.Recompensa;

public class ControladorCombate {

	private Cartas cartas = Cartas.instanciaCartas;
	private Combate combate = Combate.instanciaCombate;
	private static Ataque siguienteAtaque;

	// Método para preparar el combate, asignar mazo y repartir cartas
	public void combateSetup() {
		// Obtener el enemigo y el personaje
		Enemigo enemigo = Partida.enemigoActual;
		combate.saludEnemigoLabel.setText(enemigo.getVida() + "/" + enemigo.getVidaRestante());
		combate.saludEnemigoLabel.setBackground(null);
		combate.bloqueoEnemigoLabel.setBackground(null);
		combate.nombreEnemigoLabel.setText(enemigo.getNombre());
		combate.nombreEnemigoLabel.setBackground(null);

		Personaje personaje = Partida.personaje;
		combate.saludJugadorLabel.setText(personaje.getVida() + "/" + personaje.getVidaRestante());
		combate.saludJugadorLabel.setBackground(null);
		combate.energiaJugadorLabel.setText(personaje.getEnergia() + "/" + personaje.getEnergiaRestante());
		combate.energiaJugadorLabel.setBackground(null);
		combate.bloqueoJugadorLabel.setBackground(null);

		// Crear y asignar mazo para el combate
		List<String> mazoJugador = new ArrayList<>(Partida.mazo);

		Collections.shuffle(mazoJugador); // Barajar el mazo
		Partida.porRobar = mazoJugador;

		// Robar las primeras X cartas para la mano del jugador
		Partida.mano = new ArrayList<>();
		for (int i = 0; i < combate.cartasPorTurno; i++) {
			if (!Partida.porRobar.isEmpty()) {
				Partida.mano.add(Partida.porRobar.remove(0));
			}
		}
		cartas.refrescaPorRobar();

		eleccionAtaque();

		// ------------------------------------------------------------------------------------------------------
		// Action Listener de las estadisticas del personaje
		// ------------------------------------------------------------------------------------------------------
		combate.infoJugadorButton.addActionListener(e -> {
			String estadisticasJugador = String.format("<html><table border='1' cellspacing='5' cellpadding='5'>"
					+ "<tr><td><b>Estadística</b></td><td><b>Valor</b></td></tr>"
					+ "<tr><td>Energía</td><td>%d/%d</td></tr>" + "<tr><td>Vida</td><td>%d/%d</td></tr>"
					+ "<tr><td>Ataque</td><td>%d</td></tr>" + "<tr><td>Defensa</td><td>%d</td></tr>"
					+ "<tr><td>Bloqueo</td><td>%d</td></tr>" + "<tr><td>Probabilidad Crítico</td><td>%.2f%%</td></tr>"
					+ "<tr><td>Daño Crítico</td><td>%.2f%%</td></tr>" + "</table></html>",
					Partida.personaje.getEnergiaRestante(), Partida.personaje.getEnergia(),
					Partida.personaje.getVidaRestante(), Partida.personaje.getVida(), Partida.personaje.getAtaque(),
					Partida.personaje.getDefensa(), Partida.personaje.getBloqueo(),
					Partida.personaje.getProbabilidadCritico(), Partida.personaje.getDanoCritico());
			JOptionPane.showMessageDialog(combate, estadisticasJugador, "Información del Jugador",
					JOptionPane.INFORMATION_MESSAGE);
		});

		// ------------------------------------------------------------------------------------------------------
		// Action Listener de las estadisticas del enemigo
		// ------------------------------------------------------------------------------------------------------
		combate.infoEnemigoButton.addActionListener(e -> {
			String estadisticasEnemigo = String.format(
					"<html><table border='1' cellspacing='5' cellpadding='5'>"
							+ "<tr><td><b>Estadística</b></td><td><b>Valor</b></td></tr>"
							+ "<tr><td>Vida</td><td>%d/%d</td></tr>" + "<tr><td>Ataque</td><td>%d</td></tr>"
							+ "<tr><td>Defensa</td><td>%d</td></tr>" + "<tr><td>Bloqueo</td><td>%d</td></tr>"
							+ "<tr><td>Probabilidad Crítico</td><td>%.2f%%</td></tr>"
							+ "<tr><td>Daño Crítico</td><td>%.2f%%</td></tr>" + "</table></html>",
					Partida.enemigoActual.getVidaRestante(), Partida.enemigoActual.getVida(),
					Partida.enemigoActual.getAtaque(), Partida.enemigoActual.getDefensa(),
					Partida.enemigoActual.getBloqueo(), Partida.enemigoActual.getProbabilidadCritico(),
					Partida.enemigoActual.getDanioCritico());
			JOptionPane.showMessageDialog(null, estadisticasEnemigo, "Información del Enemigo",
					JOptionPane.INFORMATION_MESSAGE);
		});

		// ------------------------------------------------------------------------------------------------------
		// Action Listener de cuando se usa una carta
		// ------------------------------------------------------------------------------------------------------

		combate.usarCartaButton.addActionListener(e -> {
			if (usarCarta(Partida.mano.get(combate.cartaActiva))) {
				if (!Partida.batallaActiva) {
					return;
				}
				deseleccionarCarta();
				combate.energiaJugadorLabel.setText(personaje.getEnergiaRestante() + "/" + personaje.getEnergia());
				combate.actualizarMano();
				combate.repaint();
			} else {
				JOptionPane.showMessageDialog(null, "No hay energía suficiente.");
			}
		});

		// ------------------------------------------------------------------------------------------------------
		// Action Listener de cuando se acaba el turno
		// ------------------------------------------------------------------------------------------------------
		combate.finTurnoButton.addActionListener(e -> {
			if (Combate.cartaSeleccionada)
				deseleccionarCarta();
			turnoEnemigo();
		});

		// Después de modificar la mano, actualizar la vista
		combate.actualizarMano();
	}

	// Método para reiniciar el turno (actualizar duraciones de efectos...)
	public void nuevoTurno() {

		for (int i = 0; i < combate.cartasPorTurno; i++) {
			cartas.moverCartaAMano(combate);
		}

		cartas.refrescaDescartadas();
		cartas.refrescaPorRobar();
		combate.actualizarMano();
		controladorEfectosTurno(false);
		limpiaEfectos(false);
		controladorEfectosTurno(true);
		limpiaEfectos(true);
		combate.pintaEfectos(true);
		Partida.personaje.setEnergiaRestante(Partida.personaje.getEnergia());
		combate.energiaJugadorLabel
				.setText(Partida.personaje.getEnergiaRestante() + "/" + Partida.personaje.getEnergia());
		combate.energiaJugadorLabel.repaint();
		eleccionAtaque();
	}

	public void seleccionarCarta(Carta carta, int posicion, String id) {
	    if (combate.descripcionCartaLabel != null) {
	        combate.remove(combate.descripcionCartaLabel); // Eliminar la descripción anterior
	    }

	    combate.cartaActiva = posicion;
	    Combate.cartaSeleccionada = true;

	    // Obtener los parámetros de la carta
	    List<String> parametros = Utilidades.parametrosCarta(id);

	    // Crear el panel de la descripción
	    JPanel panelDescripcion = new JPanel();
	    panelDescripcion.setLayout(new GridLayout(7, 2, 10, 10)); // Aumentar filas a 7

	    // Añadir las etiquetas con los datos de la carta
	    JLabel nombreLabel = new JLabel("Nombre: " + parametros.get(0));
	    JLabel costeLabel = new JLabel("Coste: " + parametros.get(2));
	    JLabel elementoLabel = new JLabel("Elemento: " + parametros.get(3));

	    // Usar un JTextArea para la descripción (permite múltiples líneas)
	    JTextArea descripcionArea = new JTextArea("Descripción: " + parametros.get(4));
	    descripcionArea.setEditable(false); // Hacerlo no editable
	    descripcionArea.setLineWrap(true); // Permitir salto de línea
	    descripcionArea.setWrapStyleWord(true); // Evitar cortar palabras
	    descripcionArea.setBackground(panelDescripcion.getBackground()); // Mismo fondo que el panel
	    descripcionArea.setBorder(null); // Sin bordes

	    // Añadir las etiquetas al panel
	    panelDescripcion.add(nombreLabel);
	    panelDescripcion.add(costeLabel);
	    panelDescripcion.add(elementoLabel);

	    // Si la carta tiene daño, lo añadimos
	    if (parametros.size() > 4) {
	        JLabel potenciaLabel = new JLabel("Potencia: " + parametros.get(5));
	        panelDescripcion.add(potenciaLabel);
	    }

	    // Añadir un JLabel vacío para ocupar espacio
	    panelDescripcion.add(new JLabel("")); // Celda vacía
	    panelDescripcion.add(new JLabel("Efectos:"));

	    // Añadir el JTextArea para la descripción (ocupará 2 filas)
	    panelDescripcion.add(descripcionArea);

	    // Obtener los efectos y añadirlos
	    List<Efecto> efectos = Utilidades.detallesEfectos(id);

	    // Crear un panel para los efectos
	    JPanel panelEfectos = new JPanel(new GridLayout(efectos.size(), 4));

	    // Para cada efecto (modificador o marca)
	    for (Efecto efecto : efectos) {
	        JLabel estadisticaLabel = new JLabel(efecto instanceof Modificador ? ((Modificador) efecto).getEstadistica()
	                : ((Marca) efecto).getNombre());
	        JLabel porcentajeLabel = new JLabel(
	                efecto instanceof Modificador ? ((Modificador) efecto).getPorcentaje() + "%"
	                        : ((Marca) efecto).getDescripcion());
	        JLabel variacionLabel = new JLabel(
	                efecto instanceof Modificador ? ((Modificador) efecto).getVariacionPlana() + "" : "Efectos");

	        JLabel duracionLabel = new JLabel();

	        if (efecto instanceof Modificador) {
	            Modificador mod = (Modificador) efecto;
	            if (!mod.getEstadistica().equals("Bloqueo")) {
	                duracionLabel = new JLabel(Integer.parseInt(efecto.getDuracion()[0]) > 0
	                        ? efecto.getDuracion()[0] + efecto.getDuracion()[1]
	                        : ""); // Según cuál
	            }
	        } else {
	            duracionLabel = new JLabel(Integer.parseInt(efecto.getDuracion()[0]) > 0
	                    ? efecto.getDuracion()[0] + efecto.getDuracion()[1]
	                    : ""); // Según cuál
	        }

	        // Añadir las etiquetas al panel
	        panelEfectos.add(estadisticaLabel);
	        panelEfectos.add(porcentajeLabel);
	        panelEfectos.add(variacionLabel);
	        panelEfectos.add(duracionLabel);
	    }

	    // Añadir los efectos al panel de descripción
	    panelDescripcion.add(panelEfectos);

	    // Crear el label principal para la descripción de la carta
	    combate.descripcionCartaLabel = new JPanel();
	    combate.descripcionCartaLabel.setLayout(new BorderLayout());
	    combate.descripcionCartaLabel.add(panelDescripcion, BorderLayout.CENTER);

	    // Ajustar las dimensiones del label de descripción
	    combate.descripcionCartaLabel.setBounds(Combate.ancho / 4 + Combate.ancho / 16, Combate.alto / 4,
	            Combate.ancho / 2, Combate.alto / 2); // Aumentar la altura
	    combate.descripcionCartaLabel.setOpaque(true);

	    // Añadir el label a la interfaz
	    combate.add(combate.descripcionCartaLabel);
	    combate.setComponentZOrder(combate.descripcionCartaLabel, 0);
	    combate.descripcionCartaLabel.setBackground(Color.RED);

	    // Habilitar el botón de "Usar Carta"
	    combate.usarCartaButton.setEnabled(true);

	    // Deshabilitar el botón de "Finalizar Turno"
	    combate.finTurnoButton.setEnabled(false);
	    combate.revalidate();
	    combate.repaint();
	}

	/*
	 * public static void seleccionarCarta2(Carta carta, int posicion, String id) {
	 * if (combate.descripcionCartaLabel != null) {
	 * combate.remove(combate.descripcionCartaLabel); // Eliminar la descripción
	 * anterior }
	 * 
	 * combate.cartaActiva = posicion; Combate.cartaSeleccionada = true;
	 * 
	 * // Obtener los parámetros de la carta List<String> parametros =
	 * Utilidades.parametrosCarta(id); // Usamos el método que nos diste
	 * 
	 * // Crear la descripción de la carta String descripcion = "<html>";
	 * descripcion += "<div style='text-align: left;'>" + "<strong>Coste:</strong> "
	 * + parametros.get(2) + "<br>" + "<strong>Nombre:</strong> " +
	 * parametros.get(0) + "<br>" + "<strong>Elemento:</strong> " +
	 * parametros.get(3) + "<br>" + "<strong>Descripción:</strong> " +
	 * parametros.get(4) + "<br>";
	 * 
	 * // Si la carta tiene daño, lo añadimos if (parametros.size() > 4) {
	 * descripcion += "<strong>Potencia:</strong> " + parametros.get(5) + "<br>"; }
	 * 
	 * descripcion += "</div>";
	 * 
	 * // Si tiene efectos, los añadimos a la derecha if (parametros.size() > 5) {
	 * descripcion += "<div style='text-align: right;'>" +
	 * "<strong>Efectos:</strong> " /* + parametros[5]
	 *//*
		 * + "</div>"; }
		 * 
		 * descripcion += "</html>";
		 * 
		 * // Establecer la descripción en el label combate.descripcionCartaLabel = new
		 * JLabel(descripcion, SwingConstants.CENTER);
		 * combate.descripcionCartaLabel.setBounds(Combate.ancho / 4 + Combate.ancho /
		 * 16, Combate.alto / 4, Combate.ancho / 2, Combate.alto / 3);
		 * //combate.descripcionCartaLabel.setOpaque(true);
		 * 
		 * // Habilitar el botón de "Usar Carta"
		 * combate.usarCartaButton.setEnabled(true); }
		 * 
		 */

	// Método para deseleccionar la carta
	public void deseleccionarCarta() {
		Combate.cartaSeleccionada = false;
		combate.remove(combate.descripcionCartaLabel);
		combate.repaint();
		combate.usarCartaButton.setEnabled(false);
		combate.finTurnoButton.setEnabled(true);

	}

	public boolean usarCarta(String id) {
		Enemigo enemigo = Partida.enemigoActual;
		Personaje personaje = Partida.personaje;
		List<String> carta = Utilidades.parametrosCarta(id);

		if (personaje.getEnergiaRestante() < Integer.parseInt(carta.get(2))) {
			return false;
		}

		List<Efecto> efectos = Utilidades.detallesEfectos(id);
		int dano = 0;

		// Si objetivo = 0, el objetivo es el propio jugador
		// Las cartas con efecto sobre ti mismo sólo son de estado
		if (Integer.parseInt(carta.get(7)) == 0) {
			if (efectos.size() > 0) {
				for (int i = 0; i < efectos.size(); i++) {
					personaje.agregarEfecto(combate, efectos.get(i));
					combate.pintaEfectos(true);
				}
			}
			// Si el objetivo == 1, el objetivo es el enemigo
		} else {

			if (Integer.parseInt(carta.get(5)) > 0) {
				dano = calcularDano(Integer.parseInt(carta.get(5)));
				danar(false, enemigo, dano);
				controladorEfectosAtaque(true);
			}

			if (efectos.size() > 0) {
				for (int i = 0; i < efectos.size(); i++) {
					enemigo.agregarEfecto(efectos.get(i));
					combate.pintaEfectos(false);
				}
			}

		}

		personaje.setEnergiaRestante(personaje.getEnergiaRestante() - Integer.parseInt(carta.get(2)));
		controladorEfectosAccion(true);
		cartas.moverCartaADescartes(combate, combate.cartaActiva);
		
		if (enemigo.getVidaRestante() == 0) {
			victoria();
			Partida.personaje.setEnergiaRestante(Partida.personaje.getEnergia());
			cartas.reiniciaMazo();
			ControladorPartida.terminaCombate();
			
		}
		
		return true;
	}

	// El turno del enemigo empieza cuando el jugador acaba el turno y termina
	// después de haber ejecutado el ataque y haber elegido el siguiente
	private void turnoEnemigo() {

		int cartasTotales = combate.cartasEnMano;

		for (int i = 0; i < cartasTotales; i++) {
			cartas.moverCartaADescartes(combate, 0);
		}

		combate.actualizarMano();

		JOptionPane.showMessageDialog(combate,
				Partida.enemigoActual.getNombre() + " usó " + siguienteAtaque.getNombre());

		Enemigo enemigo = Partida.enemigoActual;
		Personaje personaje = Partida.personaje;

		int dano = 0;

		// Si objetivo = 0, el objetivo es el propio enemigo
		if (Integer.parseInt(siguienteAtaque.getObjetivo()) == 0) {
			if (siguienteAtaque.getEfectos().size() > 0) {

				for (int i = 0; i < siguienteAtaque.getEfectos().size(); i++) {
					enemigo.agregarEfecto(siguienteAtaque.getEfectos().get(i));
					combate.pintaEfectos(false);

					if (siguienteAtaque.getEfectos().get(i) instanceof Modificador) {
						Modificador mod = (Modificador) siguienteAtaque.getEfectos().get(i);
						JOptionPane.showMessageDialog(combate,
								"¡" + enemigo.getNombre() + " se infligió una subida de " + mod.getEstadistica() + "!");

					} else {
						Marca mar = (Marca) siguienteAtaque.getEfectos().get(i);
						JOptionPane.showMessageDialog(combate,
								"¡" + enemigo.getNombre() + " se infligió " + mar.getNombre() + "!");
					}

				}
			}
			// Si el objetivo == 1, el objetivo es el jugador
		} else {

			if (siguienteAtaque.getPotencia() > 0) {
				dano = calcularDano(siguienteAtaque.getPotencia());
				JOptionPane.showMessageDialog(combate,
						"¡" + enemigo.getNombre() + " te infligió " + dano + " de daño!");
				danar(true, personaje, dano);
				combate.pintaEfectos(false);
			}

			if (siguienteAtaque.getEfectos().size() > 0) {
				for (int i = 0; i < siguienteAtaque.getEfectos().size(); i++) {
					personaje.agregarEfecto(combate, siguienteAtaque.getEfectos().get(i));
					combate.pintaEfectos(false);
					if (siguienteAtaque.getEfectos().get(i) instanceof Modificador) {
						Modificador mod = (Modificador) siguienteAtaque.getEfectos().get(i);
						JOptionPane.showMessageDialog(combate,
								"¡" + enemigo.getNombre() + " te infligió una bajada de " + mod.getEstadistica() + "!");
					} else {
						Marca mar = (Marca) siguienteAtaque.getEfectos().get(i);
						JOptionPane.showMessageDialog(combate,
								"¡" + enemigo.getNombre() + " te infligió " + mar.getNombre() + "!");
					}
				}
			}
		}

		controladorEfectosAccion(false);
		combate.pintaEfectos(false);

		nuevoTurno();
	}

	private void eleccionAtaque() {

		String ruta = "C:\\Users\\alegu\\git\\repository\\com.efailfull\\src\\main\\resources\\estados\\";

		siguienteAtaque = new Ataque(Partida.enemigoActual.getAtaques().get(decideAtaque(true)));

		if (siguienteAtaque.getObjetivo().equals("1")) {

			// Estado negativo al jugador
			if (siguienteAtaque.getPotencia() == 0) {
				ruta = ruta.concat("Buffratedown.png");

				// Ataque al jugador
			} else {
				ruta = ruta.concat("Attack.png");
			}

			// Estado positivo al enemigo
		} else {
			ruta = ruta.concat("Buffrateup.png");

		}

		ImageIcon icono = new ImageIcon(
				new ImageIcon(ruta).getImage().getScaledInstance(Menu.alto / 32, Menu.alto / 32, Image.SCALE_SMOOTH));

		combate.intencionEnemigoLabel.setText(null);
		combate.intencionEnemigoLabel.setIcon(icono);
		/*
		 * combate.intencionEnemigoLabel.setBounds(combate.margenAncho + Menu.ancho / 2,
		 * combate.margenAlto + 2 * Menu.alto / 32 + 2 * Menu.alto / 64, Menu.ancho / 8,
		 * Menu.alto / 24); combate.intencionEnemigoLabel.setOpaque(true);
		 * combate.intencionEnemigoLabel.setBackground(null);
		 */
		combate.intencionEnemigoLabel.setToolTipText(
				(siguienteAtaque.getObjetivo().equals("0")) ? "El enemigo planea aplicarse un estado positivo"
						: ((siguienteAtaque.getPotencia() == 0) ? "El enemigo te aplicará un estado negativo"
								: "El enemigo te atacará con una potencia de " + siguienteAtaque.getPotencia()));

	}

	private static int decideAtaque(Boolean normal) {
		if (normal) {
			int roll = (int) (Math.random() * 100) + 1;

			for (int i = 0; i < Partida.enemigoActual.getAtaques().size() || roll != 0; i++) {
				if (roll > Partida.enemigoActual.getAtaques().get(i).getProbabilidad()) {
					roll -= Partida.enemigoActual.getAtaques().get(i).getProbabilidad();
				} else {
					return i;
				}
			}
		}
		return 1;
	}

	public void limpiaEfectos(boolean esJugador) {
		List<Efecto> efectos = esJugador ? Partida.personaje.getEfectos() : Partida.enemigoActual.getEfectos();

		Iterator<Efecto> iterator = efectos.iterator();
		while (iterator.hasNext()) {
			Efecto efecto = iterator.next();
			if (efecto.getDuracionTurnos() == 0 && efecto.getDuracionAtaques() == 0
					&& efecto.getDuracionAcciones() == 0) {
				iterator.remove();
			}
		}

		combate.pintaEfectos(esJugador);
	}

	public static void controladorEfectosTurno(boolean esJugador) {
		// Seleccionar la lista de efectos según el booleano
		List<Efecto> efectos = esJugador ? Partida.personaje.getEfectos() : Partida.enemigoActual.getEfectos();

		for (Efecto efecto : efectos) {

			if (efecto.getDuracionTurnos() > 0) {
				efecto.setDuracionTurnos(efecto.getDuracionTurnos() - 1);
			}

		}

	}

	public static void controladorEfectosAtaque(boolean esJugador) {
		// Seleccionar la lista de efectos según el booleano
		List<Efecto> efectos = esJugador ? Partida.personaje.getEfectos() : Partida.enemigoActual.getEfectos();

		for (Efecto efecto : efectos) {

			if (efecto.getDuracionAtaques() > 0) {
				efecto.setDuracionAtaques(efecto.getDuracionAtaques() - 1);
			}

		}

	}

	public static void controladorEfectosAccion(boolean esJugador) {
		// Seleccionar la lista de efectos según el booleano
		List<Efecto> efectos = esJugador ? Partida.personaje.getEfectos() : Partida.enemigoActual.getEfectos();

		for (Efecto efecto : efectos) {

			if (efecto.getDuracionAcciones() > 0) {
				efecto.setDuracionAcciones(efecto.getDuracionAcciones() - 1);
			}

		}

	}

	public static int calcularDano(int potencia) {
		return potencia;
	}

	public void danar(boolean esJugador, Object objetivo, int dano) {
		int bloqueo, vidaRestante, vidaMaxima;
		JLabel bloqueoLabel, saludLabel;

		if (esJugador) {
			Personaje jugador = (Personaje) objetivo;
			bloqueo = jugador.getBloqueo();
			vidaRestante = jugador.getVidaRestante();
			vidaMaxima = jugador.getVida();
			bloqueoLabel = combate.bloqueoJugadorLabel;
			saludLabel = combate.saludJugadorLabel;
		} else {
			Enemigo enemigo = (Enemigo) objetivo;
			bloqueo = enemigo.getBloqueo();
			vidaRestante = enemigo.getVidaRestante();
			vidaMaxima = enemigo.getVida();
			bloqueoLabel = combate.bloqueoEnemigoLabel;
			saludLabel = combate.saludEnemigoLabel;
		}

		// Calcular el daño considerando el bloqueo
		if (bloqueo > 0) {
			if (bloqueo > dano) {
				bloqueo -= dano;
			} else {
				int danoAux = dano - bloqueo;
				bloqueo = 0;
				vidaRestante -= danoAux;
			}
		} else {
			vidaRestante -= dano;
		}

		// Asegurarse de que la vida restante no sea negativa
		if (vidaRestante < 0) {
			vidaRestante = 0;
		}

		// Actualizar los valores del objetivo
		if (esJugador) {
			Personaje jugador = (Personaje) objetivo;
			jugador.setBloqueo(bloqueo);
			jugador.setVidaRestante(vidaRestante);
		} else {
			Enemigo enemigo = (Enemigo) objetivo;
			enemigo.setBloqueo(bloqueo);
			enemigo.setVidaRestante(vidaRestante);
		}

		// Actualizar las etiquetas de la interfaz
		bloqueoLabel.setText((bloqueo == 0) ? "" : String.valueOf(bloqueo));
		saludLabel.setText(vidaRestante + "/" + vidaMaxima);
		bloqueoLabel.repaint();
		saludLabel.repaint();
	}
	
	public static List<Integer> eligeRecompensa() {
        List<Integer> recompensas = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < 3; i++) {
            int id = obtenerIdCarta(random);
            recompensas.add(id);
        }
        
        return recompensas;
    }

    private static int obtenerIdCarta(Random random) {
        int casilla = Mapa.casilla;
        double probabilidad = random.nextDouble() * 100; // Número entre 0 y 100

        
        /*
         * Eleccion de recompensa en base a la casilla donde el jugador está.
         * Si casilla es inferior a 5, la probabilidad de que cada id será:
         * Si la casilla es inferior a 5:
			id del 4 al 90: 82%
			id del 91 al 150: 16%
			id del 151 al 199: 2%
			
			Si casilla es entre 5 y 10, 5 incluido:
			id del 4 al 90: 70%
			id del 91 al 150: 25%
			id del 151 al 199: 5%
			
			Si casilla es igual o superior a 10:
			id del 4 al 90: 55%
			id del 91 al 150: 30%
			id del 151 al 199: 15%
			
			Se entiende que las cartas de mayor valor tendrán un id más alto.
         */
        if (casilla < 5) {
        	if (probabilidad < 100) return random.nextInt(4) + 1 +  (Partida.personajeSeleccionado - 1) * 200; // 4 - 90
            if (probabilidad < 82) return random.nextInt(87) + 4 + (Partida.personajeSeleccionado - 1) * 200; // 4 - 90
            if (probabilidad < 98) return random.nextInt(60) + 91 + (Partida.personajeSeleccionado - 1) * 200; // 91 - 150
            return random.nextInt(49) + 151 + (Partida.personajeSeleccionado - 1) * 200; // 151 - 199
        } 
        
        if (casilla <= 10) {
            if (probabilidad < 70) return random.nextInt(87) + 4 + (Partida.personajeSeleccionado - 1) * 200; // 4 - 90
            if (probabilidad < 95) return random.nextInt(60) + 91 + (Partida.personajeSeleccionado - 1) * 200; // 91 - 150
            return random.nextInt(49) + 151 + (Partida.personajeSeleccionado - 1) * 200; // 151 - 199
        }
        
        if (probabilidad < 55) return random.nextInt(87) + 4; // 4 - 90
        if (probabilidad < 85) return random.nextInt(60) + 91; // 91 - 150
        return random.nextInt(49) + 151; // 151 - 199
    }
	
	private static void victoria() {

		new Recompensa(Menu.menu, eligeRecompensa()).setVisible(true);
		
	}
	
	

}
