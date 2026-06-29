package unique.core.features.modules.combat;

import meteordevelopment.orbit.EventHandler;
import net.minecraft.class_1294;
import net.minecraft.class_1713;
import net.minecraft.class_2246;
import net.minecraft.class_2338;
import net.minecraft.class_238;
import net.minecraft.class_243;
import net.minecraft.class_2708;
import net.minecraft.class_2743;
import net.minecraft.class_2848;
import net.minecraft.class_746;
import net.minecraft.class_2350.class_2351;
import net.minecraft.class_2848.class_2849;
import unique.core.core.Core;
import unique.core.core.manager.client.ModuleManager;
import unique.core.events.impl.EventMove;
import unique.core.events.impl.EventSprint;
import unique.core.events.impl.EventSync;
import unique.core.events.impl.PacketEvent;
import unique.core.events.impl.PlayerUpdateEvent;
import unique.core.features.modules.Module;
import unique.core.injection.accesors.ISPacketEntityVelocity;
import unique.core.setting.Setting;
import unique.core.utility.player.InventoryUtility;
import unique.core.utility.player.MovementUtility;

public class TargetStrafe extends Module {
   public Setting<Mode> mode;
   public Setting<Boolean> jump;
   public Setting<Float> distance;
   private Setting<Boost> boost;
   public Setting<Float> setSpeed;
   private Setting<Float> velReduction;
   private Setting<Float> maxVelocitySpeed;
   public Setting<Boolean> smartCrit;
   public Setting<Float> collisionSpeed;
   public Setting<Float> collisionDistance;
   public static double oldSpeed;
   public static double contextFriction;
   public static double fovval;
   public static boolean needSwap;
   public static boolean needSprintState;
   public static boolean skip;
   public static boolean switchDir;
   public static boolean disabled;
   public static int noSlowTicks;
   public static int jumpTicks;
   public static int waterTicks;
   static long disableTime;
   private static TargetStrafe instance;

   public TargetStrafe() {
      super("TargetStrafe", Module.Category.COMBAT);
      this.mode = new Setting<Mode>("Mode", TargetStrafe.Mode.Normal);
      this.jump = new Setting<Boolean>("Jump", true, (var1) -> this.mode.getValue() == TargetStrafe.Mode.Normal);
      this.distance = new Setting<Float>("Distance", 1.3F, 0.2F, 7.0F, (var1) -> this.mode.getValue() == TargetStrafe.Mode.Normal);
      this.boost = new Setting<Boost>("Boost", TargetStrafe.Boost.None, (var1) -> this.mode.getValue() == TargetStrafe.Mode.Normal);
      this.setSpeed = new Setting<Float>("Speed", 1.3F, 0.0F, 2.0F, (var1) -> this.boost.getValue() == TargetStrafe.Boost.Elytra && this.mode.getValue() == TargetStrafe.Mode.Normal);
      this.velReduction = new Setting<Float>("Reduction", 6.0F, 0.1F, 10.0F, (var1) -> this.boost.getValue() == TargetStrafe.Boost.Damage && this.mode.getValue() == TargetStrafe.Mode.Normal);
      this.maxVelocitySpeed = new Setting<Float>("MaxVelocity", 0.8F, 0.1F, 2.0F, (var1) -> this.boost.getValue() == TargetStrafe.Boost.Damage && this.mode.getValue() == TargetStrafe.Mode.Normal);
      this.smartCrit = new Setting<Boolean>("SmartCrit", true, (var1) -> this.mode.getValue() == TargetStrafe.Mode.Normal);
      this.collisionSpeed = new Setting<Float>("Speed", 0.05F, 0.01F, 0.1F, (var1) -> this.mode.getValue() == TargetStrafe.Mode.Collision);
      this.collisionDistance = new Setting<Float>("Distance", 2.75F, 1.0F, 3.0F, (var1) -> this.mode.getValue() == TargetStrafe.Mode.Collision);
      instance = this;
   }

   public static TargetStrafe getInstance() {
      return instance;
   }

   public void onEnable() {
      oldSpeed = (double)0.0F;
      fovval = (Double)mc.field_1690.method_42454().method_41753();
      mc.field_1690.method_42454().method_41748((double)0.0F);
      skip = true;
   }

   public void onDisable() {
      mc.field_1690.method_42454().method_41748(fovval);
   }

   public boolean canStrafe() {
      if (mc.field_1724.method_5715()) {
         return false;
      } else if (mc.field_1724.method_5771()) {
         return false;
      } else if (ModuleManager.scaffold.isEnabled()) {
         return false;
      } else if (ModuleManager.speed.isEnabled()) {
         return false;
      } else if (!mc.field_1724.method_5869() && waterTicks <= 0) {
         return !mc.field_1724.method_31549().field_7479;
      } else {
         return false;
      }
   }

   public boolean needToSwitch(double var1, double var3) {
      if (!mc.field_1724.field_5976 && (!mc.field_1690.field_1913.method_1434() && !mc.field_1690.field_1849.method_1434() || jumpTicks > 0)) {
         for(int var5 = (int)(mc.field_1724.method_23318() + (double)4.0F); var5 >= 0; --var5) {
            class_2338 var6 = new class_2338((int)Math.floor(var1), var5, (int)Math.floor(var3));
            if (mc.field_1687.method_8320(var6).method_26204().equals(class_2246.field_10164) || mc.field_1687.method_8320(var6).method_26204().equals(class_2246.field_10036)) {
               return true;
            }

            if (!mc.field_1687.method_22347(var6)) {
               return false;
            }
         }

         return false;
      } else {
         jumpTicks = 10;
         return true;
      }
   }

   public double calculateSpeed(EventMove var1) {
      --jumpTicks;
      float var2 = this.getAIMoveSpeed();
      float var3 = mc.field_1687.method_8320((new class_2338.class_2339()).method_10102(mc.field_1724.method_23317(), this.getBoundingBox().method_1001(class_2351.field_11052) - var1.getY(), mc.field_1724.method_23321())).method_26204().method_9499() * 0.91F;
      float var4 = mc.field_1724.method_6059(class_1294.field_5913) && mc.field_1724.method_6115() ? 0.88F : (float)(oldSpeed > 0.32 && mc.field_1724.method_6115() ? 0.88 : (double)0.91F);
      if (mc.field_1724.method_24828()) {
         var4 = var3;
      }

      float var5 = (float)((double)0.1631F / Math.pow((double)var4, (double)3.0F));
      float var6;
      if (mc.field_1724.method_24828()) {
         var6 = var2 * var5;
         if (var1.getY() > (double)0.0F) {
            var6 += this.boost.getValue() == TargetStrafe.Boost.Elytra && InventoryUtility.getElytra() != -1 && disabled ? 0.65F : 0.2F;
         }

         disabled = false;
      } else {
         var6 = 0.0255F;
      }

      boolean var7 = false;
      double var8 = oldSpeed + (double)var6;
      double var10 = (double)0.0F;
      if (mc.field_1724.method_6115() && var1.getY() <= (double)0.0F) {
         double var12 = oldSpeed + (double)var6 * (double)0.25F;
         double var14 = var1.getY();
         if (var14 != (double)0.0F && Math.abs(var14) < 0.08) {
            var12 += 0.055;
         }

         if (var8 > (var10 = Math.max(0.043, var12))) {
            var7 = true;
            ++noSlowTicks;
         } else {
            noSlowTicks = Math.max(noSlowTicks - 1, 0);
         }
      } else {
         noSlowTicks = 0;
      }

      if (noSlowTicks > 3) {
         var8 = var10 - 0.019;
      } else {
         var8 = Math.max(var7 ? (double)0.0F : (double)0.25F, var8) - (mc.field_1724.field_6012 % 2 == 0 ? 0.001 : 0.002);
      }

      contextFriction = (double)var4;
      if (!mc.field_1724.method_24828()) {
         needSprintState = !mc.field_1724.field_3919;
         needSwap = true;
      } else {
         needSprintState = false;
      }

      return var8;
   }

   private boolean canCrit() {
      if (!(Boolean)this.smartCrit.getValue()) {
         return true;
      } else if (!mc.field_1724.method_5799() && !mc.field_1724.method_5869()) {
         if (mc.field_1724.method_5771()) {
            return false;
         } else if (mc.field_1724.method_6059(class_1294.field_5919)) {
            return false;
         } else if (mc.field_1724.method_6059(class_1294.field_5906)) {
            return false;
         } else {
            return mc.field_1724.field_6017 > 0.0F;
         }
      } else {
         return false;
      }
   }

   public class_238 getBoundingBox() {
      return new class_238(mc.field_1724.method_23317() - 0.1, mc.field_1724.method_23318(), mc.field_1724.method_23321() - 0.1, mc.field_1724.method_23317() + 0.1, mc.field_1724.method_23318() + (double)1.0F, mc.field_1724.method_23321() + 0.1);
   }

   public float getAIMoveSpeed() {
      boolean var1 = mc.field_1724.method_5624();
      mc.field_1724.method_5728(false);
      float var2 = mc.field_1724.method_6029() * 1.3F;
      mc.field_1724.method_5728(var1);
      return var2;
   }

   public static void disabler(int var0) {
      if (var0 != -1) {
         if (System.currentTimeMillis() - disableTime > 190L) {
            if (var0 != -2) {
               mc.field_1761.method_2906(0, var0, 1, class_1713.field_7790, mc.field_1724);
               mc.field_1761.method_2906(0, 6, 1, class_1713.field_7790, mc.field_1724);
            }

            mc.field_1724.field_3944.method_52787(new class_2848(mc.field_1724, class_2849.field_12982));
            mc.field_1724.field_3944.method_52787(new class_2848(mc.field_1724, class_2849.field_12982));
            if (var0 != -2) {
               mc.field_1761.method_2906(0, 6, 1, class_1713.field_7790, mc.field_1724);
               mc.field_1761.method_2906(0, var0, 1, class_1713.field_7790, mc.field_1724);
            }

            disableTime = System.currentTimeMillis();
         }

         disabled = true;
      }
   }

   private double wrapDS(double var1, double var3) {
      double var5 = var1 - mc.field_1724.method_23317();
      double var7 = var3 - mc.field_1724.method_23321();
      return Math.toDegrees(Math.atan2(var7, var5)) - (double)90.0F;
   }

   @EventHandler
   public void onMove(EventMove var1) {
      if (this.mode.getValue() == TargetStrafe.Mode.Normal) {
         int var2 = InventoryUtility.getElytra();
         if (this.boost.getValue() == TargetStrafe.Boost.Elytra && var2 != -1 && MovementUtility.isMoving() && !mc.field_1724.method_24828() && mc.field_1687.method_20812(mc.field_1724, mc.field_1724.method_5829().method_989((double)0.0F, var1.getY(), (double)0.0F)).iterator().hasNext() && disabled) {
            oldSpeed = (double)(Float)this.setSpeed.getValue();
         }

         if (this.canStrafe()) {
            if (Aura.target != null && ModuleManager.aura.isEnabled()) {
               double var3 = this.calculateSpeed(var1);
               double var5 = Math.atan2(mc.field_1724.method_23321() - Aura.target.method_23321(), mc.field_1724.method_23317() - Aura.target.method_23317());
               var5 += switchDir ? var3 / Math.sqrt(mc.field_1724.method_5858(Aura.target)) : -(var3 / Math.sqrt(mc.field_1724.method_5858(Aura.target)));
               double var7 = Aura.target.method_23317() + (double)(Float)this.distance.getValue() * Math.cos(var5);
               double var9 = Aura.target.method_23321() + (double)(Float)this.distance.getValue() * Math.sin(var5);
               if (this.needToSwitch(var7, var9)) {
                  switchDir = !switchDir;
                  var5 += (double)2.0F * (switchDir ? var3 / Math.sqrt(mc.field_1724.method_5858(Aura.target)) : -(var3 / Math.sqrt(mc.field_1724.method_5858(Aura.target))));
                  var7 = Aura.target.method_23317() + (double)(Float)this.distance.getValue() * Math.cos(var5);
                  var9 = Aura.target.method_23321() + (double)(Float)this.distance.getValue() * Math.sin(var5);
               }

               var1.setX(var3 * -Math.sin(Math.toRadians(this.wrapDS(var7, var9))));
               var1.setZ(var3 * Math.cos(Math.toRadians(this.wrapDS(var7, var9))));
               var1.cancel();
            }
         } else {
            oldSpeed = (double)0.0F;
         }
      }

   }

   @EventHandler
   public void onUpdateCollision(PlayerUpdateEvent var1) {
      if (this.mode.getValue() == TargetStrafe.Mode.Collision) {
         if (!MovementUtility.isMoving()) {
            return;
         }

         if (Aura.target == null || !ModuleManager.aura.isEnabled()) {
            return;
         }

         if (mc.field_1724.method_5739(Aura.target) >= (Float)this.collisionDistance.getValue()) {
            return;
         }

         class_243 var2 = mc.field_1724.method_19538();
         class_243 var3 = Aura.target.method_19538();
         class_243 var4 = var3.method_1020(var2).method_1029();
         double var5 = mc.field_1724.method_18798().field_1351;
         double var7 = (double)(Float)this.collisionSpeed.getValue();
         class_746 var10000 = mc.field_1724;
         double var10002 = var4.field_1352 * var7;
         double var10001 = mc.field_1724.method_18798().field_1352 + var10002;
         double var10004 = var4.field_1350 * var7;
         var10000.method_18800(var10001, var5, mc.field_1724.method_18798().field_1350 + var10004);
      }

   }

   @EventHandler
   public void updateValues(EventSync var1) {
      if (this.mode.getValue() == TargetStrafe.Mode.Normal) {
         oldSpeed = Math.hypot(mc.field_1724.method_23317() - mc.field_1724.field_6014, mc.field_1724.method_23321() - mc.field_1724.field_5969) * contextFriction;
         if (mc.field_1724.method_24828() && (Boolean)this.jump.getValue() && Aura.target != null) {
            mc.field_1724.method_6043();
         }
      }

      if (mc.field_1724.method_5869()) {
         waterTicks = 10;
      } else {
         --waterTicks;
      }

   }

   @EventHandler
   public void onUpdate(PlayerUpdateEvent var1) {
      if (this.mode.getValue() == TargetStrafe.Mode.Normal && this.boost.getValue() == TargetStrafe.Boost.Elytra && InventoryUtility.getElytra() != -1 && !mc.field_1724.method_24828() && mc.field_1724.field_6017 > 0.0F && !disabled) {
         disabler(InventoryUtility.getElytra());
      }

   }

   @EventHandler
   public void onPacketReceive(PacketEvent.Receive var1) {
      if (var1.getPacket() instanceof class_2708) {
         oldSpeed = (double)0.0F;
      }

      class_2743 var2;
      if (var1.getPacket() instanceof class_2743 && (var2 = (class_2743)var1.getPacket()).method_11818() == mc.field_1724.method_5628() && this.mode.getValue() == TargetStrafe.Mode.Normal && this.boost.getValue() == TargetStrafe.Boost.Damage) {
         if (mc.field_1724.method_24828()) {
            return;
         }

         double var3 = Math.abs(var2.method_11815());
         double var5 = Math.abs(var2.method_11819());
         oldSpeed = (var3 + var5) / (double)((Float)this.velReduction.getValue() * 1000.0F);
         oldSpeed = Math.min(oldSpeed, (double)(Float)this.maxVelocitySpeed.getValue());
         ((ISPacketEntityVelocity)var2).setMotionX(0);
         ((ISPacketEntityVelocity)var2).setMotionY(0);
         ((ISPacketEntityVelocity)var2).setMotionZ(0);
      }

   }

   @EventHandler
   public void actionEvent(EventSprint var1) {
      if (this.mode.getValue() == TargetStrafe.Mode.Normal) {
         if (this.canStrafe() && Core.serverSprint != needSprintState) {
            var1.setSprintState(!Core.serverSprint);
         }

         if (needSwap) {
            var1.setSprintState(!mc.field_1724.field_3919);
            needSwap = false;
         }
      }

   }

   private static enum Mode {
      Normal,
      Collision;

      // $FF: synthetic method
      private static Mode[] $values() {
         return new Mode[]{Normal, Collision};
      }
   }

   private static enum Boost {
      None,
      Elytra,
      Damage;

      // $FF: synthetic method
      private static Boost[] $values() {
         return new Boost[]{None, Elytra, Damage};
      }
   }
}
