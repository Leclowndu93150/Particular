package com.leclowndu93150.particular.network;

import com.leclowndu93150.particular.Main;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.function.Supplier;

public class NetworkHandler {
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(Main.MOD_ID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    private static int packetId = 0;

    public static void init() {
        // Register a dummy packet to ensure the mod is installed on both sides
        CHANNEL.registerMessage(
                getNextId(),
                DummyPacket.class,
                DummyPacket::encode,
                DummyPacket::decode,
                DummyPacket::handle
        );
    }

    private static int getNextId() {
        return packetId++;
    }

    public static <MSG> void sendToServer(MSG message) {
        CHANNEL.sendToServer(message);
    }

    public static <MSG> void sendToPlayer(MSG message, ServerPlayer player) {
        CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), message);
    }

    public static class DummyPacket {
        public DummyPacket() {
        }

        public static void encode(DummyPacket msg, FriendlyByteBuf buf) {
        }

        public static DummyPacket decode(FriendlyByteBuf buf) {
            return new DummyPacket();
        }

        public static void handle(DummyPacket msg, Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(() -> {
            });
            ctx.get().setPacketHandled(true);
        }
    }
}