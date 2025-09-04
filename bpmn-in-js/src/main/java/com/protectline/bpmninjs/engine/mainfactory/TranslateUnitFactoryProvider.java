package com.protectline.bpmninjs.engine.mainfactory;

import com.protectline.bpmninjs.engine.files.FileUtil;

import java.util.List;

public interface TranslateUnitFactoryProvider {
    
    List<TranslateUnitAbstractFactory> getTranslateUnitFactories(FileUtil fileUtil);
}
