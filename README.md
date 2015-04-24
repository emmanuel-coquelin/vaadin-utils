Set of utilities to use with Vaadin :

**I18nConstants annotation**

An annotation to use on an interface to allow a GWT-like definition of properties :
```java
@I18nConstants
public interface Demo{

  public String testWithoutKey(); // implementation will return testWithoutKey
  
  @Key("demo.test.with.key")
  public String testWithKey(); // implementation will return demo.test.with.key
  
}
```

