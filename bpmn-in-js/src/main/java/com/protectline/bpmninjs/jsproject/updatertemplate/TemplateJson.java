package com.protectline.bpmninjs.jsproject.updatertemplate;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class TemplateJson {
    @JsonProperty("name")
    private final String name;
    @JsonProperty("element")
    private final String element;
    @JsonProperty("template")
    private final String template;
    @JsonProperty("flag")
    private final String flag;

    @JsonCreator
    public TemplateJson(
            @JsonProperty("name") String name,
            @JsonProperty("element") String element,
            @JsonProperty("template") String template,
            @JsonProperty("flag") String flag) {
        this.name = name;
        this.element = element;
        this.template = template;
        this.flag = flag;
    }

    public JsUpdaterTemplate toJsUpdaterTemplate() {
        return new JsUpdaterTemplate(name, template, flag);
    }
    
    public TemplateForParser toTemplateForParser() {
        return new TemplateForParser(name, element, template);
    }
}
