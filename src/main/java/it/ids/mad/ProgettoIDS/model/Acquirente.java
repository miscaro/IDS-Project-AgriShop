package it.ids.mad.ProgettoIDS.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

@Entity
@DiscriminatorValue("ACQUIRENTE")
public class Acquirente extends Utente {

    @OneToOne(mappedBy = "acquirente", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private Carrello carrello;

    @OneToMany(mappedBy = "acquirente", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Ordine> ordini = new ArrayList<>();

    public Acquirente() {
        super();
    }

    public Acquirente(String username, String email, String password, String ruolo) {
        super(username, email, password, ruolo);
    }

    public Acquirente(String username, String email, String password) {
        super(username, email, password, "ACQUIRENTE");
    }

    public Carrello getCarrello() {
        return carrello;
    }

    public void setCarrello(Carrello carrello) {
        this.carrello = carrello;
    }

    public List<Ordine> getOrdini() {
        return ordini;
    }

    public void setOrdini(List<Ordine> ordini) {
        this.ordini = ordini;
    }
}
