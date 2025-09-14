package modelo;

public class Cliente {

private String dni;
private String nombre;
private String apellido;
private String telefono;
private String correo; 

    // Constructor
 public Cliente(String dni, String nombre, String apellido, String telefono, String correo) {
        this.dni = dni; 
        this.nombre = nombre; 
        this.apellido = apellido;
        this.telefono = telefono; 
        this.correo = correo;
    }
    // Getters
    public String getDni() { return dni; }
    public String getNombre() { return nombre; }
    public String getApellido() { return apellido; }
    public String getNomberCompleto () { return apellido + " " + nombre; }
    public String getTelefono() { return telefono; }
    public String getCorreo() { return correo; }
    // Setters
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setApellido(String apellido) { this.apellido = apellido; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public void setCorreo(String correo) { this.correo = correo; }
    // toString
@Override
    public String toString() {
        return "Cliente{" + "dni: " + dni + ", nombre: " + nombre + ", apellido: " + apellido + ", telefono: " + telefono + ", correo: " + correo + '}';
    }
}
