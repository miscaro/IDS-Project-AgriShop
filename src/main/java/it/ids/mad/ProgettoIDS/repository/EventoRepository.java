package it.ids.mad.ProgettoIDS.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import it.ids.mad.ProgettoIDS.model.Evento;

public interface EventoRepository extends JpaRepository<Evento, Long> {
	List<Evento> findByApprovatoTrue();
	List<Evento> findByOrganizzatoreId(Long organizzatoreId);
}
