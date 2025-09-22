package it.ids.mad.ProgettoIDS.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.ids.mad.ProgettoIDS.dto.auth.JwtResponse;
import it.ids.mad.ProgettoIDS.dto.auth.LoginRequest;
import it.ids.mad.ProgettoIDS.dto.auth.MessageResponse;
import it.ids.mad.ProgettoIDS.dto.auth.SignupRequest;
import it.ids.mad.ProgettoIDS.model.Acquirente;
import it.ids.mad.ProgettoIDS.model.AnimatoreFiliera;
import it.ids.mad.ProgettoIDS.model.Curatore;
import it.ids.mad.ProgettoIDS.model.DistributoreTipicita;
import it.ids.mad.ProgettoIDS.model.GestorePiattaforma;
import it.ids.mad.ProgettoIDS.model.Produttore;
import it.ids.mad.ProgettoIDS.model.Trasformatore;
import it.ids.mad.ProgettoIDS.model.Utente;
import it.ids.mad.ProgettoIDS.repository.UtenteRepository;
import it.ids.mad.ProgettoIDS.security.JwtUtils;
import it.ids.mad.ProgettoIDS.security.UserDetailsImpl;
import jakarta.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AutenticazioneController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UtenteRepository utenteRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
    try {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String role = userDetails.getAuthorities().stream()
            .findFirst()
            .map(item -> item.getAuthority())
            .map(r -> r.startsWith("ROLE_") ? r : "ROLE_" + r)
            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));

        return ResponseEntity.ok(new JwtResponse(jwt,
            userDetails.getId(),
            userDetails.getUsername(),
            userDetails.getEmail(),
            role));
    } catch (DisabledException e) {
        return ResponseEntity.status(403).body(new MessageResponse("Account in attesa di approvazione"));
    } catch (BadCredentialsException e) {
        return ResponseEntity.status(401).body(new MessageResponse("Credenziali non valide"));
    }
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (utenteRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Errore: Username già in uso!"));
        }

        if (utenteRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Errore: Email già in uso!"));
        }

        Utente utente;
        if (signUpRequest.getTipoUtente().equalsIgnoreCase("GESTOREPIATTAFORMA")) {
            utente = new GestorePiattaforma(signUpRequest.getUsername(),
                    signUpRequest.getEmail(),
                    encoder.encode(signUpRequest.getPassword()), "GESTOREPIATTAFORMA");
            utente.setEnabled(true); // Abilita direttamente il gestore
        } else {
            switch (signUpRequest.getTipoUtente().toUpperCase()) {
                case "ACQUIRENTE":
                    utente = new Acquirente(signUpRequest.getUsername(), signUpRequest.getEmail(), encoder.encode(signUpRequest.getPassword()), "ACQUIRENTE");
                    break;
                case "PRODUTTORE":
                    utente = new Produttore(signUpRequest.getUsername(), signUpRequest.getEmail(), encoder.encode(signUpRequest.getPassword()), "PRODUTTORE", signUpRequest.getRagioneSociale(), signUpRequest.getIndirizzo(), signUpRequest.getLat(), signUpRequest.getLon());
                    break;
                case "TRASFORMATORE":
                    utente = new Trasformatore(signUpRequest.getUsername(), signUpRequest.getEmail(), encoder.encode(signUpRequest.getPassword()), "TRASFORMATORE", signUpRequest.getRagioneSociale(), signUpRequest.getIndirizzo(), signUpRequest.getLat(), signUpRequest.getLon());
                    break;
                case "DISTRIBUTORE":
                    utente = new DistributoreTipicita(signUpRequest.getUsername(), signUpRequest.getEmail(), encoder.encode(signUpRequest.getPassword()), "DISTRIBUTORE_TIPICITA", signUpRequest.getRagioneSociale(), signUpRequest.getIndirizzo(), signUpRequest.getLat(), signUpRequest.getLon());
                    break;
                case "ANIMATOREFILIERA":
                    utente = new AnimatoreFiliera(signUpRequest.getUsername(), signUpRequest.getEmail(), encoder.encode(signUpRequest.getPassword()), "ANIMATORE_FILIERA");
                    break;
                case "CURATORE":
                    utente = new Curatore(signUpRequest.getUsername(), signUpRequest.getEmail(), encoder.encode(signUpRequest.getPassword()), "CURATORE");
                    break;
                default:
                    return ResponseEntity.badRequest().body(new MessageResponse("Errore: Tipo utente non valido!"));
            }
            
            // Il CURATORE dovrebbe essere abilitato direttamente come il GESTOREPIATTAFORMA
            if (signUpRequest.getTipoUtente().equalsIgnoreCase("CURATORE")) {
                utente.setEnabled(true);
            } else {
                utente.setEnabled(false); // Disabilita gli altri utenti per approvazione
            }
        }


        utenteRepository.save(utente);

        return ResponseEntity.ok(new MessageResponse("Utente registrato con successo! In attesa di approvazione."));
    }

    @GetMapping("/me")
    public ResponseEntity<?> me() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof UserDetailsImpl)) {
            return ResponseEntity.status(401).body(new MessageResponse("Non autenticato"));
        }
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
    String role = userDetails.getAuthorities().stream()
        .findFirst()
        .map(item -> item.getAuthority())
        .map(r -> r.startsWith("ROLE_") ? r : "ROLE_" + r)
        .orElse("ROLE_USER");
        return ResponseEntity.ok(new JwtResponse(null,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                role));
    }
}
