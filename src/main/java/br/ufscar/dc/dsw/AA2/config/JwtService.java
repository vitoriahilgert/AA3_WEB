package br.ufscar.dc.dsw.AA2.config;

import br.ufscar.dc.dsw.AA2.exceptions.ResourceNotFoundException;
import br.ufscar.dc.dsw.AA2.models.User;
import br.ufscar.dc.dsw.AA2.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class JwtService {
    @Autowired
    private JwtEncoder jwtEncoder;
    @Autowired
    private JwtDecoder jwtDecoder;
    @Autowired
    private UserRepository userRepository;

    public String generateToken(UUID id) {
        Instant now = Instant.now();
        int expiresIn = 60 * 60 * 24 * 7; // 7 days

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("my-api")
                .subject(id.toString())
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expiresIn))
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    public UUID getIdFromToken(String token) {
        token = token.replace("Bearer ", "");
        Jwt jwt = jwtDecoder.decode(token);
        return UUID.fromString(jwt.getSubject());
    }

    public User getUserFromToken(String token) {
        UUID id = getIdFromToken(token);
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id.toString()));
    }


}
