# Instrucciones para agentes AI en poo-tfi-sistema-gestion

## Visión general
Este proyecto es una aplicación de escritorio para la gestión de una casa de computación. Permite administrar productos, clientes y ventas, con una arquitectura modular y extensible.

## Estructura principal
- **src/main/java/com/unpsjb/poo/**: Código fuente principal.
  - **controller/**: Controladores de vistas y lógica de interacción IGU (ej: `LoginViewController.java`, `ProductosVistaControlador.java`).
  - **model/**: Entidades de dominio (ej: `Producto.java`, `Cliente.java`, `Usuario.java`).
  - **persistence/**: Acceso a datos y DAOs (ej: `UsuarioDAO.java`, `ProductoDAOImpl.java`).
- **resources/**: Recursos estáticos y de configuración.
  - **view/**: Archivos FXML para interfaces gráficas.
  - **css/**: Estilos visuales.
  - **config.properties**: Configuración de la base de datos.
  - **bd.sql**: Script de inicialización de la base de datos.

## Patrones y convenciones
- **MVC**: Separación clara entre modelo, vista (FXML) y controlador.
- **DAO**: Acceso a datos mediante interfaces y clases `impl`.
- **FXML + Controlador**: Cada vista FXML tiene un controlador Java asociado.
- **Recursos**: Imágenes y estilos se ubican en subcarpetas de `resources/view/` y `resources/view/css/`.

## Flujo de datos
- Los controladores gestionan la interacción entre la IGU y los modelos.
- Los DAOs se encargan de la persistencia y recuperación de datos desde la base de datos.
- La configuración de la conexión se define en `config.properties` y se inicializa con `GestorDeConexion.java`.

## Workflows de desarrollo
- **Compilación y ejecución**: Usar Maven (`pom.xml`). Comando típico:
  ```powershell
  mvn clean install ; mvn exec:java -Dexec.mainClass="com.unpsjb.poo.Main"
  ```
- **Base de datos**: Inicializar con el script `bd.sql`.
- **Vistas**: Modificar FXML y su controlador correspondiente.

## Integraciones y dependencias
- **Maven**: Gestión de dependencias y ciclo de vida del proyecto.
- **JavaFX**: Para la interfaz gráfica (FXML, controladores).
- **JDBC**: Acceso a base de datos relacional.

## Ejemplo de patrón típico
- Para agregar una nueva entidad:
  1. Crear clase en `model/`.
  2. Crear DAO e implementación en `persistence/dao/` y `persistence/dao/impl/`.
  3. Crear vista FXML y controlador en `controller/`.

## Recomendaciones
- Mantener la coherencia en la nomenclatura de archivos y clases.
- Seguir el patrón MVC y la estructura modular.
- Consultar `README.md` para entender el alcance y funcionalidades principales.

---
¿Hay alguna sección que requiera mayor detalle o aclaración? Indica qué aspectos específicos del proyecto te gustaría documentar mejor.