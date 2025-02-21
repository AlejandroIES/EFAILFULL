package Objetos;

import java.util.List;
import java.util.ArrayList;

public class Marca extends Efecto {
    private String id;
    private String nombre;
    private String descripcion;
    private int duracionTurnos;
    private int duracionAtaques;
    private int duracionAcciones;
    private List<Efecto> efectos; // Lista de efectos asociados a la marca

    // Constructor
    public Marca(String id, String nombre, String descripcion, int duracionTurnos, int duracionAtaques, int duracionAcciones) {
        super(duracionTurnos, duracionAtaques, duracionAcciones);
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.efectos = new ArrayList<>();
    }
    
    public Marca(Marca marca) {
    	super(marca);
    	this.id = marca.id;
    	this.nombre = marca.nombre;
    	this.descripcion = marca.descripcion;
    	this.efectos = new ArrayList<>();
    }

    // Métodos para gestionar la lista de efectos
    public void agregarEfecto(Efecto efecto) {
        this.efectos.add(efecto);
    }

    public List<Efecto> getEfectos() {
        return this.efectos;
    }

    // Getters y setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getDuracionTurnos() {
        return duracionTurnos;
    }

    public void setDuracionTurnos(int duracionTurnos) {
        this.duracionTurnos = duracionTurnos;
    }

    public int getDuracionAtaques() {
        return duracionAtaques;
    }

    public void setDuracionAtaques(int duracionAtaques) {
        this.duracionAtaques = duracionAtaques;
    }

    public int getDuracionAcciones() {
        return duracionAcciones;
    }

    public void setDuracionAcciones(int duracionAcciones) {
        this.duracionAcciones = duracionAcciones;
    }
}
