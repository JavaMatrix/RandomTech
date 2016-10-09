package io.github.javamatrix.randomtech.gui;

import cpw.mods.fml.common.network.IGuiHandler;
import io.github.javamatrix.randomtech.container.ContainerEmpowermentTable;
import io.github.javamatrix.randomtech.container.ContainerEnergetic;
import io.github.javamatrix.randomtech.container.ContainerSynthesisMachine;
import io.github.javamatrix.randomtech.tileentities.TileEmpowermentTable;
import io.github.javamatrix.randomtech.tileentities.TileEnergetic;
import io.github.javamatrix.randomtech.tileentities.TileSynthesisMachine;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class GUIHandler implements IGuiHandler {

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world,
                                      int x, int y, int z) {
        TileEntity te = world.getTileEntity(x, y, z);
        if (te == null) {
            return null;
        }
        switch (ID) {
            case GUI.EMPOWERMENT_TABLE:
                if (te instanceof TileEmpowermentTable) {
                    return new ContainerEmpowermentTable(player,
                                                         (TileEmpowermentTable) te);
                }
            case GUI.SYNTHESIS_MACHINE:
                if (te instanceof TileSynthesisMachine) {
                    return new ContainerSynthesisMachine(player,
                                                         (TileSynthesisMachine) te);
                }
            case GUI.ENERGETIC:
                if (te instanceof TileEnergetic) {
                    return new ContainerEnergetic(player, (TileEnergetic) te);
                }
            default:
                return null;
        }
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world,
                                      int x, int y, int z) {
        TileEntity te = world.getTileEntity(x, y, z);
        if (te == null) {
            return null;
        }
        switch (ID) {
            case GUI.EMPOWERMENT_TABLE:
                if (te instanceof TileEmpowermentTable) {
                    return new GuiEmpowermentTable(player,
                                                   (TileEmpowermentTable) te);
                }
            case GUI.SYNTHESIS_MACHINE:
                if (te instanceof TileSynthesisMachine) {
                    return new GuiSynthesisMachine(player,
                                                   (TileSynthesisMachine) te);
                }
            case GUI.ENERGETIC:
                if (te instanceof TileEnergetic) {
                    return new GuiEnergetic(player, (TileEnergetic) te);
                }
            default:
                return null;
        }
    }

}
