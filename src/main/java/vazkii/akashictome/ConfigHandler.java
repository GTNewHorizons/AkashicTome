package vazkii.akashictome;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class ConfigHandler {

    public static Configuration config;

    public static boolean allItems;
    public static List<String> whitelistedItems, blacklistedItems, whitelistedNames, whitelistedDuplicatesItems,
            blacklistedMods;

    public static Map<String, String> aliases = new HashMap<>();

    public static void init(File configFile) {
        config = new Configuration(configFile);

        config.load();
        load();

        MinecraftForge.EVENT_BUS.register(new ChangeListener());
    }

    public static void load() {
        allItems = loadPropBool("Allow all items to be added", false);

        whitelistedItems = loadPropStringList(
                "Whitelisted Items",
                "roots:runedtablet",
                "OpenComputers:item:98",
                "ImmersiveEngineering:tool:3",
                "integrateddynamics:on_the_dynamics_of_integration",
                "theoneprobe:probenote",
                "evilcraft:originsOfDarkness",
                "DraconicEvolution:infoTablet",
                "witchery:ingredient:46",
                "witchery:ingredient:47",
                "witchery:ingredient:48",
                "witchery:ingredient:49",
                "witchery:ingredient:81",
                "witchery:ingredient:106",
                "witchery:ingredient:107",
                "witchery:ingredient:127",
                "witchery:vampirebook:9",
                "Thaumcraft:ItemEldritchObject:1",
                "BiblioCraft:item.StockroomCatalog",
                "BiblioCraft:item.AtlasBook",
                "structurelib:item.structurelib.constructableTrigger");

        blacklistedItems = loadPropStringList(
                "Blacklisted Items",
                "Automagy:blockBookshelfEnchanted",
                "Automagy:blockReqTome",
                "BiblioCraft:item.BigBook",
                "BiblioCraft:BookcaseFilled",
                "BiblioWoodsBoP:BiblioWoodBookcase",
                "BiblioWoodsForestry:BiblioWoodFstBookcase",
                "BiblioWoodsForestry:BiblioWoodFstBookcase2",
                "BiblioWoodsNatura:BiblioWoodBookcase",
                "HardcoreEnderExpansion:knowledge_note",
                "Natura:Natura.bookshelf",
                "OpenBlocks:guide",
                "OpenBlocks:builder_guide",
                "chisel:bookshelf",
                "gadomancy:BlockKnowledgeBook",
                "thaumicenergistics:thaumicenergistics.block.knowledge.inscriber",
                "thaumicenergistics:knowledge.core",
                "witchery:bookbiomes2",
                "witchery:vampirebook:0",
                "witchery:vampirebook:1",
                "witchery:vampirebook:2",
                "witchery:vampirebook:3",
                "witchery:vampirebook:4",
                "witchery:vampirebook:5",
                "witchery:vampirebook:6",
                "witchery:vampirebook:7",
                "witchery:vampirebook:8");

        whitelistedNames = loadPropStringList(
                "Whitelisted Names",
                "book",
                "tome",
                "lexicon",
                "nomicon",
                "manual",
                "knowledge",
                "pedia",
                "compendium",
                "guide",
                "codex",
                "journal");

        whitelistedDuplicatesItems = loadPropStringList(
                "Whitelisted Duplicates Items",
                "gregtech:gt.multiitem.books:*");

        blacklistedMods = loadPropStringList("Blacklisted Mods");

        aliases.clear();
        List<String> aliasesList = loadPropStringList(
                "Mod Aliases",
                "nautralpledge=botania",
                "incorporeal=botania",
                "thermalexpansion=thermalfoundation",
                "thermaldynamics=thermalfoundation",
                "thermalcultivation=thermalfoundation",
                "redstonearsenal=thermalfoundation",
                "rftoolsdim=rftools",
                "ae2stuff=appliedenergistics2",
                "animus=bloodmagic",
                "integrateddynamics=integratedtunnels",
                "mekanismgenerators=mekanism",
                "mekanismtools=mekanism",
                "questbook=betterquesting");

        for (String s : aliasesList) if (s.matches(".+?=.+")) {
            String[] tokens = s.toLowerCase().split("=");
            aliases.put(tokens[0], tokens[1]);
        }

        if (config.hasChanged()) config.save();
    }

    public static List<String> loadPropStringList(String propName, String... default_) {
        Property prop = config.get(Configuration.CATEGORY_GENERAL, propName, default_);
        return Arrays.asList(prop.getStringList());
    }

    public static boolean loadPropBool(String propName, boolean default_) {
        Property prop = config.get(Configuration.CATEGORY_GENERAL, propName, default_);
        return prop.getBoolean(default_);
    }

    public static class ChangeListener {

        @SubscribeEvent
        public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent eventArgs) {
            if (eventArgs.modID.equals(AkashicTome.MOD_ID)) load();
        }

    }
}
