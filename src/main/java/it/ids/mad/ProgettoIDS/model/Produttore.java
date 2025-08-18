package it.ids.mad.ProgettoIDS.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("PRODUTTORE")
public class Produttore extends Venditore {
    public Produttore() { super(); }
    public Produttore(String username, String email, String password, String ruolo) {
        super(username, email, password, ruolo);
    }

    public Produttore(String username, String email, String password, String ruolo, String ragioneSociale, String indirizzo, Double lat, Double lon) {
        super(username, email, password, ruolo, ragioneSociale, indirizzo, lat, lon);
    }

    public Produttore(String username, String email, String password, String ragioneSociale, String partitaIva, String indirizzo, String telefono) {
        super(username, email, password, ragioneSociale, partitaIva, indirizzo, telefono);
    }
}
