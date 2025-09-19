package it.ids.mad.ProgettoIDS.repository;

import it.ids.mad.ProgettoIDS.model.Acquirente;
import it.ids.mad.ProgettoIDS.model.Carrello;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CarrelloRepository extends JpaRepository<Carrello, Long> {
    Optional<Carrello> findByAcquirenteId(Long acquirenteId);
    Optional<Carrello> findByAcquirente(Acquirente acquirente);
}
