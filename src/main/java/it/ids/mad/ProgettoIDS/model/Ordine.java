package it.ids.mad.ProgettoIDS.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "ordini")
public class Ordine {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private LocalDate data;

    private BigDecimal totale;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "acquirente_id")
    @JsonIgnore
    private Acquirente acquirente;

    @OneToMany(mappedBy = "ordine", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ElementoOrdine> elementi = new ArrayList<>();

    private String indirizzoSpedizione;

    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public BigDecimal getTotale() {
        return totale;
    }

    public void setTotale(BigDecimal totale) {
        this.totale = totale;
    }

    public Acquirente getAcquirente() {
        return acquirente;
    }

    public void setAcquirente(Acquirente acquirente) {
        this.acquirente = acquirente;
    }

    public List<ElementoOrdine> getElementi() {
        return elementi;
    }

    public void setElementi(List<ElementoOrdine> elementi) {
        this.elementi = elementi;
    }

    public String getIndirizzoSpedizione() {
        return indirizzoSpedizione;
    }

    public void setIndirizzoSpedizione(String indirizzoSpedizione) {
        this.indirizzoSpedizione = indirizzoSpedizione;
    }
}
