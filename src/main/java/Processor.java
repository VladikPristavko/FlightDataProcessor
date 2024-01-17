public interface Processor {
    String getMinFlightDurationBetweenTownsByCarriers(String origin, String destination);
    double getAveragePriceBetweenTowns(String origin, String destination);
    double getMedianPriceBetweenTowns(String origin, String destination);
}
