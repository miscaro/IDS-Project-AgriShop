package it.ids.mad.ProgettoIDS.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import it.ids.mad.ProgettoIDS.dto.ArticoloResponse;
import it.ids.mad.ProgettoIDS.dto.MateriaPrimaCreateRequest;
import it.ids.mad.ProgettoIDS.dto.MateriaPrimaResponse;
import it.ids.mad.ProgettoIDS.dto.PacchettoCreateRequest;
import it.ids.mad.ProgettoIDS.dto.ProdottoCreateRequest;
import it.ids.mad.ProgettoIDS.dto.ProdottoResponse;
import it.ids.mad.ProgettoIDS.factory.ArticoloMapper;
import it.ids.mad.ProgettoIDS.model.MateriaPrima;
import it.ids.mad.ProgettoIDS.model.Pacchetto;
import it.ids.mad.ProgettoIDS.model.Prodotto;
import it.ids.mad.ProgettoIDS.model.Venditore;
import it.ids.mad.ProgettoIDS.repository.ArticoloRepository;
import it.ids.mad.ProgettoIDS.repository.MateriaPrimaRepository;
import it.ids.mad.ProgettoIDS.repository.PacchettoRepository;
import it.ids.mad.ProgettoIDS.repository.ProdottoRepository;
import it.ids.mad.ProgettoIDS.repository.UtenteRepository;
import it.ids.mad.ProgettoIDS.security.UserDetailsImpl;

@RestController
@RequestMapping({"/api/catalogo","/api"})
public class CatalogoController {

    @Autowired
    private ArticoloRepository articoloRepository;

    @Autowired
    private ProdottoRepository prodottoRepository;

    @Autowired
    private MateriaPrimaRepository materiaPrimaRepository;

    @Autowired
    private UtenteRepository utenteRepository;

    @Autowired
    private ArticoloMapper articoloMapper;

    @Autowired
    private PacchettoRepository pacchettoRepository;

    // Endpoint pubblici per visualizzare articoli approvati
    @GetMapping("/articoli")
    public List<ArticoloResponse> getArticoliPubblici() {
        return articoloMapper.toResponseArticoli(articoloRepository.findByApprovatoTrue());
    }

    @GetMapping("/prodotti")
    public List<ProdottoResponse> getProdottiPubblici() {
        return articoloMapper.toResponseProdotti(prodottoRepository.findByApprovatoTrue());
    }

    @GetMapping("/materie-prime")
    public List<MateriaPrimaResponse> getMateriePrimePubbliche() {
        return articoloMapper.toResponseMateriePrime(materiaPrimaRepository.findByApprovatoTrue());
    }

    // Alias public esplicito usato dal frontend (venditore dashboard)
    @GetMapping("/materie-prime/public")
    public List<MateriaPrimaResponse> getMateriePrimePubblicheAlias() {
        return articoloMapper.toResponseMateriePrime(materiaPrimaRepository.findByApprovatoTrue());
    }
    
    @GetMapping("/articoli/{id}")
    public ResponseEntity<ArticoloResponse> getArticoloById(@PathVariable(value = "id") Long articoloId) {
        return articoloRepository.findById(articoloId)
                .map(articolo -> ResponseEntity.ok().body(articoloMapper.toResponse(articolo)))
                .orElse(ResponseEntity.notFound().build());
    }

    // Pacchetti approvati pubblici (catalogo)
    @GetMapping("/pacchetti")
    public List<ArticoloResponse> getPacchettiPubblici() {
        return articoloMapper.toResponseArticoli(pacchettoRepository.findByApprovatoTrue());
    }

    // Pacchetti dell'utente autenticato (venditore)
    @GetMapping("/miei-pacchetti")
    @PreAuthorize("hasAnyAuthority('PRODUTTORE','TRASFORMATORE','DISTRIBUTORE_TIPICITA')")
    public List<ArticoloResponse> getMieiPacchetti() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
        return articoloMapper.toResponseArticoli(pacchettoRepository.findByAziendaOrigineId(userDetails.getId()));
    }

    // Endpoint per la creazione di prodotti (per venditori)
    @PostMapping("/prodotti")
    @PreAuthorize("hasAnyAuthority('PRODUTTORE','TRASFORMATORE','DISTRIBUTORE_TIPICITA')")
    @Transactional
    public ResponseEntity<?> createProdotto(@jakarta.validation.Valid @RequestBody ProdottoCreateRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
        Venditore venditore = (Venditore) utenteRepository.findById(userDetails.getId())
                .orElseThrow(() -> new RuntimeException("Venditore non trovato"));

        // Mapping DTO -> Entity
        Prodotto prodotto = new Prodotto();
        prodotto.setNome(request.getNome());
        prodotto.setDescrizione(request.getDescrizione());
        if (request.getPrezzo() != null) {
            prodotto.setPrezzo(request.getPrezzo());
        }
        if (request.getQuantita() != null) {
            prodotto.setQuantita(request.getQuantita());
        }
        prodotto.setCertificazioneProdotto(request.getCertificazioneProdotto());
        prodotto.setProcessoTrasformazione(request.getProcessoTrasformazione());
        prodotto.setOrigineProdotto(request.getOrigineProdotto());
        prodotto.setAziendaOrigine(venditore);
        prodotto.setApprovato(false);

        // Associazione materie prime (se presenti)
        List<Long> ids = request.getMateriePrimeIds();
        if (ids != null && !ids.isEmpty()) {
            List<MateriaPrima> trovate = new ArrayList<>();
            List<Long> mancanti = new ArrayList<>();
            for (Long id : ids) {
                materiaPrimaRepository.findById(id).ifPresentOrElse(trovate::add, () -> mancanti.add(id));
            }
            if (!mancanti.isEmpty()) {
                return ResponseEntity.badRequest().body("Materie prime inesistenti: " + mancanti);
            }
            prodotto.setMateriePrime(trovate);
        }

        Prodotto salvato = prodottoRepository.save(prodotto);
        return ResponseEntity.ok(salvato);
    }

    // Endpoint per la creazione di materie prime (per venditori)
    @PostMapping("/materie-prime")
    @PreAuthorize("hasAnyAuthority('PRODUTTORE','TRASFORMATORE','DISTRIBUTORE_TIPICITA')")
    @Transactional
    public ResponseEntity<?> createMateriaPrima(@jakarta.validation.Valid @RequestBody MateriaPrimaCreateRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
        Venditore venditore = (Venditore) utenteRepository.findById(userDetails.getId())
                .orElseThrow(() -> new RuntimeException("Venditore non trovato"));

        MateriaPrima mp = new MateriaPrima();
        mp.setNome(request.getNome());
        mp.setDescrizione(request.getDescrizione());
        mp.setPrezzo(request.getPrezzo());
        mp.setQuantita(request.getQuantita());
        mp.setMetodoColtivazione(request.getMetodoColtivazione());
        mp.setCertificazioneMP(request.getCertificazioneMP());
        mp.setAziendaOrigine(venditore);
        mp.setApprovato(false);

        MateriaPrima nuovaMateriaPrima = materiaPrimaRepository.save(mp);
        return ResponseEntity.ok(articoloMapper.toResponse(nuovaMateriaPrima));
    }

    // Endpoint per il curatore per vedere articoli pendenti
    @GetMapping("/articoli/pendenti")
    @PreAuthorize("hasAuthority('CURATORE')")
    public List<ArticoloResponse> getArticoliPendenti() {
        return articoloMapper.toResponseArticoli(articoloRepository.findByApprovatoFalse());
    }

    // Endpoint per il curatore per approvare un articolo
    @PostMapping("/articoli/{id}/approva")
    @PreAuthorize("hasAuthority('CURATORE')")
    @Transactional
    public ResponseEntity<?> approvaArticolo(@PathVariable Long id) {
        return articoloRepository.findById(id).map(articolo -> {
            articolo.setApprovato(true);
            articoloRepository.save(articolo);
            return ResponseEntity.ok("Articolo approvato con successo.");
        }).orElse(ResponseEntity.notFound().build());
    }

    // Endpoint approvazione prodotti
    @GetMapping("/prodotti/pending")
    @PreAuthorize("hasAuthority('CURATORE')")
    public List<ProdottoResponse> getProdottiPendenti() {
        return articoloMapper.toResponseProdotti(prodottoRepository.findByApprovatoFalse());
    }

    @PostMapping("/prodotti/{id}/approve")
    @PreAuthorize("hasAuthority('CURATORE')")
    @Transactional
    public ResponseEntity<?> approvaProdotto(@PathVariable Long id) {
        return prodottoRepository.findById(id).map(p -> {
            p.setApprovato(true);
            prodottoRepository.save(p);
            return ResponseEntity.ok("Prodotto approvato con successo.");
        }).orElse(ResponseEntity.notFound().build());
    }

    // Endpoints approvazione materie prime
    @GetMapping("/materie-prime/pending")
    @PreAuthorize("hasAuthority('CURATORE')")
    public List<MateriaPrimaResponse> getMateriePrimePendenti() {
        return articoloMapper.toResponseMateriePrime(materiaPrimaRepository.findByApprovatoFalse());
    }

    @PostMapping("/materie-prime/{id}/approve")
    @PreAuthorize("hasAuthority('CURATORE')")
    @Transactional
    public ResponseEntity<?> approvaMateriaPrima(@PathVariable Long id) {
        return materiaPrimaRepository.findById(id).map(mp -> {
            mp.setApprovato(true);
            materiaPrimaRepository.save(mp);
            return ResponseEntity.ok("Materia prima approvata con successo.");
        }).orElse(ResponseEntity.notFound().build());
    }

    // Pacchetti pendenti per CURATORE
    @GetMapping("/pacchetti/pending")
    @PreAuthorize("hasAuthority('CURATORE')")
    public List<ArticoloResponse> getPacchettiPendenti() {
        return articoloMapper.toResponseArticoli(pacchettoRepository.findByApprovatoFalse());
    }

    // Approva pacchetto
    @PostMapping("/pacchetti/{id}/approve")
    @PreAuthorize("hasAuthority('CURATORE')")
    @Transactional
    public ResponseEntity<?> approvaPacchetto(@PathVariable Long id) {
        return pacchettoRepository.findById(id).map(p -> {
            p.setApprovato(true);
            pacchettoRepository.save(p);
            return ResponseEntity.ok("Pacchetto approvato con successo.");
        }).orElse(ResponseEntity.notFound().build());
    }

    // Creazione Pacchetto - Solo DISTRIBUTORE_TIPICITA
    @PostMapping("/pacchetti")
    @PreAuthorize("hasAuthority('DISTRIBUTORE_TIPICITA')")
    @Transactional
    public ResponseEntity<?> createPacchetto(@jakarta.validation.Valid @RequestBody PacchettoCreateRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
        Venditore venditore = (Venditore) utenteRepository.findById(userDetails.getId())
                .orElseThrow(() -> new RuntimeException("Venditore non trovato"));

        // Carica prodotti e valida esistenza
        List<Long> ids = request.getProdottiIds();
        List<Prodotto> prodotti = new ArrayList<>();
        List<Long> mancanti = new ArrayList<>();
        if (ids != null) {
            for (Long id : ids) {
                prodottoRepository.findById(id).ifPresentOrElse(prodotti::add, () -> mancanti.add(id));
            }
        }
        if (!mancanti.isEmpty()) {
            return ResponseEntity.badRequest().body("Prodotti inesistenti: " + mancanti);
        }

        Pacchetto pacchetto = new Pacchetto();
        pacchetto.setNome(request.getNome());
        pacchetto.setDescrizione(request.getDescrizione());
        pacchetto.setPrezzo(request.getPrezzo());
        pacchetto.setQuantita(request.getQuantita());
        pacchetto.setAziendaOrigine(venditore);
        pacchetto.setApprovato(false);
        pacchetto.setProdotti(prodotti);

        Pacchetto salvato = pacchettoRepository.save(pacchetto);
        return ResponseEntity.ok(articoloMapper.toResponse(salvato));
    }

    // Link prodotto-materia prima (per trasformatori) via query param
    @PostMapping("/materie-prime/link")
    @PreAuthorize("hasAuthority('TRASFORMATORE')")
    @Transactional
    public ResponseEntity<?> linkMateriaPrima(@RequestParam Long prodottoId, @RequestParam Long materiaPrimaId) {
        var prodottoOpt = prodottoRepository.findById(prodottoId);
        var mpOpt = materiaPrimaRepository.findById(materiaPrimaId);
        if (prodottoOpt.isEmpty() || mpOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("ID non valido");
        }
        Prodotto prodotto = prodottoOpt.get();
        MateriaPrima mp = mpOpt.get();
        if (!prodotto.getMateriePrime().contains(mp)) {
            prodotto.getMateriePrime().add(mp);
            prodottoRepository.save(prodotto);
        }
        return ResponseEntity.ok("Collegamento creato");
    }
}
