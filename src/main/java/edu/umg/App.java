package edu.umg;

import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        System.out.println("=== Java + MongoDB Atlas ===");

        if (!ConexionMongo.probarConexion()) {
            System.out.println("Revise MONGODB_URI y la configuración de red de Atlas.");
            return;
        }
        System.out.println("Conexión exitosa.");

        try (ProductoDAO dao = new ProductoDAO()) {
            Scanner sc = new Scanner(System.in);
            int opcion;

            do {
                mostrarMenu();
                opcion = leerEntero(sc, "Seleccione una opción: ");

                switch (opcion) {
                    case 1 -> agregarProducto(sc, dao);
                    case 2 -> dao.listar();
                    case 3 -> buscarProducto(sc, dao);
                    case 4 -> actualizarPrecio(sc, dao);
                    case 5 -> actualizarExistencia(sc, dao);
                    case 6 -> eliminarProducto(sc, dao);
                    case 7 -> productosPocoInventario(sc, dao);
                    case 8 -> System.out.println("Saliendo...");
                    default -> System.out.println("Opción inválida. Intente de nuevo.");
                }
                System.out.println();
            } while (opcion != 8);
        }
    }

    private static void mostrarMenu() {
        System.out.println("=================================");
        System.out.println("      TIENDA - MONGODB ATLAS");
        System.out.println("=================================");
        System.out.println("1. Agregar producto");
        System.out.println("2. Listar productos");
        System.out.println("3. Buscar producto");
        System.out.println("4. Actualizar precio");
        System.out.println("5. Actualizar existencia");
        System.out.println("6. Eliminar producto");
        System.out.println("7. Productos con poco inventario");
        System.out.println("8. Salir");
    }

    private static void agregarProducto(Scanner sc, ProductoDAO dao) {
        System.out.print("Código: ");
        String codigo = sc.nextLine().trim();
        if (dao.buscarPorCodigo(codigo) != null) {
            System.out.println("Ya existe un producto con ese código.");
            return;
        }
        System.out.print("Nombre: ");
        String nombre = sc.nextLine().trim();
        System.out.print("Categoría: ");
        String categoria = sc.nextLine().trim();
        double precio = leerDecimalNoNegativo(sc, "Precio: ");
        int existencia = leerEnteroNoNegativo(sc, "Existencia: ");

        dao.insertar(new Producto(codigo, nombre, categoria, precio, existencia));
    }

    private static void buscarProducto(Scanner sc, ProductoDAO dao) {
        System.out.print("Código a buscar: ");
        dao.buscarPorCodigo(sc.nextLine().trim());
    }

    private static void actualizarPrecio(Scanner sc, ProductoDAO dao) {
        System.out.print("Código del producto: ");
        String codigo = sc.nextLine().trim();
        if (dao.buscarPorCodigo(codigo) == null) return;
        double nuevoPrecio = leerDecimalNoNegativo(sc, "Nuevo precio: ");
        dao.actualizarPrecio(codigo, nuevoPrecio);
    }

    private static void actualizarExistencia(Scanner sc, ProductoDAO dao) {
        System.out.print("Código del producto: ");
        String codigo = sc.nextLine().trim();
        if (dao.buscarPorCodigo(codigo) == null) return;
        int nuevaExistencia = leerEnteroNoNegativo(sc, "Nueva existencia: ");
        dao.actualizarExistencia(codigo, nuevaExistencia);
    }

    private static void eliminarProducto(Scanner sc, ProductoDAO dao) {
        System.out.print("Código del producto a eliminar: ");
        String codigo = sc.nextLine().trim();
        if (dao.buscarPorCodigo(codigo) == null) return;
        dao.eliminar(codigo);
    }

    private static void productosPocoInventario(Scanner sc, ProductoDAO dao) {
        int limite = leerEnteroNoNegativo(sc, "Mostrar productos con existencia menor a: ");
        dao.listarPocoInventario(limite);
    }

    private static int leerEntero(Scanner sc, String mensaje) {
        while (true) {
            System.out.print(mensaje);
            try {
                return Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Ingrese un número entero.");
            }
        }
    }

    private static int leerEnteroNoNegativo(Scanner sc, String mensaje) {
        while (true) {
            int valor = leerEntero(sc, mensaje);
            if (valor < 0) System.out.println("El valor no puede ser negativo.");
            else return valor;
        }
    }

    private static double leerDecimalNoNegativo(Scanner sc, String mensaje) {
        while (true) {
            System.out.print(mensaje);
            try {
                double valor = Double.parseDouble(sc.nextLine().trim());
                if (valor < 0) System.out.println("El precio no puede ser negativo.");
                else return valor;
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Ingrese un número.");
            }
        }
    }
}