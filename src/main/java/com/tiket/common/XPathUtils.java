package com.tiket.common;

import java.util.Locale;

/**
 * Helpers for composing XPath 1.0 expressions.
 *
 * <p>Locator XPaths frequently need to match user-facing text in a way that's tolerant
 * of UI casing changes (e.g. "All-In" vs "Paket All-in") and/or whitespace. XPath 1.0
 * lacks {@code lower-case()} / {@code upper-case()}, so we emulate case-insensitive
 * matching with the standard {@code translate(...)} idiom. Keep that idiom in one place
 * so locators stay consistent and easy to update.
 */
public final class XPathUtils {

    public static final String ASCII_UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String ASCII_LOWER = "abcdefghijklmnopqrstuvwxyz";

    private XPathUtils() {
    }

    /**
     * Wraps an XPath expression in a {@code translate(...)} call that maps ASCII A-Z to a-z.
     * The expression should evaluate to a string in XPath (e.g. {@code text()},
     * {@code normalize-space(.)}, {@code @class}).
     *
     * <p>Example:
     * <pre>{@code
     *   lowerCase("normalize-space(.)")
     *     -> "translate(normalize-space(.), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz')"
     * }</pre>
     *
     * @param xpathExpr an XPath sub-expression that evaluates to a string
     * @return a {@code translate(...)} call that lowercases the result of {@code xpathExpr}
     */
    public static String lowerCase(String xpathExpr) {
        return "translate(" + xpathExpr + ", '" + ASCII_UPPER + "', '" + ASCII_LOWER + "')";
    }

    /**
     * Builds a case-insensitive {@code contains(...)} predicate against an XPath sub-expression.
     * The needle is lowercased here; the DOM side is lowercased via {@link #lowerCase(String)}.
     *
     * <p>The Java-side lowercasing uses {@link Locale#ROOT} so it stays a pure ASCII A-Z to a-z
     * fold, matching what the XPath {@code translate(...)} call produces on the DOM side. Using
     * the JVM default locale would silently diverge under e.g. {@code tr-TR} (where {@code "I"}
     * lowercases to dotless {@code "ı"}, U+0131), producing a needle that the DOM-side fold
     * cannot match.
     *
     * <p>Example:
     * <pre>{@code
     *   containsIgnoreCase("normalize-space(.)", "All-In")
     *     -> "contains(translate(normalize-space(.), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'all-in')"
     * }</pre>
     *
     * @param xpathExpr an XPath sub-expression that evaluates to a string
     * @param needle    the substring to look for; cannot contain a single quote
     *                  (XPath 1.0 has no escape for {@code '} inside a string literal)
     * @return a {@code contains(...)} predicate suitable for inlining into an XPath
     * @throws IllegalArgumentException if {@code needle} is null or contains a single quote
     */
    public static String containsIgnoreCase(String xpathExpr, String needle) {
        if (needle == null) {
            throw new IllegalArgumentException("needle must not be null");
        }
        if (needle.indexOf('\'') >= 0) {
            throw new IllegalArgumentException(
                    "XPath 1.0 string literals cannot contain a single quote; sanitize input first: " + needle);
        }
        return "contains(" + lowerCase(xpathExpr) + ", '" + needle.toLowerCase(Locale.ROOT) + "')";
    }
}
