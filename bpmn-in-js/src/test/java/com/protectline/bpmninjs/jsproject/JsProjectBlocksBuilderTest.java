package com.protectline.bpmninjs.jsproject;

import com.protectline.bpmninjs.files.FileUtil;
import com.protectline.bpmninjs.jsproject.blocksfactory.JsProjectBlocksBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class JsProjectBlocksBuilderTest {

    JsProjectBlocksBuilder jsProjectBlocksBuilder;

    @BeforeEach
    void setup() throws IOException, URISyntaxException {
        FileUtil files = new FileUtil(com.protectline.bpmninjs.util.FileUtil.getResourcePath(JsProjectBlocksBuilderTest.class, "tobpmn"));
        jsProjectBlocksBuilder = new JsProjectBlocksBuilder(files);
    }


    @Test
    void should_parse_js_file() throws URISyntaxException, IOException {
        // Given
        FileUtil files = new FileUtil(com.protectline.bpmninjs.util.FileUtil.getResourcePath(JsProjectBlocksBuilderTest.class, "tobpmn"));
        var content = files.getJsRunnerFileContent("tus.prc.actionCombine");

        // When
        var blocksFromProject = jsProjectBlocksBuilder.parseJsToBlocks(content);

        // Then
        assertThat(blocksFromProject.size()).isEqualTo(20);
    }

}
