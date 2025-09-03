package com.protectline.bpmninjs.application;

import com.protectline.bpmninjs.common.block.Block;

public class BlockWriterFactory {
    public WriteBlock getBlockWriter() {
        return new BlockWriter();
    }
}
