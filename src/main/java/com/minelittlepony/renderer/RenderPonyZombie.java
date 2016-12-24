package com.minelittlepony.renderer;

import com.minelittlepony.PonyGender;
import com.minelittlepony.PonyRace;
import com.minelittlepony.PonySize;
import com.minelittlepony.TailLengths;
import com.minelittlepony.model.PMAPI;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.monster.ZombieType;
import net.minecraft.util.ResourceLocation;

import java.util.Random;

public class RenderPonyZombie<Zombie extends EntityZombie> extends RenderPonyMob<Zombie> {

    private static final ResourceLocation ZOMBIE = new ResourceLocation("minelittlepony", "textures/entity/zombie/zombie_pony.png");
    private static final ResourceLocation HUSK = new ResourceLocation("minelittlepony", "textures/entity/zombie/husk_pony.png");

    private static final ResourceLocation GENERIC = new ResourceLocation("minelittlepony", "textures/entity/zombie_villager/zombie_villager_pony.png");
    private static final ResourceLocation FARMER = new ResourceLocation("minelittlepony", "textures/entity/zombie_villager/zombie_farmer_pony.png");
    private static final ResourceLocation LIBRARIAN = new ResourceLocation("minelittlepony", "textures/entity/zombie_villager/zombie_librarian_pony.png");
    private static final ResourceLocation PRIEST = new ResourceLocation("minelittlepony", "textures/entity/zombie_villager/zombie_priest_pony.png");
    private static final ResourceLocation SMITH = new ResourceLocation("minelittlepony", "textures/entity/zombie_villager/zombie_smith_pony.png");
    private static final ResourceLocation BUTCHER = new ResourceLocation("minelittlepony", "textures/entity/zombie_villager/zombie_butcher_pony.png");

    public RenderPonyZombie(RenderManager rendermanager) {
        super(rendermanager, PMAPI.zombie);
    }

    @Override
    protected void preRenderCallback(Zombie entity, float partick) {
        super.preRenderCallback(entity, partick);
        Random rand = new Random(entity.getUniqueID().hashCode());

        // 50-50 chance for gender
        this.playerModel.getModel().metadata.setGender(rand.nextBoolean() ? PonyGender.MARE : PonyGender.STALLION);

        // races
        switch (rand.nextInt(2) + 2) {
            case 0:
            case 1:
                this.playerModel.getModel().metadata.setRace(PonyRace.EARTH);
                break;
            case 2:
                this.playerModel.getModel().metadata.setRace(PonyRace.PEGASUS);
                break;
            case 3:
                this.playerModel.getModel().metadata.setRace(PonyRace.UNICORN);
                break;
        }
        // Let's play the lottery!
        if (rand.nextInt(10000) == 0) {
            this.playerModel.getModel().metadata.setRace(PonyRace.ALICORN);
        }
        // sizes
        if (entity.isChild()) {
            this.playerModel.getModel().metadata.setSize(PonySize.FOAL);
        } else {
            PonySize size = randEnum(rand, PonySize.class);
            this.playerModel.getModel().metadata.setSize(size != PonySize.FOAL ? size : PonySize.NORMAL);
        }
        this.playerModel.getModel().metadata.setTail(randEnum(rand, TailLengths.class));

        // glow
        this.playerModel.getModel().metadata.setGlowColor(rand.nextInt());

        if (entity.getZombieType() == ZombieType.HUSK) {
            GlStateManager.scale(1.0625F, 1.0625F, 1.0625F);
        }
    }

    @Override
    protected void applyRotations(Zombie zombie, float p_77043_2_, float p_77043_3_, float partialTicks) {
        if (zombie.isConverting()) {
            p_77043_3_ += (float) (Math.cos(zombie.ticksExisted * 3.25D) * Math.PI * 0.25D);
        }
        super.applyRotations(zombie, p_77043_2_, p_77043_3_, partialTicks);
    }

    private static <T extends Enum<T>> T randEnum(Random rand, Class<T> en) {
        T[] values = en.getEnumConstants();
        return values[rand.nextInt(values.length)];
    }

    @Override
    protected ResourceLocation getEntityTexture(Zombie zombie) {

        return getTexture(getResource(zombie));
    }

    private ResourceLocation getResource(Zombie zombie) {

        switch (zombie.getZombieType()) {
            case VILLAGER_FARMER:
                return FARMER;
            case VILLAGER_LIBRARIAN:
                return LIBRARIAN;
            case VILLAGER_PRIEST:
                return PRIEST;
            case VILLAGER_SMITH:
                return SMITH;
            case VILLAGER_BUTCHER:
                return BUTCHER;
            case HUSK:
                return HUSK;
            case NORMAL:
            default:
                return ZOMBIE;

        }
    }

}
