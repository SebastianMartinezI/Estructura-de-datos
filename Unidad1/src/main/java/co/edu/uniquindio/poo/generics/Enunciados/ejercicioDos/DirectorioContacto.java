package co.edu.uniquindio.poo.generics.Enunciados.ejercicioDos;

import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;

public class DirectorioContacto {
    private LinkedList<Contacto> contactos = new LinkedList<>();

    public void agregarContactos(Contacto contacto){
        contactos.add(contacto);
    }

    public LinkedList<Contacto>buscarPorDominio(String dominio){
        LinkedList<Contacto>encontrados = new LinkedList<>();
        Iterator<Contacto> iterator = contactos.iterator();

        while (iterator.hasNext()){
            Contacto contacto = iterator.next();

            if(contacto.getEmail().endsWith("@" + dominio)){
                encontrados.add(contacto);
            }

        }
        return encontrados;
    }
    public void ordenarPorNombre(){
        contactos.sort(null);
    }
    public void ordenarPorTelefono(){
        Comparator<Contacto> porTelefono = (primero, segundo) ->
                primero.getTelefono().compareTo(segundo.getTelefono());
        contactos.sort(porTelefono);
    }
    public void mostrarContacto(){
        for(Contacto contacto : contactos){
            System.out.println(contacto);
        }
    }
}
