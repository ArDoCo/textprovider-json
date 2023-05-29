package io.github.ardoco.textproviderjson.converter;

import edu.kit.kastel.mcse.ardoco.core.api.data.text.Phrase;

import java.util.List;

/**
 * This utility class provides methods for the converter
 */
public final class ConverterUtil {

    private ConverterUtil() {
    }

    /**
     * gets the direct children of the given phrase
     * @param parentPhrase  the phrase
     * @return              the direct children of this phrase
     */
    public static List<Phrase> getChildPhrases(Phrase parentPhrase) {
        List<Phrase> subphrases = parentPhrase.getSubPhrases().toList();
        return subphrases.stream().filter(x -> isPhraseOnHighestLevel(subphrases, x)).toList();
    }

    private static boolean isPhraseOnHighestLevel(List<Phrase> subphrases, Phrase childPhrase) {
        for (Phrase subphrase: subphrases) {
            if (childPhrase.isSubPhraseOf(subphrase)) {
                return false;
            }
        }
        return true;
    }
}
