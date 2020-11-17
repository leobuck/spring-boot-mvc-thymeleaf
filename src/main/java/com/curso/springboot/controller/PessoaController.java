package com.curso.springboot.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.curso.springboot.model.Pessoa;
import com.curso.springboot.model.Telefone;
import com.curso.springboot.repository.PessoaRepository;
import com.curso.springboot.repository.TelefoneRepository;

@Controller
public class PessoaController {

	@Autowired
	private PessoaRepository pessoaRepository;
	
	@Autowired
	private TelefoneRepository telefoneRepository;
	
	@RequestMapping(method = RequestMethod.GET, value = "/cadastropessoa")
	public ModelAndView index() {
		ModelAndView mv = new ModelAndView("cadastro/cadastropessoa");
		Iterable<Pessoa> pessoas = pessoaRepository.findAll();
		mv.addObject("pessoas", pessoas);
		mv.addObject("pessoa", new Pessoa());
		return mv;
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "**/salvarpessoa")
	public ModelAndView salvar(@Valid Pessoa pessoa, BindingResult bindingResult) {
		
		if (bindingResult.hasErrors()) {
			ModelAndView mv = new ModelAndView("cadastro/cadastropessoa");
			Iterable<Pessoa> pessoas = pessoaRepository.findAll();
			mv.addObject("pessoas", pessoas);
			mv.addObject("pessoa", pessoa);
		
			List<String> msg = new ArrayList<>();
			for (ObjectError error : bindingResult.getAllErrors()) {
				msg.add(error.getDefaultMessage());
			}
			
			mv.addObject("msg", msg);
			return mv;
		}
		
		pessoaRepository.save(pessoa);
		
		ModelAndView mv = new ModelAndView("cadastro/cadastropessoa");
		Iterable<Pessoa> pessoas = pessoaRepository.findAll();
		mv.addObject("pessoas", pessoas);
		mv.addObject("pessoa", new Pessoa());
		return mv;
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/listarpessoas")
	public ModelAndView pessoas() {
		ModelAndView mv = new ModelAndView("cadastro/cadastropessoa");
		Iterable<Pessoa> pessoas = pessoaRepository.findAll();
		mv.addObject("pessoas", pessoas);
		mv.addObject("pessoa", new Pessoa());
		return mv;
	}
	
	@GetMapping("/editarpessoa/{idpessoa}")
	public ModelAndView editar(@PathVariable("idpessoa") Long idpessoa) {
		ModelAndView mv = new ModelAndView("cadastro/cadastropessoa");
		Optional<Pessoa> pessoa = pessoaRepository.findById(idpessoa);
		mv.addObject("pessoa", pessoa.get());
		return mv;
	}
	
	@GetMapping("/removerpessoa/{idpessoa}")
	public ModelAndView remover(@PathVariable("idpessoa") Long idpessoa) {
		pessoaRepository.deleteById(idpessoa);
		
		ModelAndView mv = new ModelAndView("cadastro/cadastropessoa");
		Iterable<Pessoa> pessoas = pessoaRepository.findAll();
		mv.addObject("pessoas", pessoas);
		mv.addObject("pessoa", new Pessoa());
		return mv;
	}
	
	@PostMapping("**/pesquisarpessoa")
	public ModelAndView pesquisar(@RequestParam("nomepesquisa") String nomepesquisa) {
		ModelAndView mv = new ModelAndView("cadastro/cadastropessoa");
		mv.addObject("pessoas", pessoaRepository.findByName(nomepesquisa));
		mv.addObject("pessoa", new Pessoa());
		return mv;
	}
	
	@GetMapping("/telefones/{idpessoa}")
	public ModelAndView telefones(@PathVariable("idpessoa") Long idpessoa) {
		ModelAndView mv = new ModelAndView("cadastro/telefones");
		Optional<Pessoa> pessoa = pessoaRepository.findById(idpessoa);
		mv.addObject("pessoa", pessoa.get());
		mv.addObject("telefones", telefoneRepository.listarTelefonesPessoa(idpessoa));
		return mv;
	}
	
	@PostMapping("**/salvartelefone/{idpessoa}")
	public ModelAndView salvarTelefone(Telefone telefone, @PathVariable("idpessoa") Long idpessoa) {
		Pessoa pessoa = pessoaRepository.findById(idpessoa).get();
		telefone.setPessoa(pessoa);
		
		telefoneRepository.save(telefone);
		
		ModelAndView mv = new ModelAndView("cadastro/telefones");
		mv.addObject("pessoa", pessoa);
		mv.addObject("telefones", telefoneRepository.listarTelefonesPessoa(idpessoa));
		return mv;
	}
	
	@GetMapping("/removertelefone/{idtelefone}")
	public ModelAndView removerTelefone(@PathVariable("idtelefone") Long idTelefone) {
		Pessoa pessoa = telefoneRepository.findById(idTelefone).get().getPessoa();
		
		telefoneRepository.deleteById(idTelefone);
		
		ModelAndView mv = new ModelAndView("cadastro/telefones");
		mv.addObject("pessoa", pessoa);
		mv.addObject("telefones", telefoneRepository.listarTelefonesPessoa(pessoa.getId()));
		return mv;
	}
}
