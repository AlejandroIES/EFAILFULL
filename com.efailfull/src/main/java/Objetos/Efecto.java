package Objetos;

public abstract class Efecto {
	protected int duracionTurnos;
	protected int duracionAtaques;
	protected int duracionAcciones;

	public Efecto(int duracionTurnos, int duracionAtaques, int duracionAcciones) {
		this.duracionTurnos = duracionTurnos;
		this.duracionAtaques = duracionAtaques;
		this.duracionAcciones = duracionAcciones;
	}
	
	public Efecto(Efecto efecto) {
		this.duracionTurnos = efecto.duracionTurnos;
		this.duracionAtaques = efecto.duracionAtaques;
		this.duracionAcciones = efecto.duracionAcciones;
	}
	
	public String[] getDuracion() {
		if (duracionTurnos > 0) {
			return new String[]{duracionTurnos + "", "turno"};
		} else if (duracionAtaques > 0) {
			return new String[]{duracionAtaques + "", "ataque"};
		} else {
			return new String[]{duracionAcciones + "", "accion"};
		}
	}

	public void setDuracionTurnos(int duracionTurnos) {
		this.duracionTurnos = duracionTurnos;
	}

	public void setDuracionAtaques(int duracionAtaques) {
		this.duracionAtaques = duracionAtaques;
	}

	public void setDuracionAcciones(int duracionAcciones) {
		this.duracionAcciones = duracionAcciones;
	}

	public int getDuracionTurnos() {
		return duracionTurnos;
	}

	public int getDuracionAtaques() {
		return duracionAtaques;
	}

	public int getDuracionAcciones() {
		return duracionAcciones;
	}

	public abstract String getDescripcion();
}
