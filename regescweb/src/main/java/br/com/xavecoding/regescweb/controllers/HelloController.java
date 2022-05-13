package br.com.xavecoding.regescweb.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
public class HelloController {

    @GetMapping("/hello-servlet")
    public String hello(HttpServletRequest request){
        request.setAttribute("nome","Bruna");
        return "hello";
    }

    @GetMapping("/hello-model")
    public String hello(Model model){
        model.addAttribute("nome","Bê");
        return "hello";
    }
    @GetMapping("/hello")
    public ModelAndView hello(){
        //modelo associado a uma view, assim recebe o arquivo a ser renderizado camo parâmetro no construtor
        ModelAndView mv = new ModelAndView("hello");
        mv.addObject("nome", "Pires");
        return mv;
    }

}
