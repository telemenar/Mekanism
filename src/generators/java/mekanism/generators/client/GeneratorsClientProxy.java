package mekanism.generators.client;

import java.util.Map;
import java.util.function.Function;
import mekanism.client.render.item.ItemLayerWrapper;
import mekanism.generators.client.gui.GuiReactorController;
import mekanism.generators.client.render.RenderAdvancedSolarGenerator;
import mekanism.generators.client.render.RenderBioGenerator;
import mekanism.generators.client.render.RenderGasGenerator;
import mekanism.generators.client.render.RenderHeatGenerator;
import mekanism.generators.client.render.RenderIndustrialTurbine;
import mekanism.generators.client.render.RenderReactor;
import mekanism.generators.client.render.RenderSolarGenerator;
import mekanism.generators.client.render.RenderTurbineRotor;
import mekanism.generators.client.render.RenderWindGenerator;
import mekanism.generators.client.render.item.RenderAdvancedSolarGeneratorItem;
import mekanism.generators.client.render.item.RenderBioGeneratorItem;
import mekanism.generators.client.render.item.RenderGasGeneratorItem;
import mekanism.generators.client.render.item.RenderHeatGeneratorItem;
import mekanism.generators.client.render.item.RenderSolarGeneratorItem;
import mekanism.generators.client.render.item.RenderWindGeneratorItem;
import mekanism.generators.common.GeneratorsCommonProxy;
import mekanism.generators.common.MekanismGenerators;
import mekanism.generators.common.inventory.container.GeneratorsContainerTypes;
import mekanism.generators.common.tile.TileEntityAdvancedSolarGenerator;
import mekanism.generators.common.tile.TileEntityBioGenerator;
import mekanism.generators.common.tile.TileEntityGasGenerator;
import mekanism.generators.common.tile.TileEntityHeatGenerator;
import mekanism.generators.common.tile.TileEntitySolarGenerator;
import mekanism.generators.common.tile.TileEntityWindGenerator;
import mekanism.generators.common.tile.reactor.TileEntityReactorController;
import mekanism.generators.common.tile.turbine.TileEntityTurbineCasing;
import mekanism.generators.common.tile.turbine.TileEntityTurbineRotor;
import mekanism.generators.common.tile.turbine.TileEntityTurbineValve;
import mekanism.generators.common.tile.turbine.TileEntityTurbineVent;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;

@OnlyIn(Dist.CLIENT)
public class GeneratorsClientProxy extends GeneratorsCommonProxy {

    @Override
    public void registerTESRs() {
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityAdvancedSolarGenerator.class, new RenderAdvancedSolarGenerator());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityBioGenerator.class, new RenderBioGenerator());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityGasGenerator.class, new RenderGasGenerator());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityHeatGenerator.class, new RenderHeatGenerator());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityReactorController.class, new RenderReactor());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySolarGenerator.class, new RenderSolarGenerator());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityTurbineCasing.class, new RenderIndustrialTurbine());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityTurbineRotor.class, new RenderTurbineRotor());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityTurbineValve.class, new RenderIndustrialTurbine());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityTurbineVent.class, new RenderIndustrialTurbine());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityWindGenerator.class, new RenderWindGenerator());
    }

    @Override
    public void registerItemRenders() {
        //TODO
        /*//Register the item inventory model locations for the various blocks
        for (GeneratorsBlock generatorsBlock : GeneratorsBlock.values()) {
            BlockItem item = generatorsBlock.getItem();
            if (item instanceof IItemRedirectedModel) {
                //TODO: Fix Glow panel item coloring
                ModelLoader.setCustomModelResourceLocation(item, 0, getInventoryMRL(((IItemRedirectedModel) item).getRedirectLocation()));
            } else {
                ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), "inventory"));
            }
        }*/
    }

    @Override
    public void registerBlockRenders() {
    }

    @Override
    public void registerScreenHandlers() {
        //TODO: Make sure all are here
        ScreenManager.registerFactory(GeneratorsContainerTypes.REACTOR_CONTROLLER, GuiReactorController::new);
    }

    @SubscribeEvent
    public void onModelBake(ModelBakeEvent event) {
        Map<ResourceLocation, IBakedModel> modelRegistry = event.getModelRegistry();
        registerItemStackModel(modelRegistry, "heat_generator", model -> RenderHeatGeneratorItem.model = model);
        registerItemStackModel(modelRegistry, "solar_generator", model -> RenderSolarGeneratorItem.model = model);
        registerItemStackModel(modelRegistry, "bio_generator", model -> RenderBioGeneratorItem.model = model);
        registerItemStackModel(modelRegistry, "wind_generator", model -> RenderWindGeneratorItem.model = model);
        registerItemStackModel(modelRegistry, "gas_burning_generator", model -> RenderGasGeneratorItem.model = model);
        registerItemStackModel(modelRegistry, "advanced_solar_generator", model -> RenderAdvancedSolarGeneratorItem.model = model);
    }

    private void registerItemStackModel(Map<ResourceLocation, IBakedModel> modelRegistry, String type, Function<ItemLayerWrapper, IBakedModel> setModel) {
        ModelResourceLocation resourceLocation = getInventoryMRL(type);
        modelRegistry.put(resourceLocation, setModel.apply(new ItemLayerWrapper(modelRegistry.get(resourceLocation))));
    }

    private ModelResourceLocation getInventoryMRL(String type) {
        return new ModelResourceLocation(new ResourceLocation(MekanismGenerators.MODID, type), "inventory");
    }

    @Override
    public void preInit() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onStitch(TextureStitchEvent.Pre event) {
    }
}