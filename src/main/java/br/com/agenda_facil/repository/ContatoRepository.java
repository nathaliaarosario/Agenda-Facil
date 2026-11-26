package br.com.agenda_facil.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import br.com.agenda_facil.model.Contato;

public interface ContatoRepository extends JpaRepository<Contato, Long> {

}