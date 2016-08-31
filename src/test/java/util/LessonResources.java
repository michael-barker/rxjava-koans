package util;

import rx.Observable;

import java.util.ArrayList;
import java.util.List;

public class LessonResources {

  public static String _____;
  public static int ____;
  public static Object ______ = "";
  public static Boolean _________;

  @SuppressWarnings("unused")
  public static class ElevatorPassenger {
    private String name;

    public int getWeightInPounds() {
      return weightInPounds;
    }

    public String getName() {
      return name;
    }

    public int weightInPounds;

    public ElevatorPassenger(String name, int weightInPounds) {
      this.name = name;
      this.weightInPounds = weightInPounds;
    }

    public String toString() {
      return "ElevatorPassenger{" +
          "name='" + name + '\'' +
          ", weightInPounds=" + weightInPounds +
          '}';
    }
  }

  public static class ComcastNetworkAdapter {
    private int attempts;

    public List<String> getData() {
      if (attempts < 42) {
        attempts++;
        System.out.println("network issues!! please reboot your computer!");
        return null;
      }
      ArrayList<String> data = new ArrayList<>();
      data.add("extremely important data");
      System.out.println("transmitting data!");
      return data;
    }
  }

  public static class Elevator {
    public static final int MAX_CAPACITY_POUNDS = 500;
    List<ElevatorPassenger> passengers = new ArrayList<>();

    public void addPassenger(ElevatorPassenger passenger) {
      passengers.add(passenger);
    }

    public int getTotalWeightInPounds() {
      return Observable.from(passengers).reduce(0, (accumulatedWeight, elevatorPassenger) ->
          elevatorPassenger.weightInPounds + accumulatedWeight)
          .toBlocking().last();
    }

    public List<ElevatorPassenger> getPassengers() {
      return passengers;
    }

    public int getPassengerCount() {
      return passengers.size();
    }

    @Override
    public String toString() {
      return "Elevator{" +
          "passengers=" + passengers + "\n" +
          "totalWeight=" + getTotalWeightInPounds() +
          '}';
    }

    public void unload() {
      passengers = new ArrayList<>();
    }
  }

  //A Carnival Food Object...
  public static class CarnivalFood {
    private String name;
    public Double price;

    public CarnivalFood(String name, Double price) {
      this.name = name;
      this.price = price;
    }

    @Override
    public String toString() {
      return "Food{" +
          "name='" + name + '\'' +
          ", price=" + price +
          "\n}";
    }
  }

}
