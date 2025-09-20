package it.ids.mad.ProgettoIDS.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import it.ids.mad.ProgettoIDS.model.Prodotto;

public interface ProdottoRepository extends JpaRepository<Prodotto, Long> {
	List<Prodotto> findByApprovatoTrue();
	List<Prodotto> findByApprovatoFalse();
}
