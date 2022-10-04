package com.github.donnebelin.umldoc.core;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        var entities = new JarParser().parse();
    }
}
