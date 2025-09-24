package it.ids.mad.ProgettoIDS.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import it.ids.mad.ProgettoIDS.model.Articolo;
import it.ids.mad.ProgettoIDS.model.Evento;
import it.ids.mad.ProgettoIDS.model.MateriaPrima;
import it.ids.mad.ProgettoIDS.model.Pacchetto;
import it.ids.mad.ProgettoIDS.model.Prodotto;
import it.ids.mad.ProgettoIDS.model.Venditore;
import it.ids.mad.ProgettoIDS.repository.ArticoloRepository;
import it.ids.mad.ProgettoIDS.repository.EventoRepository;
import it.ids.mad.ProgettoIDS.repository.ProdottoRepository;
import it.ids.mad.ProgettoIDS.repository.VenditoreRepository;

@Controller
public class WebController {

    @Autowired
    private ProdottoRepository prodottoRepository;
    @Autowired
    private ArticoloRepository articoloRepository;
    @Autowired
    private EventoRepository eventoRepository;
    @Autowired
    private VenditoreRepository venditoreRepository;

    @GetMapping("/")
    public String index(@RequestParam(value = "q", required = false) String q, Model model) {
        model.addAttribute("prodotti", prodottoRepository.findByApprovatoTrue());
        java.util.List<Articolo> articoli = (q == null || q.isBlank())
                ? articoloRepository.findByApprovatoTrue()
                : articoloRepository.findByApprovatoTrueAndNomeContainingIgnoreCase(q.trim());
        model.addAttribute("articoli", articoli);
        model.addAttribute("q", q == null ? "" : q);
        return "web/index";
    }


    @GetMapping("/articoli")
    public String articoli(Model model) {
        model.addAttribute("articoli", articoloRepository.findByApprovatoTrue());
        return "web/articoli";
    }


    @GetMapping("/articolo/{tipo}/{id}")
    public String articoloDettaglioUnificato(@PathVariable String tipo, @PathVariable Long id, Model model) {
        Articolo articolo = articoloRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid articolo Id:" + id));
        boolean isProdotto = articolo instanceof Prodotto;
        boolean isMateriaPrima = articolo instanceof MateriaPrima;
        boolean isPacchetto = articolo instanceof Pacchetto;

        model.addAttribute("articolo", articolo);
        model.addAttribute("isProdotto", isProdotto);
        model.addAttribute("isMateriaPrima", isMateriaPrima);
        model.addAttribute("isPacchetto", isPacchetto);
        if (isProdotto) {
            model.addAttribute("materiePrime", ((Prodotto) articolo).getMateriePrime());
        }
        if (isMateriaPrima) {
            model.addAttribute("prodottiAssociati", ((MateriaPrima) articolo).getProdotti());
        }
        if (isPacchetto) {
            model.addAttribute("prodottiPacchetto", ((Pacchetto) articolo).getProdotti());
        }
        return "web/articolo-dettaglio";
    }

    @GetMapping("/aziende")
    public String aziende(Model model) {
        model.addAttribute("aziende", venditoreRepository.findAll());
        return "web/aziende";
    }

    @GetMapping("/azienda/{id}")
    public String aziendaDettaglio(@PathVariable Long id, Model model) {
        Venditore azienda = venditoreRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid azienda Id:" + id));
        model.addAttribute("azienda", azienda);
        model.addAttribute("articoli", articoloRepository.findByApprovatoTrueAndAziendaOrigineId(id));
        return "web/azienda-dettaglio";
    }

    @GetMapping("/eventi")
    public String eventi(Model model) {
        java.time.LocalDateTime now = java.time.LocalDateTime.now();
        java.util.List<it.ids.mad.ProgettoIDS.model.Evento> eventi = eventoRepository.findByApprovatoTrue()
                .stream()
                .filter(e -> e.getData() != null && e.getData().isAfter(now))
                .toList();
        model.addAttribute("eventi", eventi);
        return "web/eventi";
    }

    @GetMapping("/evento/{id}")
    public String eventoDettaglio(@PathVariable Long id, Model model) {
        Evento evento = eventoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid evento Id:" + id));
        model.addAttribute("evento", evento);
        return "web/evento-dettaglio";
    }

    @GetMapping("/register")
    public String register() {
        return "web/register";
    }

    @GetMapping("/login")
    public String login() {
        return "web/login";
    }

    @GetMapping("/carrello")
    public String carrello() {
        return "web/carrello";
    }

    @GetMapping("/ordini")
    public String ordini() {
        return "web/ordini";
    }
}
