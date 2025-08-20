package it.ids.mad.ProgettoIDS.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("TRASFORMATORE")
public class Trasformatore extends Venditore {
    public Trasformatore() { super(); }
    public Trasformatore(String username, String email, String password, String ruolo) {
        super(username, email, password, ruolo);
    }

    public Trasformatore(String username, String email, String password, String ruolo, String ragioneSociale, String indirizzo, Double lat, Double lon) {
        super(username, email, password, ruolo, ragioneSociale, indirizzo, lat, lon);
    }

    public Trasformatore(String username, String email, String password, String ragioneSociale, String partitaIva, String indirizzo, String telefono) {
        super(username, email, password, ragioneSociale, partitaIva, indirizzo, telefono);
    }
}
