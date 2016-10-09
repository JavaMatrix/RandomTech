package io.github.javamatrix.randomtech.items;

import net.minecraft.item.Item;

public class ItemDebugger extends Item {

//    public void chat(EntityPlayer who, String what) {
//        who.addChatComponentMessage(new ChatComponentText(what));
//    }
//
//    @Override
//    public boolean onItemUse(ItemStack stack, EntityPlayer who, World world,
//                             int x, int y, int z, int meta, float hitX, float hitY, float hitZ) {
//        if (world.isRemote) {
//            return false;
//        }
//
//        TileEntity te = world.getTileEntity(x, y, z);
//        if (te != null) {
//            chat(who, te.getClass().getSimpleName());
//            NBTTagCompound nbt = new NBTTagCompound();
//            te.writeToNBT(nbt);
//            chatNBTCompound(who, nbt, "- ", false);
//            return true;
//        }
//        return false;
//    }
//
//    @SuppressWarnings("unchecked")
//    private void chatNBTCompound(EntityPlayer who, NBTTagCompound nbt,
//                                 String indent, boolean isChild) {
//        if (!isChild) {
//            chat(who, indent + "Compound:");
//        }
//
//        for (String key : (Set<String>) nbt.func_150296_c()) {
//            byte tagType = nbt.func_150299_b(key);
//            String tagTypeName = NBTBase.NBTTypes[tagType];
//            switch (tagTypeName) {
//                case "END":
//                    continue;
//                case "BYTE":
//                    chat(who, "  " + indent + ChatFormatting.AQUA + key + ": "
//                            + ChatFormatting.RESET + nbt.getByte(key));
//                    break;
//                case "SHORT":
//                    chat(who, "  " + indent + ChatFormatting.BLUE + key + ": "
//                            + ChatFormatting.RESET + nbt.getShort(key));
//                    break;
//                case "INT":
//                    chat(who, "  " + indent + ChatFormatting.DARK_AQUA + key + ": "
//                            + ChatFormatting.RESET + nbt.getInteger(key));
//                    break;
//                case "LONG":
//                    chat(who, "  " + indent + ChatFormatting.DARK_BLUE + key + ": "
//                            + ChatFormatting.RESET + nbt.getLong(key));
//                    break;
//                case "FLOAT":
//                    chat(who, "  " + indent + ChatFormatting.DARK_GRAY + key + ": "
//                            + ChatFormatting.RESET + nbt.getFloat(key));
//                    break;
//                case "DOUBLE":
//                    chat(who, "  " + indent + ChatFormatting.DARK_GREEN + key
//                            + ": " + ChatFormatting.RESET + nbt.getDouble(key));
//                    break;
//                case "BYTE[]":
//                    chat(who, "  " + indent + ChatFormatting.DARK_PURPLE + key
//                            + ":" + ChatFormatting.RESET);
//                    byte[] bytes = nbt.getByteArray(key);
//                    for (byte b : bytes) {
//                        chat(who, "  " + ChatFormatting.DARK_PURPLE + indent + b
//                                + ChatFormatting.RESET);
//                    }
//                    break;
//                case "STRING":
//                    chat(who, "  " + indent + ChatFormatting.DARK_RED + key + ": "
//                            + ChatFormatting.RESET + nbt.getString(key));
//                    break;
//                case "LIST":
//                    chat(who, "  " + indent + ChatFormatting.GOLD + key + ": ");
//                    chatNBTList(who, nbt.getTagList(key, detectListType(nbt, key)),
//                                " " + ChatFormatting.GOLD + indent);
//                    break;
//                case "COMPOUND":
//                    chat(who, "  " + indent + ChatFormatting.GRAY + key + ":");
//                    chatNBTCompound(who, nbt.getCompoundTag(key), "  " + indent,
//                                    true);
//                    break;
//                case "INT[]":
//                    chat(who, "  " + indent + ChatFormatting.GREEN + key + ": "
//                            + ChatFormatting.RESET);
//                    int[] ints = nbt.getIntArray(key);
//                    for (int i : ints) {
//                        chat(who, "  " + indent + i);
//                    }
//                    break;
//                default:
//                    chat(who, "  " + indent + ChatFormatting.RED + key
//                            + " (Unknown tag)" + ChatFormatting.RESET);
//            }
//        }
//    }
//
//    private int detectListType(NBTTagCompound nbt, String key) {
//        Map<String, NBTBase> tagMap = ReflectionHelper.getPrivateValue(
//                NBTTagCompound.class, nbt, 1);
//        NBTTagList list = (NBTTagList) tagMap.get(key);
//        return list.func_150303_d();
//    }
//
//    private void chatNBTList(EntityPlayer who, NBTTagList tagList, String indent) {
//
//        byte tagType = (byte) tagList.func_150303_d();
//        String tagTypeName = NBTBase.NBTTypes[tagType];
//        NBTTagList copy = (NBTTagList) tagList.copy();
//        int i = 0;
//        // Continously pop items off of the list until it's empty.
//        while (copy.tagCount() > 0) {
//            i++;
//            NBTBase tag = copy.removeTag(0);
//            switch (tagTypeName) {
//                case "END":
//                    continue;
//                case "BYTE":
//                    chat(who,
//                         "  " + indent + ChatFormatting.AQUA + "Item #" + i
//                                 + ": " + ChatFormatting.RESET
//                                 + ((NBTPrimitive) tag).func_150290_f());
//                    break;
//                case "SHORT":
//                    chat(who,
//                         "  " + indent + ChatFormatting.BLUE + "Item #" + i
//                                 + ": " + ChatFormatting.RESET
//                                 + ((NBTPrimitive) tag).func_150289_e());
//                    break;
//                case "INT":
//                    chat(who, "  " + indent + ChatFormatting.DARK_AQUA + "Item #"
//                            + i + ": " + ChatFormatting.RESET
//                            + ((NBTPrimitive) tag).func_150287_d());
//                    break;
//                case "LONG":
//                    chat(who, "  " + indent + ChatFormatting.DARK_BLUE + "Item #"
//                            + i + ": " + ChatFormatting.RESET
//                            + ((NBTPrimitive) tag).func_150291_c());
//                    break;
//                case "FLOAT":
//                    chat(who, "  " + indent + ChatFormatting.DARK_GRAY + "Item #"
//                            + i + ": " + ChatFormatting.RESET
//                            + ((NBTPrimitive) tag).func_150288_h());
//                    break;
//                case "DOUBLE":
//                    chat(who, "  " + indent + ChatFormatting.DARK_GREEN + "Item #"
//                            + i + ": " + ChatFormatting.RESET
//                            + ((NBTPrimitive) tag).func_150288_h());
//                    break;
//                case "BYTE[]":
//                    chat(who, "  " + indent + ChatFormatting.DARK_PURPLE + "Item #"
//                            + i + ":" + ChatFormatting.RESET);
//                    byte[] bytes = ((NBTTagByteArray) tag).func_150292_c();
//                    for (byte b : bytes) {
//                        chat(who, "  " + ChatFormatting.DARK_PURPLE + indent + b
//                                + ChatFormatting.RESET);
//                    }
//                    break;
//                case "STRING":
//                    chat(who, "  " + indent + ChatFormatting.DARK_RED + "Item #"
//                            + i + ": " + ChatFormatting.RESET + tag.toString());
//                    break;
//                case "LIST":
//                    chatNBTList(who, (NBTTagList) tag, " " + indent);
//                    break;
//                case "COMPOUND":
//                    chat(who, "  " + indent + ChatFormatting.GRAY + "Item #" + i
//                            + ":");
//                    chatNBTCompound(who, (NBTTagCompound) tag, "  " + indent, true);
//                    break;
//                case "INT[]":
//                    chat(who, "  " + indent + ChatFormatting.GREEN + "Item #" + i
//                            + ": " + ChatFormatting.RESET);
//                    int[] ints = ((NBTTagIntArray) tag).func_150302_c();
//                    for (int j : ints) {
//                        chat(who, "  " + indent + j);
//                    }
//                    break;
//                default:
//                    chat(who, "  " + indent + ChatFormatting.RED + "Item #" + i
//                            + " (Unknown tag)" + ChatFormatting.RESET);
//            }
//        }
//    }
}
