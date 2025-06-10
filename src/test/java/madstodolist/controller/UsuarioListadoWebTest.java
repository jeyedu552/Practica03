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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UsuarioListadoWebTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @BeforeEach
    @Transactional
    public void setup() {
        Usuario usuario = new Usuario();
        usuario.setEmail("listado@correo.com");
        usuario.setNombre("Usuario Listado");
        usuario.setFechaNacimiento(new Date());
        usuarioRepository.save(usuario);
    }

    @Test
    public void muestraVistaListadoConUsuarios() throws Exception {
        mockMvc.perform(get("/registrados"))
               .andExpect(status().isOk())
               .andExpect(view().name("listadoUsuarios"))
               .andExpect(model().attributeExists("usuarios"));
    }
}
