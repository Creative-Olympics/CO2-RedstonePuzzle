package fr.syl2010.minecraft.CreativeRedstonePuzzle.serialisation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

/**
 * Annotation used to indicate that the annotated property shall be
 * deserialized to the map key of
 * the current object. Requires that the object is a deserialized map
 * value.
 * Note: This annotation is not a standard Jackson annotation. It will
 * only work if this is
 * explicitly enabled in the {@link ObjectMapper}.
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@JsonProperty(access = Access.WRITE_ONLY)
public @interface JsonMapKey {}
