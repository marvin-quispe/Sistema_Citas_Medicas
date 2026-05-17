# \# Sistema de Citas Médicas para Clínica

# 

# \### 📚 Curso: Técnicas de Programación Orientada a Objetos

# \### 🏫 Universidad Privada del Norte (UPN)

# 

# \## 👥 Integrantes

# \* \*\*Quispe Puscan, Marvin Harley\*\* - N00429145

# \* \*\*Levano Diaz, Evelyn Gisela\*\* - N00438718

# \* \*\*Choque Martinez, José Carlos\*\* - N00441596

# \* \*\*Mendoza Yucra, Liz Jacqueline\*\* - N00541462

# 

# \## 📌 Descripción del Proyecto

# Este sistema busca automatizar la gestión de citas en una clínica, permitiendo el registro de pacientes, médicos y la programación eficiente de turnos, aplicando los principios fundamentales de la POO.

# 

# SISTEMA DE CITAS MÉDICAS - GRUPO\_04

# ====================================

# Guia de instalacion para enlazar db y ejecutar los roles

# 

# REQUISITOS:

# \- Java JDK 25

# \- Eclipse IDE

# \- MySQL Server 8.0

# \- MySQL Workbench 8.0

# 

# ====================================

# PASO 1: Importar proyecto en Eclipse

# ====================================

# 1\. Eclipse -> File -> Import -> General -> Existing Projects into Workspace

# 2\. Select root directory -> Buscar la carpeta del proyecto

# 3\. Finish

# 

# ====================================

# PASO 2: Agregar conector MySQL JDBC

# ====================================

# 1\. Descargar de: https://dev.mysql.com/downloads/connector/j/

# 2\. Copiar mysql-connector-j-X.X.X.jar a la carpeta lib/

# 3\. En Eclipse: F5 (refrescar)

# 4\. Click derecho en proyecto -> Build Path -> Configure Build Path

# 5\. Pestaña Libraries -> Classpath -> Add JARs...

# 6\. Seleccionar lib/mysql-connector-j-X.X.X.jar -> Apply -> Close

# 

# ====================================

# PASO 3: Crear base de datos

# ====================================

# 1\. Abrir MySQL Workbench

# 2\. File -> Open SQL Script -> sql/schema.sql

# 3\. Ejecutar (Ctrl+Shift+Enter o rayo)

# 4\. Verificar que aparezca 'sistema\_citas\_medicas' en SCHEMAS

# 

# ====================================

# PASO 4: Configurar conexion

# ====================================

# 1\. Abrir el archivo 'config.properties' (en la raiz del proyecto)

# 2\. Editar la linea: db.password=root

# &#x20;  Poner la contrasena de su MySQL

# &#x20;  Si su contrasena es vacia, poner: db.password=

# 3\. Guardar el archivo (No necesita recompilar)

# 

# ====================================

# PASO 5: Ejecutar

# ====================================

# 1\. En Eclipse: Project -> Clean -> Clean All Projects

# 2\. Ejecutar Main.java (src/principal/Main.java)

# 3\. Usuarios de prueba: admin/recepcionista/medico/cajero/paciente

# &#x20;  Contrasena: 1234

# 

# ====================================

# USUARIOS DEL SISTEMA

# ====================================

# Usuario      | Contrasena | Rol

# \-------------|------------|--------------

# admin        | 1234       | Administrador

# recepcionista| 1234       | Recepcionista

# medico       | 1234       | Medico

# cajero       | 1234       | Cajero

# paciente     | 1234       | Paciente



