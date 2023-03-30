/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package derivadas;

/**
 *
 * @author Gixar
 */
public class Nodo {
    private Nodo padre;
    private String relacionPad = "";
    private String exp;
    private String operador;
    private int constante;

    public int getConstante() {
        return constante;
    }

    public void setConstante(int constante) {
        this.constante = constante;
    }
    public String getOperador() {
        return operador;
    }

    public void setOperador(String operador) {
        this.operador = operador;
    }
    private Nodo izquierda, derecha;
    private boolean isIzquierda;

    public boolean IsIzquierda() {
        return isIzquierda;
    }

    public void setIsIzquierda(boolean isIzquierda) {
        this.isIzquierda = isIzquierda;
    }
    private Arbol arbol;

    public Arbol getArbol() {
        return arbol;
    }

    public void setArbol(Arbol arbol) {
        this.arbol = arbol;
    }
    public String getRelacionPad() {
        return relacionPad;
    }

    public void setRelacionPad(String relacionPad) {
        this.relacionPad = relacionPad;
    }
    
    public Nodo(){
        this.padre = null;
        this.exp = "";
        this.izquierda = null;
        this.derecha = null;
    }
    public Nodo(Arbol a){
        this.arbol = a;
    }
    public Nodo(Nodo padre){
        this.arbol = padre.getArbol();
        this.padre = padre;
        this.exp = "";
        this.izquierda = null;
        this.derecha = null;
    }
    public Nodo(Nodo padre, String exp){
        this.arbol = padre.getArbol();
        this.padre = padre;
        this.exp = exp;
        this.izquierda = null;
        this.derecha = null;
    }
    public Nodo(Nodo izquierda, Nodo derecha){
        
        this.izquierda = izquierda;
        this.derecha = derecha;
        exp = "";
        padre = null;
    }
    public Nodo(Nodo izquierda, Nodo derecha, Nodo padre){
        this.arbol = padre.getArbol();
        this.izquierda = izquierda;
        this.derecha = derecha;
        this.exp = "";
        this.padre = padre;
    }
    public Nodo(String exp){
        this.exp = exp;
        this.padre = null;
        this.izquierda = null;
        this.derecha = null;
    }
    public Nodo(Nodo izquierda, Nodo derecha, Nodo padre, String exp){
        this.arbol = padre.getArbol();
        this.izquierda = izquierda;
        this.derecha = derecha;
        this.exp = exp;
        this.padre = padre;
    }

    public Nodo getPadre() {
        return padre;
    }

    public void setPadre(Nodo padre) {
        this.padre = padre;
    }

    public String getExp() {
        return exp;
    }

    public void setExp(String exp) {
        this.exp = exp;
    }

    public Nodo getIzquierda() {
        return izquierda;
    }

    public void setIzquierda(Nodo izquierda) {
        this.izquierda = izquierda;
    }

    public Nodo getDerecha() {
        return derecha;
    }

    public void setDerecha(Nodo derecha) {
        this.derecha = derecha;
    }
    
}
