package org.minejewels.jewelsshop;

import lombok.Getter;
import net.abyssdev.abysslib.config.AbyssConfig;
import net.abyssdev.abysslib.plugin.AbyssPlugin;
import org.minejewels.jewelsshop.commands.GenShopCommand;
import org.minejewels.jewelsshop.menus.ShopMenu;

@Getter
public final class JewelsShop extends AbyssPlugin {

    private final AbyssConfig settingsConfig = this.getAbyssConfig("settings");
    private final AbyssConfig menusConfig = this.getAbyssConfig("menus");

    private ShopMenu shopMenu;
    private GenShopCommand genShopCommand;

    @Override
    public void onEnable() {
        this.shopMenu = new ShopMenu(this);
        this.genShopCommand = new GenShopCommand(this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
