(ns mario-bukkit-mod.core
  (:require [cloft.cloft :as c])
  (:require [swank.swank])
  (:require [clojure.set :as s])
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

(def soft-materials
  #{Material/LEAVES Material/DIRT Material/GLASS Material/GLOWSTONE
    Material/GRASS Material/GRAVEL Material/HUGE_MUSHROOM_1
    Material/HUGE_MUSHROOM_2 Material/ICE Material/LOG Material/PUMPKIN
    Material/SAND Material/SOIL Material/STEP Material/THIN_GLASS Material/TNT
    Material/WOOD Material/WOOL Material/WORKBENCH})

(def normal-materials
  #{Material/COAL Material/COBBLESTONE Material/COBBLESTONE_STAIRS
    Material/DIAMOND_BLOCK Material/DIAMOND_ORE Material/DOUBLE_STEP
    Material/FENCE Material/FENCE_GATE Material/GOLD_BLOCK Material/GOLD_ORE
    Material/IRON_BLOCK Material/IRON_ORE Material/LAPIS_ORE
    Material/MOSSY_COBBLESTONE Material/REDSTONE_ORE Material/SANDSTONE
    Material/SMOOTH_BRICK Material/SMOOTH_STAIRS Material/STONE})

(def hard-materials
  #{Material/BRICK Material/BRICK_STAIRS Material/MOB_SPAWNER
    Material/OBSIDIAN})

(defn player-move-event [evt]
  (when (c/jumping? evt)
    (let [block (.getBlock (.add (.getLocation (.getPlayer evt)) 0 2 0))
          helmet (.getHelmet (.getInventory (.getPlayer evt)))]
      (letfn [(go []
                (.breakNaturally block)
                (c/add-velocity (.getPlayer evt) 0 0.5 0))]
        (cond
          (nil? helmet)
          (when (soft-materials (.getType block))
            (go))

          (and
            (#{Material/LEATHER_HELMET Material/GOLD_HELMET Material/IRON_HELMET Material/CHAINMAIL_HELMET} (.getType helmet))
            ((s/union soft-materials normal-materials) (.getType block)))
          (go)

          (and
            (#{Material/DIAMOND_HELMET} (.getType helmet))
            ((s/union soft-materials normal-materials hard-materials) (.getType block)))
          (go)

          :else
          nil)))))

(defonce swank* nil)
(defn on-enable [plugin]
  (when (nil? swank*)
    (def swank* (swank.swank/start-repl 4005))))
