package model;

public class PublicKeyItem {
    private long id;
    private int countOrderSignature;
    private String createdAt;
    private String publicKey;
    private int status;
    private String updatedAt;
    private long userId;

    public PublicKeyItem(long id, int countOrderSignature, String createdAt, String publicKey, int status, String updatedAt, long userId) {
        this.id = id;
        this.countOrderSignature = countOrderSignature;
        this.createdAt = createdAt;
        this.publicKey = publicKey;
        this.status = status;
        this.updatedAt = updatedAt;
        this.userId = userId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getCountOrderSignature() {
        return countOrderSignature;
    }

    public void setCountOrderSignature(int countOrderSignature) {
        this.countOrderSignature = countOrderSignature;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}
