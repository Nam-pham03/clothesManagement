package com.example.project_prm.Activity.User.VirtualRoom;

public class TryOnRequest {
    private String model_image;
    private String cloth_image;
    private String category;
    private int num_inference_steps;
    private int guidance_scale;
    private int seed;
    private boolean base64;

    public TryOnRequest(String model_image, String cloth_image, String category, int num_inference_steps, int guidance_scale, int seed, boolean base64) {
        this.model_image = model_image;
        this.cloth_image = cloth_image;
        this.category = category;
        this.num_inference_steps = num_inference_steps;
        this.guidance_scale = guidance_scale;
        this.seed = seed;
        this.base64 = base64;
    }

    public String getModel_image() {
        return model_image;
    }

    public void setModel_image(String model_image) {
        this.model_image = model_image;
    }

    public boolean isBase64() {
        return base64;
    }

    public void setBase64(boolean base64) {
        this.base64 = base64;
    }

    public int getSeed() {
        return seed;
    }

    public void setSeed(int seed) {
        this.seed = seed;
    }

    public int getGuidance_scale() {
        return guidance_scale;
    }

    public void setGuidance_scale(int guidance_scale) {
        this.guidance_scale = guidance_scale;
    }

    public int getNum_inference_steps() {
        return num_inference_steps;
    }

    public void setNum_inference_steps(int num_inference_steps) {
        this.num_inference_steps = num_inference_steps;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCloth_image() {
        return cloth_image;
    }

    public void setCloth_image(String cloth_image) {
        this.cloth_image = cloth_image;
    }
}

