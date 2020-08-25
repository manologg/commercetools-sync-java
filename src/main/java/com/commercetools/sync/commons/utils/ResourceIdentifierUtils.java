package com.commercetools.sync.commons.utils;

import com.fasterxml.jackson.databind.JsonNode;
import io.sphere.sdk.models.Reference;
import io.sphere.sdk.models.Referenceable;
import io.sphere.sdk.models.ResourceIdentifier;
import io.sphere.sdk.models.WithKey;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;
import java.util.Optional;

import static java.util.Optional.ofNullable;

public final class ResourceIdentifierUtils {
    public static final String REFERENCE_TYPE_ID_FIELD = "typeId";
    public static final String REFERENCE_ID_FIELD = "id";
    /**
     * Given a {@link ResourceIdentifier}  the type {@code T}, if it is a {@link Reference}, it  returns the
     * key of the referenced Object, otherwise it will return the key of the {@link ResourceIdentifier}
     *
     * @param resourceIdentifier represents the resourceIdentifier, whose key should be return
     * @param <T>      type of the resourceIdentifier supplied
     * @return         The key of the resource identifier
     */

    public static <T extends ResourceIdentifier> String getKeyOfResourceIdentifier(final T resourceIdentifier) {
        String key = null;
        if (resourceIdentifier != null) {
            if (resourceIdentifier instanceof Reference) {
                key = ofNullable((Reference<WithKey>) resourceIdentifier)
                    .map(ref -> ref.getObj())
                    .map(o -> o.getKey()).orElse(null);
            } else {
                key = resourceIdentifier.getKey();
            }
        }
        return key;
    }






    /**
     * Given a {@link Referenceable} {@code resource} of the type {@code T}, if it is not null, this method applies the
     * {@link Referenceable#toResourceIdentifier()} method to return it as a {@link ResourceIdentifier} of
     * the type {@code T}. If it is {@code null}, this method returns {@code null}.
     *
     * @param resource represents the resource to return as a {@link ResourceIdentifier} if not {@code null}.
     * @param <T>      type of the resource supplied.
     * @param <S>      represents the type of the {@link ResourceIdentifier} returned.
     * @return the supplied resource in the as a {@link ResourceIdentifier} if not {@code null}. If it is {@code null},
     *         this method returns {@code null}.
     */
    @Nullable
    public static <T extends Referenceable<S>, S> ResourceIdentifier<S> toResourceIdentifierIfNotNull(
        @Nullable final T resource) {
        return ofNullable(resource)
            .map(Referenceable::toResourceIdentifier)
            .orElse(null);
    }

    /**
     * Given a {@link JsonNode} {@code referenceValue} which is the JSON representation of CTP Reference object,
     * this method checks if it is has a {@code typeId} with the value equal to {@code referenceTypeId}.
     *
     * @param referenceValue JSON representation of CTP reference object
     * @param referenceTypeId the typeId to check of the reference is of the same type or not.
     * @return true if the typeId field of the reference has the same value as {@code referenceTypeId}, otherwise,
     *         false.
     */
    public static boolean isReferenceOfType(@Nonnull final JsonNode referenceValue, final String referenceTypeId) {
        return getReferenceTypeId(referenceValue)
            .map(resolvedReferenceTypeId -> Objects.equals(resolvedReferenceTypeId, referenceTypeId))
            .orElse(false);
    }

    @Nonnull
    private static Optional<String> getReferenceTypeId(@Nonnull final JsonNode referenceValue) {
        final JsonNode typeId = referenceValue.get(REFERENCE_TYPE_ID_FIELD);
        return Optional.ofNullable(typeId).map(JsonNode::asText);
    }

    private ResourceIdentifierUtils() {
    }
}
