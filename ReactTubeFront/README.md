# Dashboard
En la ruta *https://localhost:5173/dashboard*
puedes obtener los datos de tu usuario y tu API Key.

# Documentación de la API del Controlador de Aeropuertos

Este documento proporciona detalles sobre cómo acceder a las diferentes rutas proporcionadas por el controlador `AirportController` en la API de Aeropuertos.

## Descripción General
El controlador `AirportController` permite realizar operaciones relacionadas con aeropuertos. La API requiere una clave de API válida para acceder a la mayoría de las rutas, a excepción de algunas rutas públicas.

### Base URL
La URL base para acceder a la API es: `http://localhost:puerto/api/airport`

## Rutas Disponibles

### Obtener todos los Aeropuertos
- **Método:** GET
- **Ruta:** `/airport`
- **Permisos:** Público (`@PreAuthorize("permitAll")`)
- **Descripción:** Esta ruta permite obtener una lista de todos los aeropuertos disponibles.
- **Parámetros de consulta:** Ninguno
- **Respuesta Exitosa (Código de Estado 200):** Lista de objetos Airport (Aeropuertos).
- **Respuesta de Error (Código de Estado 403):** Si la clave de API es inválida.

### Obtener un Aeropuerto por ID
- **Método:** GET
- **Ruta:** `/airport/{id}`
- **Permisos:** Público (`@PreAuthorize("permitAll")`)
- **Descripción:** Esta ruta permite obtener un aeropuerto por su ID.
- **Parámetros de ruta:** `id` - El ID del aeropuerto que se desea obtener.
- **Respuesta Exitosa (Código de Estado 200):** Objeto Airport (Aeropuerto) correspondiente al ID proporcionado.
- **Respuesta de Error (Código de Estado 403):** Si la clave de API es inválida.

### Buscar Aeropuertos por Nombre
- **Método:** GET
- **Ruta:** `/airport/search`
- **Permisos:** Público (`@PreAuthorize("permitAll")`)
- **Descripción:** Esta ruta permite buscar aeropuertos por su nombre.
- **Parámetros de consulta:** `name` - El nombre del aeropuerto que se desea buscar.
- **Respuesta Exitosa (Código de Estado 200):** Lista de objetos Airport (Aeropuertos) que coinciden con el nombre proporcionado.
- **Respuesta de Error (Código de Estado 403):** Si la clave de API es inválida.

### Agregar un Nuevo Aeropuerto
- **Método:** POST
- **Ruta:** `/airport`
- **Permisos:** Editar Aeropuerto (`@PreAuthorize("hasAuthority('EDIT_AIRPORT')")`)
- **Descripción:** Esta ruta permite agregar un nuevo aeropuerto.
- **Cuerpo de la Solicitud:** Objeto Airport (Aeropuerto) en formato JSON.
- **Respuesta Exitosa (Código de Estado 200):** Objeto Airport (Aeropuerto) recién creado.
- **Respuesta de Error (Código de Estado 403):** Si la clave de API es inválida o si el usuario no tiene permiso para editar aeropuertos.

### Actualizar un Aeropuerto por ID
- **Método:** PUT
- **Ruta:** `/airport/{id}`
- **Permisos:** Editar Aeropuerto (`@PreAuthorize("hasAuthority('EDIT_AIRPORT')")`)
- **Descripción:** Esta ruta permite actualizar un aeropuerto existente por su ID.
- **Parámetros de ruta:** `id` - El ID del aeropuerto que se desea actualizar.
- **Cuerpo de la Solicitud:** Objeto Airport (Aeropuerto) actualizado en formato JSON.
- **Respuesta Exitosa (Código de Estado 200):** Mensaje de confirmación.
- **Respuesta de Error (Código de Estado 403):** Si la clave de API es inválida o si el usuario no tiene permiso para editar aeropuertos.

### Eliminar un Aeropuerto por ID
- **Método:** DELETE
- **Ruta:** `/airport/{id}`
- **Permisos:** Eliminar Aeropuerto (`@PreAuthorize("hasAuthority('DELETE_AIRPORT')")`)
- **Descripción:** Esta ruta permite eliminar un aeropuerto por su ID.
- **Parámetros de ruta:** `id` - El ID del aeropuerto que se desea eliminar.
- **Respuesta Exitosa (Código de Estado 200):** Mensaje de confirmación.
- **Respuesta de Error (Código de Estado 403):** Si la clave de API es inválida o si el usuario no tiene permiso para eliminar aeropuertos.

## Gestión de Errores
En caso de producirse un error, la API proporcionará una respuesta con un código de estado adecuado y un mensaje de error descriptivo.

# Documentación del Controlador de Usuarios

Este documento proporciona detalles sobre cómo acceder a las diferentes rutas proporcionadas por el controlador `UserController` en el sistema de usuarios.

## Descripción General
El controlador `UserController` se encarga de gestionar usuarios en el sistema. Proporciona rutas para obtener información sobre usuarios, crear nuevos usuarios, actualizar información de usuarios existentes y eliminar usuarios.

### Base URL
La URL base para acceder a la API es: `http://localhost:puerto/api/user`

## Rutas Disponibles

### Obtener Todos los Usuarios
- **Método:** GET
- **Ruta:** `/user`
- **Permisos:** Admin (`@PreAuthorize("hasAuthority('ROLE_ADMIN')")`)
- **Descripción:** Esta ruta permite obtener una lista de todos los usuarios registrados en el sistema.
- **Respuesta Exitosa (Código de Estado 200):** Lista de objetos User (Usuarios).

### Verificar si un Usuario Existe por Correo Electrónico
- **Método:** GET
- **Ruta:** `/user/search`
- **Permisos:** Admin (`@PreAuthorize("hasAuthority('ROLE_ADMIN')")`)
- **Descripción:** Esta ruta permite verificar si un usuario con el correo electrónico proporcionado ya existe en el sistema.
- **Parámetros de consulta:** `email` - El correo electrónico del usuario que se desea verificar.
- **Respuesta Exitosa (Código de Estado 200):** `true` si el usuario existe, `false` si no existe.

### Obtener un Usuario por ID
- **Método:** GET
- **Ruta:** `/user/{id}`
- **Permisos:** Admin (`@PreAuthorize("hasAuthority('ROLE_ADMIN')")`)
- **Descripción:** Esta ruta permite obtener información detallada de un usuario por su ID.
- **Parámetros de ruta:** `id` - El ID del usuario que se desea obtener.
- **Respuesta Exitosa (Código de Estado 200):** Objeto User (Usuario) correspondiente al ID proporcionado.

### Crear un Nuevo Usuario
- **Método:** POST
- **Ruta:** `/user`
- **Permisos:** Ninguno (público)
- **Descripción:** Esta ruta permite crear un nuevo usuario en el sistema.
- **Cuerpo de la Solicitud:** Objeto User (Usuario) en formato JSON.
- **Respuesta Exitosa (Código de Estado 200):** Objeto User (Usuario) recién creado.

### Actualizar un Usuario por ID
- **Método:** PUT
- **Ruta:** `/user/{id}`
- **Permisos:** Admin (`@PreAuthorize("hasAuthority('ROLE_ADMIN')")`)
- **Descripción:** Esta ruta permite actualizar la información de un usuario existente por su ID.
- **Parámetros de ruta:** `id` - El ID del usuario que se desea actualizar.
- **Cuerpo de la Solicitud:** Objeto User (Usuario) actualizado en formato JSON.
- **Respuesta Exitosa (Código de Estado 200):** Objeto User (Usuario) actualizado.

### Eliminar un Usuario por ID
- **Método:** DELETE
- **Ruta:** `/user/{id}`
- **Permisos:** Admin (`@PreAuthorize("hasAuthority('ROLE_ADMIN')")`)
- **Descripción:** Esta ruta permite eliminar un usuario del sistema por su ID.
- **Parámetros de ruta:** `id` - El ID del usuario que se desea eliminar.
- **Respuesta Exitosa (Código de Estado 200):** `true` si el usuario se eliminó correctamente, `false` si no se pudo eliminar.

Esta documentación describe las rutas y los permisos disponibles en el controlador `UserController`. Asegúrate de seguir los permisos y las restricciones adecuadas al utilizar estas rutas en tu sistema de usuarios.



#   a i r p o r t A P I F r o n t  
 