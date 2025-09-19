package it.ids.mad.ProgettoIDS.repository;

import it.ids.mad.ProgettoIDS.model.ElementoOrdine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ElementoOrdineRepository extends JpaRepository<ElementoOrdine, Long> {
}
