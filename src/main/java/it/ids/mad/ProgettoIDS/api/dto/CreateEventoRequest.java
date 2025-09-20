package it.ids.mad.ProgettoIDS.api.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CreateEventoRequest {
    @NotBlank
    private String nome;
    @NotNull
    private LocalDateTime data;
    @NotBlank
    private String luogo;
    private String descrizione;
    @NotBlank
    private String tipo;
    private String hostVisita;

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public LocalDateTime getData() { return data; }
    public void setData(LocalDateTime data) { this.data = data; }
    public String getLuogo() { return luogo; }
    public void setLuogo(String luogo) { this.luogo = luogo; }
    public String getDescrizione() { return descrizione; }
    public void setDescrizione(String descrizione) { this.descrizione = descrizione; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public String getHostVisita() { return hostVisita; }
    public void setHostVisita(String hostVisita) { this.hostVisita = hostVisita; }
}
