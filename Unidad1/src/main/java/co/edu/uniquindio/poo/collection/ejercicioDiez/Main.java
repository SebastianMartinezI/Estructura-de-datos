package co.edu.uniquindio.poo.collection.ejercicioDiez;
public class Main {
    //En un edificio con control de acceso, los empleados deben identificarse mediante
    // un código único para poder ingresar. Para gestionar estos accesos sin permitir
    // duplicados, se utilizará un HashSet, donde cada ID de empleado será almacenado
    // y verificado antes de permitir la entrada.
    public static void main(String[] args) {

        ControlAcceso control = new ControlAcceso();

        System.out.println(
                "Registro de empleados"
        );

        control.registrarEmpleado("Emple001");
        control.registrarEmpleado("Emple002");
        control.registrarEmpleado("Emple003");

        System.out.println("\nIntento de registro duplicados");

        control.registrarEmpleado("Emple002");

        System.out.println("\nVerificacion de acceso");

        control.verificarAcceso("Emple001");

        control.verificarAcceso("Emple005");

        System.out.println("\nLista de empleados");

        control.mostrarEmpleados();

        System.out.println("\nEliminacion de empleados");

        control.eliminarEmpleado("Emple003");

        System.out.println("\nLista actualizada");

        control.mostrarEmpleados();

    }
}