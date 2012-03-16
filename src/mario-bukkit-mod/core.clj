(ns mario-bukkit-mod.core
  (:require [cloft.cloft :as c])
  (:require [swank.swank])
  (:require [clojure.string :as s])
  (:import [org.bukkit Bukkit Material])
  (:import [org.bukkit.entity Animals Arrow Blaze Boat CaveSpider Chicken
            ComplexEntityPart ComplexLivingEntity Cow Creature Creeper Egg
            EnderCrystal EnderDragon EnderDragonPart Enderman EnderPearl
            EnderSignal ExperienceOrb Explosive FallingSand Fireball Fish
            Flying Ghast Giant HumanEntity Item LightningStrike LivingEntity
            MagmaCube Minecart Monster MushroomCow NPC Painting Pig PigZombie
            Player PoweredMinecart Projectile Sheep Silverfish Skeleton Slime
            SmallFireball Snowball Snowman Spider Squid StorageMinecart
            ThrownPotion TNTPrimed Vehicle Villager WaterMob Weather Wolf
            Zombie])
  (:import [org.bukkit.event.entity EntityDamageByEntityEvent
            EntityDamageEvent$DamageCause]))

(defn player-move-event [evt]
  (when (c/jumping? evt)
    (let [block (.getBlock (.add (.getLocation (.getPlayer evt)) 0 2 0))]
      (when (#{Material/LEAVES Material/DIRT} (.getType block))
        (.setType block Material/AIR)
        (c/add-velocity (.getPlayer evt) 0 0.5 0)))))

(defonce swank* nil)
(defn on-enable [plugin]
  (when (nil? swank*)
    (def swank* (swank.swank/start-repl 4005))))
