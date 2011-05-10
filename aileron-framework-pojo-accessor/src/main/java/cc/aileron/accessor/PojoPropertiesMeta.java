package cc.aileron.accessor;

import java.lang.reflect.Type;
import java.util.List;

/**
 * @author Aileron
 * 
 * @param <T>
 */
public interface PojoPropertiesMeta<T>
{

    /**
     * @param target
     * @param name
     * @return object
     */
    Getter get(final T target, final String name);

    /**
     * @param method
     * @return keys
     */
    List<String> keys(final PojoAccessorMethod method);

    /**
     * @param target
     * @param name
     * @return accessor
     */
    Setter set(final T target, final String name);

}

/**
 * @author Aileron
 */
interface Accessor extends AccessorGetter, AccessorSetter
{
}

/**
 * @author Aileron
 */
interface AccessorGetter
{
    /**
     * @return {@link Type}
     */
    Type genericType();

    /**
     * @param target
     * @param key
     * @return value
     */
    Object get(Object target, String key);

    /**
     * @return {@link PojoPropertiesMetaCategory}
     */
    PojoPropertiesMetaCategory meta();

    /**
     * @return type
     */
    Class<?> resultType();
}

/**
 * @author Aileron
 */
interface AccessorMapSetter
{
    /**
     * @return type
     */
    Class<?> argumentType();

    /**
     * @return {@link Type}
     */
    Type genericType();

    /**
     * @return {@link PojoPropertiesMetaCategory}
     */
    PojoPropertiesMetaCategory meta();

    /**
     * @param target
     * @param key
     * @param value
     */
    void set(Object target, String key, Object value);
}

/**
 * @author Aileron
 */
interface AccessorSetter
{
    /**
     * @return type
     */
    Class<?> argumentType();

    /**
     * @return {@link Type}
     */
    Type genericType();

    /**
     * @return {@link PojoPropertiesMetaCategory}
     */
    PojoPropertiesMetaCategory meta();

    /**
     * @param target
     * @param value
     */
    void set(Object target, Object value);
}

interface Getter
{
    /**
     * @return {@link Type}
     */
    Type genericType();

    /**
     * @return value
     */
    Object get();

    /**
     * @return {@link PojoPropertiesMetaCategory}
     */
    PojoPropertiesMetaCategory meta();

    /**
     * @return type
     */
    Class<?> resultType();
}

/**
 * @author Aileron
 */
interface Setter
{
    /**
     * @return type
     */
    Class<?> argumentType();

    /**
     * @return {@link Type}
     */
    Type genericType();

    /**
     * @return {@link PojoPropertiesMetaCategory}
     */
    PojoPropertiesMetaCategory meta();

    /**
     * @param value
     */
    void set(Object value);
}