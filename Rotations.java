package unique.core.features.modules.client;

import net.minecraft.class_243;
import net.minecraft.class_3532;
import unique.core.core.Managers;
import unique.core.core.manager.client.ModuleManager;
import unique.core.events.impl.EventFixVelocity;
import unique.core.events.impl.EventKeyboardInput;
import unique.core.events.impl.EventPlayerJump;
import unique.core.events.impl.EventPlayerTravel;
import unique.core.features.modules.Module;
import unique.core.features.modules.combat.Aura;
import unique.core.setting.Setting;

public class Rotations extends Module {
   private final Setting<MoveFixMode> moveFix;
   public final Setting<Boolean> clientLook;
   public float fixRotation;
   private float prevYaw;
   private float prevPitch;

   public Rotations() {
      super("MoveFix", Module.Category.MOVEMENT);
      this.moveFix = new Setting<MoveFixMode>("MoveFix", Rotations.MoveFixMode.Focused);
      this.clientLook = new Setting<Boolean>("ClientLook", false);
      this.fixRotation = Float.NaN;
   }

   public void onJump(EventPlayerJump var1) {
      if (!Float.isNaN(this.fixRotation) && this.moveFix.getValue() != Rotations.MoveFixMode.Off && !mc.field_1724.method_3144()) {
         if (var1.isPre()) {
            this.prevYaw = mc.field_1724.method_36454();
            mc.field_1724.method_36456(this.fixRotation);
         } else {
            mc.field_1724.method_36456(this.prevYaw);
         }

      }
   }

   public void onPlayerMove(EventFixVelocity var1) {
      if (this.moveFix.getValue() == Rotations.MoveFixMode.Free) {
         if (Float.isNaN(this.fixRotation) || mc.field_1724.method_3144()) {
            return;
         }

         var1.setVelocity(this.fix(this.fixRotation, var1.getMovementInput(), var1.getSpeed()));
      }

   }

   public void modifyVelocity(EventPlayerTravel var1) {
      if (ModuleManager.aura.isEnabled()) {
         Aura var10000 = ModuleManager.aura;
         if (Aura.target != null && ModuleManager.aura.rotationMode.not(Aura.Mode.None) && (Boolean)ModuleManager.aura.elytraTarget.getValue() && Managers.PLAYER.ticksElytraFlying > 5) {
            if (var1.isPre()) {
               this.prevYaw = mc.field_1724.method_36454();
               this.prevPitch = mc.field_1724.method_36455();
               mc.field_1724.method_36456(this.fixRotation);
               mc.field_1724.method_36457(ModuleManager.aura.rotationPitch);
            } else {
               mc.field_1724.method_36456(this.prevYaw);
               mc.field_1724.method_36457(this.prevPitch);
            }

            return;
         }
      }

      if (this.moveFix.getValue() == Rotations.MoveFixMode.Focused && !Float.isNaN(this.fixRotation) && !mc.field_1724.method_3144()) {
         if (var1.isPre()) {
            this.prevYaw = mc.field_1724.method_36454();
            mc.field_1724.method_36456(this.fixRotation);
         } else {
            mc.field_1724.method_36456(this.prevYaw);
         }
      }

   }

   public void onKeyInput(EventKeyboardInput var1) {
      if (this.moveFix.getValue() == Rotations.MoveFixMode.Free) {
         if (Float.isNaN(this.fixRotation) || mc.field_1724.method_3144()) {
            return;
         }

         float var2 = mc.field_1724.field_3913.field_3905;
         float var3 = mc.field_1724.field_3913.field_3907;
         float var4 = (mc.field_1724.method_36454() - this.fixRotation) * ((float)Math.PI / 180F);
         float var5 = class_3532.method_15362(var4);
         float var6 = class_3532.method_15374(var4);
         mc.field_1724.field_3913.field_3907 = (float)Math.round(var3 * var5 - var2 * var6);
         mc.field_1724.field_3913.field_3905 = (float)Math.round(var2 * var5 + var3 * var6);
      }

   }

   private class_243 fix(float var1, class_243 var2, float var3) {
      double var4 = var2.method_1027();
      if (var4 < 1.0E-7) {
         return class_243.field_1353;
      } else {
         class_243 var6 = (var4 > (double)1.0F ? var2.method_1029() : var2).method_1021((double)var3);
         float var7 = class_3532.method_15374(var1 * ((float)Math.PI / 180F));
         float var8 = class_3532.method_15362(var1 * ((float)Math.PI / 180F));
         return new class_243(var6.field_1352 * (double)var8 - var6.field_1350 * (double)var7, var6.field_1351, var6.field_1350 * (double)var8 + var6.field_1352 * (double)var7);
      }
   }

   public boolean isToggleable() {
      return false;
   }

   public static enum MoveFixMode {
      Off,
      Focused,
      Free;

      // $FF: synthetic method
      private static MoveFixMode[] $values() {
         return new MoveFixMode[]{Off, Focused, Free};
      }
   }
}
