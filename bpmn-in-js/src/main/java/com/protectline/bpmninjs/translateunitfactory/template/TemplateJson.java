package com.protectline.bpmninjs.translateunitfactory.template;

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

    public Template toTemplate() {
        return new Template(name, template, flag, element);
    }
    
    public Template toTemplateForParser() {
        return new Template(name, element, template, true);
    }
}
