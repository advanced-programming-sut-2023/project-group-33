package model.resource;

import utility.DataManager;

import java.util.ArrayList;
import java.util.HashMap;

public class ResourceHolder {
    protected final ResourcesName kind;
    protected int count;
    protected final HashMap<ResourcesName, Integer> price;
    protected int howManyFor1Price;
    protected static final ArrayList<ResourceHolder> ALL_RESOURCE_HOLDERS = new ArrayList<>();

    public static HashMap<ResourcesName, Integer> getResourcePrice(ResourcesName name) {
        return getResourceByResourcesName(name).getPrice();
    }

    static {
        ArrayList<String[]> resourceCsv = DataManager.getArrayListFromCSV(DataManager.WEAPONS_PATH);
        String[] attributeNames = resourceCsv.get(0);

        for (int i = 1; i < resourceCsv.size(); i++) {
            String[] attributes = resourceCsv.get(i);
            String type = null;
            String name = null;
            HashMap<ResourcesName, Integer> price = new HashMap<>();
            int damage = 0, attackRange = 0;
            int speedBoost = 0, defenceBoost = 0;
            int howMany = 0;
            for (int j = 0; j < attributeNames.length; j++) {
                switch (attributeNames[j]) {
                    case "Type": {
                        type = attributes[j];
                    }
                    case "Name": {
                        name = attributes[j];
                        break;
                    }
                    case "Price Kind": {
                        if (!attributes[j].equals("null")) {
                            price.put(ResourcesName.getResourceByName(attributes[j]), Integer.parseInt(attributes[j + 1]));
                        }
                        break;
                    }
                    case "Count": {
                        if (!attributes[j].equals("null")) {
                            howMany = Integer.parseInt(attributes[j]);
                        }
                        break;
                    }
                    case "Attack Range": {
                        if (!attributes[j].equals("null")) {
                            attackRange = Integer.parseInt(attributes[j]);
                        }
                        break;
                    }
                    case "Damage": {
                        if (!attributes[j].equals("null")) {
                            damage = Integer.parseInt(attributes[j]);
                        }
                        break;
                    }
                    case "Speed Boost": {
                        if (!attributes[j].equals("null"))
                            speedBoost = Integer.parseInt(attributes[j]);
                        break;
                    }
                    case "Defence Boost": {
                        if (!attributes[j].equals("null"))
                            defenceBoost = Integer.parseInt(attributes[j]);
                        break;
                    }
                }
            }

            switch (type) {
                case "Initial", "Food" -> {
                    new ResourceHolder(name, price, howMany);
                }
                case "Weapon" -> new Weapon(name, price, howMany, damage, attackRange);
                case "Armour" -> new Armour(name, price, howMany, speedBoost, defenceBoost);
            }
        }
    }

    protected ResourceHolder(String name, HashMap<ResourcesName, Integer> price, int howManyFor1Price) {
        kind = ResourcesName.getResourceByName(name);
        count = 0;
        this.price = price;
        this.howManyFor1Price = howManyFor1Price;

        ALL_RESOURCE_HOLDERS.add(this);
    }

    public ResourceHolder(String name, int count) {
        ResourceHolder resourceHolderToClone = getResourceByName(name);
        assert resourceHolderToClone != null;

        this.count = count;
        this.howManyFor1Price = resourceHolderToClone.howManyFor1Price;
        this.price = resourceHolderToClone.price;
        this.kind = resourceHolderToClone.kind;
    }

    public ResourceHolder(String name) {
        ResourceHolder resourceHolderToClone = getResourceByName(name);
        assert resourceHolderToClone != null;

        this.count = 0;
        this.howManyFor1Price = resourceHolderToClone.howManyFor1Price;
        this.price = resourceHolderToClone.price;
        this.kind = resourceHolderToClone.kind;
    }

    public ResourceHolder(ResourcesName name, int count) {
        ResourceHolder resourceHolderToClone = getResourceByResourcesName(name);
        assert resourceHolderToClone != null;

        this.count = count;
        this.price = resourceHolderToClone.getPrice();
        this.kind = resourceHolderToClone.getKind();
    }

    public ResourceHolder(ResourcesName name) {
        ResourceHolder resourceHolderToClone = getResourceByResourcesName(name);
        assert resourceHolderToClone != null;

        this.count = 0;
        this.price = resourceHolderToClone.getPrice();
        this.kind = resourceHolderToClone.getKind();
    }

    ResourceHolder getResourceByName(String name) {
        for (ResourceHolder resourceHolder : ALL_RESOURCE_HOLDERS) {
            if (resourceHolder.getKind().equals(ResourcesName.getResourceByName(name)))
                return resourceHolder;
        }
        return null;
    }

    static ResourceHolder getResourceByResourcesName(ResourcesName resourceName) {

        for (ResourceHolder resourceHolder : ALL_RESOURCE_HOLDERS) {
            if (resourceHolder.getKind().equals(resourceName))
                return resourceHolder;
        }
        return null;
    }

    public ResourcesName getKind() {
        return kind;
    }

    public int getCount() {
        return count;
    }

    public HashMap<ResourcesName, Integer> getPrice() {
        return price;
    }

    public void addCount(int count) {
        this.count += count;
    }

    public void removeCount(int count) {
        this.count -= count;
    }

    @Override
    public String toString() {
        return "Resource{" +
                "kind=" + kind +
                ", count=" + count +
                ", price=" + price +
                ", howManyFor1Price=" + howManyFor1Price +
                "}\n";
    }
}
