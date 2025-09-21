package it.ids.mad.ProgettoIDS.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import it.ids.mad.ProgettoIDS.model.MateriaPrima;

public interface MateriaPrimaRepository extends JpaRepository<MateriaPrima, Long> {
    List<MateriaPrima> findByApprovatoTrue();
    List<MateriaPrima> findByApprovatoFalse();
}
