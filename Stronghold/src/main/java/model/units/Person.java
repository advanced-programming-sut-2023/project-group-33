package model.units;

import model.map.Block;
import model.map.Direction;
import model.map.Map;
import model.resourecs.Armour;
import model.resourecs.ResourcesName;
import model.resourecs.Weapon;
import model.society.Government;
import model.units.enums.UnitName;
import utility.DataManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

public abstract class Person {
    protected int hp;
    protected int speed;
    protected int defencePower;

    protected UnitName name;

    protected final HashMap<ResourcesName, Integer> price;
    protected Block block;
    protected Government government;
    protected Queue<Block> moveQueue;

    protected boolean canClimbLadder;
    protected boolean canDigMoat;
    protected static final ArrayList<Person> allUnits = new ArrayList<>();

    static {
        ArrayList<String[]> resourceCsv = DataManager.getArrayListFromCSV(DataManager.UNITS_PATH);
        String[] attributeNames = resourceCsv.get(0);

        start:
        for (int i = 1; i < resourceCsv.size(); i++) {

            String[] attributes = resourceCsv.get(i);
            String kind = "";
            int hp = 0, speed = 0, defencePower = 0, damage = 0, attackRange = 0;
            UnitName name = null;
            boolean canClimbLadder = false, canDigMoat = false;
            HashMap<ResourcesName, Integer> price = new HashMap<>();
            Weapon weaponToAdd = null;
            ArrayList<Armour> armours = new ArrayList<>();

            for (int j = 0; j < attributeNames.length; j++) {
                switch (attributeNames[j]) {
                    case "Kind": {
                        kind = attributes[j];
                        break;
                    }
                    case "Name": {
                        name = UnitName.getUnitByName(attributes[j]);
                        break;
                    }
                    case "Hp": {
                        hp = Integer.parseInt(attributes[j]);
                        break;
                    }
                    case "Speed": {
                        speed = Integer.parseInt(attributes[j]);
                        break;
                    }
                    case "Defence Power": {
                        defencePower = Integer.parseInt(attributes[j]);
                        break;
                    }
                    case "Armour": {
                        if (!attributes[j].equals("null")) {
                            String[] armourNames = attributes[j].split("~");

                            for (String armourName : armourNames) {
                                armours.add(new Armour(armourName));
                            }
                        }
                        break;
                    }
                    case "Weapon": {
                        if (!attributes[j].equals("null"))
                            weaponToAdd = new Weapon(attributes[j]);
                        break;
                    }
                    case "Price kind": {
                        String[] priceKinds = attributes[j].split("~");
                        String[] priceCounts = attributes[j + 1].split("~");

                        for (int i1 = 0; i1 < priceKinds.length && i1 < priceCounts.length; i1++) {
                            price.put(
                                    ResourcesName.getResourceByName(priceKinds[i1]),
                                    Integer.parseInt(priceCounts[i1])
                            );
                        }
                        break;
                    }
                    case "Climb Ladder": {
                        switch (attributes[j]) {
                            case "Yes": {
                                canClimbLadder = true;
                                break;
                            }
                            case "No": {
                                canClimbLadder = false;
                                break;
                            }
                        }
                    }
                    case "Dig Moat": {
                        switch (attributes[j]) {
                            case "Yes": {
                                canDigMoat = true;
                                break;
                            }
                            case "No": {
                                canDigMoat = false;
                                break;
                            }
                        }
                    }
                }
            }

            if (weaponToAdd != null) {
                damage = weaponToAdd.getDamage();
                attackRange = weaponToAdd.getAttackRange();
            }

            for (Armour armour : armours) {
                defencePower += armour.getDefenceBoost();
                speed += armour.getSpeedBoost();
            }

            switch (kind) {
                case "Soldier": {
                    new Soldier(hp, speed, defencePower, damage, attackRange, name, canClimbLadder, canDigMoat, price);
                    break;
                }
                case "Worker": {
                    new WorkerUnit(hp, speed, defencePower, name, price, canClimbLadder, canDigMoat);
                    break;
                }
                default:
                    throw new RuntimeException();
            }

        }
    }


    protected Person(int hp, int speed, int defencePower,
                     UnitName name,
                     HashMap<ResourcesName, Integer> price,
                     boolean canClimbLadder, boolean canDigMoat) {
        this.hp = hp;
        this.speed = speed;
        this.defencePower = defencePower;
        this.name = name;
        this.price = price;
        this.canClimbLadder = canClimbLadder;
        this.canDigMoat = canDigMoat;

        allUnits.add(this);
    }

    public Person(String name) {
        Person personToClone = getPersonByName(name);

        this.hp = personToClone.hp;
        this.speed = personToClone.speed;
        this.defencePower = personToClone.defencePower;
        this.name = personToClone.name;
        this.price = personToClone.price;
        this.canClimbLadder = personToClone.canClimbLadder;
        this.canDigMoat = personToClone.canDigMoat;
    }

    public Person(UnitName name) {
        Person personToClone = getPersonByUnitName(name);

        this.hp = personToClone.hp;
        this.speed = personToClone.speed;
        this.defencePower = personToClone.defencePower;
        this.name = personToClone.name;
        this.price = personToClone.price;
        this.canClimbLadder = personToClone.canClimbLadder;
        this.canDigMoat = personToClone.canDigMoat;
    }

    protected Person getPersonByName(String name) {
        for (Person unit : allUnits) {
            if (unit.getName().equals(UnitName.getUnitByName(name)))
                return unit;
        }
        return null;
    }

    protected Person getPersonByUnitName(UnitName name) {
        for (Person unit : allUnits) {
            if (unit.getName().equals(name))
                return unit;
        }
        return null;
    }

    public int getHp() {
        return hp;
    }

    public int getSpeed() {
        return speed;
    }

    public int getDefencePower() {
        return defencePower;
    }

    public UnitName getName() {
        return name;
    }

    public Block getBlock() {
        return block;
    }

    public void takeDamage(int damage) {

    }

    public void findPath(Block destination) {
        HashMap<Block, Block> route = BFS(destination);
        if (route == null)
            return;
        addRouteToQueue(route, destination);
    }

    public void move() {
        if (!moveQueue.isEmpty()) {
            Block lastBlock = null;
            int blocksMoved = 0;
            while (!moveQueue.isEmpty() && blocksMoved < speed) {
                lastBlock = moveQueue.peek();
                moveQueue.remove();
                blocksMoved += 1;
            }

            this.block.removeUnit(this);
            lastBlock.addUnit(this);
            this.block = lastBlock;
        }
    }

    private HashMap<Block, Block> BFS(Block destination) {
        Map map = block.getMap();

        Queue<Block> queue = new LinkedList<>();
        queue.add(block);


        boolean[][] visited = new boolean[map.getHeight()][map.getWidth()];
        HashMap<Block, Block> route = new HashMap<>();


        while (!queue.isEmpty()) {
            Block currentBlock = queue.poll();
            int x = currentBlock.getX();
            int y = currentBlock.getY();

            for (int i = 0; i < 3; i++) {
                for (Direction dir : Direction.values()) {
                    int nextX = x + dir.deltaX, nextY = y + dir.deltaY;
                    if (map.isValidXY(nextX, nextY) && !visited[nextY][nextX]) {
                        Block nextBlock = map.getBlockByXY(nextX, nextY);

                        if (nextBlock.canPassThisBlock(this)) {

                            visited[nextX][nextY] = true;
                            queue.add(nextBlock);

                            route.put(currentBlock, nextBlock);

                            if (nextBlock.equals(destination)) {
                                queue.clear();
                                return route;
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    private void addRouteToQueue(HashMap<Block, Block> route, Block destination) {
        Block blockIter = block;
        while (!blockIter.equals(destination)) {
            moveQueue.add(blockIter);
            blockIter = route.get(blockIter);
        }
    }

    private void die() {

    }
}
