package mekanism.generators.common.inventory.container;

import java.util.ArrayList;
import java.util.List;
import mekanism.common.base.IItemProvider;
import mekanism.generators.common.GeneratorsBlock;
import mekanism.generators.common.MekanismGenerators;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.network.IContainerFactory;
import net.minecraftforge.registries.IForgeRegistry;

public class GeneratorsContainerTypes {

    private static final List<ContainerType<?>> types = new ArrayList<>();

    public static final ContainerType<ContainerReactorController> REACTOR_CONTROLLER = create(GeneratorsBlock.REACTOR_CONTROLLER, ContainerReactorController::new);



    //Can just use IItemProvider because IBlockProvider extends it. This way we support both tiles and items
    private static <T extends Container> ContainerType<T> create(IItemProvider provider, IContainerFactory<T> factory) {
        return create(provider.getRegistryName(), factory);
    }

    private static <T extends Container> ContainerType<T> create(String name, IContainerFactory<T> factory) {
        return create(new ResourceLocation(MekanismGenerators.MODID, name), factory);
    }

    private static <T extends Container> ContainerType<T> create(ResourceLocation registryName, IContainerFactory<T> factory) {
        ContainerType<T> type = IForgeContainerType.create(factory);
        type.setRegistryName(registryName);
        types.add(type);
        return type;
    }

    public static void registerContainers(IForgeRegistry<ContainerType<?>> registry) {
        types.forEach(registry::register);
        //TODO: Should the list be cleared afterwards as it isn't really needed anymore after registration
    }
}