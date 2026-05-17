package vista;

import modelo.Usuario;
import util.Constantes;
import servicio.AuthServicio;
import dades.UsuarioDB;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;

public class VentanaMenu extends JFrame {   // ← AÑADIDO extends JFrame

    private final Usuario usuarioActual;

    private JPanel panelPrincipal;
    private JLabel lblBienvenida;
    private JLabel lblStats;
    private JButton btnJugar;
    private JButton btnRanking;
    private JButton btnSalir;

    public VentanaMenu(Usuario usuario) {
        this.usuarioActual = usuario;
        inicializarUI();
        configurarVentana();
    }

    private void configurarVentana() {
        setTitle("Bright Falls — Menú");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 520);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    private void inicializarUI() {
        panelPrincipal = new JPanel(new GridBagLayout());
        panelPrincipal.setBackground(Constantes.NEGRO);
        panelPrincipal.setBorder(new EmptyBorder(50, 60, 50, 60));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill    = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.insets  = new Insets(8, 0, 8, 0);

        JLabel lblLogo = new JLabel("BRIGHT FALLS");
        lblLogo.setFont(Constantes.FUENTE_TITULO);
        lblLogo.setForeground(Constantes.BLANCO);
        lblLogo.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0; gbc.gridy = 0;
        panelPrincipal.add(lblLogo, gbc);

        JSeparator sep = new JSeparator();
        sep.setForeground(Constantes.GRANATE);
        sep.setBackground(Constantes.GRANATE);
        gbc.gridy = 1; gbc.insets = new Insets(0, 0, 20, 0);
        panelPrincipal.add(sep, gbc);

        lblBienvenida = new JLabel("Hola, " + usuarioActual.getNombre().toUpperCase());
        lblBienvenida.setFont(Constantes.FUENTE_SUBTITULO);
        lblBienvenida.setForeground(Constantes.OCRE);
        lblBienvenida.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 2; gbc.insets = new Insets(8, 0, 4, 0);
        panelPrincipal.add(lblBienvenida, gbc);

        lblStats = new JLabel(formatearStats());
        lblStats.setFont(Constantes.FUENTE_SMALL);
        lblStats.setForeground(Constantes.GRIS);
        lblStats.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 3; gbc.insets = new Insets(0, 0, 30, 0);
        panelPrincipal.add(lblStats, gbc);

        btnJugar = crearBotonMenu("NUEVA PARTIDA", Constantes.GRANATE, Constantes.BLANCO);
        gbc.gridy = 4; gbc.insets = new Insets(6, 0, 6, 0);
        panelPrincipal.add(btnJugar, gbc);

        btnRanking = crearBotonMenu("RANKING & HISTORIAL", Constantes.GRIS, Constantes.BLANCO);
        gbc.gridy = 5;
        panelPrincipal.add(btnRanking, gbc);

        btnSalir = crearBotonMenu("CERRAR SESIÓN", Constantes.NEGRO, Constantes.GRIS);
        btnSalir.setBorder(BorderFactory.createLineBorder(Constantes.GRIS, 1));
        gbc.gridy = 6; gbc.insets = new Insets(20, 0, 0, 0);
        panelPrincipal.add(btnSalir, gbc);

        btnJugar.addActionListener(e -> abrirDialogoJugar());
        btnRanking.addActionListener(e -> abrirRanking());
        btnSalir.addActionListener(e -> cerrarSesion());

        setContentPane(panelPrincipal);
    }

    private void abrirDialogoJugar() {
        String nombreJ2 = JOptionPane.showInputDialog(
                this,
                "Nombre del Jugador 2:",
                "Nueva partida",
                JOptionPane.PLAIN_MESSAGE
        );

        if (nombreJ2 == null || nombreJ2.isBlank()) return;

        // Usar el mismo paquete que el import de cabecera: dades.UsuarioDB
        try {
            UsuarioDB db = new UsuarioDB();                          // ← unificado
            Usuario jugador2 = db.buscarPorNombre(nombreJ2.trim()); // ← unificado

            if (jugador2 == null) {
                JOptionPane.showMessageDialog(this,
                        "El jugador '" + nombreJ2.trim() + "' no existe.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (jugador2.getId() == usuarioActual.getId()) {
                JOptionPane.showMessageDialog(this,
                        "No puedes jugar contra ti mismo.",
                        "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            VentanaJuego ventanaJuego = new VentanaJuego(usuarioActual, jugador2, this);
            ventanaJuego.setVisible(true);
            setVisible(false);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al buscar jugador: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void abrirRanking() {
        VentanaRanking ranking = new VentanaRanking(this);
        ranking.setVisible(true);
        setVisible(false);
    }

    private void cerrarSesion() {
        int confirm = JOptionPane.showConfirmDialog(
                this, "¿Cerrar sesión?", "Bright Falls",
                JOptionPane.YES_NO_OPTION
        );
        if (confirm == JOptionPane.YES_OPTION) {
            new VentanaLogin().setVisible(true);
            dispose();
        }
    }

    public void actualizarStats(Usuario usuarioActualizado) {
        lblStats.setText(formatearStats(usuarioActualizado));
    }

    private String formatearStats() {
        return formatearStats(usuarioActual);
    }

    private String formatearStats(Usuario u) {
        return String.format(
                "V: %d   D: %d   E: %d   — %d partidas",
                u.getVictorias(), u.getDerrotas(),
                u.getEmpates(), u.getTotalPartidas()
        );
    }

    private JButton crearBotonMenu(String texto, Color fondo, Color fuente) {
        JButton btn = new JButton(texto);
        btn.setFont(Constantes.FUENTE_BTN);
        btn.setBackground(fondo);
        btn.setForeground(fuente);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(0, 46));
        Color fondoHover = fondo.equals(Constantes.NEGRO) ? Constantes.GRIS_CLARO : Constantes.OCRE;
        btn.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) {
                btn.setBackground(fondoHover);
                btn.setForeground(Constantes.NEGRO);
            }
            @Override public void mouseExited(MouseEvent e) {
                btn.setBackground(fondo);
                btn.setForeground(fuente);
            }
        });
        return btn;
    }
}