# Checklist trazable a Proyecto Final ETD - 2026-2
Marcar un requisito únicamente si está implementado, probado y conectado a la GUI.

## Modelo y reservas
- [ ] Perfil huésped: nombre, documento, nacimiento, teléfono, correo, foto opcional.
- [ ] Titular, ocupantes y nacimiento; menores de 6 cupo gratis.
- [ ] Apartamentos: código único, nombre, bloque, piso, dormitorios, capacidad,
      características, temporadas, tarifa, estado y motivo fuera de servicio.
- [ ] Reserva portal, directa, externa y estados válidos.
- [ ] Portal Pendiente hasta 30 % y vencimiento automático a 24h.
- [ ] Disponibilidad sin solapamientos de noches en cualquier canal.
- [ ] Cotización noche por noche (estancia Iterable y cálculo recursivo).
- [ ] Pago, cargos, saldo, reversión con movimiento contrario.
- [ ] Cancelación con vista previa de devolución y no-show.
- [ ] Grupos grandes por backtracking (mínimo número de apartamentos).
- [ ] Favoritos sin repetidos e historial ordenado.
- [ ] Orden por precio, capacidad, dormitorios y comparador por número de reservas.

## Preparación y grafo
- [ ] Check-in solo si Preparado, check-out alimenta cola.
- [ ] Estados operativos, transiciones y motivo fuera de servicio.
- [ ] Cola prioritaria de preparación por entrada hoy/hora.
- [ ] Cola prioritaria de vencimientos por hora.
- [ ] Cola externa FIFO y conflictos entre canales registrados.
- [ ] Grafo de recepción, escaleras, aseo y apartamentos con pasillos ponderados.
- [ ] Dijkstra ruta mínima, BFS/DFS, conectividad y cierres de pasillo.
- [ ] Turnos de recepcionistas en lista circular.

## Estructuras y conceptos de Java
- [ ] Grafo genérico, Iterable e Iterator propios.
- [ ] Cola de prioridad genérica, Iterable e Iterator propios.
- [ ] Cola genérica, Iterable e Iterator propios.
- [ ] Pila genérica, Iterable e Iterator propios.
- [ ] Lista doblemente enlazada genérica, Iterable e Iterator propios.
- [ ] Lista circular genérica, Iterable e Iterator propios.
- [ ] Árbol BST genérico con inserción, búsqueda, eliminación, altura,
      recorridos recursivos e Iterable/Iterator propio.
- [ ] Conjunto genérico, sin duplicados, Iterable e Iterator propios.
- [x] Estancia Iterable/Iterator de noches (primera versión implementada).
- [ ] Comparable en Apartamento/Huesped/Reserva.
- [ ] Al menos cuatro Comparator requeridos, prioridad con thenComparing.
- [ ] Recursión en DFS, solapamientos, liquidación y backtracking.
- [ ] Excepciones propias: solapamiento, capacidad, fecha, transición y estructura vacía.
- [ ] Sin colecciones java.util prohibidas en núcleo.

## Interfaz y entrega
- [ ] Tablero estados con colores.
- [ ] Panel reservas, confirmación/cancelación/cola externa.
- [ ] Calendario mensual por apartamento y estado de noche.
- [ ] Panel huésped con folio, favoritos e historial.
- [ ] Panel preparación con prioridad y ruta.
- [ ] Mapa gráfico interactivo, estado actualizado y Dijkstra resaltado.
- [ ] Estadísticas de ocupación, ingresos, canales, conflictos y tiempos.
- [ ] Carga desde archivo o botón.
- [ ] Diagrama de clases final actualizado.
- [ ] Diagrama de estructuras propias final actualizado.
- [ ] GitHub: 10+ commits convencionales por Juan y Cristian.
- [ ] Demostración funcional y los dos preparados para sustentar.
