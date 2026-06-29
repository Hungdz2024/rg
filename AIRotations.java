package unique.core.features.modules.rotations;

import java.security.SecureRandom;
import java.util.LinkedList;
import net.minecraft.class_1297;
import net.minecraft.class_1309;
import net.minecraft.class_243;
import net.minecraft.class_310;
import net.minecraft.class_3532;

public class AIRotations {
   private static final class_310 mc = class_310.method_1551();
   private static final SecureRandom RANDOM = new SecureRandom();
   private float baseYawSpeed;
   private float basePitchSpeed;
   private float fastYawSpeed;
   private float fastPitchSpeed;
   private float yawThreshold;
   private float pitchThreshold;
   private float microJitterYaw;
   private float microJitterPitch;
   private final LinkedList<Float> yawHistory = new LinkedList();
   private final LinkedList<Float> pitchHistory = new LinkedList();
   private int historySize;
   private long sessionStartTime = 0L;
   private long lastProfileChange = 0L;
   private long lastPauseTime = 0L;
   private long pauseDuration = 0L;
   private long lastWanderUpdate = 0L;
   private long errorEndTime = 0L;
   private boolean isPaused = false;
   private int movePhase = 0;
   private float phaseProgress = 0.0F;
   private float fatigueLevel = 0.0F;
   private int currentProfile = 0;
   private int nextProfile = 1;
   private float profileBlend = 0.0F;
   private float wanderYaw = 0.0F;
   private float wanderPitch = 0.0F;
   private float errorYaw = 0.0F;
   private float errorPitch = 0.0F;
   private int tickCounter = 0;
   private float lastTotalDelta = 0.0F;
   private float yawVelocity = 0.0F;
   private float pitchVelocity = 0.0F;

   public AIRotations() {
      this.initializeSession();
   }

   private void initializeSession() {
      this.sessionStartTime = System.currentTimeMillis();
      this.lastProfileChange = this.sessionStartTime;
      this.fatigueLevel = 0.0F;
      this.currentProfile = RANDOM.nextInt(4);
      this.nextProfile = (this.currentProfile + 1 + RANDOM.nextInt(3)) % 4;
      this.profileBlend = 0.0F;
      this.regenerateParameters();
   }

   private void regenerateParameters() {
      float var1 = class_3532.method_16439(this.profileBlend, this.getProfileFactor(this.currentProfile), this.getProfileFactor(this.nextProfile));
      float var2 = class_3532.method_16439(this.profileBlend, this.getJitterFactor(this.currentProfile), this.getJitterFactor(this.nextProfile));
      this.baseYawSpeed = (52.0F + this.randomRange(-5.0F, 8.0F)) * var1;
      this.basePitchSpeed = (26.0F + this.randomRange(-3.0F, 4.0F)) * var1;
      this.fastYawSpeed = (58.0F + this.randomRange(-8.0F, 12.0F)) * var1;
      this.fastPitchSpeed = (28.0F + this.randomRange(-4.0F, 6.0F)) * var1;
      this.yawThreshold = 38.0F + this.randomRange(-8.0F, 12.0F);
      this.pitchThreshold = 16.0F + this.randomRange(-4.0F, 6.0F);
      this.microJitterYaw = (1.2F + this.randomRange(-0.4F, 0.7F)) * var2;
      this.microJitterPitch = (0.55F + this.randomRange(-0.2F, 0.4F)) * var2;
      this.historySize = 2 + RANDOM.nextInt(2);
   }

   private float getProfileFactor(int var1) {
      float var10000;
      switch (var1) {
         case 0 -> var10000 = 1.25F + this.randomRange(-0.1F, 0.15F);
         case 1 -> var10000 = 0.75F + this.randomRange(-0.1F, 0.1F);
         case 2 -> var10000 = 1.0F + this.randomRange(-0.15F, 0.15F);
         case 3 -> var10000 = 0.85F + this.randomRange(-0.1F, 0.1F);
         default -> var10000 = 1.0F;
      }

      return var10000;
   }

   private float getJitterFactor(int var1) {
      float var10000;
      switch (var1) {
         case 0 -> var10000 = 0.8F + this.randomRange(-0.1F, 0.2F);
         case 1 -> var10000 = 0.6F + this.randomRange(-0.1F, 0.15F);
         case 2 -> var10000 = 1.8F + this.randomRange(-0.2F, 0.4F);
         case 3 -> var10000 = 1.0F + this.randomRange(-0.15F, 0.2F);
         default -> var10000 = 1.0F;
      }

      return var10000;
   }

   public Rotation process(Rotation var1, class_243 var2, class_1297 var3) {
      if (mc.field_1724 != null && mc.field_1687 != null && var3 != null) {
         if (Float.isNaN(var1.yaw())) {
            var1 = new Rotation(mc.field_1724.method_36454(), mc.field_1724.method_36455());
         }

         if (!(var3 instanceof class_1309)) {
            return var1;
         } else if (var2 == null) {
            return var1;
         } else {
            long var4 = System.currentTimeMillis();
            ++this.tickCounter;
            this.updateDynamicState(var4);
            if (this.isPaused) {
               if (var4 < this.lastPauseTime + this.pauseDuration) {
                  float var22 = this.randomRange(-0.3F, 0.3F) * (1.0F + this.fatigueLevel);
                  float var23 = this.randomRange(-0.15F, 0.15F) * (1.0F + this.fatigueLevel);
                  return this.applyGcd(new Rotation(var1.yaw() + var22, class_3532.method_15363(var1.pitch() + var23, -90.0F, 90.0F)), var1);
               }

               this.isPaused = false;
            }

            if (this.shouldPause(var4)) {
               this.isPaused = true;
               this.lastPauseTime = var4;
               this.pauseDuration = (long)(50 + RANDOM.nextInt(200));
               return var1;
            } else {
               float var6 = class_3532.method_15393((float)Math.toDegrees(Math.atan2(var2.field_1350, var2.field_1352)) - 90.0F - var1.yaw());
               float var7 = var1.yaw() + var6;
               float var8 = (float)(-Math.toDegrees(Math.atan2(var2.field_1351, Math.hypot(var2.field_1350, var2.field_1352))));
               float var9 = class_3532.method_15393(var7 - var1.yaw());
               float var10 = class_3532.method_15393(var8 - var1.pitch());
               float var11 = (float)Math.hypot((double)var9, (double)var10);
               this.updateMovePhase(var11, false);
               this.updateRandomErrors(var4, var11);
               this.updateWander(var4, false);
               float var12 = 1.0F - this.fatigueLevel * 0.25F;
               float var13 = this.calculateYawSpeed(var9, var11) * var12;
               float var14 = this.calculatePitchSpeed(var10, var11) * var12;
               float var15 = this.applyHumanEasing(var9, var13);
               float var16 = this.applyHumanEasing(var10, var14);
               float var17 = 1.0F + this.fatigueLevel * 0.8F;
               float var18 = this.calculateMicroJitter(this.microJitterYaw * var17, var4, true);
               float var19 = this.calculateMicroJitter(this.microJitterPitch * var17, var4, false);
               var15 = this.smoothWithHistory(var15, this.yawHistory);
               var16 = this.smoothWithHistory(var16, this.pitchHistory);
               if (var11 < 10.0F) {
                  float var20 = this.calculateOvershoot(var11);
                  var15 *= 1.0F + var20;
                  var16 *= 1.0F + var20;
               }

               var15 += this.errorYaw + this.wanderYaw;
               var16 += this.errorPitch + this.wanderPitch;
               float var28 = var1.yaw() + var15 + var18;
               float var21 = class_3532.method_15363(var1.pitch() + var16 + var19, -89.9F, 89.9F);
               this.lastTotalDelta = var11;
               this.yawVelocity = var15;
               this.pitchVelocity = var16;
               return this.applyGcd(new Rotation(var28, var21), var1);
            }
         }
      } else {
         return var1;
      }
   }

   private Rotation applyGcd(Rotation var1, Rotation var2) {
      double var3 = Math.pow((Double)mc.field_1690.method_42495().method_41753() * 0.6 + 0.2, (double)3.0F) * 1.2;
      float var5 = (float)((double)var1.yaw() - (double)(var1.yaw() - var2.yaw()) % var3);
      float var6 = (float)((double)var1.pitch() - (double)(var1.pitch() - var2.pitch()) % var3);
      return new Rotation(var5, var6);
   }

   private void updateDynamicState(long var1) {
      long var3 = var1 - this.sessionStartTime;
      this.fatigueLevel = Math.min(1.0F, (float)var3 / (60000.0F * (20.0F + this.randomRange(0.0F, 8.0F))));
      long var5 = (long)(120000 + RANDOM.nextInt(90000));
      if (var1 - this.lastProfileChange > var5) {
         this.currentProfile = this.nextProfile;
         this.nextProfile = (this.currentProfile + 1 + RANDOM.nextInt(3)) % 4;
         this.profileBlend = 0.0F;
         this.lastProfileChange = var1;
         this.regenerateParameters();
      } else {
         this.profileBlend = Math.min(1.0F, (float)(var1 - this.lastProfileChange) / 30000.0F);
      }

      if (this.tickCounter % (600 + RANDOM.nextInt(1200)) == 0) {
         this.baseYawSpeed *= 0.95F + this.randomRange(0.0F, 0.1F);
         this.basePitchSpeed *= 0.95F + this.randomRange(0.0F, 0.1F);
         this.microJitterYaw *= 0.9F + this.randomRange(0.0F, 0.2F);
         this.microJitterPitch *= 0.9F + this.randomRange(0.0F, 0.2F);
      }

   }

   private boolean shouldPause(long var1) {
      float var3 = 8.0E-4F + this.fatigueLevel * 0.002F;
      if (var1 - this.lastPauseTime > 15000L) {
         var3 *= 1.5F;
      }

      if (this.currentProfile == 3) {
         var3 *= 1.3F;
      }

      return RANDOM.nextFloat() < var3;
   }

   private void updateRandomErrors(long var1, float var3) {
      if (var1 > this.errorEndTime) {
         this.errorYaw = 0.0F;
         this.errorPitch = 0.0F;
         float var4 = 0.005F + this.fatigueLevel * 0.01F;
         if (RANDOM.nextFloat() < var4) {
            float var5 = 2.0F + this.randomRange(0.0F, 6.0F);
            float var6 = this.randomRange(0.0F, 360.0F) * ((float)Math.PI / 180F);
            this.errorYaw = (float)(Math.cos((double)var6) * (double)var5);
            this.errorPitch = (float)(Math.sin((double)var6) * (double)var5 * (double)0.5F);
            this.errorEndTime = var1 + 100L + (long)RANDOM.nextInt(300);
         }
      } else {
         this.errorYaw *= 0.92F;
         this.errorPitch *= 0.92F;
      }

   }

   private void updateWander(long var1, boolean var3) {
      if (var1 - this.lastWanderUpdate > (long)(500 + RANDOM.nextInt(1000))) {
         this.lastWanderUpdate = var1;
         float var4 = var3 ? 0.3F : 1.0F;
         var4 *= 1.0F + this.fatigueLevel * 0.5F;
         this.wanderYaw = class_3532.method_16439(0.3F, this.wanderYaw, this.randomRange(-1.5F, 1.5F) * var4);
         this.wanderPitch = class_3532.method_16439(0.3F, this.wanderPitch, this.randomRange(-0.8F, 0.8F) * var4);
      }

   }

   private float calculateYawSpeed(float var1, float var2) {
      float var3 = Math.abs(var1);
      float var4 = 1.0F + this.randomRange(-0.08F, 0.08F);
      if (var3 > this.yawThreshold) {
         float var7 = class_3532.method_16439(Math.min(var3 / 90.0F, 1.0F), 0.65F + this.randomRange(0.0F, 0.1F), 1.15F + this.randomRange(0.0F, 0.1F));
         return Math.max(25.0F, this.fastYawSpeed * var7 * this.getPhaseMultiplier() * var4);
      } else {
         float var5 = 1.0F - var3 / this.yawThreshold;
         float var6 = class_3532.method_16439(var5, 0.85F + this.randomRange(0.0F, 0.1F), 0.35F + this.randomRange(0.0F, 0.1F));
         return this.baseYawSpeed * var6 * this.getPhaseMultiplier() * var4;
      }
   }

   private float calculatePitchSpeed(float var1, float var2) {
      float var3 = Math.abs(var1);
      float var4 = 1.0F + this.randomRange(-0.08F, 0.08F);
      if (var3 > this.pitchThreshold) {
         float var7 = class_3532.method_16439(Math.min(var3 / 45.0F, 1.0F), 0.55F + this.randomRange(0.0F, 0.1F), 1.05F + this.randomRange(0.0F, 0.1F));
         return this.fastPitchSpeed * var7 * this.getPhaseMultiplier() * var4;
      } else {
         float var5 = 1.0F - var3 / this.pitchThreshold;
         float var6 = class_3532.method_16439(var5, 0.8F + this.randomRange(0.0F, 0.1F), 0.3F + this.randomRange(0.0F, 0.1F));
         return this.basePitchSpeed * var6 * this.getPhaseMultiplier() * var4;
      }
   }

   private float applyHumanEasing(float var1, float var2) {
      if (Math.abs(var1) < 0.01F) {
         return 0.0F;
      } else {
         float var3 = Math.signum(var1);
         float var4 = Math.abs(var1);
         float var5 = var2 * (0.9F + this.randomRange(0.0F, 0.2F));
         float var6 = Math.min(var4, var5);
         float var7 = var6 / Math.max(var4, 1.0F);
         float var8 = 1.4F + this.randomRange(0.0F, 0.3F) + this.fatigueLevel * 0.2F;
         float var9 = 1.0F - (float)Math.pow((double)(1.0F - var7), (double)var8);
         float var10 = 0.05F + this.fatigueLevel * 0.05F;
         float var11 = 1.0F + this.randomRange(-var10, var10);
         return var3 * var6 * var9 * var11;
      }
   }

   private float calculateMicroJitter(float var1, long var2, boolean var4) {
      float var5 = 1.0F + this.fatigueLevel * 0.3F + this.randomRange(-0.1F, 0.1F);
      double var6 = var4 ? (double)(80 + this.currentProfile * 5) : (double)(115 + this.currentProfile * 7);
      double var8 = var4 ? (double)(40 + this.currentProfile * 3) : (double)(62 + this.currentProfile * 5);
      double var10 = var4 ? (double)(150 + this.currentProfile * 8) : (double)(195 + this.currentProfile * 10);
      double var12 = (double)200.0F + (double)RANDOM.nextInt(100);
      float var14 = (float)Math.sin((double)var2 / (var6 * (double)var5)) * (0.45F + this.randomRange(0.0F, 0.1F));
      float var15 = (float)Math.sin((double)var2 / (var8 * (double)var5)) * (0.28F + this.randomRange(0.0F, 0.08F));
      float var16 = (float)Math.cos((double)var2 / (var10 * (double)var5)) * (0.18F + this.randomRange(0.0F, 0.06F));
      float var17 = (float)Math.sin((double)var2 / var12) * (0.12F + this.randomRange(0.0F, 0.05F));
      float var18 = (var14 + var15 + var16 + var17) * var1;
      var18 += this.randomRange(-var1 * 0.35F, var1 * 0.35F);
      if (RANDOM.nextFloat() < 0.008F) {
         var18 += this.randomRange(-var1 * 2.0F, var1 * 2.0F);
      }

      return var18;
   }

   private float smoothWithHistory(float var1, LinkedList<Float> var2) {
      var2.addLast(var1);

      while(var2.size() > this.historySize) {
         var2.removeFirst();
      }

      float var3 = 0.0F;
      float var4 = 0.0F;
      int var5 = 0;
      float var6 = 1.4F + this.randomRange(0.0F, 0.2F);

      for(float var8 : var2) {
         float var9 = (float)Math.pow((double)var6, (double)var5) * (0.95F + this.randomRange(0.0F, 0.1F));
         var3 += var8 * var9;
         var4 += var9;
         ++var5;
      }

      return var3 / var4;
   }

   private float calculateOvershoot(float var1) {
      float var2 = 1.0F - Math.min(var1 / 10.0F, 1.0F);
      float var3 = (0.12F + this.fatigueLevel * 0.08F) * var2;
      float var10000;
      switch (this.currentProfile) {
         case 0 -> var10000 = 0.35F;
         case 1 -> var10000 = 0.15F;
         case 2 -> var10000 = 0.3F;
         default -> var10000 = 0.25F;
      }

      float var4 = var10000;
      var4 += this.fatigueLevel * 0.1F;
      if (RANDOM.nextFloat() < var4) {
         return RANDOM.nextFloat() < 0.3F ? this.randomRange(-var3 * 0.5F, 0.0F) : this.randomRange(0.0F, var3);
      } else {
         return 0.0F;
      }
   }

   private void updateMovePhase(float var1, boolean var2) {
      this.phaseProgress += 0.05F;
      if (var1 > 60.0F) {
         this.movePhase = 0;
      } else if (var1 < 5.0F && var2) {
         this.movePhase = 2;
      } else {
         this.movePhase = 1;
      }

   }

   private float getPhaseMultiplier() {
      float var10000;
      switch (this.movePhase) {
         case 0 -> var10000 = 1.35F + this.randomRange(-0.1F, 0.15F);
         case 1 -> var10000 = 1.05F + this.randomRange(-0.1F, 0.1F);
         case 2 -> var10000 = 0.55F + this.randomRange(-0.06F, 0.06F);
         default -> var10000 = 1.0F;
      }

      float var1 = var10000;
      return var1 * (1.0F - this.fatigueLevel * 0.12F);
   }

   public static class_243 getBestVector(class_1309 var0, float var1) {
      if (mc.field_1724 != null && var0 != null) {
         double var2 = class_3532.method_15350(mc.field_1724.method_23320() - var0.method_23320(), (double)var0.method_17682() / (double)2.0F, (double)var0.method_17682()) / (double)(mc.field_1724.method_6128() ? 10.0F : (!mc.field_1690.field_1903.method_1434() && mc.field_1724.method_24828() ? (var0.method_5715() ? 0.8F : 0.6F) : 1.0F));
         return var0.method_19538().method_1031((double)0.0F, var2, (double)0.0F).method_1031((double)var1, (double)var1 / (double)2.0F, (double)var1).method_1020(mc.field_1724.method_33571()).method_1029();
      } else {
         return null;
      }
   }

   public void startSmoothHandover() {
      this.yawVelocity *= 0.25F;
      this.pitchVelocity *= 0.25F;
   }

   public boolean isSmoothingActive() {
      return Math.abs(this.yawVelocity) > 0.6F || Math.abs(this.pitchVelocity) > 0.4F;
   }

   public float getYawVelocity() {
      return this.yawVelocity;
   }

   public float getPitchVelocity() {
      return this.pitchVelocity;
   }

   private float randomRange(float var1, float var2) {
      return class_3532.method_16439(RANDOM.nextFloat(), var1, var2);
   }

   public void reset() {
      this.yawVelocity = 0.0F;
      this.pitchVelocity = 0.0F;
      this.yawHistory.clear();
      this.pitchHistory.clear();
      this.errorYaw = 0.0F;
      this.errorPitch = 0.0F;
      this.wanderYaw = 0.0F;
      this.wanderPitch = 0.0F;
      this.tickCounter = 0;
      this.initializeSession();
   }

   public static record Rotation(float yaw, float pitch) {
   }
}
