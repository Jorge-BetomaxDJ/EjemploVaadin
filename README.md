# Ejecutar el proyecto en modo de desarrollo

`mvn spring-boot:run`

Espere a que se inicie la aplicación

Abra http://localhost:8080/ para ver la aplicación.

Las credenciales predeterminadas son admin/admin para acceso de administrador y user/user para acceso de usuario normal.

Tenga en cuenta que cuando se ejecuta en modo de desarrollo, la aplicación no funcionará en IE11.

# Ejecución de pruebas de integración y Linter

Las pruebas de integración se implementan utilizando TestBench. Las pruebas tardan decenas de minutos en ejecutarse y, por lo tanto, se incluyen en un perfil separado. Recomendamos ejecutar pruebas con una compilación de producción para minimizar la posibilidad de que las cadenas de herramientas del tiempo de desarrollo afecten la estabilidad de la prueba. Para ejecutar las pruebas, ejecute

`mvn verify -Pit,production`

y asegúrese de tener instalada una licencia válida de TestBench.

Profile `it` agrega los siguientes parámetros para ejecutar pruebas de integración:
```sh
-Dwebdriver.chrome.driver=path_to_driver
-Dcom.vaadin.testbench.Parameters.runLocally=chrome
```

si desea ejecutar una prueba separada, asegúrese de haber agregado estos parámetros a las Opciones de VM de la configuración de ejecución de JUnit

Ejecute linter para verificar el código de interfaz agregando `-DrunLint` al comando de compilación/ejecución.

# Reinicio automático y recarga en vivo

Para activar spring-boot-devtools es necesario:
1. Agregue la dependencia spring-boot-devtools
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-devtools</artifactId>
    <optional>true</optional>
    <scope>runtime</scope>
</dependency>
```
2. Bifurque el proceso utilizado para ejecutar la aplicación cambiando la configuración de spring-boot-maven-plugin
```xml
<plugin>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-maven-plugin</artifactId>
    <version>${spring-boot.version}</version>
    <configuration>
        <fork>true</fork>
    </configuration>
</plugin>
```
3. Opcionalmente, es posible que desee evitar que el generador de datos se ejecute en cada recarga individual, por lo tanto, haga que la base de datos H2 almacene entidades en el sistema de archivos en lugar de en la memoria agregando las siguientes líneas a `src/main/resources/application.properties`
```properties
spring.datasource.url=jdbc:h2:file:~/test-data
spring.jpa.hibernate.ddl-auto=update
```
Para activar el reinicio, es necesario actualizar classpath.
En Eclipse se puede hacer automáticamente después de guardar el archivo modificado.
En IntelliJ IDEA se puede hacer manualmente `Build -> Build Project`

Se admite la recarga en vivo y las extensiones del navegador se pueden encontrar en http://livereload.com/extensions/.

# Ejecutar el proyecto en modo de producción

`mvn spring-boot:run -Pproduction`

El modo predeterminado cuando se construye o inicia la aplicación es 'desarrollo'. El modo de 'producción' se activa habilitando el perfil de 'producción' al compilar o iniciar la aplicación.

En el modo de 'producción', todos los recursos de interfaz de la aplicación se pasan a través del comando 'construcción de polímero', que los minimiza y genera dos versiones: para navegadores compatibles con ES5 y ES6. Eso agrega tiempo adicional al proceso de compilación, pero reduce el tamaño total de descarga para los clientes y permite ejecutar la aplicación en navegadores que no son compatibles con ES6 (por ejemplo, en Internet Explorer 11).

Tenga en cuenta que si cambia entre el modo de producción y el modo de desarrollo, debe hacer
```sh
mvn clean
```
ntes de ejecutar en el otro modo.

# Ejecutando en Eclipse o IntelliJ
Como ambos IDE admiten la ejecución de aplicaciones Spring Boot, solo tiene que importar el proyecto y seleccionar `com.examen.jorge.Application` como clase principal si no se hace automáticamente. El uso de un IDE también le permitirá acelerar aún más el desarrollo. Simplemente consulte https://vaadin.com/blog/developing-without-server-restarts.

## IntelliJ < 2018
Desafortunadamente, hasta las dependencias de IntelliJ 2017 definidas como "proporcionadas" en Maven POM no se cargarán al inicio. Como solución, tendrá que eliminar la definición de alcance de `spring-boot-starter-tomcat` y `javax.servlet-api` del pom.xml.

# Ejecución de pruebas de escalabilidad

Esta aplicacion incluye pruebas de escalabilidad. Una vez que haya implementado una compilación de producción de la aplicacion, puede ejecutarla para verificar cómo se comporta la aplicación bajo carga. Las pruebas de escalabilidad se pueden ejecutar completamente en su máquina local, pero también puede ejecutar localmente solo los agentes de prueba mientras la aplicación que se está probando se implementa en un entorno cercano a su producción.

Para ejecutar las pruebas de escalabilidad localmente:

1. Asegúrese de estar usando Java 8 (el complemento Gatling Maven aún no funciona con Java 9+)

1. Cree e inicie la aplicacion en el modo de producción (por ejemplo, ```mvn clean spring-boot:run -DskipTests -Pproduction```)

1. Abrir terminal en la raíz del proyecto

1. Inicie una prueba desde la línea de comando:

    ```sh
    mvn -Pscalability gatling:test
    ```

1. Los resultados de la prueba se almacenan en la carpeta de destino (por ejemplo, en ```target/gatling/Flow-1487784042461/index.html```)

1. De forma predeterminada, la prueba de escalabilidad inicia 10 sesiones de usuario en un intervalo de 10 ms para una repetición, todas las cuales se conectan a una aplicación que se ejecuta localmente. Estos valores predeterminados se pueden anular con las propiedades del sistema `gatling.sessionCount`, `gatling.sessionStartInterval` `gatling.sessionRepeats` y `gatling.baseUrl`. Vea una ejecución de ejemplo para 300 usuarios iniciada en 50 s:

    ```sh
    mvn -Pscalability gatling:test -Dgatling.sessionCount=300 -Dgatling.sessionStartInterval=50
    ```

Nota: si ejecuta la aplicacion con una base de datos en memoria (como H2, que es la predeterminada), lógicamente usará más memoria que cuando usa una base de datos externa (como PostgreSQL). Se recomienda ejecutar pruebas de escalabilidad para la aplicacion solo después de haberlo configurado para usar una base de datos externa.
