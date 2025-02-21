package Vista;

import javax.swing.*;
import BDD.Utilidades;
import Modelo.Partida;
import Objetos.Carta;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class Recompensa extends JDialog {
    private Carta cartaSeleccionada = null;
    private JButton btnConfirmar;
    private int idSeleccionado;

    public Recompensa(JFrame parent, List<Integer> opciones) {
        super(parent, "Selecciona tu recompensa", true);
        setSize(600, 300);
        setLayout(new BorderLayout());

        // Panel donde se mostrarán las cartas
        JPanel panelCartas = new JPanel();
        panelCartas.setLayout(new FlowLayout());

        for (Integer id : opciones) {
            Carta carta = Utilidades.devuelveCarta(id + "", 120, 180); // Suponiendo que devuelve una Carta como JPanel
            carta.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); // Cursor de mano al pasar sobre la carta

            carta.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent evt) {
                	idSeleccionado = id;
                    seleccionarCarta(carta);
                }
            });

            panelCartas.add(carta);
        }

        // Botón de confirmar, desactivado inicialmente
        btnConfirmar = new JButton("Confirmar");
        btnConfirmar.setEnabled(false);
        btnConfirmar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	Partida.mazo.add(idSeleccionado + "");
                dispose();
            }
        });

        // Panel inferior para el botón
        JPanel panelBoton = new JPanel();
        panelBoton.add(btnConfirmar);

        // Agregar componentes a la ventana
        add(panelCartas, BorderLayout.CENTER);
        add(panelBoton, BorderLayout.SOUTH);

        setLocationRelativeTo(parent);
    }

    private void seleccionarCarta(Carta carta) {
        if (cartaSeleccionada != null) {
            cartaSeleccionada.setBorder(null); // Quita el borde de la anterior selección
        }
        
        cartaSeleccionada = carta;
        cartaSeleccionada.setBorder(BorderFactory.createLineBorder(Color.YELLOW, 3)); // Resaltar selección
        btnConfirmar.setEnabled(true); // Activar botón de confirmar
    }

    public Carta getRecompensaSeleccionada() {
        return cartaSeleccionada;
    }
}
