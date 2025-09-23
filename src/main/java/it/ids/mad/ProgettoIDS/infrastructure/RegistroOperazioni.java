package it.ids.mad.ProgettoIDS.infrastructure;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import it.ids.mad.ProgettoIDS.model.Utente;

@Component
public class RegistroOperazioni {
    private static final Logger log = LoggerFactory.getLogger(RegistroOperazioni.class);

    public void registraOperazione(String operazione, Utente utente) {
        String user = (utente != null ? utente.getUsername() : "anonimo");
        log.info("Operazione: {} by {}", operazione, user);
    }
}
