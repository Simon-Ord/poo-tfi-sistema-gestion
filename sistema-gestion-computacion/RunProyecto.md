# Proyecto Sistema de Gestión - Casa de Computación

## Ejecutar el Proyecto

Para correr el proyecto desde Visual Studio Code:
1. Abrir una terminal nueva (... -> terminal -> Nuevo Terminal)
2. `cd prueba-casi-completa-vista`
3. `mvn clean javafx:run`

## Sistema de Ventanas Internas

Este proyecto utiliza un sistema de ventanas internas que permite abrir múltiples vistas dentro del escritorio principal.

### Documentación Disponible

- **[GUIA_RAPIDA.md](GUIA_RAPIDA.md)** - Guía rápida para crear ventanas (3 minutos)
- **[WINDOW_SYSTEM_GUIDE.md](WINDOW_SYSTEM_GUIDE.md)** - Guía completa del sistema de ventanas
- **[RESUMEN_CAMBIOS.md](RESUMEN_CAMBIOS.md)** - Resumen de cambios implementados y arquitectura

### Inicio Rápido

Para crear una ventana interna en tu controlador:

```java
public class MiControlador extends BaseControlador {
    
    @FXML
    private void abrirVentana() {
        // Para ventanas de gestión
        crearVentana("/view/miVista.fxml", "Título");
        
        // Para formularios
        crearFormulario("/view/formulario.fxml", "Título");
        
        // Para diálogos
        crearDialogo("/view/dialogo.fxml", "Título");
    }
}
```

Ver **GUIA_RAPIDA.md** para más ejemplos.

## Requisitos

- Java 17
- Maven 3.6+
- JavaFX 21.0.4
- PostgreSQL (para persistencia)

