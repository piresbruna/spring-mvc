package br.com.xavecoding.regescweb.controllers;

import br.com.xavecoding.regescweb.dto.RequisicaoFormProfessor;
import br.com.xavecoding.regescweb.models.Professor;
import br.com.xavecoding.regescweb.models.StatusProfessor;
import br.com.xavecoding.regescweb.repositories.ProfessorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/professores")
public class ProfessorController {
    @Autowired
    private ProfessorRepository professorRepository;

    @GetMapping("")
    public ModelAndView index(){
        List<Professor> professores = this.professorRepository.findAll();
        ModelAndView mv = new ModelAndView("professores/index");
        mv.addObject("professores", professores);
        return mv;
    }

    @GetMapping("/new")
    public ModelAndView nnew(RequisicaoFormProfessor requisicaoFormProfessor){
        ModelAndView mv = new ModelAndView("professores/new");
        mv.addObject("listaStatusProfessor", StatusProfessor.values());
        return mv;
    }

    @PostMapping("")
    public ModelAndView create(@Valid RequisicaoFormProfessor requisicaoFormProfessor, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            ModelAndView mv = new ModelAndView("/professores/new");
            mv.addObject("listaStatusProfessor", StatusProfessor.values());
            return mv;
        }
        else{
            Professor professor= requisicaoFormProfessor.toProfessor();
            this.professorRepository.save(professor);
            return new ModelAndView("redirect:/professores/" + professor.getId());
        }
    }
    @GetMapping("/{id}")
    public  ModelAndView show(@PathVariable Long id){
        Optional<Professor> optional = this.professorRepository.findById(id);
        if(optional.isPresent()){
            Professor professor = optional.get();

            ModelAndView mv = new ModelAndView("professores/show");
            mv.addObject("professor", professor);

            return mv;
        }else {
            //n??o achou um professor com o id especificado
            return this.retornaErroProfessor("SHOW ERROR: Professor #" + id + " n??o encontrado!");
        }
    }
    @GetMapping("/{id}/edit")
    public ModelAndView edit(@PathVariable Long id, RequisicaoFormProfessor requisicaoFormProfessor){
        Optional<Professor> optional = this.professorRepository.findById(id);

        if(optional.isPresent()){
            Professor professor = optional.get();
            requisicaoFormProfessor.fromProfessor(professor);

            ModelAndView mv =new ModelAndView("professores/edit");
            mv.addObject("professorId", professor.getId());
            mv.addObject("listaStatusProfessor", StatusProfessor.values());

            return mv;
        }else {
            //n??o achou um professor com o id especificado
            return this.retornaErroProfessor("EDIT ERROR: Professor #" + id + " n??o encontrado!");
        }

    }
    @PostMapping("/{id}")
    public ModelAndView update(@PathVariable Long id, @Valid RequisicaoFormProfessor requisicaoFormProfessor, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            ModelAndView mv = new ModelAndView("/professores/edit");
            mv.addObject("listaStatusProfessor", StatusProfessor.values());
            mv.addObject("professorId", id);
            return mv;
        }
        else{
            Optional<Professor> optional = this.professorRepository.findById(id);
            if(optional.isPresent()){
                Professor professor = requisicaoFormProfessor.toProfessor(optional.get());
                this.professorRepository.save(professor);
                return new ModelAndView("redirect:/professores/" + professor.getId());
            }else {
                //n??o achou um professor com o id especificado
                return this.retornaErroProfessor("UPDATE ERROR: Professor #" + id + " n??o encontrado!");
            }
        }
    }
    @GetMapping("/{id}/delete")
    public ModelAndView delete(@PathVariable Long id){
        ModelAndView mv = new ModelAndView("redirect:/professores");

        try{
            this.professorRepository.deleteById(id);
            mv.addObject("mensagem", "Professor #" + id + " deletado com sucesso!");
            mv.addObject("erro", false);
        }catch(EmptyResultDataAccessException e){
            System.out.println(e);
            mv = this.retornaErroProfessor("DELETE ERROR: Professor #" + id + " n??o encontrado!");
        }
        return mv;
    }

    private ModelAndView retornaErroProfessor(String msg){
        ModelAndView mv = new ModelAndView("redirect:/professores");
        mv.addObject("mensagem", msg);
        mv.addObject("erro", true);
        return mv;
    }
}
