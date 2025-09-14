# poo-tfi-sistema-gestion

## 1. Integrantes del equipo
- Caseres, Marcos David.
- Coronel, Alexis.
- Ordeig, Simon.
- Vilte, Elias.

## 2. Dominio y Alcance del Sistema 

### Descripción del Problema 

Se busca desarrollar una aplicación de escritorio para la gestión de una casa de computación. El sistema permitirá administrar de forma sencilla la información de productos, clientes y ventas.

Actualmente, estas tareas suelen realizarse de manera manual, lo que dificulta el control del stock, el registro de clientes y el seguimiento de las ventas. La falta de un sistema centralizado puede generar errores, pérdidas de información, demoras en la atención al cliente y utilización ineficaz del personal contratado. 

El sistema propuesto tiene como objetivo digitalizar estos procesos básicos, ofreciendo una herramienta que centralice la información y facilite la gestión eficiente diaria del negocio.

### Objetivo del Sistema

El sistema será una aplicación funcional y extensible que permita a la casa de computación:
- Mantener un inventario actualizado de productos.
-  Automatizar procesos y reducir personal necesario.
- Registrar ventas de manera estructurada y confiable.
- Administrar datos de clientes en un repositorio centralizado.

Con un diseño modular para posibilitar la incorporación de nuevas funcionalidades en el futuro.

### Descripcion funcionalidades Principales (Features) 

##### Sistema de gestion de Inventario 
- Alta, baja y modificación de productos.
- Consulta del stock disponible

##### Gestion de clientes
- Registro/modificación de datos básicos de clientes (DNI, nombre, teléfono, correo).
- Consulta de clientes registrados.

##### Gestion de ventas
- Registrar una nueva venta asociada a un cliente.
- Selección de productos vendidos y actualización automática de stock.


##### Interfaz Grafica (IGU)
- Ventana principal intuitiva para la interacción con el sistema.
- Tablas y formularios para visualizar y cargar información de forma clara.

##### Persistencia
- Utilización de base de datos para persistencia de la información.

  
### Funcionalidades Opcionales / Futuras Extensiones 
##### Sistema de autogestion para el cliente
- Interfaz dedicada al cliente para realizar compras de forma autónoma, permitiendo seleccionar productos, generar un ticket y presentarlo en caja para completar el pago.
##### Sistema de registro service
Permite registrar y administrar los servicios ofrecidos por la casa de computación, incluyendo:
- Equipos ingresados con su diagnóstico inicial.
- Estado del servicio: pendiente, en proceso o finalizado.
- Historial de servicios por cliente.

Tipos de servicios contemplados:
- Reparación.
- Mantenimiento.
- Armado de PC.


