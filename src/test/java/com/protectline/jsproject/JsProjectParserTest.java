package com.protectline.jsproject;

import com.protectline.files.FileUtil;
import com.protectline.jsproject.parser.JsProjectParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class JsProjectParserTest {

    JsProjectParser jsProjectParser;

    @BeforeEach
    void setup() throws IOException, URISyntaxException {
        FileUtil files = new FileUtil(com.protectline.util.FileUtil.getResourcePath(JsProjectParserTest.class, "tobpmn"));
        jsProjectParser = new JsProjectParser(files);
    }


    @Test
    void should_parse_js_file() throws URISyntaxException, IOException {
        // Given
        FileUtil files =new FileUtil(com.protectline.util.FileUtil.getResourcePath(JsProjectParserTest.class, "tobpmn"));
        var content = files.getJsRunnerFileContent("tus.prc.actionCombine");

        // When
        var blocksFromProject = jsProjectParser.parseJsToBlocks(content);

        // Then
        assertThat(blocksFromProject.size()).isEqualTo(20);
    }

}
