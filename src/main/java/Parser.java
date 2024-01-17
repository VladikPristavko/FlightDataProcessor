import java.util.List;

public interface Parser {
    List<Tickets> parse(String path);
}
