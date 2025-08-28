package com.protectline.jsproject.updatertemplate;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class JsUpdaterTemplateJson {
    @JsonProperty("name")
    private final String name;
    @JsonProperty("template")
    private final String template;
    @JsonProperty("flag")
    private final String flag;

    public JsUpdaterTemplateJson(JsUpdaterTemplate jsUpdaterTemplate) {
        this.name = jsUpdaterTemplate.getName();
        this.template = jsUpdaterTemplate.getTemplate();
        this.flag = jsUpdaterTemplate.getFlag();
    }

    @JsonCreator
    public JsUpdaterTemplateJson(
            @JsonProperty("name") String name,
            @JsonProperty("template") String template,
            @JsonProperty("flag") String flag) {
        this.name = name;
        this.template = template;
        this.flag = flag;
    }

    public JsUpdaterTemplate toJsUpdaterTemplate() {
        return new JsUpdaterTemplate(name, template, flag);
    }
}
