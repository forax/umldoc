package com.github.pereiratostain.generator;

import com.github.forax.umldoc.core.Entity;

import java.io.IOException;
import java.util.List;

public interface Generator {
    void generate(List<Entity> entities) throws IOException;
}
