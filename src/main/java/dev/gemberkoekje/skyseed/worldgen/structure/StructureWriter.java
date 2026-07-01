package dev.gemberkoekje.skyseed.worldgen.structure;

import net.minecraft.SharedConstants;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.DoubleTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.level.block.state.BlockState;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Serialises a relative block map (0-based positions) into a vanilla {@code StructureTemplate} {@code .nbt}
 * file — the same format Minecraft's structure blocks save, so the result loads through
 * {@link net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager} and a
 * structure-block-authored {@code .nbt} would load equally well. Used as a dev-time generator to author
 * Skyseed's building templates in code (see {@link HamletTemplates}); not used at runtime.
 */
public final class StructureWriter {
    private StructureWriter() {}

    public static void write(Map<BlockPos, BlockState> blocks, Path file) throws IOException {
        write(blocks, Map.of(), file);
    }

    /** As {@link #write(Map, Path)}, but {@code blockEntities} attaches block-entity NBT (e.g. jigsaw blocks). */
    public static void write(Map<BlockPos, BlockState> blocks, Map<BlockPos, CompoundTag> blockEntities, Path file) throws IOException {
        write(blocks, blockEntities, Map.of(), file);
    }

    /**
     * As above, but {@code entities} bakes structure entities (keyed by their block cell) — e.g. a chest minecart on a
     * mineshaft rail. Each value is the entity NBT (must include {@code id}); it is stored centred on its cell at the
     * cell's Y, the way a structure block saves entities, so the jigsaw placement spawns it.
     */
    public static void write(Map<BlockPos, BlockState> blocks, Map<BlockPos, CompoundTag> blockEntities,
                             Map<BlockPos, CompoundTag> entities, Path file) throws IOException {
        write(blocks, Map.of(), blockEntities, entities, file);
    }

    /** A palette entry: a vanilla-analog {@link BlockState} whose serialised {@code Name} is overridden by
     *  {@code modName} when non-null. Two mod blocks sharing an analog (or a mod block vs. the same literal vanilla
     *  state) key distinctly, so they never collapse into one palette slot. */
    private record PaletteKey(BlockState state, String modName) {}

    /**
     * As above, but {@code modNames} maps positions to a mod block id (e.g. {@code biomeswevegone:skyris_planks}) that
     * <b>replaces</b> the serialised palette {@code Name} for that block, while the {@code blocks} value is a vanilla
     * <b>analog</b> of the same block class used for the shape math and property serialisation (BWG blocks subclass
     * the vanilla {@code StairBlock}/{@code SlabBlock}/{@code FenceBlock}/… so the property schema matches). This is
     * how Skyseed authors BWG-styled village templates in code without BWG on the classpath — the emitted {@code .nbt}
     * carries {@code biomeswevegone:} names and is only ever loaded when a village assembles over a BWG biome.
     */
    public static void write(Map<BlockPos, BlockState> blocks, Map<BlockPos, String> modNames,
                             Map<BlockPos, CompoundTag> blockEntities, Map<BlockPos, CompoundTag> entities,
                             Path file) throws IOException {
        int sx = 0, sy = 0, sz = 0;
        for (BlockPos p : blocks.keySet()) {
            sx = Math.max(sx, p.getX());
            sy = Math.max(sy, p.getY());
            sz = Math.max(sz, p.getZ());
        }

        final CompoundTag root = new CompoundTag();
        //? if >=26.1.2 {
        /*root.putInt("DataVersion", SharedConstants.getCurrentVersion().dataVersion().version());*/
        //?} else {
        root.putInt("DataVersion", SharedConstants.getCurrentVersion().getDataVersion().getVersion());
        //?}

        final ListTag size = new ListTag();
        size.add(IntTag.valueOf(sx + 1));
        size.add(IntTag.valueOf(sy + 1));
        size.add(IntTag.valueOf(sz + 1));
        root.put("size", size);

        final List<PaletteKey> palette = new ArrayList<>();
        final Map<PaletteKey, Integer> indexOf = new HashMap<>();
        final ListTag blocksTag = new ListTag();
        for (Map.Entry<BlockPos, BlockState> e : blocks.entrySet()) {
            final PaletteKey key = new PaletteKey(e.getValue(), modNames.get(e.getKey()));
            final int idx = indexOf.computeIfAbsent(key, k -> {
                palette.add(k);
                return palette.size() - 1;
            });
            final CompoundTag b = new CompoundTag();
            final ListTag pos = new ListTag();
            pos.add(IntTag.valueOf(e.getKey().getX()));
            pos.add(IntTag.valueOf(e.getKey().getY()));
            pos.add(IntTag.valueOf(e.getKey().getZ()));
            b.put("pos", pos);
            b.putInt("state", idx);
            final CompoundTag be = blockEntities.get(e.getKey());
            if (be != null) {
                b.put("nbt", be.copy());
            }
            blocksTag.add(b);
        }

        final ListTag paletteTag = new ListTag();
        for (PaletteKey key : palette) {
            final CompoundTag entry = NbtUtils.writeBlockState(key.state());
            if (key.modName() != null) {
                entry.putString("Name", key.modName()); // keep the analog's Properties, swap in the BWG block id
            }
            paletteTag.add(entry);
        }
        root.put("palette", paletteTag);
        root.put("blocks", blocksTag);

        final ListTag entitiesTag = new ListTag();
        for (Map.Entry<BlockPos, CompoundTag> e : entities.entrySet()) {
            final BlockPos bp = e.getKey();
            final CompoundTag ent = new CompoundTag();
            final ListTag pos = new ListTag(); // entity world pos (doubles), centred on the cell at its floor
            pos.add(DoubleTag.valueOf(bp.getX() + 0.5));
            pos.add(DoubleTag.valueOf(bp.getY()));
            pos.add(DoubleTag.valueOf(bp.getZ() + 0.5));
            ent.put("pos", pos);
            final ListTag blockPos = new ListTag();
            blockPos.add(IntTag.valueOf(bp.getX()));
            blockPos.add(IntTag.valueOf(bp.getY()));
            blockPos.add(IntTag.valueOf(bp.getZ()));
            ent.put("blockPos", blockPos);
            ent.put("nbt", e.getValue().copy());
            entitiesTag.add(ent);
        }
        root.put("entities", entitiesTag);

        Files.createDirectories(file.getParent());
        NbtIo.writeCompressed(root, file);
    }
}
