package co.edu.uniquindio.poo.collection.ejercicioSeis;

public class Main {

    //En una tienda se necesita una aplicación para gestionar el inventario de
    // productos(codigo, nombre, precio), permitiendo agregar nuevos artículos,
    // eliminar los que están agotados, buscar productos específicos y listar
    // todo el inventario en orden alfabetico y por orden de precio.
    // Para ello, se utilizará una ArrayList, que ofrece acceso rápido a los
    // elementos y permite su manipulación de manera eficiente.

    public static void main(String[] args) {


        Inventario inventario = new Inventario();

        inventario.agregarProducto(new Producto(1, "Raton", 45000, true));

        inventario.agregarProducto(new Producto(2, "Teclado", 80000, true));

        inventario.agregarProducto(new Producto(3, "Monitor", 650000, false));

        inventario.agregarProducto(new Producto(4, "Audifonos", 120000, true));


        System.out.println("Inventario Inicial:");
        inventario.mostrarProductos();

        System.out.println("\nBusqueda de productos:");
        inventario.buscarProducto("Teclado");

        System.out.println("\nProductos ordenados por nombre:");

        inventario.ordenarPorNombre();
        inventario.mostrarProductos();

        System.out.println("\nProdcutos ordenados por precio:");

        inventario.ordenarPorPrecio();
        inventario.mostrarProductos();

        System.out.println("\nEliminando productos agotados:");

        inventario.eliminarAgotados();
        inventario.mostrarProductos();

    }
}