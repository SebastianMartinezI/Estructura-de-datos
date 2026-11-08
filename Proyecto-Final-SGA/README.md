# SGA · Sistema de Gestión de Alojamiento

Proyecto final de Estructuras de Datos (2026-2). Caso: **Aparta-Suites UQ**, Salento, Quindío.

**Equipo (2 integrantes):** Juan (reservas, huéspedes y canal externo) y Cristian (operación, grafo, folios y reportes).

> **Estado:** versión inicial, NO es el proyecto terminado. Incluye una interfaz Swing
> ejecutable y una demostración real de `Iterable<LocalDate>` para noches de estancia.
> Las demás pantallas están marcadas explícitamente como pendientes.

## Requisitos
- JDK 17 o superior para compilar (se fija Java 17 como nivel del código).
- Apache Maven 3.8+ para compilar y ejecutar tests JUnit.
- Git para colaborar en GitHub.

## Abrir en el IDE (recomendado)
1. Descomprimir o clonar el repositorio.
2. Abrir la carpeta completa como proyecto Maven en IntelliJ IDEA, NetBeans o Eclipse.
3. Esperar a que Maven cargue las dependencias de pruebas.
4. Ejecutar `co.edu.uniquindio.sga.Main`.

## Por terminal
```bash
mvn clean verify
mvn package
java -jar target/sga-alojamiento-0.1.0-SNAPSHOT.jar
```

Alternativa sin Maven para **solo la aplicación inicial**, con JDK y terminal tipo Bash:
```bash
mkdir -p out
javac --release 17 -encoding UTF-8 -d out       src/main/java/co/edu/uniquindio/sga/Main.java       src/main/java/co/edu/uniquindio/sga/excepciones/FechaInvalidaException.java       src/main/java/co/edu/uniquindio/sga/dominio/modelo/Estancia.java       src/main/java/co/edu/uniquindio/sga/interfaz/MainFrame.java       src/main/java/co/edu/uniquindio/sga/interfaz/PanelEstancia.java
java -cp out co.edu.uniquindio.sga.Main
```
Este comando no ejecuta pruebas JUnit (esas necesitan Maven y sus dependencias).

## Reglas de arquitectura
- El **núcleo** (dominio, servicios, estructuras) NO utiliza `ArrayList`,
  `LinkedList`, `Stack`, `HashSet`, `PriorityQueue`, `TreeMap` u otras
  colecciones de `java.util` como implementación del algoritmo.
- Se permite usar contratos estándar como `Iterable`, `Iterator`, `Comparator` y `Comparable`.
- Las clases de dominio no deben importar `javax.swing`.
- Una interfaz gráfica nunca debe decidir la validez de una reserva por su cuenta.
- Todas las estructuras propias deben ser genéricas y tener pruebas dedicadas.
- No modificar archivos ajenos sin acordarlo y revisar en Pull Request.

## Documentación
- [Reparto y entregables](docs/01-reparto-y-etapas.md)
- [Arquitectura y paquetes](docs/02-arquitectura-y-contratos.md)
- [Reglas críticas y decisiones pendientes](docs/03-reglas-y-decisiones.md)
- [Flujo Git/GitHub y commits](CONTRIBUTING.md)
- [Lista de verificación del enunciado](docs/04-checklist-requisitos.md)
- [Diagrama preliminar de estructuras](docs/diagramas/estructuras.mmd)
- [Diagrama preliminar de módulos](docs/diagramas/modulos.mmd)
