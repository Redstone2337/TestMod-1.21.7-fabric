package net.redstone233.test.core.transaction;

import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import net.minecraft.village.TradeOffers;
import net.minecraft.village.VillagerProfession;
import net.redstone233.test.TestMod;
import net.redstone233.test.blocks.ModBlocks;
import net.redstone233.test.items.ModItems;

public class CustomTrades {
    public static void init() {
        TradeOfferHelper.registerVillagerOffers(
                VillagerProfession.FARMER, 2, factories -> {
                    factories.add(new TradeOffers.BuyItemFactory(ModItems.DELICIOUS_BLACK_GARLIC,4,10,5,10));
                    factories.add(new TradeOffers.SellItemFactory(ModItems.DELICIOUS_BLACK_GARLIC,10,4,5,2,0.5f));
                }
        );

        TradeOfferHelper.registerVillagerOffers(
                VillagerProfession.TOOLSMITH, 3, factories -> {
                    factories.add(new TradeOffers.ProcessItemFactory(
                            ModItems.RAW_SILICON,1,2,
                            ModItems.SILICON_INGOT,1,10,15,0.5f)
                    );
                    factories.add(new TradeOffers.ProcessItemFactory(
                            ModBlocks.SILICON_ORE,1,2,
                            ModItems.SILICON_INGOT,1,10,15,0.5f)
                    );
                    factories.add(new TradeOffers.ProcessItemFactory(
                            ModBlocks.DEEPSLATE_SILICON_ORE,1,2,
                            ModItems.SILICON_INGOT,1,2,15,0.5f)
                    );
                }
        );

        TradeOfferHelper.registerVillagerOffers(
                VillagerProfession.CLERIC, 5, factories -> {
                    factories.add(new TradeOffers.BuyItemFactory(ModItems.BLUE_CHEESE,1,1,10,64));
                    factories.add(new TradeOffers.SellItemFactory(ModItems.BLUE_CHEESE,64,1,1,2,0.5f));
                }
        );

        TradeOfferHelper.registerVillagerOffers(
                VillagerProfession.WEAPONSMITH, 9, factories -> {
                    factories.add(new TradeOffers.BuyItemFactory(ModItems.FREEZE_SWORD,1,1,10,64));
                    factories.add(new TradeOffers.SellItemFactory(ModItems.FREEZE_SWORD,64,1,1,2,0.5f));
                }
        );

        TestMod.LOGGER.info("自定义交易成功！");
    }
}
