package it.ids.mad.ProgettoIDS.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_evento", discriminatorType = DiscriminatorType.STRING)
public abstract class Evento {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private LocalDateTime data;
    private String luogo;
    private String descrizione;
    private boolean approvato = false;

    @ManyToOne
    private Utente organizzatore;

    @ManyToMany
    private List<Utente> partecipanti = new ArrayList<>();

    private double lat;
    private double lon;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public LocalDateTime getData() { return data; }
    public void setData(LocalDateTime data) { this.data = data; }
    public String getLuogo() { return luogo; }
    public void setLuogo(String luogo) { this.luogo = luogo; }
    public String getDescrizione() { return descrizione; }
    public void setDescrizione(String descrizione) { this.descrizione = descrizione; }
    public boolean isApprovato() { return approvato; }
    public void setApprovato(boolean approvato) { this.approvato = approvato; }
    public Utente getOrganizzatore() { return organizzatore; }
    public void setOrganizzatore(Utente organizzatore) { this.organizzatore = organizzatore; }
    public List<Utente> getPartecipanti() { return partecipanti; }
    public void setPartecipanti(List<Utente> partecipanti) { this.partecipanti = partecipanti; }
    public double getLat() {
        return lat;
    }
    public void setLat(double lat) {
        this.lat = lat;
    }
    public double getLon() {
        return lon;
    }
    public void setLon(double lon) {
        this.lon = lon;
    }
}
