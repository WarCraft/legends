package gg.warcraft.legends.item;

import gg.warcraft.legends.combat.Specialization;
import gg.warcraft.legends.player.LegendsPlayer;
import gg.warcraft.monolith.event.EventService;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class ItemService {
    private static final ItemService instance = new ItemService();
    private static final Random random = new Random(System.currentTimeMillis());
    private static final List<Equipment> berserkerEquipments;
    private static final List<Equipment> armsmasterEquipments;
    private static final List<Equipment> hunterEquipments;
    private static final List<Equipment> marksmanEquipments;
    private static final List<Equipment> wizardEquipments;
    private static final List<Equipment> tempestEquipments;
    private static final List<Specialization> validSpecs;

    private static boolean isArmorPiece(Equipment equipment) {
        return equipment.name().startsWith("HEAD") ||
                equipment.name().startsWith("CHEST") ||
                equipment.name().startsWith("LEGS") ||
                equipment.name().startsWith("FEET");
    }

    static {
        berserkerEquipments = Arrays.stream(Equipment.values())
                .filter(equipment -> isArmorPiece(equipment) ||
                        equipment.name().startsWith("AXE") ||
                        equipment.name().startsWith("OFFHAND_SKULL"))
                .collect(Collectors.toList());
        Collections.shuffle(berserkerEquipments);

        armsmasterEquipments = Arrays.stream(Equipment.values())
                .filter(equipment -> isArmorPiece(equipment) ||
                        equipment.name().startsWith("SWORD") ||
                        equipment.name().startsWith("OFFHAND_SHIELD"))
                .collect(Collectors.toList());
        Collections.shuffle(armsmasterEquipments);

        hunterEquipments = Arrays.stream(Equipment.values())
                .filter(equipment -> isArmorPiece(equipment) ||
                        equipment.name().startsWith("BOW"))
                .collect(Collectors.toList());
        Collections.shuffle(hunterEquipments);

        marksmanEquipments = Arrays.stream(Equipment.values())
                .filter(equipment -> isArmorPiece(equipment) ||
                        equipment.name().startsWith("BOW"))
                .collect(Collectors.toList());
        Collections.shuffle(marksmanEquipments);

        wizardEquipments = Arrays.stream(Equipment.values())
                .filter(equipment -> isArmorPiece(equipment) ||
                        equipment.name().startsWith("WAND") ||
                        equipment.name().startsWith("OFFHAND_BOOK"))
                .collect(Collectors.toList());
        Collections.shuffle(wizardEquipments);

        tempestEquipments = Arrays.stream(Equipment.values())
                .filter(equipment -> isArmorPiece(equipment) ||
                        equipment.name().startsWith("STAFF"))
                .collect(Collectors.toList());
        Collections.shuffle(tempestEquipments);

        validSpecs = Arrays.asList(Specialization.ARMSMASTER, Specialization.BERSERKER,
                Specialization.HUNTER, Specialization.MARKSMAN, Specialization.WIZARD, Specialization.TEMPEST);
    }

    public static ItemService getInstance() {
        return instance;
    }

    private ItemService() {
        EventService.getInstance().registerForEvents(new NonCustomItemDropPreventor());
        EventService.getInstance().registerForEvents(new CraftingLoreListener());
    }

    private Equipment getRandomEquipmentFrom(List<Equipment> equipments) {
        int numberOfTypes = equipments.size();
        int randomType = random.nextInt(numberOfTypes);
        return equipments.get(randomType);
    }

    public ItemStack generateEquipment(LegendsPlayer player, boolean cheat) {
        Specialization specialization;
        if (random.nextDouble() > 0.1) {
            specialization = player.getSpecialization();
        } else {
            int numberOfSpecs = validSpecs.size();
            int randomSpec = random.nextInt(numberOfSpecs);
            specialization = validSpecs.get(randomSpec);
        }

        Equipment equipment;
        switch (specialization) {
            case BERSERKER:
                equipment = getRandomEquipmentFrom(berserkerEquipments);
                break;
            case ARMSMASTER:
                equipment = getRandomEquipmentFrom(armsmasterEquipments);
                break;
            case HUNTER:
                equipment = getRandomEquipmentFrom(hunterEquipments);
                break;
            case MARKSMAN:
                equipment = getRandomEquipmentFrom(marksmanEquipments);
                break;
            case WIZARD:
                equipment = getRandomEquipmentFrom(wizardEquipments);
                break;
            case TEMPEST:
                equipment = getRandomEquipmentFrom(tempestEquipments);
                break;
            default:
                System.out.println("Attempted to generate item for unsupported spec " + specialization);
                return null;
        }

        try {
            return equipment.generateRandom(player, specialization.getPrimaryAttribute(), cheat);
        } catch (Exception ex) {
            System.out.println("Failed to drop item for equipment " + equipment + " and player " + player);
            return new ItemStack(Material.AIR);
        }
    }
}
