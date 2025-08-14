package it.ids.mad.ProgettoIDS.model;

import jakarta.persistence.Entity;

@Entity
public abstract class Venditore extends Utente {
    private String ragioneSociale;
    private String partitaIVA;
    private String indirizzo;
    private String telefono;
    private Double lat;
    private Double lon;

    public Venditore() {
        super();
    }

    public Venditore(String username, String email, String password, String ruolo) {
        super(username, email, password, ruolo);
    }

    public Venditore(String username, String email, String password, String ruolo, String ragioneSociale, String indirizzo, Double lat, Double lon) {
        super(username, email, password, ruolo);
        this.ragioneSociale = ragioneSociale;
        this.indirizzo = indirizzo;
        this.lat = lat;
        this.lon = lon;
    }

    public Venditore(String username, String email, String password, String ragioneSociale, String partitaIVA, String indirizzo, String telefono) {
        super(username, email, password, null);
        this.ragioneSociale = ragioneSociale;
        this.partitaIVA = partitaIVA;
        this.indirizzo = indirizzo;
        this.telefono = telefono;
    }

    public String getRagioneSociale() { return ragioneSociale; }
    public void setRagioneSociale(String ragioneSociale) { this.ragioneSociale = ragioneSociale; }
    public String getPartitaIVA() { return partitaIVA; }
    public void setPartitaIVA(String partitaIVA) { this.partitaIVA = partitaIVA; }
    public String getIndirizzo() { return indirizzo; }
    public void setIndirizzo(String indirizzo) { this.indirizzo = indirizzo; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public Double getLat() { return lat; }
    public void setLat(Double lat) { this.lat = lat; }
    public Double getLon() { return lon; }
    public void setLon(Double lon) { this.lon = lon; }
}
