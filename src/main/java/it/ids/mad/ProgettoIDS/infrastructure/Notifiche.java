package it.ids.mad.ProgettoIDS.infrastructure;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.ids.mad.ProgettoIDS.model.Utente;

@Component
public class Notifiche {
    private final List<NotificationObserver> observers;

    @Autowired
    public Notifiche(List<NotificationObserver> observers) {
        this.observers = observers;
    }

    public void inviaNotifica(Utente destinatario, String messaggio) {
        for (NotificationObserver o : observers) {
            o.onNotify(destinatario, messaggio);
        }
    }
}
