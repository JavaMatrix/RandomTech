package io.github.javamatrix.randomtech.entities.effect;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import org.lwjgl.util.vector.Vector3f;

import java.util.ArrayList;
import java.util.Random;

// Currently broken, but may return someday.
@SuppressWarnings("unused")
public class EntityLightning extends Entity {

    public ArrayList<Vector3f> lightning_path = new ArrayList<Vector3f>();
    Vector3f begin;
    Vector3f end;
    int life;
    Random lightning_random = new Random();

    public EntityLightning(World world, float x1, float y1, float z1, float x2,
                           float y2, float z2, int lifetime) {
        super(world);
        begin = new Vector3f(x1, y1, z1);
        end = new Vector3f(x2, y2, z2);
        life = lifetime;
        this.posX = x1;
        this.posY = y1;
        this.posZ = z1;
        entityPostInit();
    }

    @Override
    protected void entityInit() {
    }

    protected void entityPostInit() {
        // We're gonna walk along the path 1 block at a time, displacing it by
        // up to one block in each direction each time.

        // Copy the beginning position for the current position.
        Vector3f current_position = new Vector3f(begin);

        // Now calculate the walking vector.
        Vector3f walking_vector = new Vector3f();
        // First get the relative vector between begin and end.
        Vector3f.sub(end, begin, walking_vector);
        // Grab the distance magnitude while we're here.
        float total_dist = walking_vector.length();
        // Then normalize to a length of 1 block.
        walking_vector.normalise();

        // We'll start 1 step after the beginning and stop 1 step before the end
        // to keep the ends in fixed positions.
        int steps = (int) total_dist - 2;

        // Add the start position to the pos list.
        lightning_path.add(begin);

        for (int i = 0; i < steps; i++) {
            // Note that we're incrementing before the first offset, to keep the
            // beginning position fixed.

            // Add the walking vector to the current position.
            // Equivalent to current_position += walking_vector. Unfortunately,
            // this syntax is not supported by LWJGL vectors.
            Vector3f.add(current_position, walking_vector, current_position);

            // Now copy the current position so as not to stray too far off of
            // the path.
            Vector3f new_point = new Vector3f(current_position);
            // And mutate it by up to 1 block in each direction.
            // Subtracting a random number in the range [0, 1) from another
            // random number in the same range generates a number in the range
            // (-1, 1).
            new_point.x += lightning_random.nextFloat()
                    - lightning_random.nextFloat();
            new_point.y += lightning_random.nextFloat()
                    - lightning_random.nextFloat();
            new_point.z += lightning_random.nextFloat()
                    - lightning_random.nextFloat();

            // And now, add the new point to the list of points on the lightning
            // bolt.
            lightning_path.add(new_point);
        }
        // Let's not forget the endpoint.
        lightning_path.add(end);
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound nbt) {
        lightning_path.clear();
        int num_points = nbt.getInteger("num_points");
        NBTTagCompound path = nbt.getCompoundTag("path");
        for (int i = 0; i < num_points; i++) {
            NBTTagCompound point = path.getCompoundTag("point_" + i);
            Vector3f new_point = new Vector3f();
            new_point.x = point.getFloat("x");
            new_point.y = point.getFloat("y");
            new_point.z = point.getFloat("z");
            lightning_path.add(new_point);
        }
        life = nbt.getInteger("life");
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound nbt) {
        nbt.setInteger("num_points", lightning_path.size());
        NBTTagCompound path = new NBTTagCompound();
        for (int i = 0; i < lightning_path.size(); i++) {
            Vector3f point = lightning_path.get(i);
            NBTTagCompound vec_nbt = new NBTTagCompound();
            vec_nbt.setFloat("x", point.x);
            vec_nbt.setFloat("y", point.y);
            vec_nbt.setFloat("z", point.z);
            path.setTag("point_" + i, vec_nbt);
        }
        nbt.setTag("path", path);
        nbt.setInteger("life", life);
    }

    @Override
    public void onEntityUpdate() {
        super.onEntityUpdate();
        life--;
        if (life <= 0) {
            this.kill();
        }
    }

}
