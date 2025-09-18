package it.ids.mad.ProgettoIDS.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("VISITA")
public class Visita extends Evento {
    private String hostVisita;

    public String getHostVisita() { return hostVisita; }
    public void setHostVisita(String hostVisita) { this.hostVisita = hostVisita; }
}
