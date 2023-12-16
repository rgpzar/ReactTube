# Anexos: Descripción Detallada de Dependencias en ReactTube
## Backend (Java con Spring Boot)
- Spring Boot Starter Web: Utilizada para construir la API REST del proyecto, manejar solicitudes HTTP y configurar el servidor embebido Tomcat. Facilita la creación de controladores y la gestión de rutas, siendo esencial para la interacción entre el frontend y el backend.

- Spring Boot Starter Data JPA: Empleada para interactuar con la base de datos MySQL, facilitando operaciones CRUD a través de repositorios JPA. Permite una integración sencilla y eficiente con la base de datos, mejorando la gestión de entidades y relaciones.

- Spring Boot Starter Security: Proporciona autenticación y autorización en la aplicación, posiblemente implementando seguridad basada en roles y protegiendo rutas específicas. Esencial para la seguridad de la API y la gestión de accesos.

- Spring Security Test: Ofrece soporte para pruebas con Spring Security, permitiendo validar la configuración de seguridad y las reglas de acceso en diferentes escenarios.

- Spring Boot Devtools: Herramientas para mejorar la experiencia de desarrollo, como reinicio automático y configuraciones de desarrollo optimizadas.

- MySQL Connector Java: Conector JDBC para MySQL, utilizado para conectar la aplicación con bases de datos MySQL, facilitando las operaciones de base de datos.

- JSON Web Token (JWT) Dependencies: Conjunto de dependencias para crear y verificar tokens JWT, usados para la autenticación y autorización, asegurando que los usuarios estén correctamente identificados y autorizados.

- Spring WebFlux: Utilizado para construir aplicaciones reactivas con Spring Framework, posiblemente para manejar solicitudes asíncronas y mejorar el rendimiento.

- MapStruct: Facilita la implementación de mapeos entre objetos Java, especialmente útil para convertir entidades de base de datos en DTOs, mejorando la separación entre la capa de persistencia y la lógica de negocio.

- Lombok: Reduce la cantidad de código repetitivo, como getters, setters y constructores, mejorando la legibilidad y mantenimiento del código.

- JCodec y JCodec JavaSE: Bibliotecas para trabajar con archivos de video y audio, posiblemente utilizadas para procesar o manipular contenido multimedia en la aplicación.

- ISO Parser: Utilizado para el procesamiento de archivos multimedia, especialmente formatos como MP4, facilitando la gestión de contenido de video.

- Bucket4j: Implementa el algoritmo de token bucket, útil para limitar la tasa de uso de ciertos recursos, como la prevención de abuso en la API.

## Frontend (React con Vite)
- Redux Toolkit: Simplifica la configuración y uso de Redux en aplicaciones React, mejorando la gestión del estado global y facilitando el manejo de estados complejos en la interfaz de usuario.

- React Router: Gestiona la navegación y el enrutamiento en aplicaciones React, esencial para la creación de una aplicación de página única (SPA) con múltiples vistas y rutas.

- React Toastify: Proporciona notificaciones ligeras y personalizables, mejorando la experiencia del usuario al mostrar mensajes de éxito, error o información.

- React Hook Form: Facilita la gestión de formularios en React, mejorando la recolección y validación de datos de entrada del usuario.

- React Icons: Ofrece una amplia gama de iconos para usar en la interfaz de usuario, mejorando la estética y la usabilidad.

- React Dropzone: Permite arrastrar y soltar archivos en la interfaz de usuario, facilitando la carga de archivos como videos.

- SweetAlert2 y SweetAlert2 React Content: Bibliotecas para crear alertas y diálogos modales atractivos, mejorando la interacción con el usuario en situaciones como confirmaciones y advertencias.

- Vite Plugin SVGR: Permite importar y usar SVGs como componentes React, mejorando la integración y manejo de gráficos vectoriales.

- Date-fns: Biblioteca para manipular y formatear fechas, utilizada para mostrar fechas y horas de manera legible y en diferentes formatos.

- JWT Decode: Decodifica tokens JWT en el lado del cliente, útil para extraer información del usuario autenticado.

- ESLint y sus plugins: Herramientas para asegurar la calidad y consistencia del código JavaScript, mejorando las prácticas de desarrollo y manteniendo un código limpio y ordenado.

- Jest: Marco de pruebas para JavaScript, utilizado para escribir y ejecutar pruebas unitarias y de integración, asegurando la calidad y fiabilidad del código.