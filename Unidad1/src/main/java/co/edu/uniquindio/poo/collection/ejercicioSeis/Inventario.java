package co.edu.uniquindio.poo.collection.ejercicioSeis;

import java.util.ArrayList;
import java.util.Comparator;

public class Inventario {

    private ArrayList<Producto> productos;
    public Inventario() {
        productos = new ArrayList<>();
    }
    public void agregarProducto(Producto producto) {
        productos.add(producto);
    }
    public  void eliminarAgotados() {
        productos.removeIf(producto -> !producto.isDisponible());
    }

    public void buscarProducto(String nombre) {
        for (Producto producto : productos) {
            if (producto.getNombre().equalsIgnoreCase(nombre)) {
                System.out.println("Pruducto encontrado");
                System.out.println(producto);
                return;
            }
        }
        System.out.println("No se encontro el producto");
    }

    public void mostrarProductos() {
        for (Producto producto : productos) {
            System.out.println(producto);
        }
    }
    public void ordenarPorNombre() {
        productos.sort(Comparator.comparing(Producto::getNombre));
    }

    public void ordenarPorPrecio() {
        productos.sort(Comparator.comparingDouble(Producto::getPrecio));
    }
}
