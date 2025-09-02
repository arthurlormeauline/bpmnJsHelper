package com.protectline.bpmninjs.translateunitfactory.template;

import lombok.Getter;

@Getter
public class Template {
    private final String name;
    private final String template;
    private final String flag;
    private final String element;
    
    public Template(String name, String template, String flag, String element) {
        this.name = name;
        this.template = template;
        this.flag = flag;
        this.element = element;
    }
    
    public Template(String name, String template, String flag) {
        this.name = name;
        this.template = template;
        this.flag = flag;
        this.element = null;
    }
    
    // Constructeur pour compatibilité avec TemplateForParser (ordre : name, element, template)
    public Template(String name, String element, String template, boolean forParser) {
        this.name = name;
        this.template = template;
        this.flag = null;
        this.element = element;
    }
}
