package it.ids.mad.ProgettoIDS.model;

import java.math.BigDecimal;

import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.ManyToOne;


@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_articolo", discriminatorType = DiscriminatorType.STRING)
public abstract class Articolo {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String descrizione;
    private BigDecimal prezzo;
    private boolean approvato = false;
    private int quantita;
    @ManyToOne
    private Venditore aziendaOrigine;

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
    public Venditore getAziendaOrigine() { return aziendaOrigine; }
    public void setAziendaOrigine(Venditore aziendaOrigine) { this.aziendaOrigine = aziendaOrigine; }

    public String getTipo() {
        return getClass().getSimpleName();
    }

    public String getTipoSlug() {
        String simple = getTipo().toLowerCase();
        return switch(simple) {
            case "materiaprima" -> "materia-prima";
            default -> simple;
        };
    }
}
