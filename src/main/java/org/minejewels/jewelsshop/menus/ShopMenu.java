package org.minejewels.jewelsshop.menus;

import net.abyssdev.abysslib.builders.ItemBuilder;
import net.abyssdev.abysslib.economy.provider.Economy;
import net.abyssdev.abysslib.economy.registry.impl.DefaultEconomyRegistry;
import net.abyssdev.abysslib.menu.MenuBuilder;
import net.abyssdev.abysslib.menu.templates.AbyssMenu;
import net.abyssdev.abysslib.placeholder.PlaceholderReplacer;
import net.abyssdev.abysslib.utils.Utils;
import net.abyssdev.abysslib.utils.WordUtils;
import org.bukkit.inventory.ItemStack;
import org.bukkit.entity.Player;
import org.eclipse.collections.api.factory.Sets;
import org.minejewels.jewelsgens.JewelsGens;
import org.minejewels.jewelsgens.gen.Generator;
import org.minejewels.jewelsshop.JewelsShop;

public class ShopMenu extends AbyssMenu {

    private final JewelsShop plugin;
    private final JewelsGens gens;
    private final Economy economy;

    private final ItemBuilder notEnoughMoneyItem;

    public ShopMenu(final JewelsShop plugin) {
        super(plugin.getMenusConfig(), "shop-menu.");

        this.plugin = plugin;
        this.gens = JewelsGens.get();
        this.economy = DefaultEconomyRegistry.get().getEconomy("VAULT");
        this.notEnoughMoneyItem = this.plugin.getSettingsConfig().getItemBuilder("special-items.not-enough-money");
    }

    @Override
    public void open(final Player player) {

        final MenuBuilder menuBuilder = this.createBase();

        if (this.plugin.getSettingsConfig().getBoolean("custom-menu-texture")) {
            menuBuilder.setBorders(Sets.mutable.empty());
        }

        for (final String key : this.plugin.getMenusConfig().getSectionKeys("shop-menu.generators")) {

            if (!this.gens.getGeneratorRegistry().containsKey(key.toUpperCase())) return;

            final Generator generator = this.gens.getGeneratorRegistry().get(key.toUpperCase()).get();
            final int slot = this.plugin.getMenusConfig().getInt("shop-menu.generators." + key + ".slot");
            final long price = this.plugin.getMenusConfig().getInt("shop-menu.generators." + key + ".price");
            final ItemStack item = this.plugin.getMenusConfig().getItemStack("shop-menu.generators." + key + ".item");

            menuBuilder.setItem(slot, item);

            menuBuilder.addClickEvent(slot, event -> {

                if (!this.economy.hasBalance(player, price))  {
                    final double needed = price - this.economy.getBalance(player);
                    menuBuilder.setTempItem(slot, this.notEnoughMoneyItem.parse(new PlaceholderReplacer().addPlaceholder("%amount%", Utils.format(needed))), 40);
                    return;
                }

                this.economy.withdrawBalance(player, price);

                final PlaceholderReplacer replacer = new PlaceholderReplacer().addPlaceholder("%amount%", String.valueOf(1)).addPlaceholder("%type%", WordUtils.formatText(generator.getIdentifier().toLowerCase().replace("_", " ")));

                this.gens.getMessageCache().sendMessage(player, "messages.generator-given", replacer);

                player.getInventory().addItem(generator.getItem());
            });
        }

        player.openInventory(menuBuilder.build());
    }
}
