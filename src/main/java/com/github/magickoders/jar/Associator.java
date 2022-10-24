package com.github.magickoders.jar;

import com.github.forax.umldoc.core.AssociationDependency;
import com.github.forax.umldoc.core.Dependency;
import com.github.forax.umldoc.core.Entity;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Associator {
    private final Map<String, Entity> entities;
    private final List<Dependency> dependencies = new ArrayList<>();

    public Associator(List<Entity> entities) {
        this.entities = entities.stream()
//                .peek(System.out::println)
                .collect(Collectors.toUnmodifiableMap(entity -> entity.type().name(), Function.identity()));
//        System.out.println(this.entities);
    }

    public List<AssociationDependency> getDependencies() {
//        var entityNames = entities.stream().map(Entity::type).map(TypeInfo::name).collect(Collectors.toUnmodifiableSet());

        return entities.values().stream()
                .flatMap(entity -> {
                    var side1 = new AssociationDependency.Side(entity, Optional.empty(), false, AssociationDependency.Cardinality.ONLY_ONE);

                    return entity.fields().stream() // contains weird L and ZL
//                            .peek(System.out::println)
                            .filter(field -> entities.containsKey(field.typeInfo().name().replace("/", ".")))
                            .map(field -> entities.get(field.typeInfo().name().replace("/", ".")))
                            .map(entity1 -> new AssociationDependency.Side(entity1, Optional.empty(), false, AssociationDependency.Cardinality.ONLY_ONE))
                            .map(side -> new AssociationDependency(side1, side));
                })
                .toList();
    }
}
