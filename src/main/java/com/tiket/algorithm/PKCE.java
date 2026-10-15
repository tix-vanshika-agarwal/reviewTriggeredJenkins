package com.tiket.algorithm;

/**
 * Generates a PKCE (RFC&nbsp;7636) {@code code_verifier} / {@code code_challenge} pair for
 * OAuth&nbsp;2.0 Authorization Code with PKCE flows.
 *
 * <p>Implementations must produce a verifier that satisfies the RFC&nbsp;7636 length and charset
 * constraints (43–128 chars from the URL-safe alphabet, no padding) and a challenge that is the
 * base64url-no-padding SHA-256 hash of the verifier (i.e. {@code S256} method).
 */
public interface PKCE {

    /**
     * Generates a fresh, cryptographically random code verifier and the matching SHA-256 code
     * challenge. Each call returns an independent pair; callers must keep the verifier secret
     * until the token exchange.
     */
    PKCEModel generateCode();
}
