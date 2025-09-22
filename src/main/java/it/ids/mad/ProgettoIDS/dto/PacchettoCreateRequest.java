package it.ids.mad.ProgettoIDS.dto;

import java.math.BigDecimal;
import java.util.List;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class PacchettoCreateRequest {

    @NotBlank
    @Size(max = 255)
    private String nome;

    @Size(max = 2000)
    private String descrizione;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = true)
    private BigDecimal prezzo;

    @NotNull
    @Positive
    private Integer quantita;

    @NotNull
    @Size(min = 2)
    private List<Long> prodottiIds;

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getDescrizione() { return descrizione; }
    public void setDescrizione(String descrizione) { this.descrizione = descrizione; }
    public BigDecimal getPrezzo() { return prezzo; }
    public void setPrezzo(BigDecimal prezzo) { this.prezzo = prezzo; }
    public Integer getQuantita() { return quantita; }
    public void setQuantita(Integer quantita) { this.quantita = quantita; }
    public List<Long> getProdottiIds() { return prodottiIds; }
    public void setProdottiIds(List<Long> prodottiIds) { this.prodottiIds = prodottiIds; }
}
