package game.model;

import java.util.HashMap;
import java.util.Map;

public class NPC {
    private String name;
    private String greeting;
    private Map<String, String> trades; // item to give → item to receive

    public NPC(String name, String greeting) {
        this.name = name;
        this.greeting = greeting;
        this.trades = new HashMap<>();
    }

    public void addTrade(String give, String receive) {
        trades.put(give.toLowerCase(), receive.toLowerCase());
    }

    public String getGreeting() { return greeting; }
    public String getName() { return name; }
    public Map<String, String> getTrades() { return trades; }

    public String getTradeList() {
        if (trades.isEmpty()) return name + " has nothing to trade.";
        StringBuilder sb = new StringBuilder();
        sb.append("\n🧙 ").append(name).append(" offers:\n");
        trades.forEach((give, receive) ->
                sb.append("  Give: ").append(give)
                        .append(" → Receive: ").append(receive).append("\n"));
        return sb.toString();
    }

    public String interact() {
        return "\n🧙 " + name + " says: \"" + greeting + "\"\n"
                + getTradeList();
    }
}