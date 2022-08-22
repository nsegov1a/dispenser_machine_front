package model;

public class Producto {
    
    private String id;
    private String descripcion;
    private String precio;
    private String codigo;

    public Producto(String id, String descripcion, String precio, String codigo) {
        this.id = id;
        this.descripcion = descripcion;
        this.precio = precio;
        this.codigo = codigo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
}
