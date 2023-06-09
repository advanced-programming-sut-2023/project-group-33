package model.resource;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public enum ResourcesName
{
//  Initial materials:
    GOLD("Gold"),
    WHEAT("Wheat"),
    FLOUR("Flour"),
    HOPS("Hops"),
    ALE("Ale"),
    STONE("Stone"),
    IRON("Iron"),
    WOOD("Wood"),
    PITCH("Pitch"),
    OIL("Oil"),

//  Foods:
    MEAT("Meat"),
    APPLE("Apple"),
    CHEESE("Cheese"),
    BREAD("Bread"),

//  Weapons:
    BOW("Bow"),
    CROSSBOW("Crossbow"),
    SPEAR("Spear"),
    PIKE("Pike"),
    MACE("Mace"),
    SWORD("Sword"),
    STAFF("Staff"),
    TORCH("Torch"),
    SLING("Sling"),
    SCIMITAR("Scimitar"),
    GREEK_FIRE("Greek Fire"),
    RAM("RAM"),
    THROWER("Thrower"),

//  Armors:
    LEATHER("Leather"),
    METAL("Metal"),
    HORSE("Horse"),

//  Others:
    COW("Cow"),
    PEOPLE("People");


    private final String name;
    public final static ArrayList<ResourcesName> Materials = new ArrayList<>(List.of(WHEAT,FLOUR,HOPS,ALE,STONE,WOOD,PITCH));
    public final static ArrayList<ResourcesName> foods = new ArrayList<>(List.of(MEAT, APPLE, CHEESE,BREAD));
    public final static ArrayList<ResourcesName> weapons = new ArrayList<>(List.of(BOW,CROSSBOW,SPEAR,PIKE,MACE,SWORD,LEATHER,METAL));

    ResourcesName(String name) {
        this.name = name;
    }

    @NotNull
    public static ResourcesName getResourceByName(String resourceName)
    {
        for (ResourcesName resource : ResourcesName.values()) {
            if (resource.name.equalsIgnoreCase(resourceName.trim()))
                return resource;
        }
        throw new RuntimeException();
    }

    public static boolean isValidName(String name) {
        for (ResourcesName value : ResourcesName.values()) {
            if (value.name().equalsIgnoreCase(name))
                return true;
        }
        return false;
    }



}
