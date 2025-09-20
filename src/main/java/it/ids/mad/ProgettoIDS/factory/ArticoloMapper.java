package it.ids.mad.ProgettoIDS.factory;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import it.ids.mad.ProgettoIDS.dto.ArticoloResponse;
import it.ids.mad.ProgettoIDS.dto.MateriaPrimaResponse;
import it.ids.mad.ProgettoIDS.dto.ProdottoResponse;
import it.ids.mad.ProgettoIDS.dto.ProdottoResponse.MateriaPrimaSimple;
import it.ids.mad.ProgettoIDS.dto.VenditoreSlimResponse;
import it.ids.mad.ProgettoIDS.model.Articolo;
import it.ids.mad.ProgettoIDS.model.MateriaPrima;
import it.ids.mad.ProgettoIDS.model.Prodotto;
import it.ids.mad.ProgettoIDS.model.Venditore;

@Component
public class ArticoloMapper {

    private VenditoreSlimResponse mapVenditore(Venditore v) {
        if (v == null) return null;
        VenditoreSlimResponse resp = new VenditoreSlimResponse();
        resp.setId(v.getId());
        resp.setUsername(v.getUsername());
        resp.setRagioneSociale(v.getRagioneSociale());
        return resp;
    }

    public ArticoloResponse toResponse(Articolo articolo) {
        if (articolo instanceof Prodotto p) {
            return toResponse(p);
        } else if (articolo instanceof MateriaPrima m) {
            return toResponse(m);
        }
        ArticoloResponse base = new ArticoloResponse();
        base.setId(articolo.getId());
        base.setNome(articolo.getNome());
        base.setDescrizione(articolo.getDescrizione());
        base.setPrezzo(articolo.getPrezzo());
        base.setApprovato(articolo.isApprovato());
        base.setQuantita(articolo.getQuantita());
        base.setTipo(articolo.getTipo());
        base.setAziendaOrigine(mapVenditore(articolo.getAziendaOrigine()));
        return base;
    }

    public ProdottoResponse toResponse(Prodotto prodotto) {
        ProdottoResponse resp = new ProdottoResponse();
        resp.setId(prodotto.getId());
        resp.setNome(prodotto.getNome());
        resp.setDescrizione(prodotto.getDescrizione());
        resp.setPrezzo(prodotto.getPrezzo());
        resp.setApprovato(prodotto.isApprovato());
        resp.setQuantita(prodotto.getQuantita());
        resp.setTipo(prodotto.getTipo());
        resp.setAziendaOrigine(mapVenditore(prodotto.getAziendaOrigine()));
        resp.setCertificazioneProdotto(prodotto.getCertificazioneProdotto());
        resp.setProcessoTrasformazione(prodotto.getProcessoTrasformazione());
        resp.setOrigineProdotto(prodotto.getOrigineProdotto());
        if (prodotto.getMateriePrime() != null) {
            List<MateriaPrimaSimple> list = prodotto.getMateriePrime().stream().map(mp -> {
                MateriaPrimaSimple s = new MateriaPrimaSimple();
                s.setId(mp.getId());
                s.setNome(mp.getNome());
                return s;
            }).collect(Collectors.toList());
            resp.setMateriePrime(list);
        }
        return resp;
    }

    public MateriaPrimaResponse toResponse(MateriaPrima mp) {
        MateriaPrimaResponse resp = new MateriaPrimaResponse();
        resp.setId(mp.getId());
        resp.setNome(mp.getNome());
        resp.setDescrizione(mp.getDescrizione());
        resp.setPrezzo(mp.getPrezzo());
        resp.setApprovato(mp.isApprovato());
        resp.setQuantita(mp.getQuantita());
        resp.setTipo(mp.getTipo());
        resp.setAziendaOrigine(mapVenditore(mp.getAziendaOrigine()));
        resp.setMetodoColtivazione(mp.getMetodoColtivazione());
        resp.setCertificazioneMP(mp.getCertificazioneMP());
        return resp;
    }

    public List<ArticoloResponse> toResponseArticoli(List<? extends Articolo> list) {
        return list.stream().map(this::toResponse).collect(Collectors.toList());
    }

    public List<ProdottoResponse> toResponseProdotti(List<Prodotto> list) {
        return list.stream().map(this::toResponse).collect(Collectors.toList());
    }

    public List<MateriaPrimaResponse> toResponseMateriePrime(List<MateriaPrima> list) {
        return list.stream().map(this::toResponse).collect(Collectors.toList());
    }
}
