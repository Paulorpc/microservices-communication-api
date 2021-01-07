package br.blog.smarti.ms.comunication.bank.repositories;

import org.springframework.data.repository.CrudRepository;

import br.blog.smarti.ms.comunication.bank.entities.Pagamento;

public interface PagamentoRepository extends CrudRepository<Pagamento, Long>{
}