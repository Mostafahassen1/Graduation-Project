package com.codemeet.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.SecurityFilterChain;

import com.codemeet.service.UserService;
import com.codemeet.utils.dto.UserInfoResponse;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserService userService;


    public SecurityConfig(UserService userService  ) {
        this.userService = userService;
    }

    @Bean
    public <EntityUser> SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .anyRequest().authenticated()
            )
            .oauth2Login(oauth2 -> oauth2
                .successHandler((request, response, authentication) -> {

                    if (authentication.getPrincipal() instanceof OidcUser) {

                        OidcUser user = (OidcUser) authentication.getPrincipal();

                        String jwtToken = user.getIdToken().getTokenValue();
                        String name = user.getFullName();
                        String email = user.getEmail();
                        String uSerName = user.getName();
                        OidcIdToken idToken = user.getIdToken();
                        String nickNAme = user.getNickName();
                        String picture = user.getPicture();
                        String birthday = user.getBirthdate();
                        String firstName = user.getGivenName();
                        String LastName = user.getFamilyName();

                        System.out.println("email: ==>  " + email);
                        System.out.println("nickNAme: ==>  " + nickNAme);
                        System.out.println("picture: ==>  " + picture);
                        System.out.println("LastName: ==>  " + LastName);
                        System.out.println("firstName: ==>  " + firstName);


                        CurrentClient currentclient = new CurrentClient(nickNAme);

                        UserInfoResponse client ;
                        try{
                            client = userService.getUserByUsername(nickNAme);
                        }
                        catch (Exception e){
                            client = null;
                        }

                        if (client == null) {
                            userService.saveUserEntity(
                                    firstName,
                                    LastName,
                                    nickNAme,
                                    email,
                                    "123456",
                                    "123456",
                                    picture);

                        }



                        response.sendRedirect("http://localhost:4200/home");
                    }
                })
            );
        return http.build();
    }


}