package it.ids.mad.ProgettoIDS.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("GESTORE_PIATTAFORMA")
public class GestorePiattaforma extends Utente {
    public GestorePiattaforma() { super(); }
    public GestorePiattaforma(String username, String email, String password, String ruolo) {
        super(username, email, password, ruolo);
    }
}
