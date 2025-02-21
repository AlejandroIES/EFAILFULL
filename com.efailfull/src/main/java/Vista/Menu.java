package Vista;

import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class Menu extends JFrame {

	private static final long serialVersionUID = 1L;

	public static int ancho = Login.dimensiones[0], alto = Login.dimensiones[1];
	
	public static Menu menu;

	public Menu() {
		
		menu = this;
		setName("menu");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, ancho, alto);
		//contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().setLayout(null);

		JPanel panel = new JPanel(); // 5 filas, 2 columnas, con espacio horizontal y vertical de 5
		//panel.setBorder(new LineBorder(UIManager.getColor("CheckBox.darkShadow")));
		panel.setBounds(ancho / 3, (int) (1.5 * alto / 3), ancho / 3, alto / 3);
		panel.setLayout(new GridLayout(4, 3, 10, 10)); // GridLayout con 4 filas y 3 columnas, espacios de 10 pixels
														// entre filas y columnas

		Font font = new Font("Arial", Font.PLAIN, 12);

		// Botón "Continuar"
		JButton btnContinuar = new JButton("Continuar");
		btnContinuar.setFont(font);
		btnContinuar.setEnabled(false); // Desactivar el botón "Continuar"
		btnContinuar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Acción a realizar cuando se hace clic en "Continuar"
				System.out.println("Botón 'Continuar' clicado");
			}
		});
		panel.add(btnContinuar);

		// Botón "Nueva Partida"
		JButton btnNuevaPartida = new JButton("Nueva Partida");
		btnNuevaPartida.setFont(font);
		btnNuevaPartida.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				NuevaPartida nuevaPartida = new NuevaPartida();
				nuevaPartida.setVisible(true);
				dispose();
			}
		});
		panel.add(btnNuevaPartida);

		// Botón "Estadísticas"
		JButton btnEstadisticas = new JButton("Estadísticas");
		btnEstadisticas.setFont(font);
		btnEstadisticas.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		panel.add(btnEstadisticas);

		// Botón "Salir"
		JButton btnSalir = new JButton("Salir");
		btnSalir.setFont(font);
		btnSalir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Window window = SwingUtilities.getWindowAncestor(btnSalir);
		        window.dispose();
			}
		});
		panel.add(btnSalir);

		getContentPane().add(panel);

		ImageIcon iconoOriginal = new ImageIcon(
				"C:\\Users\\alegu\\eclipse-workspace\\com.efailfull\\src\\main\\resources\\title.jpg");

		// Reescalar la imagen a un nuevo tamaño
		Image imagenOriginal = iconoOriginal.getImage();
		Image imagenReescalada = imagenOriginal.getScaledInstance(2 * ancho / 3, alto / 3, Image.SCALE_SMOOTH);

		// Crear un nuevo ImageIcon con la imagen reescalada
		ImageIcon iconoReescalado = new ImageIcon(imagenReescalada);

		JLabel imgTitulo = new JLabel();
		imgTitulo.setBounds(ancho / 6, alto / 8, 2 * ancho / 3, alto / 3);
		imgTitulo.setIcon(new ImageIcon(imagenReescalada));
		getContentPane().add(imgTitulo);
	}

	
	public static void main(String[] args) {
		Menu menu = new Menu();
		menu.setVisible(true);
	}

}
