package io.github.ardoco.textproviderjson.converter;

import edu.kit.kastel.mcse.ardoco.core.api.text.DependencyTag;
import io.github.ardoco.textproviderjson.dto.*;
import io.github.ardoco.textproviderjson.error.InvalidJsonException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

class JsonConverterTest {

    @Test
    void testValidateJson() throws IOException {
        Assertions.assertTrue(JsonConverter.validateJson(Files.readString(Path.of("./src/test/resources/valid-example-text.json"))));
        Assertions.assertFalse(JsonConverter.validateJson(Files.readString(Path.of("./src/test/resources/invalid-example-text.json"))));
    }

    @Test
    void testToJsonString() throws IOException, InvalidJsonException {
        // invalid text
        TextDTO invalidText = new TextDTO();
        Assertions.assertThrows(InvalidJsonException.class, () -> JsonConverter.toJsonString(invalidText));

        // valid text
        TextDTO validText = getValidTextDtoExample();
        Assertions.assertDoesNotThrow(() -> JsonConverter.toJsonString(validText));
        String generatedJson = JsonConverter.toJsonString(validText);
        Assertions.assertDoesNotThrow(() -> JsonConverter.fromJsonString(generatedJson));
        Assertions.assertEquals(validText, JsonConverter.fromJsonString(generatedJson));
    }

    @Test
    void testFromJsonString() throws IOException, InvalidJsonException {
        // invalid json
        String invalidJsonText = Files.readString(Path.of("./src/test/resources/invalid-example-text.json"));
        Assertions.assertThrows(InvalidJsonException.class, () -> JsonConverter.fromJsonString(invalidJsonText));

        // valid json
        String validJsonText = getValidJsonExample();
        Assertions.assertDoesNotThrow(() -> JsonConverter.fromJsonString(validJsonText));
        TextDTO generatedText = JsonConverter.fromJsonString(validJsonText);
        TextDTO expectedText = getValidTextDtoExample();
        Assertions.assertEquals(expectedText, generatedText);
    }

    private String getValidJsonExample() throws IOException {
        return Files.readString(Path.of("./src/test/resources/valid-example-text.json"));
    }

    private TextDTO getValidTextDtoExample() throws IOException {
        WordDTO expectedWord = new WordDTO();
        expectedWord.setId(1);
        expectedWord.setSentenceNo(1);
        expectedWord.setLemma("hello");
        expectedWord.setText("Hello");
        expectedWord.setPosTag(PosTag.forValue("UH"));

        OutgoingDependencyDTO expectedOutDep = new OutgoingDependencyDTO();
        expectedOutDep.setTargetWordId(1);
        expectedOutDep.setDependencyTag(DependencyTag.APPOS);
        List<OutgoingDependencyDTO> expectedOutList = new ArrayList<>();
        expectedOutList.add(expectedOutDep);
        expectedWord.setOutgoingDependencies(expectedOutList);

        IncomingDependencyDTO expectedInDep = new IncomingDependencyDTO();
        expectedInDep.setSourceWordId(1);
        expectedInDep.setDependencyTag(DependencyTag.APPOS);
        List<IncomingDependencyDTO> expectedInList = new ArrayList<>();
        expectedInList.add(expectedInDep);
        expectedWord.setIncomingDependencies(expectedInList);

        List<WordDTO> expectedWords = new ArrayList<>();
        expectedWords.add(expectedWord);

        SentenceDTO expectedSentence = new SentenceDTO();
        expectedSentence.setSentenceNo(1);
        expectedSentence.setText("Hello World!");
        expectedSentence.setConstituencyTree("(ROOT (FRAG (INTJ (UH Hello)) (NP (NNP World)) (. !)))");
        expectedSentence.setWords(expectedWords);

        List<SentenceDTO> expectedSentences = new ArrayList<>();
        expectedSentences.add(expectedSentence);

        TextDTO expectedText = new TextDTO();
        expectedText.setSentences(expectedSentences);
        return expectedText;
    }
}
