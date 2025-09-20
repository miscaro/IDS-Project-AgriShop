package it.ids.mad.ProgettoIDS.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.ids.mad.ProgettoIDS.api.dto.AggiungiArticoloRequest;
import it.ids.mad.ProgettoIDS.api.dto.CheckoutRequest;
import it.ids.mad.ProgettoIDS.api.dto.CheckoutResponse;
import it.ids.mad.ProgettoIDS.api.dto.UpdateQuantitaRequest;
import it.ids.mad.ProgettoIDS.model.Carrello;
import it.ids.mad.ProgettoIDS.model.Ordine;
import it.ids.mad.ProgettoIDS.service.CarrelloService;

@RestController
@RequestMapping("/api/carrello")
public class CarrelloController {

    @Autowired
    private CarrelloService carrelloService;

    @GetMapping("/{acquirenteId}")
    public ResponseEntity<Carrello> getCarrello(@PathVariable Long acquirenteId) {
        Carrello carrello = carrelloService.getCarrello(acquirenteId);
        return ResponseEntity.ok(carrello);
    }

    @PostMapping("/{acquirenteId}/articoli")
    public ResponseEntity<Carrello> aggiungiArticolo(@PathVariable Long acquirenteId, @jakarta.validation.Valid @RequestBody AggiungiArticoloRequest request) {
        Carrello carrello = carrelloService.aggiungiArticolo(acquirenteId, request.getArticoloId(), request.getQuantita());
        return ResponseEntity.ok(carrello);
    }

    @DeleteMapping("/{acquirenteId}/articoli/{articoloId}")
    public ResponseEntity<Carrello> rimuoviArticolo(@PathVariable Long acquirenteId, @PathVariable Long articoloId) {
        Carrello carrello = carrelloService.rimuoviArticolo(acquirenteId, articoloId);
        return ResponseEntity.ok(carrello);
    }

    @PutMapping("/{acquirenteId}/articoli/quantita")
    public ResponseEntity<Carrello> aggiornaQuantita(@PathVariable Long acquirenteId, @RequestBody UpdateQuantitaRequest request) {
        Carrello carrello = carrelloService.aggiornaQuantita(acquirenteId, request.getArticoloId(), request.getQuantita());
        return ResponseEntity.ok(carrello);
    }

    @PostMapping("/{acquirenteId}/checkout")
    public ResponseEntity<CheckoutResponse> checkout(@PathVariable Long acquirenteId, @jakarta.validation.Valid @RequestBody CheckoutRequest request) {
        Ordine ordine = carrelloService.checkout(acquirenteId, request);
        CheckoutResponse resp = new CheckoutResponse(ordine.getId(), ordine.getData(), ordine.getTotale(), "SUCCESSO", ordine.getIndirizzoSpedizione());
        return ResponseEntity.ok(resp);
    }
}
