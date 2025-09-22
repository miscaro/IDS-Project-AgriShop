package it.ids.mad.ProgettoIDS.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.ids.mad.ProgettoIDS.model.Utente;
import it.ids.mad.ProgettoIDS.model.Venditore;
import it.ids.mad.ProgettoIDS.repository.UtenteRepository;
import it.ids.mad.ProgettoIDS.repository.VenditoreRepository;

@RestController
@RequestMapping("/api/utenti")
public class GestoreUtenti {

    @Autowired
    private UtenteRepository utenteRepository;

    @Autowired
    private VenditoreRepository venditoreRepository;

    // Endpoint per ottenere tutti gli utenti (per gestore piattaforma)
    @GetMapping
    @PreAuthorize("hasAuthority('GESTOREPIATTAFORMA')")
    public List<Utente> getAllUtenti() {
        return utenteRepository.findAll();
    }

    // Endpoint per ottenere tutti i venditori (pubblico o per gestori)
    @GetMapping("/venditori")
    public List<Venditore> getAllVenditori() {
        return venditoreRepository.findAll();
    }

    // Endpoint per ottenere un venditore specifico
    @GetMapping("/venditori/{id}")
    public ResponseEntity<Venditore> getVenditoreById(@PathVariable(value = "id") Long venditoreId) {
        return venditoreRepository.findById(venditoreId)
                .map(venditore -> ResponseEntity.ok().body(venditore))
                .orElse(ResponseEntity.notFound().build());
    }

    // Endpoint per l'approvazione di un utente (gestore piattaforma)
    @PostMapping("/{id}/approva")
    @PreAuthorize("hasAuthority('GESTOREPIATTAFORMA')")
    public ResponseEntity<?> approvaUtente(@PathVariable Long id) {
        return utenteRepository.findById(id).map(utente -> {
            utente.setEnabled(true);
            utenteRepository.save(utente);
            return ResponseEntity.ok("Utente approvato con successo.");
        }).orElse(ResponseEntity.notFound().build());
    }
    
    // Endpoint per ottenere utenti in attesa di approvazione
    @GetMapping("/pendenti")
    @PreAuthorize("hasAuthority('GESTOREPIATTAFORMA')")
    public List<Utente> getUtentiPendenti() {
        return utenteRepository.findByEnabled(false);
    }
}
