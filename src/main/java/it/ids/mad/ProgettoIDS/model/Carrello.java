package it.ids.mad.ProgettoIDS.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Carrello {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "acquirente_id")
    @JsonIgnore
    private Acquirente acquirente;

    @OneToMany(mappedBy = "carrello", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ElementoCarrello> elementi = new ArrayList<>();

    private BigDecimal totale;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Acquirente getAcquirente() {
        return acquirente;
    }

    public void setAcquirente(Acquirente acquirente) {
        this.acquirente = acquirente;
    }

    public List<ElementoCarrello> getElementi() {
        return elementi;
    }

    public void setElementi(List<ElementoCarrello> elementi) {
        this.elementi = elementi;
    }

    public BigDecimal getTotale() {
        return totale;
    }

    public void setTotale(BigDecimal totale) {
        this.totale = totale;
    }
}
