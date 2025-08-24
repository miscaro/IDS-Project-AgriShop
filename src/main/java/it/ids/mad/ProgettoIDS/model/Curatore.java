package it.ids.mad.ProgettoIDS.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("CURATORE")
public class Curatore extends Utente {
    public Curatore() {
        super();
    }

    public Curatore(String username, String email, String password, String ruolo) {
        super(username, email, password, ruolo);
    }
}
