package it.ids.mad.ProgettoIDS.factory;

import org.springframework.stereotype.Component;

import it.ids.mad.ProgettoIDS.api.dto.CreateEventoRequest;
import it.ids.mad.ProgettoIDS.model.Evento;
import it.ids.mad.ProgettoIDS.model.Fiera;
import it.ids.mad.ProgettoIDS.model.Visita;

@Component
public class EventFactory {
    public Evento creaEvento(CreateEventoRequest req) {
        if (req == null || req.getTipo() == null) {
            throw new IllegalArgumentException("Tipo evento mancante");
        }
        String tipo = req.getTipo().trim().toUpperCase();
        switch (tipo) {
            case "FIERA":
                Fiera f = new Fiera();
                f.setNome(req.getNome());
                f.setDescrizione(req.getDescrizione());
                f.setLuogo(req.getLuogo());
                f.setData(req.getData());
                return f;
            case "VISITA":
                Visita v = new Visita();
                v.setNome(req.getNome());
                v.setDescrizione(req.getDescrizione());
                v.setLuogo(req.getLuogo());
                v.setData(req.getData());
                v.setHostVisita(req.getHostVisita());
                return v;
            default:
                throw new IllegalArgumentException("Tipo evento non valido. Usa FIERA o VISITA.");
        }
    }
}
