# Sistema de Gestión - Casa de Computación "MundoPC"

## 1. Integrantes del Equipo
- Caseres, Marcos David
- Coronel, Alexis
- Ordeig, Simon
- Vilte, Elias

---

## 2. Dominio y Alcance del Sistema

### Descripción del Problema

Se busca desarrollar una aplicación de escritorio para la gestión integral de una casa de computación. El sistema permite administrar de forma centralizada y eficiente la información de productos, clientes, ventas y usuarios del sistema.

Actualmente, estas tareas suelen realizarse de manera manual o con sistemas desintegrados, lo que dificulta el control del stock, el registro de clientes y el seguimiento de las ventas. La falta de un sistema centralizado puede generar errores, pérdidas de información, demoras en la atención al cliente y utilización ineficaz del personal contratado.

El sistema propuesto tiene como objetivo digitalizar estos procesos básicos, ofreciendo una herramienta que centralice la información y facilite la gestión eficiente diaria del negocio.

### Objetivo del Sistema

El sistema es una aplicación funcional y extensible que permite a la casa de computación:
- Mantener un inventario actualizado de productos (físicos y digitales)
- Automatizar procesos de venta y reducir errores manuales
- Registrar ventas de manera estructurada y confiable
- Administrar datos de clientes en un repositorio centralizado
- Gestionar usuarios del sistema con diferentes roles y permisos
- Generar reportes y auditorías de las operaciones realizadas
- Exportar información a PDF para facturación y reportes

Con un diseño modular basado en patrones de diseño reconocidos para posibilitar la incorporación de nuevas funcionalidades en el futuro.

---

## Funcionalidades Principales

### 2.1. Sistema de Gestión de Inventario

**Productos Físicos y Digitales:**
- Alta, baja y modificación de productos
- Soporte para productos físicos (con fabricante) y digitales (con proveedor)
- Gestión de categorías de productos
- Consulta de stock disponible en tiempo real
- Actualización automática de stock al realizar ventas
- Gestión de fabricantes de productos físicos
- Gestión de proveedores digitales
- Búsqueda y filtrado de productos por múltiples criterios

### 2.2. Gestión de Clientes

- Registro de nuevos clientes con datos completos (nombre, CUIT, dirección, teléfono, email)
- Modificación de datos de clientes existentes
- Clasificación de clientes por tipo (Consumidor Final, Responsable Inscripto, etc.)
- Estado de actividad de clientes (activo/inactivo)
- Búsqueda y filtrado de clientes

### 2.3. Gestión de Ventas

**Proceso de Venta con Patrón State:**

El sistema implementa un flujo de venta mediante el patrón State, con las siguientes etapas:

1. **Agregar Productos al Carrito:**
   - Selección de productos del inventario
   - Especificación de cantidades
   - Cálculo automático de subtotales
   - Validación de stock disponible

2. **Ingreso de Datos de Factura:**
   - Selección del cliente
   - Elección del tipo de factura (Factura o Ticket)
   - Validación de datos requeridos según tipo de factura

3. **Confirmación de Pago:**
   - Selección de método de pago (Estrategia de pago)
   - Cálculo de comisiones según método seleccionado
   - Confirmación final de la transacción

**Características adicionales:**
- Generación de código único por venta (FACT-XXXX o TICK-XXXX)
- Actualización automática de stock al confirmar venta
- Cancelación de venta en cualquier etapa del proceso
- Persistencia de ventas en base de datos
- Exportación de facturas a PDF

### 2.4. Gestión de Usuarios
 
- Registro de usuarios del sistema (empleados y administradores)
- Autenticación mediante login con usuario y contraseña
- Control de roles (ADMINISTRADOR, EMPLEADO)
- Gestión de permisos según rol
- Estado de usuario (activo/inactivo)
- Cambio de contraseña
- Auditoría de acciones por usuario

### 2.5. Sistema de Reportes y Auditoría

- Registro automático de todas las multiples del sistema
- Consulta de auditoría por:
- Usuario que realizó la acción
- Tipo de acción (INSERT, UPDATE, DELETE, LOGIN, etc.)
- Entidad afectada (productos, clientes, ventas, etc.)
- Rango de fechas
- Exportación de reportes a PDF
- Visualización detallada de eventos en tabla con filtros

### 2.6. Interfaz Gráfica (GUI)

- **Login:** Autenticación de usuarios con validación
- **Ventana Principal:** Panel intuitivo con acceso a todas las funcionalidades
- **Gestión de Productos:** Tablas con búsqueda, filtros y formularios de ABM
- **Gestión de Clientes:** Interfaz similar para administración de clientes
- **Proceso de Facturación:** Flujo guiado por pasos con validaciones
- **Reportes:** Interfaz de consulta y exportación
- **Formularios Modal:** Para alta y modificación de entidades
- **Validaciones en tiempo real:** Feedback inmediato al usuario

---

## 3. Arquitectura y Diseño

### 3.1. Patrones de Diseño Implementados

#### **Patrón DAO (Data Access Object)**

Implementado para toda la capa de persistencia:

- **Interfaz genérica:** `DAO<T>`
- **Implementaciones concretas:**
  - `ProductoDAOImpl`
  - `ClienteDAOImpl`
  - `VentaDAOImpl`
  - `CategoriaDAOImpl`
  - `FabricanteDAOImpl`
  - `ProveedorDigitalDAOImpl`

**Beneficios:**
- Separación entre lógica de negocio y acceso a datos
- Facilita cambio de tecnología de persistencia
- Operaciones CRUD estandarizadas

#### **Patrón Singleton**

Implementado en:
- `GestorDeConexion` - Gestión centralizada de conexiones a base de datos
- DAOs compartidos entre instancias de modelos

### 3.2 Patrones de Diseño Adicional

#### **Patrón State **

Implementado en el proceso de ventas para manejar las diferentes etapas de una transacción:

- **Interfaz:** `EstadoVenta`
- **Contexto:** `Venta`
- **Estados Concretos:**
  - `EstadoAgregarProductos` - Primera etapa: agregar productos al carrito
  - `EstadoDatosFactura` - Segunda etapa: ingresar datos de facturación
  - `EstadoConfirmacionPago` - Tercera etapa: confirmar método de pago

**Beneficios:**
- Separación clara de responsabilidades por etapa
- Facilita agregar nuevos estados sin modificar el contexto
- Validaciones específicas por estado
- Control del flujo de navegación (siguiente/anterior)

#### **Patrón Strategy **

Implementado para manejar diferentes métodos de pago:

- **Interfaz:** `EstrategiaPago`
- **Estrategias Concretas:**
  - `PagoEfectivo` - Sin comisión, pago inmediato
  - `PagoTarjeta` - Con comisión 

**Beneficios:**
- Fácil incorporación de nuevos métodos de pago
- Encapsulación de lógica específica de cada método
- Cálculo dinámico de comisiones

### 3.3 Programación Concurrente (Hilos)

- Procesamiento asíncrono de exportación de PDF

### 3.4. Diagrama de Diseño
<img width="1391" height="396" alt="diagrama-patrones-strategy" src="https://github.com/user-attachments/assets/1725ec7f-d242-48d1-9158-e27bf6873465" />
<img width="1563" height="396" alt="diagrama-patrones-state-" src="https://github.com/user-attachments/assets/9b817484-6b3f-4d36-a487-aaf079f189a4" />


### 3.5. ** Prototipo de la IGU (WireFrame) ** --- INSERT SCREENSHOTS
![IMAGEN_SISTEMA](https://github.com/user-attachments/assets/58e1f114-c341-4179-8d5e-8b9fd24a15a5)


## 4. Stack Tecnológico

### 4.1. Lenguajes y Frameworks

- **Lenguaje:** Java 21 
- **Framework GUI:** JavaFX 21.0.4

### 4.2. Base de Datos

- **SGBD:** PostgreSQL

### 4.3. Herramientas de Desarrollo

- **IDEs soportados:** Visual Studio Code, Eclipse, NetBeans
- **Control de Versiones:** Git/GitHub
