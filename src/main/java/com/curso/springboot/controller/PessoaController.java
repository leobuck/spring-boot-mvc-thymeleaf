package com.curso.springboot.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.curso.springboot.model.Pessoa;
import com.curso.springboot.model.Telefone;
import com.curso.springboot.repository.PessoaRepository;
import com.curso.springboot.repository.ProfissaoRepository;
import com.curso.springboot.repository.TelefoneRepository;
import com.curso.springboot.util.ReportUtil;

@Controller
public class PessoaController {

	@Autowired
	private PessoaRepository pessoaRepository;
	
	@Autowired
	private TelefoneRepository telefoneRepository;
	
	@Autowired
	private ProfissaoRepository profissaoRepository;
	
	@Autowired
	private ReportUtil reportUtil;
	
	@RequestMapping(method = RequestMethod.GET, value = "/cadastropessoa")
	public ModelAndView index() {
		ModelAndView mv = new ModelAndView("cadastro/cadastropessoa");
		mv.addObject("pessoas", pessoaRepository.findAll(PageRequest.of(0, 5, Sort.by("nome"))));
		mv.addObject("pessoa", new Pessoa());
		mv.addObject("profissoes", profissaoRepository.findAll());
		return mv;
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "**/salvarpessoa", consumes = {"multipart/form-data"})
	public ModelAndView salvar(@Valid Pessoa pessoa, BindingResult bindingResult, final MultipartFile file) 
			throws IOException {
		
		pessoa.setTelefones(telefoneRepository.listarTelefonesPessoa(pessoa.getId()));
		
		if (bindingResult.hasErrors()) {
			ModelAndView mv = new ModelAndView("cadastro/cadastropessoa");
			mv.addObject("pessoas", pessoaRepository.findAll(PageRequest.of(0, 5, Sort.by("nome"))));
			mv.addObject("pessoa", pessoa);
		
			List<String> msg = new ArrayList<>();
			for (ObjectError error : bindingResult.getAllErrors()) {
				msg.add(error.getDefaultMessage());
			}
			
			mv.addObject("msg", msg);
			return mv;
		}
		
		if (file.getSize() > 0) {
			pessoa.setCurriculo(file.getBytes());
			pessoa.setTipoArquivoCurriculo(file.getContentType());
			pessoa.setNomeArquivoCurriculo(file.getOriginalFilename());
		} else {
			if (pessoa.getId() != null) {
				Pessoa pessoaAux = pessoaRepository.findById(pessoa.getId()).get();
				pessoa.setCurriculo(pessoaAux.getCurriculo());
				pessoa.setTipoArquivoCurriculo(pessoaAux.getTipoArquivoCurriculo());
				pessoa.setNomeArquivoCurriculo(pessoaAux.getNomeArquivoCurriculo());
			}
		}
		
		pessoaRepository.save(pessoa);
		
		ModelAndView mv = new ModelAndView("cadastro/cadastropessoa");
		mv.addObject("pessoas", pessoaRepository.findAll(PageRequest.of(0, 5, Sort.by("nome"))));
		mv.addObject("pessoa", new Pessoa());
		mv.addObject("profissoes", profissaoRepository.findAll());
		return mv;
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/listarpessoas")
	public ModelAndView pessoas() {
		ModelAndView mv = new ModelAndView("cadastro/cadastropessoa");
		mv.addObject("pessoas", pessoaRepository.findAll(PageRequest.of(0, 5, Sort.by("nome"))));
		mv.addObject("pessoa", new Pessoa());
		mv.addObject("profissoes", profissaoRepository.findAll());
		return mv;
	}
	
	@GetMapping("/editarpessoa/{idpessoa}")
	public ModelAndView editar(@PathVariable("idpessoa") Long idpessoa) {
		ModelAndView mv = new ModelAndView("cadastro/cadastropessoa");
		Optional<Pessoa> pessoa = pessoaRepository.findById(idpessoa);
		mv.addObject("pessoa", pessoa.get());
		mv.addObject("profissoes", profissaoRepository.findAll());
		return mv;
	}
	
	@GetMapping("/removerpessoa/{idpessoa}")
	public ModelAndView remover(@PathVariable("idpessoa") Long idpessoa) {
		pessoaRepository.deleteById(idpessoa);
		
		ModelAndView mv = new ModelAndView("cadastro/cadastropessoa");
		mv.addObject("pessoas", pessoaRepository.findAll(PageRequest.of(0, 5, Sort.by("nome"))));
		mv.addObject("pessoa", new Pessoa());
		mv.addObject("profissoes", profissaoRepository.findAll());
		return mv;
	}
	
	@PostMapping("**/pesquisarpessoa")
	public ModelAndView pesquisar(
			@RequestParam("nomepesquisa") String nomepesquisa, 
			@RequestParam("sexopesquisa") String sexopesquisa,
			@PageableDefault(size = 5, sort = {"nome"}) Pageable pageable) {
		
		Page<Pessoa> pessoas = null;
		
		if (sexopesquisa != null && !sexopesquisa.isEmpty()) {
			pessoas = pessoaRepository.findByNameAndSexoPage(nomepesquisa, sexopesquisa, pageable);
		} else {
			pessoas = pessoaRepository.findByNamePage(nomepesquisa, pageable);
		}
		
		ModelAndView mv = new ModelAndView("cadastro/cadastropessoa");
		mv.addObject("pessoas", pessoas);
		mv.addObject("pessoa", new Pessoa());
		mv.addObject("profissoes", profissaoRepository.findAll());
		mv.addObject("nomepesquisa", nomepesquisa);
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
	
	@GetMapping("**/pesquisarpessoa")
	public void imprimirPdf(@RequestParam("nomepesquisa") String nomepesquisa, @RequestParam("sexopesquisa") String sexopesquisa,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		List<Pessoa> pessoas = new ArrayList<>();
		
		if (sexopesquisa != null && !sexopesquisa.isEmpty() && nomepesquisa != null && !nomepesquisa.isEmpty()) {
			pessoas = pessoaRepository.findByNameAndSexo(nomepesquisa, sexopesquisa);
		} else if (nomepesquisa != null && !nomepesquisa.isEmpty()) {
			pessoas = pessoaRepository.findByName(nomepesquisa);
		} else if (sexopesquisa != null && !sexopesquisa.isEmpty()) {
			pessoas = pessoaRepository.findBySexo(sexopesquisa);
		} else {
			Iterable<Pessoa> iterable = pessoaRepository.findAll();
			
			for (Pessoa pessoa : iterable) {
				pessoas.add(pessoa);
			}
		}
		
		byte[] pdf = reportUtil.gerarRelatorio(pessoas, "pessoa", request.getServletContext());
		
		response.setContentLength(pdf.length);
		response.setContentType("application/octet-stream");
		String headerKey = "Content-Disposition";
		String headerValue = String.format("attachment; filename=\"%s\"", "relatorio.pdf");
		response.setHeader(headerKey, headerValue);
		
		response.getOutputStream().write(pdf);
	}
	
	@GetMapping("**/downloadcurriculo/{idpessoa}")
	public void downloadCurriculo(@PathVariable("idpessoa") Long idpessoa, 
			HttpServletResponse response) throws IOException {
		
		Pessoa pessoa = pessoaRepository.findById(idpessoa).get();
		
		if (pessoa.getCurriculo() != null) {
			response.setContentLength(pessoa.getCurriculo().length);
			response.setContentType(pessoa.getTipoArquivoCurriculo());
			
			String headerKey = "Content-Disposition";
			String headerValue = String.format("attachment; filename=\"%s\"", pessoa.getNomeArquivoCurriculo());
			response.setHeader(headerKey, headerValue);
			
			response.getOutputStream().write(pessoa.getCurriculo());
		}
	}
	
	@GetMapping("/pessoaspaginacao")
	public ModelAndView carregarPessoaPorPaginacao(@PageableDefault(size = 5) Pageable pageable,
			ModelAndView model, @RequestParam("nomepesquisa") String nomepesquisa) {
		
		Page<Pessoa> pagePessoa = pessoaRepository.findByNamePage(nomepesquisa, pageable);
		model.addObject("pessoas", pagePessoa);
		model.addObject("pessoa", new Pessoa());
		model.addObject("nomepesquisa", nomepesquisa);
		model.setViewName("cadastro/cadastropessoa");
		
		return model;
	}
	
}
