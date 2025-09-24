package it.ids.mad.ProgettoIDS.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.ids.mad.ProgettoIDS.model.Ordine;
import it.ids.mad.ProgettoIDS.repository.OrdineRepository;

@Service
@Transactional
public class OrdineService {

    @Autowired
    private OrdineRepository ordineRepository;

    public List<Ordine> getOrdiniByAcquirente(Long acquirenteId) {
        return ordineRepository.findByAcquirenteId(acquirenteId);
    }

    public Ordine createOrdine(Ordine ordine, String indirizzo) {
        // Simulazione del processo di pagamento
        System.out.println("Processing payment...");
        // In un'applicazione reale, qui ci sarebbe l'integrazione con un gateway di pagamento.
        // Se il pagamento ha successo:
        ordine.setIndirizzoSpedizione(indirizzo);
        return ordineRepository.save(ordine);
    }
}
