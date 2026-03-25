package com.atlassian_router;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RouterTest {

  private Router router;

  @BeforeEach
  void setup() {
    this.router = new Router();

  }

  @Test
  void TestSimplePath() {

    router.addRoute("/abc/c", "ABC path");
    router.addRoute("/", "");

    Assertions.assertEquals("ABC path", router.callRoute("/abc/c"));
    Assertions.assertNotEquals("ABC path", router.callRoute("/abc/c/d"));
    Assertions.assertEquals("", router.callRoute("/"));
  }

  @Test
  void Test_Any_WildCard() {
    router.addRoute("/abc/*/b", "Skip any");
    router.addRoute("/abc/*", "Skip any at the end");
    router.addRoute("/def/*/b", "Skip exactly one");
    router.addRoute("/*/end", "Anything end");

    Assertions.assertEquals("Anything end", router.callRoute("/kjl/edf/dgf/end"));
    Assertions.assertEquals("Skip exactly one", router.callRoute("/def/e/b"));
    Assertions.assertEquals("Skip any", router.callRoute("/abc/d/e/b"));
    Assertions.assertEquals("Skip any at the end", router.callRoute("/abc/edf/bef/cde/def/abc"));

  }


  @Test
  void Test_With_Path_Params() {
    router.addRoute("/abc/:id/d/:id", "2 Id");
    router.addRoute("/abc/:id/b", "1 Id");
    router.addRoute("/abc/:id", "Id in end");

    Assertions.assertEquals("2 Id", router.callRoute("/abc/d/d/b"));
    Assertions.assertEquals("1 Id", router.callRoute("/abc/edf/b"));
    Assertions.assertEquals("Id in end", router.callRoute("/abc/edf"));

  }

  @Test
  void Complex_MatchTest() {
    router.addRoute("/abc/:id/d/:id/*/b/*/end", "Complex Route");

    Assertions.assertEquals("Complex Route", router.callRoute("/abc/edf/d/gh/xcs/b/ews/sdd/asa/end"));

  }


}
