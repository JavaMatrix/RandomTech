package io.github.javamatrix.randomtech.util;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;

/**
 * Functions for saving custom data in the world data folder.
 *
 * @author JavaMatrix
 */
public class SaveHandler extends WorldSavedData {

    // The data being stored.
    protected NBTTagCompound data;

    /**
     * Makes a new SaveHandler for the world with the given name.
     *
     * @param saveID the identifier for the save data.
     */
    protected SaveHandler(String saveID) {
        // Just hand it up the chain.
        super(saveID);
    }

    /**
     * Fetches the SaveHandler for the given world or makes a new one if one
     * doesn't exist.
     *
     * @param theWorld The world to load data from.
     * @return The SaveHandler for the given world.
     */
    public static SaveHandler forWorld(World theWorld) {
        // Fetch the save from the per-world storage in the given world
        // object.
        SaveHandler save = (SaveHandler) theWorld.perWorldStorage.loadData(
                SaveHandler.class, "snowtech");

        // If it's null, we need to make a new one.
        if (save == null) {
            // Make a new one.
            save = new SaveHandler("snowtech");
            // And save it to the world.
            theWorld.setItemData("snowtech", save);
            // Mark it for refreshing.
            save.markDirty();
        }

        // Give 'em what they came for!
        return save;
    }

    /**
     * Reads in the data from NBT.
     */
    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        data = (NBTTagCompound) nbt.getTag("data");
    }

    /**
     * Write the data to NBT.
     */
    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        nbt.setTag("data", data);
    }

    /**
     * Add a tag to the save.
     *
     * @param name The name of the tag.
     * @param nbt  The tag to save.
     */
    public void setTag(String name, NBTBase nbt) {
        // Save the tag.
        data.setTag(name, nbt);
        // And mark the save for refreshing.
        markDirty();
    }

    public NBTBase getTag(String name) {
        // Grab the tag from the save.
        return data.getTag(name);
    }
}
