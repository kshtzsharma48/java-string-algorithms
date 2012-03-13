package algorithms.search;

public class KnuthMorrisPrattTest extends StringSearchTestBase {

  @Override
  protected StringSearch createNewSearchApi() {
    return Searchers.newKnuthMorrisPrattStringSearcher();
  }
}
