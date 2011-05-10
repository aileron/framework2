/**
 *
 */
package cc.aileron.container.model;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import cc.aileron.container.Injector;
import cc.aileron.container.Provider;

/**
 * @author aileron
 */
public interface BindingMap
{
    /**
     * @param <T>
     * @param type
     * @param implementType
     * @param annotation
     * @param scope
     * @return binding
     */
    <T> Binding<T> bindImplement(Type type, Class<? extends T> implementType,
            Annotation annotation, Class<? extends Annotation> scope);

    /**
     * @param <T>
     * @param type
     * @param instance
     * @param annotation
     */
    <T> void bindInstance(Type type, T instance, Annotation annotation);

    /**
     * @param <T>
     * @param type
     * @param to
     * @param annotation
     * @param scope
     * @return binding
     */
    <T> Binding<T> bindProviderImplement(Type type,
            Class<? extends Provider<T>> to, Annotation annotation,
            Class<? extends Annotation> scope);

    /**
     * @param <T>
     * @param type
     * @param provider
     * @param scope
     * @param annotation
     */
    <T> void bindProviderInstance(Type type, Provider<T> provider,
            Annotation annotation, Class<? extends Annotation> scope);

    /**
     * @param <T>
     * @param isProvider
     * @param category
     * @param type
     * @param annotation
     * @return binding
     */
    <T> Binding<T> getBinding(Type type, Annotation annotation);

    /**
     * @return {@link Injector}
     */
    Injector getInjector();
}
