package Objetos;

import java.util.ArrayList;
import java.util.List;

import BDD.Utilidades;

public class Enemigo {

	private int id;
	private int nivel;
	private String nombre;
	private String elemento;
	private int vida;
	private int vidaRestante;
	private int ataque;
	private int defensa;
	private int bloqueo;
	private double probabilidadCritico;
	private double danioCritico;

	private List<Efecto> efectos = new ArrayList<Efecto>();
	private List<Ataque> ataques = new ArrayList<Ataque>();
	private String intencion;

	/**
	 * 
	 * @param id
	 * @param nombre
	 * @param elemento
	 * @param vida
	 * @param vidaRestante
	 * @param ataque
	 * @param defensa
	 * @param bloqueo
	 * @param probabilidadCritico
	 * @param danioCritico
	 * @param efectos
	 * @param intencion
	 */
	public Enemigo(int id, int nivel, String nombre, String elemento, int vida, int vidaRestante, int ataque,
			int defensa, int bloqueo, double probabilidadCritico, double danioCritico, String intencion) {
		this.id = id;
		this.nombre = nombre;
		this.elemento = elemento;
		this.vida = vida;
		this.vidaRestante = vidaRestante;
		this.ataque = ataque;
		this.defensa = defensa;
		this.bloqueo = bloqueo;
		this.probabilidadCritico = probabilidadCritico;
		this.danioCritico = danioCritico;
		this.intencion = intencion;
		this.nivel = nivel;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getElemento() {
		return elemento;
	}

	public void setElemento(String elemento) {
		this.elemento = elemento;
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

	public double getDanioCritico() {
		return danioCritico;
	}

	public void setDanioCritico(double danioCritico) {
		this.danioCritico = danioCritico;
	}

	public List<Efecto> getEfectos() {
		return efectos;
	}

	public void setEfectos(List<Efecto> efectos) {
		this.efectos = efectos;
	}

	public List<Ataque> getAtaques() {
		return ataques;
	}

	public void setAtaques(List<Ataque> ataques) {
		this.ataques = ataques;
	}

	public String getIntencion() {
		return intencion;
	}

	public void setIntencion(String intencion) {
		this.intencion = intencion;
	}

	public int getNivel() {
		return nivel;
	}

	public void setNivel(int nivel) {
		this.nivel = nivel;
	}

	// Método para agregar un efecto
	public void agregarEfecto(Efecto efecto) {
		this.efectos.add(efecto);
	}

	// Método para remover un efecto
	public void removerEfecto(Efecto efecto) {
		this.efectos.remove(efecto);
	}

	// Método para agregar un ataque
	public void agregarAtaque(Ataque ataque) {
		this.ataques.add(ataque);
	}

	// Método para remover un ataque
	public void removerAtaque(Ataque ataque) {
		this.ataques.remove(ataque);
	}

	public void asignaAtaques() {
		this.ataques.clear();
		List<String> ataquesEnemigo;
		String sql = "SELECT ae.IdAtaque FROM ataqueEnemigoBase ae INNER JOIN EnemigoBase e ON e.id = ae.IdEnemigoBase where e.id = ?";
		ataquesEnemigo = Utilidades.consulta(sql, this.id + "");

		for (int i = 0; i < ataquesEnemigo.size(); i++) {
			List<String> datosAtaque = Utilidades.consulta(
					"SELECT Nombre, Potencia, Probabilidad, tipo, objetivo from ataque where id = ?", ataquesEnemigo.get(i));
			List<Efecto> efectosAtaque = Utilidades.detallesEfectosAtaques(ataquesEnemigo.get(i));

			Ataque ataqueEnemigo = new Ataque(datosAtaque.get(0), Integer.parseInt(datosAtaque.get(1)),
					Integer.parseInt(datosAtaque.get(2)), datosAtaque.get(3), datosAtaque.get(4), efectosAtaque);
			this.agregarAtaque(ataqueEnemigo);
		}

	}

}
