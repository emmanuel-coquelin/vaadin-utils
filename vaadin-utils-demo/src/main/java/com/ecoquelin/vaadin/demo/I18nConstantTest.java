package com.ecoquelin.vaadin.demo;

import com.ecoquelin.vaadin.utils.annotations.I18nConstants;
import com.ecoquelin.vaadin.utils.annotations.Key;

/**
 * Created by Emmanuel on 24/04/2015.
 */
@I18nConstants
public interface I18nConstantTest{

    @Key("hello")
    String  testWithAnnotation();

    @Key
    String testWithEmptyAnnotation();


    String testWithoutAnnotation();
}
