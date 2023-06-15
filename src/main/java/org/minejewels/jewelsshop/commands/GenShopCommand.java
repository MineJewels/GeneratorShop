package org.minejewels.jewelsshop.commands;

import net.abyssdev.abysslib.command.AbyssCommand;
import net.abyssdev.abysslib.command.context.CommandContext;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerEditBookEvent;
import org.eclipse.collections.api.factory.Lists;
import org.minejewels.jewelsshop.JewelsShop;

public class GenShopCommand extends AbyssCommand<JewelsShop, Player> {

    public GenShopCommand(final JewelsShop plugin) {
        super(plugin, "genshop", Player.class);

        this.setAliases(Lists.mutable.of("genshop", "gensshop", "jewelshop"));
        this.register();
    }

    @Override
    public void execute(CommandContext<Player> context) {

        final Player player = context.getSender();

        this.plugin.getShopMenu().open(player);
    }
}
