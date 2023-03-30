/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package derivadas;

/**
 *
 * @author Gixar
 */
public class Arbol {
    private Nodo raiz;

    public Nodo getRaiz() {
        return raiz;
    }

    public void setRaiz(Nodo raiz) {
        this.raiz = raiz;
    }
    public Arbol(Nodo raiz){
        this.raiz = raiz;
    }
    public Arbol(String exp){
        Nodo n = new Nodo(exp);
        n.setRelacionPad(exp);
        this.raiz = n;
    }
}
