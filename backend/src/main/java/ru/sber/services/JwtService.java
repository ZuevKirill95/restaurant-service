package ru.sber.services;

import org.springframework.security.oauth2.jwt.Jwt;

public interface JwtService {

    String getSubClaim(Jwt jwt);

    String getEmailClaim(Jwt jwt);

    String getPhoneNumberClaim(Jwt jwt);

    String getPreferredUsernameClaim(Jwt jwt);
    String getFirstNameClaim(Jwt jwt);
    String getLastNameClaim(Jwt jwt);
}
