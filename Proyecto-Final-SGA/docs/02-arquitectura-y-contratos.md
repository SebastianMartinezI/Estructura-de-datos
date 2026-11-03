# Arquitectura y contratos comunes

## Capas
1. `interfaz/`: Swing y Java2D. Solo valida el formato de entrada y muestra resultados.
2. `servicios/`: casos de uso, reglas del negocio y orquestación.
3. `dominio/modelo/`: entidades, valores, estados y sus invariantes.
4. `estructuras/`: estructuras genéricas, sin clases del negocio ni Swing.
5. `persistencia/`: lectura y guardado del escenario de prueba.

Las dependencias apuntan hacia las reglas de negocio; las estructuras
**nunca** importan `Apartamento`, `Huesped` ni `Reserva`.

## Contrato de integración a acordar antes de codificar
- Código de apartamento `String` único, documento de huésped `String` único.
- ID de reserva propio y único (no usar solo fechas como identidad).
- Estancia `[entrada, salida)`, usando `LocalDate`.
- Marcas de tiempo usando `LocalDateTime` o `Instant` y `Clock` inyectable.
- Dinero como `BigDecimal`, sin `double`.
- Capacidad = total real de personas incluyendo menores que no pagan.
- Las tarifas se calculan noche a noche según temporada y apartamento.
- Consultas de servicios devuelven estructuras propias o tipos de dominio;
  no `List`, `Map` ni `Set` de java.util en el núcleo.
- Toda operación que cambia una reserva debe verificar estados y permisos.
- Un índice por apartamento permite verificar conflictos antes de confirmar.
- Las excepciones propias distinguen fecha inválida, capacidad, conflicto,
  estado inválido y estructura vacía.

## Interfaces de servicios sugeridas (borrador, NO código definitivo)
```java
interface ServicioReservas {
    Cotizacion cotizar(String codigoApartamento, Estancia estancia, Ocupantes ocupantes);
    Reserva reservar(SolicitudReserva solicitud);
    boolean estaDisponible(String codigoApartamento, Estancia estancia);
}
interface ServicioPreparacion {
    void registrarCheckOut(String idReserva);
    Apartamento siguientePorPreparar();
    void finalizarPreparacion(String codigoApartamento);
}
interface ServicioFolio {
    void registrarPago(String idReserva, Movimiento movimiento);
    void deshacerUltimaOperacion(String idReserva);
}
```
Crear estos tipos cuando acordemos nombres y modelos, **no copiarlos todavía como archivos vacíos**.

## Ordenamientos obligatorios
- Natural `Apartamento`: por código.
- Natural `Huesped`: por documento.
- Natural `Reserva`: por fecha de entrada; desempate por ID para evitar pérdida de datos.
- Comparator 1: apartamento por precio de cotización en el periodo solicitado.
- Comparator 2: apartamento por capacidad.
- Comparator 3: apartamento por número de reservas.
- Comparator 4: cola de preparación, entrada hoy primero, después hora de llegada.

## Métodos de estructuras que deben acordarse
- `ArbolBinarioBusqueda<T extends Comparable<T>>`: insertar, buscar,
  eliminar, contiene, altura, inOrden, iterable y búsqueda de conflictos
  por subárboles en el caso concreto de reservas.
- `ListaDoblementeEnlazada<T>`: agregar, quitar, tamaño, iterador.
- `Grafo<T>`: insertar vértice/arista, BFS, DFS, Dijkstra, alcance.
- `ColaPrioridad<T>`: encolar, desencolar, primero, vacía, iterable,
  con `Comparator<? super T>` configurado en constructor.
- `Cola<T>` y `Pila<T>`: ofrecer/retirar y apilar/desapilar, iteradores.
- `Conjunto<T>`: agregar, quitar, contiene, tamaño, iterable.
- `ListaCircular<T>`: agregar, avanzar turno, iterable.

**Evitar falsos desacoplamientos:** no es SOLID crear interfaces por cada
clase sin una necesidad real. Los servicios dependen de contratos donde
convenga sustituir implementaciones y probarlas.
