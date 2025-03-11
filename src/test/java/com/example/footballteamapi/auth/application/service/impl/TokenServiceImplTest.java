package com.example.footballteamapi.auth.application.service.impl;

import com.example.footballteamapi.auth.application.service.InvalidTokenService;
import com.example.footballteamapi.auth.domain.model.Token;
import com.example.footballteamapi.auth.infrastructure.config.TokenConfigurationParameter;
import com.example.footballteamapi.base.AbstractBaseServiceTest;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.security.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Unit test class for {@link TokenServiceImpl}.
 * This test class validates the functionality of the token service,
 * which is responsible for generating, validating, and managing tokens.
 * It mocks the {@link TokenConfigurationParameter} and {@link InvalidTokenService}
 * to test the token-related logic without dependencies on the actual configuration or token invalidation logic.
 */
class TokenServiceImplTest extends AbstractBaseServiceTest {

    @InjectMocks
    private TokenServiceImpl tokenService;

    @Mock
    private TokenConfigurationParameter tokenConfigurationParameter;

    @Mock
    private InvalidTokenService invalidTokenService;

    @Test
    void testGenerateTokenWithoutRefreshToken() throws Exception {

        // Given
        Map<String, Object> claims = new HashMap<>();
        claims.put("USER_ID", "12345");

        // Generate a valid RSA key pair for testing
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        PrivateKey privateKey = keyPair.getPrivate();

        // When
        when(tokenConfigurationParameter.getAccessTokenExpireMinute()).thenReturn(60);
        when(tokenConfigurationParameter.getRefreshTokenExpireDay()).thenReturn(7);
        when(tokenConfigurationParameter.getPrivateKey()).thenReturn(privateKey);
        when(tokenConfigurationParameter.getIssuer()).thenReturn("issuer");

        // Then
        Token token = tokenService.generateToken(claims);

        assertNotNull(token, "Token should not be null");
        assertNotNull(token.getAccessToken(), "Access token should not be null");
        assertNotNull(token.getRefreshToken(), "Refresh token should not be null");

        // Verify
        verify(tokenConfigurationParameter).getAccessTokenExpireMinute();
        verify(tokenConfigurationParameter).getRefreshTokenExpireDay();
        verify(tokenConfigurationParameter, Mockito.times(2)).getPrivateKey();

    }

    @Test
    void testGenerateTokenWithRefreshToken() throws NoSuchAlgorithmException {

        // Given
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048); // Use an appropriate key size
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        PrivateKey privateKey = keyPair.getPrivate();
        PublicKey publicKey = keyPair.getPublic();

        String refreshToken = Jwts.builder()
                .id(UUID.randomUUID().toString())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000))
                .signWith(privateKey) // Use the private key for signing
                .compact();

        Map<String, Object> claims = new HashMap<>();
        claims.put("USER_ID", "12345");

        // When
        when(tokenConfigurationParameter.getPrivateKey()).thenReturn(privateKey);
        when(tokenConfigurationParameter.getPublicKey()).thenReturn(publicKey);
        when(tokenConfigurationParameter.getAccessTokenExpireMinute()).thenReturn(60);
        when(tokenConfigurationParameter.getIssuer()).thenReturn("issuer");
        doNothing().when(invalidTokenService).checkForInvalidityOfToken(anyString());


        // Then
        Token token = tokenService.generateToken(claims, refreshToken);

        assertNotNull(token);
        assertNotNull(token.getAccessToken());
        assertNotNull(token.getRefreshToken());
        assertNotNull(token.getAccessTokenExpiresAt());

        // Verify
        verify(tokenConfigurationParameter).getAccessTokenExpireMinute();
        verify(invalidTokenService).checkForInvalidityOfToken(anyString());

    }

    @Test
    void testGetClaims() throws NoSuchAlgorithmException {

        // Given
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048); // Use an appropriate key size
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        PrivateKey privateKey = keyPair.getPrivate();
        PublicKey publicKey = keyPair.getPublic();

        String jwt = Jwts.builder()
                .id(UUID.randomUUID().toString())
                .issuer("issuer")
                .subject("subject")
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 60 * 60 * 1000)) // 1 hour
                .signWith(privateKey)
                .compact();

        // When
        when(tokenConfigurationParameter.getPublicKey()).thenReturn(publicKey);

        // Then
        Jws<Claims> claims = tokenService.getClaims(jwt);

        assertNotNull(claims, "Claims should not be null");
        assertEquals("issuer", claims.getPayload().getIssuer(), "Issuer should match");
        assertEquals("subject", claims.getPayload().getSubject(), "Subject should match");

        // Verify
        verify(tokenConfigurationParameter).getPublicKey();

    }

    @Test
    void testGetPayload() throws NoSuchAlgorithmException {

        // Given
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048); // Use an appropriate key size
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        PrivateKey privateKey = keyPair.getPrivate();
        PublicKey publicKey = keyPair.getPublic();

        String jwt = Jwts.builder()
                .id(UUID.randomUUID().toString())
                .issuer("issuer")
                .subject("subject")
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 60 * 60 * 1000)) // 1 hour
                .signWith(privateKey)
                .compact();

        // When
        when(tokenConfigurationParameter.getPublicKey()).thenReturn(publicKey);

        // Then
        Claims payload = tokenService.getPayload(jwt);


        assertNotNull(payload, "Payload should not be null");
        assertEquals("issuer", payload.getIssuer(), "Issuer should match");
        assertEquals("subject", payload.getSubject(), "Subject should match");

        // Verify
        verify(tokenConfigurationParameter).getPublicKey();

    }

    @Test
    void testVerifyAndValidateSet() throws NoSuchAlgorithmException {

        // Given
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048); // Use an appropriate key size
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        PrivateKey privateKey = keyPair.getPrivate();
        PublicKey publicKey = keyPair.getPublic();

        String jwt1 = Jwts.builder()
                .id(UUID.randomUUID().toString())
                .issuer("issuer1")
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 60 * 60 * 1000)) // 1 hour
                .signWith(privateKey)
                .compact();

        String jwt2 = Jwts.builder()
                .id(UUID.randomUUID().toString())
                .issuer("issuer2")
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 60 * 60 * 1000)) // 1 hour
                .signWith(privateKey)
                .compact();

        Set<String> jwts = Set.of(jwt1, jwt2);

        // When
        when(tokenConfigurationParameter.getPublicKey()).thenReturn(publicKey);

        // Then
        assertDoesNotThrow(() -> tokenService.verifyAndValidate(jwts), "All tokens should be valid");

        // Verify
        verify(tokenConfigurationParameter, Mockito.times(2)).getPublicKey();

    }


}