/* eslint-disable react/no-unescaped-entities */
import Header from "../../widgets/Header";

export const Documentation = () => {

    const current = "docs";

    return (
        <>
            <Header current={current} />
            <section>
                <h1 id="documentación-de-la-api-del-controlador-de-aeropuertos">Documentación de la API del Controlador de Aeropuertos</h1>
                <p>Este documento proporciona detalles sobre cómo acceder a las diferentes rutas proporcionadas por el controlador <code>AirportController</code> en la API de Aeropuertos.</p>
                <h2 id="descripción-general">Descripción General</h2>
                <p>El controlador <code>AirportController</code> permite realizar operaciones relacionadas con aeropuertos. La API requiere una clave de API válida para acceder a la mayoría de las rutas, a excepción de algunas rutas públicas.</p>
                <h3 id="base-url">Base URL</h3>
                <p>La URL base para acceder a la API es: <code>http://localhost:puerto/api/airport</code></p>
                <h2 id="rutas-disponibles">Rutas Disponibles</h2>
                <h3 id="obtener-todos-los-aeropuertos">Obtener todos los Aeropuertos</h3>
                <ul>
                    <li><strong>Método:</strong> GET</li>
                    <li><strong>Ruta:</strong> <code>/airport</code></li>
                    <li><strong>Permisos:</strong> Público (<code>@PreAuthorize("permitAll")</code>)</li>
                    <li><strong>Descripción:</strong> Esta ruta permite obtener una lista de todos los aeropuertos disponibles.</li>
                    <li><strong>Parámetros de consulta:</strong> Ninguno</li>
                    <li><strong>Respuesta Exitosa (Código de Estado 200):</strong> Lista de objetos Airport (Aeropuertos).</li>
                    <li><strong>Respuesta de Error (Código de Estado 403):</strong> Si la clave de API es inválida.</li>
                </ul>
                <h3 id="obtener-un-aeropuerto-por-id">Obtener un Aeropuerto por ID</h3>
                <ul>
                    <li><strong>Método:</strong> GET</li>
                    <li><strong>Ruta:</strong> <code>{"/airport/{id}"}</code></li>
                    <li><strong>Permisos:</strong> Público (<code>@PreAuthorize("permitAll")</code>)</li>
                    <li><strong>Descripción:</strong> Esta ruta permite obtener un aeropuerto por su ID.</li>
                    <li><strong>Parámetros de ruta:</strong> <code>id</code> - El ID del aeropuerto que se desea obtener.</li>
                    <li><strong>Respuesta Exitosa (Código de Estado 200):</strong> Objeto Airport (Aeropuerto) correspondiente al ID proporcionado.</li>
                    <li><strong>Respuesta de Error (Código de Estado 403):</strong> Si la clave de API es inválida.</li>
                </ul>
                <h3 id="buscar-aeropuertos-por-nombre">Buscar Aeropuertos por Nombre</h3>
                <ul>
                    <li><strong>Método:</strong> GET</li>
                    <li><strong>Ruta:</strong> <code>/airport/search</code></li>
                    <li><strong>Permisos:</strong> Público (<code>@PreAuthorize("permitAll")</code>)</li>
                    <li><strong>Descripción:</strong> Esta ruta permite buscar aeropuertos por su nombre.</li>
                    <li><strong>Parámetros de consulta:</strong> <code>name</code> - El nombre del aeropuerto que se desea buscar.</li>
                    <li><strong>Respuesta Exitosa (Código de Estado 200):</strong> Lista de objetos Airport (Aeropuertos) que coinciden con el nombre proporcionado.</li>
                    <li><strong>Respuesta de Error (Código de Estado 403):</strong> Si la clave de API es inválida.</li>
                </ul>
                <h3 id="agregar-un-nuevo-aeropuerto">Agregar un Nuevo Aeropuerto</h3>
                <ul>
                    <li><strong>Método:</strong> POST</li>
                    <li><strong>Ruta:</strong> <code>/airport</code></li>
                    <li><strong>Permisos:</strong> Editar Aeropuerto (<code>@PreAuthorize("hasAuthority('EDIT_AIRPORT')")</code>)</li>
                    <li><strong>Descripción:</strong> Esta ruta permite agregar un nuevo aeropuerto.</li>
                    <li><strong>Cuerpo de la Solicitud:</strong> Objeto Airport (Aeropuerto) en formato JSON.</li>
                    <li><strong>Respuesta Exitosa (Código de Estado 200):</strong> Objeto Airport (Aeropuerto) recién creado.</li>
                    <li><strong>Respuesta de Error (Código de Estado 403):</strong> Si la clave de API es inválida o si el usuario no tiene permiso para editar aeropuertos.</li>
                </ul>
                <h3 id="actualizar-un-aeropuerto-por-id">Actualizar un Aeropuerto por ID</h3>
                <ul>
                    <li><strong>Método:</strong> PUT</li>
                    <li><strong>Ruta:</strong> <code>{"/airport/{id}"}</code></li>
                    <li><strong>Permisos:</strong> Editar Aeropuerto (<code>@PreAuthorize("hasAuthority('EDIT_AIRPORT')")</code>)</li>
                    <li><strong>Descripción:</strong> Esta ruta permite actualizar un aeropuerto existente por su ID.</li>
                    <li><strong>Parámetros de ruta:</strong> <code>id</code> - El ID del aeropuerto que se desea actualizar.</li>
                    <li><strong>Cuerpo de la Solicitud:</strong> Objeto Airport (Aeropuerto) actualizado en formato JSON.</li>
                    <li><strong>Respuesta Exitosa (Código de Estado 200):</strong> Mensaje de confirmación.</li>
                    <li><strong>Respuesta de Error (Código de Estado 403):</strong> Si la clave de API es inválida o si el usuario no tiene permiso para editar aeropuertos.</li>
                </ul>
                <h3 id="eliminar-un-aeropuerto-por-id">Eliminar un Aeropuerto por ID</h3>
                <ul>
                    <li><strong>Método:</strong> DELETE</li>
                    <li><strong>Ruta:</strong> <code>{"/airport/{id}"}</code></li>
                    <li><strong>Permisos:</strong> Eliminar Aeropuerto (<code>@PreAuthorize("hasAuthority('DELETE_AIRPORT')")</code>)</li>
                    <li><strong>Descripción:</strong> Esta ruta permite eliminar un aeropuerto por su ID.</li>
                    <li><strong>Parámetros de ruta:</strong> <code>id</code> - El ID del aeropuerto que se desea eliminar.</li>
                    <li><strong>Respuesta Exitosa (Código de Estado 200):</strong> Mensaje de confirmación.</li>
                    <li><strong>Respuesta de Error (Código de Estado 403):</strong> Si la clave de API es inválida o si el usuario no tiene permiso para eliminar aeropuertos.</li>
                </ul>
                <h2 id="gestión-de-errores">Gestión de Errores</h2>
                <p>En caso de producirse un error, la API proporcionará una respuesta con un código de estado adecuado y un mensaje de error descriptivo.</p>
                <h3 id="ejemplo-de-respuesta-de-error">Ejemplo de Respuesta de Error</h3>
                <pre>
                    <code>


                        {JSON.stringify({
                            message: "API Key inválida",
                            timestamp: "Fri Oct 01 2023 14:30:00 GMT-0700 (Hora estándar del Pacífico)",
                            url: "http://localhost:puerto/api/airport",
                            httpMethod: "GET"
                        })}

                    </code>
                </pre>

            </section>
        </>
    );
}

export default Documentation;