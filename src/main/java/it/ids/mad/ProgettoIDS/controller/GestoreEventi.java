package it.ids.mad.ProgettoIDS.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.ids.mad.ProgettoIDS.api.dto.CreateEventoRequest;
import it.ids.mad.ProgettoIDS.factory.EventFactory;
import it.ids.mad.ProgettoIDS.infrastructure.Notifiche;
import it.ids.mad.ProgettoIDS.infrastructure.RegistroOperazioni;
import it.ids.mad.ProgettoIDS.model.Evento;
import it.ids.mad.ProgettoIDS.model.Utente;
import it.ids.mad.ProgettoIDS.repository.EventoRepository;
import it.ids.mad.ProgettoIDS.repository.UtenteRepository;
import it.ids.mad.ProgettoIDS.service.OsmService;

@RestController
@RequestMapping("/api/eventi")
public class GestoreEventi {

    @Autowired
    private EventoRepository eventoRepository;
    @Autowired
    private UtenteRepository utenteRepository;
    @Autowired
    private OsmService osmService;

    @Autowired
    private EventFactory eventFactory;
    @Autowired
    private RegistroOperazioni registroOperazioni;
    @Autowired
    private Notifiche notifiche;

    @GetMapping
    public List<Evento> getAllEventi() {
        return eventoRepository.findAll();
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ANIMATORE_FILIERA','ANIMATORE')")
    public ResponseEntity<?> createEvento(@RequestBody CreateEventoRequest req) {
        if (req.getTipo() == null) {
            return ResponseEntity.badRequest().body("Tipo evento mancante");
        }
        Evento evento;
        try {
            evento = eventFactory.creaEvento(req);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    evento.setApprovato(false);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getName() == null) {
            return ResponseEntity.status(401).body("Non autenticato");
        }
        Utente organizzatore = utenteRepository.findByUsername(auth.getName()).orElse(null);
        if (organizzatore == null) {
            return ResponseEntity.status(401).body("Utente non trovato");
        }
        evento.setOrganizzatore(organizzatore);

        if (evento.getLuogo() != null && !evento.getLuogo().isEmpty()) {
            double[] coords = osmService.getCoordinates(evento.getLuogo());
            if (coords != null) {
                evento.setLat(coords[0]);
                evento.setLon(coords[1]);
            }
        }
    Evento saved = eventoRepository.save(evento);
    registroOperazioni.registraOperazione("CREA_EVENTO", organizzatore);
    notifiche.inviaNotifica(organizzatore, "Evento creato e in attesa di approvazione");
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/me")
    @PreAuthorize("hasAnyAuthority('ANIMATORE_FILIERA','ANIMATORE')")
    public ResponseEntity<?> mieiEventi() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getName() == null) {
            return ResponseEntity.status(401).build();
        }
        Utente me = utenteRepository.findByUsername(auth.getName()).orElse(null);
        if (me == null) return ResponseEntity.status(401).build();
        return ResponseEntity.ok(eventoRepository.findByOrganizzatoreId(me.getId()));
    }

    @GetMapping("/me/prenotazioni")
    @PreAuthorize("hasAuthority('ACQUIRENTE')")
    public ResponseEntity<?> miePrenotazioni() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getName() == null) {
            return ResponseEntity.status(401).body(Map.of("status", "error", "message", "Non autenticato"));
        }
        Utente me = utenteRepository.findByUsername(auth.getName()).orElse(null);
        if (me == null) {
            return ResponseEntity.status(401).body(Map.of("status", "error", "message", "Utente non trovato"));
        }
        List<Evento> tutti = eventoRepository.findAll();
        List<Evento> miei = tutti.stream()
                .filter(e -> e.getPartecipanti().stream().anyMatch(p -> p.getId().equals(me.getId())))
                .toList();
        return ResponseEntity.ok(miei);
    }

    @PostMapping("/{id}/annulla")
    @PreAuthorize("hasAuthority('ACQUIRENTE')")
    public ResponseEntity<?> annullaPrenotazione(@PathVariable Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getName() == null) {
            return ResponseEntity.status(401).body(Map.of("status", "error", "message", "Non autenticato"));
        }
        Utente me = utenteRepository.findByUsername(auth.getName()).orElse(null);
        if (me == null) return ResponseEntity.status(401).body(Map.of("status", "error", "message", "Utente non trovato"));
        Optional<Evento> eventoOpt = eventoRepository.findById(id);
        if (eventoOpt.isEmpty()) return ResponseEntity.status(404).body(Map.of("status", "error", "message", "Evento non trovato"));
        Evento evento = eventoOpt.get();
        boolean removed = evento.getPartecipanti().removeIf(p -> p.getId().equals(me.getId()));
        if (!removed) return ResponseEntity.status(400).body(Map.of("status", "error", "message", "Non eri prenotato"));
        eventoRepository.save(evento);
        return ResponseEntity.ok(Map.of("status", "ok", "message", "Prenotazione annullata"));
    }

    @PostMapping("/{id}/delete")
    @PreAuthorize("hasAnyAuthority('ANIMATORE_FILIERA','ANIMATORE')")
    public ResponseEntity<?> deleteMyEvent(@PathVariable Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getName() == null) {
            return ResponseEntity.status(401).build();
        }
        Utente me = utenteRepository.findByUsername(auth.getName()).orElse(null);
        return eventoRepository.findById(id).map(e -> {
            if (me == null || e.getOrganizzatore() == null || !e.getOrganizzatore().getId().equals(me.getId())) {
                return ResponseEntity.status(403).build();
            }
            eventoRepository.delete(e);
            return ResponseEntity.ok().build();
        }).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/prenota")
    @PreAuthorize("hasAuthority('ACQUIRENTE')")
    public ResponseEntity<?> prenota(@PathVariable Long id, @RequestBody(required = false) Map<String, Object> payload) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getName() == null) {
            return ResponseEntity.status(401).body(Map.of("status", "error", "message", "Non autenticato"));
        }
        Optional<Utente> utenteOpt = utenteRepository.findByUsername(auth.getName());
        Optional<Evento> eventoOpt = eventoRepository.findById(id);
        if (eventoOpt.isEmpty()) return ResponseEntity.status(404).body(Map.of("status", "error", "message", "Evento non trovato"));
        if (utenteOpt.isEmpty()) return ResponseEntity.status(401).body(Map.of("status", "error", "message", "Utente non trovato"));

        Evento evento = eventoOpt.get();
        Utente utente = utenteOpt.get();

        if (!evento.isApprovato()) {
            return ResponseEntity.status(400).body(Map.of("status", "error", "message", "Evento non ancora approvato"));
        }
        if (evento.getData() != null && evento.getData().isBefore(java.time.LocalDateTime.now())) {
            return ResponseEntity.status(400).body(Map.of("status", "error", "message", "Evento già svolto"));
        }
        boolean already = evento.getPartecipanti().stream().anyMatch(p -> p.getId().equals(utente.getId()));
        if (already) {
            return ResponseEntity.ok().body(Map.of("status", "ok", "message", "Già prenotato"));
        }
        evento.getPartecipanti().add(utente);
        eventoRepository.save(evento);
        return ResponseEntity.ok().body(Map.of("status", "ok", "message", "Prenotazione confermata"));
    }
}
