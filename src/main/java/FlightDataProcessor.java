import java.time.Duration;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.stream.Collectors.*;

public class FlightDataProcessor implements Processor{
    private final List<Tickets> ticketsList;
    public FlightDataProcessor(String path, Parser parser) {
        this.ticketsList = parser.parse(path);
    }

    @Override
    public String getMinFlightDurationBetweenTownsByCarriers(String origin, String destination) {
        Map<String, Optional<Ticket>> map = ticketsList.stream()
                .flatMap(tickets -> tickets.getTickets().stream())
                .filter(ticket -> ticket.getOrigin().equals(origin))
                .filter(ticket -> ticket.getDestination().equals(destination))
                .collect(groupingBy(Ticket::getCarrier,
                        reducing((t1, t2) -> {
                            Duration d1 = Duration.between(t1.getArrival_time(),t1.getDeparture_time());
                            Duration d2 = Duration.between(t2.getArrival_time(),t2.getDeparture_time());
                            return d1.compareTo(d2) < 0 ? t2 : t1;
                        }
                )));
        StringBuilder result = new StringBuilder();
        map.keySet().forEach(key -> {
            Ticket ticket = map.get(key).orElseThrow();
            result.append(key)
                    .append(" - ")
                    .append(LocalTime.ofSecondOfDay(
                            ticket.getArrival_time().toSecondOfDay()
                                    - ticket.getDeparture_time().toSecondOfDay())
                    ).append(System.lineSeparator());
        });
        return result.toString();
    }

    @Override
    public double getAveragePriceBetweenTowns(String origin, String destination) {
        return ticketsList.stream()
                .flatMap(tickets -> tickets.getTickets().stream())
                .filter(ticket -> ticket.getOrigin().equals(origin))
                .filter(ticket -> ticket.getDestination().equals(destination))
                .mapToDouble(Ticket::getPrice).average().orElseThrow();
    }

    @Override
    public double getMedianPriceBetweenTowns(String origin, String destination) {
        double[] prices = ticketsList.stream()
                .flatMap(tickets -> tickets.getTickets().stream())
                .filter(ticket -> ticket.getOrigin().equals(origin))
                .filter(ticket -> ticket.getDestination().equals(destination))
                .mapToDouble(Ticket::getPrice)
                .sorted()
                .toArray();
        if (prices.length == 0) return 0.0;
        int medianPosition = prices.length / 2;
        return prices.length % 2 == 0 ?
                (prices[medianPosition - 1] + prices[medianPosition]) / 2 :
                prices[medianPosition];
    }


}
