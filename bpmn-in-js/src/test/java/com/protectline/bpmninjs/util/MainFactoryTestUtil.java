package com.protectline.bpmninjs.util;

import com.protectline.bpmninjs.application.files.FileUtil;
import com.protectline.bpmninjs.translateunitfactory.DefaultTranslateUnitFactoryProvider;
import com.protectline.bpmninjs.application.mainfactory.MainFactory;

import java.io.IOException;

public class MainFactoryTestUtil {
    
    public static MainFactory createWithDefaults(FileUtil fileUtil) throws IOException {
        return new MainFactory(fileUtil, new DefaultTranslateUnitFactoryProvider());
    }
}
