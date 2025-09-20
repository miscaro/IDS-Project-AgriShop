package it.ids.mad.ProgettoIDS.infrastructure;

import it.ids.mad.ProgettoIDS.model.Utente;

public interface NotificationObserver {
    void onNotify(Utente destinatario, String messaggio);
}
