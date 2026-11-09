package co.edu.uniquindio.poo.servicios;

import co.edu.uniquindio.poo.estructuras.ColaPropia;
import co.edu.uniquindio.poo.modelos.Paquete;


public class PaqueteServico {

    public ColaPropia<Paquete> SeleccionarPaquetes(ColaPropia<Paquete> pendiente, int capacidad) {
        ColaPropia<Paquete> seleccionados = new ColaPropia<>();

        if (capacidad <= 0 || pendiente.estaVacio()) {
            return seleccionados;
        }

        int capacidadRestante = seleccionarTipo(pendiente,seleccionados, Paquete.TipoEntrega.URGENTE,capacidad, pendiente.tamano());
        seleccionarTipo(pendiente,seleccionados, Paquete.TipoEntrega.NORMAL,capacidadRestante,pendiente.tamano());
        return seleccionados;
    }

    private int seleccionarTipo(ColaPropia<Paquete> pendiente, ColaPropia<Paquete> seleccionados, Paquete.TipoEntrega tipoBuscado,
                                int capacidadRestante, int porRevisar) {
        if (porRevisar == 0) {
            return capacidadRestante;
        }

        Paquete paquete = pendiente.desencolar();

        if (paquete.getTipoEntrega() == tipoBuscado) {
            if (paquete.getPeso() <= capacidadRestante) {
                seleccionados.encolar(paquete);
                capacidadRestante -= paquete.getPeso();

                System.out.println(paquete.getCodigo() + " seleccionado. Capaidad restante: " + capacidadRestante + "kg");
            } else {
                pendiente.encolar(paquete);
                System.out.println(paquete.getCodigo() + " queda pendiente: no cabe. " + "Capacidad restante: " + capacidadRestante + "kg");
            }
        } else {
            pendiente.encolar(paquete);
        }
        return seleccionarTipo(pendiente, seleccionados, tipoBuscado, capacidadRestante, porRevisar - 1);
    }
}
