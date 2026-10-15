package com.tiket.algorithm;

/**
 * Immutable PKCE pair returned by {@link PKCE#generateCode()}.
 *
 * <p>{@code codeVerifier} is the high-entropy secret kept by the client; {@code codeChallenge} is
 * the base64url-no-padding SHA-256 hash of the verifier and is the value sent on the
 * {@code /authorize} request. Java equivalent of the Kotlin {@code data class PKCEModel}.
 */
public record PKCEModel(String codeVerifier, String codeChallenge) {
}
