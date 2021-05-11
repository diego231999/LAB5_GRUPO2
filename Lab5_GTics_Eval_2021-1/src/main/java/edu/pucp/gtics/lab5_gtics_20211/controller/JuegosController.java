package edu.pucp.gtics.lab5_gtics_20211.controller;

import edu.pucp.gtics.lab5_gtics_20211.entity.Juegos;
import edu.pucp.gtics.lab5_gtics_20211.entity.Plataformas;
import edu.pucp.gtics.lab5_gtics_20211.entity.User;
import edu.pucp.gtics.lab5_gtics_20211.repository.JuegosRepository;
import edu.pucp.gtics.lab5_gtics_20211.repository.PlataformasRepository;
import edu.pucp.gtics.lab5_gtics_20211.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.net.JarURLConnection;
import java.util.List;
import java.util.Optional;

@Controller
public class JuegosController {
    @Autowired
    UserRepository userRepository;

    @Autowired
    JuegosRepository juegosRepository;
    @Autowired
    PlataformasRepository plataformasRepository;


    @GetMapping(value = {"", "/", "/vista"})
    public String VistaJuegos (Model model){
        model.addAttribute("listaJuegos",juegosRepository.findAll(Sort.by("nombre")));
        return "/juegos/vista";
    }

    @GetMapping(value = {"/juegos/lista","/juegos","/juegos/"})
    public String ListaJuegos (Model model){
        model.addAttribute("listaJuegos",juegosRepository.findAll(Sort.by("precio")));
        return"/juegos/lista";
    }

    @GetMapping("/juegos/nuevo")
    public String nuevoJuegos(Model model, @ModelAttribute("juego") Juegos juego){

        model.addAttribute("listaPlataforma",plataformasRepository.findAll());
        return "/juegos/editarFrm";
    }

    @GetMapping("/juegos/editar")
    public String editarJuegos(@RequestParam("id") int id, Model model, @ModelAttribute("juego") Juegos juego){
        Optional<Juegos> juegosOpt = juegosRepository.findById(id);

        if(juegosOpt.isPresent()){
            juego = juegosOpt.get();
            model.addAttribute("juego",juego);
            model.addAttribute("listaPlataforma",plataformasRepository.findAll());
            return "/juegos/editarFrm";
        }else{
            return "redirect:/juegos/lista";
        }
    }

    @PostMapping("/juegos/guardar")
    public String guardarJuegos(Model model, RedirectAttributes attr, @ModelAttribute("juego") @Valid Juegos juego, BindingResult bindingResult ){

        if(bindingResult.hasFieldErrors() || juego.getPlataforma().getIdplataforma() == -1){
            model.addAttribute("msg","Ingrese una plataforma");
            model.addAttribute("listaPlataforma",plataformasRepository.findAll());
            return "/juegos/editarFrm";
        }

        if(juego.getIdjuego() == 0){
            attr.addFlashAttribute("msgCreate","Juego creado exitosamente");
        }else{
            attr.addFlashAttribute("msgEdit","Juego editado exitosamente");
        }
        juegosRepository.save(juego);
        return "redirect:/juegos/lista";
    }

    @GetMapping("/juegos/borrar")
    public String borrarDistribuidora(@RequestParam("id") int id, RedirectAttributes attr){
        Optional<Juegos> opt = juegosRepository.findById(id);
        if (opt.isPresent()) {
            juegosRepository.deleteById(id);
        }
        attr.addFlashAttribute("msgDelete","Juego borrado exitosamente");
        return "redirect:/juegos/lista";
    }
    @GetMapping("/juegos/porusuario")
    public String juegosXusuario(Model model, HttpSession session) {
        model.addAttribute("listaJuegosXusuario", juegosRepository.obtenerJuegosPorUser(1));
        return "juegos/comprado";
    }
}
