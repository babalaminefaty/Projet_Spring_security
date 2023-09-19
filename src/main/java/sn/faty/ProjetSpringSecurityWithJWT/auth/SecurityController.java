package sn.faty.ProjetSpringSecurityWithJWT.auth;

import com.nimbusds.jwt.JWTClaimsSet;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
public class SecurityController {

  private  AuthenticationManager authenticationManager ;

  private JwtEncoder jwtEncoder ;

    @GetMapping("/profile")
    public Authentication authentication(Authentication authentication){
        return  authentication ;
    }

    @PostMapping("/login")

    public ResponseEntity<Map<String,String>> login(@RequestBody UserRequest userRequest ){

        Authentication authentication= authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userRequest.username(), userRequest.password()));

        String role=authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(","));

        //Constrcution du payload

        JwtClaimsSet jwtClaimsSet= JwtClaimsSet.builder()
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plus(10, ChronoUnit.MINUTES))
                .subject(userRequest.username())
                .claim("scope", role)
                .build();

        JwtEncoderParameters jwtEncoderParameters = JwtEncoderParameters.from(JwsHeader.with(MacAlgorithm.HS512).build(), jwtClaimsSet);

          String token=jwtEncoder.encode(jwtEncoderParameters).getTokenValue();

          Map<String , String>  tokenjwt= new HashMap<>();

          tokenjwt.put("token****", token);


            return  ResponseEntity.ok(tokenjwt);
    }


}
