package madstodolist.controller;

import madstodolist.model.Usuario;
import madstodolist.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UsuarioDetalleWebTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UsuarioRepository usuarioRepository;

    private Usuario usuario;

    @BeforeEach
    @Transactional
    public void setup() {
        usuario = new Usuario();
        usuario.setEmail("detalle@correo.com");
        usuario.setNombre("Usuario Detalle");
        usuario.setFechaNacimiento(new Date());
        usuarioRepository.save(usuario);
    }

    @Test
    public void muestraVistaDetalleConDatosDelUsuario() throws Exception {
        mockMvc.perform(get("/registrados/" + usuario.getId()))
               .andExpect(status().isOk())
               .andExpect(view().name("usuarioDetalle"))
               .andExpect(model().attributeExists("usuario"))
               .andExpect(model().attribute("usuario", hasProperty("email", is("detalle@correo.com"))))
               .andExpect(model().attribute("usuario", hasProperty("nombre", is("Usuario Detalle"))));
    }

    @Test
    public void redirigeSiUsuarioNoExiste() throws Exception {
    Long idInexistente = -999L; // ID negativo que nunca existirá
    mockMvc.perform(get("/registrados/" + idInexistente))
           .andExpect(status().is3xxRedirection())
           .andExpect(redirectedUrl("/registrados"));
    }   
}
