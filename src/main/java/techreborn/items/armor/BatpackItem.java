/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2020 TechReborn
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

package techreborn.items.armor;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DefaultedList;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import reborncore.common.powerSystem.PowerSystem;
import reborncore.common.util.ItemDurabilityExtensions;
import reborncore.common.util.ItemUtils;
import team.reborn.energy.Energy;
import team.reborn.energy.EnergyHolder;
import team.reborn.energy.EnergyTier;
import techreborn.TechReborn;
import techreborn.utils.InitUtils;

public class BatpackItem extends ArmorItem implements EnergyHolder, ItemDurabilityExtensions {

	public final int maxCharge;
	public final EnergyTier tier;

	public BatpackItem(int maxCharge, ArmorMaterial material, EnergyTier tier) {
		super(material, EquipmentSlot.CHEST, new Settings().group(TechReborn.ITEMGROUP).maxCount(1).maxDamage(-1));
		this.maxCharge = maxCharge;
		this.tier = tier;

		this.addPropertyGetter(new Identifier("techreborn:empty"), (stack, worldIn, entityIn) -> {
			if (!stack.isEmpty() && Energy.of(stack).getEnergy() == 0) {
				return 1.0F;
			}
			return 0.0F;
		});
	}

	private void distributePowerToInventory(World world, PlayerEntity player, ItemStack itemStack, int maxOutput) {
		if (world.isClient || !Energy.valid(itemStack)) {
			return;
		}

		for (int i = 0; i < player.inventory.getInvSize(); i++) {
			if (Energy.valid(player.inventory.getInvStack(i))) {
				Energy.of(itemStack)
						.into(Energy.of(player.inventory.getInvStack(i)))
						.move(maxOutput);
			}
		}
	}

	// Item
	@Override
	public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		if (entityIn instanceof PlayerEntity) {
			distributePowerToInventory(worldIn, (PlayerEntity) entityIn, stack, tier.getMaxOutput());
		}
	}

	@Override
	public boolean isDamageable() {
		return false;
	}

	@Override
	public boolean isEnchantable(ItemStack stack) {
		return true;
	}

	@Environment(EnvType.CLIENT)
	@Override
	public void appendStacks(ItemGroup group, DefaultedList<ItemStack> itemList) {
		if (!isIn(group)) {
			return;
		}
		InitUtils.initPoweredItems(this, itemList);
	}

	// EnergyHolder
	@Override
	public double getMaxStoredPower() {
		return maxCharge;
	}

	@Override
	public EnergyTier getTier() {
		return tier;
	}

	// ItemDurabilityExtensions
	@Override
	public int getDurabilityColor(ItemStack stack) {
		return PowerSystem.getDisplayPower().colour;
	}

	@Override
	public boolean showDurability(ItemStack stack) {
		return true;
	}

	@Override
	public double getDurability(ItemStack stack) {
		return 1 - ItemUtils.getPowerForDurabilityBar(stack);
	}
}
