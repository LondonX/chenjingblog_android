package com.londonx.lutil.entity;

/**
 * Created by london on 15/12/7.
 * an {@link LEntity} with a String public field named "name";
 */
public class NamedEntity extends LEntity {
    public String name;

    @Override
    public String toString() {
        return name;
    }
}
