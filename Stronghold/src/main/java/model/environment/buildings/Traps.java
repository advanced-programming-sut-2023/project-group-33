package model.environment.buildings;

import model.environment.buildings.enums.BuildingCategory;
import model.environment.buildings.enums.BuildingName;
import model.map.Block;
import model.resource.ResourcesName;
import model.society.Government;
import model.units.Person;

import java.util.HashMap;

public class Traps extends Building{

    protected Traps(int hp,
                    BuildingCategory category,
                    BuildingName name,
                    HashMap<ResourcesName, Integer> price) {
        super(hp, category, name, price);
    }

    public Traps(BuildingName name, Government government, Block block) {
        super(name, government, block);
    }

    @Override
    public boolean canPassBuilding(Person person){
        return true;
    }
}
