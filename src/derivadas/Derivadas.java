package derivadas;
import java.util.LinkedList;
import java.util.Scanner;

public class Derivadas {
    public static int prioridad(String c){
        return switch (c) {
            case "+", "-" -> 1;
            case "*", "/" -> 2;
            case "e^" -> 3;
            case "^" -> 4;
            case "sen", "cos", "tan", "csc", "sec", "ctg", "ln", "arccot", "raiz", "sin", "cot", "arcsen", "arcsin", "arccos", "arctan"  -> 5;
            case "e^x" -> 6;
            default -> 99999;
        };
    }
    
    public static LinkedList<String> dividirOperacion(String expresion) {
        String expresionOriginal = expresion;
        LinkedList<String> partes = new LinkedList<>();
        char[] letras = expresion.toCharArray();
        String expAct = "";
        String expGen = "";
        String pNodo = "";
        String sNodo = "";
        int par = 0;
        String operadorAct = "";
        int prioridadAct = 9;
        int constante;
        int potencia = 0;
        boolean esNeg = false;
        if(letras[0] == '-'){
            esNeg = true;
            letras = expresion.substring(1).toCharArray();
        }
        String numero = "";
        for(char c:letras){
            String digitos = "1234567890";
            if(!(digitos.contains("" + c))) break;
            numero += c;
            potencia++;
        }
        try{
            constante = Integer.parseInt(numero);
        }catch(Exception e){
            constante = 1;
        }
        constante = esNeg ? constante * -1 : constante;
        expresion = expresion.substring(potencia + (esNeg ? 1 : 0), expresion.length());
        //6+sen(6x)
        //1/sen(x) ==> sen(x)^(-1)
        //e^x
        try{
            if(expresion.charAt(0) == '+' || expresion.charAt(0) == '-'){
                expresion = constante + expresion;
            }
            if(expresion.charAt(0) == '/'){
                expresion = expresion.substring(1);
                return dividirOperacion((constante == 1 ? "" : constante) + "(" + expresion +")^(-1)");
            }
        }catch(Exception e){}
        if(expresion.equals("e^x")){
            operadorAct = "e^x";
            constante  = 1;
            partes.add(operadorAct);
            partes.add("" + constante);
            return partes;
        }
        
        
        letras = expresion.toCharArray();
        for(int i = 0; i < letras.length; i++){
            switch(letras[i]){
                case 'e' -> {
                    if(letras[i+1] == '^' && par == 0){
                        if(prioridad("e^") < prioridadAct){
                            i++;
                            prioridadAct = prioridad("e^");
                            operadorAct = "e^";
                            expGen = "e";
                        }
                    }
                }
                case '(' -> {
                    if(prioridad(expGen) != 99999){
                        prioridadAct = prioridad(expGen);
                        operadorAct = expGen;
                    }
                    par++;
                }
                case ')' -> {
                    par--;
                    //expAct += ')';
                    if(par == 0 && pNodo.isEmpty() && (i + 1) == letras.length){
                        pNodo = expAct;
                    }
                }
                default -> {
                    if((prioridad("" + letras[i]) < prioridadAct) && par == 0){
                        operadorAct = "" + letras[i];
                        prioridadAct = prioridad("" + letras[i]);
                        pNodo += expGen;
                        expGen = "";
                        expAct = "";
                    }
                    
                }
            }
            if(par != 0){
                if(!((par -1) == 0 && letras[i] == '(')){
                    expAct += letras[i];
                }
            }
            expGen += letras[i];
            
            
        }
        String expComp = (constante == 1 ? "" : constante) + expGen;
        if(constante == 0){
            constante = 1;
        }
        char cC;
        
        if((!expComp.equals(expresionOriginal) || expGen.equals("x")) && !expGen.isEmpty() && expGen.charAt(0) != '('){
            sNodo = expGen;
            if(!expGen.equals("x")){
                sNodo = (String) sNodo.subSequence(1, sNodo.length());
            }
        }
        if(operadorAct.equals("^") && pNodo.charAt(0) == '('){
            pNodo = pNodo.substring(1, pNodo.length());
            pNodo = pNodo.substring(0, pNodo.length() - 1);
        }
        if(!operadorAct.isEmpty()){
            partes.add(operadorAct);
        }else{
            try{
                return dividirOperacion(pNodo);
                
            }catch(Exception e){}
        }
        if(!pNodo.isEmpty()){
            partes.add(pNodo);
        }
        if(!sNodo.isEmpty()){
            partes.add(sNodo);
        }
        partes.addLast("" + constante);
        return partes;
    }
    public static Arbol crearArbol(LinkedList<String> partes, Nodo padre){
        Arbol arbol;
        if(padre instanceof Nodo){
            arbol = padre.getArbol();
            padre.setOperador(partes.get(0));
        }else{
            padre = new Nodo(partes.get((0)));
            padre.setRelacionPad(partes.get(0));
            padre.setOperador(partes.get(0));
            arbol = new Arbol(padre);
            padre.setArbol(arbol);
        }
        padre.setConstante(Integer.parseInt(partes.get(partes.size() - 1)));
        for(int i = partes.size() - 1; i  >= 1 ; i--){
            switch(i){
                case 2 -> {
                    padre.setDerecha(new Nodo(padre, partes.get(i)));
                    Nodo derecha = padre.getDerecha();
                    derecha.setIsIzquierda(false);
                    derecha.setRelacionPad(partes.get(0));
                    LinkedList<String> partD = dividirOperacion(derecha.getExp());
                    if(partD.size() > 1){
                        crearArbol(partD, derecha);
                    }
                }
                case 1 -> {
                    padre.setIzquierda(new Nodo(padre, partes.get(i)));
                    Nodo izq = padre.getIzquierda();
                    izq.setIsIzquierda(true);
                    izq.setRelacionPad(partes.get(0));
                    LinkedList<String> partI = dividirOperacion(izq.getExp());
                    if(partI.size() > 1){
                        crearArbol(partI, izq);
                    }
                }   
            }
        }
        return arbol;
    }
    public static String derivar(String exp){
        return "f(x)=" + exp + "\nf'(x)=" +derivar(crearArbol(dividirOperacion(exp),null).getRaiz());
    }
    public static String derivar(Nodo d){
        String derivada;
        if(!(d instanceof Nodo)) return null;
        String derIzq = derivar(d.getIzquierda());
        String derDer = derivar(d.getDerecha());
        
        boolean aplicarCad = false;
        if(d.getOperador() instanceof String){
            aplicarCad = prioridad(d.getOperador()) >= 3 && prioridad(d.getOperador()) != 99999;
        }else{
            d.setOperador("no tiene");
        }
        switch(d.getOperador()){
            case "+", "-" -> {
                
                if(("" + d.getConstante()).equals(d.getDerecha().getExp()) && d.getOperador().equals("-")){
                    derIzq = "-(" + derIzq + ")";
                    d.setOperador("");
                }
                if(derDer.equals("") || derDer.equals("0")){
                    derDer = "";
                    d.setOperador("");
                }
                if(derIzq.equals("") || derIzq.equals("0")){
                    derIzq = "";
                    if(d.getOperador().equals("+")){
                        d.setOperador("");
                    }
                }
                //0+cos(x) ==> cos(x)
                //cos(x) - 9 ==> cos(x)
                //9-cos(x) ==> -(cos(x))
                derivada = derIzq + d.getOperador() + derDer;
                
            }
            case "*" -> {
                String constante = (d.getConstante() == 1 ? "" : "(" + d.getConstante() + ")");
                String dI = (derIzq.equals("1") ? "" : "(" + derIzq + ")*");
                String dD = (derDer.equals("1") ? "" : "(" + derDer + ")*");
                derivada = constante + "(" + dI + "(" + d.getDerecha().getExp() +")+"+dD + "(" + d.getIzquierda().getExp() +"))";
            }
            case "/" -> {
                if(!(derDer instanceof String)){
                    derivada = "0";
                    break;
                }
                if(derDer.equals("0")){
                    derivada = "(" + d.getConstante() + "/" + d.getDerecha().getExp() + ")" + "(" + derIzq + ")"; 
                    if(d.getDerecha().getExp().equals("" + d.getConstante())){
                        String p = "" + d.getConstante()+"(("+d.getIzquierda().getExp()+")^(-1))";
                        derivada = derivar(crearArbol(dividirOperacion("" + d.getConstante()+"("+d.getIzquierda().getExp()+")^(-1)"),null).getRaiz());
                    }
                }else{
                    //derivada = (d.getConstante() != 1 ? ("(" + d.getConstante() + ")") : "") + "((" + (derIzq.equals("1") ? "" : ("(" derIzq + "*" )) + d.getDerecha().getExp() + ")-(" + d.getIzquierda().getExp() + (derDer.equals("1") ? "" : "*" + derDer) + "))/(" + d.getDerecha().getExp() + ")^2";
                    String constante = (d.getConstante() == 1 ? "" : "(" + d.getConstante() + ")");
                    String dI = (derIzq.equals("1") ? "" : "(" + derIzq + ")*");
                    String dD = (derDer.equals("1") ? "" : "(" + derDer + ")*");
                    derivada = constante + "(" + dI + "(" + d.getDerecha().getExp() +")-"+dD + "(" + d.getIzquierda().getExp() +"))/(" + d.getDerecha().getExp() + ")^2";
                }
            }
            case "sen", "sin" -> {
                derivada = "cos(" + d.getIzquierda().getExp() + ")";
            }
            case "cos" -> {
                derivada = "-sen(" + d.getIzquierda().getExp() + ")";
            }
            case "tan" -> {
                derivada = "sec^2(" + d.getIzquierda().getExp() + ")";
            } 
            case "csc" -> {
                derivada = "-ctg(" + d.getIzquierda().getExp() + ")*csc("+ d.getIzquierda().getExp() +")";
            }
            case "sec" -> {
                derivada = "sec(" + d.getIzquierda().getExp() + ")*tan("+ d.getIzquierda().getExp() +")";
            } 
            case "ctg", "cot" -> {
                derivada = "(-csc^2(" + d.getIzquierda().getExp() + "))";
            }
            case "ln" -> {
                derivada = "1/(" + d.getIzquierda().getExp()+")";
            }
            case "e^" -> {
                derivada = "e^(" + d.getIzquierda().getExp() + ")";
            }
            case "e^x" -> {
                derivada = "e^x";
            }
            case "raiz" -> {
                derivada = derivar(crearArbol(dividirOperacion("(" + d.getIzquierda().getExp() + ")^(1/2)"), null).getRaiz());
                aplicarCad = false;
            }
            case "arcsen", "arcsin" -> {
                derivada = "1/raiz(1-(" + d.getIzquierda().getExp() + ")^2)";
            }
            case "arccos" -> {
                derivada = "-1/raiz(1-(" + d.getIzquierda().getExp() + ")^2)";
            }
            case "arctan" -> {
                derivada = "1/(1+(" + d.getIzquierda().getExp() + ")^2)";
            }
            case "^" -> {
                derivada = "";
                int n;
                
                if(d.getDerecha().getExp().charAt(0) == '('){
                    d.getDerecha().setExp(d.getDerecha().getExp().substring(1, d.getDerecha().getExp().length() - 1));
                }
                String exponente = d.getDerecha().getExp();
                try{
                    n = Integer.parseInt(exponente);
                    derivada = (n * d.getConstante()) + "(" + d.getIzquierda().getExp() + ")" + ((n - 1) != 1 ? "^" + (n - 1) : "" );
                    d.setConstante(1);
                }catch(Exception e){
                    if(exponente.contains("x")){
                        derivada = derivar(crearArbol(dividirOperacion("ln("+d.getIzquierda().getExp() + ")*("+exponente+")"),null).getRaiz());
                        derivada = (d.getConstante() != 1 ? "(" + d.getConstante() + ")" : "") + "(" + d.getIzquierda().getExp() + ")^(" + d.getDerecha().getExp() + ")*(" + derivada + ")";
                        derivada = (d.getConstante() == 0 ? "0" : derivada);
                        d.setConstante(1);
                        aplicarCad = false;
                    }else{
                        
                        String temp = "";
                        int a= 0 , b = 0;
                        if(exponente.contains("/")){
                            for(char c:exponente.toCharArray()){
                                if(c == '/'){   
                                    a = Integer.parseInt(temp);
                                    temp = "";
                                }else{
                                    temp += c;
                                }
                            }
                            b = Integer.parseInt(temp);
                            boolean vaAbajo = a < b;
                            int aC = a;
                            a = a - b;
                            if(vaAbajo){
                                a *= -1;
                                derivada = aC + "/(" + b + "(" + d.getIzquierda().getExp() + ")^(" + a + "/" + b + "))";
                            }else{
                                derivada = aC + "(" + d.getIzquierda().getExp() + ")^(" + a + "/" + b + ")" + "/" + b;
                            }
                        }
                    }
                } 
            }
            case "arccot" -> {
                derivada = "-(1/(" + d.getIzquierda().getExp() + ")^2+1)";
            }
            default -> {
                if(d.getExp().contains("x")){
                    derivada = "" + d.getConstante();
                }else{
                    derivada = "0";
                }
            }
        }
        
        if(aplicarCad){
            if(derIzq instanceof String){
                if(d.getConstante() >= 2){
                    derivada = "(" + d.getConstante() + ")" + "(" + derivada + ")";
                }
                if(!derIzq.equals("1") && !derIzq.equals("0")){
                    derivada = "(" + derIzq + ")*(" + derivada + ")";
                }
            }
        }
        return derivada;
    }
    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        System.out.println("Ingresa la funcion a derivar: ");
        String derivada = derivar(s.nextLine());
        System.out.println(derivada);
    }
}