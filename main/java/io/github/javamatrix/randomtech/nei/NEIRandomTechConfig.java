package io.github.javamatrix.randomtech.nei;

import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;
import io.github.javamatrix.randomtech.RandomTech;

public class NEIRandomTechConfig implements IConfigureNEI {

    @Override
    public String getName() {
        return RandomTech.name;
    }

    @Override
    public String getVersion() {
        return RandomTech.version;
    }

    @Override
    public void loadConfig() {
        API.registerRecipeHandler(new SynthesisMachineHandler());
        API.registerUsageHandler(new SynthesisMachineHandler());

        API.registerRecipeHandler(new SmithyHandler());
        API.registerUsageHandler(new SmithyHandler());
    }

}
