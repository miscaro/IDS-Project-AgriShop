package it.ids.mad.ProgettoIDS.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;

@Entity
@DiscriminatorValue("PACCHETTO")
public class Pacchetto extends Articolo {
    @ManyToMany
    private List<Prodotto> prodotti = new ArrayList<>();

    public List<Prodotto> getProdotti() { return prodotti; }
    public void setProdotti(List<Prodotto> prodotti) { this.prodotti = prodotti; }
}
