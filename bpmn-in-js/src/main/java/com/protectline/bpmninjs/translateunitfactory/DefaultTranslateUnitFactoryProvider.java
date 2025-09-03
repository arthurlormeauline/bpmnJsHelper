package com.protectline.bpmninjs.translateunitfactory;

import com.protectline.bpmninjs.application.mainfactory.TranslateUnitAbstractFactory;

import java.util.List;

public class DefaultTranslateUnitFactoryProvider implements TranslateUnitFactoryProvider {

    @Override
    public List<TranslateUnitAbstractFactory> getTranslateUnitFactories() {
        return List.of(
            new EntryPointTranslateUnitFactory(),
            new FunctionTranslateUnitFactory()
        );
    }
}
