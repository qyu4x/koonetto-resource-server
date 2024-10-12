package com.koonetto.koonetto_resource_server.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.*;
import java.util.stream.Collectors;

public class AuthServerGrantedAuthoritiesConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    @Override
    public Collection<GrantedAuthority> convert(Jwt source) {
        List<String> authorities = (List<String>) source.getClaims().get("roles");
        return authorities.stream()
                .map(authority -> "ROLE_" + authority)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

    }
}
