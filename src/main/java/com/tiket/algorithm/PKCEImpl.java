package com.tiket.algorithm;

import java.security.SecureRandom;

/**
 * Default {@link PKCE} implementation following RFC&nbsp;7636 with the {@code S256} challenge
 * method.
 *
 * <p>The verifier is 32 random bytes ({@code SecureRandom}) base64url-no-padding-encoded, which
 * yields a 43-character string — the minimum length permitted by the RFC. The challenge is the
 * base64url-no-padding SHA-256 hash of the verifier.
 *
 * <p>{@link SecureRandom} is thread-safe, so a single instance is reused across calls instead of
 * being constructed per-call (the original Android source allocated a new instance every time,
 * which is wasteful on the hot path).
 */
public final class PKCEImpl implements PKCE {

    private static final int VERIFIER_BYTE_LENGTH = 32;

    private final Algorithm algorithm = new Algorithm();
    private final SecureRandom secureRandom = new SecureRandom();

    @Override
    public PKCEModel generateCode() {
        String codeVerifier = generateCodeVerifier();
        return new PKCEModel(codeVerifier, generateCodeChallenge(codeVerifier));
    }

    private String generateCodeVerifier() {
        byte[] code = new byte[VERIFIER_BYTE_LENGTH];
        secureRandom.nextBytes(code);
        return algorithm.getBase64String(code);
    }

    private String generateCodeChallenge(String codeVerifier) {
        byte[] input = algorithm.getASCIIBytes(codeVerifier);
        byte[] signature = algorithm.getSHA256(input);
        return algorithm.getBase64String(signature);
    }
}
