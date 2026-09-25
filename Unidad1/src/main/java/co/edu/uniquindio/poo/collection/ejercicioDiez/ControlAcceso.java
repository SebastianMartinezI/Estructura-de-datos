package co.edu.uniquindio.poo.collection.ejercicioDiez;

import java.util.HashSet;

public class ControlAcceso {

    private HashSet<String> empleados;

    public ControlAcceso() {
        empleados = new HashSet<>();
    }

    // Registrar empleado

    public void registrarEmpleado(String codigo) {
        if(empleados.add(codigo)) {
            System.out.println("Empleado registrado correctamente: " + codigo);
        } else {
            System.out.println("El código ya existe: " + codigo);
        }
    }

    // Verificar acceso

    public void verificarAcceso(String codigo) {
        if(empleados.contains(codigo)) {
            System.out.println("Acceso permitido para el empleado: " + codigo);
        } else {
            System.out.println("Acceso rechazado. Código no registrado: " + codigo);
        }
    }

    // Eliminar empleado

    public void eliminarEmpleado(String codigo) {
        if(empleados.remove(codigo)) {
            System.out.println("Empleado eliminado: " + codigo);
        } else {
            System.out.println("No existe el código indicado");
        }
    }

    // Mostrar empleados registrados

    public void mostrarEmpleados() {
        System.out.println("Empleados registrados:");
        for(String codigo : empleados) {
            System.out.println(codigo);
        }
    }
}