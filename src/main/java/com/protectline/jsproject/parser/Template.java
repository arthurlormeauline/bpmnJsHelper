package com.protectline.jsproject.parser;

public class Template {
    private String name;
    private String element;
    private String template;
    private String flag;
    
    public Template() {}
    
    public Template(String name, String element, String template, String flag) {
        this.name = name;
        this.element = element;
        this.template = template;
        this.flag = flag;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getElement() {
        return element;
    }
    
    public void setElement(String element) {
        this.element = element;
    }
    
    public String getTemplate() {
        return template;
    }
    
    public void setTemplate(String template) {
        this.template = template;
    }
    
    public String getFlag() {
        return flag;
    }
    
    public void setFlag(String flag) {
        this.flag = flag;
    }
}