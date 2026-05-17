package modelo;
import java.time.LocalDateTime;
public class Usuario {


    private int id;
    private String nombre;
    private String passwordHash;
    private int victorias;
    private int derrotas;
    private int empates;
    private LocalDateTime creadoEn;

    public Usuario() {}

    public Usuario(String nombre, String passwordHash) {
        this.nombre       = nombre;
        this.passwordHash = passwordHash;
    }

    public int getId()                       { return id; }
    public void setId(int id)               { this.id = id; }

    public String getNombre()               { return nombre; }
    public void setNombre(String nombre)    { this.nombre = nombre; }

    public String getPasswordHash()         { return passwordHash; }
    public void setPasswordHash(String h)  { this.passwordHash = h; }

    public int getVictorias()               { return victorias; }
    public void setVictorias(int v)        { this.victorias = v; }

    public int getDerrotas()                { return derrotas; }
    public void setDerrotas(int d)         { this.derrotas = d; }

    public int getEmpates()                 { return empates; }
    public void setEmpates(int e)          { this.empates = e; }

    public LocalDateTime getCreadoEn()      { return creadoEn; }
    public void setCreadoEn(LocalDateTime t){ this.creadoEn = t; }

    public int getTotalPartidas() {
        return victorias + derrotas + empates;
    }

    @Override
    public String toString() {
        return nombre + " [V:" + victorias + " D:" + derrotas + " E:" + empates + "]";
    }

}
