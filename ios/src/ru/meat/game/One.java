package ru.meat.game;

public abstract class One {
  public static void main(String[] args) {

    try {

      throw new UnsupportedOperationException();

    } catch(Throwable t) {

      System.out.print("1");

    } catch(Exception e) {

      System.out.print("2");

    } catch(UnsupportedOperationException uoe) {

      System.out.print("3");

    }

  }
}

