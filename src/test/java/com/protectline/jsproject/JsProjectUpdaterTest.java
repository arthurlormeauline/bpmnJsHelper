package com.protectline.jsproject;

import com.protectline.files.FileUtil;
import com.protectline.jsproject.jsupdater.FunctionUpdater;
import com.protectline.jsproject.jsupdater.MainUpdater;
import com.protectline.jsproject.updatertemplate.JsUpdaterTemplate;
import com.protectline.util.AssertUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import static com.protectline.application.tojsproject.stub.StubBlock.getExpectedBlocks;
import static com.protectline.jsproject.updatertemplate.JsUpdaterTemplateUtil.readJsUpdaterTemplatesFromFile;
import static com.protectline.util.FileUtil.getResourcePath;

// WONT PASS
class JsProjectUpdaterTest {

    private JsProjectUpdater jsProjectUpdater;
    private FileUtil fileUtil;

    @BeforeEach
    void setup() throws URISyntaxException {
        var testDirectory = "tojsproject";
        fileUtil = new FileUtil(getResourcePath(JsProjectUpdaterTest.class, testDirectory));
        jsProjectUpdater = new JsProjectUpdater(fileUtil);
    }

    @Test
    void should_update_project_from_blocks() throws IOException {
        // Given
        var process = "simplify";
        var blocks = getExpectedBlocks();
        var updaters= getUpdaters();

        // When
        jsProjectUpdater.updateProject(process, blocks, updaters);

        // Then
        AssertUtil.assertJsProjectIsEqualToExpected(fileUtil, process);
    }

    private List<JsUpdater> getUpdaters() throws IOException {
        var templates=readJsUpdaterTemplatesFromFile(fileUtil);
        var mainUpdaterTemplate = templates.stream()
                .filter(template -> template.getName().equals("MAIN"))
                .findFirst()
                .get();
        var functionUpdaterTemplate = templates.stream()
                .filter(template -> template.getName().equals("FUNCTION"))
                .findFirst()
                .get();
        return List.of(new MainUpdater(mainUpdaterTemplate), new FunctionUpdater(functionUpdaterTemplate));
    }
}
