package modelo;

public class Paciente extends Persona {

    private String telefono;
    private String correo;
    private String fechaNacimiento; // formato: yyyy-MM-dd
    private String sexo;            // "Masculino" o "Femenino"
    private String tipoSeguro;      // SIS, EsSalud, Particular, SOAT

    public Paciente() {
    }

    public Paciente(int id, String nombre, String apellido, String dni,
                    String telefono, String correo, String fechaNacimiento,
                    String sexo, String tipoSeguro) {
        super(id, nombre, apellido, dni);
        this.telefono = telefono;
        this.correo = correo;
        this.fechaNacimiento = fechaNacimiento;
        this.sexo = sexo;
        this.tipoSeguro = tipoSeguro;
    }

    // Polimorfismo: sobreescribe el método abstracto de Persona
    @Override
    public void mostrarDatos() {
        System.out.println("=== DATOS DEL PACIENTE ===");
        System.out.println("ID       : " + id);
        System.out.println("Nombre   : " + getNombreCompleto());
        System.out.println("DNI      : " + dni);
        System.out.println("Teléfono : " + telefono);
        System.out.println("Correo   : " + correo);
        System.out.println("F. Nac.  : " + fechaNacimiento);
        System.out.println("Sexo     : " + sexo);
        System.out.println("Seguro   : " + tipoSeguro);
    }

    // Getters y Setters
    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getTipoSeguro() {
        return tipoSeguro;
    }

    public void setTipoSeguro(String tipoSeguro) {
        this.tipoSeguro = tipoSeguro;
    }

    @Override
    public String toString() {
        return getNombreCompleto() + " (" + dni + ")";
    }
}