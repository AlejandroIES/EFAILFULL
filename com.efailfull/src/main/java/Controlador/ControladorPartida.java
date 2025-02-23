package Controlador;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import BDD.Conexion;
import BDD.Utilidades;
import Modelo.Partida;
import Objetos.Personaje;
import Vista.Combate;
import Vista.Login;
import Vista.Mapa;
import Vista.NuevaPartida;

public class ControladorPartida {

	public static List<String> statsBase;
	public static int idPartida;

	/**
	 * setupInicial() tiene dos funciones principales. <br>
	 * En primer lugar, crea en la base de datos la fila correspondiente al
	 * personaje, jugador y partida que se hayan elegido.<br>
	 * La fila quedaría de la siguiente manera:<br>
	 * <br>
	 * 
	 * Id:1 <br>
	 * IdPartida:1 <br>
	 * IdPersonaje:1 <br>
	 * Nombre:Alexio <br>
	 * Nivel:1 <br>
	 * Energía:3 <br>
	 * EnergíaRestante:3 <br>
	 * Vida:45 <br>
	 * VidaRestante:45 <br>
	 * Ataque:32 <br>
	 * Defensa:27 <br>
	 * Bloqueo:0 <br>
	 * ProbabilidadCritico:5.00 <br>
	 * DanioCritico:50.00 <br>
	 * <br>
	 *
	 * Además, inicializa con esos datos el objeto Partida.personaje, que
	 * representará al jugador en los combates.<br>
	 * Esto lo hace mediante el método setupPersonaje().
	 */
	public static void setupInicial() {

		PreparedStatement statement;

		if (NuevaPartida.personajeSeleccionado == -1) {
			Conexion.conectar();
			Login.usuario = 1;
			Login.usuarioNombre = "Alexio";
			NuevaPartida.personajeSeleccionado = 1;
		}

		String sqlPar = "INSERT INTO partidas (IdJugador, Casilla) VALUES (" + Login.usuario + ", 1)";

		try {
			statement = Conexion.conn.prepareStatement(sqlPar);
			statement.executeUpdate();
		} catch (SQLException e) {
			Conexion.mensajeError(e);
		}

		String sqlStats = "SELECT * FROM PERSONAJE WHERE ID = ?";
		List<String> datos;

		datos = Utilidades.consulta(sqlStats, NuevaPartida.personajeSeleccionado + "");
		statsBase = new ArrayList<>(datos.subList(4, 8));

		List<String> lista = new ArrayList<>();
		lista.add(Login.usuario + "");
		idPartida = Integer.parseInt(Utilidades
				.consultaDatoUnico("SELECT * FROM Partidas WHERE IdJugador = ? ORDER BY Id DESC LIMIT 1", lista));

		setupPersonaje();
		ajuste(5, statsBase);

		String sqlPJ = "INSERT INTO "
				+ "PersonajePartida (IdPartida, IdPersonaje, Nombre, Nivel, Energia, EnergiaRestante, Vida, VidaRestante, Ataque, Defensa, ProbabilidadCritico, DanoCritico) "
				+ "VALUES (" + idPartida + ", " + NuevaPartida.personajeSeleccionado + ", \" " + Login.usuarioNombre
				+ "\", 1, " + datos.get(2) + ", " + datos.get(3) + ", " + Partida.personaje.getVida() + ", "
				+ Partida.personaje.getVidaRestante() + "" + ", " + Partida.personaje.getAtaque() + ", "
				+ Partida.personaje.getDefensa() + ", " + datos.get(9) + ", " + datos.get(10)
				+ ")";

		boolean partidaCreada = false;

		try {
			Statement statement2;
			statement2 = Conexion.conn.createStatement();
			statement2.executeUpdate(sqlPJ);
			partidaCreada = true;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		if (partidaCreada) {
			String sqlMazo = "INSERT INTO mazos (IdPartida, IdCarta) values " + "(" + idPartida + ", "
					+ ((NuevaPartida.personajeSeleccionado - 1) * 200 + 1) + "), " + "(" + idPartida + ", "
					+ ((NuevaPartida.personajeSeleccionado - 1) * 200 + 1) + "), " + "(" + idPartida + ", "
					+ ((NuevaPartida.personajeSeleccionado - 1) * 200 + 1) + "), " + "(" + idPartida + ", "
					+ ((NuevaPartida.personajeSeleccionado - 1) * 200 + 1) + "), " + "(" + idPartida + ", "
					+ ((NuevaPartida.personajeSeleccionado - 1) * 200 + 2) + "), " + "(" + idPartida + ", "
					+ ((NuevaPartida.personajeSeleccionado - 1) * 200 + 2) + "), " + "(" + idPartida + ", "
					+ ((NuevaPartida.personajeSeleccionado - 1) * 200 + 2) + "), " + "(" + idPartida + ", "
					+ ((NuevaPartida.personajeSeleccionado - 1) * 200 + 2) + "), " + "(" + idPartida + ", "
					+ ((NuevaPartida.personajeSeleccionado - 1) * 200 + 3) + "), " + "(" + idPartida + ", "
					+ ((NuevaPartida.personajeSeleccionado - 1) * 200 + 4) + ")";

			try {
				Statement statement2;
				statement2 = Conexion.conn.createStatement();
				statement2.executeUpdate(sqlMazo);

				// CREACION DEL MAZO EN LA PARTIDA
				Partida.mazo = Utilidades.consulta("SELECT IdCarta FROM Mazos WHERE IdPartida = ?", idPartida + "");

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}

	public static void setupPersonaje() {
		String sql = "Select * from personaje where id = ?;";
		List<String> personajeBaseBDD;

		personajeBaseBDD = Utilidades.consulta(sql, NuevaPartida.personajeSeleccionado + "");

		Partida.personaje = new Personaje(50, personajeBaseBDD.get(1), Integer.parseInt(personajeBaseBDD.get(2)),
				Integer.parseInt(personajeBaseBDD.get(3)), Integer.parseInt(personajeBaseBDD.get(4)),
				Integer.parseInt(personajeBaseBDD.get(5)), Integer.parseInt(personajeBaseBDD.get(6)),
				Integer.parseInt(personajeBaseBDD.get(7)), Integer.parseInt(personajeBaseBDD.get(8)),
				Double.parseDouble(personajeBaseBDD.get(9)), Double.parseDouble(personajeBaseBDD.get(10)));

	}

	/**
	 * Ajusta los stats del personaje jugable respecto a su base en el nivel
	 * indicado.
	 * 
	 * @param nivel
	 * @param statsBase
	 */
	public static void ajuste(int nivel, List<String> statsBase) {

		Partida.personaje.setVida((nivel + 5) * Integer.parseInt(statsBase.get(0)) / 50);
		Partida.personaje.setVidaRestante((nivel + 5) * Integer.parseInt(statsBase.get(0)) / 50);
		Partida.personaje.setAtaque((nivel + 5) * Integer.parseInt(statsBase.get(2)) / 50);
		Partida.personaje.setDefensa((nivel + 5) * Integer.parseInt(statsBase.get(3)) / 50);
		Partida.personaje.setNivel(nivel);

	}

	public static void actualizarStats(List<String> statsNuevos) {

		Partida.personaje.setVida(Integer.parseInt(statsNuevos.get(0)));
		Partida.personaje.setAtaque(Integer.parseInt(statsNuevos.get(2)));
		Partida.personaje.setDefensa(Integer.parseInt(statsNuevos.get(3)));

	}

	public static void subirNivel(int nivelesSubidos, List<String> statsBase) {

		JOptionPane.showMessageDialog(null, "Vida: " + Partida.personaje.getVida() + "+"
				+ (nivelesSubidos * Integer.parseInt(statsBase.get(0)) / 50) + "\n" + "Ataque: "
				+ Partida.personaje.getAtaque() + "+" + (nivelesSubidos * Integer.parseInt(statsBase.get(2)) / 50)
				+ "\n" + "Defensa: " + Partida.personaje.getDefensa() + "+"
				+ (nivelesSubidos * Integer.parseInt(statsBase.get(3)) / 50) + "\n", "¡Subiste de nivel!", 1);

		Partida.personaje.setVida(nivelesSubidos * Integer.parseInt(statsBase.get(0)) / 50);
		Partida.personaje.setAtaque(nivelesSubidos * Integer.parseInt(statsBase.get(2)) / 50);
		Partida.personaje.setDefensa(nivelesSubidos * Integer.parseInt(statsBase.get(3)) / 50);
		Partida.personaje.curar(nivelesSubidos * Integer.parseInt(statsBase.get(0)) / 50);
		Partida.personaje.aumentaNivel(nivelesSubidos);

	}
	
	public static void terminaCombate() {
    	Partida.panelPartida.setSelectedComponent(Partida.mapaTab);
    	Partida.batallaActiva = false;
    	Partida.panelPartida.remove(Partida.combateTab);
    	Partida.combateTab = null;
    	Partida.combateTab = new Combate();
    	Partida.panelPartida.add("Combate", Partida.combateTab);
    	Partida.panelPartida.setEnabledAt(2, Partida.batallaActiva); // Deshabilitar la pestaña de Combate
    	Partida.mapaTab.avanzarCasilla();
    }

	public static void main(String[] args) {
		Conexion.conectar();
		// NuevaPartida.personajeSeleccionado = 1;
		setupInicial();
	}

	public static void guardarProgreso() {

		Utilidades.guardarPartida();
		
		//TODO: Hay una conversacion pendiente con chatgpt sobre esto
		
		//¿Qué más queda?
		/*
		 * - Cargar partida
		 * - Fórmula de daño
		 * - Meter datos
		 * - Corregir rutas de imágenes
		 * - Meter imágenes
		 * - Corregir métodos como elegir enemigo y las cartas de recompensa
		 * - Perder
		 * - Zonas de descanso
		 * - Subidas de nivel de jugador y enemigos (progresión)
		 * 
		 */
		
	}
}
