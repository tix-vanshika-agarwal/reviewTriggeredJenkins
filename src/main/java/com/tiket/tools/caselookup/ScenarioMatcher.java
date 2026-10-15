package com.tiket.tools.caselookup;

import com.tiket.tools.caselookup.LookupModels.CaseMeta;

import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * Maps a Klov scenario name onto the TestRail case it belongs to.
 *
 * <p>API runs record the scenario name from the test-data sheet, not the TestRail id, so
 * the only link back to a case is its title. Near-identical siblings are the norm
 * ("... orderId is invalid" next to "... orderId is empty"), so a loose match would
 * happily attribute a run to the wrong scenario. The matcher therefore prefers an exact
 * normalised hit and otherwise demands both a high similarity and a clear gap to the
 * runner-up; anything less is reported as unmatched rather than guessed.
 */
final class ScenarioMatcher {

    private static final double MIN_SCORE = 0.75;
    private static final double MIN_MARGIN = 0.08;

    private ScenarioMatcher() {
    }

    static CaseMeta best(String scenario, List<CaseMeta> candidates) {
        String normalised = normalise(scenario);
        if (normalised.isEmpty() || candidates.isEmpty()) {
            return null;
        }
        for (CaseMeta candidate : candidates) {
            if (normalised.equals(normalise(candidate.title()))) {
                return candidate;
            }
        }
        Set<String> scenarioTokens = tokens(normalised);
        CaseMeta leader = null;
        double leaderScore = 0;
        double runnerUpScore = 0;
        for (CaseMeta candidate : candidates) {
            double score = dice(scenarioTokens, tokens(normalise(candidate.title())));
            if (score > leaderScore) {
                runnerUpScore = leaderScore;
                leaderScore = score;
                leader = candidate;
            } else if (score > runnerUpScore) {
                runnerUpScore = score;
            }
        }
        if (leaderScore < MIN_SCORE || leaderScore - runnerUpScore < MIN_MARGIN) {
            return null;
        }
        return leader;
    }

    static String normalise(String value) {
        if (value == null) {
            return "";
        }
        return value.toLowerCase(Locale.ROOT)
                .replaceAll("[^a-z0-9]+", " ")
                .trim()
                .replaceAll("\\s+", " ");
    }

    private static Set<String> tokens(String normalised) {
        Set<String> tokens = new HashSet<>();
        for (String token : normalised.split(" ")) {
            if (!token.isBlank()) {
                tokens.add(token);
            }
        }
        return tokens;
    }

    private static double dice(Set<String> left, Set<String> right) {
        if (left.isEmpty() || right.isEmpty()) {
            return 0;
        }
        int shared = 0;
        for (String token : left) {
            if (right.contains(token)) {
                shared++;
            }
        }
        return 2.0 * shared / (left.size() + right.size());
    }
}
