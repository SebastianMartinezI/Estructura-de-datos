# Plan de trabajo del SGA — Equipo de dos integrantes

**Integrantes:** Juan y Cristian. Las asignaciones identifican al responsable de la implementación; ambos deben revisar, probar y poder sustentar el proyecto completo. El alcance se mantiene igual al enunciado del profesor.

## Juan: reservas, disponibilidad, perfiles y canales

**Carpetas propias:** `estructuras/arbol/`, `estructuras/lista/`, `estructuras/cola/`, `estructuras/conjunto/`, `servicios/reservas/`, `servicios/administracion/huespedes/`, `interfaz/reservas/`, `interfaz/calendario/`, `interfaz/huespedes/`.

**Cuatro estructuras propias:**
1. `ArbolBinarioBusqueda<T extends Comparable<T>>`: insertar, buscar, eliminar, altura y recorridos recursivos, `Iterable/Iterator`; índices por código de apartamento, documento de huésped y fechas de reservas.
2. `ListaDoblementeEnlazada<T>`: historial, ocupantes, desglose por noches e iterador.
3. `Cola<T>`: canal externo FIFO; registrar llegadas, procesar de una en una y contabilizar conflictos de canal.
4. `Conjunto<T>`: favoritos sin repetidos, iterador propio.

**Reglas y casos de uso:**
- Entidades de reserva/huésped/apartamento, identificadores y capacidad, coordinadas con Cristian antes de su primera implementación.
- Disponibilidad para estancias `[entrada, salida)` y número de personas; comparación por precio, capacidad y dormitorios; también por número de reservas.
- Solapamientos de reservas activas mediante índice de árbol por apartamento y recorrido recursivo; no inspeccionar todas las reservas globales.
- Tarifas por temporada y cotización recursiva por noche; ocupantes menores de 6 años no pagan pero ocupan cupo.
- Reservas por portal, directa y canal externo; estados, anticipo del 30 %, cancelación con vista previa de devolución y no-show. Acordar política de devolución, no especificada en el enunciado, antes de programarla.
- Asignación de grupos grandes por backtracking minimizando el número de apartamentos.
- Búsqueda y registro de huéspedes, favoritos e historial.
- GUI: panel de reservas, búsqueda y filtros, calendario mensual de ocupación, panel de huésped.
- Pruebas unitarias: árbol, cola, conjunto, lista, solapamientos, precios, estados, backtracking y canales.

## Cristian: operación, rutas, folios y reportes

**Carpetas propias:** `estructuras/grafo/`, `estructuras/cola_prioridad/`, `estructuras/circular/`, `estructuras/pila/`, `servicios/operacion/`, `servicios/folio/`, `servicios/administracion/reportes/`, `persistencia/`, `interfaz/preparacion/`, `interfaz/mapa/`, `interfaz/reportes/`.

**Cuatro estructuras propias:**
1. `Grafo<T>` ponderado: nodos, pasillos, cierres, Dijkstra y BFS/DFS, recursividad en DFS e iterador.
2. `ColaPrioridad<T>`: orden de limpieza (entradas de hoy primero; hora estimada después) y reservas pendientes ordenadas por vencimiento; `Comparator` y `thenComparing` donde corresponde.
3. `ListaCircular<T>`: rotación de turnos de recepción, con iterador.
4. `Pila<T>`: deshacer la operación de folio mediante movimiento contrario, sin borrar el historial; iterador.

**Reglas y casos de uso:**
- Estados operativos de apartamentos, con motivo obligatorio para Fuera de servicio.
- Check-in solo si Preparado; check-out cambia a Pendiente de preparación y encola limpieza.
- Prioridad de preparación, registro de inicio y fin, medición de tiempos.
- Vencimiento a las 24 horas usando un reloj inyectable y llamando al contrato de cancelación de reservas de Juan (nunca modificar reservas directamente desde la cola).
- Grafo del edificio; ruta más corta, visualización interactiva de nodos/pasillos y recorrido resaltado; alcance tras cierre de pasillo, agrupación por bloque.
- Folios inmutables: cargos, pagos, saldos y contramovimientos; integración con las reservas de Juan.
- Reportes de ocupación, ingresos, canales, conflictos y tiempos; datos desde estructuras propias.
- Carga de escenario inicial mediante archivo plano/serializado o botón.
- GUI: tablero inicial, panel preparación, mapa, folios y reportes.
- Pruebas: grafo, rutas, cierres, cola de prioridad, pila, cola de vencimiento, pagos, preparación y estadísticas.

## Asuntos compartidos (no tienen propietario unilateral)
- `MainFrame.java`, `Main.java`, `pom.xml`, enums de integración, modelos transversales y firmas de servicios: aprobar contratos antes de cambiarlos. Un PR exclusivo de integración cuando cambien.
- `docs/`: ambos redactan y mantienen diagramas de clases y estructuras, README y guion de demostración.
- **Mínimo 10 commits reales por persona**, cada uno desde su cuenta, con Conventional Commits.
- **Ambos sustentan**: la persona elegida por el profesor puede tener que defender cualquier módulo.

## Contratos que deben acordarse ANTES de escribir servicios dependientes
1. Juan define `Reserva`, `Apartamento`, `Huesped`, `Estancia`, `Ocupante`, temporadas y API de disponibilidad; Cristian revisa estados operativos que necesita. Ambas partes aprueban tipos, ID, constructores y comparadores.
2. Cristian propone contrato `ServicioFolio` para que reservas registre cargos y anticipos sin alterar movimientos desde otra clase.
3. Juan propone contrato `ServicioReservas` para que vencimientos y check-in cambien estados sin modificar sus árboles externamente.
4. Compartir `Clock` (reloj inyectable), `BigDecimal` para importes y `LocalDate` para noches; nunca usar `double` para dinero.
5. Los servicios no devuelven colecciones `java.util` como almacenamiento de dominio; estructuras propias y DTOs claros. Las estructuras no dependen de las entidades del negocio.

## Etapas y entregables

| Etapa | Juan | Cristian | Integración / condición para avanzar |
|---|---|---|---|
| 0. Base y Git | Crear repositorio, subir base | Clonar y ejecutar | `main` y `develop`, CI revisada |
| 1. Modelos y contratos | Modelos de reserva y hospedaje, interfaces de disponibilidad | Contratos folio/operación y estados | Nombres, estados y firmas aprobados |
| 2. Estructuras | BST, lista doble, cola FIFO, conjunto | Grafo, prioridad, circular, pila | Pruebas JUnit de todas, sin GUI |
| 3. Reglas de negocio | Disponibilidad, cotización, reservas, canales, backtracking, huéspedes | Check-in/out, limpieza, caducidad, folios, reportes, carga de datos | Integración de servicios sin Swing |
| 4. Interfaz | Reservas, calendario y huésped | Dashboard, preparación, mapa, folios y reportes | Ventanas conectadas a casos de uso reales |
| 5. Cierre | Casos extremos y pruebas cruzadas | Datos demostración y pruebas cruzadas | Diagrama de clases, estructuras, demo completa |

## Primeras tareas individuales
- **Juan:** validar que la base corre; en `feat/juan-arbol-bst`, implementar BST genérico con inserción/búsqueda recursivas y pruebas; luego eliminación, altura, recorridos e iterador.
- **Cristian:** validar la base; en `feat/cristian-grafo`, implementar grafo genérico ponderado con API acordada y pruebas; después Dijkstra y BFS/DFS.
- **Ambos:** aprobar los modelos y contratos antes de integrar cambios que dependan uno del otro. Nadie modifica `MainFrame.java` unilateralmente.

## Regla de calidad
Cada PR debe traer código compilable, pruebas nuevas y descripción clara. No integrar a `develop` si `mvn clean verify` falla. No hacer commits vacíos para alcanzar los 10 pedidos por persona.
