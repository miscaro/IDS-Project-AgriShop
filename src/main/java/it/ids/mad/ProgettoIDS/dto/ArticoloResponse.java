package it.ids.mad.ProgettoIDS.dto;

import java.math.BigDecimal;

public class ArticoloResponse {
    private Long id;
    private String nome;
    private String descrizione;
    private BigDecimal prezzo;
    private boolean approvato;
    private int quantita;
    private String tipo;
    private VenditoreSlimResponse aziendaOrigine;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getDescrizione() { return descrizione; }
    public void setDescrizione(String descrizione) { this.descrizione = descrizione; }
    public BigDecimal getPrezzo() { return prezzo; }
    public void setPrezzo(BigDecimal prezzo) { this.prezzo = prezzo; }
    public boolean isApprovato() { return approvato; }
    public void setApprovato(boolean approvato) { this.approvato = approvato; }
    public int getQuantita() { return quantita; }
    public void setQuantita(int quantita) { this.quantita = quantita; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public VenditoreSlimResponse getAziendaOrigine() { return aziendaOrigine; }
    public void setAziendaOrigine(VenditoreSlimResponse aziendaOrigine) { this.aziendaOrigine = aziendaOrigine; }
}
