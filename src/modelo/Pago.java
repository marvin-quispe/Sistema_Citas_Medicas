package modelo;

public class Pago {

    private int id;
    private Cita cita;
    private double monto;
    private String metodoPago;  // Efectivo, Tarjeta, EsSalud, SIS, SOAT
    private String fecha;       // formato: yyyy-MM-dd

    public Pago() {
    }

    public Pago(int id, Cita cita, double monto,
                String metodoPago, String fecha) {
        this.id = id;
        this.cita = cita;
        this.monto = monto;
        this.metodoPago = metodoPago;
        this.fecha = fecha;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Cita getCita() {
        return cita;
    }

    public void setCita(Cita cita) {
        this.cita = cita;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    @Override
    public String toString() {
        return "Pago #" + id + " | S/ " + String.format("%.2f", monto)
               + " | " + metodoPago + " | " + fecha;
    }
}