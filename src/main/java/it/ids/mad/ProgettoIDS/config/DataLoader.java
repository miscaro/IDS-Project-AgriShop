package it.ids.mad.ProgettoIDS.config;

import java.math.BigDecimal;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import it.ids.mad.ProgettoIDS.model.Acquirente;
import it.ids.mad.ProgettoIDS.model.AnimatoreFiliera;
import it.ids.mad.ProgettoIDS.model.Curatore;
import it.ids.mad.ProgettoIDS.model.DistributoreTipicita;
import it.ids.mad.ProgettoIDS.model.GestorePiattaforma;
import it.ids.mad.ProgettoIDS.model.MateriaPrima;
import it.ids.mad.ProgettoIDS.model.Prodotto;
import it.ids.mad.ProgettoIDS.model.Produttore;
import it.ids.mad.ProgettoIDS.model.Trasformatore;
import it.ids.mad.ProgettoIDS.repository.MateriaPrimaRepository;
import it.ids.mad.ProgettoIDS.repository.ProdottoRepository;
import it.ids.mad.ProgettoIDS.repository.UtenteRepository;

@Configuration
public class DataLoader {

    @Bean
    CommandLineRunner seed(UtenteRepository utenteRepository, ProdottoRepository prodottoRepository, MateriaPrimaRepository materiaPrimaRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            // Creazione venditori già approvati
            
            // Produttore
            Produttore produttore = new Produttore();
            produttore.setUsername("produttore");
            produttore.setPassword(passwordEncoder.encode("produttore"));
            produttore.setEmail("produttore@test.com");
            produttore.setEnabled(true);
            produttore.setRuolo("PRODUTTORE");
            produttore.setRagioneSociale("Azienda Agricola Sole");
            produttore.setPartitaIVA("12345678901");
            produttore.setIndirizzo("Via dei Campi 123, Firenze");
            produttore.setTelefono("055-1234567");
            utenteRepository.save(produttore);

            // Trasformatore
            Trasformatore trasformatore = new Trasformatore();
            trasformatore.setUsername("trasformatore");
            trasformatore.setPassword(passwordEncoder.encode("trasformatore"));
            trasformatore.setEmail("trasformatore@test.com");
            trasformatore.setEnabled(true);
            trasformatore.setRuolo("TRASFORMATORE");
            trasformatore.setRagioneSociale("Caseificio Monti");
            trasformatore.setPartitaIVA("10987654321");
            trasformatore.setIndirizzo("Via del Latte 45, Siena");
            trasformatore.setTelefono("0577-987654");
            utenteRepository.save(trasformatore);

            // Distributore di Tipicità
            DistributoreTipicita distributore = new DistributoreTipicita();
            distributore.setUsername("distributore");
            distributore.setPassword(passwordEncoder.encode("distributore"));
            distributore.setEmail("distributore@test.com");
            distributore.setEnabled(true);
            distributore.setRuolo("DISTRIBUTORE_TIPICITA");
            distributore.setRagioneSociale("Tipicità Toscane Srl");
            distributore.setPartitaIVA("11223344556");
            distributore.setIndirizzo("Piazza del Mercato 10, Pisa");
            distributore.setTelefono("050-111222");
            utenteRepository.save(distributore);

            // Acquirente
            Acquirente acquirente = new Acquirente();
            acquirente.setUsername("acquirente");
            acquirente.setPassword(passwordEncoder.encode("acquirente"));
            acquirente.setEmail("acquirente@test.com");
            acquirente.setEnabled(true);
            acquirente.setRuolo("ACQUIRENTE");
            utenteRepository.save(acquirente);

            // Gestore Piattaforma
            GestorePiattaforma gestore = new GestorePiattaforma();
            gestore.setUsername("gestore");
            gestore.setPassword(passwordEncoder.encode("gestore"));
            gestore.setEmail("gestore@test.com");
            gestore.setEnabled(true);
            gestore.setRuolo("GESTOREPIATTAFORMA");
            utenteRepository.save(gestore);

            // Animatore Filiera
            AnimatoreFiliera animatore = new AnimatoreFiliera();
            animatore.setUsername("animatore");
            animatore.setPassword(passwordEncoder.encode("animatore"));
            animatore.setEmail("animatore@test.com");
            animatore.setEnabled(true);
            animatore.setRuolo("ANIMATORE_FILIERA");
            utenteRepository.save(animatore);

            // Curatore
            Curatore curatore = new Curatore();
            curatore.setUsername("curatore");
            curatore.setPassword(passwordEncoder.encode("curatore"));
            curatore.setEmail("curatore@test.com");
            curatore.setEnabled(true);
            curatore.setRuolo("CURATORE");
            utenteRepository.save(curatore);

            // Creazione materie prime di esempio (già approvate per il test)
            MateriaPrima latte = new MateriaPrima();
            latte.setNome("Latte Bio");
            latte.setDescrizione("Latte fresco da vacche al pascolo");
            latte.setMetodoColtivazione("Biologico");
            latte.setCertificazioneMP("BIO");
            latte.setApprovato(true);
            latte.setAziendaOrigine(produttore);
            latte.setPrezzo(new BigDecimal("1.50"));
            latte.setQuantita(500);
            materiaPrimaRepository.save(latte);

            MateriaPrima grano = new MateriaPrima();
            grano.setNome("Grano Duro");
            grano.setDescrizione("Grano duro per pasta");
            grano.setMetodoColtivazione("Tradizionale");
            grano.setCertificazioneMP("DOP");
            grano.setApprovato(true);
            grano.setAziendaOrigine(produttore);
            grano.setPrezzo(new BigDecimal("0.80"));
            grano.setQuantita(1000);
            materiaPrimaRepository.save(grano);

            MateriaPrima pomodori = new MateriaPrima();
            pomodori.setNome("Pomodori San Marzano");
            pomodori.setDescrizione("Pomodori San Marzano DOP");
            pomodori.setMetodoColtivazione("Tradizionale");
            pomodori.setCertificazioneMP("DOP");
            pomodori.setApprovato(true);
            pomodori.setAziendaOrigine(produttore);
            pomodori.setPrezzo(new BigDecimal("2.50"));
            pomodori.setQuantita(200);
            materiaPrimaRepository.save(pomodori);

            // Creazione prodotti
            Prodotto prodotto = new Prodotto();
            prodotto.setNome("Formaggio Stagionato");
            prodotto.setDescrizione("Formaggio a pasta dura, 24 mesi");
            prodotto.setPrezzo(new BigDecimal("25.50"));
            prodotto.setQuantita(100);
            prodotto.setApprovato(true);
            prodotto.setCertificazioneProdotto("DOP");
            prodottoRepository.save(prodotto);
        };
    }
}
