package modelo;

public class Usuario {

    private int id;
    private String username;
    private String password;
    private String rol;
    private String nombre;
    private int entidadId;

    public static final String ADMIN = "Administrador";
    public static final String RECEPCIONISTA = "Recepcionista";
    public static final String MEDICO = "Médico";
    public static final String CAJERO = "Cajero";
    public static final String PACIENTE = "Paciente";

    public Usuario() {
    }

    public Usuario(int id, String username, String password, String rol, String nombre) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.rol = rol;
        this.nombre = nombre;
        this.entidadId = 0;
    }

    public Usuario(int id, String username, String password, String rol, String nombre, int entidadId) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.rol = rol;
        this.nombre = nombre;
        this.entidadId = entidadId;
    }

    public boolean autenticar(String user, String pass) {
        return this.username.equals(user) && this.password.equals(pass);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return nombre + " (" + rol + ")";
    }

    public int getEntidadId() {
        return entidadId;
    }

    public void setEntidadId(int entidadId) {
        this.entidadId = entidadId;
    }

    public boolean esAdmin() {
        return ADMIN.equals(this.rol);
    }

    public boolean esMedico() {
        return MEDICO.equals(this.rol);
    }

    public boolean esPaciente() {
        return PACIENTE.equals(this.rol);
    }

    public boolean tieneEnlaceEntidad() {
        return entidadId > 0;
    }
}