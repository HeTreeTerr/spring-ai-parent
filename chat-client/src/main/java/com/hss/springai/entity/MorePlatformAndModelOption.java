package com.hss.springai.entity;


/**
 * 多平台模型选项
 */
public class MorePlatformAndModelOption {
    /**
     * 平台
     */
    private String platform;
    /**
     * 模型
     */
    private String model;
    /**
     * 温度
     */
    private Double temperature;

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }
}
