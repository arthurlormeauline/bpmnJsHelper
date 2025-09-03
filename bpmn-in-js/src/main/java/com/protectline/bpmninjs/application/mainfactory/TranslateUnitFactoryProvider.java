package com.protectline.bpmninjs.application.mainfactory;

import java.util.List;

public interface TranslateUnitFactoryProvider {
    
    List<TranslateUnitAbstractFactory> getTranslateUnitFactories(com.protectline.bpmninjs.files.FileUtil fileUtil);
}
