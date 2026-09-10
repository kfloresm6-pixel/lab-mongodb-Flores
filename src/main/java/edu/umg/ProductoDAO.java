package edu.umg;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;

public class ProductoDAO implements AutoCloseable {

    private final MongoClient cliente;
    private final MongoCollection<Document> productos;

    public ProductoDAO() {
        cliente = ConexionMongo.conectar();
        MongoDatabase db = cliente.getDatabase("tienda");
        productos = db.getCollection("productos");
    }

    public void insertar(Producto producto) {
        Document doc = new Document("codigo", producto.getCodigo())
                .append("nombre", producto.getNombre())
                .append("categoria", producto.getCategoria())
                .append("precio", producto.getPrecio())
                .append("existencia", producto.getExistencia());
        productos.insertOne(doc);
        System.out.println("Producto insertado correctamente.");
    }

    public void listar() {
        for (Document doc : productos.find()) {
            imprimirProducto(doc);
        }
    }

    public Document buscarPorCodigo(String codigo) {
        Document doc = productos.find(Filters.eq("codigo", codigo)).first();
        if (doc == null) {
            System.out.println("Producto no encontrado.");
        } else {
            imprimirProducto(doc);
        }
        return doc;
    }

    public void actualizarExistencia(String codigo, int nuevaExistencia) {
        UpdateResult r = productos.updateOne(Filters.eq("codigo", codigo), Updates.set("existencia", nuevaExistencia));
        System.out.println(r.getModifiedCount() > 0 ? "Existencia actualizada." : "No se encontró un producto con ese código.");
    }

    public void actualizarPrecio(String codigo, double nuevoPrecio) {
        UpdateResult r = productos.updateOne(Filters.eq("codigo", codigo), Updates.set("precio", nuevoPrecio));
        System.out.println(r.getModifiedCount() > 0 ? "Precio actualizado." : "No se encontró un producto con ese código.");
    }

    public void eliminar(String codigo) {
        DeleteResult r = productos.deleteOne(Filters.eq("codigo", codigo));
        System.out.println(r.getDeletedCount() > 0 ? "Producto eliminado." : "No se encontró un producto con ese código.");
    }

    public void listarPocoInventario(int limite) {
        for (Document doc : productos.find(Filters.lt("existencia", limite))) {
            imprimirProducto(doc);
        }
    }

    private void imprimirProducto(Document doc) {
        System.out.printf("%-8s %-20s %-15s Q%-9.2f %-5d%n",
                doc.getString("codigo"),
                doc.getString("nombre"),
                doc.getString("categoria"),
                doc.getDouble("precio"),
                doc.getInteger("existencia"));
    }

    @Override
    public void close() {
        cliente.close();
    }
}