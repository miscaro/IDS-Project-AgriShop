package it.ids.mad.ProgettoIDS.infrastructure;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.ids.mad.ProgettoIDS.exception.PaymentDeclinedException;
import it.ids.mad.ProgettoIDS.exception.PaymentGatewayException;
import it.ids.mad.ProgettoIDS.model.Utente;
import it.ids.mad.ProgettoIDS.payment.PaymentStrategy;

@Component
public class SistemaPagamento {
    @Autowired
    private PaymentStrategy paymentStrategy;
    @Autowired
    private Notifiche notifiche;

    public boolean processaPagamento(Utente utente, String numeroCarta, String scadenza, String cvv, BigDecimal importo) {
        try {
            paymentStrategy.process(numeroCarta, scadenza, cvv, importo);
            confermaPagamento(utente);
            return true;
        } catch (PaymentGatewayException e) {
            notificaErroreGenerico(utente);
            throw e;
        } catch (PaymentDeclinedException e) {
            notificaRifiuto(utente);
            throw e;
        }
    }

    public void notificaRifiuto(Utente utente) {
        if (utente != null) notifiche.inviaNotifica(utente, "Pagamento rifiutato");
    }

    public void notificaErroreGenerico(Utente utente) {
        if (utente != null) notifiche.inviaNotifica(utente, "Errore di pagamento");
    }

    public void confermaPagamento(Utente utente) {
        if (utente != null) notifiche.inviaNotifica(utente, "Pagamento confermato");
    }
}
