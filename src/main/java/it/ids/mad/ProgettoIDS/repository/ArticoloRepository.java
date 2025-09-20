package it.ids.mad.ProgettoIDS.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import it.ids.mad.ProgettoIDS.model.Articolo;

public interface ArticoloRepository extends JpaRepository<Articolo, Long> {
	List<Articolo> findByApprovatoTrue();
	List<Articolo> findByApprovatoFalse();
	List<Articolo> findByApprovatoTrueAndNomeContainingIgnoreCase(String nome);
	List<Articolo> findByApprovatoTrueAndAziendaOrigineId(Long aziendaId);
}
