package model;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Objects;

public class Estudiante {
    
    private String id;
    private String espolID;
    private String nombre;
    private String apellido;
    private String tarjetaID;
    private String telefono;
    private String saldo;

    public Estudiante(String id,
            String espolID,
            String nombre,
            String apellido,
            String tarjetaID,
            String telefono,
            String saldo) {
        this.id = id;
        this.espolID = espolID;
        this.nombre = nombre;
        this.apellido = apellido;
        this.tarjetaID = tarjetaID;
        this.telefono = telefono;
        this.saldo = saldo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEspolID() {
        return espolID;
    }

    public void setEspolID(String espolID) {
        this.espolID = espolID;
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

    public String getTarjetaID() {
        return tarjetaID;
    }

    public void setTarjetaID(String tarjetaID) {
        this.tarjetaID = tarjetaID;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getSaldo() {
        return saldo;
    }

    public void setSaldo(String saldo) {
        this.saldo = saldo;
    }
    
    public void aumentarSaldo(String incremento) {
        BigDecimal actual = new BigDecimal(this.saldo);
        BigDecimal increm = new BigDecimal(incremento);
        actual = actual.add(increm);
        this.saldo = new DecimalFormat("#0.##").format(actual);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Estudiante other = (Estudiante) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.espolID, other.espolID)) {
            return false;
        }
        if (!Objects.equals(this.nombre, other.nombre)) {
            return false;
        }
        if (!Objects.equals(this.apellido, other.apellido)) {
            return false;
        }
        if (!Objects.equals(this.tarjetaID, other.tarjetaID)) {
            return false;
        }
        if (!Objects.equals(this.telefono, other.telefono)) {
            return false;
        }
        return Objects.equals(this.saldo, other.saldo);
    }
    
    
}
