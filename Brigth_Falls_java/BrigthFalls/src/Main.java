
import dades.ConexionDB;
import vista.VentanaLogin;
import javax.swing.*;
void main() {

    try {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch (Exception e) {
        System.err.println("LookAndFeel no disponible: " + e.getMessage());
    }

    SwingUtilities.invokeLater(() -> {
        VentanaLogin login = new VentanaLogin();
        login.setVisible(true);
    });

    Runtime.getRuntime().addShutdownHook(new Thread(ConexionDB::cerrar));

}
