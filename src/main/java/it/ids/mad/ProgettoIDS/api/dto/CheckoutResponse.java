package it.ids.mad.ProgettoIDS.api.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class CheckoutResponse {
    private Long ordineId;
    private LocalDate data;
    private BigDecimal totale;
    private String statoPagamento;
    private String indirizzoSpedizione;

    public CheckoutResponse(Long ordineId, LocalDate data, BigDecimal totale, String statoPagamento, String indirizzoSpedizione) {
        this.ordineId = ordineId;
        this.data = data;
        this.totale = totale;
        this.statoPagamento = statoPagamento;
        this.indirizzoSpedizione = indirizzoSpedizione;
    }

    public Long getOrdineId() { return ordineId; }
    public LocalDate getData() { return data; }
    public BigDecimal getTotale() { return totale; }
    public String getStatoPagamento() { return statoPagamento; }
    public String getIndirizzoSpedizione() { return indirizzoSpedizione; }
}
