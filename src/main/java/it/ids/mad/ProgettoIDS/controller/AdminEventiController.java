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

import it.ids.mad.ProgettoIDS.model.Evento;
import it.ids.mad.ProgettoIDS.repository.EventoRepository;

@RestController
@RequestMapping("/api/admin/eventi")
public class AdminEventiController {
    @Autowired
    private EventoRepository eventoRepository;

    @GetMapping("/pending")
    @PreAuthorize("hasAuthority('GESTOREPIATTAFORMA')")
    public List<Evento> getPending() {
        return eventoRepository.findAll().stream().filter(e -> !e.isApprovato()).toList();
    }

    @PostMapping("/{id}/approva")
    @PreAuthorize("hasAuthority('GESTOREPIATTAFORMA')")
    public ResponseEntity<?> approva(@PathVariable Long id) {
        return eventoRepository.findById(id).map(e -> {
            e.setApprovato(true);
            eventoRepository.save(e);
            return ResponseEntity.ok().build();
        }).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/rifiuta")
    @PreAuthorize("hasAuthority('GESTOREPIATTAFORMA')")
    public ResponseEntity<?> rifiuta(@PathVariable Long id) {
        return eventoRepository.findById(id).map(e -> {
            eventoRepository.delete(e);
            return ResponseEntity.ok().build();
        }).orElse(ResponseEntity.notFound().build());
    }
}
