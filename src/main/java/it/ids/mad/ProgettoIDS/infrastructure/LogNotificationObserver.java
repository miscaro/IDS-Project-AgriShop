package it.ids.mad.ProgettoIDS.infrastructure;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import it.ids.mad.ProgettoIDS.model.Utente;

@Component
public class LogNotificationObserver implements NotificationObserver {
    private static final Logger log = LoggerFactory.getLogger(LogNotificationObserver.class);

    @Override
    public void onNotify(Utente destinatario, String messaggio) {
        String user = destinatario != null ? destinatario.getUsername() : "anonimo";
        log.info("Notifica a {}: {}", user, messaggio);
    }
}
