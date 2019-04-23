package de.brockhausag.jedichat.auth.jwt;

import de.brockhausag.jedichat.auth.JediChatUserDetailsService;
import de.brockhausag.jedichat.auth.JediChatUserPrincipal;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class JwtProviderService {
    private static final String SECRET = "SECRET";
    private static final long VALID_IN_MS = 60 * 60 * 1000; // 1 hour

    private String encodedSecret;

    private JediChatUserDetailsService userDetailsService;

    @Autowired
    public JwtProviderService(JediChatUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @PostConstruct
    private void initSecret() {
        encodedSecret = Base64.getEncoder().encodeToString(SECRET.getBytes());
    }

    public String createToken(String nickname) {
        JediChatUserPrincipal principal = userDetailsService.loadByNickName(nickname);
        List<String> roles = principal.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.toList());

        Date now = new Date();
        Date validUntil = new Date(now.getTime() + VALID_IN_MS);

        return Jwts.builder()
            .setSubject(principal.getUsername())
            .claim("roles", roles)
            .setIssuedAt(now)
            .setExpiration(validUntil)
            .signWith(SignatureAlgorithm.HS256, encodedSecret)
            .compact();
    }

    public Optional<Authentication> getAuthentication(HttpServletRequest req) {
        String token = resolveToken(req);

        try {
            validateToken(token);
        } catch (JwtInvalidException e) {
            return Optional.empty();
        }

        String userName = Jwts.parser().setSigningKey(encodedSecret)
            .parseClaimsJws(token)
            .getBody()
            .getSubject();

        JediChatUserPrincipal principal = userDetailsService.loadByNickName(userName);

        UsernamePasswordAuthenticationToken result = new UsernamePasswordAuthenticationToken(userName, "", principal.getAuthorities());
        return Optional.of(result);
    }

    private String resolveToken(HttpServletRequest req) {
        String bearerToken = req.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring("Bearer ".length());
        }
        return null;
    }

    private void validateToken(String token) throws JwtInvalidException {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(encodedSecret).parseClaimsJws(token);
            if (claims.getBody().getExpiration().before(new Date())) {
                throw new JwtInvalidException("Expired or invalid JWT token");
            }
        } catch (JwtException | IllegalArgumentException e) {
            throw new JwtInvalidException("Expired or invalid JWT token");
        }
    }
}
