package Vista;

import javax.swing.*;
import java.awt.*;

public class prueba {

    public static void main(String[] args) {
        // Crear el marco principal
        JFrame frame = new JFrame("Ejemplo de Tabbed Pane con Scroll y Layout Nulo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);

        // Crear el TabbedPane
        JTabbedPane tabbedPane = new JTabbedPane();

        // Crear el panel con Layout nulo
        JPanel panelConScroll = new JPanel();
        panelConScroll.setLayout(null); // Establecer Layout nulo

        // Añadir varios componentes manualmente con posiciones específicas
        for (int i = 1; i <= 10; i++) {
            JLabel label = new JLabel("Etiqueta " + i);
            label.setBounds(10, i * 30, 100, 20); // Posición y tamaño de cada etiqueta
            panelConScroll.add(label);
        }

        // Crear un JScrollPane para el panel con layout nulo
        JScrollPane scrollPane = new JScrollPane(panelConScroll);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        // Configurar tamaño del panel y del JScrollPane
        panelConScroll.setPreferredSize(new Dimension(200, 350)); // Tamaño total del contenido
        scrollPane.setPreferredSize(new Dimension(300, 200)); // Tamaño visible en la pestaña

        // Añadir el JScrollPane al TabbedPane
        tabbedPane.addTab("Pestaña con Layout Nulo", scrollPane);

        // Añadir el TabbedPane al marco
        frame.add(tabbedPane);
        frame.setVisible(true);
    }
}
