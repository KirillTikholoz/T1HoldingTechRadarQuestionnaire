package utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.example.utils.JwtTokenUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Date;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {JwtTokenUtils.class})
@TestPropertySource(properties = "jwt.secret-key=aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa")
public class JwtTokenUtilsTest {
    @Autowired
    private JwtTokenUtils jwtTokenUtils;
    String validToken;
    @BeforeEach
    public void setUp(){
        validToken =
                Jwts
                        .builder()
                        .subject("1")
                        .expiration(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000))
                        .claim("role", "ADMIN")
                        .signWith(jwtTokenUtils.getSignKey())
                        .compact();
    }
    @Test
    public void successfulGetId(){
        Assertions.assertEquals(1L, jwtTokenUtils.getId(validToken));
    }

    @Test
    public void successfulGetRole(){
        Assertions.assertEquals("ADMIN", jwtTokenUtils.getRole(validToken));
    }

    @Test
    public void tokenThrowSignatureException(){
        String notValidKey = "bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb";
        String notValidToken =
                Jwts
                        .builder()
                        .subject("1")
                        .expiration(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000))
                        .claim("role", "ADMIN")
                        .signWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(notValidKey)))
                        .compact();

        Assertions.assertThrows(SignatureException.class, () ->
                jwtTokenUtils.getId(notValidToken));
    }
}
