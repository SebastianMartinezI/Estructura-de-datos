package co.edu.uniquindio.poo.servicios;
import co.edu.uniquindio.poo.estructuras.PilaPropia;
import co.edu.uniquindio.poo.modelos.Caja;
import java.util.Objects;

/**
 * Gestiona el retiro de cajas de una pila propia.
 *
 * Reorganiza las cajas restantes segun la paridad
 * del valor de la caja retirada.
 */
public class RetiroCajasService {

    /**
     * Retira una caja y reorganiza las restantes.
     *
     * @param pila pila de cajas.
     * @param codigo codigo de la caja que se desea retirar.
     * @return true si se retira la caja, false en caso contrario.
     */
    public boolean retirarCaja(
            PilaPropia<Caja> pila, String codigo) {

        Objects.requireNonNull(pila, "La pila no puede ser nula");

        if (pila.estaVacia() || codigo == null) {
            return false;
        }

        PilaPropia<Caja> auxiliar = new PilaPropia<>();

        boolean encontrada = false;
        int valorRetirado = 0;

        // Buscar la caja y conservar las restantes.
        while (!pila.estaVacia()) {
            Caja actual = pila.desapilar();

            if (!encontrada && actual.getCodigo().equals(codigo)) {
                encontrada = true;
                valorRetirado = actual.getValor();
            } else {
                auxiliar.apilar(actual);
            }
        }

        // Restaurar el orden de las cajas restantes.
        transferir(auxiliar, pila);

        if (!encontrada) {
            return false;
        }

        PilaPropia<Caja> impares = new PilaPropia<>();
        PilaPropia<Caja> pares = new PilaPropia<>();

        int posicion = 1;

        // Clasificar segun la posicion desde la cima.
        while (!pila.estaVacia()) {
            Caja actual = pila.desapilar();

            if (posicion % 2 != 0) {
                impares.apilar(actual);
            } else {
                pares.apilar(actual);
            }

            posicion++;
        }

        // Reconstruir la pila conservando el orden relativo.
        if (valorRetirado % 2 == 0) {
            transferir(pares, pila);
            transferir(impares, pila);
        } else {
            transferir(impares, pila);
            transferir(pares, pila);
        }

        return true;
    }

    /**
     * Transfiere todos los elementos entre pilas propias.
     */
    private void transferir(
            PilaPropia<Caja> origen,
            PilaPropia<Caja> destino) {

        while (!origen.estaVacia()) {
            destino.apilar(origen.desapilar());
        }
    }
}
