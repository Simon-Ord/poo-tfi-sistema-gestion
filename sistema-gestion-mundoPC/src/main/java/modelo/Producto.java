package modelo;

public class Producto {

private Integer id;    
private String nombre;
private String descripcion;
private Double precio;
private int stock;

    // Constructor
 public Producto(Integer id, String nombre, String descripcion, double precio, int stock) {
        this.id = id; 
        this.nombre = nombre; 
        this.descripcion = descripcion;
        this.precio = precio; 
        this.stock = stock;
    }
    // Getters
    public Integer getIdProducto() { return id; }
    public String getNombreProducto() { return nombre; }
    public String getDescripcionProducto() { return descripcion; }
    public double getPrecio() { return precio; }
    public int getStock() { return stock; }
    // Setters
    public void setNombreProducto(String nombre) { this.nombre = nombre; }
    public void setDescripcionProducto(String descripcion) { this.descripcion = descripcion; }
    public void setPrecio(double precio) { this.precio = precio; }
    public void setStock(int stock) { this.stock = stock; }
    // toString
@Override
    public String toString() {
        return "Producto{" + "id: " + id + ", nombre: " + nombre + ", descripcion: " + descripcion + ", precio: " + precio + ", stock: " + stock + '}';
    }    
}
