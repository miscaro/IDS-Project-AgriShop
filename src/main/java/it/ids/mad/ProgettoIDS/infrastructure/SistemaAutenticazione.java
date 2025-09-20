package it.ids.mad.ProgettoIDS.infrastructure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class SistemaAutenticazione {
    @Autowired
    private AuthenticationManager authenticationManager;

    public boolean verificaCredenziali(String username, String password) {
        Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        return auth != null && auth.isAuthenticated();
    }
}
