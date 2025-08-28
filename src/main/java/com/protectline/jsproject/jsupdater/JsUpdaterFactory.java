package com.protectline.jsproject.jsupdater;

import com.protectline.common.block.Block;
import com.protectline.common.block.BlockType;
import com.protectline.jsproject.JsUpdater;
import com.protectline.jsproject.updatertemplate.JsUpdaterTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class JsUpdaterFactory {

    private static JsUpdater getUpdater(BlockType jsUpdaterType, List<JsUpdaterTemplate> jsUpdaterTemplates) {
        switch (jsUpdaterType) {
            case FUNCTION:
                return new FunctionUpdater(getUpdater(jsUpdaterTemplates, "FUNCTION"));
            default:
                throw new IllegalArgumentException("Unknown js updater type : " + jsUpdaterType.toString());
        }
    }

    private static List<BlockType> getBlockTypeInOrder() {
        return List.of(BlockType.FUNCTION);
    }

    public static List<JsUpdater> getJsUpdaters(List<Block> blocks, List<JsUpdaterTemplate> jsUpdaterTemplates) {
        List updaters = new ArrayList<JsUpdater>();
        JsUpdaterTemplate mainUpdaterTemplate = getUpdater(jsUpdaterTemplates, "MAIN");
        updaters.add(new MainUpdater(mainUpdaterTemplate));
        getUpdatersBasedOnBlocks(blocks, updaters, jsUpdaterTemplates);
        return updaters;
    }

    private static JsUpdaterTemplate getUpdater(List<JsUpdaterTemplate> jsUpdaterTemplates, String updaterName) {
        try {
            return jsUpdaterTemplates.stream()
                    .filter(template -> template.getName().equals(updaterName))
                    .findFirst()
                    .get();
        } catch (Exception e) {
            System.out.println("No updater for type " + updaterName + " in template");
            throw e;
        }
    }

    private static void getUpdatersBasedOnBlocks(List<Block> blocks, List updaters, List<JsUpdaterTemplate> jsUpdaterTemplates) {
        var blockTypes = blocks.stream().map(block -> block.getType()).collect(Collectors.toSet());
        getBlockTypeInOrder()
                .forEach(type -> {
                    if (blockTypes.contains(type.name()))
                        updaters.add(JsUpdaterFactory.getUpdater(type, jsUpdaterTemplates));
                });
    }

}
