package io.github.ardoco.textproviderjson.converter;

import io.github.ardoco.textproviderjson.TestUtil;
import io.github.ardoco.textproviderjson.dto.TextDTO;
import io.github.ardoco.textproviderjson.error.NotConvertableException;
import io.github.ardoco.textproviderjson.textobject.TextImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

class ObjectToDtoConverterTest {

    @Test
    void testConvertTextToDTO() throws NotConvertableException, IOException {
        ObjectToDtoConverter converter = new ObjectToDtoConverter();

        // convert null Text
        Assertions.assertThrows(NotConvertableException.class, () -> converter.convertTextToDTO(null));

        // convert empty Text
        TextDTO expected1 = TestUtil.generateEmptyDTO();
        Assertions.assertDoesNotThrow(() -> converter.convertTextToDTO(new TextImpl()));
        Assertions.assertEquals(expected1, converter.convertTextToDTO(new TextImpl()));

        // convert Text with one sentence
        TextDTO expected2 = TestUtil.generateDefaultDTO();
        Assertions.assertDoesNotThrow(() -> converter.convertTextToDTO(TestUtil.generateDefaultText()));
        Assertions.assertEquals(expected2, converter.convertTextToDTO(TestUtil.generateDefaultText()));

        // convert Text with multiple sentences
        TextDTO expected3 = TestUtil.generateDTOWithMultipleSentences();
        Assertions.assertDoesNotThrow(() -> converter.convertTextToDTO(TestUtil.generateTextWithMultipleSentences()));
        Assertions.assertEquals(expected3, converter.convertTextToDTO(TestUtil.generateTextWithMultipleSentences()));

        // convert Text with incoming and outgoing dependencies
        TextDTO expected4 = TestUtil.generateTextDtoWithDependencies();
        Assertions.assertDoesNotThrow(() -> converter.convertTextToDTO(TestUtil.generateTextWithDependencies()));
        Assertions.assertEquals(expected4, converter.convertTextToDTO(TestUtil.generateTextWithDependencies()));


    }
}
