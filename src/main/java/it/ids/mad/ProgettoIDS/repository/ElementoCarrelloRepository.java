package it.ids.mad.ProgettoIDS.repository;

import it.ids.mad.ProgettoIDS.model.ElementoCarrello;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ElementoCarrelloRepository extends JpaRepository<ElementoCarrello, Long> {
}
