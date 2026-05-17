package modelo;
import java.time.LocalDateTime;

public class Partida {

    private int id;
    private int jugador1Id;
    private int jugador2Id;
    private Integer ganadorId;
    private int duracionSegundos;
    private LocalDateTime jugadaEn;


    private String nombreJugador1;
    private String nombreJugador2;
    private String nombreGanador;

    public Partida() {}

    public Partida(int jugador1Id, int jugador2Id) {
        this.jugador1Id = jugador1Id;
        this.jugador2Id = jugador2Id;
    }

    public int getId()                          { return id; }
    public void setId(int id)                  { this.id = id; }

    public int getJugador1Id()                  { return jugador1Id; }
    public void setJugador1Id(int id)          { this.jugador1Id = id; }

    public int getJugador2Id()                  { return jugador2Id; }
    public void setJugador2Id(int id)          { this.jugador2Id = id; }

    public Integer getGanadorId()               { return ganadorId; }
    public void setGanadorId(Integer id)       { this.ganadorId = id; }

    public int getDuracionSegundos()            { return duracionSegundos; }
    public void setDuracionSegundos(int d)     { this.duracionSegundos = d; }

    public LocalDateTime getJugadaEn()          { return jugadaEn; }
    public void setJugadaEn(LocalDateTime t)   { this.jugadaEn = t; }

    public String getNombreJugador1()           { return nombreJugador1; }
    public void setNombreJugador1(String n)    { this.nombreJugador1 = n; }

    public String getNombreJugador2()           { return nombreJugador2; }
    public void setNombreJugador2(String n)    { this.nombreJugador2 = n; }

    public String getNombreGanador()            { return nombreGanador; }
    public void setNombreGanador(String n)     { this.nombreGanador = n; }

    public String getResultadoTexto() {
        if (ganadorId == null)          return "Empate";
        if (ganadorId == jugador1Id)    return nombreJugador1 + " gana";
        return nombreJugador2 + " gana";
    }
}
