package ej6;

import java.io.Serializable;

public class Numeros implements Serializable {
    private int numero;
    private long cuadrado = 0;
    private long cubo = 0;

    public Numeros(int n) {
        this.numero = n;
    }

    public Numeros() {

    }

    public int getNumero() {
        return numero;
    }

    public long getCuadrado() {
        return cuadrado;
    }

    public long getCubo() {
        return cubo;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public void setCuadrado(long cuadrado) {
        this.cuadrado = cuadrado;
    }

    public void setCubo(long cubo) {
        this.cubo = cubo;
    }
}
