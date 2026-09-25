package co.edu.uniquindio.poo.collection.ejercicioCuatro;

public class Tarea implements Comparable<Tarea> {

    private String nombre;
    private int prioridad;
    public Tarea(String nombre, int prioridad) {
        this.nombre = nombre;
        this.prioridad = prioridad;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(int prioridad) {
        this.prioridad = prioridad;
    }

    @Override
    public int compareTo(Tarea otra) {
       // Menor numero significa mayor prioirdad
        return this.prioridad - otra.getPrioridad();
    }

    @Override
    public String toString() {
        return "Tarea{" +
                "nombre='" + nombre + '\'' +
                ", prioridad=" + prioridad +
                '}';
    }
}
