package com.protectline.bpmninjs.application.mainfactory;

import com.protectline.bpmninjs.application.files.FileUtil;

import java.util.List;

public interface TranslateUnitFactoryProvider {
    
    List<TranslateUnitAbstractFactory> getTranslateUnitFactories(FileUtil fileUtil);
}
