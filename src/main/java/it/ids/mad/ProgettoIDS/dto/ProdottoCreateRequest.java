package it.ids.mad.ProgettoIDS.dto;

import java.math.BigDecimal;
import java.util.List;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class ProdottoCreateRequest {

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

    @Size(max = 255)
    private String certificazioneProdotto;

    @Size(max = 255)
    private String processoTrasformazione;

    @Size(max = 255)
    private String origineProdotto;

    // Id delle materie prime da associare
    private List<Long> materiePrimeIds;

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getDescrizione() { return descrizione; }
    public void setDescrizione(String descrizione) { this.descrizione = descrizione; }
    public BigDecimal getPrezzo() { return prezzo; }
    public void setPrezzo(BigDecimal prezzo) { this.prezzo = prezzo; }
    public Integer getQuantita() { return quantita; }
    public void setQuantita(Integer quantita) { this.quantita = quantita; }
    public String getCertificazioneProdotto() { return certificazioneProdotto; }
    public void setCertificazioneProdotto(String certificazioneProdotto) { this.certificazioneProdotto = certificazioneProdotto; }
    public String getProcessoTrasformazione() { return processoTrasformazione; }
    public void setProcessoTrasformazione(String processoTrasformazione) { this.processoTrasformazione = processoTrasformazione; }
    public String getOrigineProdotto() { return origineProdotto; }
    public void setOrigineProdotto(String origineProdotto) { this.origineProdotto = origineProdotto; }
    public List<Long> getMateriePrimeIds() { return materiePrimeIds; }
    public void setMateriePrimeIds(List<Long> materiePrimeIds) { this.materiePrimeIds = materiePrimeIds; }
}
