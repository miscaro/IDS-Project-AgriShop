package it.ids.mad.ProgettoIDS.repository;

import it.ids.mad.ProgettoIDS.model.Ordine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrdineRepository extends JpaRepository<Ordine, Long> {
    List<Ordine> findByAcquirenteId(Long acquirenteId);
}
