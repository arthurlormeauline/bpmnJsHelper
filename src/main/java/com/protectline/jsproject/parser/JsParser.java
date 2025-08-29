package com.protectline.jsproject.parser;

import com.protectline.common.block.Block;
import org.javatuples.Pair;

import java.util.List;

public interface JsParser {

    Pair<List<Block>, List<String>> parse(String content);
}
