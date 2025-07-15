/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package shop.model;

/**
 *
 * @author HoangSang
 */
public class StorePlatform {
    private int platformId;
    private Integer gameDetailsId; 
    private String storeOSName;

    public StorePlatform() {
    }

    public StorePlatform(int platformId, String storeOSName) {
        this.platformId = platformId;
        this.storeOSName = storeOSName;
    }

    public StorePlatform(int platformId, Integer gameDetailsId, String storeOSName) {
        this.platformId = platformId;
        this.gameDetailsId = gameDetailsId;
        this.storeOSName = storeOSName;
    }

    public int getPlatformId() {
        return platformId;
    }

    public void setPlatformId(int platformId) {
        this.platformId = platformId;
    }

    public Integer getGameDetailsId() {
        return gameDetailsId;
    }

    public void setGameDetailsId(Integer gameDetailsId) {
        this.gameDetailsId = gameDetailsId;
    }

    public String getStoreOSName() {
        return storeOSName;
    }

    public void setStoreOSName(String storeOSName) {
        this.storeOSName = storeOSName;
    }
    
}
