package rba.entity;

import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import rba.enums.Status;
import rba.enums.StatusConverter;


@Entity
@Table(name = "person")
public class Person {
  @Id
  private String oib;
  private String name;
  private String surname;

  @Convert(converter = StatusConverter.class)
  private Status status;

  public String getOib() {
    return oib;
  }

  public void setOib(String oib) {
    this.oib = oib;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getSurname() {
    return surname;
  }

  public void setSurname(String surname) {
    this.surname = surname;
  }

  public Status getStatus() {
    return status;
  }

  public void setStatus(Status status) {
    this.status = status;
  }

  @Override
  public String toString() {
    return
        "oib=" + oib + '\n' +
        "ime=" + name + '\n' +
        "prezime=" + surname + '\n' +
        "status=" + status;
  }
}

