package it.ids.mad.ProgettoIDS.payment;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import it.ids.mad.ProgettoIDS.exception.PaymentDeclinedException;
import it.ids.mad.ProgettoIDS.exception.PaymentGatewayException;

@Component
public class DefaultPaymentStrategy implements PaymentStrategy {
    @Override
    public void process(String numeroCarta, String scadenza, String cvv, BigDecimal importo) {
        if (numeroCarta == null || !numeroCarta.matches("\\d{16}")) {
            throw new PaymentDeclinedException("Numero carta non valido");
        }
        if (cvv == null || !cvv.matches("\\d{3}")) {
            throw new PaymentDeclinedException("CVV non valido");
        }
        if (numeroCarta.startsWith("0")) {
            throw new PaymentGatewayException("Gateway non raggiungibile");
        }
        int lastDigit = Character.getNumericValue(numeroCarta.charAt(numeroCarta.length()-1));
        if (lastDigit % 2 != 0) {
            throw new PaymentDeclinedException("Transazione rifiutata per fondi insufficienti");
        }
    }
}
