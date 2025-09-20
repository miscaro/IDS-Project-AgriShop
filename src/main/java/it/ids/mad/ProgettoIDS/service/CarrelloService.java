package it.ids.mad.ProgettoIDS.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.ids.mad.ProgettoIDS.model.Acquirente;
import it.ids.mad.ProgettoIDS.model.Articolo;
import it.ids.mad.ProgettoIDS.model.Carrello;
import it.ids.mad.ProgettoIDS.model.ElementoCarrello;
import it.ids.mad.ProgettoIDS.model.ElementoOrdine;
import it.ids.mad.ProgettoIDS.model.Ordine;
import it.ids.mad.ProgettoIDS.repository.AcquirenteRepository;
import it.ids.mad.ProgettoIDS.repository.ArticoloRepository;
import it.ids.mad.ProgettoIDS.repository.CarrelloRepository;
import it.ids.mad.ProgettoIDS.repository.OrdineRepository;

@Service
@Transactional
public class CarrelloService {

    @Autowired
    private CarrelloRepository carrelloRepository;

    @Autowired
    private AcquirenteRepository acquirenteRepository;

    @Autowired
    private ArticoloRepository articoloRepository;

    @Autowired
    private OrdineRepository ordineRepository;

    @Autowired
    private it.ids.mad.ProgettoIDS.infrastructure.SistemaPagamento sistemaPagamento;

    public Carrello getCarrello(Long acquirenteId) {
        return carrelloRepository.findByAcquirenteId(acquirenteId)
                .orElseGet(() -> createCarrello(acquirenteId));
    }

    private Carrello createCarrello(Long acquirenteId) {
        Acquirente acquirente = acquirenteRepository.findById(acquirenteId)
                .orElseThrow(() -> new RuntimeException("Acquirente non trovato"));
        Carrello carrello = new Carrello();
        carrello.setAcquirente(acquirente);
        acquirente.setCarrello(carrello);
        return carrelloRepository.save(carrello);
    }

    public Carrello aggiungiArticolo(Long acquirenteId, Long articoloId, int quantita) {
        Carrello carrello = getCarrello(acquirenteId);
        Articolo articolo = articoloRepository.findById(articoloId)
                .orElseThrow(() -> new RuntimeException("Articolo non trovato"));

        Optional<ElementoCarrello> elementoEsistente = carrello.getElementi().stream()
                .filter(e -> e.getArticolo().getId().equals(articoloId))
                .findFirst();

        if (elementoEsistente.isPresent()) {
            ElementoCarrello elemento = elementoEsistente.get();
            elemento.setQuantita(elemento.getQuantita() + quantita);
        } else {
            ElementoCarrello nuovoElemento = new ElementoCarrello();
            nuovoElemento.setCarrello(carrello);
            nuovoElemento.setArticolo(articolo);
            nuovoElemento.setQuantita(quantita);
            carrello.getElementi().add(nuovoElemento);
        }

        carrello.setTotale(calcolaTotale(carrello));
        return carrelloRepository.save(carrello);
    }

    public Carrello rimuoviArticolo(Long acquirenteId, Long articoloId) {
        Carrello carrello = getCarrello(acquirenteId);
        carrello.getElementi().removeIf(e -> e.getArticolo().getId().equals(articoloId));
        carrello.setTotale(calcolaTotale(carrello));
        return carrelloRepository.save(carrello);
    }

    public Carrello aggiornaQuantita(Long acquirenteId, Long articoloId, int nuovaQuantita) {
        if (nuovaQuantita <= 0) {
            throw new IllegalArgumentException("La quantità deve essere positiva");
        }
        Carrello carrello = getCarrello(acquirenteId);
        ElementoCarrello elemento = carrello.getElementi().stream()
                .filter(e -> e.getArticolo().getId().equals(articoloId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Articolo non presente nel carrello"));

        Articolo articolo = elemento.getArticolo();
        if (nuovaQuantita > articolo.getQuantita()) {
            throw new IllegalArgumentException("Quantità richiesta oltre lo stock disponibile");
        }

        elemento.setQuantita(nuovaQuantita);
        carrello.setTotale(calcolaTotale(carrello));
        return carrelloRepository.save(carrello);
    }

    private BigDecimal calcolaTotale(Carrello carrello) {
        return carrello.getElementi().stream()
                .map(e -> e.getArticolo().getPrezzo().multiply(new BigDecimal(e.getQuantita())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public Ordine checkout(Long acquirenteId, it.ids.mad.ProgettoIDS.api.dto.CheckoutRequest request) {
        Carrello carrello = getCarrello(acquirenteId);
        if (carrello.getElementi().isEmpty()) {
            throw new RuntimeException("Il carrello è vuoto");
        }
        // Validazioni base indirizzo
        if (request.getIndirizzo() == null || request.getIndirizzo().isBlank()) {
            throw new IllegalArgumentException("Indirizzo mancante");
        }
        if (request.getCap() == null || !request.getCap().matches("\\d{5}")) {
            throw new IllegalArgumentException("CAP non valido");
        }

        Acquirente acquirente = acquirenteRepository.findById(acquirenteId)
                .orElseThrow(() -> new RuntimeException("Acquirente non trovato"));

        Ordine ordine = new Ordine();
        ordine.setAcquirente(acquirente);
        ordine.setData(LocalDate.now());

        List<ElementoOrdine> elementiOrdine = carrello.getElementi().stream().map(elementoCarrello -> {
            Articolo articolo = elementoCarrello.getArticolo();
            // Controllo stock
            if (elementoCarrello.getQuantita() > articolo.getQuantita()) {
                throw new IllegalArgumentException("Quantità per articolo '" + articolo.getNome() + "' oltre lo stock");
            }
            ElementoOrdine elementoOrdine = new ElementoOrdine();
            elementoOrdine.setOrdine(ordine);
            elementoOrdine.setArticolo(articolo);
            elementoOrdine.setQuantita(elementoCarrello.getQuantita());
            elementoOrdine.setPrezzo(articolo.getPrezzo());
            return elementoOrdine;
        }).collect(Collectors.toList());

        ordine.setElementi(elementiOrdine);

        BigDecimal totale = elementiOrdine.stream()
                .map(e -> e.getPrezzo().multiply(new BigDecimal(e.getQuantita())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        ordine.setTotale(totale);

    // Simulazione pagamento
    sistemaPagamento.processaPagamento(acquirente, request.getNumeroCarta(), request.getScadenzaCarta(), request.getCvv(), totale);

        // Indirizzo completo
        String indirizzoCompleto = String.format("%s %s, %s %s (%s)",
                request.getNome(), request.getCognome(), request.getIndirizzo() + " " + request.getNumeroCivico(), request.getCitta(), request.getCap());
        ordine.setIndirizzoSpedizione(indirizzoCompleto);

        Ordine savedOrdine = ordineRepository.save(ordine);

        // Aggiorna stock e svuota carrello
        for (ElementoOrdine eo : elementiOrdine) {
            Articolo art = eo.getArticolo();
            art.setQuantita(art.getQuantita() - eo.getQuantita());
        }
        carrello.getElementi().clear();
        carrelloRepository.save(carrello);

        return savedOrdine;
    }
}
