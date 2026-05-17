package vista;

import modelo.Usuario;
import servicio.JuegoServicio;
import servicio.JuegoServicio.EstadoJuego;
import util.Constantes;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;

public class VentanaJuego extends JFrame {   // ← AÑADIDO extends JFrame

    private final Usuario jugador1;
    private final Usuario jugador2;
    private final VentanaMenu ventanaMenu;
    private final JuegoServicio juegoServicio;

    private JLabel lblTurno;
    private JLabel lblJ1;
    private JLabel lblJ2;
    private JButton[][] celdas;
    private JPanel panelTablero;
    private JButton btnNuevaPartida;
    private JButton btnVolver;

    public VentanaJuego(Usuario jugador1, Usuario jugador2, VentanaMenu ventanaMenu) {
        this.jugador1      = jugador1;
        this.jugador2      = jugador2;
        this.ventanaMenu   = ventanaMenu;
        this.juegoServicio = new JuegoServicio();

        inicializarUI();
        configurarVentana();
        iniciarNuevaPartida();
    }

    private void configurarVentana() {
        setTitle("Bright Falls — Juego");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(Constantes.VENTANA_ANCHO, Constantes.VENTANA_ALTO);
        setLocationRelativeTo(null);
        setResizable(false);

        addWindowListener(new WindowAdapter() {
            @Override public void windowClosing(WindowEvent e) {
                volverAlMenu();
            }
        });
    }

    private void inicializarUI() {
        JPanel panelPrincipal = new JPanel(new BorderLayout(0, 0));
        panelPrincipal.setBackground(Constantes.NEGRO);

        JPanel panelInfo = new JPanel(new GridLayout(1, 3));
        panelInfo.setBackground(Constantes.GRIS_CLARO);
        panelInfo.setBorder(new EmptyBorder(14, 20, 14, 20));

        lblJ1 = new JLabel();
        lblJ1.setFont(Constantes.FUENTE_TEXTO);
        lblJ1.setForeground(Constantes.BLANCO);
        lblJ1.setHorizontalAlignment(SwingConstants.LEFT);

        lblTurno = new JLabel("", SwingConstants.CENTER);
        lblTurno.setFont(Constantes.FUENTE_BTN);
        lblTurno.setForeground(Constantes.OCRE);

        lblJ2 = new JLabel();
        lblJ2.setFont(Constantes.FUENTE_TEXTO);
        lblJ2.setForeground(Constantes.BLANCO);
        lblJ2.setHorizontalAlignment(SwingConstants.RIGHT);

        panelInfo.add(lblJ1);
        panelInfo.add(lblTurno);
        panelInfo.add(lblJ2);
        panelPrincipal.add(panelInfo, BorderLayout.NORTH);

        panelTablero = new JPanel(new GridLayout(3, 3, 4, 4));
        panelTablero.setBackground(Constantes.GRANATE);
        panelTablero.setBorder(new EmptyBorder(20, 60, 20, 60));

        celdas = new JButton[3][3];
        for (int f = 0; f < 3; f++) {
            for (int c = 0; c < 3; c++) {
                celdas[f][c] = crearCelda(f, c);
                panelTablero.add(celdas[f][c]);
            }
        }
        panelPrincipal.add(panelTablero, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 14));
        panelBotones.setBackground(Constantes.NEGRO);

        btnNuevaPartida = new JButton("NUEVA PARTIDA");
        btnNuevaPartida.setFont(Constantes.FUENTE_BTN);
        btnNuevaPartida.setBackground(Constantes.GRANATE);
        btnNuevaPartida.setForeground(Constantes.BLANCO);
        btnNuevaPartida.setFocusPainted(false);
        btnNuevaPartida.setBorderPainted(false);
        btnNuevaPartida.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnNuevaPartida.setPreferredSize(new Dimension(180, 38));

        btnVolver = new JButton("VOLVER AL MENÚ");
        btnVolver.setFont(Constantes.FUENTE_BTN);
        btnVolver.setBackground(Constantes.GRIS);
        btnVolver.setForeground(Constantes.BLANCO);
        btnVolver.setFocusPainted(false);
        btnVolver.setBorderPainted(false);
        btnVolver.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnVolver.setPreferredSize(new Dimension(180, 38));

        panelBotones.add(btnNuevaPartida);
        panelBotones.add(btnVolver);
        panelPrincipal.add(panelBotones, BorderLayout.SOUTH);

        btnNuevaPartida.addActionListener(e -> iniciarNuevaPartida());
        btnVolver.addActionListener(e -> volverAlMenu());

        setContentPane(panelPrincipal);
    }

    private JButton crearCelda(int fila, int col) {
        JButton btn = new JButton("");
        btn.setFont(Constantes.FUENTE_CELDA);
        btn.setBackground(Constantes.NEGRO);
        btn.setForeground(Constantes.BLANCO);
        btn.setFocusPainted(false);
        btn.setBorder(new LineBorder(Constantes.GRIS_CLARO, 1));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) {
                if (btn.getText().isEmpty()) btn.setBackground(Constantes.GRIS_CLARO);
            }
            @Override public void mouseExited(MouseEvent e) {
                if (btn.getText().isEmpty()) btn.setBackground(Constantes.NEGRO);
            }
        });

        btn.addActionListener(e -> procesarMovimiento(fila, col));
        return btn;
    }

    private void iniciarNuevaPartida() {
        juegoServicio.iniciarPartida(jugador1, jugador2);
        limpiarTableroUI();
        actualizarInfoTurno();
    }

    private void limpiarTableroUI() {
        for (int f = 0; f < 3; f++) {
            for (int c = 0; c < 3; c++) {
                celdas[f][c].setText("");
                celdas[f][c].setBackground(Constantes.NEGRO);
                celdas[f][c].setForeground(Constantes.BLANCO);
                celdas[f][c].setEnabled(true);
            }
        }
    }

    private void procesarMovimiento(int fila, int col) {
        EstadoJuego estado = juegoServicio.hacerMovimiento(fila, col);

        switch (estado) {
            case CONTINUA -> {
                actualizarCeldaUI(fila, col);
                actualizarInfoTurno();
            }
            case GANADOR -> {
                actualizarCeldaUI(fila, col);
                deshabilitarTablero();
                guardarYMostrarResultado(false);
            }
            case EMPATE -> {
                actualizarCeldaUI(fila, col);
                deshabilitarTablero();
                guardarYMostrarResultado(true);
            }
            case CELDA_OCUPADA, MOVIMIENTO_INVALIDO -> { }
            default -> { }
        }
    }

    private void actualizarCeldaUI(int fila, int col) {
        char ficha = juegoServicio.getTablero()[fila][col];
        celdas[fila][col].setText(String.valueOf(ficha));

        if (ficha == JuegoServicio.FICHA_1) {
            celdas[fila][col].setForeground(Constantes.OCRE);
        } else {
            celdas[fila][col].setForeground(Constantes.BLANCO);
        }
        celdas[fila][col].setEnabled(false);
    }

    private void deshabilitarTablero() {
        for (JButton[] fila : celdas) {
            for (JButton celda : fila) {
                celda.setEnabled(false);
            }
        }
    }

    private void guardarYMostrarResultado(boolean esEmpate) {
        try {
            juegoServicio.guardarResultado();
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al guardar partida: " + ex.getMessage(),
                    "Error BD", JOptionPane.ERROR_MESSAGE);
        }

        String mensaje;
        if (esEmpate) {
            mensaje = "¡Empate! Nadie gana esta vez.";
            lblTurno.setText("EMPATE");
        } else {
            Usuario ganador = juegoServicio.getJugadorGanador();
            mensaje = "¡" + ganador.getNombre().toUpperCase() + " GANA!";
            lblTurno.setText(mensaje);
        }

        JOptionPane.showMessageDialog(this, mensaje, "Fin de partida",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void actualizarInfoTurno() {
        Usuario actual = juegoServicio.getJugadorActual();
        char ficha     = juegoServicio.getTurnoActual();

        lblJ1.setText(jugador1.getNombre() + " [X]"
                + "  V:" + jugador1.getVictorias());
        lblJ2.setText("V:" + jugador2.getVictorias()
                + "  [O] " + jugador2.getNombre());
        lblTurno.setText("Turno: " + actual.getNombre()
                + " (" + ficha + ")");
    }

    private void volverAlMenu() {
        ventanaMenu.actualizarStats(jugador1);
        ventanaMenu.setVisible(true);
        dispose();
    }
}