package com.dsi.insibo.sice.Seguridad;

import java.util.ArrayList;
import java.util.List;
import java.security.SecureRandom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.dsi.insibo.sice.entity.Usuario;

@Controller
public class gestionarRechazados {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/gestionarRechazados")
    public String cargarRechazados(Model model, @RequestParam(required = false, defaultValue = "1") int pagina) {
        pagina=(pagina-1);
        List<Usuario> listadoUsuarios = usuarioService.listaUsuariosRechazadosIntervalos(pagina);

        List<UsuarioConNombre> listadoCompleto =new ArrayList<>();
        //Obtenemos los nombres
        for (Usuario usuario : listadoUsuarios) {
            String rol = usuario.getRolUsuario();
            String nombre="";

            if(rol.equals("Administrador")){
                nombre = "Administrador";
            }
            if(rol.equals("Docente")){

                nombre = usuario.getDocente().getNombreDocente() + " " + usuario.getDocente().getApellidoDocente() ;
            }
            if(rol.equals("Personal Administrativo")){
                nombre = usuario.getPersonalAdministrativo().getNombrePersonal()+ " " + usuario.getDocente().getApellidoDocente();
            }

            listadoCompleto.add(new UsuarioConNombre(usuario, nombre));
        }

        model.addAttribute("Usuarios", listadoCompleto);

       // Obtenemos la cantidad de usuarios que tenemos
        List<Usuario> totalUsuario = usuarioService.listaUsuariosRechazados();
        int cantidad = (int) Math.ceil((double) totalUsuario.size() / 7);
        model.addAttribute("Cantidad", cantidad);
    
        return "/seguridad/gestionarRechazados";
    }

    @GetMapping("/aceptarUsuarioRechazado/{id}")
    public String aceptarUsuarioRechazado(@PathVariable("id") int idUsuario, RedirectAttributes attribute) {
        Usuario usuario = usuarioService.buscarPorIdUsuario(idUsuario);
        String contrasena = generateRandomPassword(8);
        usuario.setEstadoUsuario("Activo");
        usuario.setContrasenaUsuario(contrasena);
        usuarioService.guardarUsuario(usuario);
        return "redirect:/gestionarRechazados";
    }


    //----------------------------------------------------------------
    //         ALGORITMO GENERADOR DE CONTRASEÑA ALEATORIA
    //----------------------------------------------------------------
        // Definimos los caracteres que queremos que se incluyan en la contraseña
        private static final String UPPERCASE_LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        private static final String LOWERCASE_LETTERS = "abcdefghijklmnopqrstuvwxyz";
        private static final String NUMBERS = "0123456789";

        public static String generateRandomPassword(int length) {
        // Concatenamos todos los caracteres posibles
        String allCharacters = UPPERCASE_LETTERS + LOWERCASE_LETTERS + NUMBERS;
        SecureRandom random = new SecureRandom();

        StringBuilder password = new StringBuilder(length);

        // Aseguramos que la contraseña contenga al menos un carácter de cada tipo
        password.append(UPPERCASE_LETTERS.charAt(random.nextInt(UPPERCASE_LETTERS.length())));
        password.append(LOWERCASE_LETTERS.charAt(random.nextInt(LOWERCASE_LETTERS.length())));
        password.append(NUMBERS.charAt(random.nextInt(NUMBERS.length())));

        // Rellenamos el resto de la contraseña con caracteres aleatorios
        for (int i = 4; i < length; i++) {
            password.append(allCharacters.charAt(random.nextInt(allCharacters.length())));
        }

        // Mezclamos los caracteres para que no sigan un patrón fijo
        char[] passwordArray = password.toString().toCharArray();
        for (int i = 0; i < passwordArray.length; i++) {
            int randomIndex = random.nextInt(passwordArray.length);
            char temp = passwordArray[i];
            passwordArray[i] = passwordArray[randomIndex];
            passwordArray[randomIndex] = temp;
        }

        return new String(passwordArray);
    }

    //----------------------------------------------------------------
}
