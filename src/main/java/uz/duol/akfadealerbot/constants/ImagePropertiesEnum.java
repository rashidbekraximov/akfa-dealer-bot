package uz.duol.akfadealerbot.constants;

import java.util.Arrays;
import java.util.List;

public enum ImagePropertiesEnum {

    TOTAL_SALE("total_sale.png", 100, 200, 14.0f),
    TOTAL_INCOME("total_income.png", 150, 250, 16.0f),
    TOP_PRODUCT("top_product.png", 200, 300, 18.0f),
    TOP_ENTITLEMENT("total_entitlement.png", 250, 350, 20.0f),
    TOTAL_DEBT("total_debt.png", 300, 400, 22.0f);

    private final String imageName;
    private final int x;
    private final int y;
    private final float fontSize;

    ImagePropertiesEnum(String imageName, int x, int y, float fontSize) {
        this.imageName = imageName;
        this.x = x;
        this.y = y;
        this.fontSize = fontSize;
    }

    public String getImageName() {
        return imageName;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public float getFontSize() {
        return fontSize;
    }

    public static List<ImagePropertiesEnum> getAll() {
        return Arrays.asList(values());
    }
}

