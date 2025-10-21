package com.unpsjb.poo.model;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import com.unpsjb.poo.model.productos.Producto;


//El carrito de compra solo gestiona los items, no los productos directamente
//Tampoco se encarga de la logica de negocio
public class CarritoDeCompra {
    private List<ItemCarrito> items;



public CarritoDeCompra() {
    this.items = new ArrayList<>();
}

public void agregarItemAlCarrito(Producto producto, int cantidad) {
    ItemCarrito item = new ItemCarrito(producto, cantidad, producto.getPrecioProducto());
    items.add(item);
}

public void eliminarItemDelCarrito(ItemCarrito item) {
    items.remove(item);
}

/**
 * Devulve el total del carrito usando BigDecimal
 */
public BigDecimal getTotal() {
    BigDecimal total = BigDecimal.ZERO.setScale(2,RoundingMode.HALF_UP);

    for (ItemCarrito item : items) {
        total = total.add(item.getSubtotal());  // Suma el subtotal de cada item al total
    }
    return total;
}

public void vaciarCarrito() {
    items.clear();
}

//Getters

public List<ItemCarrito> getItems() {
    return items;
}


//Este getter es importante para la vista, ya que convierte la lista de items a ObservableList
//para que la tabla pueda actualizarse automáticamente al agregar o quitar items
public javafx.collections.ObservableList<ItemCarrito> getItemsObservable() {
    return javafx.collections.FXCollections.observableArrayList(items);
   }
}
