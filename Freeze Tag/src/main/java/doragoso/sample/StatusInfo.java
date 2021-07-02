package doragoso.sample;

import java.util.HashMap;
import java.util.Map;

public class StatusInfo {
    public static Map<String, Float> Speed = new HashMap<>();
    public static Map<String, Double> Damage = new HashMap<>();
    public static final Map<String, Long> Cooldown = new HashMap<>();
    public static final Long trickLeft = 20L; //トリックの左クリックのクールダウン
    static {
        Cooldown.put("feather", 8L);
        Cooldown.put("bone", 5L);
        Cooldown.put("trick", 60L);
        Cooldown.put("highjump", 10L);
        Cooldown.put("medikit", 2L);
        Cooldown.put("detect", 60L);
        Cooldown.put("self_enchant", 60L);
        Cooldown.put("superjump", 30L);
        Cooldown.put("ultrajump", 180L);
        Cooldown.put("marking", 30L);
    }
    static {
        Speed.put("ベーシック", 1.0F);
        Speed.put("トリックスター", 1.0F);
        Speed.put("メディック", 1.0F);
        Speed.put("ディテクティブ", 1.0F);
        Speed.put("ラビット", 1.05F);
        Speed.put("エンチャンター", 1.0F);
        Speed.put("ボルト", 1.1F);
    }
}
