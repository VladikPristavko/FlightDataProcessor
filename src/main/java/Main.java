import org.apache.log4j.PropertyConfigurator;

public class Main {
    public static void main(String[] args) {
        PropertyConfigurator.configure("log4j.properties");

        Processor processor = new FlightDataProcessor("data/", new JSONParser());
        String task1 = processor.getMinFlightDurationBetweenTownsByCarriers("VVO", "TLV");
        System.out.println(task1);

        double averagePrice = processor.getAveragePriceBetweenTowns("VVO", "TLV");

        double medianPrice = processor.getMedianPriceBetweenTowns("VVO", "TLV");

        System.out.println("Difference between average and median price: " + (averagePrice - medianPrice));
    }
}
