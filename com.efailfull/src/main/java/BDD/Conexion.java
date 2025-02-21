package BDD;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

public class Conexion {
	static final String URL = "jdbc:mysql://localhost:3306/Efail";
	static final String USER = "root";
	static final String PASSWORD = "toor";
	public static Connection conn = null;

	public static void mensajeError(Exception e) {
		JOptionPane.showMessageDialog(null, "ERROR: " + e.getMessage());
	}

	public static void conectar() {
		try {
			// Cargar el controlador JDBC
			Class.forName("com.mysql.cj.jdbc.Driver");

			// Establecer la conexión
			conn = DriverManager.getConnection(URL, USER, PASSWORD);

		} catch (SQLException e) {
			System.out.println("Error al conectar a la base de datos.");
			mensajeError(e);
		} catch (ClassNotFoundException e) {
			System.out.println("Controlador JDBC no encontrado.");
			mensajeError(e);
		}
	}

	public static boolean login(String usuario, String contrasena) {

		boolean existe = false, correcto = false;

		String sentencia = "SELECT * FROM Jugador WHERE Nombre = '" + usuario + "'";
		existe = Utilidades.existeEnBDD(sentencia);

		if (existe) {
			sentencia = "SELECT * FROM Jugador WHERE Nombre = '" + usuario + "' AND Contrasena = '" + contrasena
					+ "'";
			correcto = Utilidades.existeEnBDD(sentencia);
		}

		return correcto;
	}

	public static void desconcectar() {
		try {
			// Cerrar la conexión cuando hayas terminado
			if (conn != null) {
				conn.close();
				System.out.println("Conexión cerrada.");
			}
		} catch (SQLException ex) {
			System.out.println("Error al cerrar la conexión.");
			ex.printStackTrace();
		}
	}
}
