package model.map;

import model.environment.Environment;
import model.environment.Rock;
import model.environment.buildings.Building;
import model.environment.buildings.enums.BuildingName;
import model.society.Government;
import model.units.Person;
import model.units.enums.UnitName;

import java.util.ArrayList;
import java.util.Objects;
import java.lang.Math;

public class Block {
    private final int x;
    private final int y;
    private Texture texture;
    private Map map;

    private Environment environment;
    private ArrayList<Person> units;

    protected Block(int x, int y, Texture texture, Map map) {
        this.x = x;
        this.y = y;
        this.texture = texture;
        this.map = map;
    }

    {
        units = new ArrayList<Person>();
    }

    public Map getMap() {
        return map;
    }

    public ArrayList<Person> getUnits() {
        return units;
    }

    public void addUnit(Person person) {
        units.add(person);
    }

    public void removeUnit(Person person) {
        for (Person unit : units) {
            if (unit.equals(person))
            {
                units.remove(person);
                break;
            }
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }


    public Environment getEnvironment() {
        return environment;
    }

    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    private boolean haveLadder(Person person){
        int x,y;
        ArrayList<Person> units;
        for (Direction direction : Direction.values()) {
            x = this.x + direction.deltaX;
            y = this.y + direction.deltaY;
            if (getMap().isValidXY(x,y)) {
                units = getMap().getBlockByXY(x, y).getUnits();
                for (Person unit : units) {
                    if((unit.getName().equals(UnitName.LADDERMAN) && person.canClimbLadder())
                    || unit.getName().equals(UnitName.SIEGE_TOWER))
                        return true;
                }
            }
        }

        return false;
    }

    public boolean canPassThisBlock(Person person) {
        if(this.environment == null)
            return this.texture.canPass();
        else {
            if(environment instanceof Building){
                return (this.haveLadder(person) || ((Building) environment).canPassBuilding(person));
            }else {
                return false;
            }
        }
    }

    public Person selectUnit(Government government, int selectCount) {
        ArrayList<Person> thisGovernmentUnits = new ArrayList<>();
        for (Person unit : units) {
            if (unit.getGovernment().equals(government))
                thisGovernmentUnits.add(unit);
        }
        return thisGovernmentUnits.get(selectCount % (thisGovernmentUnits.size() - 1));
    }

    public boolean doesGovernmentHaveUnit(Government government) {
        for (Person unit : units) {
            if (unit.getGovernment().equals(government))
                return true;
        }
        return false;
    }

    public boolean canBuildOnThis(BuildingName buildingName) {
        if (this.environment != null)
            return false;

        if (!this.texture.canPass() || texture.equals(Texture.WATER))
            return false;

        switch (buildingName){
            case IRON_MINE:
                if(!texture.equals(Texture.IRON))
                    return false;
                break;

            case PITCH_RIG:
                if(!texture.equals(Texture.PITCH))
                    return false;
                break;

            case QUARRY:
                if (!texture.equals(Texture.ROCK))
                    return false;
                break;

            case WHEAT_FARMER:
                if (texture.equals(Texture.SAND))
                    return false;
                break;

            case HOPS_FARMER:
                return !texture.equals(Texture.SAND);

            default:
                break;

        }



        return true;
    }
    public Block findClosestBlock(int range, Block block) {
        int xCorner = this.x - range, yCorner = this.y - range;
        Block closestBlock = null;
        double distance = Double.MAX_VALUE;
        for (int y = yCorner; y < 2 * range + 1 + yCorner; y++) {
            for (int x = xCorner; x < 2 * range + 1 + xCorner; x++) {
                if (map.isValidXY(x, y)) {
                    Block blockToCheck = getMap().getBlockByXY(x, y);
                    if (distance > block.distanceTo(blockToCheck)){
                        closestBlock = blockToCheck;
                        distance = this.distanceTo(blockToCheck);
                    }
                }
            }
        }
        return closestBlock;
    }

    public double distanceTo(Block block) {
        int delX = this.x - block.getX();
        int delY = this.y - block.getY();
        return Math.sqrt(Math.pow(delX, 2) + Math.pow(delY, 2));
    }
    public String showDetails() {
        String output  = "" + this.texture;
        if(environment != null) {
            if (environment instanceof Building) {
                output += "\nBuilding: " + ((Building) environment).getName();
            } else if (environment instanceof Rock){
                output += "\nRock";
            } else {
                output += "\nTree";
            }

            if(units.size() != 0) {
                output += "\nUnits:";
                for (Person unit : units) {
                    output += "\n" + unit.getName();
                }
            }
        }
        return output;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Block block = (Block) o;
        return x == block.x && y == block.y && Objects.equals(map, block.map);
    }

    @Override
    public String toString() {
        return " {" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
