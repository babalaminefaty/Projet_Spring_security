package sn.faty.ProjetSpringSecurityWithJWT.auth;
import com.nimbusds.jose.jwk.source.ImmutableSecret;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.spec.SecretKeySpec;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

  @Value("${secret.key}")

  private  String secretKey ;


    @Bean
    public PasswordEncoder passwordEncoder(){

        return  new BCryptPasswordEncoder();
    }

    @Bean
    public InMemoryUserDetailsManager inMemoryUserDetailsManager(){
        return  new InMemoryUserDetailsManager(
                User.withUsername("user").
                        password(passwordEncoder().encode("passer")).
                        roles("ADMIN").
                        authorities("ADMIN","USER").
                        build(),
                User.withUsername("user").
                        password(passwordEncoder().encode("passer")).
                        roles("USER").
                        username("faty.lamine").
                        authorities("USER").
                        build()
        );
    }

    @Bean

    public SecurityFilterChain filterchain(HttpSecurity httpSecurity) throws Exception {

        return  httpSecurity.sessionManagement(ssm -> ssm.sessionCreationPolicy(SessionCreationPolicy.STATELESS)).
                csrf(csrf -> csrf.disable()).
                authorizeHttpRequests(authorisation -> authorisation.requestMatchers("/auth/login").permitAll()).
                authorizeHttpRequests(authorisation -> authorisation.anyRequest().authenticated()).
                oauth2ResourceServer(oathRes -> oathRes.jwt(Customizer.withDefaults()))
                .build();
    }

    @Bean
public JwtEncoder jwtEncoder(){

        SecretKeySpec secretKeySpec=new SecretKeySpec(secretKey.getBytes(), "RSA");

        JWKSource<SecurityContext> immutablekey= new ImmutableSecret<>(secretKeySpec);

        return  new NimbusJwtEncoder(immutablekey);

}

@Bean
public JwtDecoder jwtDecoder(){

        SecretKeySpec secretKeySpec= new SecretKeySpec(secretKey.getBytes(),"RSA");

       return NimbusJwtDecoder.withSecretKey(secretKeySpec)

            .macAlgorithm(MacAlgorithm.HS512).build();
}

 @Bean
  public AuthenticationManager authenticationManager(UserDetailsService userDetailsService){

      DaoAuthenticationProvider daoAuthenticationProvider= new DaoAuthenticationProvider();

      daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());

      daoAuthenticationProvider.setUserDetailsService(userDetailsService);

      return new ProviderManager(daoAuthenticationProvider);

  }



}
