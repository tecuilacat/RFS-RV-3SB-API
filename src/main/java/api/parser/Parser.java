package api.parser;

/**
 * Parses one type to another
 * @param <source> Source type
 * @param <target> Target type
 */
public interface Parser<source, target> {

    /**
     * Parses one object of a type to another object of the target type
     * @param source Sourceobject
     * @return Targetobject
     */
    target parse(source source);

}
