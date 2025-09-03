package com.protectline.bpmninjs.translateunitfactory;

import com.protectline.bpmninjs.application.mainfactory.TranslateUnitAbstractFactory;

import java.util.List;

public interface TranslateUnitFactoryProvider {
    
    /**
     * Returns the list of TranslateUnitAbstractFactory in the correct order
     */
    List<TranslateUnitAbstractFactory> getTranslateUnitFactories();
}