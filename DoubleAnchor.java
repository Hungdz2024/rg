package dlindustries.vigillant.system.module.modules.crystal;

import dlindustries.vigillant.system.event.events.TickListener;
import dlindustries.vigillant.system.module.Category;
import dlindustries.vigillant.system.module.Module;
import dlindustries.vigillant.system.utils.BlockUtils;
import dlindustries.vigillant.system.utils.EncryptedString;
import net.minecraft.class_1268;
import net.minecraft.class_1802;
import net.minecraft.class_2338;
import net.minecraft.class_239;
import net.minecraft.class_2885;
import net.minecraft.class_3965;
import org.lwjgl.glfw.GLFW;

public final class DoubleAnchor extends Module implements TickListener {
   private class_2338 pos;
   private int count;

   public DoubleAnchor() {
      super(EncryptedString.of("Airplace Optimizer"), EncryptedString.of("Helps you do the air place/double anchor by making the time window biggger"), -1, Category.CRYSTAL);
   }

   public void onEnable() {
      this.eventManager.add(TickListener.class, this);
      this.pos = null;
      this.count = 0;
      super.onEnable();
   }

   public void onDisable() {
      this.eventManager.remove(TickListener.class, this);
      super.onDisable();
   }

   public void onTick() {
      if (this.mc.field_1755 == null) {
         assert this.mc.field_1724 != null;

         if (this.mc.field_1724.method_6047().method_31574(class_1802.field_23141)) {
            assert this.mc.field_1687 != null;

            class_239 var2 = this.mc.field_1765;
            if (var2 instanceof class_3965) {
               class_3965 h = (class_3965)var2;
               if (BlockUtils.isAnchorCharged(h.method_17777()) && GLFW.glfwGetMouseButton(this.mc.method_22683().method_4490(), 1) == 1) {
                  if (h.method_17777().equals(this.pos)) {
                     if (this.count >= 1) {
                        return;
                     }
                  } else {
                     this.pos = h.method_17777();
                     this.count = 0;
                  }

                  this.mc.method_1562().method_52787(new class_2885(class_1268.field_5808, h, 0));
                  ++this.count;
               }
            }
         }
      }

   }
}
