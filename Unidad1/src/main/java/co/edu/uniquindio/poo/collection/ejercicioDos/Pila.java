package co.edu.uniquindio.poo.collection.ejercicioDos;

public class Pila {
    private Nodo cima;
    public Pila() {
        cima = null;
    }

    public void insertar(Object elemento) {

        if (cima == null) {
            cima = new Nodo(elemento);

            System.out.println("El elemento " + elemento + " ha sido insertado");
        }
        else {
            Class tipoCima = cima.elemento.getClass();

            if (tipoCima.equals(elemento.getClass())) {
                Nodo nuevo = new Nodo(elemento);
                nuevo.proximo = cima;
                cima = nuevo;
                System.out.println("El elemento " + elemento + " ha sido insertado");
            } else {
                System.out.println("El elemento " + elemento + " no es el tipo de cima");
            }
        }
    }
    public Object pop(){
        if (cima == null) {
            System.out.println("Pila esta vacia");
            return null;
        }
        Object valor = cima.elemento;

        cima = cima.proximo;
        return valor;
        }
    public void mostrar(){
        Nodo actual = cima;
        while(actual != null){

            System.out.println(
                    actual.elemento +
                            " (" + actual.elemento.getClass().getSimpleName()+")"
            );

            actual = actual.proximo;
        }
    }
}