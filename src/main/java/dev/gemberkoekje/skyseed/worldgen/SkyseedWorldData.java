package dev.gemberkoekje.skyseed.worldgen;

import dev.gemberkoekje.skyseed.Skyseed;
//? if >=26.1.2 {
/*import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.gemberkoekje.skyseed.compat.Ids;
import net.minecraft.world.level.saveddata.SavedDataType;
import java.util.ArrayList;*/
//?}
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

/**
 * Per-world persisted flags: whether the curated start island was placed and where the player spawns on it,
 * plus which players have already been handled on join (given the guide, dropped on the start island). The
 * join flags live here rather than on the player's persistent data so they reliably survive relogs.
 */
public final class SkyseedWorldData extends SavedData {
    public static final String NAME = Skyseed.MODID + "_world";

    private boolean startPlaced = false;
    @Nullable
    private BlockPos startSpawn = null;
    @Nullable
    private String createdVersion = null;
    private final Set<UUID> guided = new HashSet<>();
    private final Set<UUID> spawned = new HashSet<>();

    /** Chunks Skyseed currently force-loads for in-progress grows, as {@code "<dimId>;<cx>;<cz>"} (5.3 / CRASHRESUMEPLAN):
     *  a clean stop drains every job so this ends empty, so anything present at startup is a crash leak to un-force. */
    private final Set<String> forcedChunks = new HashSet<>();
    /** In-progress island grows resumable after a hard crash (5.2), keyed by {@link PendingIsland#key()}. */
    private final Map<String, PendingIsland> pendingIslands = new LinkedHashMap<>();

    /** No-arg constructor used for a fresh instance (and by the codec's {@code SavedDataType} supplier on 26.1.2). */
    public SkyseedWorldData() {
    }

    // 26.1.2 replaced SavedData's save(CompoundTag)/load model with a Codec registered through a SavedDataType.
    //? if >=26.1.2 {
    /*// IMPORTANT: this Codec's on-disk schema must stay IDENTICAL to the 1.21.1 NBT load()/save() below — same keys,
    // same encodings — so a world upgraded 1.21.1 -> 26.1.2 keeps its start spawn and per-player join flags. Minecraft
    // applies no DataFixer to a mod's SavedData, so a divergent schema would silently drop them (lost respawn anchor,
    // re-issued guide books). Hence SpawnX/Y/Z as separate ints (not a packed BlockPos.CODEC) and Guided/Spawned as
    // lists of UUID *strings* (not UUIDUtil.CODEC_SET, which encodes int-array UUIDs).
    private static final Codec<Set<UUID>> UUID_STRING_SET = Codec.STRING.comapFlatMap(s -> {
        try {
            return DataResult.success(UUID.fromString(s));
        } catch (IllegalArgumentException e) {
            return DataResult.error(() -> "Not a UUID: " + s);
        }
    }, UUID::toString).listOf().xmap(HashSet::new, ArrayList::new);

    // A pending-island descriptor. Keys/encodings must stay identical to pendingToNbt/pendingFromNbt in the 1.21.1 block.
    private static final Codec<PendingIsland> PENDING_CODEC = RecordCodecBuilder.create(p -> p.group(
            Codec.STRING.fieldOf("Dim").forGetter(PendingIsland::dimId),
            Codec.STRING.fieldOf("Theme").forGetter(PendingIsland::themeId),
            Codec.INT.fieldOf("X").forGetter(PendingIsland::centerX),
            Codec.INT.fieldOf("Y").forGetter(PendingIsland::centerY),
            Codec.INT.fieldOf("Z").forGetter(PendingIsland::centerZ),
            Codec.STRING.optionalFieldOf("ForcedBiome", "").forGetter(PendingIsland::forcedBiome),
            Codec.INT.optionalFieldOf("ForcedRare", -1).forGetter(PendingIsland::forcedRare),
            Codec.BOOL.optionalFieldOf("ForcedWaterfall", false).forGetter(PendingIsland::forcedWaterfall),
            Codec.INT.optionalFieldOf("BlockIndex", 0).forGetter(PendingIsland::blockIndex),
            Codec.INT.optionalFieldOf("TreeIndex", 0).forGetter(PendingIsland::treeIndex),
            Codec.INT.optionalFieldOf("TreesPlaced", 0).forGetter(PendingIsland::treesPlaced),
            Codec.INT.optionalFieldOf("ScatterIndex", 0).forGetter(PendingIsland::scatterIndex),
            Codec.INT.optionalFieldOf("FinalizeStep", 0).forGetter(PendingIsland::finalizeStep)
    ).apply(p, PendingIsland::new));

    public static final Codec<SkyseedWorldData> CODEC = RecordCodecBuilder.create(i -> i.group(
            Codec.BOOL.optionalFieldOf("StartPlaced", false).forGetter(d -> d.startPlaced),
            Codec.INT.optionalFieldOf("SpawnX").forGetter(d -> d.startSpawn == null ? Optional.<Integer>empty() : Optional.of(d.startSpawn.getX())),
            Codec.INT.optionalFieldOf("SpawnY").forGetter(d -> d.startSpawn == null ? Optional.<Integer>empty() : Optional.of(d.startSpawn.getY())),
            Codec.INT.optionalFieldOf("SpawnZ").forGetter(d -> d.startSpawn == null ? Optional.<Integer>empty() : Optional.of(d.startSpawn.getZ())),
            Codec.STRING.optionalFieldOf("CreatedVersion").forGetter(d -> Optional.ofNullable(d.createdVersion)),
            UUID_STRING_SET.optionalFieldOf("Guided", new HashSet<>()).forGetter(d -> d.guided),
            UUID_STRING_SET.optionalFieldOf("Spawned", new HashSet<>()).forGetter(d -> d.spawned),
            Codec.STRING.listOf().optionalFieldOf("ForcedChunks", new ArrayList<>()).forGetter(d -> new ArrayList<>(d.forcedChunks)),
            PENDING_CODEC.listOf().optionalFieldOf("PendingIslands", new ArrayList<>()).forGetter(d -> new ArrayList<>(d.pendingIslands.values()))
    ).apply(i, SkyseedWorldData::new));

    public static final SavedDataType<SkyseedWorldData> TYPE =
            new SavedDataType<>(Ids.mod("world"), SkyseedWorldData::new, CODEC);

    private SkyseedWorldData(boolean startPlaced, Optional<Integer> spawnX, Optional<Integer> spawnY,
                             Optional<Integer> spawnZ, Optional<String> createdVersion,
                             Set<UUID> guided, Set<UUID> spawned,
                             List<String> forcedChunks, List<PendingIsland> pendingIslands) {
        this.startPlaced = startPlaced;
        if (spawnX.isPresent() && spawnY.isPresent() && spawnZ.isPresent()) {
            this.startSpawn = new BlockPos(spawnX.get(), spawnY.get(), spawnZ.get());
        }
        this.createdVersion = createdVersion.orElse(null);
        this.guided.addAll(guided);
        this.spawned.addAll(spawned);
        this.forcedChunks.addAll(forcedChunks);
        for (PendingIsland p : pendingIslands) {
            this.pendingIslands.put(p.key(), p);
        }
    }
    *///?} else {
    public static SavedData.Factory<SkyseedWorldData> factory() {
        return new SavedData.Factory<>(SkyseedWorldData::new, SkyseedWorldData::load);
    }

    public static SkyseedWorldData load(CompoundTag tag, HolderLookup.Provider provider) {
        SkyseedWorldData data = new SkyseedWorldData();
        data.startPlaced = tag.getBoolean("StartPlaced");
        if (tag.contains("SpawnX")) {
            data.startSpawn = new BlockPos(tag.getInt("SpawnX"), tag.getInt("SpawnY"), tag.getInt("SpawnZ"));
        }
        if (tag.contains("CreatedVersion")) {
            data.createdVersion = tag.getString("CreatedVersion");
        }
        readUuids(tag, "Guided", data.guided);
        readUuids(tag, "Spawned", data.spawned);
        for (Tag t : tag.getList("ForcedChunks", Tag.TAG_STRING)) {
            data.forcedChunks.add(t.getAsString());
        }
        for (Tag t : tag.getList("PendingIslands", Tag.TAG_COMPOUND)) {
            PendingIsland p = pendingFromNbt((CompoundTag) t);
            data.pendingIslands.put(p.key(), p);
        }
        return data;
    }

    @Override
    public CompoundTag save(CompoundTag tag, HolderLookup.Provider provider) {
        tag.putBoolean("StartPlaced", startPlaced);
        if (startSpawn != null) {
            tag.putInt("SpawnX", startSpawn.getX());
            tag.putInt("SpawnY", startSpawn.getY());
            tag.putInt("SpawnZ", startSpawn.getZ());
        }
        if (createdVersion != null) {
            tag.putString("CreatedVersion", createdVersion);
        }
        tag.put("Guided", writeUuids(guided));
        tag.put("Spawned", writeUuids(spawned));
        ListTag fc = new ListTag();
        for (String s : forcedChunks) {
            fc.add(StringTag.valueOf(s));
        }
        tag.put("ForcedChunks", fc);
        ListTag pi = new ListTag();
        for (PendingIsland p : pendingIslands.values()) {
            pi.add(pendingToNbt(p));
        }
        tag.put("PendingIslands", pi);
        return tag;
    }

    private static void readUuids(CompoundTag tag, String key, Set<UUID> into) {
        for (Tag t : tag.getList(key, Tag.TAG_STRING)) {
            try {
                into.add(UUID.fromString(t.getAsString()));
            } catch (IllegalArgumentException ignored) {
                // skip a malformed id rather than fail the whole load
            }
        }
    }

    private static ListTag writeUuids(Set<UUID> from) {
        ListTag list = new ListTag();
        for (UUID id : from) {
            list.add(StringTag.valueOf(id.toString()));
        }
        return list;
    }

    // Schema mirror of PENDING_CODEC above — same keys/encodings so a 1.21.1 -> 26.1.2 upgrade keeps in-progress grows.
    private static CompoundTag pendingToNbt(PendingIsland p) {
        CompoundTag t = new CompoundTag();
        t.putString("Dim", p.dimId());
        t.putString("Theme", p.themeId());
        t.putInt("X", p.centerX());
        t.putInt("Y", p.centerY());
        t.putInt("Z", p.centerZ());
        t.putString("ForcedBiome", p.forcedBiome());
        t.putInt("ForcedRare", p.forcedRare());
        t.putBoolean("ForcedWaterfall", p.forcedWaterfall());
        t.putInt("BlockIndex", p.blockIndex());
        t.putInt("TreeIndex", p.treeIndex());
        t.putInt("TreesPlaced", p.treesPlaced());
        t.putInt("ScatterIndex", p.scatterIndex());
        t.putInt("FinalizeStep", p.finalizeStep());
        return t;
    }

    private static PendingIsland pendingFromNbt(CompoundTag t) {
        return new PendingIsland(t.getString("Dim"), t.getString("Theme"),
                t.getInt("X"), t.getInt("Y"), t.getInt("Z"),
                t.getString("ForcedBiome"), t.getInt("ForcedRare"), t.getBoolean("ForcedWaterfall"),
                t.getInt("BlockIndex"), t.getInt("TreeIndex"), t.getInt("TreesPlaced"),
                t.getInt("ScatterIndex"), t.getInt("FinalizeStep"));
    }
    //?}

    public boolean isStartPlaced() {
        return startPlaced;
    }

    @Nullable
    public BlockPos getStartSpawn() {
        return startSpawn;
    }

    /** The Skyseed version this world was created on, or null for worlds made before stamping (pre-0.35.2). */
    @Nullable
    public String getCreatedVersion() {
        return createdVersion;
    }

    public void setCreatedVersion(String version) {
        this.createdVersion = version;
        setDirty();
    }

    /** Mark the start island as handled for this world; {@code spawn} is null for existing worlds (no island). */
    public void markStartPlaced(@Nullable BlockPos spawn) {
        this.startPlaced = true;
        this.startSpawn = spawn;
        setDirty();
    }

    /** Whether this player has already received the guide book (granted once, on first join). */
    public boolean hasGuided(UUID player) {
        return guided.contains(player);
    }

    public void markGuided(UUID player) {
        if (guided.add(player)) {
            setDirty();
        }
    }

    /** Whether this player has already been dropped on the start island (done once, on first join). */
    public boolean hasSpawned(UUID player) {
        return spawned.contains(player);
    }

    public void markSpawned(UUID player) {
        if (spawned.add(player)) {
            setDirty();
        }
    }

    // --- Crash-resume state (5.2 / 5.3 — CRASHRESUMEPLAN) ------------------------------------------------------------

    /** The overworld-scoped instance — all crash-resume state lives here, keyed by dimension id so twins are covered. */
    public static SkyseedWorldData get(MinecraftServer server) {
        ServerLevel overworld = server.overworld();
        //? if >=26.1.2 {
        /*return overworld.getDataStorage().computeIfAbsent(TYPE);*/
        //?} else {
        return overworld.getDataStorage().computeIfAbsent(factory(), NAME);
        //?}
    }

    public void addForcedChunk(String dimId, int cx, int cz) {
        if (forcedChunks.add(dimId + ";" + cx + ";" + cz)) {
            setDirty();
        }
    }

    public void removeForcedChunk(String dimId, int cx, int cz) {
        if (forcedChunks.remove(dimId + ";" + cx + ";" + cz)) {
            setDirty();
        }
    }

    /** A snapshot of the currently-recorded force-loaded chunks (each {@code "<dimId>;<cx>;<cz>"}). */
    public List<String> forcedChunks() {
        return List.copyOf(forcedChunks);
    }

    public void clearForcedChunks() {
        if (!forcedChunks.isEmpty()) {
            forcedChunks.clear();
            setDirty();
        }
    }

    /** Record (or replace, by {@link PendingIsland#key()}) an in-progress island grow for crash-resume. */
    public void putPendingIsland(PendingIsland island) {
        pendingIslands.put(island.key(), island);
        setDirty();
    }

    public void removePendingIsland(String key) {
        if (pendingIslands.remove(key) != null) {
            setDirty();
        }
    }

    /** A snapshot of the in-progress island grows recorded for crash-resume. */
    public Collection<PendingIsland> pendingIslands() {
        return List.copyOf(pendingIslands.values());
    }

    public void clearPendingIslands() {
        if (!pendingIslands.isEmpty()) {
            pendingIslands.clear();
            setDirty();
        }
    }
}
