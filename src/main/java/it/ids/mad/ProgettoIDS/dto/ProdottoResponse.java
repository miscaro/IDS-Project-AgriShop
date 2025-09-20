package it.ids.mad.ProgettoIDS.dto;

import java.util.List;

public class ProdottoResponse extends ArticoloResponse {
    private String certificazioneProdotto;
    private String processoTrasformazione;
    private String origineProdotto;
    private List<MateriaPrimaSimple> materiePrime;

    public String getCertificazioneProdotto() { return certificazioneProdotto; }
    public void setCertificazioneProdotto(String certificazioneProdotto) { this.certificazioneProdotto = certificazioneProdotto; }
    public String getProcessoTrasformazione() { return processoTrasformazione; }
    public void setProcessoTrasformazione(String processoTrasformazione) { this.processoTrasformazione = processoTrasformazione; }
    public String getOrigineProdotto() { return origineProdotto; }
    public void setOrigineProdotto(String origineProdotto) { this.origineProdotto = origineProdotto; }
    public List<MateriaPrimaSimple> getMateriePrime() { return materiePrime; }
    public void setMateriePrime(List<MateriaPrimaSimple> materiePrime) { this.materiePrime = materiePrime; }

    public static class MateriaPrimaSimple {
        private Long id;
        private String nome;
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getNome() { return nome; }
        public void setNome(String nome) { this.nome = nome; }
    }
}
