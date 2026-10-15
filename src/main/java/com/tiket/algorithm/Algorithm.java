package com.tiket.algorithm;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * Cryptographic primitives used to build a PKCE pair.
 *
 * <p>Package-private on purpose — this is an implementation detail of {@link PKCEImpl}. The JDK
 * counterparts of the original Android calls are:
 * <ul>
 *   <li>{@code Base64.URL_SAFE | NO_WRAP | NO_PADDING} → {@code Base64.getUrlEncoder().withoutPadding()}
 *       (the JDK url/basic encoders never wrap; only the MIME encoder does).</li>
 *   <li>{@code MessageDigest "SHA-256"} → unchanged; available on every JDK.</li>
 * </ul>
 */
final class Algorithm {

    static final String SHA_256 = "SHA-256";

    private static final Logger logger = LogManager.getLogger(Algorithm.class);

    /**
     * Encodes {@code source} using base64url without padding (RFC&nbsp;4648 §5). Equivalent to the
     * Android {@code URL_SAFE | NO_WRAP | NO_PADDING} flag combination.
     */
    String getBase64String(byte[] source) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(source);
    }

    /**
     * Returns the US-ASCII byte encoding of {@code value}. The PKCE spec mandates ASCII for the
     * verifier-to-challenge hashing input.
     */
    byte[] getASCIIBytes(String value) {
        return value.getBytes(StandardCharsets.US_ASCII);
    }

    /**
     * Computes the SHA-256 digest of {@code input}.
     *
     * @throws IllegalStateException if the JVM somehow lacks SHA-256 (should never happen on any
     *         supported runtime; required by the JCA spec).
     */
    byte[] getSHA256(byte[] input) {
        try {
            MessageDigest md = MessageDigest.getInstance(SHA_256);
            md.update(input, 0, input.length);
            return md.digest();
        } catch (NoSuchAlgorithmException e) {
            logger.error("Failed to get SHA-256 signature", e);
            throw new IllegalStateException("Failed to get SHA-256 signature", e);
        }
    }
}
