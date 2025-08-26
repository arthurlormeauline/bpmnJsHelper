package com.protectline.tojsproject.blockbuilder;

import com.protectline.bpmndocument.model.camundaadapter.BpmnCamundaDocument;
import com.protectline.jsproject.model.block.Block;
import com.protectline.tojsproject.block.BlockBuilder;
import com.protectline.tojsproject.block.MainBlockBuilder;
import com.protectline.tojsproject.block.functionblock.FunctionBlockBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

class MainBlockBuilderTest {

    private MainBlockBuilder builder;
    private String testFolderName;
    private Path resourcePath;
    private Path workingdir;

    @BeforeEach
    void setup() throws URISyntaxException {
        builder = new MainBlockBuilder();
        testFolderName = "tests";
        resourcePath = Path.of(Objects.requireNonNull(
                MainBlockBuilderTest.class.getClassLoader().getResource(testFolderName)).toURI()).getParent();
        workingdir = resourcePath.resolve(testFolderName);
    }

    @ParameterizedTest
    @MethodSource("shouldExtractAllBlocksData")
    void should_extract_all_blocks(List<BlockBuilder> builders) {
        // Given
        var process = "simplify";
        var document = new BpmnCamundaDocument(workingdir, process);

        // When
        List<Block> actual = builder
                .registerSubBlockBuilders(builders)
                .getBlocks(document);

        // Then
    }

    private static Stream<Arguments> shouldExtractAllBlocksData() {
        var scripBuilder = new ArrayList<BlockBuilder>();
        scripBuilder.add(new FunctionBlockBuilder());
        return Stream.of(
                Arguments.of(scripBuilder)
        );
    }
}
