package Vista;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import BDD.Conexion;
import BDD.Utilidades;

public class Login extends JFrame {

	private static final long serialVersionUID = 1L;
	public static int usuario;
	public static String usuarioNombre;

	public static int[] dimensiones = { 1280, 720 };
	// public static int[] dimensiones = {1600, 900};

	public static int ancho = dimensiones[0], alto = dimensiones[1];

	public Login() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, ancho, alto);
		// contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().setLayout(null);

		JPanel panel = new JPanel(); // 5 filas, 2 columnas, con espacio horizontal y vertical de 5
		// panel.setBorder(new LineBorder(UIManager.getColor("CheckBox.darkShadow")));

		panel.setBounds(ancho / 3, (int) (1.5 * alto / 3), ancho / 3, alto / 3);
		panel.setLayout(null);

		// Fila 1: Label
		JLabel label1 = new JLabel("Usuario");
		label1.setBounds(5, 5, ancho / 3 - 10, alto / 15 - 5);
		// label1.setBounds()
		panel.add(label1);

		// Fila 2: TextField
		JTextField txtUsuario = new JTextField();
		txtUsuario.setBounds(5, alto / 15, ancho / 3 - 10, alto / 15 - 5);
		panel.add(txtUsuario);

		// Fila 3: Label
		JLabel label2 = new JLabel("Contraseña");
		label2.setBounds(5, 2 * alto / 15, ancho / 3 - 10, alto / 15 - 5);
		panel.add(label2);

		// Fila 4: TextField
		JTextField txtContrasena = new JTextField();
		txtContrasena.setBounds(5, 3 * alto / 15, ancho / 3 - 10, alto / 15 - 5);
		panel.add(txtContrasena);

		// Fila 5: Botón
		JButton btnInicioSesion = new JButton("Botón");
		btnInicioSesion.setBounds(5, 4 * alto / 15, ancho / 3 - 10, alto / 15 - 5);
		btnInicioSesion.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				Conexion.conectar();

				boolean pruebaexiste = Conexion.login(txtUsuario.getText().toString(),
						txtContrasena.getText().toString());

				if (pruebaexiste) {
					String conusltaUsuario = "Select Id from jugador where Nombre = ? and Contrasena = ?";

					List<String> parameters = new ArrayList<String>();

					parameters.add(txtUsuario.getText().toString());
					parameters.add(txtContrasena.getText().toString());

					usuario = Integer.parseInt((String) Utilidades.consultaDatoUnico(conusltaUsuario, parameters));
					usuarioNombre = parameters.get(0);

					Menu menu = new Menu();
					menu.setVisible(true);
					dispose();
				}
			}
		});
		panel.add(btnInicioSesion);

		getContentPane().add(panel);

		ImageIcon iconoOriginal = new ImageIcon(
				"C:\\Users\\alegu\\git\\repository\\com.efailfull\\src\\main\\resources\\title.jpg");

		// Reescalar la imagen a un nuevo tamaño
		Image imagenOriginal = iconoOriginal.getImage();
		Image imagenReescalada = imagenOriginal.getScaledInstance(2 * ancho / 3, alto / 3, Image.SCALE_SMOOTH);

		JLabel imgTitulo = new JLabel();
		imgTitulo.setBounds(ancho / 6, alto / 8, 2 * ancho / 3, alto / 3);
		imgTitulo.setIcon(new ImageIcon(imagenReescalada));
		getContentPane().add(imgTitulo);
	}

	public static void main(String[] args) {
		Login frame = new Login();
		frame.setVisible(true);
	}
}
