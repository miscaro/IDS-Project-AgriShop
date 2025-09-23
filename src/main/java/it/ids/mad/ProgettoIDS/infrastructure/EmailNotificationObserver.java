package it.ids.mad.ProgettoIDS.infrastructure;

import org.springframework.stereotype.Component;

import it.ids.mad.ProgettoIDS.model.Utente;

@Component
public class EmailNotificationObserver implements NotificationObserver {
    @Override
    public void onNotify(Utente destinatario, String messaggio) {
        // Placeholder: qui potremmo delegare a un provider email
    }
}
