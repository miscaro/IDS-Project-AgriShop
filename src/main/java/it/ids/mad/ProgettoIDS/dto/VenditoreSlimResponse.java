package it.ids.mad.ProgettoIDS.dto;

public class VenditoreSlimResponse {
    private Long id;
    private String username;
    private String ragioneSociale;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getRagioneSociale() { return ragioneSociale; }
    public void setRagioneSociale(String ragioneSociale) { this.ragioneSociale = ragioneSociale; }
}
