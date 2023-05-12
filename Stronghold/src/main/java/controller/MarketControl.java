package controller;

import model.resourecs.ResourcesName;
import model.society.Government;

import java.util.HashMap;

public class MarketControl
{
    static HashMap<ResourcesName,Integer> buyPrice;
    static HashMap<ResourcesName,Integer> sellPrice;

    static {
        buyPrice = new HashMap<>();
        sellPrice = new HashMap<>();

        for (ResourcesName food : ResourcesName.foods) {
            buyPrice.put(food,5);
        }

        for (ResourcesName weapon : ResourcesName.weapons) {
            buyPrice.put(weapon,20);
        }

        for (ResourcesName material : ResourcesName.Materials) {
            buyPrice.put(material,10);
        }

        for (ResourcesName food : ResourcesName.foods) {
            sellPrice.put(food,3);
        }

        for (ResourcesName weapon : ResourcesName.weapons) {
            sellPrice.put(weapon,15);
        }

        for (ResourcesName material : ResourcesName.Materials) {
            sellPrice.put(material,5);
        }
    }

    public static String showPrice(){
        String output = "foods";
        for (ResourcesName food : ResourcesName.foods) {
            output += "\n" + food.name() + ": buy = " + buyPrice.get(food) + "  sell =" + sellPrice.get(food);
        }
        output += "\nmaterials:" ;

        for (ResourcesName material : ResourcesName.Materials) {
            output += "\n" + material.name() + ": buy = " + buyPrice.get(material) + "  sell =" + sellPrice.get(material);
        }

        output += "\nweapons:";
        for (ResourcesName weapon : ResourcesName.weapons) {
            output += "\n" + weapon.name() + ": buy = " + buyPrice.get(weapon) + "  sell =" + sellPrice.get(weapon);
        }

        return output;
    }

    public static boolean checkBuy(String name,int amount,Government government){

        if(!ResourcesName.isValidName(name))
            return false;

        int price = buyPrice.get(ResourcesName.getResourceByName(name)) * amount;

        if(amount > government.getResource().getGold())
            return false;

        government.getResource().addGold(-amount);

        return true;
    }

    public static void main(String[] args) {
        System.out.println(showPrice());
    }



}
