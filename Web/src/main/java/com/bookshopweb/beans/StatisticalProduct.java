package com.bookshopweb.beans;

public class StatisticalProduct {
    private long productId;
    private String productName;
//    private Timestamp begin;
//    private Timestamp end;
    private int importQuatity;
    private int sellQuantity;
    private double totalImportPrice;
    private double totalSellPrice;
    private int quantityRemaining;
    private double saleRate;
    private int quantityRefund;

    public StatisticalProduct(long productId, String productName, int importQuatity, int sellQuantity, double totalImportPrice, double totalSellPrice, int quantityRemaining, double saleRate, int quantityRefund) {
        this.productId = productId;
        this.productName = productName;
        this.importQuatity = importQuatity;
        this.sellQuantity = sellQuantity;
        this.totalImportPrice = totalImportPrice;
        this.totalSellPrice = totalSellPrice;
        this.quantityRemaining = quantityRemaining;
        this.saleRate = saleRate;
        this.quantityRefund = quantityRefund;
    }

    public StatisticalProduct(long productId, String productName, int importQuatity, int sellQuantity, double totalImportPrice, double totalSellPrice) {
        this.productId = productId;
        this.productName = productName;
        this.importQuatity = importQuatity;
        this.sellQuantity = sellQuantity;
        this.totalImportPrice = totalImportPrice;
        this.totalSellPrice = totalSellPrice;

    }

    public StatisticalProduct(long productId, String productName, int sellQuantity, double totalSellPrice) {
        this.productId = productId;
        this.productName = productName;
        this.sellQuantity = sellQuantity;
        this.totalSellPrice = totalSellPrice;
    }

    public StatisticalProduct(long productId, String productName, int quantityRemaining, int sellQuantity) {
        this.productId = productId;
        this.productName = productName;
        this.quantityRemaining = quantityRemaining;
        this.sellQuantity = sellQuantity;
    }

    public StatisticalProduct(long productId, String productName, int importQuatity, int sellQuantity, int quantityRemaining, double saleRate) {
        this.productId = productId;
        this.productName = productName;
        this.importQuatity = importQuatity;
        this.sellQuantity = sellQuantity;
        this.quantityRemaining = quantityRemaining;
        this.saleRate = saleRate;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getImportQuatity() {
        return importQuatity;
    }

    public void setImportQuatity(int importQuatity) {
        this.importQuatity = importQuatity;
    }

    public int getSellQuantity() {
        return sellQuantity;
    }

    public void setSellQuantity(int sellQuantity) {
        this.sellQuantity = sellQuantity;
    }

    public double getTotalImportPrice() {
        return totalImportPrice;
    }

    public void setTotalImportPrice(double totalImportPrice) {
        this.totalImportPrice = totalImportPrice;
    }

    public double getTotalSellPrice() {
        return totalSellPrice;
    }

    public void setTotalSellPrice(double totalSellPrice) {
        this.totalSellPrice = totalSellPrice;
    }

    public int getQuantityRemaining() {
        return quantityRemaining;
    }

    public void setQuantityRemaining(int quantityRemaining) {
        this.quantityRemaining = quantityRemaining;
    }

    public double getSaleRate() {
        return saleRate;
    }

    public void setSaleRate(double saleRate) {
        this.saleRate = saleRate;
    }

    public int getQuantityRefund() {
        return quantityRefund;
    }

    public void setQuantityRefund(int quantityRefund) {
        this.quantityRefund = quantityRefund;
    }

    @Override
    public String toString() {
        return "StatisticalProduct{" +
                "idProduct=" + productId +
                ", nameProduct='" + productName + '\'' +
                ", importQuatity=" + importQuatity +
                ", sellQuantity=" + sellQuantity +
                ", totalImportPrice=" + totalImportPrice +
                ", totalSellPrice=" + totalSellPrice +
                ", quantityRemaining=" + quantityRemaining +
                ", saleRate=" + saleRate +
                ", quantityRefund=" + quantityRefund +
                '}';
    }
}
