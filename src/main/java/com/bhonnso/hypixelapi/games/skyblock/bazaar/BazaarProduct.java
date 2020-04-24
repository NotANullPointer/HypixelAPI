package com.bhonnso.hypixelapi.games.skyblock.bazaar;

import com.bhonnso.hypixelapi.APIUtils;
import org.json.JSONObject;

import java.util.ArrayList;

public class BazaarProduct extends com.bhonnso.hypixelapi.JSONObject {

    private String productId;
    private QuickStatus quickStatus;
    private ArrayList<BuySellSummary> buySummary = new ArrayList<>();
    private ArrayList<BuySellSummary> sellSummary = new ArrayList<>();
    private ArrayList<HistoricStatus> weekHistoric = new ArrayList<>();

    public BazaarProduct(JSONObject data) {
        super(data);
        if(data.has("product_info"))
            data = data.getJSONObject("product_info");
        this.productId = data.getString("product_id");
        this.quickStatus = new QuickStatus(data.getJSONObject("quick_status"));
        APIUtils.extractJSONToArray(BuySellSummary.class, data.getJSONArray("buy_summary"), buySummary);
        APIUtils.extractJSONToArray(BuySellSummary.class, data.getJSONArray("sell_summary"), sellSummary);
        if(data.has("week_historic"))
            APIUtils.extractJSONToArray(HistoricStatus.class, data.getJSONArray("week_historic"), weekHistoric);
    }

    public QuickStatus getQuickStatus() {
        return quickStatus;
    }

    public ArrayList<BuySellSummary> getSellSummary() {
        return sellSummary;
    }

    public ArrayList<BuySellSummary> getBuySummary() {
        return buySummary;
    }

    public ArrayList<HistoricStatus> getWeekHistoric() {
        return weekHistoric;
    }

    public String getProductId() {
        return productId;
    }

    public static class BuySellSummary extends com.bhonnso.hypixelapi.JSONObject {
        private int amount;
        private float pricePerUnit;
        private int orders;

        public BuySellSummary(JSONObject data) {
            super(data);
            this.amount = data.getInt("amount");
            this.pricePerUnit = data.getFloat("pricePerUnit");
            this.orders = data.getInt("orders");
        }

        public int getAmount() {
            return amount;
        }

        public float getPricePerUnit() {
            return pricePerUnit;
        }

        public int getOrders() {
            return orders;
        }
    }

    public static class QuickStatus extends com.bhonnso.hypixelapi.JSONObject {
        private String productId;
        private float buyPrice;
        private int buyVolume;
        private int buyMovingWeek;
        private int buyOrders;
        private float sellPrice;
        private int sellVolume;
        private int sellMovingWeek;
        private int sellOrders;

        public QuickStatus(JSONObject data) {
            super(data);
            this.productId = data.getString("productId");
            this.buyPrice = data.getFloat("buyPrice");
            this.buyVolume = data.getInt("buyVolume");
            this.buyMovingWeek = data.getInt("buyMovingWeek");
            this.buyOrders = data.getInt("buyOrders");
            this.sellPrice = data.getFloat("sellPrice");
            this.sellVolume = data.getInt("sellVolume");
            this.sellMovingWeek = data.getInt("sellMovingWeek");
            this.sellOrders = data.getInt("sellOrders");
        }

        public String getProductId() {
            return productId;
        }

        public float getBuyPrice() {
            return buyPrice;
        }

        public int getBuyVolume() {
            return buyVolume;
        }

        public int getBuyMovingWeek() {
            return buyMovingWeek;
        }

        public int getBuyOrders() {
            return buyOrders;
        }

        public float getSellPrice() {
            return sellPrice;
        }

        public int getSellVolume() {
            return sellVolume;
        }

        public int getSellMovingWeek() {
            return sellMovingWeek;
        }

        public int getSellOrders() {
            return sellOrders;
        }
    }

    public static class HistoricStatus extends com.bhonnso.hypixelapi.JSONObject {
        private String productId;
        private long timestamp;
        private int nowBuyVolume;
        private int nowSellVolume;
        private float buyCoins;
        private int buyVolume;
        private int buys;
        private float sellCoins;
        private int sellVolume;
        private int sells;

        public HistoricStatus(JSONObject data) {
            super(data);
            this.productId = data.getString("productId");
            this.timestamp = data.getLong("timestamp");
            this.nowBuyVolume = data.getInt("nowBuyVolume");
            this.nowSellVolume = data.getInt("nowSellVolume");
            this.buyCoins = data.getFloat("buyCoins");
            this.buyVolume = data.getInt("buyVolume");
            this.buys = data.getInt("buys");
            this.sellCoins = data.getFloat("sellCoins");
            this.sellVolume = data.getInt("sellVolume");
            this.sells = data.getInt("sells");
        }

        public String getProductId() {
            return productId;
        }

        public long getTimestamp() {
            return timestamp;
        }

        public int getNowBuyVolume() {
            return nowBuyVolume;
        }

        public int getNowSellVolume() {
            return nowSellVolume;
        }

        public float getBuyCoins() {
            return buyCoins;
        }

        public int getBuyVolume() {
            return buyVolume;
        }

        public int getBuys() {
            return buys;
        }

        public float getSellCoins() {
            return sellCoins;
        }

        public int getSellVolume() {
            return sellVolume;
        }

        public int getSells() {
            return sells;
        }
    }


}

