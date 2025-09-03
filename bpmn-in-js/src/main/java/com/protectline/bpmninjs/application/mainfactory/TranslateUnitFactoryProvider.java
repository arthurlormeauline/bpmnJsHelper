package com.protectline.bpmninjs.application.mainfactory;

import java.util.List;

public interface TranslateUnitFactoryProvider {
    
    /**
     * Returns the list of TranslateUnitAbstractFactory in the correct order
     */
    List<TranslateUnitAbstractFactory> getTranslateUnitFactories();
}
