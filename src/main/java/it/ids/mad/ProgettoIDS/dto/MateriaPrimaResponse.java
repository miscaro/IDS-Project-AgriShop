package it.ids.mad.ProgettoIDS.dto;

public class MateriaPrimaResponse extends ArticoloResponse {
    private String metodoColtivazione;
    private String certificazioneMP;
    public String getMetodoColtivazione() { return metodoColtivazione; }
    public void setMetodoColtivazione(String metodoColtivazione) { this.metodoColtivazione = metodoColtivazione; }
    public String getCertificazioneMP() { return certificazioneMP; }
    public void setCertificazioneMP(String certificazioneMP) { this.certificazioneMP = certificazioneMP; }
}
