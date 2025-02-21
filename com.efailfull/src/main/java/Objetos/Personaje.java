package Objetos;

import java.util.ArrayList;
import java.util.List;

import Vista.Combate;

public class Personaje {

	// Atributos
	protected int nivel;
	protected String clase;
	protected int energia;
	protected int energiaRestante;
	protected int vida;
	protected int vidaRestante;
	protected int ataque;
	protected int defensa;
	protected int bloqueo;
	protected double probabilidadCritico;
	protected double danoCritico;
	protected List<Efecto> efectos = new ArrayList<Efecto>();

	// Constructor
	public Personaje(int id, String clase, int energia, int energiaRestante, int vida, int vidaRestante, int ataque,
			int defensa, int bloqueo, double probabilidadCritico, double danoCritico) {
		this.nivel = id;
		this.clase = clase;
		this.energia = energia;
		this.energiaRestante = energiaRestante;
		this.vida = vida;
		this.vidaRestante = vidaRestante;
		this.ataque = ataque;
		this.defensa = defensa;
		this.bloqueo = bloqueo;
		this.probabilidadCritico = probabilidadCritico;
		this.danoCritico = danoCritico;
	}

	public int getNivel() {
		return nivel;
	}

	public void setNivel(int nivel) {
		this.nivel = nivel;
	}

	public String getClase() {
		return clase;
	}

	public void setClase(String clase) {
		this.clase = clase;
	}

	public int getEnergia() {
		return energia;
	}

	public void setEnergia(int energia) {
		this.energia = energia;
	}

	public int getEnergiaRestante() {
		return energiaRestante;
	}

	public void setEnergiaRestante(int energiaRestante) {
		this.energiaRestante = energiaRestante;
	}

	public int getVida() {
		return vida;
	}

	public void setVida(int vida) {
		this.vida = vida;
	}

	public int getVidaRestante() {
		return vidaRestante;
	}

	public void setVidaRestante(int vidaRestante) {
		this.vidaRestante = vidaRestante;
	}

	public int getAtaque() {
		return ataque;
	}

	public void setAtaque(int ataque) {
		this.ataque = ataque;
	}

	public int getDefensa() {
		return defensa;
	}

	public void setDefensa(int defensa) {
		this.defensa = defensa;
	}

	public int getBloqueo() {
		return bloqueo;
	}

	public void setBloqueo(int bloqueo) {
		this.bloqueo = bloqueo;
	}

	public double getProbabilidadCritico() {
		return probabilidadCritico;
	}

	public void setProbabilidadCritico(double probabilidadCritico) {
		this.probabilidadCritico = probabilidadCritico;
	}

	public double getDanoCritico() {
		return danoCritico;
	}

	public void setDanoCritico(double danoCritico) {
		this.danoCritico = danoCritico;
	}

	public List<Efecto> getEfectos() {
		return efectos;
	}

	public void setEfectos(List<Efecto> efectos) {
		this.efectos = efectos;
	}

	// Método para agregar un efecto
	public void agregarEfecto(Combate combate, Efecto efecto) {
		boolean esBloqueo = false;
		if (efecto instanceof Modificador) {
			Modificador mod = (Modificador) efecto;
			if (mod.getEstadistica().equals("Bloqueo")) {
				this.setBloqueo((int) (this.getBloqueo() + mod.getVariacionPlana()));
				combate.bloqueoJugadorLabel.setText((this.getBloqueo() == 0) ? "" : this.getBloqueo() + "");
				
			}
		}
		if (!esBloqueo) {
			this.efectos.add(efecto);
		}
	}

	// Método para remover un efecto
	public void removerEfecto(Efecto efecto) {
		this.efectos.remove(efecto);
	}

	public void curar(int cantidad) {
		this.vidaRestante += cantidad;

		if (this.vidaRestante > this.vida) {
			this.vidaRestante = this.vida;
		}
	}

	public void aumentaNivel(int cantidad) {
		this.nivel += cantidad;
	}

}
