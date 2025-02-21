package Vista;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import BDD.Conexion;
import BDD.Utilidades;
import Controlador.ControladorPartida;
import Modelo.Partida;

public class NuevaPartida extends JFrame {

	private static final long serialVersionUID = 1L;
	private List<JLabel> Personajes = new ArrayList<JLabel>();
	private boolean[] selected = { false, false, false };
	private JButton selectButton;
	private JLabel selectionLabel;
	public static int personajeSeleccionado = -1;

	public NuevaPartida() {

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(null);

		setSize(Menu.ancho, Menu.alto);

		// Calcular posiciones y tamaños
		int characterWidth = Menu.ancho / 6;
		int characterHeight = 4 * Menu.alto / 6;
		int margin = Menu.ancho / 6;
		int spacing = Menu.ancho / 12;

		// Cargar las imágenes de los personajes (asegúrate de tener las imágenes en la
		// ruta correcta)
		ImageIcon[] characterImages = {
				UtilidadesInterfaz.reescalar(new ImageIcon("C:\\Users\\alegu\\Pictures\\Vertical1.jpg"), characterWidth,
						characterHeight),
				UtilidadesInterfaz.reescalar(new ImageIcon("C:\\Users\\alegu\\Pictures\\Vertical1.jpg"), characterWidth,
						characterHeight),
				UtilidadesInterfaz.reescalar(new ImageIcon("C:\\Users\\alegu\\Pictures\\Vertical1.jpg"), characterWidth,
						characterHeight) };

		// Crear y posicionar las etiquetas de los personajes
		for (int i = 0; i < 3; i++) {
			Personajes.add(new JLabel(characterImages[i]));
			Personajes.get(i).setBounds(margin + i * (characterWidth + spacing), Menu.alto / 12, characterWidth,
					characterHeight);
			Personajes.get(i).setBorder(BorderFactory.createLineBorder(Color.BLACK));
			add(Personajes.get(i));

			final int index = i;
			Personajes.get(i).addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					selectCharacter(index);
				}
			});
		}

		// Crear la etiqueta para mostrar la selección
		selectionLabel = new JLabel("");
		selectionLabel.setBounds(Menu.ancho / 2 - Menu.ancho / 24, characterHeight + Menu.alto / 12, Menu.ancho / 12,
				Menu.alto / 24);
		selectionLabel.setHorizontalAlignment(SwingConstants.CENTER);
		add(selectionLabel);

		// Crear el botón "Seleccionar"
		selectButton = new JButton("Seleccionar");
		selectButton.setBounds(Menu.ancho / 2 - Menu.ancho / 24, characterHeight + Menu.alto / 6, Menu.ancho / 12,
				Menu.alto / 24);
		selectButton.setEnabled(false);
		add(selectButton);

		// Añadir ActionListener al botón "Seleccionar"
		selectButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (personajeSeleccionado != -1) {
					personajeSeleccionado++;
					ControladorPartida.setupInicial();
					
					new Partida();
					dispose();
				}
			}
		});

		// Mostrar la ventana
		setLocationRelativeTo(null);
		setVisible(true);
	}

	private void selectCharacter(int index) {
		// Desmarcar todos los personajes
		for (int i = 0; i < 3; i++) {
			if (i != index) {
				selected[i] = false;
				Personajes.get(i).setBorder(BorderFactory.createLineBorder(Color.BLACK));
			}
		}

		// Marcar el personaje seleccionado
		selected[index] = !selected[index];
		Personajes.get(index).setBorder(BorderFactory.createLineBorder(selected[index] ? Color.GREEN : Color.BLACK));
		Conexion.conectar();
		// Actualizar la etiqueta de selección y habilitar/deshabilitar el botón
		if (selected[index]) {
			personajeSeleccionado = index;
			selectionLabel.setBounds(5 * Menu.ancho / 24 + index * (Menu.ancho / 6 + Menu.ancho / 12),
					4 * Menu.alto / 6 + Menu.alto / 12, Menu.ancho / 12, Menu.alto / 24);
			selectionLabel.setText(Utilidades.consultaDatoUnico("Select Clase from personaje where id = ?",
					personajeSeleccionado + 1 + ""));

			selectButton.setEnabled(true);
		} else {
			personajeSeleccionado = -1;
			selectionLabel.setText("");
			selectButton.setEnabled(false);
		}
	}

	public static void main(String[] args) {
		NuevaPartida nuevaPartida = new NuevaPartida();
		nuevaPartida.setVisible(true);
	}
}
