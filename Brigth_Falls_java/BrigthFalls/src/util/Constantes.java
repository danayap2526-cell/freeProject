package util;
import java.awt.*;

public class Constantes {

    public static final Color NEGRO        = new Color(0x1E1E1E);
    public static final Color GRANATE      = new Color(0x532214);
    public static final Color GRIS         = new Color(0x3D505C);
    public static final Color OCRE         = new Color(0xD18E4A);
    public static final Color BLANCO       = new Color(0xF5F0E8);
    public static final Color VERDE_OSCURO = new Color(0x0F2D1A);
    public static final Color ROJO_ERROR   = new Color(0xC0392B);
    public static final Color GRIS_CLARO   = new Color(0x2A2A2A);

    public static final Font FUENTE_TITULO    = new Font("Bebas Neue", Font.PLAIN, 48);
    public static final Font FUENTE_SUBTITULO = new Font("Bebas Neue", Font.PLAIN, 24);
    public static final Font FUENTE_TEXTO     = new Font("Segoe UI",   Font.PLAIN, 14);
    public static final Font FUENTE_SMALL     = new Font("Segoe UI",   Font.PLAIN, 11);
    public static final Font FUENTE_BTN       = new Font("Segoe UI",   Font.BOLD,  13);
    public static final Font FUENTE_CELDA     = new Font("Bebas Neue", Font.PLAIN, 64);


    public static final int VENTANA_ANCHO  = 900;
    public static final int VENTANA_ALTO   = 650;
    public static final int LOGIN_ANCHO    = 480;
    public static final int LOGIN_ALTO     = 560;

    public static final int TAMANYO_TABLERO = 3;

    public static final String DB_URL      = "jdbc:mysql://localhost:3306/brightfalls_db";
    public static final String DB_USUARIO  = "root";
    public static final String DB_PASSWORD = "25mayo2006D";

    private Constantes() {}
}
