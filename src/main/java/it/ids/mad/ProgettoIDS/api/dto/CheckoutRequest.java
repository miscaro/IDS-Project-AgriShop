package it.ids.mad.ProgettoIDS.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class CheckoutRequest {
    @NotBlank(message = "Nome obbligatorio")
    private String nome;

    @NotBlank(message = "Cognome obbligatorio")
    private String cognome;

    @NotBlank(message = "Indirizzo obbligatorio")
    private String indirizzo;

    @NotBlank(message = "Numero civico obbligatorio")
    private String numeroCivico;

    @Pattern(regexp = "\\d{5}", message = "CAP non valido")
    private String cap;

    @NotBlank(message = "Città obbligatoria")
    private String citta;

    @Pattern(regexp = "\\d{16}", message = "Numero carta non valido")
    private String numeroCarta;

    @Pattern(regexp = "(0[1-9]|1[0-2])\\/(\\d{2})", message = "Scadenza carta non valida (MM/YY)")
    private String scadenzaCarta;

    @Pattern(regexp = "\\d{3}", message = "CVV non valido")
    private String cvv;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public String getIndirizzo() {
        return indirizzo;
    }

    public void setIndirizzo(String indirizzo) {
        this.indirizzo = indirizzo;
    }

    public String getNumeroCivico() {
        return numeroCivico;
    }

    public void setNumeroCivico(String numeroCivico) {
        this.numeroCivico = numeroCivico;
    }

    public String getCap() {
        return cap;
    }

    public void setCap(String cap) {
        this.cap = cap;
    }

    public String getCitta() {
        return citta;
    }

    public void setCitta(String citta) {
        this.citta = citta;
    }

    public String getNumeroCarta() {
        return numeroCarta;
    }

    public void setNumeroCarta(String numeroCarta) {
        this.numeroCarta = numeroCarta;
    }

    public String getScadenzaCarta() {
        return scadenzaCarta;
    }

    public void setScadenzaCarta(String scadenzaCarta) {
        this.scadenzaCarta = scadenzaCarta;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }
}
