package sample.connection;

public @interface Connection {
    String uri();
    String pathVariable() default "";
}
