package techreborn.client.render.entitys;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import techreborn.blockentity.cable.CableBlockEntity;
import techreborn.blocks.cable.CableBlock;

import java.util.Random;

public class CableCoverRenderer extends BlockEntityRenderer<CableBlockEntity> {

	public CableCoverRenderer(BlockEntityRenderDispatcher dispatcher) {
		super(dispatcher);
	}

	@Override
	public void render(CableBlockEntity blockEntity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		if (blockEntity.getWorld() == null) {
			return;
		}
		BlockState blockState = blockEntity.getWorld().getBlockState(blockEntity.getPos());
		if (!(blockState.getBlock() instanceof CableBlock) || !blockState.get(CableBlock.COVERED)) {
			return;
		}
		final BlockRenderManager blockRenderManager = MinecraftClient.getInstance().getBlockRenderManager();
		BlockState coverState = blockEntity.getCover() != null ? blockEntity.getCover() : Blocks.OAK_PLANKS.getDefaultState();
		VertexConsumer consumer = vertexConsumers.getBuffer(RenderLayers.getBlockLayer(coverState));
		blockRenderManager.renderBlock(coverState, blockEntity.getPos(), blockEntity.getWorld(), matrices, consumer, true, new Random());
	}

}
