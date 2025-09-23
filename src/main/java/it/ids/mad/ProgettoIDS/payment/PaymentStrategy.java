package it.ids.mad.ProgettoIDS.payment;

import java.math.BigDecimal;

public interface PaymentStrategy {
    void process(String numeroCarta, String scadenza, String cvv, BigDecimal importo);
}
