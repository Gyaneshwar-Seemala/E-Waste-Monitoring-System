import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

class Device {
    String name;
    String model;
    Date purchaseDate;
    Date recyclingDate;
    int lifespan;
    boolean recycled;
    ArrayList<String> features;

    Device(String name, String model, Date purchaseDate, int lifespan, ArrayList<String> features) {
        this.name = name;
        this.model = model;
        this.purchaseDate = purchaseDate;
        this.recyclingDate = null;
        this.lifespan = lifespan;
        this.recycled = false;
        this.features = new ArrayList<>(features);
    }

    boolean isDueForRecycling() {
        if (recycled) return false;  // Avoid recycling if already marked as recycled
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(purchaseDate);
        calendar.add(Calendar.MONTH, lifespan);
        Date endDate = calendar.getTime();
        return new Date().after(endDate);
    }

    void recycle() {
        recycled = true;
        recyclingDate = new Date();
        System.out.println("\n** " + name + " (" + model + ") has been marked for recycling. **\n");
    }

    void addFeature(String feature) {
        features.add(feature);
        System.out.println("\n** Added feature '" + feature + "' to " + name + " (" + model + ") **\n");
    }

    void listFeatures() {
        System.out.println("\n** Features of " + name + " (" + model + "): " + features + " **\n");
    }

    String toFileString() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String purchaseDateStr = sdf.format(purchaseDate);
        String recyclingDateStr = (recyclingDate != null) ? sdf.format(recyclingDate) : "null";
        return name + "," + model + "," + lifespan + "," + purchaseDateStr + "," + String.join(";", features) + "," + recycled + "," + recyclingDateStr;
    }
}

public class Main {
    List<Device> devices = new ArrayList<>();
    static final String FILE_PATH = "devices.txt";

    void addDevice(String name, String model, Date purchaseDate, int lifespan, ArrayList<String> features) {
        Device existingDevice = null;
        for (Device device : devices) {
            if (device.name.equals(name) && device.model.equals(model)) {
                existingDevice = device;
                break;
            }
        }
        if (existingDevice != null) {
            if (features.size() > existingDevice.features.size()) {
                devices.remove(existingDevice);
                devices.add(new Device(name, model, purchaseDate, lifespan, features));
                System.out.println("\n** Replaced device with more features. **\n");
            } else {
                System.out.println("\n** Kept existing device with more or equal features. **\n");
            }
        } else {
            devices.add(new Device(name, model, purchaseDate, lifespan, features));
        }

        saveDevicesToFile();
    }

    void checkForRecycling() {
        System.out.println("**********************************************************************");
        int count=0;
        for (Device device : devices) {
            if (device.isDueForRecycling()) {
                System.out.println("\n** Device " + device.name + " (" + device.model + ") is due for recycling. **\n");
                device.recycle();
                count++;
            }
        }
        if(count==0) System.out.println("All devices are in perfect Condition");
        System.out.println("**********************************************************************");
        saveDevicesToFile();
    }

    void updateDeviceFeatures(String name, String model, String newFeature) {
        for (Device device : devices) {
            if (device.name.equals(name) && device.model.equals(model)) {
                device.addFeature(newFeature);
                saveDevicesToFile();
                return;
            }
        }
        System.out.println("Device not found.");
    }

    void listDeviceFeatures(String name, String model) {
        for (Device device : devices) {
            if (device.name.equals(name) && device.model.equals(model)) {
                device.listFeatures();
                return;
            }
        }
        System.out.println("Device not found.");
    }

    void schedulePickup() {
        System.out.println("**********************************************************************");
        for (Device device : devices) {
            if (device.recycled && device.recyclingDate != null) {
                System.out.println("\n** Scheduling pickup for: " + device.name + " (" + device.model + ") **\n");
                device.recyclingDate = null;  // Pickup scheduled, reset the recycling date to prevent further scheduling
            }
        }
        System.out.println("**********************************************************************");
        saveDevicesToFile();
    }

    void saveDevicesToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (Device device : devices) {
                writer.write(device.toFileString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving devices to file.");
        }
    }

    void loadDevicesFromFile() {
        devices.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                String name = parts[0];
                String model = parts[1];
                int lifespan = Integer.parseInt(parts[2]);
                Date purchaseDate = new SimpleDateFormat("yyyy-MM-dd").parse(parts[3]);
                ArrayList<String> features = new ArrayList<>(Arrays.asList(parts[4].split(";")));
                boolean recycled = Boolean.parseBoolean(parts[5]);
                Date recyclingDate = parts[6].equals("null") ? null : new SimpleDateFormat("yyyy-MM-dd").parse(parts[6]);

                Device device = new Device(name, model, purchaseDate, lifespan, features);
                device.recycled = recycled;
                device.recyclingDate = recyclingDate;
                devices.add(device);
            }
        } catch (IOException | ParseException e) {
            System.out.println("Error loading devices from file.");
        }
    }

    void startMonitoring() {
        loadDevicesFromFile();

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\nOptions:");
            System.out.println("1. Add a new device");
            System.out.println("2. Check devices for recycling");
            System.out.println("3. Add new feature to device");
            System.out.println("4. List device features");
            System.out.println("5. Schedule pickup for recycled devices");
            System.out.println("6. Exit\n\n");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Enter device name: ");
                    String name = scanner.nextLine();
                    System.out.print("Enter model: ");
                    String model = scanner.nextLine();
                    System.out.print("Enter lifespan (in months): ");
                    int lifespan = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Enter purchase date (YYYY-MM-DD): ");
                    String dateStr = scanner.nextLine();
                    Date purchaseDate = new GregorianCalendar(
                            Integer.parseInt(dateStr.split("-")[0]),
                            Integer.parseInt(dateStr.split("-")[1]) - 1,
                            Integer.parseInt(dateStr.split("-")[2])
                    ).getTime();

                    ArrayList<String> features = new ArrayList<>();
                    System.out.println("Enter features (type 'done' when finished): ");
                    while (true) {
                        String feature = scanner.nextLine();
                        if (feature.equalsIgnoreCase("done")) break;
                        features.add(feature);
                    }

                    addDevice(name, model, purchaseDate, lifespan, features);
                    break;

                case 2:
                    checkForRecycling();
                    break;

                case 3:
                    System.out.print("Enter device name to update: ");
                    String deviceName = scanner.nextLine();
                    System.out.print("Enter device model to update: ");
                    String deviceModel = scanner.nextLine();
                    System.out.print("Enter new feature: ");
                    String newFeature = scanner.nextLine();
                    updateDeviceFeatures(deviceName, deviceModel, newFeature);
                    break;

                case 4:
                    System.out.print("Enter device name to list features: ");
                    String listName = scanner.nextLine();
                    System.out.print("Enter device model to list features: ");
                    String listModel = scanner.nextLine();
                    listDeviceFeatures(listName, listModel);
                    break;

                case 5:
                    schedulePickup();
                    break;

                case 6:
                    System.out.println("Exiting the system...");
                    saveDevicesToFile();
                    scanner.close();
                    return;

                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    public static void main(String[] args) {
        Main system = new Main();
        system.startMonitoring();
    }
}
