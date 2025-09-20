package it.ids.mad.ProgettoIDS.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.ids.mad.ProgettoIDS.model.Venditore;
import it.ids.mad.ProgettoIDS.repository.VenditoreRepository;
import it.ids.mad.ProgettoIDS.service.OsmService;

@RestController
@RequestMapping("/api/aziende")
public class AziendeController {
    @Autowired private VenditoreRepository venditoreRepository;
    @Autowired private OsmService osmService;

    @GetMapping("/public")
    public List<Venditore> publicAziende(){
        List<Venditore> all = venditoreRepository.findAll();
        all.forEach(v -> {
            if(v.getLat() == null || v.getLon() == null){
                String addr = v.getIndirizzo();
                if(addr != null && !addr.isBlank()){
                    double[] coords = osmService.getCoordinates(addr);
                    if(coords != null){ v.setLat(coords[0]); v.setLon(coords[1]); }
                }
            }
        });
        return all;
    }
}
