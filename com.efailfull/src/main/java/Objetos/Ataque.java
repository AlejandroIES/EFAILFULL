package Objetos;

import java.awt.Image;
import java.util.ArrayList;
import java.util.List;
import Objetos.Efecto;

public class Ataque {

	private String nombre;
	private int potencia;
	private int probabilidad;
	private String tipo;
	private String objetivo;
	private List<Efecto> efectos;

	public Ataque(String nombre, int potencia, int probabilidad, String tipo, String objetivo, List<Efecto> efectos) {
		this.nombre = nombre;
		this.potencia = potencia;
		this.probabilidad = probabilidad;
		this.tipo = tipo;
		this.objetivo = objetivo;
		this.efectos = efectos;
	}

	public Ataque(Ataque ataque) {
		this.nombre = ataque.nombre;
		this.potencia = ataque.potencia;
		this.probabilidad = ataque.probabilidad;
		this.tipo = ataque.tipo;
		this.objetivo = ataque.objetivo;this.efectos = new ArrayList<>();
		
		for (Efecto efecto : ataque.efectos) {
            if (efecto instanceof Modificador) {
                this.efectos.add(new Modificador((Modificador) efecto));
            } else if (efecto instanceof Marca) {
                this.efectos.add(new Marca((Marca) efecto));
            }
        }
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public int getPotencia() {
		return potencia;
	}

	public void setPotencia(int potencia) {
		this.potencia = potencia;
	}

	public int getProbabilidad() {
		return probabilidad;
	}

	public void setProbabilidad(int probabilidad) {
		this.probabilidad = probabilidad;
	}

	public List<Efecto> getEfectos() {
		return efectos;
	}

	public void setEfectos(List<Efecto> efectos) {
		this.efectos = efectos;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getObjetivo() {
		return objetivo;
	}

	public void setObjetivo(String objetivo) {
		this.objetivo = objetivo;
	}

}
