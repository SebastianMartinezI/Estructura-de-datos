package co.edu.uniquindio.poo.collection.ejercicioUno;

//Crear la lista de productos en una clase empresa utilizando treeset,
//se debe realizar un método que busque un producto por su código.

public class Main {
    public static void main(String[] args) {

        Empresa miEmpresa = new Empresa();

        miEmpresa.agregarProducto(new Producto("123","Pc", 1500000));
        miEmpresa.agregarProducto(new Producto("234","Mouse", 20000));
        miEmpresa.agregarProducto(new Producto("345","Celular", 3500000));

        Producto productoEncontrado = miEmpresa.buscarProducto("123");
        System.out.println(productoEncontrado);
    }
}
