package com.curso.springboot.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.curso.springboot.model.Pessoa;

@Repository
public interface PessoaRepository extends CrudRepository<Pessoa, Long> {

	@Query("SELECT p FROM Pessoa p WHERE p.nome LIKE %?1%")
	List<Pessoa> findByName(String nome);
	
	@Query("SELECT p FROM Pessoa p WHERE p.sexo LIKE %?1%")
	List<Pessoa> findBySexo(String sexo);
	
	@Query("SELECT p FROM Pessoa p WHERE p.nome LIKE %?1% AND p.sexo = ?2")
	List<Pessoa> findByNameAndSexo(String nome, String sexo);
}
