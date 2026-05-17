package servicio;


import dades.PartidaDB;
import dades.UsuarioDB;
import modelo.Partida;
import modelo.Usuario;
import util.Constantes;
import java.sql.SQLException;
public class JuegoServicio {


    public static final char VACIA   = ' ';
    public static final char FICHA_1 = 'X';
    public static final char FICHA_2 = 'O';


    private char[][] tablero;
    private char turnoActual;
    private boolean partidaTerminada;
    private char ganadorFicha;      // ' ' = empate, 'X' o 'O'

    private Usuario jugador1;
    private Usuario jugador2;
    private long tiempoInicio;

    private final PartidaDB partidaDB;
    private final UsuarioDB usuarioDB;

    public JuegoServicio() {
        this.partidaDB = new PartidaDB();
        this.usuarioDB = new UsuarioDB();
    }


    public void iniciarPartida(Usuario jugador1, Usuario jugador2) {
        this.jugador1        = jugador1;
        this.jugador2        = jugador2;
        this.tablero         = new char[Constantes.TAMANYO_TABLERO][Constantes.TAMANYO_TABLERO];
        this.turnoActual     = FICHA_1;
        this.partidaTerminada = false;
        this.ganadorFicha    = VACIA;
        this.tiempoInicio    = System.currentTimeMillis();


        for (int f = 0; f < Constantes.TAMANYO_TABLERO; f++) {
            for (int c = 0; c < Constantes.TAMANYO_TABLERO; c++) {
                tablero[f][c] = VACIA;
            }
        }
    }


    public EstadoJuego hacerMovimiento(int fila, int col) {
        if (partidaTerminada) {
            return EstadoJuego.PARTIDA_YA_TERMINADA;
        }
        if (!celdaValida(fila, col)) {
            return EstadoJuego.MOVIMIENTO_INVALIDO;
        }
        if (tablero[fila][col] != VACIA) {
            return EstadoJuego.CELDA_OCUPADA;
        }

        tablero[fila][col] = turnoActual;

        if (hayGanador(turnoActual)) {
            ganadorFicha    = turnoActual;
            partidaTerminada = true;
            return EstadoJuego.GANADOR;
        }
        if (tableroLleno()) {
            partidaTerminada = true;
            return EstadoJuego.EMPATE;
        }


        turnoActual = (turnoActual == FICHA_1) ? FICHA_2 : FICHA_1;
        return EstadoJuego.CONTINUA;
    }


    public void guardarResultado() {
        if (!partidaTerminada) return;

        int duracion = (int) ((System.currentTimeMillis() - tiempoInicio) / 1000);

        Partida partida = new Partida(jugador1.getId(), jugador2.getId());
        partida.setDuracionSegundos(duracion);

        Usuario ganador  = null;
        Usuario perdedor = null;

        if (ganadorFicha == FICHA_1) {
            partida.setGanadorId(jugador1.getId());
            ganador  = jugador1;
            perdedor = jugador2;
        } else if (ganadorFicha == FICHA_2) {
            partida.setGanadorId(jugador2.getId());
            ganador  = jugador2;
            perdedor = jugador1;
        }


        try {
            partidaDB.insertar(partida);

            if (ganador != null) {
                ganador.setVictorias(ganador.getVictorias() + 1);
                perdedor.setDerrotas(perdedor.getDerrotas() + 1);
                usuarioDB.actualizarEstadisticas(
                        ganador.getId(),
                        ganador.getVictorias(),
                        ganador.getDerrotas(),
                        ganador.getEmpates()
                );
                usuarioDB.actualizarEstadisticas(
                        perdedor.getId(),
                        perdedor.getVictorias(),
                        perdedor.getDerrotas(),
                        perdedor.getEmpates()
                );
            } else {
                // Empate: +1 a ambos
                jugador1.setEmpates(jugador1.getEmpates() + 1);
                jugador2.setEmpates(jugador2.getEmpates() + 1);
                usuarioDB.actualizarEstadisticas(
                        jugador1.getId(),
                        jugador1.getVictorias(),
                        jugador1.getDerrotas(),
                        jugador1.getEmpates()
                );
                usuarioDB.actualizarEstadisticas(
                        jugador2.getId(),
                        jugador2.getVictorias(),
                        jugador2.getDerrotas(),
                        jugador2.getEmpates()
                );
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar la partida: " + e.getMessage(), e);
        }
    }


    private boolean hayGanador(char ficha) {
        int N = Constantes.TAMANYO_TABLERO;


        for (int i = 0; i < N; i++) {
            if (lineaCompleta(ficha,
                    tablero[i][0], tablero[i][1], tablero[i][2])) return true;
            if (lineaCompleta(ficha,
                    tablero[0][i], tablero[1][i], tablero[2][i])) return true;
        }

        if (lineaCompleta(ficha,
                tablero[0][0], tablero[1][1], tablero[2][2])) return true;
        if (lineaCompleta(ficha,
                tablero[0][2], tablero[1][1], tablero[2][0])) return true;

        return false;
    }

    private boolean lineaCompleta(char ficha, char a, char b, char c) {
        return a == ficha && b == ficha && c == ficha;
    }

    private boolean tableroLleno() {
        for (char[] fila : tablero) {
            for (char c : fila) {
                if (c == VACIA) return false;
            }
        }
        return true;
    }

    private boolean celdaValida(int fila, int col) {
        int N = Constantes.TAMANYO_TABLERO;
        return fila >= 0 && fila < N && col >= 0 && col < N;
    }


    public char[][] getTablero()          { return tablero; }
    public char getTurnoActual()          { return turnoActual; }
    public boolean isPartidaTerminada()   { return partidaTerminada; }
    public char getGanadorFicha()         { return ganadorFicha; }
    public Usuario getJugador1()          { return jugador1; }
    public Usuario getJugador2()          { return jugador2; }

    public Usuario getJugadorActual() {
        return turnoActual == FICHA_1 ? jugador1 : jugador2;
    }

    public Usuario getJugadorGanador() {
        if (ganadorFicha == FICHA_1) return jugador1;
        if (ganadorFicha == FICHA_2) return jugador2;
        return null; // empate
    }


    public enum EstadoJuego {
        CONTINUA,
        GANADOR,
        EMPATE,
        CELDA_OCUPADA,
        MOVIMIENTO_INVALIDO,
        PARTIDA_YA_TERMINADA
    }
}
