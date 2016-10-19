package io.github.javamatrix.randomtech.recipes;

import io.github.javamatrix.randomtech.Registration.RandomTechBlocks;
import io.github.javamatrix.randomtech.Registration.RandomTechItems;
import io.github.javamatrix.randomtech.items.hardtools.ItemHardenedMetalIngot;
import io.github.javamatrix.randomtech.tileentities.TileSynthesisMachine;
import io.github.javamatrix.randomtech.tileentities.TileSynthesisMachine.Slots;
import io.github.javamatrix.randomtech.util.WorldUtils;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class SynthesisRecipes {

    protected static SynthesisRecipes recipes = null;
    public List<ISynthesisRecipeHandler> listeners = new ArrayList<>();

    public SynthesisRecipes() {
        listeners.add(new DefaultSynthesisRecipes());
    }

    public static SynthesisRecipes instance() {
        if (recipes == null) {
            recipes = new SynthesisRecipes();
        }
        return recipes;
    }

    private boolean isClass(TileSynthesisMachine machine, Slots slot, Class c) {
        return machine.getStackInSlot(slot) != null && machine.getStackInSlot(slot).getItem().getClass().equals(c);
    }

    public RecipeResult getResult(TileSynthesisMachine machine) {
        for (ISynthesisRecipeHandler handler : listeners) {
            RecipeResult result = handler.getResult(machine);
            if (result != null) {
                return result;
            }
        }
        return null;
    }

    public interface ISynthesisRecipeHandler {
        RecipeResult getResult(TileSynthesisMachine machine);
    }

    public static class RecipeResult {
        ItemStack result = null;
        int time = 0;

        public RecipeResult(ItemStack result, int time) {
            this.result = result;
            this.time = time;
        }

        public ItemStack getResult() {
            return result;
        }

        /**
         * Gets the amount of work required.
         *
         * @return Amount of work required in RF ticks.
         */
        public int getWorkRequired() {
            return time;
        }
    }

    public class DefaultSynthesisRecipes implements ISynthesisRecipeHandler {

        public Map<ItemStack[], RecipeResult> simpleRecipes = new HashMap<>();

        DefaultSynthesisRecipes() {
            simpleRecipes.put(new ItemStack[]{
                    new ItemStack(Blocks.iron_block),
                    new ItemStack(Blocks.redstone_block)}, new RecipeResult(
                    new ItemStack(RandomTechBlocks.machineCore, 2), 8000));
            simpleRecipes.put(new ItemStack[]{new ItemStack(Blocks.dirt),
                    new ItemStack(Blocks.vine), new ItemStack(Blocks.dirt),
                    new ItemStack(Blocks.vine)}, new RecipeResult(
                    new ItemStack(Blocks.grass, 2), 16000));
            simpleRecipes.put(new ItemStack[]{new ItemStack(Blocks.ladder),
                    new ItemStack(Blocks.gold_block),
                    new ItemStack(Items.redstone),
                    new ItemStack(Items.redstone)}, new RecipeResult(
                    new ItemStack(RandomTechBlocks.speedLadder, 4), 8000));
            simpleRecipes.put(new ItemStack[]{
                    new ItemStack(Items.gold_ingot),
                    new ItemStack(RandomTechItems.hardenedMetalPlate),
                    new ItemStack(Items.redstone),
                    new ItemStack(Items.redstone)}, new RecipeResult(
                    new ItemStack(RandomTechItems.cpu), 8000));
            simpleRecipes.put(new ItemStack[]{new ItemStack(Items.blaze_rod),
                    new ItemStack(Items.blaze_rod),
                    new ItemStack(Items.blaze_rod),
                    new ItemStack(Items.blaze_rod)}, new RecipeResult(
                    new ItemStack(RandomTechItems.superFuel, 1, 0), 500));
            simpleRecipes.put(new ItemStack[]{
                                      new ItemStack(RandomTechItems.superFuel, 1, 0),
                                      new ItemStack(RandomTechItems.superFuel, 1, 0),
                                      new ItemStack(RandomTechItems.superFuel, 1, 0),
                                      new ItemStack(RandomTechItems.superFuel, 1, 0)},
                              new RecipeResult(new ItemStack(RandomTechItems.superFuel,
                                                             1, 1), 500));
            simpleRecipes.put(new ItemStack[]{
                                      new ItemStack(RandomTechItems.superFuel, 1, 3),
                                      new ItemStack(RandomTechItems.superFuel, 1, 3),
                                      new ItemStack(RandomTechItems.superFuel, 1, 3),
                                      new ItemStack(RandomTechItems.superFuel, 1, 3)},
                              new RecipeResult(new ItemStack(RandomTechItems.superFuel,
                                                             1, 4), 500));
            simpleRecipes.put(new ItemStack[]{
                    new ItemStack(RandomTechItems.superFuel, 1, 4),
                    new ItemStack(Items.lava_bucket, 1),
                    new ItemStack(RandomTechItems.superFuel, 1, 4),
                    new ItemStack(Items.lava_bucket, 1)}, new RecipeResult(
                    new ItemStack(RandomTechItems.superFuel, 1, 5), 500));
            simpleRecipes.put(new ItemStack[]{new ItemStack(Blocks.stone),
                    new ItemStack(Blocks.stone), new ItemStack(Blocks.stone),
                    new ItemStack(Items.iron_ingot)}, new RecipeResult(
                    new ItemStack(RandomTechBlocks.reinforcedRock, 32), 5000));
            simpleRecipes.put(new ItemStack[]{
                    new ItemStack(Blocks.stonebrick),
                    new ItemStack(Blocks.gold_block),
                    new ItemStack(Blocks.redstone_block),
                    new ItemStack(Blocks.gold_block)}, new RecipeResult(
                    new ItemStack(RandomTechBlocks.yellowBrickRoad, 8), 10000));
            simpleRecipes.put(new ItemStack[]{
                    new ItemStack(Items.coal, 1),
                    new ItemStack(Items.coal, 1),
                    new ItemStack(Items.coal, 1, 1),
                    new ItemStack(Items.coal, 1, 1)}, new RecipeResult(
                    new ItemStack(RandomTechItems.carbonNanotube, 1), 5000));
        }

        @Override
        public RecipeResult getResult(TileSynthesisMachine machine) {
            if (isClass(machine, Slots.INPUT_TOP_LEFT,
                        ItemHardenedMetalIngot.class)
                    && isClass(machine, Slots.INPUT_TOP_RIGHT,
                               ItemHardenedMetalIngot.class)
                    && isClass(machine, Slots.INPUT_BOTTOM_LEFT,
                               ItemHardenedMetalIngot.class)
                    && isClass(machine, Slots.INPUT_BOTTOM_RIGHT,
                               ItemHardenedMetalIngot.class)) {
                for (int i = Slots.INPUT_TOP_LEFT.id(); i <= Slots.INPUT_BOTTOM_RIGHT
                        .id(); i++) {
                    if (machine.getStackInSlot(i).getItemDamage() < 100) {
                        return null;
                    }
                }
                return new RecipeResult(new ItemStack(
                        RandomTechItems.hardenedMetalPlate, 2), 8000);
            }

            List<ItemStack> stacksCrafting = new ArrayList<>();
            for (int i = Slots.INPUT_TOP_LEFT.id(); i <= Slots.INPUT_BOTTOM_RIGHT
                    .id(); i++) {
                ItemStack stack = machine.getStackInSlot(i);
                if (stack != null) {
                    stacksCrafting.add(stack);
                }
            }

            for (Entry<ItemStack[], RecipeResult> recipe : simpleRecipes
                    .entrySet()) {
                boolean canCraft = true;
                List<ItemStack> availableStacks = new ArrayList<>(
                        stacksCrafting);
                for (ItemStack s : recipe.getKey()) {
                    boolean hasStack = false;
                    ItemStack inMachine = null;
                    for (ItemStack b : availableStacks) {
                        if (WorldUtils.matches(s, b)) {
                            inMachine = b;
                            hasStack = true;
                        }
                    }
                    if (!hasStack) {
                        canCraft = false;
                        break;
                    }
                    availableStacks.remove(inMachine);
                }
                if (canCraft) {
                    return recipe.getValue();
                }
            }

            return null;
        }
    }

}
