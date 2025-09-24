package it.ids.mad.ProgettoIDS.service;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.ids.mad.ProgettoIDS.payment.PaymentStrategy;

@Service
public class PaymentService {

    private final PaymentStrategy paymentStrategy;

    @Autowired
    public PaymentService(PaymentStrategy paymentStrategy) {
        this.paymentStrategy = paymentStrategy;
    }

    public void process(String numeroCarta, String scadenza, String cvv, BigDecimal importo) {
        paymentStrategy.process(numeroCarta, scadenza, cvv, importo);
    }
}
