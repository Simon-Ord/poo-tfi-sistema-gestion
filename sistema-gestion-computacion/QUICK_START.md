# ⚡ Quick Start: Método crearVentana()

## 🚀 Inicio Rápido en 3 Pasos

### 1️⃣ Importa la clase (si no está importada)
```java
import com.unpsjb.poo.controller.VentanaVistaControlador;
```

### 2️⃣ Asegúrate de tener acceso al desktop
```java
@FXML private Pane desktop;  // En PrincipalVistaControlador ya existe
```

### 3️⃣ ¡Usa el método!
```java
VentanaVistaControlador.crearVentana(desktop, "/view/tuVista.fxml", "Tu Título");
```

---

## 📝 Casos de Uso Más Comunes

### ✨ Caso 1: Ventana simple (sin necesidad del controlador)
```java
@FXML
private void abrirProductos(ActionEvent event) {
    VentanaVistaControlador.crearVentana(
        desktop, 
        "/view/productosVista.fxml", 
        "Gestión de Productos"
    );
}
```

### 📏 Caso 2: Ventana con tamaño específico
```java
@FXML
private void abrirClientes(ActionEvent event) {
    VentanaVistaControlador.crearVentana(
        desktop, 
        "/view/ClientesView.fxml", 
        "Gestión de Clientes",
        900,  // ancho
        650   // alto
    );
}
```

### 🎛️ Caso 3: Necesitas el controlador de la vista
```java
@FXML
private void abrirReportes(ActionEvent event) {
    var resultado = VentanaVistaControlador.crearVentana(
        desktop, 
        "/view/reportesView.fxml", 
        "Reportes del Sistema",
        1000,
        700
    );
    
    // Obtener el controlador
    ReportesControlador ctrl = resultado.getControlador(ReportesControlador.class);
    
    // Hacer algo con el controlador
    ctrl.cargarDatos();
}
```

---

## 🎨 Personalización

### Dimensiones por Defecto
Si no especificas dimensiones, usa: **640x420 píxeles**

```java
// Estas dos llamadas son equivalentes:
VentanaVistaControlador.crearVentana(desktop, "/view/miVista.fxml", "Título");
VentanaVistaControlador.crearVentana(desktop, "/view/miVista.fxml", "Título", 640, 420);
```

### Posicionamiento Automático
Las ventanas se posicionan automáticamente en **cascada**:
- Primera ventana: (30, 30)
- Segunda ventana: (54, 48)
- Tercera ventana: (78, 66)
- etc.

---

## ⚠️ Manejo de Errores

### Automático
El método maneja errores automáticamente mostrando un diálogo de alerta.

### Manual (opcional)
Si quieres manejar errores personalizados:

```java
try {
    VentanaVistaControlador.crearVentana(desktop, "/view/miVista.fxml", "Mi Vista");
} catch (RuntimeException e) {
    // Tu manejo personalizado
    System.err.println("Error personalizado: " + e.getMessage());
    // Mostrar mensaje al usuario de otra forma
}
```

---

## 📋 Checklist para Crear una Nueva Ventana

- [ ] Crear el archivo FXML en `/src/main/resources/view/`
- [ ] Crear el controlador Java en `/src/main/java/.../controller/`
- [ ] En el controlador que abre la ventana:
  - [ ] Tener acceso al `desktop` (Pane)
  - [ ] Llamar a `VentanaVistaControlador.crearVentana()`
  - [ ] Especificar: desktop, ruta FXML, título
  - [ ] Opcionalmente: especificar ancho y alto
- [ ] ¡Listo! 🎉

---

## 🆚 Comparación Rápida

| Característica | Método Antiguo | Método Nuevo |
|----------------|----------------|--------------|
| Líneas de código | 15+ | 1-8 |
| Complejidad | Alta | Baja |
| Propenso a errores | Sí | No |
| Requiere imports | 5-6 | 1-2 |
| Manejo de errores | Manual | Automático |
| Posicionamiento | Manual | Automático |
| Acceso al controlador | Directo | Via ResultadoVentana |

---

## 💡 Tips y Mejores Prácticas

1. **Usa dimensiones apropiadas:**
   - Formularios pequeños: 600x400
   - Tablas: 800-1000x500-600
   - Reportes: 900-1200x600-800

2. **Nombres de vistas FXML:**
   - Usa nombres descriptivos
   - Sigue convención: `nombreVista.fxml`
   - Ejemplo: `productosVista.fxml`, `ClientesView.fxml`

3. **Solo obtén el controlador si lo necesitas:**
   - Si solo quieres abrir la vista, no necesitas el controlador
   - Si necesitas inicializar datos o llamar métodos, entonces sí

4. **Títulos descriptivos:**
   - Usa títulos claros y descriptivos
   - Ejemplos: "Gestión de Productos", "Reportes de Ventas"

---

## 📞 ¿Necesitas Ayuda?

### Documentación Completa
Lee `VENTANAS_INTERNAS.md` para ejemplos más detallados

### Ejemplos Antes/Después
Consulta `EJEMPLO_COMPLETO.md` para ver comparaciones completas

### Resumen de Cambios
Ver `CAMBIOS_VENTANAS.md` para entender qué cambió

---

## 🎓 Ejemplo Completo de Controlador

```java
package com.unpsjb.poo.controller;

import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import javafx.event.ActionEvent;
import com.unpsjb.poo.controller.VentanaVistaControlador;

public class MiMenuControlador {
    
    @FXML private Pane desktop;
    
    @FXML
    private void abrirProductos(ActionEvent event) {
        VentanaVistaControlador.crearVentana(
            desktop, 
            "/view/productosVista.fxml", 
            "Productos", 
            1000, 
            600
        );
    }
    
    @FXML
    private void abrirClientes(ActionEvent event) {
        VentanaVistaControlador.crearVentana(
            desktop, 
            "/view/ClientesView.fxml", 
            "Clientes", 
            900, 
            550
        );
    }
    
    @FXML
    private void abrirReportes(ActionEvent event) {
        var resultado = VentanaVistaControlador.crearVentana(
            desktop, 
            "/view/reportesView.fxml", 
            "Reportes", 
            1100, 
            700
        );
        
        // Si necesito el controlador
        ReportesControlador ctrl = resultado.getControlador(ReportesControlador.class);
        ctrl.inicializar();
    }
}
```

---

**¡Ahora estás listo para usar el método `crearVentana()`! 🚀**
