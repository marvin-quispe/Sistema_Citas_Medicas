package controlador;

import datos.ListaUsuarios;
import modelo.Usuario;

public class UsuarioControlador {

    public static Usuario autenticar(String username, String password) {
        return ListaUsuarios.autenticar(username, password);
    }
}