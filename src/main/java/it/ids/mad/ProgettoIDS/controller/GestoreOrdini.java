package it.ids.mad.ProgettoIDS.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.ids.mad.ProgettoIDS.model.Ordine;
import it.ids.mad.ProgettoIDS.service.OrdineService;

@RestController
@RequestMapping("/api/ordini")
public class GestoreOrdini {

    @Autowired
    private OrdineService ordineService;

    @GetMapping("/{acquirenteId}")
    public ResponseEntity<List<Ordine>> getOrdiniByAcquirente(@PathVariable Long acquirenteId) {
        List<Ordine> ordini = ordineService.getOrdiniByAcquirente(acquirenteId);
        // Inizializza elementi per la serializzazione JSON
        ordini.forEach(o -> {
            o.getElementi().forEach(e -> {
                if (e.getArticolo() != null) {
                    e.getArticolo().getNome();
                    e.getArticolo().getPrezzo();
                }
            });
        });
        return ResponseEntity.ok(ordini);
    }

}
