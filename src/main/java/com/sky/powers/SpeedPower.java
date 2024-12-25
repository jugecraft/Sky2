package com.sky.powers;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class SpeedPower implements Power {
    @Override
    public void activate(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 600, 1)); // Velocidad durante 30 segundos
    }
}
