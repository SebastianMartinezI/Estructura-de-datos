# Trabajo colaborativo sin conflictos

## Una sola copia del proyecto
Juan crea un repositorio **privado** `sga-alojamiento-uq`, agrega a Cristian como colaborador y sube la estructura inicial a `main`.
Solo se integra código a `develop` vía Pull Request; `main` conserva las
versiones funcionales y demostrables.

## Ramas
- `main`: estable, solo merges validados desde `develop`.
- `develop`: integración diaria, con CI verde.
- `feat/juan-arbol-bst`, `feat/cristian-grafo`, `feat/juan-cola-fifo`, `feat/cristian-folio`:
  ramas cortas, una funcionalidad por rama.
- `fix/...`, `test/...`, `docs/...` según el tipo.

## Comandos de ejemplo
```bash
git clone URL_DEL_REPOSITORIO
cd sga-alojamiento-uq
git switch develop
git pull --ff-only origin develop
git switch -c feat/juan-arbol-bst
# Programar y probar...
mvn clean verify
git status
git add src/main/java/co/edu/uniquindio/sga/estructuras/arbol/
git commit -m "feat(estructuras): implementar insercion BST"
git push -u origin feat/juan-arbol-bst
```
Crear el Pull Request hacia `develop`, pedir revisión y fusionarlo cuando
las pruebas pasen. Evitar `git push --force` y commits directos en `main`.

## Conventional Commits
- `feat(reservas): validar solapamiento por noches`
- `feat(grafo): calcular ruta minima con dijkstra`
- `fix(folio): evitar reversar dos veces un pago`
- `test(cola): cubrir retiro de estructura vacia`
- `docs(uml): actualizar diagrama de estructuras`
- `refactor(servicios): separar cotizacion de reservas`
- `build(maven): configurar compilacion java 17`

**Mínimo 10 commits reales por integrante.** Preferible 12–15 commits
pequeños y verificables, no commits artificiales vacíos. Cada persona usa
su propia cuenta y correo de Git.

## Acuerdos anticolisiones
1. Cada integrante edita sus carpetas propietarias.
2. `MainFrame.java`, `pom.xml`, enums comunes y contratos compartidos se
   modifican en PR separados después de acordar las interfaces.
3. Antes de abrir PR, actualizar rama con `develop`, ejecutar pruebas.
4. Una sola persona integra por vez. Los demás revisan el PR.
5. Resolver conflictos manualmente, no aceptar automáticamente 'ours/theirs'.
6. Nunca borrar movimientos económicos ni reescribir el historial de Git.
7. Registrar cada cambio al contrato de un servicio en `docs/02-arquitectura-y-contratos.md`.
