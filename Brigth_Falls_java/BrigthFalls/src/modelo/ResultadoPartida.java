package modelo;

public enum ResultadoPartida {
    VICTORIA_J1("Jugador 1 gana"),
    VICTORIA_J2("Jugador 2 gana"),
    EMPATE("Empate");

    private final String descripcion;

    ResultadoPartida(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() { return descripcion; }
}
