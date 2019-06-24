package sample.connection;

public @interface Connection {
    String uri();
    Class requestBody() default Object.class;
    String pathVariable() default "";

    String method() default "";

    String[] requestParam() default {};
}
