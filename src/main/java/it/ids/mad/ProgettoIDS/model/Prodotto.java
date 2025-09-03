package it.ids.mad.ProgettoIDS.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
@DiscriminatorValue("PRODOTTO")
public class Prodotto extends Articolo {
    private String certificazioneProdotto;
    private String processoTrasformazione;
    private String origineProdotto;

    @ManyToMany
    private List<MateriaPrima> materiePrime = new ArrayList<>();

    public String getCertificazioneProdotto() { return certificazioneProdotto; }
    public void setCertificazioneProdotto(String certificazioneProdotto) { this.certificazioneProdotto = certificazioneProdotto; }
    public String getProcessoTrasformazione() { return processoTrasformazione; }
    public void setProcessoTrasformazione(String processoTrasformazione) { this.processoTrasformazione = processoTrasformazione; }
    public String getOrigineProdotto() { return origineProdotto; }
    public void setOrigineProdotto(String origineProdotto) { this.origineProdotto = origineProdotto; }
    public List<MateriaPrima> getMateriePrime() { return materiePrime; }
    public void setMateriePrime(List<MateriaPrima> materiePrime) { this.materiePrime = materiePrime; }
}
