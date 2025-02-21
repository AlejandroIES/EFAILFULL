package Vista;

import java.awt.Image;

import javax.swing.ImageIcon;

public class UtilidadesInterfaz {

	public static ImageIcon reescalar(ImageIcon iconoOriginal, int ancho, int alto) {

		// Reescalar la imagen a un nuevo tamaño
		Image imagenOriginal = iconoOriginal.getImage();
		Image imagenReescalada = imagenOriginal.getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);

		// Crear un nuevo ImageIcon con la imagen reescalada
		ImageIcon iconoReescalado = new ImageIcon(imagenReescalada);
		return iconoReescalado;
	}

}
