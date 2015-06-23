package ch02;


public interface Configurable {

  void readConfiguration(Json json);
  void writeConfiguration(Json json);
}
