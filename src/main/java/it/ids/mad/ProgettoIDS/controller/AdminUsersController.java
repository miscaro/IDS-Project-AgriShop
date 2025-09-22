package it.ids.mad.ProgettoIDS.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.ids.mad.ProgettoIDS.repository.UtenteRepository;

@RestController
@RequestMapping("/api/admin/users")
public class AdminUsersController {

    @Autowired
    private UtenteRepository utenteRepository;

    public static class UserSummary {
        public Long id;
        public String username;
        public String email;
        public String ruolo;
        public String tipoUtente;
        public boolean enabled;

        public UserSummary(Long id, String username, String email, String ruolo, boolean enabled) {
            this.id = id;
            this.username = username;
            this.email = email;
            this.ruolo = ruolo;
            this.tipoUtente = ruolo;
            this.enabled = enabled;
        }
    }

    @GetMapping("/pending")
    @PreAuthorize("hasAuthority('GESTOREPIATTAFORMA')")
    public List<UserSummary> getPendingUsers() {
        return utenteRepository.findByEnabled(false)
                .stream()
                .map(u -> new UserSummary(u.getId(), u.getUsername(), u.getEmail(), u.getRuolo(), u.isEnabled()))
                .collect(Collectors.toList());
    }

    @PostMapping("/approve/{id}")
    @PreAuthorize("hasAuthority('GESTOREPIATTAFORMA')")
    public ResponseEntity<?> approveUser(@PathVariable Long id) {
        return utenteRepository.findById(id)
                .map(u -> {
                    u.setEnabled(true);
                    utenteRepository.save(u);
                    return ResponseEntity.ok().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
