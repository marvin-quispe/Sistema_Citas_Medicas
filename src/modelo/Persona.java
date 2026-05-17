package modelo;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

public abstract class Persona {
	protected int id;
	protected String nombre;
	protected String apellido;
	protected String dni;
	
	public Persona()
	{
		
	}
	public Persona(int id, String nombre, String apellido, String dni) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
    }
	
	// Método abstracto — obliga a Paciente y Medico a implementarlo
    // Esto demuestra polimorfismo
    public abstract void mostrarDatos();

 // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }
    
    // Calcula la edad a partir de la fecha de nacimiento (formato yyyy-MM-dd)
    // Se usa como campo informativo en las vistas, no como dato persistente
    public static int calcularEdad(String fechaNacimiento) {
        if (fechaNacimiento == null || fechaNacimiento.isEmpty()) return 0;
        try {
            LocalDate nacimiento = LocalDate.parse(fechaNacimiento, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            return Period.between(nacimiento, LocalDate.now()).getYears();
        } catch (Exception e) {
            return 0;
        }
    }
    // Método concreto compartido por todas las subclases
    public String getNombreCompleto() {
        return nombre + " " + apellido;
    }
}
