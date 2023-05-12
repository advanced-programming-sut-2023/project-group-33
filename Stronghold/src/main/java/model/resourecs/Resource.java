package model.resourecs;

import java.util.HashMap;

public class Resource {
    private HashMap<ResourcesName, Integer> allResources;

    public Resource() {
        allResources = new HashMap<>();

        for (ResourcesName name : ResourcesName.values()) {
            allResources.put(name, 0);
        }
    }

    public void add(HashMap<ResourcesName,Integer> product){
        int s;
        for (ResourcesName resourcesName : product.keySet()) {
            s = this.allResources.get(resourcesName) + product.get(resourcesName);
            this.allResources.put(resourcesName,s);
        }
    }

    public void pay(HashMap<ResourcesName,Integer> price){
        int s;
        for (ResourcesName resourcesName : price.keySet()) {
            s = this.allResources.get(resourcesName) - price.get(resourcesName);
            this.allResources.put(resourcesName,s);
        }
    }

    public boolean checkPay(HashMap<ResourcesName,Integer> price){
        for (ResourcesName resourcesName : price.keySet()) {
            if(this.allResources.get(resourcesName) < price.get(resourcesName))
                return false;
        }

        return true;
    }

    public int getFoodDiversity(){
        int output = 0;
        for (ResourcesName food : ResourcesName.foods) {
            if (this.allResources.get(food) != 0)
                output ++;
        }
        return output;
    }

    public int getFoodAmount(){
        int output = 0;
        for (ResourcesName food : ResourcesName.foods) {
            output += this.allResources.get(food);
        }
        return output;
    }

}
