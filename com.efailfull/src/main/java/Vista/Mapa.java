package Vista;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;  // Importar la clase JButton
import javax.swing.JLabel;
import javax.swing.JPanel;

import Controlador.ControladorCombate;
import Modelo.Partida;

public class Mapa extends JPanel {
    private JLabel jugadorLabel;
    private JLabel mapaLabel;
    public static int casilla = 1;
    public static String[] tipoCasilla = new String []{	"Combate", "Combate", "Combate", "Descanso", "Jefe", 
											    		"Combate", "Combate", "Combate", "Descanso", "Recompensa", 
											    		"Combate", "Combate", "Combate", "Descanso", "Jefe"};
    
    public static int jugadorX;
    public static int jugadorY;
    public static int jugadorHeight;
    public static int jugadorWidth;
    public static Image jugadorEscalado;
    public static JButton continuarButton;

    public Mapa() {
        setLayout(null); // Usar layout nulo para colocar componentes en posiciones específicas

        // Dimensiones del panel
        int panelWidth = Menu.ancho;
        int panelHeight = Menu.alto;

        // Proporciones para el tamaño del mapa
        int mapaWidth = panelWidth * 3 / 4; // El mapa ocupa dos tercios del ancho del panel
        int mapaHeight = panelHeight * 3 / 4; // El mapa ocupa dos tercios del alto del panel
        int mapaX = (panelWidth - mapaWidth) / 2; // Centrar horizontalmente
        int mapaY = (panelHeight - mapaHeight) / 2; // Centrar verticalmente

        // Cargar la imagen del mapa
        ImageIcon mapaIcon = new ImageIcon("C:\\Users\\alegu\\eclipse-workspace\\com.efailfull\\src\\main\\resources\\Mapa.png");
        mapaLabel = new JLabel(mapaIcon);
        mapaLabel.setBounds(mapaX, mapaY, mapaWidth, mapaHeight);

        // Escalar la imagen del mapa
        Image mapaImage = mapaIcon.getImage();
        Image mapaEscalado = mapaImage.getScaledInstance(mapaWidth, mapaHeight, Image.SCALE_SMOOTH);
        mapaLabel.setIcon(new ImageIcon(mapaEscalado));

        // Agregar la imagen del mapa al panel
        add(mapaLabel);

        // Crear y agregar el icono del jugador
        ImageIcon jugadorIcon = new ImageIcon("C:\\Users\\alegu\\eclipse-workspace\\com.efailfull\\src\\main\\resources\\Circulo_Jugador.png");
        Image jugadorImage = jugadorIcon.getImage();
        Image jugadorEscalado = jugadorImage.getScaledInstance(panelWidth * 1 / 12, panelHeight * 1 / 12, Image.SCALE_SMOOTH);
        
        jugadorLabel = new JLabel(jugadorIcon);
        jugadorWidth = panelWidth * 1 / 6;
        jugadorHeight = panelHeight * 1 / 6;
        jugadorX = panelWidth * 1 / 8 + panelWidth * 1 / 80;
        jugadorY = panelHeight * 1 / 8 + panelHeight * 1 / 20;
        jugadorLabel.setBounds(jugadorX, jugadorY, jugadorWidth, jugadorHeight);
        jugadorLabel.setIcon(new ImageIcon(jugadorEscalado));

        // Agregar el icono del jugador al panel
        add(jugadorLabel);

        // Crear el botón "Continuar"
        continuarButton = new JButton("Continuar");
        int buttonWidth = panelWidth/8;
        int buttonHeight = panelHeight/18;
        int buttonX = (panelWidth - buttonWidth) / 2; // Centrar horizontalmente
        int buttonY = mapaHeight; // Colocar justo debajo de la imagen grande con un margen de 10px

        continuarButton.setEnabled(!Partida.batallaActiva);
        continuarButton.setBounds(buttonX, buttonY, buttonWidth, buttonHeight);
        
        continuarButton.addActionListener(new ActionListener () {

			@Override
			public void actionPerformed(ActionEvent e) {

	        	continuarButton.setEnabled(false);
	        	
	        	Partida.tipoCasillaActual = iniciaEvento();
	        	
	        	if (Partida.tipoCasillaActual.equals("Combate") || Partida.tipoCasillaActual.equals("Jefe")) {

		        	Partida.batallaActiva = true;
		        	Partida.eligeEnemigo();
		        	Partida.combateTab = new Combate();
		        	Partida.panelPartida.remove(2);
		        	Partida.panelPartida.addTab("Combate", Partida.combateTab);
		        	Partida.panelPartida.setEnabledAt(2, Partida.batallaActiva);
		        	
	        	}
	        	
			}

        });
        
        // Agregar el botón al panel
        add(continuarButton);
    }

    // Método para actualizar la posición del jugador
    public void moverJugador(int x, int y) {
        jugadorLabel.setLocation(x, y);
        repaint(); // Repintar el panel para actualizar la posición del jugador
    }

    public void avanzarCasilla() {
        if (casilla % 5 == 0) {
            jugadorY += Menu.alto * 1 / 8 + Menu.alto * 3 / 40;
            if (casilla != 5)
                jugadorX = Menu.ancho * 1 / 8 + Menu.ancho * 1 / 80;
        } else {
            if (casilla > 5 && casilla < 10)
                jugadorX -= Menu.ancho * 1 / 8 + Menu.ancho * 2 / 80;
            else
                jugadorX += Menu.ancho * 1 / 8 + Menu.ancho * 2 / 80;
        }
        jugadorLabel.setLocation(jugadorX, jugadorY);
        repaint();
    }
    
    public String iniciaEvento() {
    	return tipoCasilla[casilla-1];
    }
}
