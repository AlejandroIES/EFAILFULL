package BDD;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JOptionPane;


public class Conexion2 {

	// La URL de conexión a la base de datos Oracle
	public static Connection conn;


	// Método para establecer la conexión
	/**
	 * 
	 * @param url
	 * @param user
	 * @param password
	 * @return
	 */
	/*
	public static Connection conectar(String url, String user, String password) {
		try {
			// Class.forName("oracle.jdbc.driver.OracleDriver");
			conn = DriverManager.getConnection(url, user, password);

		} catch (Exception e) {
			Conexion.mensajeError(e);
		}
		return conn;
	}

	public static boolean existeObjeto(String tabla, String codigo, String campo) {
		Boolean existe = false;

		String sql = "SELECT * FROM " + tabla + " WHERE ENTIDAD = " + Main.entidad + " AND " + campo
				+ " = ?";
		
		if (!Vista.primerCampo.equals("ENTIDAD") || tabla.equals("xent")) {
			sql = "SELECT * FROM " + tabla + " WHERE " + campo
					+ " = ?";
		}

		PreparedStatement statement = null;
		ResultSet resultSet = null;

		try {
			// Preparar la declaración
			statement = conn.prepareStatement(sql);
			// Establecer el valor del parámetro
			statement.setString(1, codigo);

			// Ejecutar la consulta
			resultSet = statement.executeQuery();

			if (resultSet.next())
				existe = true;

		} catch (Exception e) {
		} finally {
			// Cerrar recursos
			try {
				if (resultSet != null) {
					resultSet.close();
				}
				if (statement != null) {
					statement.close();
				}
			} catch (Exception e) {
			}
		}

		return existe;
	}

	public static Object consultaDatoUnico(String sql, String linea, String parametro) {

		PreparedStatement statement = null;
		ResultSet resultSet = null;

		sql = sql.substring(0, sql.indexOf("?")) + linea + sql.substring(sql.indexOf("?") + 1);

		try {

			// Preparar la declaración
			statement = conn.prepareStatement(sql);
			statement.setString(1, parametro);

			// Ejecutar la consulta
			resultSet = statement.executeQuery();

			String consulta = "Err";

			if (sql.substring(0, sql.indexOf(" ")).equalsIgnoreCase("SELECT")) {
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
			}

			return null;

		} catch (Exception e) {
			Configuracion.mensajeError(e);
		}

		return null;
	}

	public static List<String> consulta(String sql, String linea, String parametro, String tipoConsulta) {

		PreparedStatement statement = null;
		ResultSet resultSet = null;
		List<String> consulta = new ArrayList<>();
		sql = sql.substring(0, sql.indexOf("?")) + linea + sql.substring(sql.indexOf("?") + 1);

		try {

			// Preparar la declaración
			statement = conn.prepareStatement(sql);

			if (parametro.length() > 0)
				statement.setString(1, parametro);

			// Ejecutar la consulta
			resultSet = statement.executeQuery();

			ResultSetMetaData metaData = resultSet.getMetaData();
			int columnCount = metaData.getColumnCount();

			if (tipoConsulta.equals("Datos")) {
				while (resultSet.next()) {
					for (int i = 1; i <= columnCount; i++) {
						consulta.add(resultSet.getString(i));
					}
				}

			} else if (tipoConsulta.equals("Campos")) {
				while (resultSet.next()) {
					for (int i = 1; i <= columnCount; i++) {
						consulta.add(metaData.getColumnName(i));
					}
				}
			} else if (tipoConsulta.equals("Tipos")) {
				while (resultSet.next()) {
					for (int i = 1; i <= columnCount; i++) {
						consulta.add(metaData.getColumnTypeName(i));
					}
				}
			}

			return consulta;

		} catch (Exception e) {
			Configuracion.mensajeError(e);
		}

		return null;
	}

	public static void create(List<String> valores, List<String> tipos) {

		StringBuilder sql = new StringBuilder("INSERT INTO ").append(Configuracion.tabla).append(" VALUES (");
		for (int i = 0; i < valores.size(); i++) {
			// Verificar si el tipo de dato del campo requiere conversión
			if (tipos.get(i).equalsIgnoreCase("DATE")) {
				// Si el tipo es "fecha", agregar TO_DATE
				sql.append("TO_DATE(?, 'YYYY-MM-DD HH24:MI:SS')");
			} else {
				// Si no, agregar ? como de costumbre
				sql.append("?");
			}
			if (i < valores.size() - 1) {
				sql.append(", ");
			}
		}
		sql.append(")");

		try (PreparedStatement statement = conn.prepareStatement(sql.toString())) {
			// Establecer los valores de los parámetros
			for (int i = 0; i < valores.size(); i++) {

				if (tipos.get(i).equalsIgnoreCase("NUMBER")) {

					if (!valores.get(i).trim().equals("") && valores.get(i) != null) {
						statement.setObject(i + 1, Double.parseDouble(valores.get(i)));

					} else {
						statement.setObject(i + 1, null);
					}

				} else {
					statement.setObject(i + 1, valores.get(i));
				}

			}

			// Ejecutar la sentencia INSERT
			statement.executeUpdate();

		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null,
					"--------------------------------------Error en la conexión.--------------------------------------"
							+ "\nError: \n" + e,
					"Error", 0, null);
		}

	}

	// Read es "consulta"

	public static void update(List<String> valores, List<String> columnas, List<String> tipos) {

		// Crear una consulta SQL dinámica
		StringBuilder sql = new StringBuilder("UPDATE ").append(Configuracion.tabla).append(" SET ");

		for (int i = 0; i < valores.size(); i++) {
			sql.append(columnas.get(i)).append(" = ");

			// Verificar si el tipo de dato del campo requiere conversión
			if (tipos.get(i).equalsIgnoreCase("DATE")) {
				// Si el tipo es "fecha", agregar TO_DATE
				sql.append("TO_DATE(?, 'YYYY-MM-DD HH24:MI:SS')");
			} else {
				// Si no, agregar ? como de costumbre
				sql.append("?");
			}

			if (i < valores.size() - 1) {
				sql.append(", ");
			}
		}

		if (Vista.primerCampo.equals("ENTIDAD") && !Configuracion.tabla.equals("xent")) {
			sql.append(" WHERE ENTIDAD = ").append(Main.entidad).append(" AND ").append(Configuracion.campoCodigo)
			.append(" = ?");
		}
		
		PreparedStatement statement = null;
		
		try {
			
			statement = conn.prepareStatement(sql.toString());

			// Establecer los valores de los parámetros
			for (int i = 0; i < valores.size(); i++) {
				if (tipos.get(i).equalsIgnoreCase("NUMBER")) {
					if (!valores.get(i).trim().isEmpty() && valores.get(i) != null) {
						statement.setObject(i + 1, Double.parseDouble(valores.get(i)));
					} else {
						statement.setObject(i + 1, null);
					}
				} else {
					statement.setObject(i + 1, valores.get(i));
				}
			}
			
			statement.setString(valores.size() + 1, valores.get(1));

			// Ejecutar la sentencia UPDATE
			statement.executeUpdate();

		} catch (SQLException e) {
			Configuracion.mensajeError(e);
		}
	}

	public static boolean delete(String parametro) {

		PreparedStatement statement = null;

		String sql = "DELETE FROM " + Configuracion.tabla + " WHERE ENTIDAD = " + Main.entidad + " AND "
				+ Configuracion.campoCodigo + " = ?";
		
		if (!Vista.primerCampo.equals("ENTIDAD") || Configuracion.tabla.equals("xent")) {
			sql= "DELETE FROM " + Configuracion.tabla + " WHERE " + Configuracion.campoCodigo + " = ?";
		}
		
		try {

			statement = conn.prepareStatement(sql);
			statement.setString(1, parametro);

			boolean borrado = (statement.executeUpdate() == 1);

			statement.close();

			return borrado;

		} catch (Exception e) {
			Configuracion.mensajeError(e);
		}

		return false;
	}

	// Método para desconectar
	public static void desconectar() {
		try {
			if (conn != null) {
				conn.close();
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Error al desconectar: " + e);
		}
	}
*/
}
