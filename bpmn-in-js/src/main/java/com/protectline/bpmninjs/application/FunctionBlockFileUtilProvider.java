package com.protectline.bpmninjs.application;

public class FunctionBlockFileUtilProvider implements BlockFileUtilProvider {

    @Override
    public BlockWriterFactory getBlockWriterFactory() {
        return new BlockWriterFactory();
    }
}
