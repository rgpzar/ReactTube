# Implementación

## Arquitectura de base de datos
### Entidad USER (Usuario):

Representa a los usuarios del sistema.
Atributos:
- id: Identificador único del usuario (clave primaria).
- email: Correo electrónico del usuario (único).
- first_name: Nombre del usuario.
- last_name: Apellido del usuario.
- password: Contraseña del usuario.
- phone_number: Número de teléfono del usuario.
- role: Rol del usuario (puede ser 'ADMIN' o 'USER').
- username: Nombre de usuario (único).
### Entidad VIDEO (Vídeo):

Representa los vídeos subidos por los usuarios.
Atributos:
- id: Identificador único del vídeo (clave primaria).
- description: Descripción del vídeo.
- duration_in_seconds: Duración del vídeo en segundos.
- title: Título del vídeo (único).
- upload_date: Fecha de subida del vídeo.
- uploaded_by: Identificador del usuario que subió el vídeo (clave foránea).
### Entidad COMMENT (Comentario):

Representa los comentarios hechos por los usuarios en los vídeos.
Atributos:
- time: Fecha y hora del comentario (parte de la clave primaria).
- user_id: Identificador del usuario que hizo el comentario (clave foránea y parte de la clave primaria).
- video_id: Identificador del vídeo comentado (clave foránea y parte de la clave primaria).
- message: Mensaje del comentario.
- username: Nombre de usuario que hizo el comentario.
### Entidad VISIT (Visita):

Representa las visitas de los usuarios a los vídeos.
Atributos:
- time: Fecha y hora de la visita (parte de la clave primaria).
- user_id: Identificador del usuario que visitó el vídeo (clave foránea y parte de la clave primaria).
- video_id: Identificador del vídeo visitado (clave foránea y parte de la clave primaria).
#### Relaciones:

- USER a VIDEO: Un usuario puede subir varios vídeos.
- USER a COMMENT: Un usuario puede hacer varios comentarios.
- USER a VISIT: Un usuario puede realizar varias visitas a vídeos.
- VIDEO a COMMENT: Un vídeo puede tener varios comentarios.
- VIDEO a VISIT: Un vídeo puede ser visitado varias veces.

## *Arquitectura backend - Springboot*
## Controladores en Spring Boot

En Spring Boot, un controlador es una clase que maneja solicitudes HTTP. Se utiliza para dirigir el flujo de datos entre el frontend y el backend, y está marcado con anotaciones como `@RestController` y `@RequestMapping`. Los controladores en Spring Boot facilitan la creación de APIs RESTful, permitiendo manejar diferentes tipos de solicitudes y devolver respuestas adecuadas. En este caso, voy a esbozar mi controlador mas complejo, el controlador de video:

## Lógica del Controlador de Video en ReactTube

| Método | Tipo de Solicitud | Descripción |
| ------ | ----------------- | ----------- |
| `getAllVideos` | GET | Devuelve una lista de todos los videos. |
| `getVideoById` | GET | Obtiene un video específico por su ID. |
| `searchVideos` | GET | Busca videos basados en una consulta de búsqueda. |
| `watchVideo` | GET | Permite ver un video específico. |
| `sendVideoUrl` | GET | Envía la URL de un video para su visualización. |
| `uploadVideo` | POST | Sube un nuevo video a la plataforma. |
| `updateVideo` | PUT | Actualiza los detalles de un video existente. |
| `getVideoThumbnail` | GET | Obtiene la miniatura de un video. |
| `deleteVideo` | DELETE | Elimina un video específico. |
| `addComment` | POST | Añade un comentario a un video. |

Cada método del `VideoController` está diseñado para manejar una operación específica relacionada con los videos en la plataforma ReactTube, utilizando servicios inyectados para procesar la lógica de negocio.

### Código del controlador de video

```java
package com.ReactTube.backApplication.controllers;
@RestController
@RequestMapping("/video")
public class VideoController {
    private final VideoService videoService;
    private VisitService visitService;
    private final CommentService commentService;
    private final VideoFileService videoFileService;

    private final AuthenticationService authenticationService;

    private final Logger LOGGER = Logger.getLogger(VideoController.class.getName());

    public VideoController(
            @Autowired VideoService videoService,
            @Autowired VisitService visitService,
            @Autowired CommentService commentService,
            @Autowired VideoFileService videoFileService,
            @Autowired AuthenticationService authenticationService
    ) {
        this.videoService = videoService;
        this.visitService = visitService;
        this.commentService = commentService;
        this.videoFileService = videoFileService;
        this.authenticationService = authenticationService;
    }

    @GetMapping()
    public List<VideoDto> getAllVideos(){
        return videoService.getAllVideos();
    }

    @GetMapping("/{id}")
    public VideoDto getVideoById(@PathVariable("id") long id) {
        return videoService.getVideoDtoById(id);
    }

    @GetMapping("/search")
    public List<VideoDto> searchVideos(@RequestParam String searchQuery){
        return videoService.searchVideos(searchQuery);
    }

    @GetMapping(value = "watch/{id}", produces = "video/mp4")
    public Mono<Resource> watchVideo(@PathVariable("id") long id) throws NoUserAuthorizedException {
        Video video = videoService.getVideoById(id);
        return videoFileService.getVideo(video);
    }

    @GetMapping("/client/watch/{id}")
    public String sendVideoUrl (@PathVariable("id") long id) throws NoUserAuthorizedException {
        Video video = videoService.getVideoById(id);
        visitService.addVisit(video);
        return "http://localhost:8080/video/watch/" + id;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadVideo(
            @RequestParam("file") MultipartFile file,
            @RequestParam("title") String title,
            @RequestParam("description") String description
    ){

        try{
            //Use getCurrentAuthenticatedUser() to get the current logged user, there will be a logged user because of the security
            User currentUser = authenticationService.getCurrentAuthenticatedUser();
            boolean videoExists = videoService.existsByTitle(title);
            boolean videoUploadSuccess;


            if(!videoExists){
                videoFileService.uploadVideo(file, title);

                Video video = Video.builder()
                        .title(title)
                        .description(description)
                        .uploadedBy(currentUser)
                        .uploadDate(new Date())
                        .durationInSeconds(videoFileService.getVideoDurationInSeconds(title))
                        .build();

                videoUploadSuccess = videoService.saveVideo(video);
                
                if(videoUploadSuccess)
                    return new ResponseEntity<>("Video uploaded successfully", HttpStatus.OK);
            }else {
                return new ResponseEntity<>("Video already exists", HttpStatus.CONFLICT);
            }


        } catch (Exception e){
            LOGGER.warning(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @PutMapping("/{id}")
    public Boolean updateVideo(@PathVariable("id") long id, @RequestBody VideoInputDto videoInputDto) throws NoUserAuthorizedException, IOException {
        Video video = videoService.getVideoById(id);
        User authenticatedUser = authenticationService.getCurrentAuthenticatedUser();

        if(video.getUploadedBy().getId() != authenticatedUser.getId()){
            throw new NoUserAuthorizedException("User is not authorized to update this video");
        }

        videoService.updateVideo(video, videoInputDto);
        return true;
    }
    @GetMapping(value = "getVideoThumbnail/{id}", produces = "image/png")
    public Mono<Resource> getVideoThumbnail(@PathVariable("id") long id)  {
        Video video = videoService.getVideoById(id);
        return videoFileService.getVideoThumbnail(video.getTitle());
    }


    @DeleteMapping("/{id}")
    public Boolean deleteVideo(@PathVariable("id") long id) throws NoUserAuthorizedException {
        Video video = videoService.getVideoById(id);
        User authenticatedUser = authenticationService.getCurrentAuthenticatedUser();

        if(video.getUploadedBy().getId() != authenticatedUser.getId()){
            throw new NoUserAuthorizedException("User is not authorized to delete this video");
        }

        return videoService.deleteVideo(id);
    }

    @PostMapping("/{id}/addComment")
    public Comment addCommment(@PathVariable("id") long videoId, @RequestBody String comment) throws NoUserAuthorizedException {
        return commentService.addComment(videoId, comment);
    }
}
}
```
---
## Modelos en Spring Boot

En Spring Boot, los modelos representan la estructura de datos y se utilizan para mapear las tablas de la base de datos. Son esenciales para el manejo de la información en el backend y facilitan la interacción con la base de datos a través de JPA (Java Persistence API).

## Estructura del Modelo de Usuario en ReactTube

El modelo `User` en ReactTube representa a los usuarios de la plataforma. Implementa la interfaz `UserDetails` de Spring Security, lo que facilita la integración con el sistema de autenticación y autorización.

El modelo de Usuario es un componente clave en la arquitectura de ReactTube, permitiendo una gestión eficiente y segura de los usuarios y sus interacciones con la plataforma. Implementa varias abstracciones de relaciones complejas MySql entre las distintas tablas de la aplicacion.

### Propiedades del Modelo de Usuario

| Propiedad | Tipo | Descripción |
| --------- | ---- | ----------- |
| `id` | `long` | Identificador único del usuario. |
| `videoVisits` | `Set<Video>` | Conjunto de videos visitados por el usuario. |
| `videosCommented` | `List<Video>` | Lista de videos en los que el usuario ha comentado. |
| `videosUploaded` | `Set<Video>` | Conjunto de videos subidos por el usuario. |
| `username` | `String` | Nombre de usuario, utilizado para la autenticación. |
| `email` | `String` | Dirección de correo electrónico del usuario. |
| `password` | `String` | Contraseña del usuario para la autenticación. |
| `role` | `Role` | Rol del usuario en la aplicación. |
| `firstName` | `String` | Nombre del usuario (opcional). |
| `lastName` | `String` | Apellido del usuario (opcional). |
| `phoneNumber` | `String` | Número de teléfono del usuario (opcional). |

### Funcionalidad y Escalabilidad

El modelo `User` está diseñado para ser escalable y adaptable a futuras expansiones de la aplicación. Incluye campos para información personal y de contacto, que aunque no son requeridos para el registro, están preparados para una posible escalación en futuras versiones de la aplicación. Este enfoque permite una fácil adaptación a nuevas funcionalidades y requisitos.

---

## Servicios en Spring Boot

Los servicios en Spring Boot son componentes clave que contienen la lógica de negocio y las operaciones de la aplicación. Están marcados con la anotación `@Service` y suelen interactuar con los repositorios para realizar operaciones en la base de datos.

## Lógica del Servicio de Video en ReactTube

El `VideoService` maneja las operaciones relacionadas con los videos en la plataforma, incluyendo la visualización, búsqueda, actualización y eliminación de videos.

### Métodos del VideoService

| Método | Descripción |
| ------ | ----------- |
| `getAllVideos` | Devuelve una lista de todos los videos. |
| `searchVideos` | Busca videos basados en una consulta de búsqueda. |
| `getVideoDtoById` | Obtiene los detalles de un video específico por su ID. |
| `getVideoById` | Recupera un video por su ID para operaciones internas. |
| `existsByTitle` | Verifica si existe un video con un título específico. |
| `saveVideo` | Guarda un nuevo video en la base de datos. |
| `deleteVideo` | Elimina un video específico de la base de datos. |
| `updateVideo` | Actualiza los detalles de un video existente. |

```java
@Service
public class VideoService {
    private final VideoRepo videoRepo;
    private  VisitService visitService;
    private final CommentService commentService;

    private final VideoFileService videoFileService;

    private static final Logger LOGGER = LoggerFactory.getLogger(VideoService.class);

    public VideoService(
            @Autowired VideoRepo videoRepo,
            @Autowired VisitService visitService,
            @Autowired CommentService commentService,
            @Autowired VideoFileService videoFileService
    ) {
        this.videoRepo = videoRepo;
        this.visitService = visitService;
        this.commentService = commentService;
        this.videoFileService = videoFileService;
    }


    public List<VideoDto> getAllVideos(){
        List<Video> videoList = (ArrayList<Video>) videoRepo.findAll();

        return videoList.stream()
                .map(video -> getVideoDtoById(video.getId()))
                .collect(Collectors.toList());
    }

    public List<VideoDto> searchVideos(String searchQuery){
        List<Video> videoList = (ArrayList<Video>) videoRepo.findAll();

        return videoList.stream()
                .filter(video -> video.getTitle().toLowerCase().contains(searchQuery.toLowerCase()))
                .map(video -> getVideoDtoById(video.getId()))
                .collect(Collectors.toList());
    }

    @Transactional
    public VideoDto getVideoDtoById(long id) {
        Video video = videoRepo.findById(id)
                .orElseThrow(() -> new VideoNotFoundException("Video couldn't be found."));
        List<Comment> comments = commentService.getCommentByVideoId(id);
        Collections.reverse(comments);
        Set<Visit> visits = visitService.getVisitsByVideoId(id);

        UploadedByDto uploadedBy = UserMapper.INSTANCE.uploadedByDtoFromUser(video.getUploadedBy());

        return new VideoDto(video, (ArrayList<Comment>) comments, visits, uploadedBy);
    }

    @Transactional
    public Video getVideoById(long id) {
        return videoRepo.findById(id)
                .orElseThrow(() -> new VideoNotFoundException("Video couldn't be found."));
    }

    public Boolean existsByTitle(String title){
        return videoRepo.existsByTitle(title);
    }

    public Boolean saveVideo(Video video){
        boolean videoExists = existsByTitle(video.getTitle());

        if(videoExists){
            throw new VideoAlreadyExistsException("Video already exists.");
        }

        try{
            video.setUploadDate(new Date());
            videoRepo.save(video);
            return true;
        }catch (Exception e){
            LOGGER.error(e.getMessage());
        }

        return false;
    }

    public boolean deleteVideo(long videoId){
        try{
            videoRepo.deleteById(videoId);
            return true;
        }catch (Exception e){
            System.out.println(e); //log e
            return false;
        }

    }

    public void updateVideo(Video video, VideoInputDto videoInputDto) throws IOException {
        videoFileService.updateVideoTitle(video.getTitle(), videoInputDto.getTitle());
        VideoMapper.INSTANCE.updateVideoFromVideoInputDto(videoInputDto, video);
        videoRepo.save(video);
    }
}
```

## Lógica del Servicio de Autenticación en ReactTube

El `AuthenticationService` gestiona la autenticación y autorización de los usuarios, incluyendo el inicio de sesión y la generación de tokens JWT.

### Métodos del AuthenticationService

| Método | Descripción |
| ------ | ----------- |
| `getCurrentAuthenticatedUser` | Obtiene el usuario actualmente autenticado. |
| `login` | Autentica a un usuario y genera un token JWT. |
| `generateExtraClaims` | Genera reclamaciones adicionales para el token JWT. |

---
## Filtros en Spring Boot

Los filtros en Spring Boot son componentes que interceptan y procesan solicitudes HTTP antes de que lleguen a los controladores. Son esenciales para tareas como autenticación, registro y manejo de errores.

## Lógica del JwtAuthenticationFilter en ReactTube

El `JwtAuthenticationFilter` es un filtro personalizado que se ejecuta una vez por cada solicitud HTTP para manejar la autenticación basada en JWT (JSON Web Token).

### Funcionamiento del JwtAuthenticationFilter

El filtro `JwtAuthenticationFilter` extiende `OncePerRequestFilter`, asegurando que se ejecute una vez por cada solicitud entrante.

#### Método del JwtAuthenticationFilter

| Método | Descripción |
| ------ | ----------- |
| `doFilterInternal` | Intercepta la solicitud, extrae y valida el token JWT, autentica al usuario y continúa con la cadena de filtros. |

#### Proceso del Filtro

1. **Obtención del Token**: Extrae el token JWT del encabezado `Authorization` de la solicitud HTTP.
2. **Validación del Token**: Verifica si el encabezado de autorización es nulo o si no comienza con "Bearer ".
3. **Extracción del Usuario**: Utiliza el `JwtService` para extraer el nombre de usuario (subject) del token.
4. **Autenticación del Usuario**: Busca el usuario en la base de datos usando `UserRepo` y, si se encuentra, crea un `UsernamePasswordAuthenticationToken` con las autoridades del usuario.
5. **Establecimiento del Contexto de Seguridad**: Establece el objeto `Authentication` en el `SecurityContext` de Spring Security, autenticando al usuario para la solicitud actual.

#### Código del Filtro

```java
@Component
@Builder
@Getter
@AllArgsConstructor
@Component
@Builder
@Getter
@AllArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserRepo userRepo;
    public static final String AUTHORIZATION = "Authorization";
    public static final String BEARER = "Bearer ";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //Obtención del token
        String authHeader = request.getHeader(AUTHORIZATION);

        if(authHeader == null || !authHeader.startsWith(BEARER)){
            filterChain.doFilter(request, response);
            return;
        }

        String jwt = authHeader.split(" ")[1];

        //Obtencion del subject (username en este caso) del token
        String username = jwtService.extractUsername(jwt);

        //Settear un objeto Authentication (Usuario autenticado) en el SecurityContext
        User user = userRepo.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                username, null, user.getAuthorities()
        );

        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }
}
```

## DTOs (Data Transfer Objects) en ReactTube

Los DTOs (Data Transfer Objects) en ReactTube son utilizados para transferir datos entre diferentes capas de la aplicación, especialmente entre la lógica de negocio y la presentación. Permiten una comunicación eficiente y segura de los datos.

## Estructura de los DTOs en ReactTube

### AuthenticationRequest

El `AuthenticationRequest` DTO se utiliza para encapsular los datos de autenticación del usuario.

#### Propiedades del AuthenticationRequest

| Propiedad | Tipo | Descripción |
| --------- | ---- | ----------- |
| `username` | `String` | Nombre de usuario utilizado para la autenticación. |
| `password` | `String` | Contraseña del usuario para la autenticación. |

#### Código del DTO

``` java
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationRequest {
    private String username;
    private String password;
}
```

### VideoInputDto

El `VideoInputDto` DTO se utiliza para transferir datos de entrada relacionados con los videos.

#### Propiedades del VideoInputDto

| Propiedad | Tipo | Descripción |
| --------- | ---- | ----------- |
| `title` | `String` | Título del video. |
| `description` | `String` | Descripción del video. |

#### Código del DTO

``` java
@Builder
@Data
public class VideoInputDto {
    private String title;
    private String description;
}
```

---

## Manejo Global de Excepciones en ReactTube

El manejo global de excepciones en ReactTube se realiza a través del `GlobalExceptionHandler`. Este componente intercepta las excepciones lanzadas durante la ejecución de la aplicación y proporciona respuestas HTTP adecuadas.

## Funcionamiento del GlobalExceptionHandler

El `GlobalExceptionHandler` extiende `ResponseEntityExceptionHandler` y está anotado con `@ControllerAdvice` para manejar excepciones de toda la aplicación.

### Métodos del GlobalExceptionHandler

| Método | Excepción Manejada | Descripción |
| ------ | ------------------ | ----------- |
| `handleResourceNotFoundException` | `CommentNotFoundException`, `VideoNotFoundException`, `VisitNotFoundException`, `UserNotFoundException` | Devuelve una respuesta 404 cuando no se encuentra un recurso. |
| `handleNoUserAuthorizedException` | `NoUserAuthorizedException` | Devuelve una respuesta 403 cuando el usuario no tiene autorización. |
| `handleSignatureException` | `SignatureException`, `ExpiredJwtException` | Maneja errores de autenticación y devuelve una respuesta 401. |
| `handleClientAbortException` | `ClientAbortException` | Maneja excepciones de aborto del cliente, generalmente no devuelve una respuesta. |
| `handleIllegalStateException` | `IllegalStateException` | Devuelve una respuesta 400 en caso de una solicitud mal formada. |
| `handleFileNotFoundException` | `FileNotFoundException` | Devuelve una respuesta 404 cuando no se encuentra un archivo solicitado. |
| `handleGenericExceptions` | `Exception` | Maneja excepciones genéricas, devolviendo una respuesta 500 o 403 según el tipo de excepción. |

---

Este manejador global de excepciones es crucial para la robustez de ReactTube, asegurando que los errores se manejen de manera coherente y proporcionando respuestas claras y útiles a los usuarios y al sistema.

## *Arquitectura frontend - ReactJS*

## Arquitectura Frontend - React

### Enrutamiento en React con React Router

React Router es una biblioteca esencial para el enrutamiento en aplicaciones React. Facilita la definición de rutas y la renderización de componentes basados en la URL, lo que es crucial para la navegación y la estructura de la aplicación.

#### Uso de React Router en ReactTube

ReactTube utiliza React Router para gestionar la navegación entre diferentes vistas y componentes. A continuación se muestra cómo se configura el enrutamiento en ReactTube:

```jsx
export const AppRouter = () => {
    return (
        <Router>
            <Routes>
                <Route path="/home" Component={() => <Home/>}/>
                <Route path="/dashboard" Component={() => <Dashboard/>}/>
                <Route path="/logout" Component={() => <LogOut/>}/>
                <Route path="/uploadVideo" Component={() => <UploadVideoForm/>}/>
                <Route path="watch/:id" Component={() => <VideoPage/>}/>
                <Route path="edit/:id" Component={() => <VideoEditPage/>}/>
                <Route exact path="/" Component={() => <Authenticate/>}/>
            </Routes>
        </Router>
    );
}

export default AppRouter;
```
---

### Estado Global en React con Redux
El estado global en una aplicación React se refiere a un estado que está disponible en toda la aplicación y que puede ser accedido y modificado por varios componentes sin necesidad de pasar propiedades a través de múltiples niveles de jerarquía. Redux es una biblioteca muy utilizada para gestionar el estado global en aplicaciones React.

### ¿Qué es Redux?
Redux es una biblioteca de gestión de estado global para aplicaciones JavaScript, incluyendo aplicaciones React. Proporciona una forma predecible y centralizada de administrar el estado de una aplicación, lo que facilita la gestión de datos compartidos entre múltiples componentes y la manipulación del estado de forma segura.

#### Redux se basa en tres conceptos principales:

- Store: El "almacén" es un objeto que contiene todo el estado de la aplicación. Es único y accesible desde cualquier parte de la aplicación.

- Actions: Las "acciones" son objetos que describen un cambio en el estado. Son emitidas por componentes o por middleware y se envían al "almacén".

- Reducers: Los "reductores" son funciones puras que toman el estado actual y una acción, y devuelven un nuevo estado. Cada acción se procesa en un reductor, y este actualiza el estado en consecuencia.

#### Implementación de Estado Global en ReactTube
En el proyecto ReactTube, se utiliza Redux para gestionar el estado global de la autenticación del usuario. A continuación se muestra cómo se configura y utiliza Redux en el proyecto:

```jsx
import { configureStore } from "@reduxjs/toolkit";
import { actions } from "./actions.js";
import { jwtDecode } from "jwt-decode";
import thunk from "redux-thunk";
import { sessionCheckerMiddleware } from "./sessionCheckerMiddleware.js";

// Función para guardar la sesión en cookies
const saveSession = (jwt, user) => {
    // Considerar el uso de localStorage o sessionStorage
    const expirationTime = new Date();
    expirationTime.setTime(expirationTime.getTime() + 30 * 60 * 1000); // 30 minutos

    document.cookie = `jwt=Bearer ${jwt}; expires=${expirationTime.toUTCString()}; path=/`;
    document.cookie = `user=${JSON.stringify(user)}; expires=${expirationTime.toUTCString()}; path=/`;
};

// Función para obtener la sesión almacenada en cookies
const getStoredSession = () => {
    const jwtCookie = document.cookie.split('; ').find(row => row.startsWith('jwt='));
    const userCookie = document.cookie.split('; ').find(row => row.startsWith('user='));

    const jwt = jwtCookie ? jwtCookie.split('=')[1] : null;
    const user = userCookie ? JSON.parse(userCookie.split('=')[1]) : null;

    return {
        jwt,
        user
    };
};

const initialState = getStoredSession();

// Reductor para gestionar la autenticación del usuario
const loginReducer = (state = initialState, action) => {
    switch (action.type) {
        case actions.CHECK_STORAGE:
            const session = getStoredSession();
            if (!session.jwt) {
                location.replace('/');
            }
            return session.jwt ? session : state;

        case actions.STORE_NEW_SESSION:
            const jwt = action.payload.jwt;
            const decodedJwt = jwtDecode(jwt);
            const user = { ...decodedJwt };

            saveSession(jwt, user);
            return { jwt: `Bearer ${jwt}`, user };

        case actions.CLEAR_STATE:
            return null;

        default:
            return state;
    }
};

// Configuración del almacén Redux
export const configureAppStore = () => {
    return configureStore({
        reducer: loginReducer,
        middleware: (getDefaultMiddleware) => getDefaultMiddleware().concat(thunk, sessionCheckerMiddleware)
    });
};

// Selectores para acceder al estado desde componentes
export const getJwt = (state) => state?.jwt || null;
export const getUser = (state) => state?.user || null;

```


###Componentes en React
Los componentes son bloques de construcción fundamentales en una aplicación React. Son piezas reutilizables de código que encapsulan la lógica y la interfaz de usuario de una parte específica de la aplicación. Los componentes permiten dividir una aplicación en partes más pequeñas y manejables, lo que facilita el desarrollo y el mantenimiento de la aplicación.

####¿Qué son los Componentes en React?
En React, un componente es una función o una clase que devuelve elementos de React (usualmente JSX) que representan una parte de la interfaz de usuario. Los componentes pueden ser simples, como botones o encabezados, o complejos, como formularios o páginas completas.

####Implementación de Componentes en ReactTube
En el proyecto ReactTube, se utilizan varios componentes para construir la página de visualización de un video. Aquí hay una descripción de cómo se utilizan los componentes en el código proporcionado:

En el código:

```jsx

const VideoPage = () => {
  const { id } = useParams();
  const [videoUrl, setVideoUrl] = useState("");
  const [videoInfo, setVideoInfo] = useState({});
  const [newComment, setNewComment] = useState("");
  const [loadingVideoUrl, setLoadingVideoUrl] = useState(true);
  const [loadingVideoInfo, setLoadingVideoInfo] = useState(true);

  const { jwt } = useCheckSession();

  useEffect(() => {
    if (jwt) {
      setLoadingVideoUrl(true);
      fetch(`http://localhost:8080/video/client/watch/${id}`, {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
          Authorization: jwt,
        },
      })
          .then((response) => response.text())
          .then((videoUrl) => {
            setVideoUrl(videoUrl);
          })
          .catch((error) => {
            console.error("Error fetching video:", error);
          })
          .finally(() => setLoadingVideoUrl(false));
    }
  }, [id, jwt]);

  useEffect(() => {
    if (jwt && id) {
      setNewComment("");
      setLoadingVideoInfo(true);
      fetch(`http://localhost:8080/video/${id}`, {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
          Authorization: jwt,
        },
      })
          .then((response) => response.json())
          .then((data) => {
            setVideoInfo(data);
          })
          .catch((error) => {
            console.error("Error fetching additional data:", error);
          })
          .finally(() => setLoadingVideoInfo(false));
    }
  }, [id, jwt]);

  const { video, uploadedBy, videoComments, videoVisits } = videoInfo;

  if (loadingVideoUrl || loadingVideoInfo) {
    return <div>Loading...</div>;
  }

  if(newComment){
    videoComments.unshift(newComment);
  }



  return (
      <>
        <Header />
        <section className={styles.video_page_container}>
          <div className={styles.video_section}>
            <div className={styles.video_box}>
              {!loadingVideoUrl && videoUrl &&(
                  <video controls autoPlay>
                    <source src={videoUrl} type="video/mp4" />
                    Your browser does not support the video tag.
                  </video>
              )}
              <h2 id={styles["video_title"]}>{video.title}</h2>
              <div className={styles.video_info}>
                <h4>Uploaded by - {uploadedBy.username}</h4>
              </div>
              <div className={styles.video_description}>
                <p>
                  {getVideoTotalVisits(videoVisits)}  {(getVideoTotalVisits(videoVisits) > 1) || (getVideoTotalVisits(videoVisits) == 0)  ? 'visualizations' : 'visualization'}
                  - {getUploadDateFormatted(video.uploadDate)} ◷
                </p>
                {video.description}
              </div>
            </div>

            <div className={styles.comment_container}>
              <CommentInput videoId={video.id} setNewComment={setNewComment}/>

              {
                  videoComments && videoComments.map(comment => {
                    return (
                        <VideoComment key={comment.id.time} comment={comment}/>
                    );
                  })
              }
            </div>
          </div>
          <div className={styles.recommended_video_section}>
            <h2>Recommended videos</h2>
            <RecommendedVideoWrapper excludedId={video.id}/>
          </div>
        </section>
      </>
  );
};

export default VideoPage;





```

Header, RecommendedVideoWrapper, VideoComment, y CommentInput son ejemplos de componentes React utilizados en la página de visualización de video. Cada uno de estos componentes encapsula una funcionalidad específica y se reutiliza en diferentes partes de la aplicación. Los useEffect son hooks de react, que permiten ejecutar acciones de manera reactiva junto a las variables de estado, actuando los useEffect como observadores y los useState (variables de estado del componente) observables, dentro de la programacion reactiva.

---

##Arquitectura general del frontend
Puesto que la complejidad del frontend lejos de residir en los conceptos utilizados lo hace en como se usan, voy a dar una visión general sobre la estructura del proyecto:

El proyecto ReactTube utiliza una arquitectura basada en React y Redux para manejar el estado global y la interacción entre componentes. La aplicación se inicia en [`App.jsx`](https://github.com/rgpzar/ReactTube/blob/main/ReactTubeFront/src/main/App.jsx), donde se configura el store de Redux y se define el enrutador principal de la aplicación.

### Enrutamiento y Navegación

El enrutamiento se gestiona a través de `AppRouter.jsx`, que se encuentra en [`AppRouter.jsx`](https://github.com/rgpzar/ReactTube/blob/main/ReactTubeFront/src/main/AppRouter.jsx). Aquí se definen las rutas para las diferentes páginas y componentes de la aplicación, como la página de inicio, el dashboard, la autenticación, y las páginas de visualización y edición de videos.

### Estado Global y Redux

Para la gestión del estado global, se utiliza Redux. La configuración del store de Redux se realiza en [`configureAppStore.js`](https://github.com/rgpzar/ReactTube/blob/main/ReactTubeFront/src/helpers/configureAppStore.js), donde se definen los reducers y se aplica middleware para tareas como la verificación de sesión. Este store se utiliza para manejar datos como la autenticación del usuario y la información de sesión.

### Componentes y Carga de Datos

Los componentes principales de la aplicación incluyen:

*   **VideoWrapper**: En [`VideoWrapper.jsx`](https://github.com/rgpzar/ReactTube/blob/main/ReactTubeFront/src/components/VideoWrapper/VideoWrapper.jsx), se maneja la presentación de una lista de videos, utilizando el componente `VideoCard` para cada video.
*   **VideoCard**: [`VideoCard.jsx`](https://github.com/rgpzar/ReactTube/blob/main/ReactTubeFront/src/components/VideoCard/VideoCard.jsx) es un componente clave que muestra la información de cada video, como el título, la miniatura y las visitas. Utiliza Redux para acceder al estado global y cargar datos como las miniaturas de los videos.
*   **Header y Search**: El [`Header`](https://github.com/rgpzar/ReactTube/blob/main/ReactTubeFront/src/components/Header/Header.jsx) y el componente [`Search`](https://github.com/rgpzar/ReactTube/blob/main/ReactTubeFront/src/components/Search/Search.jsx) facilitan la navegación y la búsqueda dentro de la aplicación.
*   **UserInfo y CommentInput**: Componentes como [`UserInfo`](https://github.com/rgpzar/ReactTube/blob/main/ReactTubeFront/src/components/UserInfo/UserInfo.jsx) y [`CommentInput`](https://github.com/rgpzar/ReactTube/blob/main/ReactTubeFront/src/components/CommentInput/CommentInput.jsx) se utilizan para mostrar información del usuario y permitir la interacción con comentarios en los videos.

### Interacción entre Componentes

Los componentes interactúan entre sí principalmente a través del estado global gestionado por Redux y las props. Por ejemplo, `VideoWrapper` recibe una lista de videos y la pasa a `VideoCard`. `VideoCard` a su vez utiliza el estado global para cargar datos adicionales como las miniaturas.

### Carga de Datos y Middleware

La carga de datos desde el backend se realiza mediante llamadas a la API en componentes como `VideoCard` y `UserInfo`. Además, se utiliza middleware en Redux para tareas como la verificación de sesión y la gestión de estados de autenticación.