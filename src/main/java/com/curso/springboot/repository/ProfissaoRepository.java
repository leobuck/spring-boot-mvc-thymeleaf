package com.curso.springboot.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.curso.springboot.model.Profissao;

@Repository
public interface ProfissaoRepository extends CrudRepository<Profissao, Long> {

}
