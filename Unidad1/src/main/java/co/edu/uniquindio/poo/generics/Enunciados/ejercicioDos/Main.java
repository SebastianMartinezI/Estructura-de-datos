package co.edu.uniquindio.poo.generics.Enunciados.ejercicioDos;

//DirectorioContactos con orden natural por nombre
//Crear la clase Contacto (nombre, teléfono, email) con orden natural por nombre (Comparable<Contacto>).
// Diseñar DirectorioContactos que mantenga una LinkedList<Contacto>. Implementar:
//*Búsqueda de contactos cuyo email termine en un dominio dado, solo con Iterator.
//*Un método que ordene por teléfono usando un Comparator.

public class Main {
    public static void main(String[] args) {
        DirectorioContacto directorio = new DirectorioContacto();

        directorio.agregarContactos(new Contacto("Sebastian", "3210215512", "sebas@uq.edu"));
        directorio.agregarContactos(new Contacto("Carlos", "3201745011", "luis@uq.edu"));
        directorio.agregarContactos(new Contacto("Maria", "3152102511", "maria@correo.com"));

        System.out.println("Contactos de la UQ");
        System.out.println(directorio.buscarPorDominio("uq.edu"));

        System.out.println("\nOrdenados por nombres:");
        directorio.ordenarPorNombre();
        directorio.mostrarContacto();

        System.out.println("\nOrdenados por telefono:");
        directorio.ordenarPorTelefono();
        directorio.mostrarContacto();


    }
}
