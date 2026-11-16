# SGA — Sistema de Gestión de Alojamiento

Proyecto final de **Estructuras de Datos (2026-2)** para el caso **Aparta-Suites UQ**, Salento, Quindío.

**Equipo (2 integrantes):**
- **Juan:** reservas, huéspedes, disponibilidad, cotización, calendario y canal externo.
- **Cristian:** operación, preparación, mapa del edificio, folios, turnos, estadísticas y carga de datos.

> **Estado actual: prototipo en desarrollo, no sistema terminado.** La aplicación ya inicia con JavaFX y carga una pantalla principal definida en FXML, con estilos CSS y navegación básica entre secciones. Las opciones del menú son demostrativas: las funciones de negocio de cada módulo aún deben implementarse y conectarse a sus servicios. Se conservan temporalmente clases de la antigua interfaz Swing para evitar eliminaciones prematuras.

## Requisitos

- JDK 17 o superior; el proyecto compila con nivel de lenguaje Java 17.
- Apache Maven 3.8+.
- Git y acceso al repositorio compartido de GitHub.
- Las dependencias JavaFX se administran mediante Maven; no se necesita descargar manualmente el SDK de JavaFX para la ejecución Maven configurada.


   ```bash
   mvn clean verify
   mvn javafx:run
   ```

`mvn clean verify` compila y ejecuta las pruebas configuradas. `mvn javafx:run` inicia la ventana de JavaFX. Antes de integrar cambios, comprobar que Maven termine con **BUILD SUCCESS** y que la interfaz abra correctamente.

> **Distribución:** el JAR básico generado por `mvn package` no incluye automáticamente todas las dependencias y el entorno de ejecución necesarios para JavaFX; para esta etapa utilizar `mvn javafx:run`.

## Tecnologías

- **Java 17** y **Maven**.
- **JavaFX**, **FXML** y **CSS** para la interfaz gráfica.
- **JUnit 5** para pruebas automatizadas.
- **Estructuras propias genéricas** para la lógica del proyecto, conforme al enunciado.

## Organización por capas

- `src/main/java/co/edu/uniquindio/sga/dominio/`: entidades, valores y validaciones del modelo.
- `src/main/java/co/edu/uniquindio/sga/estructuras/`: estructuras de datos genéricas desarrolladas por el equipo.
- `src/main/java/co/edu/uniquindio/sga/servicios/`: casos de uso y reglas de negocio.
- `src/main/java/co/edu/uniquindio/sga/interfaz/`: arranque JavaFX y controladores.
- `src/main/java/co/edu/uniquindio/sga/persistencia/`: carga y guardado de datos.
- `src/main/resources/co/edu/uniquindio/sga/vistas/`: archivos FXML.
- `src/main/resources/co/edu/uniquindio/sga/css/`: hojas de estilo CSS.
- `src/test/`: pruebas unitarias.

### Interfaz JavaFX inicial

- `co.edu.uniquindio.sga.Main`: punto de entrada.
- `co.edu.uniquindio.sga.interfaz.SgaApplication`: carga `principal.fxml` y `estilos.css`.
- `co.edu.uniquindio.sga.interfaz.controladores.PrincipalController`: navegación demostrativa del menú; **no implementa procesos del negocio**.

## Estructuras y responsabilidades

| Responsable | Estructuras propias | Funcionalidades previstas |
| --- | --- | --- |
| Juan | BST, lista doblemente enlazada, cola FIFO, conjunto | Búsqueda y registro de huéspedes/apartamentos/reservas, disponibilidad, historial, ocupantes, canal externo, favoritos, liquidación por noche y backtracking |
| Cristian | Grafo ponderado, cola de prioridad, lista circular, pila | Rutas, preparación, vencimientos, turnos, folios, deshacer y estadísticas |

La implementación inicial del **BST** y la clase **Estancia** forman parte de la base del proyecto; su integración con los casos de uso continuará por etapas.

## Principios y restricciones

1. Las estructuras propias deben ser **genéricas** y no depender del dominio ni de JavaFX.
2. No utilizar `ArrayList`, `LinkedList`, `HashSet`, `PriorityQueue`, `TreeMap` o `Stack` de `java.util` para sustituir las estructuras exigidas en el núcleo del sistema.
3. Se permiten contratos estándar como `Comparable`, `Comparator`, `Iterable` e `Iterator`.
4. Los controladores JavaFX muestran información, recogen acciones y delegan en servicios; **no calculan disponibilidad, tarifas ni rutas por su cuenta**.
5. Mantener encapsulamiento, responsabilidades claras, documentación Javadoc y pruebas para los algoritmos principales, aplicando SOLID sin sobreingeniería.
6. No editar contratos compartidos ni archivos asignados a otro integrante sin coordinación previa.
7. Documentar las reglas que el enunciado no define completamente antes de implementarlas.

## Flujo Git/GitHub

- `main`: versión estable.
- `develop`: rama de integración validada.
- `feat/...`: una funcionalidad por rama de trabajo; los cambios se integran por **Pull Request hacia `develop`**.
- Cada integrante debe registrar **al menos 10 commits reales** con mensajes en formato **Conventional Commits**.
- Antes de integrar: actualizar desde `develop`, revisar cambios, ejecutar `mvn clean verify` y realizar una revisión del Pull Request.

## Documentación del proyecto

- [Reparto y entregables](docs/01-reparto-y-etapas.md)
- [Arquitectura y contratos](docs/02-arquitectura-y-contratos.md)
- [Reglas y decisiones pendientes](docs/03-reglas-y-decisiones.md)
- [Flujo Git y contribuciones](CONTRIBUTING.md)
- [Checklist de requisitos](docs/04-checklist-requisitos.md)
- [Diagrama de estructuras](docs/diagramas/estructuras.mmd)
- [Diagrama de módulos](docs/diagramas/modulos.mmd)
