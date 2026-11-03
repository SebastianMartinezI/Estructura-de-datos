# Reglas de negocio y puntos a definir

## Reglas textuales del proyecto
- Se gestiona un alojamiento con apartamentos completos, no dormitorios sueltos.
- Los menores de 6 años ocupan cupo, pero no pagan alojamiento.
- La noche de salida no está ocupada ni se cobra.
- Precio por persona y noche, temporada Baja / Media / Alta, cobro noche a noche.
- Portal: reserva Pendiente hasta anticipo del 30 %.
- Externa: solicitudes en cola FIFO, conflictos registrados.
- Toda reserva: Pendiente → Confirmada → En curso → Finalizada;
  posible Cancelada o No-show antes del final.
- No se permite compartir una noche entre dos reservas activas.
- Check-in requiere apartamento Preparado; check-out deja Pendiente de preparación.
- Prioridad de limpieza: llegada hoy y hora estimada; después resto.
- Pendientes caducan 24 horas después de ser creadas.
- Cargos/pagos no se borran: al deshacer se agrega una transacción opuesta.

## Decisiones del grupo pendientes de aprobación
1. **Menor de 6:** calcular edad en la fecha de cada noche o en el día de
   entrada. El documento no define cuál. Sugerimos por cada noche para
   que sea consistente con cobro noche a noche.
2. **Titular:** precisar si siempre es ocupante, o si puede reservar sin
   alojarse, para evitar contarlo dos veces.
3. **Cancelación/devolución:** el documento pide previsualizar devolución,
   pero NO fija porcentaje o plazo. Definir política configurable y
   documentarla antes de implementar reembolsos.
4. **Reserva directa/externa:** precisar cómo se confirma y si exige
   anticipo de 30 % igual que el portal (solo el portal lo especifica).
5. **No-show:** definir quién lo marca y a partir de qué momento.
6. **Temporadas:** establecer prioridad ante fechas de temporada duplicadas.
7. **Empates:** ordenar preparación por hora, luego código de apartamento.
8. **Ruta:** pasillos ponderados positivos; Dijkstra (BFS no sirve para
   mínimos ponderados generales). Tramos cerrados se omiten del recorrido.
9. **Persistencia:** decidir si la carga es un solo escenario reemplazable
   y qué formato de archivo se versionará.

## Pruebas de aceptación representativas
- 10 al 12 de marzo → 2 noches.
- 10–12 y 12–14 en un mismo apartamento → no se solapan.
- 10–12 y 11–13 → colisión.
- Reserva cancelada no bloquea fechas; pendiente no vencida sí.
- 2 adultos y menor de 5 → capacidad 3, pagadores 2.
- 24h de una reserva portal sin anticipo → pasa a Cancelada.
- Checkout → Pendiente de preparación → En preparación → Preparado.
- Pagar/abonar y deshacer genera contramovimiento, no borra original.
- Solicitudes externas se procesan en orden FIFO.
- Entrada hoy priorizada antes de un apartamento sin llegada hoy.
- Grafo con pasillo cerrado detecta puntos inalcanzables.
