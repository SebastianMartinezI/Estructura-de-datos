package co.edu.uniquindio.poo.collection.ejercicioUno;

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
