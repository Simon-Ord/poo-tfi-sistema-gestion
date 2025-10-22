# Guía de Uso: Ventanas Internas

## Método `crearVentana()` de VentanaVistaControlador

Esta guía explica cómo usar el método simplificado para crear ventanas internas dentro del escritorio.

## Descripción

El método `VentanaVistaControlador.crearVentana()` es un método estático que facilita la apertura de vistas FXML como ventanas internas en el escritorio del sistema. Elimina la necesidad de escribir código repetitivo para cargar FXML y crear ventanas.

## Sintaxis

### Versión completa (con dimensiones personalizadas)

```java
VentanaVistaControlador.ResultadoVentana resultado = 
    VentanaVistaControlador.crearVentana(
        desktop,                      // Pane contenedor (campo de PrincipalVistaControlador)
        "/view/miVista.fxml",        // Ruta del archivo FXML
        "Mi Título",                  // Título de la ventana
        800,                          // Ancho en píxeles
        600                           // Alto en píxeles
    );
```

### Versión simple (dimensiones por defecto: 640x420)

```java
VentanaVistaControlador.crearVentana(
    desktop, 
    "/view/miVista.fxml", 
    "Mi Título"
);
```

## Ejemplos de Uso

### Ejemplo 1: Abrir una ventana simple

```java
@FXML
private void abrirProductos(ActionEvent event) {
    VentanaVistaControlador.crearVentana(
        desktop, 
        "/view/productosVista.fxml", 
        "Productos"
    );
}
```

### Ejemplo 2: Abrir una ventana con dimensiones específicas

```java
@FXML
private void abrirClientes(ActionEvent event) {
    VentanaVistaControlador.crearVentana(
        desktop, 
        "/view/ClientesView.fxml", 
        "Gestión de Clientes", 
        900, 
        650
    );
}
```

### Ejemplo 3: Obtener el controlador de la vista cargada

```java
@FXML
private void abrirReportes(ActionEvent event) {
    // Capturar el resultado para acceder al controlador
    VentanaVistaControlador.ResultadoVentana resultado = 
        VentanaVistaControlador.crearVentana(
            desktop, 
            "/view/reportesView.fxml", 
            "Reportes del Sistema", 
            900, 
            600
        );
    
    // Acceder al controlador si es necesario
    ReportesControlador controller = resultado.getControlador(ReportesControlador.class);
    
    // Ahora puedes llamar métodos del controlador si lo necesitas
    // controller.cargarDatos();
}
```

### Ejemplo 4: Manejo de errores

```java
@FXML
private void abrirVista(ActionEvent event) {
    try {
        VentanaVistaControlador.crearVentana(
            desktop, 
            "/view/miVista.fxml", 
            "Mi Vista"
        );
    } catch (Exception e) {
        e.printStackTrace();
        new Alert(Alert.AlertType.ERROR, 
            "Error al abrir la vista: " + e.getMessage()).showAndWait();
    }
}
```

## Ventajas del nuevo método

1. **Simplicidad**: Reduce múltiples líneas de código a una sola llamada
2. **Menos errores**: Maneja automáticamente la carga de FXML y la creación de ventanas
3. **Consistencia**: Todas las ventanas se posicionan en cascada automáticamente
4. **Flexibilidad**: Permite acceder al controlador si se necesita
5. **Documentación**: Código autodocumentado con JavaDoc

## Comparación: Antes vs Ahora

### Antes (código antiguo)

```java
@FXML
private void productosAction(ActionEvent event) {
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/productosVista.fxml"));
        Node view = loader.load();
        
        VentanaVistaControlador win = new VentanaVistaControlador("Productos", view);
        win.setPrefSize(1000, 500);
        
        int count = desktop.getChildren().size();
        win.relocate(30 + 24 * count, 30 + 18 * count);
        
        desktop.getChildren().add(win);
        win.toFront();
    } catch (Exception e) {
        e.printStackTrace();
        new Alert(Alert.AlertType.ERROR, "Error: " + e.getMessage()).showAndWait();
    }
}
```

### Ahora (código simplificado)

```java
@FXML
private void productosAction(ActionEvent event) {
    VentanaVistaControlador.crearVentana(
        desktop, 
        "/view/productosVista.fxml", 
        "Productos", 
        1000, 
        500
    );
}
```

## Notas Importantes

- El parámetro `desktop` debe ser el `Pane` contenedor de las ventanas (típicamente definido en `PrincipalVistaControlador`)
- Las rutas FXML deben empezar con `/` para indicar que son rutas del classpath
- Las ventanas se posicionan automáticamente en cascada
- El método lanza excepciones si el FXML no se encuentra o tiene errores
- La clase `ResultadoVentana` incluye tanto la ventana como el controlador para mayor flexibilidad
