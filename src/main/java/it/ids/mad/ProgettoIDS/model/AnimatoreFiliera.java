package it.ids.mad.ProgettoIDS.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("ANIMATORE_FILIERA")
public class AnimatoreFiliera extends Utente {
    public AnimatoreFiliera() { super(); }
    public AnimatoreFiliera(String username, String email, String password, String ruolo) {
        super(username, email, password, ruolo);
    }
}
