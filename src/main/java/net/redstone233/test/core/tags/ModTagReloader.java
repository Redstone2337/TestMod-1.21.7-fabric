package net.redstone233.test.core.tags;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.redstone233.test.TestMod;

import java.io.InputStreamReader;
import java.util.Map;

public class ModTagReloader implements SimpleSynchronousResourceReloadListener {
    public static final ModTagReloader INSTANCE = new ModTagReloader();
    /** 要扫描的“目录”前缀，注意末尾不要带 '/' */
    private static final String ROOT = "custom_pack/data/mtc/tags/item";
    
    
    private ModTagReloader() {}

    @Override
    public Identifier getFabricId() {
        return Identifier.of(TestMod.MOD_ID, "mod_tags_reloader");
    }

    @Override
    public void reload(ResourceManager manager) {
        // 1. 找到目录下所有 .json 文件
        Map<Identifier, Resource> files = manager.findResources(ROOT, path -> path.toString().endsWith(".json"));

        // 2. for 循环逐个读取
        for (var entry : files.entrySet()) {
            Identifier fileId = entry.getKey();           // 形如 "custom_pack/data/mtc/tags/item/xxx.json"
            Resource  res     = entry.getValue();

            try (var reader = new InputStreamReader(res.getInputStream())) {
                JsonObject json = JsonParser.parseReader(reader).getAsJsonObject();
                // TODO: 把 json 解析成 TagFile、自己的 POJO，或直接刷新缓存
                TestMod.LOGGER.info("Loaded {} -> {}", fileId, json);
            } catch (Exception e) {
                TestMod.LOGGER.error("Failed to load {}", fileId, e);
            }
        }
    }
}
