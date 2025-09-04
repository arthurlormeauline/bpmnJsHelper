package com.protectline.bpmninjs.application;

import com.protectline.bpmninjs.engine.files.FileUtil;
import com.protectline.bpmninjs.engine.mainfactory.TranslateUnitAbstractFactory;
import com.protectline.bpmninjs.engine.mainfactory.TranslateUnitFactoryProvider;
import com.protectline.bpmninjs.application.entrypoint.EntryPointTranslateUnitFactory;
import com.protectline.bpmninjs.application.function.FunctionTranslateUnitFactory;

import java.util.List;

public class DefaultTranslateUnitFactoryProvider implements TranslateUnitFactoryProvider {

    @Override
    public List<TranslateUnitAbstractFactory> getTranslateUnitFactories(FileUtil fileUtil) {
        return List.of(
            new EntryPointTranslateUnitFactory(fileUtil),
            new FunctionTranslateUnitFactory(fileUtil)
        );
    }
}
