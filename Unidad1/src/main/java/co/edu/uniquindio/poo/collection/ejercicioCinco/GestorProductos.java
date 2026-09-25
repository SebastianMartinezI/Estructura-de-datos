package co.edu.uniquindio.poo.collection.ejercicioCinco;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.TreeMap;

public class GestorProductos {

    private HashMap<String, Producto> hashMap;

    private LinkedHashMap<String, Producto> linkedHashMap;

    private TreeMap<String, Producto> treeMap;

    public GestorProductos() {
        hashMap = new HashMap<>();
        linkedHashMap = new LinkedHashMap<>();
        treeMap = new TreeMap<>();
    }

    public void agregarProducto(Producto producto) {
        hashMap.put(producto.getCodigo(), producto);
        linkedHashMap.put(producto.getCodigo(), producto);
        treeMap.put(producto.getCodigo(), producto);
    }

    public void mostrrarHashMap(){
        System.out.println("\nProducto en la hashMap");

        for(Producto producto: hashMap.values()){
            System.out.println(producto);
        }
    }
    public void mostrrarLinkedHashMap(){
        System.out.println("\nProducto en la linkedHashMap");
        for(Producto producto: linkedHashMap.values()){
            System.out.println(producto);
        }
    }
    public void mostrrarTreeMap(){
        System.out.println("\nProducto en la treeMap");
        for(Producto producto: treeMap.values()){
            System.out.println(producto);
        }
    }
}
