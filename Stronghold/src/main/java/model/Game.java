package model;

import model.environment.buildings.Building;
import model.map.Map;
import model.siegeutil.SiegeUtil;
import model.society.Government;
import model.society.Trade;
import model.units.Person;

import java.util.ArrayList;

public class Game
{
    Map map;
    int turn;
    private final ArrayList<Government> governments =new ArrayList<>();

    private final ArrayList<Person> allUnits = new ArrayList<>();


    private final static ArrayList<Trade> allTrades = new ArrayList<>();

    private final  ArrayList<Building> allBuildings =new ArrayList<>();

    private final ArrayList<SiegeUtil> allSiegeUtil = new ArrayList<>();

    public Map getMap() {
        return map;
    }

    public ArrayList<Person> getAllUnits() {
        return allUnits;
    }

    public ArrayList<Building> getAllBuildings() {
        return allBuildings;
    }

    public ArrayList<SiegeUtil> getAllSiegeUtil() {
        return allSiegeUtil;
    }

    public void addUnit(Person unit){
        allUnits.add(unit);
    }

    public void addBuilding(Building building){
        allBuildings.add(building);
    }

    public void addSiegeUtil(SiegeUtil siegeUtil){
        allSiegeUtil.add(siegeUtil);
    }

    public void removeUnit(Person unit){
        allUnits.remove(unit);
    }

    public void removeBuilding(Building building){
        allBuildings.remove(building);
    }

    public void removeSiegeUtil(SiegeUtil siegeUtil){
        allSiegeUtil.remove(siegeUtil);
    }



    private void runGame()
    {

    }

}
