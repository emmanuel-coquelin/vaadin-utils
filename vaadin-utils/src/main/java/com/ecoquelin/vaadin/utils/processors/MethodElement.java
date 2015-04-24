package com.ecoquelin.vaadin.utils.processors;

import javax.lang.model.element.ExecutableElement;

/**
 * Created by Emmanuel on 24/04/2015.
 */
public class MethodElement {
    private ExecutableElement method;
    private String value;

    public MethodElement(ExecutableElement method, String value){
        this.method = method;
        this.value = value;
    }

    public ExecutableElement getMethod(){
        return this.method;
    }

    public  String getValue(){
        return  this.value;
    }
}
