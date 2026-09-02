package net.blay09.mods.nolittering.mixin;

import net.blay09.mods.nolittering.NoLitteringConfig;
import net.minecraft.core.Holder;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.treedecorators.PlaceOnGroundDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlaceOnGroundDecorator.class)
public class PlaceOnGroundDecoratorMixin {
    @Shadow
    @Final
    private Holder<BlockStateProvider> blockStateProvider;

    @Inject(method = "place", at = @At("HEAD"), cancellable = true)
    public void place(TreeDecorator.Context context, CallbackInfo ci) {
        if (!NoLitteringConfig.getActive().disableLitterInWorldGeneration) {
            return;
        }

        if (blockStateProvider.value() instanceof WeightedStateProviderAccessor weightedStateProvider) {
            final var weightedStates = weightedStateProvider.getWeightedList().unwrap();
            for (final var weightedState : weightedStates) {
                if (weightedState.value().is(Blocks.LEAF_LITTER)) {
                    ci.cancel();
                    return;
                }
            }
        }
    }
}
