package com.curso.springboot.repository;

import java.util.List;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.curso.springboot.model.Pessoa;

@Repository
public interface PessoaRepository extends JpaRepository<Pessoa, Long> {

	@Query("SELECT p FROM Pessoa p WHERE p.nome LIKE %?1%")
	List<Pessoa> findByName(String nome);
	
	@Query("SELECT p FROM Pessoa p WHERE p.sexo LIKE %?1%")
	List<Pessoa> findBySexo(String sexo);
	
	@Query("SELECT p FROM Pessoa p WHERE p.nome LIKE %?1% AND p.sexo = ?2")
	List<Pessoa> findByNameAndSexo(String nome, String sexo);
	
	default Page<Pessoa> findByNamePage(String nome, Pageable pageable) {
		Pessoa pessoa = new Pessoa();
		pessoa.setNome(nome);
		
		ExampleMatcher exampleMatcher = ExampleMatcher.matchingAny()
				.withMatcher("nome", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());
		
		Example<Pessoa> example = Example.of(pessoa, exampleMatcher);
		
		Page<Pessoa> pessoas = findAll(example, pageable);
		
		return pessoas;
	}
	
	default Page<Pessoa> findByNameAndSexoPage(String nome, String sexo, Pageable pageable) {
		Pessoa pessoa = new Pessoa();
		pessoa.setNome(nome);
		pessoa.setSexo(sexo);
		
		ExampleMatcher exampleMatcher = ExampleMatcher.matchingAny()
				.withMatcher("nome", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
				.withMatcher("sexo", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());
		
		Example<Pessoa> example = Example.of(pessoa, exampleMatcher);
		
		Page<Pessoa> pessoas = findAll(example, pageable);
		
		return pessoas;
	}
}
