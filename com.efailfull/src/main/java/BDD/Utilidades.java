package BDD;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import Controlador.ControladorPartida;
import Modelo.Partida;
import Objetos.Carta;
import Objetos.Efecto;
import Objetos.Marca;
import Objetos.Modificador;
import Vista.Login;
import Vista.Mapa;

public class Utilidades {

	public static boolean isDebug = true;

	public static boolean existeEnBDD(String query) {

		Statement stmt = null;
		boolean existe = false;

		try {

			stmt = Conexion.conn.createStatement();

			ResultSet rs = stmt.executeQuery(query);

			existe = rs.next();

			rs.close();
			stmt.close();

			return existe;

		} catch (SQLException e) {
			Conexion.mensajeError(e);
			return false;

		}

	}

	public static List<String> consulta(String sql, String parametro) {

		PreparedStatement statement = null;
		ResultSet resultSet = null;
		List<String> consulta = new ArrayList<>();

		try {

			// Preparar la declaración
			statement = Conexion.conn.prepareStatement(sql);

			statement.setString(1, parametro);

			// Ejecutar la consulta
			resultSet = statement.executeQuery();

			ResultSetMetaData metaData = resultSet.getMetaData();
			int columnCount = metaData.getColumnCount();

			while (resultSet.next()) {
				for (int i = 1; i <= columnCount; i++) {
					consulta.add(resultSet.getString(i));
				}
			}

			return consulta;

		} catch (Exception e) {
			Conexion.mensajeError(e);
		}

		return null;
	}

	public static List<String> consulta(String sql, List<String> parametros) {

		PreparedStatement statement = null;
		ResultSet resultSet = null;
		List<String> consulta = new ArrayList<>();

		try {

			// Preparar la declaración
			statement = Conexion.conn.prepareStatement(sql);

			for (int i = 0; i < parametros.size(); i++) {

				statement.setString(i + 1, parametros.get(i));
			}

			// Ejecutar la consulta
			resultSet = statement.executeQuery();

			ResultSetMetaData metaData = resultSet.getMetaData();
			int columnCount = metaData.getColumnCount();

			while (resultSet.next()) {
				for (int i = 1; i <= columnCount; i++) {
					consulta.add(resultSet.getString(i));
				}
			}

			return consulta;

		} catch (Exception e) {
			Conexion.mensajeError(e);
		}

		return null;
	}

	public static String consultaDatoUnico(String sql, List<String> parametros) {

		PreparedStatement statement = null;
		ResultSet resultSet = null;

		try {
			// Preparar la declaración
			statement = Conexion.conn.prepareStatement(sql);

			for (int i = 0; i < parametros.size(); i++) {

				statement.setString(i + 1, parametros.get(i));
			}

			// Ejecutar la consulta
			resultSet = statement.executeQuery();

			String consulta = "Err";

			if (resultSet.next())
				consulta = resultSet.getString(1);

			// Cerrar recursos
			if (resultSet != null) {
				resultSet.close();
			}
			if (statement != null) {
				statement.close();
			}

			return consulta;

		} catch (Exception e) {
			Conexion.mensajeError(e);
		}

		return "Err";
	}

	public static String consultaDatoUnico(String sql, String parametro) {

		PreparedStatement statement = null;
		ResultSet resultSet = null;

		try {
			// Preparar la declaración
			statement = Conexion.conn.prepareStatement(sql);

			statement.setString(1, parametro);

			// Ejecutar la consulta
			resultSet = statement.executeQuery();

			String consulta = "Err";

			if (resultSet.next())
				consulta = resultSet.getString(1);

			// Cerrar recursos
			if (resultSet != null) {
				resultSet.close();
			}
			if (statement != null) {
				statement.close();
			}

			return consulta;

		} catch (Exception e) {
			Conexion.mensajeError(e);
		}

		return "Err";
	}

	public static int buscaCarta(String nombre) {
		return Integer.parseInt(consultaDatoUnico("SELECT Id FROM Carta WHERE Nombre = ?", nombre));
	}

	public static String buscaCarta(int id) {
		return consultaDatoUnico("SELECT Nombre FROM Carta WHERE Id = ?", id + "");
	}

	// TODO: Este metodo debe retornar los efectos de una carta y el tipo de los
	// mismos dado el id de la carta
	public static List<String> parametrosCarta(String id) {

		PreparedStatement statement = null;
		ResultSet resultSet = null;
		List<String> carta = new ArrayList<>();

		String sql = "SELECT nombre, clase, coste, elemento, descripcion, potencia, tipo, objetivo FROM Carta WHERE id = ?";

		/*
		 * CONSULTA PARA RECOGER LOS NOMBRES --BASICOS-- DE LOS EFECTOS DE UNA CARTA
		 * SABIENDO SU ID
		 * 
		 * String sql2 =
		 * "SELECT e.Nombre FROM efecto e JOIN modificadores m JOIN efectosCartas ec " +
		 * "ON e.id = m.idEfecto AND m.id = ec.idModificador " + "WHERE ec.idCarta = ?";
		 * 
		 */
		try {

			// Preparar la declaración
			statement = Conexion.conn.prepareStatement(sql);

			statement.setString(1, id);

			// Ejecutar la consulta
			resultSet = statement.executeQuery();

			ResultSetMetaData metaData = resultSet.getMetaData();
			int columnCount = metaData.getColumnCount();

			while (resultSet.next()) {
				for (int i = 1; i <= columnCount; i++) {
					carta.add(resultSet.getString(i));
				}
			}

			return carta;

		} catch (Exception e) {
			Conexion.mensajeError(e);
		}

		return null;
	}

	public static String nombreEfectosCarta(String id) {

		PreparedStatement statement = null;
		ResultSet resultSet = null;
		List<String> modificadores = new ArrayList<>();

		String sql = "SELECT e.Nombre FROM efecto e JOIN modificadores m JOIN efectosCartas ec "
				+ "ON e.id = m.idEfecto AND e.id = ec.idEfecto " + "WHERE ec.idCarta = ?";

		try {

			// Preparar la declaración
			statement = Conexion.conn.prepareStatement(sql);

			statement.setString(1, id);

			// Ejecutar la consulta
			resultSet = statement.executeQuery();

			ResultSetMetaData metaData = resultSet.getMetaData();
			int columnCount = metaData.getColumnCount();

			while (resultSet.next()) {
				for (int i = 1; i <= columnCount; i++) {
					modificadores.add(resultSet.getString(i));
				}
			}

			String modificadoresString = String.join(", ", modificadores);

			return modificadoresString;

		} catch (Exception e) {
			Conexion.mensajeError(e);
		}

		return null;
	}

	public static List<Efecto> detallesEfectos(String idCarta) {
		PreparedStatement statementModificadores = null;
		PreparedStatement statementMarcas = null;
		ResultSet resultSetModificadores = null;
		ResultSet resultSetMarcas = null;
		List<Efecto> detalles = new ArrayList<>();

		// Consultas SQL
		String sqlModificadores = "SELECT m.Estadistica, m.porcentaje, m.variacionPlana, ec.DuracionTurnos, ec.DuracionAtaques, ec.DuracionAcciones "
				+ "FROM efecto e JOIN modificadores m JOIN efectosCartas ec "
				+ "ON e.id = m.idEfecto AND e.id = ec.idEfecto " + "WHERE ec.idCarta = ?";

		String sqlMarcas = "SELECT ma.Id, ma.Nombre, ma.Descripcion, ec.DuracionTurnos, ec.DuracionAtaques, ec.DuracionAcciones "
				+ "FROM efecto e JOIN marcas ma JOIN efectosCartas ec "
				+ "ON e.id = ma.idEfecto AND e.id = ec.idEfecto " + "WHERE ec.idCarta = ?";

		try {
			// Consultar modificadores
			statementModificadores = Conexion.conn.prepareStatement(sqlModificadores);
			statementModificadores.setString(1, idCarta);
			resultSetModificadores = statementModificadores.executeQuery();

			while (resultSetModificadores.next()) {
				Modificador mod = new Modificador(resultSetModificadores.getString("Estadistica"),
						resultSetModificadores.getDouble("porcentaje"),
						resultSetModificadores.getDouble("variacionPlana"),
						resultSetModificadores.getInt("DuracionTurnos"),
						resultSetModificadores.getInt("DuracionAtaques"),
						resultSetModificadores.getInt("DuracionAcciones"));
				detalles.add(mod);
			}

			// Consultar marcas
			statementMarcas = Conexion.conn.prepareStatement(sqlMarcas);
			statementMarcas.setString(1, idCarta);
			resultSetMarcas = statementMarcas.executeQuery();

			while (resultSetMarcas.next()) {
				Marca marca = new Marca(resultSetMarcas.getString("Id"), resultSetMarcas.getString("Nombre"),
						resultSetMarcas.getString("Descripcion"), resultSetMarcas.getInt("DuracionTurnos"),
						resultSetMarcas.getInt("DuracionAtaques"), resultSetMarcas.getInt("DuracionAcciones"));

				// Obtener efectos asociados a la marca
				List<Efecto> efectos = detallesEfectosDeMarcas(marca);
				for (Efecto efecto : efectos) {
					marca.agregarEfecto(efecto);
				}
				detalles.add(marca);
			}

		} catch (Exception e) {
			Conexion.mensajeError(e);
		} finally {
			try {
				if (resultSetModificadores != null)
					resultSetModificadores.close();
				if (statementModificadores != null)
					statementModificadores.close();
				if (resultSetMarcas != null)
					resultSetMarcas.close();
				if (statementMarcas != null)
					statementMarcas.close();
			} catch (Exception e) {
				Conexion.mensajeError(e);
			}
		}

		return detalles;
	}
	
	public static List<Efecto> detallesEfectosAtaques(String idAtaque) {
		PreparedStatement statementModificadores = null;
		PreparedStatement statementMarcas = null;
		ResultSet resultSetModificadores = null;
		ResultSet resultSetMarcas = null;
		List<Efecto> detalles = new ArrayList<>();

		// Consultas SQL
		String sqlModificadores = "SELECT m.Estadistica, m.porcentaje, m.variacionPlana, ea.DuracionTurnos, ea.DuracionAtaques, ea.DuracionAcciones "
				+ "FROM efecto e JOIN modificadores m JOIN efectosAtaques ea "
				+ "ON e.id = m.idEfecto AND e.id = ea.idEfecto " + "WHERE ea.idAtaque = ?";

		String sqlMarcas = "SELECT ma.Id, ma.Nombre, ma.Descripcion, ea.DuracionTurnos, ea.DuracionAtaques, ea.DuracionAcciones "
				+ "FROM efecto e JOIN marcas ma JOIN efectosAtaques ea "
				+ "ON e.id = ma.idEfecto AND e.id = ea.idEfecto " + "WHERE ea.idAtaque = ?";

		try {
			// Consultar modificadores
			statementModificadores = Conexion.conn.prepareStatement(sqlModificadores);
			statementModificadores.setString(1, idAtaque);
			resultSetModificadores = statementModificadores.executeQuery();

			while (resultSetModificadores.next()) {
				Modificador mod = new Modificador(resultSetModificadores.getString("Estadistica"),
						resultSetModificadores.getDouble("porcentaje"),
						resultSetModificadores.getDouble("variacionPlana"),
						resultSetModificadores.getInt("DuracionTurnos"),
						resultSetModificadores.getInt("DuracionAtaques"),
						resultSetModificadores.getInt("DuracionAcciones"));
				detalles.add(mod);
			}

			// Consultar marcas
			statementMarcas = Conexion.conn.prepareStatement(sqlMarcas);
			statementMarcas.setString(1, idAtaque);
			resultSetMarcas = statementMarcas.executeQuery();

			while (resultSetMarcas.next()) {
				Marca marca = new Marca(resultSetMarcas.getString("Id"), resultSetMarcas.getString("Nombre"),
						resultSetMarcas.getString("Descripcion"), resultSetMarcas.getInt("DuracionTurnos"),
						resultSetMarcas.getInt("DuracionAtaques"), resultSetMarcas.getInt("DuracionAcciones"));

				// Obtener efectos asociados a la marca
				List<Efecto> efectos = detallesEfectosDeMarcas(marca);
				for (Efecto efecto : efectos) {
					marca.agregarEfecto(efecto);
				}
				detalles.add(marca);
			}

		} catch (Exception e) {
			Conexion.mensajeError(e);
		} finally {
			try {
				if (resultSetModificadores != null)
					resultSetModificadores.close();
				if (statementModificadores != null)
					statementModificadores.close();
				if (resultSetMarcas != null)
					resultSetMarcas.close();
				if (statementMarcas != null)
					statementMarcas.close();
			} catch (Exception e) {
				Conexion.mensajeError(e);
			}
		}

		return detalles;
	}

	private static List<Efecto> detallesEfectosDeMarcas(Marca marca) {
	    List<Efecto> efectos = new ArrayList<>();
	    String sql = "SELECT m.Estadistica, m.porcentaje, m.variacionPlana "
	               + "FROM modificadores m JOIN ModificadoresMarcas mm "
	               + "ON m.id = mm.idModificador "
	               + "WHERE mm.idMarca = ?";

	    try (PreparedStatement statement = Conexion.conn.prepareStatement(sql)) {
	        statement.setString(1, marca.getId());
	        try (ResultSet resultSet = statement.executeQuery()) {
	            while (resultSet.next()) {
	                Efecto modificador = new Modificador(
	                    resultSet.getString("Estadistica"),
	                    resultSet.getDouble("porcentaje"),
	                    resultSet.getDouble("variacionPlana"),
	                    marca.getDuracionTurnos(),
	                    marca.getDuracionAtaques(),
	                    marca.getDuracionAcciones()
	                );
	                efectos.add(modificador);
	            }
	        }
	    } catch (Exception e) {
	        Conexion.mensajeError(e);
	    }

	    return efectos; // Devuelve una lista vacía en caso de error
	}


	public static Carta devuelveCarta(String id, int ancho, int alto) {

		List<String> datosCarta = Utilidades.parametrosCarta(id);

		return new Carta(ancho, alto, datosCarta.get(0), datosCarta.get(1), datosCarta.get(2), datosCarta.get(3),
				datosCarta.get(4), datosCarta.get(5), datosCarta.get(6), Utilidades.nombreEfectosCarta(id));

	}

	public static void incrementarCasilla(int idPartida) {
	    String sql = "UPDATE Partidas SET casilla = casilla + 1 WHERE Id = ?";

	    try (PreparedStatement stmt = Conexion.conn.prepareStatement(sql)) {
	        stmt.setInt(1, idPartida);
	        stmt.executeUpdate();

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	
	public static void guardarPartida() {
	    String actualizarPartidaSQL = "UPDATE Partidas SET casilla = ? WHERE Id = ?";
	    String actualizarPersonajeSQL = "UPDATE PersonajePartida SET Nombre = ?, Nivel = ?, Energia = ?, EnergiaRestante = ?, " +
	            "Vida = ?, VidaRestante = ?, Ataque = ?, Defensa = ?, Bloqueo = ?, ProbabilidadCritico = ?, DanoCritico = ? " +
	            "WHERE IdPartida = ?";
	    String borrarMazoSQL = "DELETE FROM Mazos WHERE IdPartida = ?";
	    String insertarMazoSQL = "INSERT INTO Mazos (IdPartida, IdCarta) VALUES (?, ?)";

	    try (PreparedStatement stmtPartida = Conexion.conn.prepareStatement(actualizarPartidaSQL);
	         PreparedStatement stmtPersonaje = Conexion.conn.prepareStatement(actualizarPersonajeSQL);
	         PreparedStatement stmtBorrarMazo = Conexion.conn.prepareStatement(borrarMazoSQL);
	         PreparedStatement stmtInsertarMazo = Conexion.conn.prepareStatement(insertarMazoSQL)) {

	        // Actualizar la casilla en la Partida
	        stmtPartida.setInt(1, Mapa.casilla);
	        stmtPartida.setInt(2, ControladorPartida.idPartida);
	        stmtPartida.executeUpdate();

	        // Actualizar datos del personaje
	        stmtPersonaje.setString(1, Login.usuarioNombre);
	        stmtPersonaje.setInt(2, Partida.getPersonaje().getNivel());
	        stmtPersonaje.setInt(3, Partida.getPersonaje().getEnergia());
	        stmtPersonaje.setInt(4, Partida.getPersonaje().getEnergiaRestante());
	        stmtPersonaje.setInt(5, Partida.getPersonaje().getVida());
	        stmtPersonaje.setInt(6, Partida.getPersonaje().getVidaRestante());
	        stmtPersonaje.setInt(7, Partida.getPersonaje().getAtaque());
	        stmtPersonaje.setInt(8, Partida.getPersonaje().getDefensa());
	        stmtPersonaje.setInt(9, Partida.getPersonaje().getBloqueo());
	        stmtPersonaje.setBigDecimal(10, BigDecimal.valueOf(Partida.getPersonaje().getProbabilidadCritico()));
	        stmtPersonaje.setBigDecimal(11, BigDecimal.valueOf(Partida.getPersonaje().getDanoCritico()));
	        stmtPersonaje.setInt(12, ControladorPartida.idPartida);
	        stmtPersonaje.executeUpdate();

	        // Borrar el mazo actual
	        stmtBorrarMazo.setInt(1, ControladorPartida.idPartida);
	        stmtBorrarMazo.executeUpdate();

	        // Insertar las cartas del mazo actual
	        for (String idCarta : Partida.mazo) {
	            stmtInsertarMazo.setInt(1, ControladorPartida.idPartida);
	            stmtInsertarMazo.setInt(2, Integer.parseInt(idCarta));
	            stmtInsertarMazo.executeUpdate();
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	
	public static void main(String[] args) {
		ControladorPartida.setupInicial();
	}
}
