# Documentación técnica de la Práctica 2

## Introducción
Esta documentación tiene como objetivo dejar registro técnico de los cambios y funcionalidades desarrolladas durante la implementación de la Práctica 2. Está destinada a los miembros del equipo de desarrollo y pretende facilitar la comprensión de las decisiones tomadas, los componentes agregados y la estructura general del código.

## Nuevas funcionalidades implementadas

Se implementaron tres funcionalidades principales:

1. **Listado de usuarios registrados** en la ruta `/registrados`
2. **Detalle de usuario individual** en la ruta `/registrados/{id}`
3. **Integración del enlace "Usuarios Registrados" en el navbar** para facilitar la navegación interna

Todas estas funcionalidades permiten consultar información desde la base de datos y visualizarla mediante vistas Thymeleaf, manteniendo la cohesión visual del sistema.

## Nuevas clases y métodos

### Clases nuevas:
- No se añadieron clases Java nuevas, pero se extendió la funcionalidad de las siguientes existentes:
  - `UsuarioController.java`
  - `UsuarioService.java`

### Métodos nuevos:
- `UsuarioController.java`
  ```java
  @GetMapping("/registrados")
  public String listarUsuarios(Model model) { ... }

  @GetMapping("/registrados/{id}")
  public String mostrarUsuario(@PathVariable Long id, Model model) { ... }
  ```

- `UsuarioService.java`
  ```java
  public List<Usuario> findAllUsuarios();
  public Usuario findUsuarioById(Long id);
  ```

## Plantillas Thymeleaf añadidas o modificadas

- `listadoUsuarios.html`: tabla con todos los usuarios registrados
- `usuarioDetalle.html`: muestra los datos del usuario (excepto la contraseña)
- `fragments.html` (o plantilla compartida de menú): se añadió el enlace a `/registrados` si el usuario está logueado

## Ejemplo de código relevante y explicación

### Fragmento del controlador para el detalle de usuario:
```java
@GetMapping("/registrados/{id}")
public String mostrarUsuario(@PathVariable Long id, Model model) {
    Usuario usuario = usuarioService.findUsuarioById(id);
    if (usuario == null) {
        return "redirect:/registrados";
    }
    model.addAttribute("usuario", usuario);
    return "usuarioDetalle";
}
```
**Explicación:** Este método del controlador maneja la petición GET para mostrar los datos individuales de un usuario. Primero consulta si existe, y en caso contrario redirige al listado general. Esto evita errores de renderizado si el ID no es válido.

### Fragmento de código del navbar:
```html
<li><a th:href="@{/registrados}">Usuarios Registrados</a></li>
```
**Explicación:** Esta línea fue añadida en la plantilla de fragmento común (`fragments.html`) para que todos los usuarios logueados puedan acceder fácilmente al listado general de usuarios desde cualquier vista.

---

## Tests implementados

Se crearon dos clases de test de integración usando `MockMvc`:

- `UsuarioListadoWebTest.java`:
  - Verifica que `/registrados` responde con status 200 y carga el modelo "usuarios".

- `UsuarioDetalleWebTest.java`:
  - Verifica que `/registrados/{id}` carga correctamente los datos del usuario existente.
  - Verifica que un ID inexistente redirige correctamente.

Ejemplo de test:
```java
@Test
public void redirigeSiUsuarioNoExiste() throws Exception {
    mockMvc.perform(get("/registrados/99999"))
           .andExpect(status().is3xxRedirection())
           .andExpect(redirectedUrl("/registrados"));
}
```

---

## Persistencia y despliegue con Docker

- Se modificó el archivo `application.properties` para usar:
  ```properties
  spring.datasource.url=jdbc:h2:file:/data/h2/dev-db
  ```
- Se creó un `Dockerfile` para contenerizar la app.
- Se ejecuta con:
  ```bash
  docker run -d -p 8080:8080 -v $(pwd)/data:/data practica03-app
  ```
- Se subió la imagen a Docker Hub como: `jeremyyugsi5/epn-todolist-jyugsi:1.0.1`
