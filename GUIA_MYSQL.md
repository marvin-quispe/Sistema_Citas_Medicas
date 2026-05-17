# GUÍA DE INSTALACIÓN — CONEXIÓN A MySQL

## Prerrequisitos

1. **MySQL Server** instalado y ejecutándose (puerto 3306)
2. **MySQL Workbench** instalado
3. **Java JDK 25** (ya lo tienes)

---

## Paso 1: Descargar el conector JDBC

1. Ve a: https://dev.mysql.com/downloads/connector/j/
2. Selecciona **Platform Independent → ZIP**
3. Descarga el archivo (ej: `mysql-connector-j-9.2.0.zip`)
4. Extrae el archivo y copia `mysql-connector-j-9.2.0.jar` (o similar) a:
   ```
   SistemaCitasMedicas2\lib\
   ```
5. En Eclipse: Selecciona el proyecto → F5 (Refresh)
6. Click derecho en el proyecto → **Build Path → Configure Build Path**
7. Pestaña **Libraries** → **Classpath** → **Add JARs...**
8. Selecciona `lib\mysql-connector-j-X.X.X.jar` → Apply → Close

---

## Paso 2: Crear la base de datos en MySQL Workbench

1. Abre **MySQL Workbench**
2. Conéctate a tu servidor local
3. Ve a **File → Open SQL Script**
4. Selecciona: `SistemaCitasMedicas2\sql\schema.sql`
5. Ejecuta el script completo (icono ⚡ o Ctrl+Shift+Enter)

Esto creará:
- Base de datos: `sistema_citas_medicas`
- Tablas: especialidades, medicos, pacientes, usuarios, horarios, citas, pagos, urgencias
- Datos de prueba (médicos, pacientes, usuarios)

---

## Paso 3: Verificar credenciales MySQL

Abre el archivo:

```
src\util\DatabaseConnection.java
```

Verifica que coincidan con tu instalación MySQL:

```java
private static final String URL = "jdbc:mysql://localhost:3306/sistema_citas_medicas";
private static final String USER = "root";
private static final String PASSWORD = ""; // ← tu contraseña aquí
```

---

## Paso 4: Ejecutar

1. En Eclipse: **Project → Clean → Build All**
2. Ejecuta `Main.java` (o `Login.java`)

Si todo está bien:
- Se conectará automáticamente a MySQL
- Cargará los usuarios de prueba desde la base de datos
- Usuarios: admin / recepcionista / medico / cajero / paciente — todos con contraseña 1234

---

## Estructura de archivos creados

| Archivo | Propósito |
|---|---|
| `sql/schema.sql` | Script para MySQL Workbench (crear DB, tablas, datos de prueba) |
| `src/util/DatabaseConnection.java` | Conexión singleton a MySQL |
| `src/datos/Lista*.java` | Modificados: ya no usan ArrayList, usan JDBC |

---

## Notas importantes

- La contraseña de root por defecto en XAMPP/WAMP es vacía (`""`)
- Si usas MySQL 8+ instalado aparte, la contraseña es la que configuraste
- Los datos de prueba se insertan desde el SQL, no desde Java
- Si necesitas cambiar usuario/contraseña, edita solo `DatabaseConnection.java`
