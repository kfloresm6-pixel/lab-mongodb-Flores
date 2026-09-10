# Laboratorio: Java + MongoDB Atlas con GitHub Codespaces

## Curso
Programación II

## Objetivo
Construir una aplicación Java ejecutada en GitHub Codespaces que se conecte a MongoDB Atlas y permita realizar operaciones CRUD sobre una colección de productos.

## Tecnologías
- Java 17
- Maven
- GitHub
- GitHub Codespaces
- MongoDB Atlas
- MongoDB Java Sync Driver

## Caso de estudio
La aplicación administrará productos con los campos:

- `codigo`
- `nombre`
- `categoria`
- `precio`
- `existencia`

Base de datos: `tienda`  
Colección: `productos`

## 1. Crear el Codespace
1. Abra este repositorio en GitHub.
2. Seleccione **Code > Codespaces**.
3. Seleccione **Create codespace on main**.
4. Espere a que se abra Visual Studio Code en el navegador.

## 2. Configurar MongoDB Atlas
1. Cree un proyecto en MongoDB Atlas.
2. Cree una implementación gratuita disponible.
3. Cree un usuario de base de datos.
4. Configure **Network Access** para permitir la conexión desde el Codespace.
5. En **Connect > Drivers > Java**, copie la cadena de conexión.

## 3. Configurar el secreto
No escriba la contraseña dentro del código.

Cree un secreto de Codespaces llamado:

`MONGODB_URI`

Como alternativa temporal para una sesión de laboratorio:

```bash
export MONGODB_URI='mongodb+srv://USUARIO:CLAVE@CLUSTER/?retryWrites=true&w=majority'
```

No suba esta cadena al repositorio.

## 4. Compilar
```bash
mvn clean compile
```

## 5. Ejecutar
```bash
mvn exec:java
```

Al inicio debe mostrarse:

```text
=== Java + MongoDB Atlas ===
Conexión exitosa.
```

## 6. Trabajo a realizar
Complete `ProductoDAO.java` e implemente en `App.java` un menú que incluya:

1. Agregar producto.
2. Listar productos.
3. Buscar producto por código.
4. Actualizar precio.
5. Actualizar existencia.
6. Eliminar producto.
7. Mostrar productos con poco inventario.
0. Salir.

## 7. Requisitos
- Utilizar `insertOne()`.
- Utilizar `find()`.
- Utilizar un filtro con `Filters.eq()`.
- Utilizar `updateOne()`.
- Utilizar `deleteOne()`.
- Utilizar una consulta con `Filters.lt()` o equivalente.
- Validar entradas básicas.
- No almacenar credenciales dentro del código.
- Mantener una estructura clara de clases.

## 8. Evidencias
Incluya en la entrega:

- Enlace al repositorio.
- Captura de la aplicación ejecutándose en Codespaces.
- Captura de la colección `productos` en MongoDB Atlas.
- Evidencia de insertar, buscar, actualizar y eliminar.
- Al menos 5 productos registrados.
- Respuestas a las preguntas de análisis solicitadas por el docente.

## Seguridad
Nunca suba:
- contraseña de Atlas;
- URI completa con contraseña;
- archivos `.env`;
- capturas que muestren credenciales.

## Estructura esperada
```text
.
├── .devcontainer/
│   └── devcontainer.json
├── src/
│   ├── main/
│   │   └── java/
│   │       └── edu/
│   │           └── umg/
│   │               ├── App.java
│   │               ├── ConexionMongo.java
│   │               ├── Producto.java
│   │               └── ProductoDAO.java
│   └── test/
│       └── java/
│           └── edu/
│               └── umg/
├── .gitignore
├── pom.xml
└── README.md
```

## Entrega
Realice `commit` y `push` de todos los archivos de código. Verifique nuevamente que las credenciales no estén presentes en el historial del repositorio.


## Preguntas de Análisis

1. **¿Qué diferencia existe entre una tabla SQL y una colección MongoDB?**
Una tabla SQL tiene una estructura fija de columnas y tipos de datos definidos previamente (esquema rígido), y todos sus registros (filas) deben respetar esa estructura. Una colección de MongoDB no exige un esquema fijo: cada documento dentro de ella puede tener campos distintos, y la estructura puede variar entre documentos.

2. **¿Qué representa un documento en MongoDB?**
Un documento representa un registro individual dentro de una colección, almacenado en formato tipo JSON (BSON internamente). Equivale a una fila en una tabla SQL, pero con estructura flexible.

3. **¿Cuál es la función del campo `_id`?**
Es el identificador único de cada documento dentro de una colección. MongoDB lo genera automáticamente si no se especifica, y sirve para localizar, actualizar o eliminar ese documento de forma inequívoca.

4. **¿Dónde se ejecuta Java cuando utiliza GitHub Codespaces?**
Se ejecuta en un contenedor de desarrollo alojado en los servidores de GitHub (en la nube), al cual se accede remotamente a través del navegador; no se ejecuta en la computadora local del estudiante.

5. **¿Dónde se almacenan los datos cuando utiliza MongoDB Atlas?**
Se almacenan en un clúster alojado en la infraestructura en la nube de MongoDB Atlas (sobre proveedores como AWS, Azure o GCP), no en el disco local de la computadora ni del Codespace.

6. **¿Qué función cumple el MongoDB Java Driver?**
Es la librería que permite que una aplicación Java se comunique con una base de datos MongoDB: traduce las instrucciones del código Java (insertar, buscar, actualizar, eliminar) al protocolo que MongoDB entiende, y gestiona la conexión de red.

7. **¿Qué ventaja representa utilizar una base de datos en la nube?**
No es necesario instalar ni administrar un servidor de base de datos localmente; se puede acceder desde cualquier lugar con internet, escala automáticamente, y ofrece respaldos y alta disponibilidad gestionados por el proveedor.

8. **¿Por qué no debe almacenarse la contraseña dentro de App.java?**
Porque el código fuente se sube a un repositorio público en GitHub. Si la contraseña estuviera escrita ahí, cualquier persona con acceso al repositorio (o a su historial de commits) podría verla y usarla para acceder a la base de datos sin autorización.

9. **¿Para qué se utiliza la variable de entorno MONGODB_URI?**
Para guardar la cadena de conexión completa (incluyendo usuario y contraseña) fuera del código fuente, de forma que la aplicación pueda leerla en tiempo de ejecución con `System.getenv()` sin que quede expuesta ni se suba al repositorio.

10. **Explique paso a paso el flujo: Usuario → Java → Codespaces → Java Driver → Internet → Atlas**
El usuario interactúa con el menú de la aplicación Java (App.java), que se ejecuta dentro del contenedor de GitHub Codespaces. Cuando el usuario elige una operación (por ejemplo, insertar un producto), la aplicación llama a un método del ProductoDAO, el cual usa el MongoDB Java Driver para construir la petición correspondiente. El driver toma la cadena de conexión (MONGODB_URI) y envía la solicitud a través de internet hasta el clúster de MongoDB Atlas, donde se ejecuta la operación sobre la colección `productos` y se retorna el resultado por el mismo camino de vuelta hasta la aplicación.