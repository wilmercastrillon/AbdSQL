# AbdSQL

Programa en java para la administración de bases de datos SQL, el programa 
compilado se encuentra en el archivo zip "AbdSql", para ejecutarlo es necesario 
tener instalado Java jre.

Permite conectar con bases de datos MySQL, Oracle y PostgreSQL cuenta con 
funciones basicas para la creacion y modificacion de tablas, registros, triggers 
y procedimientos ademas permite ejecutar query's y update's desde comandos SQL.

Incluye opciones para exportar las bases de datos a codigo SQL, y cuenta con 
una funcion en etapa beta para migrar de MySQL a PostgreSQL.

Utiliza librerias externas para la conexion a las bases de datos, generar 
diccionarios de datos y para el editor de textos con autocompletar.

## Instalacion
Unicamente requiere de descargar el zip AbdSQL_v3, descomprimir y abrir el 
ejecutable java, es necesario tener instalado Java jre.

## Herramientas
Generador de plantilla MVC(Modelo vista controlador) en codigo C#.
Generador diccionario de datos en archivo de Microsoft Word.
Exportador a codigo SQL para bases de datos MySQL y PostgreSQL.
Convertidor de MySQL a PostgreSQL.

## librerias externas
* [Apache poi](https://poi.apache.org/) - Para generar documentos de word con el diccionario de datos
* [RSyntaxTextArea](https://github.com/bobbylight/RSyntaxTextArea) - Como editor de texto para el codigo SQL.
* [AutoComplete](https://github.com/bobbylight/AutoComplete) - Para autocompletar codigo SQL en el RSyntaxTextArea
* Conectores de MySQL, Oracle y PostgreSQL para Java.
