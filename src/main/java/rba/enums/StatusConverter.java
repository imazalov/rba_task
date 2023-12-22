package rba.enums;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;


@Converter(autoApply = true)
public class StatusConverter implements AttributeConverter<Status, Integer> {

  @Override
  public Integer convertToDatabaseColumn(Status status) {
    if (status == null) {
      return null;
    }
    return status.getValue();
  }

  @Override
  public Status convertToEntityAttribute(Integer value) {
    if (value == null) {
      return null;
    }
    return Status.fromValue(value);
  }
}
