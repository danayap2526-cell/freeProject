package vista;

import dades.PartidaDB;
import dades.UsuarioDB;
import modelo.Partida;
import modelo.Usuario;
import util.Constantes;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class VentanaRanking extends JFrame {   // ← AÑADIDO extends JFrame

    private final VentanaMenu ventanaMenu;
    private final UsuarioDB usuarioDB;
    private final PartidaDB partidaDB;

    private JTable tablaRanking;
    private JTable tablaHistorial;

    private static final DateTimeFormatter FMT =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public VentanaRanking(VentanaMenu ventanaMenu) {
        this.ventanaMenu = ventanaMenu;
        this.usuarioDB   = new UsuarioDB();
        this.partidaDB   = new PartidaDB();

        inicializarUI();
        configurarVentana();
        cargarDatos();
    }

    private void configurarVentana() {
        setTitle("Bright Falls — Ranking & Historial");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(Constantes.VENTANA_ANCHO, Constantes.VENTANA_ALTO);
        setLocationRelativeTo(null);
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override public void windowClosing(java.awt.event.WindowEvent e) {
                volver();
            }
        });
    }

    private void inicializarUI() {
        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBackground(Constantes.NEGRO);

        JPanel panelHeader = new JPanel(new BorderLayout());
        panelHeader.setBackground(Constantes.GRIS_CLARO);
        panelHeader.setBorder(new EmptyBorder(16, 24, 16, 24));

        JLabel lblTitulo = new JLabel("RANKING & HISTORIAL");
        lblTitulo.setFont(Constantes.FUENTE_SUBTITULO);
        lblTitulo.setForeground(Constantes.OCRE);
        panelHeader.add(lblTitulo, BorderLayout.WEST);

        JButton btnVolver = new JButton("← VOLVER");
        btnVolver.setFont(Constantes.FUENTE_BTN);
        btnVolver.setBackground(Constantes.GRANATE);
        btnVolver.setForeground(Constantes.BLANCO);
        btnVolver.setFocusPainted(false);
        btnVolver.setBorderPainted(false);
        btnVolver.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnVolver.addActionListener(e -> volver());
        panelHeader.add(btnVolver, BorderLayout.EAST);

        panelPrincipal.add(panelHeader, BorderLayout.NORTH);

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setBackground(Constantes.NEGRO);
        splitPane.setDividerSize(6);
        splitPane.setResizeWeight(0.45);

        splitPane.setTopComponent(crearPanelTabla(
                "RANKING DE JUGADORES",
                crearTablaRanking()
        ));

        splitPane.setBottomComponent(crearPanelTabla(
                "HISTORIAL DE PARTIDAS",
                crearTablaHistorial()
        ));

        panelPrincipal.add(splitPane, BorderLayout.CENTER);
        setContentPane(panelPrincipal);
    }

    private JPanel crearPanelTabla(String titulo, JTable tabla) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Constantes.NEGRO);
        panel.setBorder(new EmptyBorder(12, 16, 0, 16));

        JLabel lbl = new JLabel(titulo);
        lbl.setFont(Constantes.FUENTE_SMALL);
        lbl.setForeground(Constantes.OCRE);
        lbl.setBorder(new EmptyBorder(0, 0, 6, 0));
        panel.add(lbl, BorderLayout.NORTH);

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.getViewport().setBackground(Constantes.NEGRO);
        scroll.setBorder(BorderFactory.createLineBorder(Constantes.GRIS, 1));
        panel.add(scroll, BorderLayout.CENTER);

        return panel;
    }

    private JTable crearTablaRanking() {
        String[] columnas = {"#", "Jugador", "Victorias", "Derrotas", "Empates", "Partidas"};
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tablaRanking = estilizarTabla(new JTable(modelo));
        return tablaRanking;
    }

    private JTable crearTablaHistorial() {
        String[] columnas = {"Jugador 1", "Jugador 2", "Ganador", "Duración", "Fecha"};
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tablaHistorial = estilizarTabla(new JTable(modelo));
        return tablaHistorial;
    }

    private JTable estilizarTabla(JTable tabla) {
        tabla.setBackground(Constantes.NEGRO);
        tabla.setForeground(Constantes.BLANCO);
        tabla.setFont(Constantes.FUENTE_SMALL);
        tabla.setRowHeight(28);
        tabla.setGridColor(Constantes.GRIS_CLARO);
        tabla.setSelectionBackground(Constantes.GRANATE);
        tabla.setSelectionForeground(Constantes.BLANCO);
        tabla.setShowVerticalLines(false);
        tabla.setIntercellSpacing(new Dimension(0, 1));

        JTableHeader header = tabla.getTableHeader();
        header.setBackground(Constantes.GRIS_CLARO);
        header.setForeground(Constantes.OCRE);
        header.setFont(Constantes.FUENTE_BTN);
        header.setReorderingAllowed(false);

        DefaultTableCellRenderer centrado = new DefaultTableCellRenderer();
        centrado.setHorizontalAlignment(SwingConstants.CENTER);
        centrado.setBackground(Constantes.NEGRO);
        centrado.setForeground(Constantes.BLANCO);

        for (int i = 0; i < tabla.getColumnCount(); i++) {
            tabla.getColumnModel().getColumn(i).setCellRenderer(centrado);
        }

        return tabla;
    }

    private void cargarDatos() {
        cargarRanking();
        cargarHistorial();
    }

    private void cargarRanking() {
        try {
            List<Usuario> usuarios = usuarioDB.obtenerRanking();
            DefaultTableModel modelo = (DefaultTableModel) tablaRanking.getModel();
            modelo.setRowCount(0);

            int pos = 1;
            for (Usuario u : usuarios) {
                modelo.addRow(new Object[]{
                        pos++,
                        u.getNombre(),
                        u.getVictorias(),
                        u.getDerrotas(),
                        u.getEmpates(),
                        u.getTotalPartidas()
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al cargar ranking: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarHistorial() {
        try {
            List<Partida> partidas = partidaDB.obtenerHistorial(50);
            DefaultTableModel modelo = (DefaultTableModel) tablaHistorial.getModel();
            modelo.setRowCount(0);

            for (Partida p : partidas) {
                String ganador  = p.getNombreGanador() != null ? p.getNombreGanador() : "Empate";
                String duracion = p.getDuracionSegundos() + "s";
                String fecha    = p.getJugadaEn() != null ? p.getJugadaEn().format(FMT) : "—";

                modelo.addRow(new Object[]{
                        p.getNombreJugador1(),
                        p.getNombreJugador2(),
                        ganador,
                        duracion,
                        fecha
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al cargar historial: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void volver() {
        ventanaMenu.setVisible(true);
        dispose();
    }
}