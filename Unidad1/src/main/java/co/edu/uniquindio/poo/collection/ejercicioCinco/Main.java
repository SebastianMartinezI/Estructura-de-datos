package co.edu.uniquindio.poo.collection.ejercicioCinco;

public class Main {
    public static void main(String[] args) {

        GestorProductos gestor = new GestorProductos();

        gestor.agregarProducto(new Producto("103", "monitor"));
        gestor.agregarProducto(new Producto("101", "raton"));
        gestor.agregarProducto(new Producto("102", "teclado"));


        //No garantiza un orden especifico de los elementos y permite busquedas rapidas
        //mediante la clave
        gestor.mostrrarHashMap();

        //Mantiene el orden en que fueron agregados los elementos
        gestor.mostrrarLinkedHashMap();

        //Ordena automaticamente los elementos utilizando la clave
        gestor.mostrrarTreeMap();
    }
}
