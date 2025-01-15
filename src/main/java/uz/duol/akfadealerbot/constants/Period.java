package uz.duol.akfadealerbot.constants;

public enum Period {
    YESTERDAY,
    TODAY;


    public String toLowerCaseName() {
        return this.name().toLowerCase();
    }
}
