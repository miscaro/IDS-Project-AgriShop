package it.ids.mad.ProgettoIDS.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import it.ids.mad.ProgettoIDS.model.Pacchetto;

public interface PacchettoRepository extends JpaRepository<Pacchetto, Long> {
	List<Pacchetto> findByApprovatoFalse();
	List<Pacchetto> findByApprovatoTrue();
	List<Pacchetto> findByAziendaOrigineId(Long aziendaId);
}
