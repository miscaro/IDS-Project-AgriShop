package it.ids.mad.ProgettoIDS.repository;

import it.ids.mad.ProgettoIDS.model.Acquirente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AcquirenteRepository extends JpaRepository<Acquirente, Long> {
}
