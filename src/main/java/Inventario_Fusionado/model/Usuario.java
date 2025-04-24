// Asegúrate que el paquete sea el correcto
package Inventario_Fusionado.model;

public class Usuario {
    private int idUsuario;
    private String username;
    private String password; // Almacenaremos la contraseña aquí temporalmente

    // Constructor vacío
    public Usuario() {
    }

    // Constructor con todos los campos (útil para crear objetos desde la BD)
    public Usuario(int idUsuario, String username, String password) {
        this.idUsuario = idUsuario;
        this.username = username;
        this.password = password;
    }

    // Constructor sin ID (útil para crear nuevos usuarios antes de insertarlos)
    public Usuario(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Getters y Setters para todos los campos
    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
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

    @Override
    public String toString() {
        // No incluimos la contraseña en toString por seguridad básica
        return "Usuario{" +
                "idUsuario=" + idUsuario +
                ", username='" + username + '\'' +
                '}';
    }
}