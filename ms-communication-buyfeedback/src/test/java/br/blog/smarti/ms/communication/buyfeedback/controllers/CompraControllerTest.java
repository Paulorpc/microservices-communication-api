package br.blog.smarti.ms.communication.buyfeedback.controllers;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.junit.Assume.*;

import java.util.Calendar;
import java.util.Date;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

/***
 * Por default não são realizados os testes na ordem de escrita na classe
 * caso seja necessário rodar em uma ordem específica, o que não é o recomendado,
 * basta utilizar a annotation na claase: @FixMethodOrder(MethodSorters.NAME_ASCENDING)
 */
public class CompraControllerTest {

  // Variável com escopo para cada método. Reinicializada.
  private int i;

  // Variável com escopo de classe. Não Reinicializada.
  private static int j;

  @Before
  public void setup() {
    System.out.println("Before | " + ++i + " | " + ++j);
  }

  @After
  public void tearDown() {
    System.out.println("After");
  }

  @BeforeClass
  public static void setupClass() {
    System.out.println("BeforeClass");
  }

  @AfterClass
  public static void tearDownClass() {
    System.out.println("AfterClass");
  }

  @Test
  public void teste1() {
    assertTrue(1 == 1);
    assertThat(1, is(1));
  }

  @Test
  @Ignore // ignora execução do teste
  public void teste2() {
    assertTrue(2 == 2);
    assertThat(1, is(1));
  }

  @Test
  public void teste3() {

    /***
     * Funciona como o ignore, mas dinâmico. Neste caso, não deve ser executado o
     * teste caso o dia da semana seja terça-feira.
     */
    Date hj = new Date();
    assumeFalse(hj.getDay() + 1 == 1);
    System.out.println(hj.getDay() + 1 + " " + Calendar.TUESDAY);

    assertTrue(2 == 2);
    assertThat(1, is(1));
  }
}
