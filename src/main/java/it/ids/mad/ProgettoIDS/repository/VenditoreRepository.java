package it.ids.mad.ProgettoIDS.repository;

import it.ids.mad.ProgettoIDS.model.Venditore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VenditoreRepository extends JpaRepository<Venditore, Long> {
}
