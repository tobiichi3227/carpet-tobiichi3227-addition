package net.tobiichi3227.carpet.addition.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.tobiichi3227.carpet.addition.CarpetTobiichi3227AdditionSettings;
import net.tobiichi3227.carpet.addition.utils.Lobotomizable;

import java.util.List;

public class VillagerLobotomizeCommand {
    private static final double REACH = 10.0;

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
            CommandManager.literal("lobotomize")
                .requires(source -> source.hasPermissionLevel(2))
                .executes(ctx -> toggle(ctx))
                .then(CommandManager.literal("toggle")
                    .executes(ctx -> toggle(ctx)))
                .then(CommandManager.literal("all")
                    .executes(ctx -> lobotomizeAll(ctx)))
                .then(CommandManager.literal("restore")
                    .executes(ctx -> restoreAll(ctx)))
                .then(CommandManager.literal("status")
                    .executes(ctx -> status(ctx)))
        );
    }

    private static VillagerEntity getTargetVillager(CommandContext<ServerCommandSource> ctx) {
        ServerCommandSource source = ctx.getSource();
        Entity executor = source.getEntity();
        if (executor == null) return null;

        Vec3d eyePos = executor.getEyePos();
        Vec3d look = executor.getRotationVec(1.0F);
        Vec3d endPos = eyePos.add(look.multiply(REACH));
        Box box = executor.getBoundingBox().stretch(look.multiply(REACH)).expand(1.0);

        EntityHitResult result = ProjectileUtil.getEntityCollision(
            executor.getWorld(), executor, eyePos, endPos, box,
            e -> e instanceof VillagerEntity
        );

        if (result != null && result.getEntity() instanceof VillagerEntity villager) {
            return villager;
        }
        return null;
    }

    private static List<VillagerEntity> getAllVillagers(ServerWorld world) {
        Box worldBounds = new Box(-3.0E7, -64.0, -3.0E7, 3.0E7, 320.0, 3.0E7);
        return world.getEntitiesByClass(VillagerEntity.class, worldBounds, e -> true);
    }

    private static int toggle(CommandContext<ServerCommandSource> ctx) {
        ServerCommandSource source = ctx.getSource();
        if (!CarpetTobiichi3227AdditionSettings.villagerLobotomize) {
            source.sendError(Text.translatable("commands.lobotomize.disabled"));
            return 0;
        }

        VillagerEntity villager = getTargetVillager(ctx);
        if (villager == null) {
            source.sendError(Text.translatable("commands.lobotomize.no_target"));
            return 0;
        }

        Lobotomizable lobotomizable = (Lobotomizable) villager;
        boolean newState = !lobotomizable.isLobotomized();
        lobotomizable.setLobotomized(newState);

        if (newState) {
            source.sendFeedback(() -> Text.translatable("commands.lobotomize.toggle.on"), false);
        } else {
            source.sendFeedback(() -> Text.translatable("commands.lobotomize.toggle.off"), false);
        }
        return 1;
    }

    private static int lobotomizeAll(CommandContext<ServerCommandSource> ctx) {
        ServerCommandSource source = ctx.getSource();
        if (!CarpetTobiichi3227AdditionSettings.villagerLobotomize) {
            source.sendError(Text.translatable("commands.lobotomize.disabled"));
            return 0;
        }

        int count = 0;
        for (VillagerEntity villager : getAllVillagers(source.getWorld())) {
            Lobotomizable lob = (Lobotomizable) villager;
            if (!lob.isLobotomized()) {
                lob.setLobotomized(true);
                count++;
            }
        }
        final int finalCount = count;
        source.sendFeedback(() -> Text.translatable("commands.lobotomize.all", finalCount), true);
        return finalCount;
    }

    private static int restoreAll(CommandContext<ServerCommandSource> ctx) {
        ServerCommandSource source = ctx.getSource();
        if (!CarpetTobiichi3227AdditionSettings.villagerLobotomize) {
            source.sendError(Text.translatable("commands.lobotomize.disabled"));
            return 0;
        }
        int count = 0;
        for (VillagerEntity villager : getAllVillagers(source.getWorld())) {
            Lobotomizable lob = (Lobotomizable) villager;
            if (lob.isLobotomized()) {
                lob.setLobotomized(false);
                count++;
            }
        }
        final int finalCount = count;
        source.sendFeedback(() -> Text.translatable("commands.lobotomize.restore", finalCount), true);
        return finalCount;
    }

    private static int status(CommandContext<ServerCommandSource> ctx) {
        ServerCommandSource source = ctx.getSource();
        if (!CarpetTobiichi3227AdditionSettings.villagerLobotomize) {
            source.sendError(Text.translatable("commands.lobotomize.disabled"));
            return 0;
        }
        VillagerEntity villager = getTargetVillager(ctx);
        if (villager == null) {
            source.sendError(Text.translatable("commands.lobotomize.no_target"));
            return 0;
        }

        boolean state = ((Lobotomizable) villager).isLobotomized();
        if (state) {
            source.sendFeedback(() -> Text.translatable("commands.lobotomize.status.on"), false);
        } else {
            source.sendFeedback(() -> Text.translatable("commands.lobotomize.status.off"), false);
        }
        return 1;
    }
}
