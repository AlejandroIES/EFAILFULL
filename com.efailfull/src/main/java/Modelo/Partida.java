package Modelo;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import BDD.Conexion;
import BDD.Utilidades;
import Objetos.Enemigo;
import Objetos.Personaje;
import Vista.Cartas;
import Vista.Combate;
import Vista.Mapa;
import Vista.Menu;
import Vista.NuevaPartida;

public class Partida extends JFrame {

	public static boolean batallaActiva = false;
	public static String tipoCasillaActual;
	public static JTabbedPane panelPartida;
	public static Personaje personaje;
	public static List <String> mazo = new ArrayList<>();			//Se le da valor en el setup inicial. Contiene los id de las cartas del jugador.
	public static List <String> mano = new ArrayList<>();			//Se le da valor en el setup inicial. Contiene los id de las cartas del jugador en la mano.
	public static List <String> porRobar = new ArrayList<>();		//Se le da valor durante el combate. Contiene los id de las cartas que aún no ha robado.
	public static List <String> descartadas = new ArrayList<>();	//Se le da valor durante el combate. Contiene los id de las cartas que ya han pasado por su mano.
	public static List <String> desterradas = new ArrayList<>();	//Se le da valor durante el combate. Contiene los id de las cartas que se consumen tras su uso.
	public static Mapa mapaTab;
	public static Cartas cartasTab;
	public static Combate combateTab;
    public static Enemigo enemigoActual;
    
    public static int personajeSeleccionado;
	
    public Partida() {
    	
    	if (NuevaPartida.personajeSeleccionado != -1) {
			personajeSeleccionado = NuevaPartida.personajeSeleccionado;
		}
    	
        // Configuración básica de la ventana
        setTitle("Evade the Fail");
        setSize(Menu.ancho, Menu.alto);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Crear el panel de pestañas
        panelPartida = new JTabbedPane();

        // Crear las instancias de las pestañas
        mapaTab = new Mapa();
        cartasTab = new Cartas();
        combateTab = new Combate();

        // Agregar las pestañas al JTabbedPane
        panelPartida.addTab("Mapa", mapaTab);
        panelPartida.addTab("Cartas", cartasTab);
        panelPartida.addTab("Combate", combateTab);
        panelPartida.setEnabledAt(2, batallaActiva); // Deshabilitar la pestaña de Combate inicialmente

        // Agregar el JTabbedPane al contenido de la ventana
        add(panelPartida);

        Conexion.conectar();
        
        // Hacer visible la ventana
        setVisible(true);
    }

    public static void eligeEnemigo() {
        // Lógica para decidir el rango de IDs según el lugar en el que te encuentras

        if (Mapa.casilla==1) {
            creaEnemigo(1);
        } else {
        	creaEnemigo(3);
        }
    }

    private static void creaEnemigo(int id) {
        String sql = "SELECT * FROM enemigoBase WHERE id = ?;";
        List<String> enemigoBaseEnBDD = Utilidades.consulta(sql, id + "");
        enemigoActual = new Enemigo(
        	id,
            50, 
            enemigoBaseEnBDD.get(1), 
            enemigoBaseEnBDD.get(2),
            Integer.parseInt(enemigoBaseEnBDD.get(3)),
            Integer.parseInt(enemigoBaseEnBDD.get(4)),
            Integer.parseInt(enemigoBaseEnBDD.get(5)),
            Integer.parseInt(enemigoBaseEnBDD.get(6)),
            Integer.parseInt(enemigoBaseEnBDD.get(7)),
            Double.parseDouble(enemigoBaseEnBDD.get(8)),
            Double.parseDouble(enemigoBaseEnBDD.get(9)), 
            ""
        );
        enemigoActual.asignaAtaques();
        //Nivel de enemigo aleatorio hasta dos niveles superior o inferior al nivel del personaje
        int nivelEnemigo = (int) (Math.random() * 5 + personaje.getNivel() - 2);
        String sqlEnemigo = "INSERT INTO EnemigoActual (IdEnemigo, Nivel) VALUES (" + id + ", " + nivelEnemigo + ")";


		PreparedStatement statement;
		try {
			statement = Conexion.conn.prepareStatement(sqlEnemigo);
			statement.executeUpdate();
		} catch (SQLException e) {
			Conexion.mensajeError(e);
		}
        
    }

    public static Enemigo getEnemigoActual() {
        return enemigoActual;
    }

    public static Personaje getPersonaje() {
        return personaje;
    }
    
    
}
