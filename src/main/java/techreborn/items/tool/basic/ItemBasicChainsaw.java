/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2018 TechReborn
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package techreborn.items.tool.basic;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTier;
import net.minecraft.util.NonNullList;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import reborncore.common.powerSystem.forge.ForgePowerItemManager;
import techreborn.config.ConfigTechReborn;
import techreborn.init.TRContent;
import techreborn.items.tool.ItemChainsaw;

public class ItemBasicChainsaw extends ItemChainsaw {

	public ItemBasicChainsaw() {
		super(ItemTier.IRON, ConfigTechReborn.BasicChainsawCharge, 0.5F);
		this.cost = 50;
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void fillItemGroup(ItemGroup par2ItemGroup, NonNullList<ItemStack> itemList) {
		if (!isInGroup(par2ItemGroup)) {
			return;
		}
		ItemStack stack = new ItemStack(TRContent.BASIC_CHAINSAW);
		ItemStack charged = stack.copy();
		ForgePowerItemManager capEnergy = new ForgePowerItemManager(charged);
		capEnergy.setEnergyStored(capEnergy.getMaxEnergyStored());

		itemList.add(stack);
		itemList.add(charged);
	}

	@Override
	public boolean canHarvestBlock(IBlockState state) {
		return Items.IRON_AXE.canHarvestBlock(state);
	}
}