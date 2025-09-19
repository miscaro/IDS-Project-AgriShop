package it.ids.mad.ProgettoIDS.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("DISTRIBUTORE_TIPICITA")
public class DistributoreTipicita extends Venditore {
    public DistributoreTipicita() {
        super();
    }

    public DistributoreTipicita(String username, String email, String password, String ruolo) {
        super(username, email, password, ruolo);
    }

    public DistributoreTipicita(String username, String email, String password, String ruolo, String ragioneSociale, String indirizzo, Double lat, Double lon) {
        super(username, email, password, ruolo, ragioneSociale, indirizzo, lat, lon);
    }
}
