# E-Waste Monitoring System

## Overview

The **E-Waste Monitoring System** is a Java-based application that helps organizations efficiently manage electronic devices. It tracks device usage, manages their lifecycle, and ensures proper scheduling for recycling once they reach the end of their lifespan. The system also ensures that, when multiple devices with the same name and model exist, the device with the most features is retained. Device information is persisted using a text file, so data is retained across sessions.

## Features

- **Add Devices**: Add new electronic devices, specifying the name, model, purchase date, lifespan, and a list of features.
- **Feature Management**: Update devices with new features while keeping only the most feature-rich version when duplicates are present.
- **Recycling Management**: Automatically checks devices that are due for recycling based on their lifespan.
- **Data Persistence**: Device data is stored in a text file (`devices.txt`) and is loaded upon restarting the application.
- **Schedule Pickups**: Schedule pickups for devices that have been marked for recycling.
- **View Device Features**: List all features of a particular device by name and model.

## Prerequisites

- Java Development Kit (JDK) 8 or above
- Text editor or Integrated Development Environment (IDE) for Java
- Basic knowledge of Java I/O, OOP principles, and file handling

## Installation

1. Clone or download the repository to your local machine.
2. Open the project in your preferred IDE.
3. Ensure the Java Development Kit (JDK) is properly configured.
4. Create a text file named `devices.txt` in the root directory (if not created automatically).

## How to Use

1. Run the `Main.java` file to start the application.
2. The system will present you with options to:
   - Add a new device
   - Check for devices due for recycling
   - Add new features to existing devices
   - List features of a device
   - Schedule a recycling pickup
   - Exit the system

### Example Interaction

- **Adding a Device**: The system will prompt you to enter details such as the device name, model, purchase date (in `YYYY-MM-DD` format), lifespan (in months), and a list of features.
- **Checking for Recycling**: The system will automatically check if any devices are due for recycling based on their purchase date and lifespan.
- **Feature Management**: If you attempt to add a duplicate device with the same name and model, the system will retain the one with the most features.
- **File Persistence**: The device list is saved to `devices.txt` when any changes are made, and it is automatically loaded when the application starts.

## Code Structure

### 1. **Device Class (`Device.java`)**

This class represents an individual electronic device. It includes the following attributes and methods:

#### Attributes:
- `String name`: Name of the device.
- `String model`: Model of the device.
- `Date purchaseDate`: Date when the device was purchased.
- `int lifespan`: Lifespan of the device in months.
- `boolean recycled`: Indicates whether the device has been marked for recycling.
- `ArrayList<String> features`: List of features associated with the device.

#### Methods:
- `Device(String name, String model, Date purchaseDate, int lifespan, ArrayList<String> features)`: Constructor for initializing a new device.
- `boolean isDueForRecycling()`: Determines if the device is due for recycling based on its lifespan.
- `void recycle()`: Marks the device as recycled.
- `void addFeature(String feature)`: Adds a new feature to the device.
- `void listFeatures()`: Displays the features of the device.
- `String toFileString()`: Converts the device's attributes into a string for file storage.

### 2. **Main Class (`Main.java`)**

This class manages the entire e-waste monitoring system and interacts with the user via the command line interface.

#### Attributes:
- `List<Device> devices`: A list of all devices being monitored.
- `static final String FILE_PATH = "devices.txt"`: Path to the text file where device data is stored.

#### Methods:
- `void addDevice(String name, String model, Date purchaseDate, int lifespan, ArrayList<String> features)`: Adds a new device or updates an existing one if duplicates exist.
- `void checkForRecycling()`: Checks for devices due for recycling and marks them as recycled if necessary.
- `void updateDeviceFeatures(String name, String model, String newFeature)`: Adds a new feature to a device.
- `void listDeviceFeatures(String name, String model)`: Lists all features of a specified device.
- `void schedulePickup()`: Schedules a pickup for all devices marked for recycling.
- `void saveDevicesToFile()`: Saves the current device list to `devices.txt`.
- `void loadDevicesFromFile()`: Loads device data from the `devices.txt` file at the start of the program.
- `void startMonitoring()`: Main loop that presents options to the user and handles user interaction.

## Sample Code

```java
public class Main {
    public static void main(String[] args) {
        Main system = new Main();
        system.startMonitoring();
    }
}
```

This starts the e-waste monitoring system, loading any existing devices from `devices.txt` and interacting with the user via the command line.

## File Format

The device data is stored in `devices.txt` in the following format:

```
deviceName,deviceModel,lifespan,purchaseDate,feature1;feature2;feature3,recycled
```

Example:

```
Smartphone,Samsung Galaxy S10,24,2022-01-01,Camera;Bluetooth;WiFi,false
Laptop,Dell Inspiron,36,2021-05-20,Touchscreen;WiFi;USB-C,false
```

## License

This project is licensed under the MIT License. See the `LICENSE` file for details.

## Contribution

Feel free to fork the repository and submit pull requests. For major changes, please open an issue first to discuss your ideas.

