package Objetos;

public class Modificador extends Efecto {
    private String estadistica;
    private double porcentaje;
    private double variacionPlana;

    public Modificador(String estadistica, double porcentaje, double variacionPlana, 
                       int duracionTurnos, int duracionAtaques, int duracionAcciones) {
        super(duracionTurnos, duracionAtaques, duracionAcciones);
        this.estadistica = estadistica;
        this.porcentaje = porcentaje;
        this.variacionPlana = variacionPlana;
    }
    
    public Modificador(Modificador modificador) {
    	super(modificador);
        this.estadistica = modificador.estadistica;
        this.variacionPlana = modificador.variacionPlana;
        this.porcentaje = modificador.porcentaje;
    }

	@Override
    public Modificador clone() {
        return new Modificador(this); // Usar un constructor de copia
    }

    public String getEstadistica() {
        return estadistica;
    }

    public double getPorcentaje() {
        return porcentaje;
    }

    public double getVariacionPlana() {
        return variacionPlana;
    }

    @Override
    public String getDescripcion() {
        return "Modifica " + estadistica + " en " + porcentaje + "% (" + variacionPlana + ")";
    }
}
