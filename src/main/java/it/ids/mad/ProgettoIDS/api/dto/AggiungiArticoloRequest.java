package it.ids.mad.ProgettoIDS.api.dto;

public class AggiungiArticoloRequest {
    private Long articoloId;
    private int quantita;

    public Long getArticoloId() {
        return articoloId;
    }

    public void setArticoloId(Long articoloId) {
        this.articoloId = articoloId;
    }

    public int getQuantita() {
        return quantita;
    }

    public void setQuantita(int quantita) {
        this.quantita = quantita;
    }
}
