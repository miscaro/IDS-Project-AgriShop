package it.ids.mad.ProgettoIDS.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;

@Entity
@DiscriminatorValue("MATERIA_PRIMA")
public class MateriaPrima extends Articolo {
    private String metodoColtivazione;
    private String certificazioneMP;

    @ManyToMany(mappedBy = "materiePrime")
    @JsonIgnore
    private List<Prodotto> prodotti = new ArrayList<>();

    public String getMetodoColtivazione() { return metodoColtivazione; }
    public void setMetodoColtivazione(String metodoColtivazione) { this.metodoColtivazione = metodoColtivazione; }
    public String getCertificazioneMP() { return certificazioneMP; }
    public void setCertificazioneMP(String certificazioneMP) { this.certificazioneMP = certificazioneMP; }
    public List<Prodotto> getProdotti() { return prodotti; }
    public void setProdotti(List<Prodotto> prodotti) { this.prodotti = prodotti; }
}
