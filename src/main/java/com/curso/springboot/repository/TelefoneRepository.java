package com.curso.springboot.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.curso.springboot.model.Telefone;

@Repository
public interface TelefoneRepository extends CrudRepository<Telefone, Long> {

	@Query("SELECT t FROM Telefone t WHERE t.pessoa.id = ?1")
	List<Telefone> listarTelefonesPessoa(Long idPessoa);
}
