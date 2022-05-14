package br.com.xavecoding.regescweb.controllers;

import br.com.xavecoding.regescweb.dto.RequisicaoNovoProfessor;
import br.com.xavecoding.regescweb.models.Professor;
import br.com.xavecoding.regescweb.models.StatusProfessor;
import br.com.xavecoding.regescweb.repositories.ProfessorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;

@Controller
public class ProfessorController {
    @Autowired
    private ProfessorRepository professorRepository;

    @GetMapping("/professores")
    public ModelAndView index(){
        List<Professor> professores = this.professorRepository.findAll();
        ModelAndView mv = new ModelAndView("professores/index");
        mv.addObject("professores", professores);
        return mv;
    }

    @GetMapping("/professores/new")
    public ModelAndView nnew(RequisicaoNovoProfessor requisicaoNovoProfessor){
        //Originalmente nos vídeos o objeto requisicaoNovoProfessor não é passado como parâmetro
        //Contudo, sem isso há erros na geração do template da página professores/new
        ModelAndView mv = new ModelAndView("professores/new");
        mv.addObject("listaStatusProfessor", StatusProfessor.values());
        return mv;
    }

    @PostMapping("/professores")
    public ModelAndView create(@Valid RequisicaoNovoProfessor requisicaoNovoProfessor, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            ModelAndView mv = new ModelAndView("/professores/new");
            mv.addObject("listaStatusProfessor", StatusProfessor.values());
            return mv;
        }
        else{
            Professor professor= requisicaoNovoProfessor.toProfessor();
            this.professorRepository.save(professor);
            return new ModelAndView("redirect:/professores");
        }
    }
}
