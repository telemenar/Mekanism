package mekanism.client.render.item;

import javax.annotation.Nonnull;
import mekanism.client.MekanismClient;
import mekanism.client.model.ModelEnergyCube;
import mekanism.client.model.ModelEnergyCube.ModelEnergyCore;
import mekanism.client.render.MekanismRenderHelper;
import mekanism.client.render.MekanismRenderer;
import mekanism.client.render.tileentity.RenderEnergyCube;
import mekanism.common.SideData.IOState;
import mekanism.common.base.ITierItem;
import mekanism.common.tier.EnergyCubeTier;
import mekanism.common.util.ItemDataUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderEnergyCubeItem extends MekanismItemStackRenderer {

    private static ModelEnergyCube energyCube = new ModelEnergyCube();
    private static ModelEnergyCore core = new ModelEnergyCore();
    public static ItemLayerWrapper model;

    @Override
    protected void renderBlockSpecific(@Nonnull ItemStack stack, TransformType transformType) {
        EnergyCubeTier tier = EnergyCubeTier.values()[((ITierItem) stack.getItem()).getBaseTier(stack).ordinal()];
        MekanismRenderHelper cubeRenderHelper = new MekanismRenderHelper(true);
        MekanismRenderer.bindTexture(RenderEnergyCube.baseTexture);
        cubeRenderHelper.rotateZ(180, 1).rotateY(180, 1).translateY(-1.0F).enableBlendPreset();

        energyCube.render(0.0625F, tier, Minecraft.getMinecraft().renderEngine, true);

        for (EnumFacing side : EnumFacing.VALUES) {
            MekanismRenderer.bindTexture(RenderEnergyCube.baseTexture);
            energyCube.renderSide(0.0625F, side, side == EnumFacing.NORTH ? IOState.OUTPUT : IOState.INPUT, tier, Minecraft.getMinecraft().renderEngine);
        }
        cubeRenderHelper.cleanup();

        double energy = ItemDataUtils.getDouble(stack, "energyStored");

        if (energy / tier.getMaxEnergy() > 0.1) {
            MekanismRenderHelper coreRenderHelper = new MekanismRenderHelper(true);
            MekanismRenderer.bindTexture(RenderEnergyCube.coreTexture);
            coreRenderHelper.enableBlendPreset().enableGlow();

            MekanismRenderHelper coreColorRenderHelper = new MekanismRenderHelper(true)
                  .scale(0.4F).color(tier.getBaseTier()).translateY(Math.sin(Math.toRadians(MekanismClient.ticksPassed * 3)) / 7)
                  .rotateY(MekanismClient.ticksPassed * 4, 1).rotateYZ(36F + MekanismClient.ticksPassed * 4, 1, 1);
            core.render(0.0625F);
            coreColorRenderHelper.cleanup();
            coreRenderHelper.cleanup();
        }
    }

    @Override
    protected void renderItemSpecific(@Nonnull ItemStack stack, TransformType transformType) {

    }

    @Nonnull
    @Override
    protected TransformType getTransform(@Nonnull ItemStack stack) {
        return model.getTransform();
    }
}