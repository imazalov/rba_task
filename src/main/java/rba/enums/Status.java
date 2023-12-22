package rba.enums;

public enum Status {
  AKTIVAN(1),
  NEAKTIVAN(0);

  private final int value;

  Status(int value) {
    this.value = value;
  }

  public int getValue() {
    return value;
  }

  public static Status fromValue(int value) {
    for (Status status : Status.values()) {
      if (status.getValue() == value) {
        return status;
      }
    }
    throw new IllegalArgumentException("Neispravna vrijednost za Status: " + value);
  }
}
