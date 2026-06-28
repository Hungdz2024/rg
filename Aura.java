package thunder.hack.features.modules.combat;

import baritone.api.BaritoneAPI;
import java.security.SecureRandom;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ThreadLocalRandom;
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
import net.minecraft.class_1743;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_1829;
import net.minecraft.class_1835;
import net.minecraft.class_1839;
import net.minecraft.class_2246;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_238;
import net.minecraft.class_243;
import net.minecraft.class_2596;
import net.minecraft.class_2663;
import net.minecraft.class_2708;
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
import thunder.hack.ThunderHack;
import thunder.hack.core.Managers;
import thunder.hack.core.manager.client.ModuleManager;
import thunder.hack.core.manager.player.PlayerManager;
import thunder.hack.events.impl.EventSync;
import thunder.hack.events.impl.PacketEvent;
import thunder.hack.events.impl.PlayerUpdateEvent;
import thunder.hack.features.modules.Module;
import thunder.hack.features.modules.client.ClientSettings;
import thunder.hack.features.modules.client.HudEditor;
import thunder.hack.gui.notification.Notification;
import thunder.hack.injection.accesors.ILivingEntity;
import thunder.hack.setting.Setting;
import thunder.hack.setting.impl.BooleanSettingGroup;
import thunder.hack.setting.impl.SettingGroup;
import thunder.hack.utility.Timer;
import thunder.hack.utility.interfaces.IOtherClientPlayerEntity;
import thunder.hack.utility.math.MathUtility;
import thunder.hack.utility.player.InteractionUtility;
import thunder.hack.utility.player.InventoryUtility;
import thunder.hack.utility.player.PlayerUtility;
import thunder.hack.utility.player.SearchInvResult;
import thunder.hack.utility.render.Render2DEngine;
import thunder.hack.utility.render.Render3DEngine;
import thunder.hack.utility.render.animation.CaptureMark;

public class Aura extends Module {
   public final Setting<Float> attackRange = new Setting<Float>("Range", 3.1F, 1.0F, 6.0F);
   public final Setting<Float> wallRange = new Setting<Float>("ThroughWallsRange", 3.1F, 0.0F, 6.0F);
   public final Setting<Float> elytraAimRange = new Setting<Float>("ElytraAimRange", 100.0F, 0.0F, 1000.0F);
   public final Setting<Sprint> sprintMode;
   public final Setting<WallsBypass> wallsBypass;
   public final Setting<Integer> fov;
   public final Setting<Mode> rotationMode;
   public final Setting<Integer> interactTicks;
   public final Setting<Switch> switchMode;
   public final Setting<Boolean> onlyWeapon;
   public final Setting<BooleanSettingGroup> smartCrit;
   public final Setting<Boolean> onlySpace;
   public final Setting<Boolean> autoJump;
   public final Setting<Boolean> shieldBreaker;
   public final Setting<Boolean> pauseWhileEating;
   public final Setting<Boolean> tpsSync;
   public final Setting<Boolean> clientLook;
   public final Setting<Boolean> pauseBaritone;
   public final Setting<BooleanSettingGroup> oldDelay;
   public final Setting<Integer> minCPS;
   public final Setting<Integer> maxCPS;
   public final Setting<Integer> cpsJitter;
   public final Setting<ESP> esp;
   public final Setting<SettingGroup> espGroup;
   public final Setting<Integer> espLength;
   public final Setting<Integer> espFactor;
   public final Setting<Float> espShaking;
   public final Setting<Float> espAmplitude;
   public final Setting<Sort> sort;
   public final Setting<Boolean> lockTarget;
   public final Setting<SettingGroup> advanced;
   public final Setting<Float> aimRange;
   public final Setting<Boolean> randomHitDelay;
   public final Setting<Boolean> pauseInInventory;
   public final Setting<RayTrace> rayTrace;
   public final Setting<Boolean> grimRayTrace;
   public final Setting<Boolean> unpressShield;
   public final Setting<Boolean> deathDisable;
   public final Setting<Boolean> tpDisable;
   public final Setting<Boolean> pullDown;
   public final Setting<Boolean> onlyJumpBoost;
   public final Setting<Float> pullValue;
   public final Setting<AttackHand> attackHand;
   public final Setting<Resolver> resolver;
   public final Setting<Integer> backTicks;
   public final Setting<Boolean> resolverVisualisation;
   public final Setting<Float> attackCooldown;
   public final Setting<Float> attackBaseTime;
   public final Setting<Integer> attackTickLimit;
   public final Setting<Float> critFallDistance;
   public final Setting<SettingGroup> targets;
   public final Setting<Boolean> Players;
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
   public static int strafeBlockTicks = 0;
   public float rotationYaw;
   public float rotationPitch;
   public float pitchAcceleration;
   private float cameraYaw;
   private float aimTargetY;
   private int aimShiftTicks;
   private float yawVelocity;
   private float pitchVelocity;
   private int microPauseTicks;
   private int humanLagTicks;
   private int aimOffsetTicks;
   private float aimOffsetYaw;
   private float aimOffsetPitch;
   private class_243 rotationPoint;
   private class_243 rotationMotion;
   private int hitTicks;
   private int trackticks;
   private boolean lookingAtHitbox;
   private final Timer delayTimer;
   private final Timer pauseTimer;
   public class_238 resolvedBox;
   static boolean wasTargeted = false;
   private int waitingForCritTicks;
   private static final int MAX_CRIT_WAIT_TICKS = 15;
   private boolean prevOnGround;
   private double prevVelY;
   private boolean knockbackInAir;
   private int hvhSequence;
   private double lastPlayerX;
   private double lastPlayerZ;
   private int sprintResetTicks;
   private boolean hvhNeedRestoreSprint;
   private boolean waitingSprintReset;
   private int strafePauseTicks;
   private float sessionDriftYaw;
   private float sessionDriftPitch;
   private int driftResetTicks;
   private int inattentionTicks;
   private float jitterYaw;
   private float jitterPitch;
   private boolean newTargetLock;
   private float pitchSweepProgress;
   private class_1297 lastTarget;
   private SecureRandom legitSecureRandom;
   private float legitLastYawDelta;
   private float legitLastPitchDelta;
   private int legitRotationCount;
   private float legitPitchNoiseAcc;
   private float legitYawNoiseAcc;
   private float legitLastSpeedFactor;
   private float legitDynamicSmoothing;
   private double savedVelX;
   private double savedVelZ;

   public Aura() {
      super("Aura", Module.Category.COMBAT);
      this.sprintMode = new Setting<Sprint>("Sprint", Aura.Sprint.Normal);
      this.wallsBypass = new Setting<WallsBypass>("WallsBypass", Aura.WallsBypass.Off, (v) -> this.getWallRange() > 0.0F);
      this.fov = new Setting<Integer>("FOV", 180, 1, 180);
      this.rotationMode = new Setting<Mode>("RotationMode", Aura.Mode.Beta);
      this.interactTicks = new Setting<Integer>("InteractTicks", 3, 1, 10, (v) -> this.rotationMode.getValue() == Aura.Mode.Interact);
      this.switchMode = new Setting<Switch>("AutoWeapon", Aura.Switch.None);
      this.onlyWeapon = new Setting<Boolean>("OnlyWeapon", false, (v) -> this.switchMode.getValue() != Aura.Switch.Silent);
      this.smartCrit = new Setting<BooleanSettingGroup>("SmartCrit", new BooleanSettingGroup(true));
      this.onlySpace = (new Setting<Boolean>("OnlyCrit", false)).addToGroup(this.smartCrit);
      this.autoJump = (new Setting<Boolean>("AutoJump", false)).addToGroup(this.smartCrit);
      this.shieldBreaker = new Setting<Boolean>("ShieldBreaker", true);
      this.pauseWhileEating = new Setting<Boolean>("PauseWhileEating", false);
      this.tpsSync = new Setting<Boolean>("TPSSync", false);
      this.clientLook = new Setting<Boolean>("ClientLook", false);
      this.pauseBaritone = new Setting<Boolean>("PauseBaritone", false);
      this.oldDelay = new Setting<BooleanSettingGroup>("OldDelay", new BooleanSettingGroup(false));
      this.minCPS = (new Setting<Integer>("MinCPS", 7, 1, 20)).addToGroup(this.oldDelay);
      this.maxCPS = (new Setting<Integer>("MaxCPS", 12, 1, 20)).addToGroup(this.oldDelay);
      this.cpsJitter = (new Setting<Integer>("CPSJitter", 20, 0, 80)).addToGroup(this.oldDelay);
      this.esp = new Setting<ESP>("ESP", Aura.ESP.ThunderHack);
      this.espGroup = new Setting<SettingGroup>("ESPSettings", new SettingGroup(false, 0), (v) -> this.esp.is(Aura.ESP.ThunderHackV2));
      this.espLength = (new Setting<Integer>("ESPLength", 14, 1, 40, (v) -> this.esp.is(Aura.ESP.ThunderHackV2))).addToGroup(this.espGroup);
      this.espFactor = (new Setting<Integer>("ESPFactor", 8, 1, 20, (v) -> this.esp.is(Aura.ESP.ThunderHackV2))).addToGroup(this.espGroup);
      this.espShaking = (new Setting<Float>("ESPShaking", 1.8F, 1.5F, 10.0F, (v) -> this.esp.is(Aura.ESP.ThunderHackV2))).addToGroup(this.espGroup);
      this.espAmplitude = (new Setting<Float>("ESPAmplitude", 3.0F, 0.1F, 8.0F, (v) -> this.esp.is(Aura.ESP.ThunderHackV2))).addToGroup(this.espGroup);
      this.sort = new Setting<Sort>("Sort", Aura.Sort.LowestDistance);
      this.lockTarget = new Setting<Boolean>("LockTarget", true);
      this.advanced = new Setting<SettingGroup>("Advanced", new SettingGroup(false, 0));
      this.aimRange = (new Setting<Float>("AimRange", 3.1F, 0.0F, 20.0F)).addToGroup(this.advanced);
      this.randomHitDelay = (new Setting<Boolean>("RandomHitDelay", false)).addToGroup(this.advanced);
      this.pauseInInventory = (new Setting<Boolean>("PauseInInventory", true)).addToGroup(this.advanced);
      this.rayTrace = (new Setting<RayTrace>("RayTrace", Aura.RayTrace.OnlyTarget)).addToGroup(this.advanced);
      this.grimRayTrace = (new Setting<Boolean>("GrimRayTrace", true)).addToGroup(this.advanced);
      this.unpressShield = (new Setting<Boolean>("UnpressShield", true)).addToGroup(this.advanced);
      this.deathDisable = (new Setting<Boolean>("DisableOnDeath", true)).addToGroup(this.advanced);
      this.tpDisable = (new Setting<Boolean>("TPDisable", false)).addToGroup(this.advanced);
      this.pullDown = (new Setting<Boolean>("FastFall", false)).addToGroup(this.advanced);
      this.onlyJumpBoost = (new Setting<Boolean>("OnlyJumpBoost", false, (v) -> (Boolean)this.pullDown.getValue())).addToGroup(this.advanced);
      this.pullValue = (new Setting<Float>("PullValue", 3.0F, 0.0F, 20.0F, (v) -> (Boolean)this.pullDown.getValue())).addToGroup(this.advanced);
      this.attackHand = (new Setting<AttackHand>("AttackHand", Aura.AttackHand.MainHand)).addToGroup(this.advanced);
      this.resolver = (new Setting<Resolver>("Resolver", Aura.Resolver.Advantage)).addToGroup(this.advanced);
      this.backTicks = (new Setting<Integer>("BackTicks", 4, 1, 20, (v) -> this.resolver.is(Aura.Resolver.BackTrack))).addToGroup(this.advanced);
      this.resolverVisualisation = (new Setting<Boolean>("ResolverVisualisation", false, (v) -> !this.resolver.is(Aura.Resolver.Off))).addToGroup(this.advanced);
      this.attackCooldown = (new Setting<Float>("AttackCooldown", 0.93F, 0.5F, 1.0F)).addToGroup(this.advanced);
      this.attackBaseTime = (new Setting<Float>("AttackBaseTime", 0.5F, 0.0F, 2.0F)).addToGroup(this.advanced);
      this.attackTickLimit = (new Setting<Integer>("AttackTickLimit", 11, 0, 20)).addToGroup(this.advanced);
      this.critFallDistance = (new Setting<Float>("CritFallDistance", 0.0F, 0.0F, 1.0F)).addToGroup(this.advanced);
      this.targets = new Setting<SettingGroup>("Targets", new SettingGroup(false, 0));
      this.Players = (new Setting<Boolean>("Players", true)).addToGroup(this.targets);
      this.Mobs = (new Setting<Boolean>("Mobs", true)).addToGroup(this.targets);
      this.Animals = (new Setting<Boolean>("Animals", true)).addToGroup(this.targets);
      this.Villagers = (new Setting<Boolean>("Villagers", true)).addToGroup(this.targets);
      this.Slimes = (new Setting<Boolean>("Slimes", true)).addToGroup(this.targets);
      this.hostiles = (new Setting<Boolean>("Hostiles", true)).addToGroup(this.targets);
      this.onlyAngry = (new Setting<Boolean>("OnlyAngryHostiles", true, (v) -> (Boolean)this.hostiles.getValue())).addToGroup(this.targets);
      this.Projectiles = (new Setting<Boolean>("Projectiles", true)).addToGroup(this.targets);
      this.ignoreInvisible = (new Setting<Boolean>("IgnoreInvisibleEntities", false)).addToGroup(this.targets);
      this.ignoreNamed = (new Setting<Boolean>("IgnoreNamed", false)).addToGroup(this.targets);
      this.ignoreTeam = (new Setting<Boolean>("IgnoreTeam", false)).addToGroup(this.targets);
      this.ignoreCreative = (new Setting<Boolean>("IgnoreCreative", true)).addToGroup(this.targets);
      this.ignoreNaked = (new Setting<Boolean>("IgnoreNaked", false)).addToGroup(this.targets);
      this.ignoreShield = (new Setting<Boolean>("AttackShieldingEntities", true)).addToGroup(this.targets);
      this.pitchAcceleration = 1.0F;
      this.aimTargetY = 0.5F;
      this.aimShiftTicks = 0;
      this.yawVelocity = 0.0F;
      this.pitchVelocity = 0.0F;
      this.microPauseTicks = 0;
      this.humanLagTicks = 0;
      this.aimOffsetTicks = 0;
      this.aimOffsetYaw = 0.0F;
      this.aimOffsetPitch = 0.0F;
      this.rotationPoint = class_243.field_1353;
      this.rotationMotion = class_243.field_1353;
      this.delayTimer = new Timer();
      this.pauseTimer = new Timer();
      this.waitingForCritTicks = 0;
      this.prevOnGround = true;
      this.prevVelY = (double)0.0F;
      this.knockbackInAir = false;
      this.hvhSequence = 0;
      this.lastPlayerX = (double)0.0F;
      this.lastPlayerZ = (double)0.0F;
      this.sprintResetTicks = 0;
      this.hvhNeedRestoreSprint = false;
      this.waitingSprintReset = false;
      this.strafePauseTicks = 0;
      this.sessionDriftYaw = 0.0F;
      this.sessionDriftPitch = 0.0F;
      this.driftResetTicks = 0;
      this.inattentionTicks = 0;
      this.jitterYaw = 0.0F;
      this.jitterPitch = 0.0F;
      this.newTargetLock = true;
      this.pitchSweepProgress = 0.0F;
      this.lastTarget = null;
      this.legitSecureRandom = new SecureRandom();
      this.legitLastYawDelta = 0.0F;
      this.legitLastPitchDelta = 0.0F;
      this.legitRotationCount = 0;
      this.legitPitchNoiseAcc = 0.0F;
      this.legitYawNoiseAcc = 0.0F;
      this.legitLastSpeedFactor = 1.0F;
      this.legitDynamicSmoothing = 0.35F;
      this.savedVelX = (double)0.0F;
      this.savedVelZ = (double)0.0F;
   }

   private boolean isElytraFlying() {
      return mc.field_1724 != null && mc.field_1724.method_6128();
   }

   private static float snapGcd(float delta, double gcd) {
      return gcd < 1.0E-6 ? delta : (float)((double)Math.round((double)delta / gcd) * gcd);
   }

   private static float cubicBezier(float p0, float p1, float p2, float p3, float t) {
      float u = 1.0F - t;
      return u * u * u * p0 + 3.0F * u * u * t * p1 + 3.0F * u * t * t * p2 + t * t * t * p3;
   }

   private static float cubicBezierDeriv(float p0, float p1, float p2, float p3, float t) {
      float u = 1.0F - t;
      return 3.0F * (u * u * (p1 - p0) + 2.0F * u * t * (p2 - p1) + t * t * (p3 - p2));
   }

   private float getRange() {
      return (Float)this.attackRange.getValue();
   }

   private float getWallRange() {
      return (Float)this.wallRange.getValue();
   }

   private float getAimRange() {
      return this.isElytraFlying() ? (Float)this.elytraAimRange.getValue() : (Float)this.attackRange.getValue() + (Float)this.aimRange.getValue();
   }

   public void auraLogic() {
      if (!this.haveWeapon()) {
         target = null;
      } else {
         this.handleKill();
         this.updateTarget();
         if (target == null) {
            this.waitingForCritTicks = 0;
         } else {
            if (!mc.field_1690.field_1903.method_1434() && mc.field_1724.method_24828() && (Boolean)this.autoJump.getValue() && (this.getAttackCooldown() >= 0.85F || ((BooleanSettingGroup)this.oldDelay.getValue()).isEnabled())) {
               mc.field_1724.method_6043();
            }

            boolean readyForAttack;
            if ((Boolean)this.grimRayTrace.getValue()) {
               readyForAttack = this.autoCrit() && (this.lookingAtHitbox || this.skipRayTraceCheck());
               this.calcRotations(this.autoCrit());
            } else {
               this.calcRotations(this.autoCrit());
               readyForAttack = this.autoCrit() && (this.lookingAtHitbox || this.skipRayTraceCheck());
            }

            if (readyForAttack) {
               this.waitingForCritTicks = 0;
               if (this.shieldBreaker(false)) {
                  return;
               }

               boolean[] playerState;
               label44: {
                  playerState = this.preAttack();
                  class_1297 var4 = target;
                  if (var4 instanceof class_1657) {
                     class_1657 pl = (class_1657)var4;
                     if (pl.method_6115() && pl.method_6079().method_7909() == class_1802.field_8255 && !(Boolean)this.ignoreShield.getValue()) {
                        break label44;
                     }
                  }

                  this.attack();
               }

               this.postAttack(playerState[0], playerState[1]);
            }

         }
      }
   }

   private boolean haveWeapon() {
      if (ModuleManager.maceSwap.isEnabled() && ModuleManager.maceSwap.findMaceSlot() != -1) {
         return true;
      } else {
         class_1792 handItem = mc.field_1724.method_6047().method_7909();
         if (!(Boolean)this.onlyWeapon.getValue()) {
            return true;
         } else if (this.switchMode.getValue() == Aura.Switch.None) {
            return handItem instanceof class_1829 || handItem instanceof class_1743 || handItem instanceof class_1835;
         } else {
            return InventoryUtility.getSwordHotBar().found() || InventoryUtility.getAxeHotBar().found();
         }
      }
   }

   private boolean skipRayTraceCheck() {
      return this.rotationMode.getValue() == Aura.Mode.None || this.rayTrace.getValue() == Aura.RayTrace.OFF || this.rotationMode.is(Aura.Mode.Grim) || this.rotationMode.is(Aura.Mode.Interact) && ((Integer)this.interactTicks.getValue() <= 1 || mc.field_1687.method_20812(mc.field_1724, mc.field_1724.method_5829().method_1009((double)-0.25F, (double)0.0F, (double)-0.25F).method_989((double)0.0F, (double)1.0F, (double)0.0F)).iterator().hasNext());
   }

   public void attack() {
      if (ModuleManager.fakeLag.isEnabled()) {
         ModuleManager.fakeLag.flushForAttack();
      }

      if (this.rotationMode.is(Aura.Mode.Grim)) {
         this.sendPacket(new class_2828.class_2830(mc.field_1724.method_23317(), mc.field_1724.method_23318(), mc.field_1724.method_23321(), this.rotationYaw, this.rotationPitch, mc.field_1724.method_24828()));
      }

      if (ModuleManager.maceSwap.isEnabled()) {
         class_1297 var2 = target;
         if (var2 instanceof class_1657) {
            class_1657 pl = (class_1657)var2;
            if (pl.method_6115() && (pl.method_6079().method_7909() == class_1802.field_8255 || pl.method_6047().method_7909() == class_1802.field_8255)) {
               int axeSlot = InventoryUtility.getAxe().slot();
               if (axeSlot != -1) {
                  if (axeSlot >= 9) {
                     mc.field_1761.method_2906(mc.field_1724.field_7512.field_7763, axeSlot, mc.field_1724.method_31548().field_7545, class_1713.field_7791, mc.field_1724);
                     this.sendPacket(new class_2815(mc.field_1724.field_7512.field_7763));
                     mc.field_1761.method_2918(mc.field_1724, target);
                     this.swingHand();
                     mc.field_1761.method_2906(mc.field_1724.field_7512.field_7763, axeSlot, mc.field_1724.method_31548().field_7545, class_1713.field_7791, mc.field_1724);
                     this.sendPacket(new class_2815(mc.field_1724.field_7512.field_7763));
                  } else {
                     this.sendPacket(new class_2868(axeSlot));
                     mc.field_1761.method_2918(mc.field_1724, target);
                     this.swingHand();
                     this.sendPacket(new class_2868(mc.field_1724.method_31548().field_7545));
                  }

                  this.hitTicks = this.getHitTicks();
                  strafeBlockTicks = 5;
                  return;
               }
            }
         }
      }

      Criticals.cancelCrit = true;
      ModuleManager.criticals.doCrit();
      int prevSlot = this.switchMethod();
      strafeBlockTicks = 5;
      boolean doSprintStop = mc.field_1724.method_5624() && !mc.field_1724.method_24828() && mc.field_1724.method_18798().field_1351 < (double)0.0F && mc.field_1724.field_6017 > 0.01F && !ModuleManager.criticals.isEnabled();
      if (doSprintStop) {
         this.sendPacket(new class_2848(mc.field_1724, class_2849.field_12985));
      }

      mc.field_1761.method_2918(mc.field_1724, target);
      Criticals.cancelCrit = false;
      this.swingHand();
      this.hitTicks = this.getHitTicks();
      if (doSprintStop) {
         this.sendPacket(new class_2848(mc.field_1724, class_2849.field_12981));
      }

      if (prevSlot != -1) {
         InventoryUtility.switchTo(prevSlot);
      }

      this.legitRandomizeSeed();
      if (ModuleManager.maceSwap.isEnabled() && ModuleManager.maceSwap.isSwapped()) {
         ModuleManager.maceSwap.switchBack();
      }

   }

   private boolean @NotNull [] preAttack() {
      boolean blocking = mc.field_1724.method_6115() && mc.field_1724.method_6030().method_7909().method_7853(mc.field_1724.method_6030()) == class_1839.field_8949;
      if (blocking && (Boolean)this.unpressShield.getValue()) {
         this.sendPacket(new class_2846(class_2847.field_12974, class_2338.field_10980, class_2350.field_11033));
      }

      boolean wasSprinting = mc.field_1724.method_5624();
      this.savedVelX = mc.field_1724.method_18798().field_1352;
      this.savedVelZ = mc.field_1724.method_18798().field_1350;
      return new boolean[]{blocking, wasSprinting};
   }

   public void postAttack(boolean block, boolean wasSprinting) {
      if (this.sprintMode.getValue() != Aura.Sprint.Legit && wasSprinting) {
         class_243 vel = mc.field_1724.method_18798();
         double retainX = this.savedVelX * 0.6 + vel.field_1352 * 0.4;
         double retainZ = this.savedVelZ * 0.6 + vel.field_1350 * 0.4;
         mc.field_1724.method_18800(retainX, vel.field_1351, retainZ);
      }

      if (this.sprintMode.getValue() == Aura.Sprint.HvH && mc.field_1724.method_5624()) {
         this.sendPacket(new class_2848(mc.field_1724, class_2849.field_12985));
         this.sendPacket(new class_2848(mc.field_1724, class_2849.field_12981));
         if (mc.field_1724.method_24828()) {
            mc.field_1724.method_6043();
         }
      }

      if (block && (Boolean)this.unpressShield.getValue()) {
         this.sendSequencedPacket((id) -> new class_2886(class_1268.field_5810, id, this.rotationYaw, this.rotationPitch));
      }

      if (this.rotationMode.is(Aura.Mode.Grim)) {
         this.sendPacket(new class_2828.class_2830(mc.field_1724.method_23317(), mc.field_1724.method_23318(), mc.field_1724.method_23321(), mc.field_1724.method_36454(), mc.field_1724.method_36455(), mc.field_1724.method_24828()));
      }

   }

   public void resolvePlayers() {
      if (this.resolver.not(Aura.Resolver.Off)) {
         for(class_1657 player : mc.field_1687.method_18456()) {
            if (player instanceof class_745) {
               ((IOtherClientPlayerEntity)player).resolve(this.resolver.getValue());
            }
         }
      }

   }

   public void restorePlayers() {
      if (this.resolver.not(Aura.Resolver.Off)) {
         for(class_1657 player : mc.field_1687.method_18456()) {
            if (player instanceof class_745) {
               ((IOtherClientPlayerEntity)player).releaseResolver();
            }
         }
      }

   }

   public void handleKill() {
      if (target instanceof class_1309 && (((class_1309)target).method_6032() <= 0.0F || ((class_1309)target).method_29504())) {
         Managers.NOTIFICATION.publicity("Aura", ClientSettings.isRu() ? "Цель успешно нейтрализована!" : "Target successfully neutralized!", 3, Notification.Type.SUCCESS);
      }

   }

   private int switchMethod() {
      if (ModuleManager.maceSwap.isEnabled() && ModuleManager.maceSwap.isSwapped()) {
         return -1;
      } else {
         int prevSlot = -1;
         SearchInvResult swordResult = InventoryUtility.getSwordHotBar();
         if (swordResult.found() && this.switchMode.getValue() != Aura.Switch.None) {
            if (this.switchMode.getValue() == Aura.Switch.Silent) {
               prevSlot = mc.field_1724.method_31548().field_7545;
            }

            swordResult.switchTo();
         }

         return prevSlot;
      }
   }

   private int getHitTicks() {
      return ((BooleanSettingGroup)this.oldDelay.getValue()).isEnabled() ? 1 + (int)(20.0F / MathUtility.random((float)(Integer)this.minCPS.getValue(), (float)(Integer)this.maxCPS.getValue())) : (this.shouldRandomizeDelay() ? (int)MathUtility.random(11.0F, 13.0F) : (Integer)this.attackTickLimit.getValue());
   }

   @EventHandler
   public void onUpdate(PlayerUpdateEvent e) {
      if (this.pauseTimer.passedMs(1000L)) {
         if (!mc.field_1724.method_6115() || !(Boolean)this.pauseWhileEating.getValue()) {
            if ((Boolean)this.pauseBaritone.getValue() && ThunderHack.baritone) {
               boolean isTargeted = target != null;
               if (isTargeted && !wasTargeted) {
                  BaritoneAPI.getProvider().getPrimaryBaritone().getCommandManager().execute("pause");
                  wasTargeted = true;
               } else if (!isTargeted && wasTargeted) {
                  BaritoneAPI.getProvider().getPrimaryBaritone().getCommandManager().execute("resume");
                  wasTargeted = false;
               }
            }

            this.resolvePlayers();
            this.auraLogic();
            this.restorePlayers();
            --this.hitTicks;
            if (strafeBlockTicks > 0) {
               --strafeBlockTicks;
            }

            boolean curOnGround = mc.field_1724.method_24828();
            double curVelY = mc.field_1724.method_18798().field_1351;
            if (this.prevOnGround && !curOnGround && curVelY >= (double)0.25F && !(Boolean)this.autoJump.getValue() && !mc.field_1690.field_1903.method_1434()) {
               this.knockbackInAir = true;
            }

            if (curOnGround) {
               this.knockbackInAir = false;
            }

            this.prevOnGround = curOnGround;
            this.prevVelY = curVelY;
         }
      }
   }

   @EventHandler
   public void onSync(EventSync e) {
      if (this.pauseTimer.passedMs(1000L)) {
         if (!mc.field_1724.method_6115() || !(Boolean)this.pauseWhileEating.getValue()) {
            if (this.haveWeapon()) {
               if (target != null && this.rotationMode.getValue() != Aura.Mode.None && this.rotationMode.getValue() != Aura.Mode.Grim) {
                  this.cameraYaw = mc.field_1724.method_36454();
                  mc.field_1724.method_36456(this.rotationYaw);
                  mc.field_1724.method_36457(this.rotationPitch);
               } else {
                  this.rotationYaw = mc.field_1724.method_36454();
                  this.rotationPitch = mc.field_1724.method_36455();
               }

               if (((BooleanSettingGroup)this.oldDelay.getValue()).isEnabled() && (Integer)this.minCPS.getValue() > (Integer)this.maxCPS.getValue()) {
                  this.minCPS.setValue(this.maxCPS.getValue());
               }

               if (target != null && (Boolean)this.pullDown.getValue() && (mc.field_1724.method_6059(class_1294.field_5913) || !(Boolean)this.onlyJumpBoost.getValue())) {
                  mc.field_1724.method_5762((double)0.0F, (double)(-(Float)this.pullValue.getValue() / 1000.0F), (double)0.0F);
               }

            }
         }
      }
   }

   @EventHandler
   public void onPacketSend(PacketEvent.@NotNull Send e) {
      class_2596 var3 = e.getPacket();
      if (var3 instanceof class_2824 pie) {
         if (Criticals.getInteractType(pie) != Criticals.InteractType.ATTACK && target != null) {
            e.cancel();
         }
      }

   }

   @EventHandler
   public void onPacketReceive(PacketEvent.@NotNull Receive e) {
      class_2596 var3 = e.getPacket();
      if (var3 instanceof class_2663 status) {
         if (status.method_11470() == 30 && status.method_11469(mc.field_1687) != null && target != null && status.method_11469(mc.field_1687) == target) {
            Managers.NOTIFICATION.publicity("Aura", ClientSettings.isRu() ? "Успешно сломали щит игроку " + target.method_5477().getString() : "Succesfully destroyed " + target.method_5477().getString() + "'s shield", 2, Notification.Type.SUCCESS);
         }
      }

      if (e.getPacket() instanceof class_2708 && (Boolean)this.tpDisable.getValue()) {
         this.disable(ClientSettings.isRu() ? "Отключаю из-за телепортации!" : "Disabling due to tp!");
      }

      var3 = e.getPacket();
      if (var3 instanceof class_2663 pac) {
         if (pac.method_11470() == 3 && pac.method_11469(mc.field_1687) == mc.field_1724 && (Boolean)this.deathDisable.getValue()) {
            this.disable(ClientSettings.isRu() ? "Отключаю из-за смерти!" : "Disabling due to death!");
         }
      }

   }

   public void onEnable() {
      target = null;
      this.lookingAtHitbox = false;
      this.rotationPoint = class_243.field_1353;
      this.rotationMotion = class_243.field_1353;
      this.aimTargetY = 0.5F;
      this.aimShiftTicks = 0;
      this.yawVelocity = 0.0F;
      this.pitchVelocity = 0.0F;
      this.microPauseTicks = 0;
      this.humanLagTicks = 0;
      this.aimOffsetTicks = 0;
      this.aimOffsetYaw = 0.0F;
      this.aimOffsetPitch = 0.0F;
      this.rotationYaw = mc.field_1724.method_36454();
      this.rotationPitch = mc.field_1724.method_36455();
      this.cameraYaw = mc.field_1724.method_36454();
      this.waitingForCritTicks = 0;
      this.knockbackInAir = false;
      this.prevOnGround = true;
      this.prevVelY = (double)0.0F;
      this.hvhSequence = 0;
      this.lastPlayerX = mc.field_1724.method_23317();
      this.lastPlayerZ = mc.field_1724.method_23321();
      this.sprintResetTicks = 0;
      this.waitingSprintReset = false;
      this.strafePauseTicks = 0;
      this.sessionDriftYaw = 0.0F;
      this.sessionDriftPitch = 0.0F;
      this.driftResetTicks = 0;
      this.inattentionTicks = 0;
      this.jitterYaw = 0.0F;
      this.jitterPitch = 0.0F;
      this.newTargetLock = true;
      this.pitchSweepProgress = 0.0F;
      this.lastTarget = null;
      this.legitSecureRandom = new SecureRandom();
      this.legitLastYawDelta = 0.0F;
      this.legitLastPitchDelta = 0.0F;
      this.legitRotationCount = 0;
      this.legitPitchNoiseAcc = 0.0F;
      this.legitYawNoiseAcc = 0.0F;
      this.legitLastSpeedFactor = 1.0F;
      this.legitDynamicSmoothing = 0.35F;
      this.delayTimer.reset();
   }

   private boolean autoCrit() {
      if (this.hitTicks > 0) {
         return false;
      } else if ((Boolean)this.pauseInInventory.getValue() && Managers.PLAYER.inInventory) {
         return false;
      } else if (this.getAttackCooldown() < (Float)this.attackCooldown.getValue() && !((BooleanSettingGroup)this.oldDelay.getValue()).isEnabled()) {
         return false;
      } else if (!((BooleanSettingGroup)this.smartCrit.getValue()).isEnabled()) {
         return true;
      } else if (ModuleManager.criticals.isEnabled()) {
         return true;
      } else {
         boolean cannotCrit = mc.field_1724.method_31549().field_7479 || mc.field_1724.method_6059(class_1294.field_5919) || Managers.PLAYER.isInWeb();
         if (cannotCrit) {
            return true;
         } else {
            boolean inSpecialFlight = mc.field_1724.method_6128() || ModuleManager.elytraPlus.isEnabled() || mc.field_1724.method_6059(class_1294.field_5906);
            if (inSpecialFlight) {
               if (!mc.field_1724.method_24828() && mc.field_1724.method_18798().field_1351 < (double)0.0F && mc.field_1724.field_6017 > 0.01F) {
                  return true;
               } else {
                  ++this.waitingForCritTicks;
                  return this.waitingForCritTicks >= 3;
               }
            } else if (!mc.field_1724.method_5771() && !mc.field_1724.method_5869()) {
               if (!mc.field_1690.field_1903.method_1434() && this.isAboveWater()) {
                  return true;
               } else if (ModuleManager.maceSwap.isEnabled() && ModuleManager.maceSwap.isSwapped()) {
                  float maceThreshold = mc.field_1724.method_6128() ? 1.5F : 3.0F;
                  return !mc.field_1724.method_24828() && mc.field_1724.method_18798().field_1351 < (double)0.0F && mc.field_1724.field_6017 >= maceThreshold;
               } else {
                  boolean isFalling = !mc.field_1724.method_24828() && mc.field_1724.method_18798().field_1351 < (double)0.0F;
                  if (isFalling && mc.field_1724.field_6017 > 0.01F) {
                     return true;
                  } else if (this.knockbackInAir && isFalling) {
                     return true;
                  } else if ((Boolean)this.onlySpace.getValue() && mc.field_1724.method_24828()) {
                     return false;
                  } else if ((Boolean)this.autoJump.getValue()) {
                     return false;
                  } else {
                     class_1297 var5 = target;
                     if (var5 instanceof class_1309) {
                        class_1309 livingTarget = (class_1309)var5;
                        float targetHP = livingTarget.method_6032() + livingTarget.method_6067();
                        float actualNonCritDmg = this.calculateActualDamage(this.getWeaponDamage(), livingTarget);
                        if (actualNonCritDmg * 1.2F >= targetHP) {
                           return true;
                        }
                     }

                     if (this.sprintMode.getValue() == Aura.Sprint.HvH) {
                        return !mc.field_1724.method_24828() && mc.field_1724.method_18798().field_1351 < (double)0.0F && mc.field_1724.field_6017 > 0.01F;
                     } else if (mc.field_1724.method_24828()) {
                        byte var10000;
                        switch (((Sprint)this.sprintMode.getValue()).ordinal()) {
                           case 0 -> var10000 = 3;
                           case 1 -> var10000 = 2;
                           default -> var10000 = 2;
                        }

                        int maxWait = var10000;
                        ++this.waitingForCritTicks;
                        return this.waitingForCritTicks >= maxWait;
                     } else {
                        return mc.field_1724.method_18798().field_1351 < (double)0.0F && mc.field_1724.field_6017 > 0.01F;
                     }
                  }
               }
            } else {
               return true;
            }
         }
      }
   }

   private boolean hasCritSource() {
      if ((Boolean)this.autoJump.getValue()) {
         return true;
      } else if (mc.field_1690.field_1903.method_1434()) {
         return true;
      } else if ((Boolean)this.onlySpace.getValue()) {
         return false;
      } else if (ModuleManager.speed.isEnabled() && !mc.field_1724.method_24828()) {
         return true;
      } else {
         return !mc.field_1724.method_24828();
      }
   }

   private int estimateCritWaitTicks() {
      if (mc.field_1724.method_24828()) {
         boolean hasJumpBoost = mc.field_1724.method_6059(class_1294.field_5913);
         return hasJumpBoost ? 14 : 10;
      } else {
         double vy = mc.field_1724.method_18798().field_1351;
         if (vy < (double)0.0F && mc.field_1724.field_6017 > 0.0F) {
            float minFall = this.shouldRandomizeFallDistance() ? 0.1F : (Float)this.critFallDistance.getValue();
            float remaining = minFall - mc.field_1724.field_6017;
            return remaining <= 0.0F ? 1 : (int)Math.max((double)1.0F, Math.ceil((double)remaining / Math.abs(vy)));
         } else if (!(vy > (double)0.0F)) {
            return 3;
         } else {
            int ticksToApex = 0;
            double simVy = vy;

            while(simVy > (double)0.0F) {
               simVy = (simVy - 0.08) * 0.98;
               ++ticksToApex;
               if (ticksToApex > 20) {
                  break;
               }
            }

            return ticksToApex + 3;
         }
      }
   }

   private float getWeaponDamage() {
      return (float)mc.field_1724.method_45325(class_5134.field_23721);
   }

   private float calculateActualDamage(float damage, class_1309 target) {
      float armor = (float)target.method_6096();
      float toughness = (float)target.method_45325(class_5134.field_23725);
      float armorReduction = Math.min(20.0F, Math.max(armor / 5.0F, armor - damage / (2.0F + toughness / 4.0F)));
      damage *= 1.0F - armorReduction / 25.0F;
      damage *= 0.6F;
      return Math.max(damage, 0.0F);
   }

   private boolean shieldBreaker(boolean instant) {
      int axeSlot = InventoryUtility.getAxe().slot();
      if (axeSlot == -1) {
         return false;
      } else if (!(Boolean)this.shieldBreaker.getValue()) {
         return false;
      } else if (!(target instanceof class_1657)) {
         return false;
      } else if (!((class_1657)target).method_6115() && !instant) {
         return false;
      } else if (((class_1657)target).method_6079().method_7909() != class_1802.field_8255 && ((class_1657)target).method_6047().method_7909() != class_1802.field_8255) {
         return false;
      } else {
         if (axeSlot >= 9) {
            mc.field_1761.method_2906(mc.field_1724.field_7512.field_7763, axeSlot, mc.field_1724.method_31548().field_7545, class_1713.field_7791, mc.field_1724);
            this.sendPacket(new class_2815(mc.field_1724.field_7512.field_7763));
            mc.field_1761.method_2918(mc.field_1724, target);
            this.swingHand();
            mc.field_1761.method_2906(mc.field_1724.field_7512.field_7763, axeSlot, mc.field_1724.method_31548().field_7545, class_1713.field_7791, mc.field_1724);
            this.sendPacket(new class_2815(mc.field_1724.field_7512.field_7763));
         } else {
            this.sendPacket(new class_2868(axeSlot));
            mc.field_1761.method_2918(mc.field_1724, target);
            this.swingHand();
            this.sendPacket(new class_2868(mc.field_1724.method_31548().field_7545));
         }

         this.hitTicks = 10;
         strafeBlockTicks = 5;
         return true;
      }
   }

   private void swingHand() {
      switch (((AttackHand)this.attackHand.getValue()).ordinal()) {
         case 0 -> mc.field_1724.method_6104(class_1268.field_5808);
         case 1 -> mc.field_1724.method_6104(class_1268.field_5810);
      }

   }

   public boolean isAboveWater() {
      return mc.field_1724.method_5869() || mc.field_1687.method_8320(class_2338.method_49638(mc.field_1724.method_19538().method_1031((double)0.0F, -0.4, (double)0.0F))).method_26204() == class_2246.field_10382;
   }

   public float getAttackCooldownProgressPerTick() {
      return (float)((double)1.0F / mc.field_1724.method_45325(class_5134.field_23723) * (double)20.0F * (double)ThunderHack.TICK_TIMER * (double)((Boolean)this.tpsSync.getValue() ? Managers.SERVER.getTPSFactor() : 1.0F));
   }

   public float getAttackCooldown() {
      return class_3532.method_15363(((float)((ILivingEntity)mc.field_1724).getLastAttackedTicks() + (Float)this.attackBaseTime.getValue()) / this.getAttackCooldownProgressPerTick(), 0.0F, 1.0F);
   }

   private void legitRandomizeSeed() {
      this.legitSecureRandom = new SecureRandom();
      this.legitDynamicSmoothing = this.legitRandomGaussian(0.3F, 0.5F);
   }

   private float legitRandomGaussian(float min, float max) {
      float value = (float)this.legitSecureRandom.nextGaussian();
      value = class_3532.method_15363(value, -2.0F, 2.0F) / 4.0F + 0.5F;
      return min + (max - min) * value;
   }

   private float legitFastRandom(float min, float max) {
      return min + (max - min) * ThreadLocalRandom.current().nextFloat();
   }

   private float legitGetPlayerGcd() {
      double s = (Double)mc.field_1690.method_42495().method_41753();
      double val = s * 0.6 + 0.2;
      return (float)(Math.pow(val, (double)3.0F) * 1.2);
   }

   private float legitCorrectRotationScalar(float rot) {
      float gcd = this.legitGetPlayerGcd();
      if (gcd < 1.0E-4F) {
         return rot;
      } else {
         float rounded = (float)Math.round(rot / gcd) * gcd;
         return rounded + (float)(this.legitSecureRandom.nextGaussian() * 0.005);
      }
   }

   private float[] legitCorrectRotationPair(float yaw, float pitch) {
      return new float[]{this.legitCorrectRotationScalar(yaw), this.legitCorrectRotationScalar(pitch)};
   }

   private float legitSmoothDelta(float current, float last, float smoothFactor) {
      return last + (current - last) * smoothFactor;
   }

   private float legitEnsureNonZero(float delta, float min) {
      if (Math.abs(delta) < min) {
         return delta >= 0.0F ? min : -min;
      } else {
         return delta;
      }
   }

   private class_243 legitGetBestVector(class_1309 target, float jitter) {
      double yExpand = class_3532.method_15350(mc.field_1724.method_23320() - target.method_23320(), (double)target.method_17682() / (double)2.0F, (double)target.method_17682()) / (mc.field_1724.method_6128() ? (double)10.0F : (!mc.field_1690.field_1903.method_1434() && mc.field_1724.method_24828() ? (target.method_5715() ? 0.8 : 0.6) : (double)1.0F));
      class_243 finalVec = target.method_19538().method_1031((double)0.0F, yExpand, (double)0.0F);
      return finalVec.method_1031((double)jitter, (double)jitter / (double)2.0F, (double)jitter).method_1023(mc.field_1724.method_23317(), mc.field_1724.method_23320(), mc.field_1724.method_23321()).method_1029();
   }

   private void updateTarget() {
      class_1297 candidat = this.findTarget();
      if (target == null) {
         target = candidat;
      } else {
         if (this.sort.getValue() == Aura.Sort.FOV || !(Boolean)this.lockTarget.getValue()) {
            target = candidat;
         }

         if (candidat instanceof class_1676) {
            target = candidat;
         }

         if (this.skipEntity(target)) {
            target = null;
         }

      }
   }

   private void calcRotations(boolean ready) {
      if (ready) {
         this.trackticks = mc.field_1687.method_20812(mc.field_1724, mc.field_1724.method_5829().method_1009((double)-0.25F, (double)0.0F, (double)-0.25F).method_989((double)0.0F, (double)1.0F, (double)0.0F)).iterator().hasNext() ? 1 : (Integer)this.interactTicks.getValue();
      } else if (this.trackticks > 0) {
         --this.trackticks;
      }

      if (target != null) {
         boolean var10000;
         label110: {
            if (ModuleManager.elytraTarget.isEnabled()) {
               class_1297 var5 = target;
               if (var5 instanceof class_1309) {
                  class_1309 lt = (class_1309)var5;
                  if (lt.method_6128()) {
                     var10000 = true;
                     break label110;
                  }
               }
            }

            var10000 = false;
         }

         boolean targetElytraFlying = var10000;
         class_243 targetVec;
         if (!mc.field_1724.method_6128() && !ModuleManager.elytraPlus.isEnabled() && !targetElytraFlying) {
            targetVec = this.getLegitLook(target);
         } else {
            double predX = target.method_23317() + target.method_18798().field_1352;
            double predY = target.method_23320() + target.method_18798().field_1351;
            double predZ = target.method_23321() + target.method_18798().field_1350;
            targetVec = new class_243(predX, predY, predZ);
         }

         if (targetVec != null) {
            this.pitchAcceleration = Managers.PLAYER.checkRtx(this.rotationYaw, this.rotationPitch, this.getAimRange(), this.getAimRange(), this.rayTrace.getValue()) ? 1.0F : (this.pitchAcceleration < 8.0F ? this.pitchAcceleration * 1.65F : 8.0F);
            float delta_yaw = class_3532.method_15393((float)class_3532.method_15338(Math.toDegrees(Math.atan2(targetVec.field_1350 - mc.field_1724.method_23321(), targetVec.field_1352 - mc.field_1724.method_23317())) - (double)90.0F) - this.rotationYaw) + (float)(this.wallsBypass.is(Aura.WallsBypass.V2) && !ready && !mc.field_1724.method_6057(target) ? 20 : 0);
            float delta_pitch = (float)(-Math.toDegrees(Math.atan2(targetVec.field_1351 - (mc.field_1724.method_19538().field_1351 + (double)mc.field_1724.method_18381(mc.field_1724.method_18376())), Math.sqrt(Math.pow(targetVec.field_1352 - mc.field_1724.method_23317(), (double)2.0F) + Math.pow(targetVec.field_1350 - mc.field_1724.method_23321(), (double)2.0F))))) - this.rotationPitch;
            float absYaw = class_3532.method_15379(delta_yaw);
            double f = (Double)mc.field_1690.method_42495().method_41753() * 0.6 + 0.2;
            double gcd = f * f * f * (double)8.0F * 0.15;
            if (this.trackticks <= 0 && this.rotationMode.getValue() != Aura.Mode.Beta && this.rotationMode.getValue() != Aura.Mode.MSLAC) {
               this.rotationYaw = mc.field_1724.method_36454();
               this.rotationPitch = mc.field_1724.method_36455();
               this.yawVelocity = 0.0F;
               this.pitchVelocity = 0.0F;
            } else {
               if (mc.field_1724.method_6128()) {
                  this.rotationYaw += snapGcd(delta_yaw, gcd);
                  this.rotationPitch += snapGcd(delta_pitch, gcd);
               } else if (this.rotationMode.getValue() != Aura.Mode.Beta && this.rotationMode.getValue() != Aura.Mode.MSLAC) {
                  this.rotationYaw += snapGcd(delta_yaw, gcd);
                  this.rotationPitch += snapGcd(delta_pitch, gcd);
               } else if (this.rotationMode.getValue() == Aura.Mode.Beta) {
                  float maxSpeed;
                  float accel;
                  if (absYaw > 60.0F) {
                     maxSpeed = MathUtility.random(110.0F, 130.0F);
                     accel = (float)MathUtility.random(0.8, 0.95);
                  } else if (absYaw > 20.0F) {
                     maxSpeed = MathUtility.random(55.0F, 90.0F);
                     accel = (float)MathUtility.random(0.7, 0.88);
                  } else if (absYaw > 5.0F) {
                     maxSpeed = MathUtility.random(20.0F, 50.0F);
                     accel = (float)MathUtility.random(0.6, 0.8);
                  } else {
                     maxSpeed = MathUtility.random(8.0F, 20.0F);
                     accel = (float)MathUtility.random(0.55, (double)0.75F);
                  }

                  this.yawVelocity += (delta_yaw - this.yawVelocity) * accel;
                  this.pitchVelocity += (delta_pitch - this.pitchVelocity) * accel;
                  this.yawVelocity = class_3532.method_15363(this.yawVelocity, -maxSpeed, maxSpeed);
                  float pitchMax = Managers.PLAYER.ticksElytraFlying > 5 ? 180.0F : Math.max(maxSpeed * 0.8F, this.pitchAcceleration + MathUtility.random(-1.0F, 1.0F));
                  this.pitchVelocity = class_3532.method_15363(this.pitchVelocity, -pitchMax, pitchMax);
                  float dYaw = snapGcd(this.yawVelocity, gcd);
                  float dPitch = snapGcd(this.pitchVelocity, gcd);
                  this.jitterYaw = 0.0F;
                  this.jitterPitch = 0.0F;
                  this.rotationYaw += dYaw;
                  this.rotationPitch += dPitch;
               } else {
                  this.calcMslacRotation(target);
               }

               this.rotationPitch = class_3532.method_15363(this.rotationPitch, -90.0F, 90.0F);
            }

            if (!this.rotationMode.is(Aura.Mode.Grim)) {
               ModuleManager.rotations.fixRotation = this.rotationYaw;
            }

            this.lookingAtHitbox = Managers.PLAYER.checkRtx(this.rotationYaw, this.rotationPitch, this.getRange(), this.getWallRange(), this.rayTrace.getValue());
         }
      }
   }

   private void calcMslacRotation(class_1297 target) {
      if (target instanceof class_1309 livingTarget) {
         if (Float.isNaN(this.rotationYaw)) {
            this.rotationYaw = mc.field_1724.method_36454();
         }

         if (Float.isNaN(this.rotationPitch)) {
            this.rotationPitch = mc.field_1724.method_36455();
         }

         float targetSpeedMult = 1.0F;
         if (livingTarget.method_6059(class_1294.field_5904)) {
            int amp = livingTarget.method_6112(class_1294.field_5904).method_5578();
            targetSpeedMult = 1.0F + 0.22F * (float)(amp + 1);
         }

         class_243 targetVel = livingTarget.method_18798();
         float predScale = targetSpeedMult * 0.85F;
         class_243 predictedPos = new class_243(livingTarget.method_23317() + targetVel.field_1352 * (double)predScale, livingTarget.method_23318(), livingTarget.method_23321() + targetVel.field_1350 * (double)predScale);
         float jitter = this.legitRandomGaussian(0.1F, 0.2F);
         class_243 vec = this.legitGetBestVectorPredicted(livingTarget, predictedPos, jitter);
         float shortestYawPath = class_3532.method_15393((float)Math.toDegrees(Math.atan2(vec.field_1350, vec.field_1352)) - 90.0F - this.rotationYaw);
         float yawToTarget = this.rotationYaw + shortestYawPath;
         float pitchToTarget = (float)(-Math.toDegrees(Math.atan2(vec.field_1351, Math.hypot(vec.field_1350, vec.field_1352))));
         float lYawDelta = class_3532.method_15393(yawToTarget - this.rotationYaw);
         float lPitchDelta = class_3532.method_15393(pitchToTarget - this.rotationPitch);
         float absYaw = Math.abs(lYawDelta);
         float absPitch = Math.abs(lPitchDelta);
         float rayCaster = !Managers.PLAYER.checkRtx(this.rotationYaw, this.rotationPitch, this.getRange(), 0.0F, this.rayTrace.getValue()) ? 0.78F : 1.0F;
         float processedYawDelta;
         float processedPitchDelta;
         if (absYaw > 40.0F) {
            processedYawDelta = this.calcLinearBezierYaw(lYawDelta, targetSpeedMult);
            processedPitchDelta = this.calcLinearBezierYaw(lPitchDelta, targetSpeedMult);
         } else if (absYaw > 10.0F) {
            processedYawDelta = this.calcExponentialYaw(lYawDelta, absYaw, targetSpeedMult);
            processedPitchDelta = this.calcExponentialYaw(lPitchDelta, absPitch, targetSpeedMult);
         } else {
            processedYawDelta = this.calcGaussianCorrection(lYawDelta, targetSpeedMult);
            processedPitchDelta = this.calcGaussianCorrection(lPitchDelta, targetSpeedMult);
         }

         float maxYawSpeed = this.legitRandomGaussian(55.0F, 80.0F) * targetSpeedMult;
         float maxPitchSpeed = this.legitRandomGaussian(10.0F, 16.0F) * targetSpeedMult;
         float clampYaw = Math.min(Math.abs(processedYawDelta), maxYawSpeed) * rayCaster;
         float clampPitch = Math.min(Math.abs(processedPitchDelta), maxPitchSpeed) * rayCaster;
         lYawDelta = processedYawDelta > 0.0F ? clampYaw : -clampYaw;
         lPitchDelta = processedPitchDelta > 0.0F ? clampPitch : -clampPitch;
         float speedFactor;
         if (this.legitRotationCount % 4 == 0) {
            speedFactor = this.legitRandomGaussian(0.88F, 1.12F);
            this.legitLastSpeedFactor = speedFactor;
         } else {
            speedFactor = this.legitLastSpeedFactor;
         }

         lYawDelta /= 0.45F * speedFactor;
         lPitchDelta /= 1.0F * speedFactor;
         float yawNoiseMag = Math.max(0.025F, Math.abs(lYawDelta) * 0.07F);
         float pitchNoiseMag = Math.max(0.018F, Math.abs(lPitchDelta) * 0.09F);
         lYawDelta += this.legitRandomGaussian(-yawNoiseMag, yawNoiseMag);
         lPitchDelta += this.legitRandomGaussian(-pitchNoiseMag, pitchNoiseMag);
         float dynamicSmooth = this.legitDynamicSmoothing;
         if (livingTarget.method_6059(class_1294.field_5904)) {
            int amp = livingTarget.method_6112(class_1294.field_5904).method_5578();
            dynamicSmooth = Math.min(0.88F, this.legitDynamicSmoothing + 0.22F * (float)(amp + 1));
         }

         if (absYaw > 30.0F) {
            dynamicSmooth = Math.min(0.92F, dynamicSmooth + 0.15F);
         }

         lYawDelta = this.legitSmoothDelta(lYawDelta, this.legitLastYawDelta, dynamicSmooth);
         lPitchDelta = this.legitSmoothDelta(lPitchDelta, this.legitLastPitchDelta, dynamicSmooth);
         float nonZeroFloor = this.legitFastRandom(0.06F, 0.14F);
         lYawDelta = this.legitEnsureNonZero(lYawDelta, nonZeroFloor);
         lPitchDelta = this.legitEnsureNonZero(lPitchDelta, nonZeroFloor);
         float newYaw = this.rotationYaw + lYawDelta;
         float newPitch = class_3532.method_15363(this.rotationPitch + lPitchDelta, -89.5F, 89.5F);
         this.legitYawNoiseAcc = class_3532.method_15363(this.legitYawNoiseAcc + this.legitRandomGaussian(-0.025F, 0.025F), -0.07F, 0.07F);
         this.legitPitchNoiseAcc = class_3532.method_15363(this.legitPitchNoiseAcc + this.legitRandomGaussian(-0.025F, 0.025F), -0.07F, 0.07F);
         newYaw += this.legitYawNoiseAcc;
         newPitch += this.legitPitchNoiseAcc;
         float[] corrected = this.legitCorrectRotationPair(newYaw, newPitch);
         this.rotationYaw = corrected[0];
         this.rotationPitch = corrected[1];
         this.legitLastYawDelta = lYawDelta;
         this.legitLastPitchDelta = lPitchDelta;
         ++this.legitRotationCount;
         this.jitterYaw = 0.0F;
         this.jitterPitch = 0.0F;
         this.yawVelocity = 0.0F;
         this.pitchVelocity = 0.0F;
      }
   }

   private float calcLinearBezierYaw(float delta, float speedMult) {
      float sign = Math.signum(delta);
      float abs = Math.abs(delta);
      float t = class_3532.method_15363(abs / 90.0F, 0.0F, 1.0F);
      float bezierVal = cubicBezier(0.0F, 0.3F, 0.7F, 1.0F, t);
      float blended = abs * (0.6F + 0.4F * bezierVal);
      blended *= 0.7F + 0.3F * speedMult;
      return sign * blended;
   }

   private float calcExponentialYaw(float delta, float absDelta, float speedMult) {
      float sign = Math.signum(delta);
      float k = 0.08F * speedMult;
      float expMove = absDelta * (1.0F - (float)Math.exp((double)(-k * absDelta)));
      expMove += this.legitRandomGaussian(-absDelta * 0.04F, absDelta * 0.04F);
      return sign * expMove;
   }

   private float calcGaussianCorrection(float delta, float speedMult) {
      float sign = Math.signum(delta);
      float abs = Math.abs(delta);
      float stdDev = Math.max(0.5F, abs * 0.35F);
      float gauss = (float)(this.legitSecureRandom.nextGaussian() * (double)stdDev);
      float result = abs * 0.7F + Math.abs(gauss) * 0.3F * speedMult;
      return sign * result;
   }

   private class_243 legitGetBestVectorPredicted(class_1309 target, class_243 predictedPos, float jitter) {
      double yExpand = class_3532.method_15350(mc.field_1724.method_23320() - target.method_23320(), (double)target.method_17682() / (double)2.0F, (double)target.method_17682()) / (mc.field_1724.method_6128() ? (double)10.0F : (!mc.field_1690.field_1903.method_1434() && mc.field_1724.method_24828() ? (target.method_5715() ? 0.8 : 0.6) : (double)1.0F));
      class_243 finalVec = new class_243(predictedPos.field_1352, target.method_23318() + yExpand, predictedPos.field_1350);
      return finalVec.method_1031((double)jitter, (double)jitter / (double)2.0F, (double)jitter).method_1023(mc.field_1724.method_23317(), mc.field_1724.method_23320(), mc.field_1724.method_23321()).method_1029();
   }

   public void onRender3D(class_4587 stack) {
      if (this.haveWeapon() && target != null) {
         if ((this.resolver.is(Aura.Resolver.BackTrack) || (Boolean)this.resolverVisualisation.getValue()) && this.resolvedBox != null) {
            Render3DEngine.OUTLINE_QUEUE.add(new Render3DEngine.OutlineAction(this.resolvedBox, HudEditor.getColor(0), 1.0F));
         }

         switch (((ESP)this.esp.getValue()).ordinal()) {
            case 1 -> Render3DEngine.drawTargetEsp(stack, target);
            case 2 -> CaptureMark.render(target);
            case 3 -> Render3DEngine.drawOldTargetEsp(stack, target);
            case 4 -> Render3DEngine.renderGhosts((Integer)this.espLength.getValue(), (Integer)this.espFactor.getValue(), (Float)this.espShaking.getValue(), (Float)this.espAmplitude.getValue(), target);
            case 5 -> Render3DEngine.drawTadpoleEsp(stack, target);
         }

         if ((Boolean)this.clientLook.getValue() && this.rotationMode.getValue() != Aura.Mode.None) {
            mc.field_1724.method_36456((float)Render2DEngine.interpolate((double)mc.field_1724.field_5982, (double)this.rotationYaw, (double)Render3DEngine.getTickDelta()));
            mc.field_1724.method_36457((float)Render2DEngine.interpolate((double)mc.field_1724.field_6004, (double)this.rotationPitch, (double)Render3DEngine.getTickDelta()));
         }

      }
   }

   public void onDisable() {
      target = null;
   }

   public float getSquaredRotateDistance() {
      float dst = this.getAimRange();
      if (ModuleManager.strafe.isEnabled()) {
         dst += 4.0F;
      }

      if (this.rotationMode.getValue() != Aura.Mode.Beta && this.rotationMode.getValue() != Aura.Mode.MSLAC || this.rayTrace.getValue() == Aura.RayTrace.OFF) {
         dst = this.getRange();
      }

      return dst * dst;
   }

   public class_243 getLegitLook(class_1297 target) {
      return this.rotationMode.getValue() == Aura.Mode.MSLAC ? new class_243(target.method_23317(), target.method_23318() + (double)target.method_17682() * (double)0.5F, target.method_23321()) : this.getLegitLookDefault(target);
   }

   private class_243 getLegitLookDefault(class_1297 target) {
      float minMotionXZ = 0.003F;
      float maxMotionXZ = 0.03F;
      double lenghtX = target.method_5829().method_17939();
      double lenghtY = target.method_5829().method_17940();
      double lenghtZ = target.method_5829().method_17941();
      if (target != this.lastTarget) {
         this.lastTarget = target;
         this.newTargetLock = true;
         this.pitchSweepProgress = 0.0F;
      }

      float aimHeight;
      if (this.newTargetLock && this.rotationMode.getValue() == Aura.Mode.Beta) {
         this.pitchSweepProgress += 0.06F + (float)(Math.random() * 0.06);
         if (this.pitchSweepProgress >= 1.0F) {
            this.pitchSweepProgress = 1.0F;
            this.newTargetLock = false;
         }

         float feetY = (float)(lenghtY * 0.08);
         float bodyY = (float)(lenghtY * 0.65);
         aimHeight = feetY + (bodyY - feetY) * this.pitchSweepProgress;
      } else {
         if (this.aimShiftTicks <= 0) {
            float low = (float)(lenghtY * 0.15);
            float high = (float)(lenghtY * 0.82);
            this.aimTargetY = low + (float)(Math.random() * (double)(high - low));
            this.aimShiftTicks = 8 + (int)(Math.random() * (double)13.0F);
         } else {
            --this.aimShiftTicks;
         }

         aimHeight = this.aimTargetY;
      }

      double dist = (double)mc.field_1724.method_5739(target);
      float minYClose;
      if (dist < (double)2.5F) {
         minYClose = (float)(lenghtY * 0.32);
      } else if (dist < 3.4) {
         minYClose = (float)(lenghtY * 0.26);
      } else {
         minYClose = (float)(lenghtY * 0.18);
      }

      if (aimHeight < minYClose) {
         aimHeight = minYClose;
      }

      double currentY = this.rotationPoint.field_1351;
      double drift = 0.04 + Math.random() * 0.05;
      double newY;
      if (Math.abs(currentY - (double)aimHeight) < drift) {
         newY = (double)aimHeight;
      } else {
         newY = currentY + (double)Math.signum(aimHeight - (float)currentY) * drift;
      }

      if (this.rotationMotion.equals(class_243.field_1353)) {
         this.rotationMotion = new class_243((double)MathUtility.random(-0.05F, 0.05F), (double)0.0F, (double)MathUtility.random(-0.05F, 0.05F));
      }

      this.rotationPoint = new class_243(this.rotationPoint.field_1352 + this.rotationMotion.method_10216(), newY, this.rotationPoint.field_1350 + this.rotationMotion.method_10215());
      if (this.rotationPoint.field_1352 >= (lenghtX - 0.05) / (double)2.0F) {
         this.rotationMotion = new class_243((double)(-MathUtility.random(minMotionXZ, maxMotionXZ)), (double)0.0F, this.rotationMotion.method_10215());
      }

      if (this.rotationPoint.field_1350 >= (lenghtZ - 0.05) / (double)2.0F) {
         this.rotationMotion = new class_243(this.rotationMotion.method_10216(), (double)0.0F, (double)(-MathUtility.random(minMotionXZ, maxMotionXZ)));
      }

      if (this.rotationPoint.field_1352 <= -(lenghtX - 0.05) / (double)2.0F) {
         this.rotationMotion = new class_243((double)MathUtility.random(minMotionXZ, maxMotionXZ), (double)0.0F, this.rotationMotion.method_10215());
      }

      if (this.rotationPoint.field_1350 <= -(lenghtZ - 0.05) / (double)2.0F) {
         this.rotationMotion = new class_243(this.rotationMotion.method_10216(), (double)0.0F, (double)MathUtility.random(minMotionXZ, maxMotionXZ));
      }

      if (!mc.field_1724.method_6057(target) && Objects.requireNonNull(this.wallsBypass.getValue()) == Aura.WallsBypass.V1) {
         return target.method_19538().method_1031(MathUtility.random(-0.15, 0.15), lenghtY, MathUtility.random(-0.15, 0.15));
      } else {
         if (!Managers.PLAYER.checkRtx(this.rotationYaw, this.rotationPitch, this.getRange(), this.getWallRange(), this.rayTrace.getValue())) {
            PlayerManager var10000 = Managers.PLAYER;
            float[] rotation1 = PlayerManager.calcAngle(target.method_19538().method_1031((double)0.0F, (double)(target.method_18381(target.method_18376()) / 2.0F), (double)0.0F));
            if (PlayerUtility.squaredDistanceFromEyes(target.method_19538().method_1031((double)0.0F, (double)(target.method_18381(target.method_18376()) / 2.0F), (double)0.0F)) <= this.attackRange.getPow2Value() && Managers.PLAYER.checkRtx(rotation1[0], rotation1[1], this.getRange(), 0.0F, this.rayTrace.getValue())) {
               this.rotationPoint = new class_243((double)MathUtility.random(-0.1F, 0.1F), (double)(target.method_18381(target.method_18376()) / MathUtility.random(1.8F, 2.5F)), (double)MathUtility.random(-0.1F, 0.1F));
            } else {
               float halfBox = (float)(lenghtX / (double)2.0F);

               for(float x1 = -halfBox; x1 <= halfBox; x1 += 0.05F) {
                  for(float z1 = -halfBox; z1 <= halfBox; z1 += 0.05F) {
                     for(float y1 = 0.05F; (double)y1 <= target.method_5829().method_17940(); y1 += 0.15F) {
                        class_243 v1 = new class_243(target.method_23317() + (double)x1, target.method_23318() + (double)y1, target.method_23321() + (double)z1);
                        if (!(PlayerUtility.squaredDistanceFromEyes(v1) > this.attackRange.getPow2Value())) {
                           var10000 = Managers.PLAYER;
                           float[] rotation = PlayerManager.calcAngle(v1);
                           if (Managers.PLAYER.checkRtx(rotation[0], rotation[1], this.getRange(), 0.0F, this.rayTrace.getValue())) {
                              this.rotationPoint = new class_243((double)x1, (double)y1, (double)z1);
                              break;
                           }
                        }
                     }
                  }
               }
            }
         }

         return target.method_19538().method_1019(this.rotationPoint);
      }
   }

   public boolean isInRange(class_1297 target) {
      if (PlayerUtility.squaredDistanceFromEyes(target.method_19538().method_1031((double)0.0F, (double)target.method_18381(target.method_18376()), (double)0.0F)) > this.getSquaredRotateDistance() + 4.0F) {
         return false;
      } else {
         float halfBox = (float)(target.method_5829().method_17939() / (double)2.0F);

         for(float x1 = -halfBox; x1 <= halfBox; x1 += 0.15F) {
            for(float z1 = -halfBox; z1 <= halfBox; z1 += 0.15F) {
               for(float y1 = 0.05F; (double)y1 <= target.method_5829().method_17940(); y1 += 0.25F) {
                  if (!(PlayerUtility.squaredDistanceFromEyes(new class_243(target.method_23317() + (double)x1, target.method_23318() + (double)y1, target.method_23321() + (double)z1)) > this.getSquaredRotateDistance())) {
                     PlayerManager var10000 = Managers.PLAYER;
                     float[] rotation = PlayerManager.calcAngle(new class_243(target.method_23317() + (double)x1, target.method_23318() + (double)y1, target.method_23321() + (double)z1));
                     if (Managers.PLAYER.checkRtx(rotation[0], rotation[1], (float)Math.sqrt((double)this.getSquaredRotateDistance()), this.getWallRange(), this.rayTrace.getValue())) {
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
      List<class_1309> first_stage = new CopyOnWriteArrayList();

      for(class_1297 ent : mc.field_1687.method_18112()) {
         if ((ent instanceof class_1678 || ent instanceof class_1674) && ent.method_5805() && this.isInRange(ent) && (Boolean)this.Projectiles.getValue()) {
            return ent;
         }

         if (!this.skipEntity(ent) && ent instanceof class_1309) {
            first_stage.add((class_1309)ent);
         }
      }

      class_1309 var10000;
      switch (((Sort)this.sort.getValue()).ordinal()) {
         case 0 -> var10000 = (class_1309)first_stage.stream().min(Comparator.comparing((e) -> mc.field_1724.method_5707(e.method_19538()))).orElse((Object)null);
         case 1 -> var10000 = (class_1309)first_stage.stream().max(Comparator.comparing((e) -> mc.field_1724.method_5707(e.method_19538()))).orElse((Object)null);
         case 2 -> var10000 = (class_1309)first_stage.stream().min(Comparator.comparing((e) -> e.method_6032() + e.method_6067())).orElse((Object)null);
         case 3 -> var10000 = (class_1309)first_stage.stream().max(Comparator.comparing((e) -> e.method_6032() + e.method_6067())).orElse((Object)null);
         case 4 -> var10000 = (class_1309)first_stage.stream().min(Comparator.comparing((e) -> {
   float v = 0.0F;

   for(class_1799 armor : e.method_5661()) {
      if (armor != null && !armor.method_7909().equals(class_1802.field_8162)) {
         v += (float)(armor.method_7936() - armor.method_7919()) / (float)armor.method_7936();
      }
   }

   return v;
})).orElse((Object)null);
         case 5 -> var10000 = (class_1309)first_stage.stream().max(Comparator.comparing((e) -> {
   float v = 0.0F;

   for(class_1799 armor : e.method_5661()) {
      if (armor != null && !armor.method_7909().equals(class_1802.field_8162)) {
         v += (float)(armor.method_7936() - armor.method_7919()) / (float)armor.method_7936();
      }
   }

   return v;
})).orElse((Object)null);
         case 6 -> var10000 = (class_1309)first_stage.stream().min(Comparator.comparing(this::getFOVAngle)).orElse((Object)null);
         default -> throw new MatchException((String)null, (Throwable)null);
      }

      return var10000;
   }

   private boolean skipEntity(class_1297 entity) {
      if (this.isBullet(entity)) {
         return false;
      } else if (!(entity instanceof class_1309)) {
         return true;
      } else {
         class_1309 ent = (class_1309)entity;
         if (!ent.method_29504() && entity.method_5805()) {
            if (entity instanceof class_1531) {
               return true;
            } else if (entity instanceof class_1451) {
               return true;
            } else if (this.skipNotSelected(entity)) {
               return true;
            } else if (!InteractionUtility.isVecInFOV(ent.method_19538(), this.fov.getValue())) {
               return true;
            } else {
               if (entity instanceof class_1657) {
                  class_1657 player = (class_1657)entity;
                  if (player.method_6128() && !ModuleManager.elytraTarget.isEnabled()) {
                     return true;
                  }

                  if (ModuleManager.antiBot.isEnabled() && AntiBot.bots.contains(entity)) {
                     return true;
                  }

                  if (player == mc.field_1724 || Managers.FRIEND.isFriend(player)) {
                     return true;
                  }

                  if (player.method_7337() && (Boolean)this.ignoreCreative.getValue()) {
                     return true;
                  }

                  if (player.method_6096() == 0 && (Boolean)this.ignoreNaked.getValue()) {
                     return true;
                  }

                  if (player.method_5767() && (Boolean)this.ignoreInvisible.getValue()) {
                     return true;
                  }

                  if (player.method_22861() == mc.field_1724.method_22861() && (Boolean)this.ignoreTeam.getValue() && mc.field_1724.method_22861() != 16777215) {
                     return true;
                  }
               }

               return !this.isInRange(entity) || entity.method_16914() && (Boolean)this.ignoreNamed.getValue();
            }
         } else {
            return true;
         }
      }
   }

   private boolean isBullet(class_1297 entity) {
      return (entity instanceof class_1678 || entity instanceof class_1674) && entity.method_5805() && PlayerUtility.squaredDistanceFromEyes(entity.method_19538()) < this.getSquaredRotateDistance() && (Boolean)this.Projectiles.getValue();
   }

   private boolean skipNotSelected(class_1297 entity) {
      if (entity instanceof class_1621 && !(Boolean)this.Slimes.getValue()) {
         return true;
      } else {
         if (entity instanceof class_1588) {
            class_1588 he = (class_1588)entity;
            if (!(Boolean)this.hostiles.getValue()) {
               return true;
            }

            if ((Boolean)this.onlyAngry.getValue()) {
               return !he.method_7076(mc.field_1724);
            }
         }

         if (entity instanceof class_1657 && !(Boolean)this.Players.getValue()) {
            return true;
         } else if (entity instanceof class_1646 && !(Boolean)this.Villagers.getValue()) {
            return true;
         } else if (entity instanceof class_1308 && !(Boolean)this.Mobs.getValue()) {
            return true;
         } else {
            return entity instanceof class_1429 && !(Boolean)this.Animals.getValue();
         }
      }
   }

   private float getFOVAngle(@NotNull class_1309 e) {
      double difX = e.method_23317() - mc.field_1724.method_23317();
      double difZ = e.method_23321() - mc.field_1724.method_23321();
      float yaw = (float)class_3532.method_15338(Math.toDegrees(Math.atan2(difZ, difX)) - (double)90.0F);
      return Math.abs(yaw - class_3532.method_15393(mc.field_1724.method_36454()));
   }

   public void pause() {
      this.pauseTimer.reset();
   }

   private boolean shouldRandomizeDelay() {
      return (Boolean)this.randomHitDelay.getValue() && (mc.field_1724.method_24828() || mc.field_1724.field_6017 < 0.12F || mc.field_1724.method_5681() || mc.field_1724.method_6128());
   }

   private boolean shouldRandomizeFallDistance() {
      return (Boolean)this.randomHitDelay.getValue() && !this.shouldRandomizeDelay();
   }

   public static class Position {
      private double x;
      private double y;
      private double z;
      private int ticks;

      public Position(double x, double y, double z) {
         this.x = x;
         this.y = y;
         this.z = z;
      }

      public boolean shouldRemove() {
         return this.ticks++ > (Integer)ModuleManager.aura.backTicks.getValue();
      }

      public double getX() {
         return this.x;
      }

      public double getY() {
         return this.y;
      }

      public double getZ() {
         return this.z;
      }
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

   public static enum Switch {
      Normal,
      None,
      Silent;

      // $FF: synthetic method
      private static Switch[] $values() {
         return new Switch[]{Normal, None, Silent};
      }
   }

   public static enum Resolver {
      Off,
      Advantage,
      Predictive,
      BackTrack;

      // $FF: synthetic method
      private static Resolver[] $values() {
         return new Resolver[]{Off, Advantage, Predictive, BackTrack};
      }
   }

   public static enum Mode {
      Interact,
      Beta,
      MSLAC,
      Grim,
      None;

      // $FF: synthetic method
      private static Mode[] $values() {
         return new Mode[]{Interact, Beta, MSLAC, Grim, None};
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

   public static enum ESP {
      Off,
      ThunderHack,
      NurikZapen,
      CelkaPasta,
      ThunderHackV2,
      Tadpole;

      // $FF: synthetic method
      private static ESP[] $values() {
         return new ESP[]{Off, ThunderHack, NurikZapen, CelkaPasta, ThunderHackV2, Tadpole};
      }
   }

   public static enum WallsBypass {
      Off,
      V1,
      V2;

      // $FF: synthetic method
      private static WallsBypass[] $values() {
         return new WallsBypass[]{Off, V1, V2};
      }
   }

   public static enum Sprint {
      Legit,
      Normal,
      HvH;

      // $FF: synthetic method
      private static Sprint[] $values() {
         return new Sprint[]{Legit, Normal, HvH};
      }
   }
}
