package vista;

import modelo.Usuario;
import servicio.AuthServicio;
import util.Constantes;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;

public class VentanaLogin extends JFrame {

    private JPanel panelPrincipal;
    private JLabel lblTitulo;
    private JLabel lblSubtitulo;
    private JLabel lblNombre;
    private JLabel lblPassword;
    private JTextField txtNombre;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private JButton btnRegistrar;
    private JLabel lblMensaje;

    private JLabel lblConfirmar;
    private JPasswordField txtConfirmar;
    private JLabel lblModoActual;

    private boolean modoRegistro = false;
    private final AuthServicio authServicio;

    public VentanaLogin() {
        this.authServicio = new AuthServicio();
        inicializarUI();
        configurarVentana();
    }

    private void configurarVentana() {
        setTitle("Bright Falls — Acceso");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(Constantes.LOGIN_ANCHO, Constantes.LOGIN_ALTO);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(Constantes.NEGRO);
    }

    private void inicializarUI() {
        panelPrincipal = new JPanel();
        panelPrincipal.setLayout(new GridBagLayout());
        panelPrincipal.setBackground(Constantes.NEGRO);
        panelPrincipal.setBorder(new EmptyBorder(40, 50, 40, 50));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill    = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.insets  = new Insets(6, 0, 6, 0);

        lblTitulo = new JLabel("BRIGHT FALLS");
        lblTitulo.setFont(Constantes.FUENTE_TITULO);
        lblTitulo.setForeground(Constantes.BLANCO);
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panelPrincipal.add(lblTitulo, gbc);

        lblSubtitulo = new JLabel("HORROR GAMING STUDIO");
        lblSubtitulo.setFont(Constantes.FUENTE_SMALL);
        lblSubtitulo.setForeground(Constantes.OCRE);
        lblSubtitulo.setHorizontalAlignment(SwingConstants.CENTER);
        lblSubtitulo.setBorder(new EmptyBorder(0, 0, 20, 0));
        gbc.gridy = 1;
        panelPrincipal.add(lblSubtitulo, gbc);

        lblModoActual = new JLabel("INICIAR SESIÓN");
        lblModoActual.setFont(Constantes.FUENTE_SUBTITULO);
        lblModoActual.setForeground(Constantes.BLANCO);
        lblModoActual.setHorizontalAlignment(SwingConstants.CENTER);
        lblModoActual.setBorder(new EmptyBorder(0, 0, 16, 0));
        gbc.gridy = 2;
        panelPrincipal.add(lblModoActual, gbc);

        gbc.gridwidth = 1; gbc.weightx = 0.35;
        gbc.gridx = 0; gbc.gridy = 3;
        lblNombre = crearEtiqueta("USUARIO");
        panelPrincipal.add(lblNombre, gbc);

        gbc.gridx = 1; gbc.weightx = 0.65;
        txtNombre = crearCampoTexto();
        panelPrincipal.add(txtNombre, gbc);

        gbc.gridx = 0; gbc.gridy = 4; gbc.weightx = 0.35;
        lblPassword = crearEtiqueta("CONTRASEÑA");
        panelPrincipal.add(lblPassword, gbc);

        gbc.gridx = 1; gbc.weightx = 0.65;
        txtPassword = crearCampoPassword();
        panelPrincipal.add(txtPassword, gbc);

        gbc.gridx = 0; gbc.gridy = 5; gbc.weightx = 0.35;
        lblConfirmar = crearEtiqueta("CONFIRMAR");
        lblConfirmar.setVisible(false);
        panelPrincipal.add(lblConfirmar, gbc);

        gbc.gridx = 1; gbc.weightx = 0.65;
        txtConfirmar = crearCampoPassword();
        txtConfirmar.setVisible(false);
        panelPrincipal.add(txtConfirmar, gbc);

        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        lblMensaje = new JLabel(" ");
        lblMensaje.setFont(Constantes.FUENTE_SMALL);
        lblMensaje.setForeground(Constantes.ROJO_ERROR);
        lblMensaje.setHorizontalAlignment(SwingConstants.CENTER);
        panelPrincipal.add(lblMensaje, gbc);

        gbc.gridy = 7; gbc.insets = new Insets(10, 0, 4, 0);
        btnLogin = crearBotonPrimario("ENTRAR");
        panelPrincipal.add(btnLogin, gbc);

        gbc.gridy = 8; gbc.insets = new Insets(4, 0, 6, 0);
        btnRegistrar = crearBotonSecundario("¿No tienes cuenta? REGISTRARSE");
        panelPrincipal.add(btnRegistrar, gbc);

        btnLogin.addActionListener(e -> accionPrincipal());
        btnRegistrar.addActionListener(e -> toggleModo());

        txtPassword.addKeyListener(new KeyAdapter() {
            @Override public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) accionPrincipal();
            }
        });
        txtConfirmar.addKeyListener(new KeyAdapter() {
            @Override public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) accionPrincipal();
            }
        });

        setContentPane(panelPrincipal);
    }

    private void accionPrincipal() {
        limpiarMensaje();
        String nombre = txtNombre.getText().trim();
        String pass   = new String(txtPassword.getPassword());

        try {
            Usuario usuario;
            if (modoRegistro) {
                String confirma = new String(txtConfirmar.getPassword());
                usuario = authServicio.registrar(nombre, pass, confirma);
            } else {
                usuario = authServicio.login(nombre, pass);
            }
            abrirMenu(usuario);

        } catch (IllegalArgumentException ex) {
            mostrarError(ex.getMessage());
        } catch (RuntimeException ex) {
            mostrarError("Error de conexión. Comprueba MySQL.");
        }
    }

    private void toggleModo() {
        modoRegistro = !modoRegistro;
        limpiarMensaje();

        lblModoActual.setText(modoRegistro ? "REGISTRO" : "INICIAR SESIÓN");
        lblConfirmar.setVisible(modoRegistro);
        txtConfirmar.setVisible(modoRegistro);
        btnLogin.setText(modoRegistro ? "CREAR CUENTA" : "ENTRAR");
        btnRegistrar.setText(modoRegistro
                ? "¿Ya tienes cuenta? INICIAR SESIÓN"
                : "¿No tienes cuenta? REGISTRARSE");

        txtNombre.setText("");
        txtPassword.setText("");
        txtConfirmar.setText("");
        txtNombre.requestFocus();
        pack();
        setLocationRelativeTo(null);
    }

    private void abrirMenu(Usuario usuario) {
        SwingUtilities.invokeLater(() -> {
            new VentanaMenu(usuario).setVisible(true);
            VentanaLogin.this.dispose();   // ← referencia explícita a la ventana
        });
    }

    private void mostrarError(String msg) {
        lblMensaje.setForeground(Constantes.ROJO_ERROR);
        lblMensaje.setText(msg);
    }

    private void limpiarMensaje() {
        lblMensaje.setText(" ");
    }

    private JLabel crearEtiqueta(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(Constantes.FUENTE_SMALL);
        lbl.setForeground(Constantes.OCRE);
        return lbl;
    }

    private JTextField crearCampoTexto() {
        JTextField tf = new JTextField();
        tf.setFont(Constantes.FUENTE_TEXTO);
        tf.setBackground(Constantes.GRIS_CLARO);
        tf.setForeground(Constantes.BLANCO);
        tf.setCaretColor(Constantes.OCRE);
        tf.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Constantes.GRIS, 1),
                new EmptyBorder(6, 10, 6, 10)
        ));
        return tf;
    }

    private JPasswordField crearCampoPassword() {
        JPasswordField pf = new JPasswordField();
        pf.setFont(Constantes.FUENTE_TEXTO);
        pf.setBackground(Constantes.GRIS_CLARO);
        pf.setForeground(Constantes.BLANCO);
        pf.setCaretColor(Constantes.OCRE);
        pf.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Constantes.GRIS, 1),
                new EmptyBorder(6, 10, 6, 10)
        ));
        return pf;
    }

    private JButton crearBotonPrimario(String texto) {
        JButton btn = new JButton(texto);
        btn.setFont(Constantes.FUENTE_BTN);
        btn.setBackground(Constantes.GRANATE);
        btn.setForeground(Constantes.BLANCO);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(0, 40));
        btn.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) {
                btn.setBackground(Constantes.OCRE);
                btn.setForeground(Constantes.NEGRO);
            }
            @Override public void mouseExited(MouseEvent e) {
                btn.setBackground(Constantes.GRANATE);
                btn.setForeground(Constantes.BLANCO);
            }
        });
        return btn;
    }

    private JButton crearBotonSecundario(String texto) {
        JButton btn = new JButton(texto);
        btn.setFont(Constantes.FUENTE_SMALL);
        btn.setBackground(Constantes.NEGRO);
        btn.setForeground(Constantes.GRIS);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) {
                btn.setForeground(Constantes.BLANCO);
            }
            @Override public void mouseExited(MouseEvent e) {
                btn.setForeground(Constantes.GRIS);
            }
        });
        return btn;
    }
}