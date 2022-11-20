package io.c6.justmoveit;

import static javax.swing.SwingUtilities.invokeLater;

/**
 * @author Chandrasekhar Thotakura
 */
public final class Main {

  public static void main(final String... tcs) {
    final var app = new MainView();
    invokeLater(app::open);
  }
}
