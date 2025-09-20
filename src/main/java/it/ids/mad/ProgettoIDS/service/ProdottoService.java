package it.ids.mad.ProgettoIDS.service;

import it.ids.mad.ProgettoIDS.model.Prodotto;
import it.ids.mad.ProgettoIDS.repository.ProdottoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProdottoService {

    @Autowired
    private ProdottoRepository prodottoRepository;

    public Prodotto approvaProdotto(Long prodottoId, Long curatoreId) {
        Prodotto prodotto = prodottoRepository.findById(prodottoId)
                .orElseThrow(() -> new RuntimeException("Prodotto non trovato"));
        prodotto.setApprovato(true);
        return prodottoRepository.save(prodotto);
    }
}
