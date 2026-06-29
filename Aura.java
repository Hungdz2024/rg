package unique.core.features.modules.combat;

import java.util.Comparator;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.class_1268;
import net.minecraft.class_1294;
import net.minecraft.class_1297;
import net.minecraft.class_1308;
import net.minecraft.class_1309;
import net.minecraft.class_1429;
import net.minecraft.class_1451;
import net.minecraft.class_1531;
import net.minecraft.class_1588;
import net.minecraft.class_1621;
import net.minecraft.class_1646;
import net.minecraft.class_1657;
import net.minecraft.class_1674;
import net.minecraft.class_1676;
import net.minecraft.class_1678;
import net.minecraft.class_1713;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_1839;
import net.minecraft.class_2246;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_238;
import net.minecraft.class_243;
import net.minecraft.class_2596;
import net.minecraft.class_2663;
import net.minecraft.class_2815;
import net.minecraft.class_2824;
import net.minecraft.class_2828;
import net.minecraft.class_2846;
import net.minecraft.class_2848;
import net.minecraft.class_2868;
import net.minecraft.class_2886;
import net.minecraft.class_3532;
import net.minecraft.class_4587;
import net.minecraft.class_5134;
import net.minecraft.class_745;
import net.minecraft.class_2846.class_2847;
import net.minecraft.class_2848.class_2849;
import org.jetbrains.annotations.NotNull;
import unique.core.UniqueCore;
import unique.core.core.Core;
import unique.core.core.Managers;
import unique.core.core.manager.client.ModuleManager;
import unique.core.core.manager.player.PlayerManager;
import unique.core.events.impl.EventSync;
import unique.core.events.impl.PacketEvent;
import unique.core.events.impl.PlayerUpdateEvent;
import unique.core.features.modules.Module;
import unique.core.features.modules.client.ClientSettings;
import unique.core.features.modules.client.HudEditor;
import unique.core.features.modules.movement.ElytraTarget;
import unique.core.features.modules.player.ElytraSwap;
import unique.core.features.modules.rotations.AIRotations;
import unique.core.gui.notification.Notification;
import unique.core.injection.accesors.ILivingEntity;
import unique.core.setting.Setting;
import unique.core.setting.impl.SettingGroup;
import unique.core.utility.Timer;
import unique.core.utility.interfaces.IOtherClientPlayerEntity;
import unique.core.utility.math.MathUtility;
import unique.core.utility.player.InteractionUtility;
import unique.core.utility.player.InventoryUtility;
import unique.core.utility.player.PlayerUtility;
import unique.core.utility.render.Render2DEngine;
import unique.core.utility.render.Render3DEngine;

public class Aura extends Module {
   public final Setting<Float> attackRange = new Setting<Float>("AttackRange", 3.0F, 1.0F, 6.0F);
   public final Setting<Float> wallRange = new Setting<Float>("WallsRange", 3.0F, 0.0F, 6.0F);
   public final Setting<Float> aimRange = new Setting<Float>("AimRange", 3.0F, 0.0F, 10.0F);
   public final Setting<Float> elytraAttackRange = new Setting<Float>("ElytraRange", 4.0F, 0.0F, 8.0F);
   public final Setting<Float> elytraAimRange = new Setting<Float>("ElytraAimRange", 35.0F, 8.0F, 1000.0F);
   public final Setting<WallsBypass> wallsBypass;
   public final Setting<Integer> fov;
   public final Setting<Mode> rotationMode;
   public final Setting<Boolean> smartCrit;
   public final Setting<Boolean> onlyCrit;
   public final Setting<Boolean> shieldBreaker;
   public final Setting<Boolean> pauseWhileEating;
   public final Setting<Boolean> tpsSync;
   public final Setting<Boolean> clientLook;
   public final Setting<Timing> timing;
   public final Setting<Boolean> oldDelay;
   public final Setting<Integer> minCPS;
   public final Setting<Integer> maxCPS;
   public final Setting<Sort> sort;
   public final Setting<Boolean> lockTarget;
   public final Setting<Boolean> elytraTarget;
   public final Setting<SprintMode> sprintMode;
   public final Setting<SettingGroup> advanced;
   public final Setting<Boolean> dropSprint;
   public final Setting<Boolean> returnSprint;
   public final Setting<RayTrace> rayTrace;
   public final Setting<Boolean> grimRayTrace;
   public final Setting<Boolean> unpressShield;
   public final Setting<Boolean> deathDisable;
   public final Setting<AttackHand> attackHand;
   public final Setting<Resolver> resolver;
   public final Setting<Boolean> resolverVisualisation;
   public final Setting<AccelerateOnHit> accelerateOnHit;
   public final Setting<Integer> minYawStep;
   public final Setting<Integer> maxYawStep;
   public final Setting<Float> aimedPitchStep;
   public final Setting<Float> maxPitchStep;
   public final Setting<Float> pitchAccelerate;
   public final Setting<Float> attackCooldown;
   public final Setting<Float> attackBaseTime;
   public final Setting<Integer> attackTickLimit;
   public final Setting<Float> critFallDistance;
   public final Setting<SettingGroup> targets;
   public final Setting<Boolean> Players;
   public final Setting<Boolean> antiBotNew;
   public final Setting<Boolean> Mobs;
   public final Setting<Boolean> Animals;
   public final Setting<Boolean> Villagers;
   public final Setting<Boolean> Slimes;
   public final Setting<Boolean> hostiles;
   public final Setting<Boolean> onlyAngry;
   public final Setting<Boolean> Projectiles;
   public final Setting<Boolean> ignoreInvisible;
   public final Setting<Boolean> ignoreNamed;
   public final Setting<Boolean> ignoreTeam;
   public final Setting<Boolean> ignoreCreative;
   public final Setting<Boolean> ignoreNaked;
   public final Setting<Boolean> ignoreShield;
   public static class_1297 target;
   private class_1297 lastTarget;
   public float rotationYaw;
   public float rotationPitch;
   public float pitchAcceleration;
   private float originalYaw;
   private float originalPitch;
   private boolean restoring;
   private class_243 rotationPoint;
   private class_243 rotationMotion;
   private class_243 targetPredictedPos;
   private class_243 lastTargetVelocity;
   private int hitTicks;
   private int trackticks;
   private boolean lookingAtHitbox;
   private final Timer delayTimer;
   private final Timer pauseTimer;
   private final Timer sprintTimer;
   private boolean sprintDisabledByAura;
   private boolean wasOnGround;
   private double lastHorizontalSpeed;
   private boolean hvhServerSprint;
   private class_243 elytraPredictPoint;
   private final AIRotations aiRotations;
   private final Random rng;
   public class_238 resolvedBox;
   static boolean wasTargeted = false;
   private int legacyTickCounter;

   public Aura() {
      super("AttackAura", Module.Category.COMBAT);
      this.wallsBypass = new Setting<WallsBypass>("WallsBypass", Aura.WallsBypass.Off, (var1) -> this.getWallRange() > 0.0F);
      this.fov = new Setting<Integer>("FOV", 180, 1, 360);
      this.rotationMode = new Setting<Mode>("RotationMode", Aura.Mode.Track);
      this.smartCrit = new Setting<Boolean>("SmartCrit", true);
      this.onlyCrit = new Setting<Boolean>("OnlyCritical", false);
      this.shieldBreaker = new Setting<Boolean>("ShieldBreaker", true);
      this.pauseWhileEating = new Setting<Boolean>("PauseWhileEating", false);
      this.tpsSync = new Setting<Boolean>("TPSSync", false);
      this.clientLook = new Setting<Boolean>("ClientLook", false);
      this.timing = new Setting<Timing>("Timing", Aura.Timing.Modern);
      this.oldDelay = new Setting<Boolean>("OldDelay", false, (var1) -> this.timing.is(Aura.Timing.Legacy));
      this.minCPS = new Setting<Integer>("MinCPS", 7, 1, 20, (var1) -> (Boolean)this.oldDelay.getValue() && this.timing.is(Aura.Timing.Legacy));
      this.maxCPS = new Setting<Integer>("MaxCPS", 12, 1, 20, (var1) -> (Boolean)this.oldDelay.getValue() && this.timing.is(Aura.Timing.Legacy));
      this.sort = new Setting<Sort>("Sort", Aura.Sort.LowestDistance);
      this.lockTarget = new Setting<Boolean>("LockTarget", true);
      this.elytraTarget = new Setting<Boolean>("ElytraTarget", true);
      this.sprintMode = new Setting<SprintMode>("Sprint", Aura.SprintMode.Off);
      this.advanced = new Setting<SettingGroup>("Advanced", new SettingGroup(false, 0));
      this.dropSprint = (new Setting<Boolean>("DropSprint", false)).addToGroup(this.advanced);
      this.returnSprint = (new Setting<Boolean>("ReturnSprint", false, (var1) -> (Boolean)this.dropSprint.getValue())).addToGroup(this.advanced);
      this.rayTrace = (new Setting<RayTrace>("RayTrace", Aura.RayTrace.OnlyTarget)).addToGroup(this.advanced);
      this.grimRayTrace = (new Setting<Boolean>("GrimRayTrace", true)).addToGroup(this.advanced);
      this.unpressShield = (new Setting<Boolean>("UnpressShield", true)).addToGroup(this.advanced);
      this.deathDisable = (new Setting<Boolean>("DisableOnDeath", true)).addToGroup(this.advanced);
      this.attackHand = (new Setting<AttackHand>("AttackHand", Aura.AttackHand.MainHand)).addToGroup(this.advanced);
      this.resolver = (new Setting<Resolver>("Resolver", Aura.Resolver.Advantage)).addToGroup(this.advanced);
      this.resolverVisualisation = (new Setting<Boolean>("ResolverVisualisation", false, (var1) -> !this.resolver.is(Aura.Resolver.Off))).addToGroup(this.advanced);
      this.accelerateOnHit = (new Setting<AccelerateOnHit>("AccelerateOnHit", Aura.AccelerateOnHit.Off)).addToGroup(this.advanced);
      this.minYawStep = (new Setting<Integer>("MinYawStep", 65, 1, 180)).addToGroup(this.advanced);
      this.maxYawStep = (new Setting<Integer>("MaxYawStep", 75, 1, 180)).addToGroup(this.advanced);
      this.aimedPitchStep = (new Setting<Float>("AimedPitchStep", 1.0F, 0.0F, 90.0F)).addToGroup(this.advanced);
      this.maxPitchStep = (new Setting<Float>("MaxPitchStep", 8.0F, 1.0F, 90.0F)).addToGroup(this.advanced);
      this.pitchAccelerate = (new Setting<Float>("PitchAccelerate", 1.65F, 1.0F, 10.0F)).addToGroup(this.advanced);
      this.attackCooldown = (new Setting<Float>("AttackCooldown", 0.9F, 0.5F, 1.0F)).addToGroup(this.advanced);
      this.attackBaseTime = (new Setting<Float>("AttackBaseTime", 0.5F, 0.0F, 2.0F)).addToGroup(this.advanced);
      this.attackTickLimit = (new Setting<Integer>("AttackTickLimit", 2, 0, 20)).addToGroup(this.advanced);
      this.critFallDistance = (new Setting<Float>("CritFallDistance", 0.0F, 0.0F, 1.0F)).addToGroup(this.advanced);
      this.targets = new Setting<SettingGroup>("Targets", new SettingGroup(false, 0));
      this.Players = (new Setting<Boolean>("Players", true)).addToGroup(this.targets);
      this.antiBotNew = (new Setting<Boolean>("AntiBot", false)).addToGroup(this.targets);
      this.Mobs = (new Setting<Boolean>("Mobs", false)).addToGroup(this.targets);
      this.Animals = (new Setting<Boolean>("Animals", false)).addToGroup(this.targets);
      this.Villagers = (new Setting<Boolean>("Villagers", false)).addToGroup(this.targets);
      this.Slimes = (new Setting<Boolean>("Slimes", false)).addToGroup(this.targets);
      this.hostiles = (new Setting<Boolean>("Hostiles", false)).addToGroup(this.targets);
      this.onlyAngry = (new Setting<Boolean>("OnlyAngryHostiles", false, (var1) -> (Boolean)this.hostiles.getValue())).addToGroup(this.targets);
      this.Projectiles = (new Setting<Boolean>("Projectiles", false)).addToGroup(this.targets);
      this.ignoreInvisible = (new Setting<Boolean>("IgnoreInvisibleEntities", false)).addToGroup(this.targets);
      this.ignoreNamed = (new Setting<Boolean>("IgnoreNamed", false)).addToGroup(this.targets);
      this.ignoreTeam = (new Setting<Boolean>("IgnoreTeam", false)).addToGroup(this.targets);
      this.ignoreCreative = (new Setting<Boolean>("IgnoreCreative", false)).addToGroup(this.targets);
      this.ignoreNaked = (new Setting<Boolean>("IgnoreNaked", false)).addToGroup(this.targets);
      this.ignoreShield = (new Setting<Boolean>("AttackShieldingEntities", true)).addToGroup(this.targets);
      this.pitchAcceleration = 1.0F;
      this.restoring = false;
      this.rotationPoint = class_243.field_1353;
      this.rotationMotion = class_243.field_1353;
      this.targetPredictedPos = class_243.field_1353;
      this.lastTargetVelocity = class_243.field_1353;
      this.delayTimer = new Timer();
      this.pauseTimer = new Timer();
      this.sprintTimer = new Timer();
      this.sprintDisabledByAura = false;
      this.wasOnGround = true;
      this.lastHorizontalSpeed = (double)0.0F;
      this.hvhServerSprint = false;
      this.elytraPredictPoint = class_243.field_1353;
      this.aiRotations = new AIRotations();
      this.rng = new Random();
      this.legacyTickCounter = 0;
   }

   private int randomInt(int var1, int var2) {
      return var1 + this.rng.nextInt(Math.max(1, var2 - var1 + 1));
   }

   private float getRange() {
      return this.getAttackRange();
   }

   private float getWallRange() {
      return this.isElytra() ? (Float)this.elytraAttackRange.getValue() : (Float)this.wallRange.getValue();
   }

   private boolean isElytra() {
      return mc.field_1724 != null && mc.field_1724.method_6128();
   }

   private float getAimRange() {
      return this.isElytra() ? (Float)this.elytraAimRange.getValue() : (Float)this.aimRange.getValue();
   }

   private float getAttackRange() {
      return this.isElytra() ? (Float)this.elytraAttackRange.getValue() : (Float)this.attackRange.getValue();
   }

   private float getSearchDistance() {
      return this.isElytra() ? (Float)this.elytraAimRange.getValue() : (Float)this.aimRange.getValue();
   }

   private class_243 calcPredictedPos(class_1297 var1) {
      class_243 var2 = var1.method_19538();
      class_243 var3 = var1.method_18798();
      double var4 = mc.field_1724.method_33571().method_1022(var1.method_33571());
      double var6 = mc.field_1724.method_18798().method_1033();
      double var8 = var6 > 0.05 ? class_3532.method_15350(var4 / (var6 * (double)20.0F), (double)0.5F, (double)3.0F) : class_3532.method_15350(var4 / (double)5.0F, (double)0.5F, (double)2.0F);
      class_243 var10 = class_243.method_1030(var1.method_36455(), var1.method_36454()).method_1029();
      double var11 = var3.method_1027() > 1.0E-5 ? var3.method_1029().method_1026(var10) : (double)0.0F;
      double var13 = class_3532.method_15350(0.6 + var11 * 0.4, (double)0.5F, (double)1.0F);
      class_243 var15 = var2.method_1031(var3.field_1352 * var8 * var13, var3.field_1351 * var8 * (double)0.5F, var3.field_1350 * var8 * var13);
      return new class_243(var15.field_1352, var15.field_1351 + var1.method_5829().method_17940() * 0.6, var15.field_1350);
   }

   public void auraLogic() {
      if (!this.haveWeapon()) {
         target = null;
      } else {
         this.handleKill();
         this.updateTarget();
         if (target != null) {
            boolean var1;
            if ((Boolean)this.grimRayTrace.getValue()) {
               var1 = this.autoCrit() && (this.lookingAtHitbox || this.skipRayTraceCheck());
               this.calcRotations(this.autoCrit());
            } else {
               this.calcRotations(this.autoCrit());
               var1 = this.autoCrit() && (this.lookingAtHitbox || this.skipRayTraceCheck());
            }

            if (var1) {
               float var2 = !this.isElytra() && !ElytraSwap.justSwappedToArmor ? this.getAttackRange() : (Float)this.elytraAttackRange.getValue();
               if (mc.field_1724.method_5739(target) > var2) {
                  return;
               }

               if (this.shieldBreaker(false)) {
                  return;
               }

               boolean[] var3;
               boolean var10000;
               label49: {
                  var3 = this.preAttack();
                  class_1297 var6 = target;
                  if (var6 instanceof class_1657) {
                     class_1657 var5 = (class_1657)var6;
                     if (var5.method_6115() && var5.method_6079().method_7909() == class_1802.field_8255 && !(Boolean)this.ignoreShield.getValue()) {
                        var10000 = true;
                        break label49;
                     }
                  }

                  var10000 = false;
               }

               boolean var4 = var10000;
               if (!var4) {
                  this.attack();
               }

               this.postAttack(var3[0], var3[1]);
               ElytraSwap.justSwappedToArmor = false;
            }

         }
      }
   }

   private boolean haveWeapon() {
      return true;
   }

   private boolean skipRayTraceCheck() {
      if (this.isElytra()) {
         return true;
      } else if (ElytraSwap.justSwappedToArmor) {
         return true;
      } else {
         return this.rotationMode.getValue() == Aura.Mode.None || this.rayTrace.getValue() == Aura.RayTrace.OFF || this.rotationMode.is(Aura.Mode.Grim);
      }
   }

   public void attack() {
      Criticals.cancelCrit = true;
      ModuleManager.criticals.doCrit();
      int var1 = this.switchMethod();
      mc.field_1761.method_2918(mc.field_1724, target);
      Criticals.cancelCrit = false;
      this.swingHand();
      this.hitTicks = this.getHitTicks();
      if (var1 != -1) {
         InventoryUtility.switchTo(var1);
      }

   }

   private boolean @NotNull [] preAttack() {
      boolean var1 = mc.field_1724.method_6115() && mc.field_1724.method_6030().method_7909().method_7853(mc.field_1724.method_6030()) == class_1839.field_8949;
      if (var1 && (Boolean)this.unpressShield.getValue()) {
         this.sendPacket(new class_2846(class_2847.field_12974, class_2338.field_10980, class_2350.field_11033));
      }

      boolean var2 = mc.field_1724.method_5624();
      if (this.sprintMode.is(Aura.SprintMode.HVH)) {
         this.hvhServerSprint = Core.serverSprint;
         if (this.hvhServerSprint) {
            this.disableSprint();
         }
      }

      if (ModuleManager.maceSwap.isEnabled()) {
         ModuleManager.maceSwap.onAuraAttack();
      }

      if (var2 && (Boolean)this.dropSprint.getValue() && !this.sprintDisabledByAura) {
         boolean var3 = mc.field_1724.method_5869() || mc.field_1724.method_5799();
         boolean var4 = this.isElytra() || mc.field_1724.method_6128() || ElytraSwap.justSwappedToArmor;
         if (!var3 && !var4) {
            double var5 = mc.field_1724.method_18798().method_37268();
            double var7 = Math.abs(var5 - this.lastHorizontalSpeed);
            boolean var9 = mc.field_1724.method_24828() && !mc.field_1724.method_6101() && !mc.field_1724.method_5799() && !mc.field_1724.method_5771() && var5 > 0.0015 && var7 < 0.002 && this.getAttackCooldown() > 0.85F && (!(mc.field_1724.field_6017 < 0.15F) || mc.field_1724.method_24828() || !this.wasOnGround) && (!mc.field_1724.method_24828() || this.wasOnGround) && (mc.field_1724.field_6235 <= 0 || !(var5 > 0.08)) && mc.field_1724.field_6012 % 2 == 0;
            if (var9) {
               this.sprintDisabledByAura = true;
               mc.field_1724.method_5728(false);
               this.sprintTimer.reset();
            }
         }

         this.wasOnGround = mc.field_1724.method_24828();
         this.lastHorizontalSpeed = mc.field_1724.method_18798().method_37268();
      }

      if (this.rotationMode.is(Aura.Mode.Grim)) {
         this.sendPacket(new class_2828.class_2830(mc.field_1724.method_23317(), mc.field_1724.method_23318(), mc.field_1724.method_23321(), this.rotationYaw, this.rotationPitch, mc.field_1724.method_24828()));
      }

      return new boolean[]{var1, var2};
   }

   public void postAttack(boolean var1, boolean var2) {
      if (this.sprintMode.is(Aura.SprintMode.HVH) && this.hvhServerSprint) {
         this.enableSprint();
      }

      boolean var3 = mc.field_1724.method_5869() || mc.field_1724.method_5799();
      boolean var4 = this.isElytra() || mc.field_1724.method_6128() || ElytraSwap.justSwappedToArmor;
      if (ModuleManager.maceSwap.isEnabled()) {
         ModuleManager.maceSwap.onAuraPostAttack();
      }

      if (this.sprintDisabledByAura && (Boolean)this.returnSprint.getValue() && !var3 && !var4) {
         double var5 = mc.field_1724.method_18798().method_37268();
         long var7;
         if (var5 > 0.08) {
            var7 = (long)this.randomInt(12, 20);
         } else if (var5 > 0.03) {
            var7 = (long)this.randomInt(18, 28);
         } else {
            var7 = (long)this.randomInt(25, 40);
         }

         if (mc.field_1724.field_6017 > 0.2F && !mc.field_1724.method_24828()) {
            var7 += (long)this.randomInt(15, 25);
         }

         if (mc.field_1724.method_24828() && this.wasOnGround) {
            var7 = Math.max(0L, var7 - 5L);
         }

         if (mc.field_1724.field_6235 > 0) {
            var7 = Math.max(0L, var7 - (long)this.randomInt(8, 15));
         }

         if (this.getAttackCooldown() < 0.3F) {
            var7 += 10L;
         }

         var7 = Math.max(var7, 5L);
         if (this.sprintTimer.passedMs(var7)) {
            boolean var9 = var2 && mc.field_1724.method_24828() && !mc.field_1724.field_5976 && mc.field_1724.method_7344().method_7586() > 6 && !mc.field_1724.method_6059(class_1294.field_5919) && var5 > 0.001 && this.wasOnGround;
            if (var9) {
               mc.field_1724.method_5728(true);
            }

            this.sprintDisabledByAura = false;
         }
      }

      if (this.sprintDisabledByAura && (this.sprintTimer.passedMs(450L) || var3 || var4 || mc.field_1724.method_31549().field_7479 || !var2 || mc.field_1724.field_6017 > 1.5F)) {
         this.sprintDisabledByAura = false;
      }

      if (var1 && (Boolean)this.unpressShield.getValue()) {
         this.sendSequencedPacket((var1x) -> new class_2886(class_1268.field_5810, var1x, this.rotationYaw, this.rotationPitch));
      }

      if (this.rotationMode.is(Aura.Mode.Grim)) {
         this.sendPacket(new class_2828.class_2830(mc.field_1724.method_23317(), mc.field_1724.method_23318(), mc.field_1724.method_23321(), mc.field_1724.method_36454(), mc.field_1724.method_36455(), mc.field_1724.method_24828()));
      }

   }

   private void disableSprint() {
      mc.field_1724.method_5728(false);
      mc.field_1690.field_1867.method_23481(false);
      this.sendPacket(new class_2848(mc.field_1724, class_2849.field_12985));
   }

   private void enableSprint() {
      mc.field_1724.method_5728(true);
      mc.field_1690.field_1867.method_23481(true);
      this.sendPacket(new class_2848(mc.field_1724, class_2849.field_12981));
   }

   private boolean autoCrit() {
      if (this.hitTicks > 0) {
         return false;
      } else {
         boolean var1 = mc.field_1724.method_5869() || mc.field_1724.method_5799();
         if (var1) {
            if (this.timing.is(Aura.Timing.Modern)) {
               return this.getAttackCooldown() >= (Float)this.attackCooldown.getValue();
            } else {
               return true;
            }
         } else {
            boolean var2 = this.isElytra() || ElytraSwap.justSwappedToArmor && !mc.field_1724.method_24828();
            if (var2) {
               if (this.timing.is(Aura.Timing.Modern) && this.getAttackCooldown() < (Float)this.attackCooldown.getValue()) {
                  return false;
               } else {
                  return !this.timing.is(Aura.Timing.Legacy) || this.hitTicks <= 0;
               }
            } else if (this.timing.is(Aura.Timing.Modern) && this.getAttackCooldown() < (Float)this.attackCooldown.getValue()) {
               return false;
            } else if (!(Boolean)this.smartCrit.getValue()) {
               return true;
            } else if (ModuleManager.criticals.isEnabled() && ModuleManager.criticals.mode.is(Criticals.Mode.Grim)) {
               return true;
            } else if (!mc.field_1724.method_31549().field_7479 && !ModuleManager.elytraPlus.isEnabled() && !mc.field_1724.method_6059(class_1294.field_5919) && !mc.field_1724.method_6059(class_1294.field_5906) && !Managers.PLAYER.isInWeb() && !mc.field_1724.method_5771()) {
               if ((Boolean)this.onlyCrit.getValue() && mc.field_1724.method_24828()) {
                  return false;
               } else if (!mc.field_1690.field_1903.method_1434() && this.isAboveWater()) {
                  return true;
               } else if (ModuleManager.targetStrafe.isEnabled() && (Boolean)ModuleManager.targetStrafe.jump.getValue() && target != null && !mc.field_1724.method_24828() && mc.field_1724.field_6017 > 0.01F) {
                  return true;
               } else {
                  boolean var3 = !ModuleManager.targetStrafe.isEnabled() || !(Boolean)ModuleManager.targetStrafe.jump.getValue();
                  boolean var4 = !ModuleManager.speed.isEnabled() || mc.field_1724.method_24828();
                  if (!mc.field_1690.field_1903.method_1434() && var3 && var4) {
                     return true;
                  } else if ((double)mc.field_1724.field_6017 > 0.8 && (double)mc.field_1724.field_6017 < 1.2) {
                     return false;
                  } else {
                     float var5 = this.shouldRandomizeFallDistance() ? MathUtility.random(0.1F, 0.5F) : (Float)this.critFallDistance.getValue();
                     return !mc.field_1724.method_24828() && mc.field_1724.field_6017 > var5;
                  }
               }
            } else {
               return true;
            }
         }
      }
   }

   private boolean shieldBreaker(boolean var1) {
      int var2 = InventoryUtility.getAxe().slot();
      if (var2 != -1 && (Boolean)this.shieldBreaker.getValue()) {
         class_1297 var4 = target;
         if (var4 instanceof class_1657) {
            class_1657 var3 = (class_1657)var4;
            boolean var7 = var3.method_6047().method_7909() == class_1802.field_8255 || var3.method_6079().method_7909() == class_1802.field_8255;
            if (var3.method_6115() && var7) {
               if (!var1) {
                  boolean var5 = mc.field_1724.method_5869() || mc.field_1724.method_5799();
                  boolean var6 = this.isElytra() || mc.field_1724.method_6128();
                  if (var5) {
                     if (mc.field_1724.method_7261(0.5F) < 0.88F) {
                        return false;
                     }

                     if (mc.field_1724.method_5739(var3) > this.getAttackRange() + 0.3F) {
                        return false;
                     }
                  } else if (var6) {
                     if (mc.field_1724.method_7261(0.5F) < 0.85F) {
                        return false;
                     }

                     if (mc.field_1724.method_5739(var3) > this.getAttackRange() + 1.0F) {
                        return false;
                     }
                  } else {
                     if (!(mc.field_1724.field_6017 > 0.05F) || mc.field_1724.method_24828() || mc.field_1724.method_6101() || mc.field_1724.method_5799()) {
                        return false;
                     }

                     if (mc.field_1724.method_7261(0.5F) < 0.92F) {
                        return false;
                     }

                     if (mc.field_1724.method_5739(var3) > this.getAttackRange() + 0.5F) {
                        return false;
                     }
                  }
               }

               int var8 = mc.field_1724.method_31548().field_7545;
               boolean var9 = mc.field_1724.method_5624();
               if (var9 && !this.isElytra()) {
                  this.disableSprint();
               }

               if (var2 >= 9) {
                  mc.field_1761.method_2906(mc.field_1724.field_7512.field_7763, var2, var8, class_1713.field_7791, mc.field_1724);
                  mc.field_1761.method_2918(mc.field_1724, target);
                  this.swingHand();
                  mc.field_1761.method_2906(mc.field_1724.field_7512.field_7763, var2, var8, class_1713.field_7791, mc.field_1724);
                  this.sendPacket(new class_2815(mc.field_1724.field_7512.field_7763));
               } else {
                  this.sendPacket(new class_2868(var2));
                  mc.field_1761.method_2918(mc.field_1724, target);
                  this.swingHand();
                  this.sendPacket(new class_2868(var8));
               }

               if (var9 && !this.isElytra()) {
                  this.enableSprint();
               }

               this.hitTicks = 2;
               return true;
            }

            return false;
         }
      }

      return false;
   }

   private void swingHand() {
      switch (((AttackHand)this.attackHand.getValue()).ordinal()) {
         case 0 -> mc.field_1724.method_6104(class_1268.field_5808);
         case 1 -> mc.field_1724.method_6104(class_1268.field_5810);
      }

   }

   public void resolvePlayers() {
      if (this.resolver.not(Aura.Resolver.Off)) {
         for(class_1657 var2 : mc.field_1687.method_18456()) {
            if (var2 instanceof class_745) {
               ((IOtherClientPlayerEntity)var2).resolve(this.resolver.getValue());
            }
         }
      }

   }

   public void restorePlayers() {
      if (this.resolver.not(Aura.Resolver.Off)) {
         for(class_1657 var2 : mc.field_1687.method_18456()) {
            if (var2 instanceof class_745) {
               ((IOtherClientPlayerEntity)var2).releaseResolver();
            }
         }
      }

   }

   public void handleKill() {
      class_1297 var2 = target;
      if (var2 instanceof class_1309 var1) {
         if (var1.method_6032() <= 0.0F || var1.method_29504()) {
            Managers.NOTIFICATION.publicity("Aura", ClientSettings.isRu() ? "Цель успешно нейтрализована!" : "Target Successfully Neutralized!", 3, Notification.Type.SUCCESS);
         }
      }

   }

   private int switchMethod() {
      return -1;
   }

   private int getHitTicks() {
      if (this.timing.is(Aura.Timing.Legacy)) {
         if ((Boolean)this.oldDelay.getValue()) {
            return 1 + (int)(20.0F / MathUtility.random((float)(Integer)this.minCPS.getValue(), (float)(Integer)this.maxCPS.getValue()));
         } else {
            int var1 = this.randomInt(1, 3);
            return Math.max(1, var1);
         }
      } else {
         return Math.min((Integer)this.attackTickLimit.getValue(), this.randomInt(1, 2));
      }
   }

   private boolean canAttackLegacy() {
      if (!this.timing.is(Aura.Timing.Legacy)) {
         return true;
      } else {
         --this.legacyTickCounter;
         if (this.legacyTickCounter <= 0) {
            this.legacyTickCounter = this.getHitTicks();
            return true;
         } else {
            return false;
         }
      }
   }

   @EventHandler
   public void onUpdate(PlayerUpdateEvent var1) {
      if (this.pauseTimer.passedMs(1000L)) {
         if (!mc.field_1724.method_6115() || !(Boolean)this.pauseWhileEating.getValue()) {
            if (this.sprintMode.is(Aura.SprintMode.Legit)) {
               boolean var2 = mc.field_1724.method_5869() || mc.field_1724.method_5799();
               boolean var3 = mc.field_1724.field_3913.field_3905 > 0.0F && mc.field_1724.method_7344().method_7586() > 6 && !mc.field_1724.field_5976 && !mc.field_1724.method_6115() && !var2;
               if (target == null || !(mc.field_1724.method_7261(0.5F) > 0.9F) && this.hitTicks <= 0) {
                  if (var2) {
                     mc.field_1724.method_5728(false);
                     mc.field_1690.field_1867.method_23481(false);
                  } else {
                     mc.field_1724.method_5728(var3);
                     mc.field_1690.field_1867.method_23481(var3);
                  }
               } else {
                  mc.field_1724.method_5728(false);
                  mc.field_1690.field_1867.method_23481(false);
               }
            }

            this.resolvePlayers();
            if (this.timing.is(Aura.Timing.Legacy)) {
               if (this.canAttackLegacy()) {
                  this.auraLogic();
               }
            } else {
               this.auraLogic();
            }

            this.restorePlayers();
            if (this.hitTicks > 0) {
               --this.hitTicks;
            }

         }
      }
   }

   @EventHandler
   public void onSync(EventSync var1) {
      if (this.restoring) {
         float var2 = 3.0F;
         mc.field_1724.method_36456(class_3532.method_15393(mc.field_1724.method_36454() + class_3532.method_15363(this.originalYaw - mc.field_1724.method_36454(), -var2, var2)));
         mc.field_1724.method_36457(class_3532.method_15363(mc.field_1724.method_36455() + class_3532.method_15363(this.originalPitch - mc.field_1724.method_36455(), -var2, var2), -90.0F, 90.0F));
         if (Math.abs(mc.field_1724.method_36454() - this.originalYaw) < 0.1F && Math.abs(mc.field_1724.method_36455() - this.originalPitch) < 0.1F) {
            this.restoring = false;
         }

         this.rotationYaw = mc.field_1724.method_36454();
         this.rotationPitch = mc.field_1724.method_36455();
      }

      if (this.pauseTimer.passedMs(1000L)) {
         if (!mc.field_1724.method_6115() || !(Boolean)this.pauseWhileEating.getValue()) {
            if (this.haveWeapon()) {
               if (target != null && this.rotationMode.getValue() != Aura.Mode.None && this.rotationMode.getValue() != Aura.Mode.Grim) {
                  mc.field_1724.method_36456(this.rotationYaw);
                  mc.field_1724.method_36457(this.rotationPitch);
               } else {
                  this.rotationYaw = mc.field_1724.method_36454();
                  this.rotationPitch = mc.field_1724.method_36455();
               }

               if (this.timing.is(Aura.Timing.Legacy) && (Boolean)this.oldDelay.getValue() && (Integer)this.minCPS.getValue() > (Integer)this.maxCPS.getValue()) {
                  this.minCPS.setValue(this.maxCPS.getValue());
               }

            }
         }
      }
   }

   @EventHandler
   public void onPacketSend(PacketEvent.@NotNull Send var1) {
      class_2596 var3 = var1.getPacket();
      if (var3 instanceof class_2824 var2) {
         if (Criticals.getInteractType(var2) != Criticals.InteractType.ATTACK && target != null) {
            var1.cancel();
         }
      }

   }

   @EventHandler
   public void onPacketReceive(PacketEvent.@NotNull Receive var1) {
      class_2596 var3 = var1.getPacket();
      if (var3 instanceof class_2663 var2) {
         if (var2.method_11470() == 30 && var2.method_11469(mc.field_1687) != null && target != null && var2.method_11469(mc.field_1687) == target) {
            Managers.NOTIFICATION.publicity("Aura", ClientSettings.isRu() ? "Успешно сломали щит игроку " + target.method_5477().getString() : "Succesfully Destroyed " + target.method_5477().getString() + " Shield", 2, Notification.Type.SUCCESS);
         }

         if (var2.method_11470() == 3 && var2.method_11469(mc.field_1687) == mc.field_1724 && (Boolean)this.deathDisable.getValue()) {
            this.disable(ClientSettings.isRu() ? "Отключаю из-за смерти!" : "Disabling Due To Death!");
         }
      }

   }

   public void onEnable() {
      target = null;
      this.lookingAtHitbox = false;
      this.rotationPoint = class_243.field_1353;
      this.rotationMotion = class_243.field_1353;
      this.rotationYaw = mc.field_1724.method_36454();
      this.rotationPitch = mc.field_1724.method_36455();
      this.lastTargetVelocity = class_243.field_1353;
      this.targetPredictedPos = class_243.field_1353;
      this.wasOnGround = mc.field_1724.method_24828();
      this.lastHorizontalSpeed = mc.field_1724.method_18798().method_37268();
      this.sprintDisabledByAura = false;
      this.hvhServerSprint = false;
      this.legacyTickCounter = 0;
      this.delayTimer.reset();
      this.aiRotations.reset();
   }

   public void onDisable() {
      target = null;
      this.sprintDisabledByAura = false;
      this.hvhServerSprint = false;
      this.legacyTickCounter = 0;
      this.aiRotations.reset();
   }

   private void updateTarget() {
      float var1 = this.getSearchDistance();
      CopyOnWriteArrayList var2 = new CopyOnWriteArrayList();

      for(class_1297 var4 : mc.field_1687.method_18112()) {
         if ((var4 instanceof class_1678 || var4 instanceof class_1674) && var4.method_5805() && this.isInRange(var4) && (Boolean)this.Projectiles.getValue()) {
            target = var4;
            return;
         }

         if (!this.skipEntity(var4) && var4 instanceof class_1309 var5) {
            if (mc.field_1724.method_5739(var4) <= var1) {
               var2.add(var5);
            }
         }
      }

      class_1309 var10000;
      switch (((Sort)this.sort.getValue()).ordinal()) {
         case 0 -> var10000 = (class_1309)var2.stream().min(Comparator.comparing((var0) -> mc.field_1724.method_5707(var0.method_19538()))).orElse((Object)null);
         case 1 -> var10000 = (class_1309)var2.stream().max(Comparator.comparing((var0) -> mc.field_1724.method_5707(var0.method_19538()))).orElse((Object)null);
         case 2 -> var10000 = (class_1309)var2.stream().min(Comparator.comparing((var0) -> var0.method_6032() + var0.method_6067())).orElse((Object)null);
         case 3 -> var10000 = (class_1309)var2.stream().max(Comparator.comparing((var0) -> var0.method_6032() + var0.method_6067())).orElse((Object)null);
         case 4 -> var10000 = (class_1309)var2.stream().min(Comparator.comparing((var0) -> {
   float var1 = 0.0F;

   for(class_1799 var3 : var0.method_5661()) {
      if (var3 != null && var3.method_7909() != class_1802.field_8162) {
         var1 += (float)(var3.method_7936() - var3.method_7919()) / (float)var3.method_7936();
      }
   }

   return var1;
})).orElse((Object)null);
         case 5 -> var10000 = (class_1309)var2.stream().max(Comparator.comparing((var0) -> {
   float var1 = 0.0F;

   for(class_1799 var3 : var0.method_5661()) {
      if (var3 != null && var3.method_7909() != class_1802.field_8162) {
         var1 += (float)(var3.method_7936() - var3.method_7919()) / (float)var3.method_7936();
      }
   }

   return var1;
})).orElse((Object)null);
         case 6 -> var10000 = (class_1309)var2.stream().min(Comparator.comparing(this::getFOVAngle)).orElse((Object)null);
         default -> throw new MatchException((String)null, (Throwable)null);
      }

      class_1309 var6 = var10000;
      if ((Boolean)this.elytraTarget.getValue()) {
         class_1297 var8 = target;
         if (var8 instanceof class_1309) {
            class_1309 var7 = (class_1309)var8;
            if (var7.method_6128() && !this.skipEntity(target)) {
               return;
            }
         }
      }

      if (target == null) {
         target = var6;
         this.lastTarget = target;
      } else {
         if (this.sort.getValue() == Aura.Sort.FOV || !(Boolean)this.lockTarget.getValue()) {
            target = var6;
         }

         if (var6 instanceof class_1676) {
            target = var6;
         }

         if (this.skipEntity(target)) {
            target = null;
         }

         if (target != this.lastTarget) {
            this.lastTarget = target;
         }

      }
   }

   private void calcRotations(boolean var1) {
      if (var1) {
         this.trackticks = mc.field_1687.method_20812(mc.field_1724, mc.field_1724.method_5829().method_1009((double)-0.25F, (double)0.0F, (double)-0.25F).method_989((double)0.0F, (double)1.0F, (double)0.0F)).iterator().hasNext() ? 1 : 3;
      } else if (this.trackticks > 0) {
         --this.trackticks;
      }

      if (target != null) {
         if (this.rotationMode.is(Aura.Mode.MLSAC)) {
            this.calcMLSACRotation();
         } else {
            class_243 var2;
            if (this.isElytra() && (Boolean)this.elytraTarget.getValue()) {
               var2 = this.getElytraHitVec(target);
               this.elytraPredictPoint = var2;
            } else {
               var2 = this.getLegitLook(target);
            }

            if (var2 != null) {
               this.pitchAcceleration = Managers.PLAYER.checkRtx(this.rotationYaw, this.rotationPitch, this.getRange(), this.getRange(), this.rayTrace.getValue()) ? (Float)this.aimedPitchStep.getValue() : (this.pitchAcceleration < (Float)this.maxPitchStep.getValue() ? this.pitchAcceleration * (Float)this.pitchAccelerate.getValue() : (Float)this.maxPitchStep.getValue());
               float var3 = class_3532.method_15393((float)class_3532.method_15338(Math.toDegrees(Math.atan2(var2.field_1350 - mc.field_1724.method_23321(), var2.field_1352 - mc.field_1724.method_23317())) - (double)90.0F) - this.rotationYaw);
               if (this.wallsBypass.is(Aura.WallsBypass.V2) && !var1 && !mc.field_1724.method_6057(target)) {
                  var3 += class_3532.method_15363(var3 * 0.3F, -20.0F, 20.0F);
               }

               float var4 = (float)(-Math.toDegrees(Math.atan2(var2.field_1351 - (mc.field_1724.method_19538().field_1351 + (double)mc.field_1724.method_18381(mc.field_1724.method_18376())), Math.sqrt(Math.pow(var2.field_1352 - mc.field_1724.method_23317(), (double)2.0F) + Math.pow(var2.field_1350 - mc.field_1724.method_23321(), (double)2.0F))))) - this.rotationPitch;
               float var5 = (float)mc.field_1724.method_5707(target.method_19538());
               float var6 = class_3532.method_15363(var5 / 16.0F, 0.2F, 1.0F);
               float var7 = this.lookingAtHitbox ? class_3532.method_15363(1.0F - Math.abs(var3) / 180.0F, 0.6F, 1.0F) : 1.0F;
               float var8 = this.lookingAtHitbox ? class_3532.method_15363(Math.abs(var3) / 60.0F, 0.03F, 0.2F) : class_3532.method_15363(Math.abs(var3) / 40.0F, 0.1F, 0.8F);
               float var9 = this.isElytra() ? 0.0F : MathUtility.random(-0.12F, 0.12F) * var8 * var7;
               float var10 = this.isElytra() ? 0.0F : MathUtility.random(-0.06F, 0.06F) * var8 * var7;
               float var11 = Math.abs(var3) / 180.0F;
               float var12 = var6 < 0.4F ? 0.65F : 0.85F + var6 * 0.15F;
               float var13 = MathUtility.random((float)(Integer)this.minYawStep.getValue(), (float)(Integer)this.maxYawStep.getValue());
               float var14 = this.rotationMode.getValue() == Aura.Mode.Track ? class_3532.method_15363(var13 * (0.72F + var6 * 0.28F) * (0.82F + var11 * 0.18F) * var12 * var7, 1.0F, 180.0F) : 360.0F;
               float var15 = (float)((double)1.0F - Math.exp((double)(-Math.abs(var4)) / (double)35.0F));
               float var16 = this.lookingAtHitbox ? 0.72F : 0.62F + var15 * 0.38F;
               float var17 = this.rotationMode.getValue() == Aura.Mode.Track ? (this.isElytra() ? 180.0F : class_3532.method_15363((this.pitchAcceleration + MathUtility.random(-0.15F, 0.15F)) * var16 * var7, 0.5F, 90.0F)) : 360.0F;
               if (var1) {
                  switch (((AccelerateOnHit)this.accelerateOnHit.getValue()).ordinal()) {
                     case 1:
                        var14 = Math.min(180.0F, var14 * 2.5F);
                        break;
                     case 2:
                        var17 = Math.min(90.0F, var17 * 2.0F);
                        break;
                     case 3:
                        var14 = Math.min(180.0F, var14 * 2.5F);
                        var17 = Math.min(90.0F, var17 * 2.0F);
                  }
               }

               float var18 = this.rotationYaw + class_3532.method_15363(var3, -var14, var14) + var9;
               float var19 = class_3532.method_15363(this.rotationPitch + class_3532.method_15363(var4, -var17, var17) + var10, -90.0F, 90.0F);
               double var20 = Math.pow((Double)mc.field_1690.method_42495().method_41753() * 0.6 + 0.2, (double)3.0F) * 1.2;
               if (this.trackticks <= 0 && this.rotationMode.getValue() != Aura.Mode.Track) {
                  this.rotationYaw = mc.field_1724.method_36454();
                  this.rotationPitch = mc.field_1724.method_36455();
               } else {
                  this.rotationYaw = (float)((double)var18 - (double)(var18 - this.rotationYaw) % var20);
                  this.rotationPitch = (float)((double)var19 - (double)(var19 - this.rotationPitch) % var20);
               }

               if (!this.rotationMode.is(Aura.Mode.Grim)) {
                  ModuleManager.rotations.fixRotation = class_3532.method_15393(this.rotationYaw);
               }

               this.lookingAtHitbox = this.isElytra() ? Managers.PLAYER.checkRtx(this.rotationYaw, this.rotationPitch, (Float)this.elytraAttackRange.getValue(), (Float)this.elytraAttackRange.getValue(), Aura.RayTrace.OnlyTarget) : Managers.PLAYER.checkRtx(this.rotationYaw, this.rotationPitch, this.getRange(), this.getWallRange(), this.rayTrace.getValue());
            }
         }
      }
   }

   private void calcMLSACRotation() {
      class_1297 var2 = target;
      if (var2 instanceof class_1309 var1) {
         class_243 var5 = AIRotations.getBestVector(var1, 0.06F);
         if (var5 != null) {
            AIRotations.Rotation var3 = new AIRotations.Rotation(this.rotationYaw, this.rotationPitch);
            AIRotations.Rotation var4 = this.aiRotations.process(var3, var5, target);
            this.rotationYaw = var4.yaw();
            this.rotationPitch = class_3532.method_15363(var4.pitch(), -90.0F, 90.0F);
            if (!this.rotationMode.is(Aura.Mode.Grim)) {
               ModuleManager.rotations.fixRotation = class_3532.method_15393(this.rotationYaw);
            }

            this.lookingAtHitbox = this.isElytra() ? Managers.PLAYER.checkRtx(this.rotationYaw, this.rotationPitch, (Float)this.elytraAttackRange.getValue(), (Float)this.elytraAttackRange.getValue(), Aura.RayTrace.OnlyTarget) : Managers.PLAYER.checkRtx(this.rotationYaw, this.rotationPitch, this.getRange(), this.getWallRange(), this.rayTrace.getValue());
         }
      }
   }

   public class_243 getLegitLook(class_1297 var1) {
      double var2 = var1.method_5829().method_17939();
      double var4 = var1.method_5829().method_17940();
      double var6 = var1.method_5829().method_17941();
      if (this.rotationMotion.equals(class_243.field_1353)) {
         this.rotationMotion = new class_243((double)MathUtility.random(-0.05F, 0.05F), (double)MathUtility.random(-0.05F, 0.05F), (double)MathUtility.random(-0.05F, 0.05F));
      }

      class_243 var8 = var1.method_18798();
      class_243 var9 = var8.method_1020(this.lastTargetVelocity);
      this.targetPredictedPos = var9.method_1027() > 1.0E-4 ? var1.method_19538().method_1019(var8.method_1021((double)class_3532.method_15363((float)var8.method_1033() * 0.15F, 0.0F, 0.08F))) : var1.method_19538();
      this.lastTargetVelocity = var8;
      this.rotationPoint = this.rotationPoint.method_1019(this.rotationMotion);
      if (this.rotationPoint.field_1352 >= (var2 - 0.05) / (double)2.0F) {
         this.rotationMotion = new class_243((double)(-MathUtility.random(0.003F, 0.03F)), this.rotationMotion.method_10214(), this.rotationMotion.method_10215());
      }

      if (this.rotationPoint.field_1351 >= var4) {
         this.rotationMotion = new class_243(this.rotationMotion.method_10216(), (double)(-MathUtility.random(0.001F, 0.03F)), this.rotationMotion.method_10215());
      }

      if (this.rotationPoint.field_1350 >= (var6 - 0.05) / (double)2.0F) {
         this.rotationMotion = new class_243(this.rotationMotion.method_10216(), this.rotationMotion.method_10214(), (double)(-MathUtility.random(0.003F, 0.03F)));
      }

      if (this.rotationPoint.field_1352 <= -(var2 - 0.05) / (double)2.0F) {
         this.rotationMotion = new class_243((double)MathUtility.random(0.003F, 0.03F), this.rotationMotion.method_10214(), this.rotationMotion.method_10215());
      }

      if (this.rotationPoint.field_1351 <= 0.05) {
         this.rotationMotion = new class_243(this.rotationMotion.method_10216(), (double)MathUtility.random(0.001F, 0.03F), this.rotationMotion.method_10215());
      }

      if (this.rotationPoint.field_1350 <= -(var6 - 0.05) / (double)2.0F) {
         this.rotationMotion = new class_243(this.rotationMotion.method_10216(), this.rotationMotion.method_10214(), (double)MathUtility.random(0.003F, 0.03F));
      }

      if (!mc.field_1724.method_6057(var1)) {
         if (this.wallsBypass.getValue() == Aura.WallsBypass.V1) {
            return var1.method_19538().method_1031(MathUtility.random(-0.15, 0.15), var4, MathUtility.random(-0.15, 0.15));
         }

         if (this.wallsBypass.getValue() == Aura.WallsBypass.V3) {
            class_238 var33 = var1.method_5829();
            class_243 var34 = new class_243((var33.field_1323 + var33.field_1320) / (double)2.0F, (var33.field_1322 + var33.field_1325) / (double)2.0F, (var33.field_1321 + var33.field_1324) / (double)2.0F);
            double var35 = Math.min(0.55, (var33.field_1320 - var33.field_1323) / (double)2.0F);
            double var36 = Math.min(0.7, (var33.field_1325 - var33.field_1322) / (double)2.5F);
            double var37 = MathUtility.random((double)-1.0F, (double)1.0F) * var35 * 0.4;
            double var18 = MathUtility.random((double)-1.0F, (double)1.0F) * var36 * 0.35;
            double var20 = MathUtility.random((double)-1.0F, (double)1.0F) * var35 * 0.4;
            class_243 var22 = var34.method_1031(var37, var18, var20);
            PlayerManager var39 = Managers.PLAYER;
            float[] var23 = PlayerManager.calcAngle(var22);
            if (Managers.PLAYER.checkRtx(var23[0], var23[1], this.getRange(), this.getWallRange(), this.rayTrace.getValue())) {
               return var22;
            }

            for(double var24 = (double)0.0F; var24 <= var36; var24 += 0.1) {
               for(double var26 = -var35; var26 <= var35; var26 += 0.12) {
                  for(double var28 = -var35; var28 <= var35; var28 += 0.12) {
                     class_243 var30 = var34.method_1031(var26, var24, var28);
                     var39 = Managers.PLAYER;
                     float[] var31 = PlayerManager.calcAngle(var30);
                     if (Managers.PLAYER.checkRtx(var31[0], var31[1], this.getRange(), this.getWallRange(), this.rayTrace.getValue())) {
                        return var30;
                     }
                  }
               }
            }

            return var34;
         }
      }

      if (!Managers.PLAYER.checkRtx(this.rotationYaw, this.rotationPitch, this.getRange(), this.getWallRange(), this.rayTrace.getValue())) {
         PlayerManager var10000 = Managers.PLAYER;
         float[] var10 = PlayerManager.calcAngle(var1.method_19538().method_1031((double)0.0F, (double)(var1.method_18381(var1.method_18376()) / 2.0F), (double)0.0F));
         if (PlayerUtility.squaredDistanceFromEyes(var1.method_19538().method_1031((double)0.0F, (double)(var1.method_18381(var1.method_18376()) / 2.0F), (double)0.0F)) <= this.getAttackRange() * this.getAttackRange() && Managers.PLAYER.checkRtx(var10[0], var10[1], this.getRange(), 0.0F, this.rayTrace.getValue())) {
            this.rotationPoint = new class_243((double)MathUtility.random(-0.1F, 0.1F), (double)(var1.method_18381(var1.method_18376()) / MathUtility.random(1.8F, 2.5F)), (double)MathUtility.random(-0.1F, 0.1F));
         } else {
            float var11 = (float)(var2 / (double)2.0F);

            label104:
            for(float var12 = -var11; var12 <= var11; var12 += 0.05F) {
               for(float var13 = -var11; var13 <= var11; var13 += 0.05F) {
                  for(float var14 = 0.05F; (double)var14 <= var1.method_5829().method_17940(); var14 += 0.15F) {
                     class_243 var15 = new class_243(var1.method_23317() + (double)var12, var1.method_23318() + (double)var14, var1.method_23321() + (double)var13);
                     if (!(PlayerUtility.squaredDistanceFromEyes(var15) > this.getAttackRange() * this.getAttackRange())) {
                        var10000 = Managers.PLAYER;
                        float[] var16 = PlayerManager.calcAngle(var15);
                        if (Managers.PLAYER.checkRtx(var16[0], var16[1], this.getRange(), 0.0F, this.rayTrace.getValue())) {
                           this.rotationPoint = new class_243((double)var12, (double)var14, (double)var13);
                           break label104;
                        }
                     }
                  }
               }
            }
         }
      }

      class_243 var32 = this.calcPredictedPos(var1);
      return new class_243(var32.field_1352 + this.rotationPoint.field_1352, var32.field_1351, var32.field_1350 + this.rotationPoint.field_1350);
   }

   private class_243 getElytraHitVec(class_1297 var1) {
      if (ElytraTarget.auraPredict != null) {
         return ElytraTarget.auraPredict;
      } else {
         class_243 var2 = var1.method_19538();
         class_243 var3 = var1.method_18798();
         return new class_243(var2.field_1352 + var3.field_1352 * (double)2.0F, var2.field_1351 + var1.method_5829().method_17940() * 0.6, var2.field_1350 + var3.field_1350 * (double)2.0F);
      }
   }

   public float getSquaredRotateDistance() {
      float var1 = this.getAimRange();
      return var1 * var1;
   }

   public boolean isInRange(class_1297 var1) {
      float var2 = this.isElytra() ? (Float)this.elytraAttackRange.getValue() : (Float)this.attackRange.getValue();
      float var3 = this.isElytra() ? (Float)this.elytraAimRange.getValue() : (Float)this.aimRange.getValue();
      float var4 = this.isElytra() ? (Float)this.elytraAttackRange.getValue() : (Float)this.wallRange.getValue();
      if (PlayerUtility.squaredDistanceFromEyes(var1.method_19538().method_1031((double)0.0F, (double)var1.method_18381(var1.method_18376()), (double)0.0F)) > var3 * var3 + 4.0F) {
         return false;
      } else if (mc.field_1724.method_5739(var1) <= var2) {
         return true;
      } else {
         float var5 = (float)(var1.method_5829().method_17939() / (double)2.0F);

         for(float var6 = -var5; var6 <= var5; var6 += 0.15F) {
            for(float var7 = -var5; var7 <= var5; var7 += 0.15F) {
               for(float var8 = 0.05F; (double)var8 <= var1.method_5829().method_17940(); var8 += 0.25F) {
                  class_243 var9 = new class_243(var1.method_23317() + (double)var6, var1.method_23318() + (double)var8, var1.method_23321() + (double)var7);
                  if (!(PlayerUtility.squaredDistanceFromEyes(var9) > var3 * var3)) {
                     PlayerManager var10000 = Managers.PLAYER;
                     float[] var10 = PlayerManager.calcAngle(var9);
                     if (Managers.PLAYER.checkRtx(var10[0], var10[1], var3, var4, this.rayTrace.getValue())) {
                        return true;
                     }
                  }
               }
            }
         }

         return false;
      }
   }

   public class_1297 findTarget() {
      CopyOnWriteArrayList var1 = new CopyOnWriteArrayList();

      for(class_1297 var3 : mc.field_1687.method_18112()) {
         if ((var3 instanceof class_1678 || var3 instanceof class_1674) && var3.method_5805() && this.isInRange(var3) && (Boolean)this.Projectiles.getValue()) {
            return var3;
         }

         if (!this.skipEntity(var3) && var3 instanceof class_1309) {
            var1.add((class_1309)var3);
         }
      }

      class_1309 var10000;
      switch (((Sort)this.sort.getValue()).ordinal()) {
         case 0 -> var10000 = (class_1309)var1.stream().min(Comparator.comparing((var0) -> mc.field_1724.method_5707(var0.method_19538()))).orElse((Object)null);
         case 1 -> var10000 = (class_1309)var1.stream().max(Comparator.comparing((var0) -> mc.field_1724.method_5707(var0.method_19538()))).orElse((Object)null);
         case 2 -> var10000 = (class_1309)var1.stream().min(Comparator.comparing((var0) -> var0.method_6032() + var0.method_6067())).orElse((Object)null);
         case 3 -> var10000 = (class_1309)var1.stream().max(Comparator.comparing((var0) -> var0.method_6032() + var0.method_6067())).orElse((Object)null);
         case 4 -> var10000 = (class_1309)var1.stream().min(Comparator.comparing((var0) -> {
   float var1 = 0.0F;

   for(class_1799 var3 : var0.method_5661()) {
      if (var3 != null && var3.method_7909() != class_1802.field_8162) {
         var1 += (float)(var3.method_7936() - var3.method_7919()) / (float)var3.method_7936();
      }
   }

   return var1;
})).orElse((Object)null);
         case 5 -> var10000 = (class_1309)var1.stream().max(Comparator.comparing((var0) -> {
   float var1 = 0.0F;

   for(class_1799 var3 : var0.method_5661()) {
      if (var3 != null && var3.method_7909() != class_1802.field_8162) {
         var1 += (float)(var3.method_7936() - var3.method_7919()) / (float)var3.method_7936();
      }
   }

   return var1;
})).orElse((Object)null);
         case 6 -> var10000 = (class_1309)var1.stream().min(Comparator.comparing(this::getFOVAngle)).orElse((Object)null);
         default -> throw new MatchException((String)null, (Throwable)null);
      }

      return var10000;
   }

   private boolean skipEntity(class_1297 var1) {
      if (this.isBullet(var1)) {
         return false;
      } else if (!(var1 instanceof class_1309)) {
         return true;
      } else {
         class_1309 var2 = (class_1309)var1;
         if (!var2.method_29504() && var1.method_5805()) {
            if (!(var1 instanceof class_1531) && !(var1 instanceof class_1451)) {
               if (this.skipNotSelected(var1)) {
                  return true;
               } else if (!InteractionUtility.isVecInFOV(var2.method_19538(), this.fov.getValue())) {
                  return true;
               } else {
                  if (var1 instanceof class_1657) {
                     class_1657 var3 = (class_1657)var1;
                     if ((Boolean)this.antiBotNew.getValue()) {
                        boolean var4 = mc.method_1562().method_2871(var3.method_5667()) == null || var3.method_5767() || var3.field_6012 < 20 || var3.method_18798().method_1027() == (double)0.0F && var3.field_6012 < 60 || var3.method_6032() == var3.method_6063() && var3.field_6012 < 40 && !var3.method_6128();
                        if (var4) {
                           return true;
                        }
                     }

                     if (ModuleManager.antiBot.isEnabled() && AntiBot.bots.contains(var1)) {
                        return true;
                     }

                     if (var3 == mc.field_1724 || Managers.FRIEND.isFriend(var3)) {
                        return true;
                     }

                     if (var3.method_7337() && (Boolean)this.ignoreCreative.getValue()) {
                        return true;
                     }

                     if (var3.method_6096() == 0 && (Boolean)this.ignoreNaked.getValue()) {
                        return true;
                     }

                     if (var3.method_5767() && (Boolean)this.ignoreInvisible.getValue()) {
                        return true;
                     }

                     if (var3.method_22861() == mc.field_1724.method_22861() && (Boolean)this.ignoreTeam.getValue() && mc.field_1724.method_22861() != 16777215) {
                        return true;
                     }
                  }

                  return !this.isInRange(var1) || var1.method_16914() && (Boolean)this.ignoreNamed.getValue();
               }
            } else {
               return true;
            }
         } else {
            return true;
         }
      }
   }

   private boolean isBullet(class_1297 var1) {
      return (var1 instanceof class_1678 || var1 instanceof class_1674) && var1.method_5805() && PlayerUtility.squaredDistanceFromEyes(var1.method_19538()) < this.getSquaredRotateDistance() && (Boolean)this.Projectiles.getValue();
   }

   private boolean skipNotSelected(class_1297 var1) {
      if (var1 instanceof class_1621 && !(Boolean)this.Slimes.getValue()) {
         return true;
      } else {
         if (var1 instanceof class_1588) {
            class_1588 var2 = (class_1588)var1;
            if (!(Boolean)this.hostiles.getValue()) {
               return true;
            }

            if ((Boolean)this.onlyAngry.getValue()) {
               return !var2.method_7076(mc.field_1724);
            }
         }

         if (var1 instanceof class_1657 && !(Boolean)this.Players.getValue()) {
            return true;
         } else if (var1 instanceof class_1646 && !(Boolean)this.Villagers.getValue()) {
            return true;
         } else if (var1 instanceof class_1308 && !(Boolean)this.Mobs.getValue()) {
            return true;
         } else {
            return var1 instanceof class_1429 && !(Boolean)this.Animals.getValue();
         }
      }
   }

   private float getFOVAngle(@NotNull class_1309 var1) {
      float var2 = (float)class_3532.method_15338(Math.toDegrees(Math.atan2(var1.method_23321() - mc.field_1724.method_23321(), var1.method_23317() - mc.field_1724.method_23317())) - (double)90.0F);
      return Math.abs(var2 - class_3532.method_15393(mc.field_1724.method_36454()));
   }

   public void onRender3D(class_4587 var1) {
      if (this.haveWeapon() && target != null) {
         if ((Boolean)this.resolverVisualisation.getValue() && this.resolvedBox != null) {
            Render3DEngine.OUTLINE_QUEUE.add(new Render3DEngine.OutlineAction(this.resolvedBox, HudEditor.getColor(0), 1.0F));
         }

         if ((Boolean)this.clientLook.getValue() && this.rotationMode.getValue() != Aura.Mode.None) {
            mc.field_1724.method_36456((float)Render2DEngine.interpolate((double)mc.field_1724.field_5982, (double)this.rotationYaw, (double)Render3DEngine.getTickDelta()));
            mc.field_1724.method_36457((float)Render2DEngine.interpolate((double)mc.field_1724.field_6004, (double)this.rotationPitch, (double)Render3DEngine.getTickDelta()));
         }

      }
   }

   public boolean isAboveWater() {
      return mc.field_1724.method_5869() || mc.field_1687.method_8320(class_2338.method_49638(mc.field_1724.method_19538().method_1031((double)0.0F, -0.4, (double)0.0F))).method_26204() == class_2246.field_10382;
   }

   public float getAttackCooldownProgressPerTick() {
      return (float)((double)1.0F / mc.field_1724.method_45325(class_5134.field_23723) * (double)20.0F * (double)UniqueCore.TICK_TIMER * (double)((Boolean)this.tpsSync.getValue() ? Managers.SERVER.getTPSFactor() : 1.0F));
   }

   public float getAttackCooldown() {
      return class_3532.method_15363(((float)((ILivingEntity)mc.field_1724).getLastAttackedTicks() + (Float)this.attackBaseTime.getValue()) / this.getAttackCooldownProgressPerTick(), 0.0F, 1.0F);
   }

   public void pause() {
      this.pauseTimer.reset();
   }

   private boolean shouldRandomizeDelay() {
      return mc.field_1724.method_24828() || mc.field_1724.field_6017 < 0.12F || mc.field_1724.method_5681() || mc.field_1724.method_6128();
   }

   private boolean shouldRandomizeFallDistance() {
      return !this.shouldRandomizeDelay();
   }

   public static enum RayTrace {
      OFF,
      OnlyTarget,
      AllEntities;

      // $FF: synthetic method
      private static RayTrace[] $values() {
         return new RayTrace[]{OFF, OnlyTarget, AllEntities};
      }
   }

   public static enum Sort {
      LowestDistance,
      HighestDistance,
      LowestHealth,
      HighestHealth,
      LowestDurability,
      HighestDurability,
      FOV;

      // $FF: synthetic method
      private static Sort[] $values() {
         return new Sort[]{LowestDistance, HighestDistance, LowestHealth, HighestHealth, LowestDurability, HighestDurability, FOV};
      }
   }

   public static enum SprintMode {
      Off,
      Legit,
      HVH;

      // $FF: synthetic method
      private static SprintMode[] $values() {
         return new SprintMode[]{Off, Legit, HVH};
      }
   }

   public static enum Resolver {
      Off,
      Advantage,
      Predictive;

      // $FF: synthetic method
      private static Resolver[] $values() {
         return new Resolver[]{Off, Advantage, Predictive};
      }
   }

   public static enum Mode {
      Track,
      Grim,
      MLSAC,
      None;

      // $FF: synthetic method
      private static Mode[] $values() {
         return new Mode[]{Track, Grim, MLSAC, None};
      }
   }

   public static enum AttackHand {
      MainHand,
      OffHand,
      None;

      // $FF: synthetic method
      private static AttackHand[] $values() {
         return new AttackHand[]{MainHand, OffHand, None};
      }
   }

   public static enum AccelerateOnHit {
      Off,
      Yaw,
      Pitch,
      Both;

      // $FF: synthetic method
      private static AccelerateOnHit[] $values() {
         return new AccelerateOnHit[]{Off, Yaw, Pitch, Both};
      }
   }

   public static enum WallsBypass {
      Off,
      V1,
      V2,
      V3;

      // $FF: synthetic method
      private static WallsBypass[] $values() {
         return new WallsBypass[]{Off, V1, V2, V3};
      }
   }

   public static enum Timing {
      Modern,
      Legacy;

      // $FF: synthetic method
      private static Timing[] $values() {
         return new Timing[]{Modern, Legacy};
      }
   }

   public static class Position {
      public final double x;
      public final double y;
      public final double z;

      public Position(double var1, double var3, double var5) {
         this.x = var1;
         this.y = var3;
         this.z = var5;
      }

      public boolean shouldRemove() {
         return false;
      }
   }
}
