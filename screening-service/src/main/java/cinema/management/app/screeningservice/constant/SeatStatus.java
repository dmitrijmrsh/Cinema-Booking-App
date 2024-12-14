package cinema.management.app.screeningservice.constant;

public enum SeatStatus {
    AVAILABLE,
    TAKEN;

    public String getSeatStatusInStr() {
        return name();
    }

    public static SeatStatus of(final String seatStatusInStr) {
        return switch (seatStatusInStr) {
            case "AVAILABLE" -> AVAILABLE;
            case "TAKEN" -> TAKEN;
            default -> throw new IllegalStateException("Unsupported seat status: " + seatStatusInStr);
        };
    }
}
