package com.dajudge.ymlgen.api.features;

import com.dajudge.ymlgen.api.util.ObjectBuilder;

import java.util.function.BiConsumer;
import java.util.function.Function;

public class FeatureOwner {
    private final BiConsumer<String, ApiFeature> featureSink;

    public FeatureOwner(final BiConsumer<String, ApiFeature> featureSink) {
        this.featureSink = featureSink;
    }

    public <K> FeatureOwner constant(final String featureName, final String itemName, final K value) {
        custom(featureName, new ConstantFeature<>(itemName, value));
        return this;
    }

    public FeatureOwner child(final String childName) {
        final BiConsumer<String, ApiFeature> subSink = (featureName, feature) ->
                custom(featureName, new SubFeature<>(childName, feature));
        return new FeatureOwner(subSink);
    }

    public FeatureOwner keyValuePairs(final String featureName, final String itemName) {
        custom(featureName, new KeyValueFeature(itemName));
        return this;
    }

    public <K> FeatureOwner simpleList(
            final String featureName,
            final String listName,
            final String itemName,
            final Class<K> clazz
    ) {
        custom(featureName, new SimpleListFeature<K>(listName, itemName));
        return this;
    }

    public FeatureOwner custom(final String featureName, final ApiFeature feature) {
        featureSink.accept(featureName, feature);
        return this;
    }

    public <K> FeatureOwner simpleValue(final String featureName, final String itemName, final K initValue, final Class<K> clazz) {
        custom(featureName, new SimpleValueFeature<>(itemName, initValue, clazz));
        return this;
    }

    public FeatureOwner builderList(final String featureName, final String listName, final BuilderFactory factory) {
        custom(featureName, new BuilderListFeature(listName, factory));
        return this;
    }

    public FeatureOwner builder(final String featureName, final String itemName, final BuilderFactory factory) {
        custom(featureName, new BuilderFeature(itemName, factory));
        return this;
    }

    public interface BuilderFactory extends Function<Object[], Builder> {
    }
}